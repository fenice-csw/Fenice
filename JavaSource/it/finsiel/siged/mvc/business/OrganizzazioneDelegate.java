package it.finsiel.siged.mvc.business;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.task.jobs.DomandeErsuJob;
import it.compit.fenice.task.jobs.FetchEmailUfficioJob;
import it.compit.fenice.task.jobs.GestioneArchiviJob;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.EmailConstants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.integration.OrganizzazioneDAO;
import it.finsiel.siged.mvc.plugin.PersistencePlugIn;
import it.finsiel.siged.mvc.vo.organizzazione.AmministrazioneVO;
import it.finsiel.siged.mvc.vo.organizzazione.MailConfigVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.task.jobs.CodaInvioEmailJob;
import it.finsiel.siged.task.jobs.FetchEmailJob;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

public class OrganizzazioneDelegate {

	private static Logger logger = Logger
			.getLogger(OrganizzazioneDelegate.class.getName());

	private OrganizzazioneDAO organizzazioneDAO = null;

	private ServletConfig config = null;

	private static OrganizzazioneDelegate delegate = null;

	private OrganizzazioneDelegate() {
		// Connect to DAO
		try {
			if (organizzazioneDAO == null) {
				config = PersistencePlugIn.servletConfig;
				organizzazioneDAO = (OrganizzazioneDAO) DAOFactory
						.getDAO(Constants.ORGANIZZAZIONE_DAO_CLASS);

				logger.debug("OrganizzazioneDAO instantiated:"
						+ Constants.ORGANIZZAZIONE_DAO_CLASS);
			}
		} catch (Exception e) {
			logger.error(
					"Exception while connecting to OrganizzazioneDAOjdbc!!", e);
		}

	}

	public static OrganizzazioneDelegate getInstance() {
		if (delegate == null)
			delegate = new OrganizzazioneDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.ORGANIZZAZIONE_DELEGATE;
	}

	public void loadOrganizzazione() {

		Organizzazione org = null;
		RegistroDelegate registroDelegate = null;
		try {
			org = Organizzazione.getInstance();
			org.resetOrganizzazione();
			org.setValueObject(organizzazioneDAO.getAmministrazione());
			org.setCaricaResponsabile(organizzazioneDAO.getCaricaResponsabile());
			org.setCaricaResponsabileUfficioProtocollo(organizzazioneDAO
					.getCaricaResponsabileUfficioProtocollo());
			Collection<AreaOrganizzativa> areeOrganizzative = AreaOrganizzativaDelegate
					.getInstance().getAreeOrganizzative().values();
			for (AreaOrganizzativa i : areeOrganizzative) {
				// aoo = i.next();
				i.setUtenteResponsabileId(AreaOrganizzativaDelegate
						.getInstance().getUtenteResponsabile(
								i.getValueObject().getId()));
				org.addAreaOrganizzativa(i);
				gestisciDirectoryDoc(i, org);
				// INIZIALIZZA EMAIL-LOG
				EmailDelegate.getInstance().initEmailLog(
						"Inizializzazione Email",
						EmailConstants.SUCCESS_EMAIL_INGRESSO,
						i.getValueObject().getId());
			}
			for (Iterator<UfficioVO> i = organizzazioneDAO.getUffici()
					.iterator(); i.hasNext();) {
				UfficioVO uff = (UfficioVO) i.next();
				org.addUfficio(new Ufficio(uff));
			}
			Collection<Ufficio> uffici = org.getUffici();
			for (Iterator<Ufficio> i = uffici.iterator(); i.hasNext();) {
				Ufficio ui = i.next();
				int parentId = ui.getValueObject().getParentId();
				if (parentId == 0) {

					AreaOrganizzativa aoo = org.getAreaOrganizzativa(ui
							.getValueObject().getAooId());
					aoo.setUfficioCentrale(ui);
				} else {
					for (Iterator<Ufficio> j = uffici.iterator(); j.hasNext();) {
						Ufficio uj = j.next();
						int id = uj.getValueObject().getId().intValue();
						if (id == parentId) {
							ui.setUfficioDiAppartenenza(uj);
							break;
						}
					}
				}
			}
			Collection<UtenteVO> utenti = UtenteDelegate.getInstance()
					.getUtenti();
			for (Iterator<UtenteVO> u = utenti.iterator(); u.hasNext();) {
				UtenteVO uteVO = (UtenteVO) u.next();
				Utente ute = new Utente(uteVO);
				org.addUtente(ute);
				ArrayList<Integer> ids = getIdentificativiUffici(uteVO.getId()
						.intValue());
				for (Iterator<Integer> i = ids.iterator(); i.hasNext();) {
					int uffId = i.next().intValue();
					int uteId = ute.getValueObject().getId().intValue();

					boolean attivo = (UfficioDelegate.getInstance()
							.isUfficioAttivo(uffId) && uteVO.isAbilitato());
					CaricaVO car = CaricaDelegate.getInstance()
							.getCaricaByUtenteAndUfficio(uteId, uffId);
					if (car.getReturnValue() != ReturnValues.NOT_FOUND
							&& attivo) {
						ute.addCaricaUfficio(car);
						if (car.isAttivo())
							if (org.getUfficio(uffId).getValueObject()
									.getParentId() > 0) {
								org.getUfficio(uffId).addUtente(ute);
								if (car.isReferente()) {
									org.getUfficio(uffId).addCaricaReferente(
											car);
								}
								if (car.isDirigente())
									org.getUfficio(uffId)
											.getValueObject()
											.setCaricaDirigenteId(
													car.getCaricaId());
							}
					}
				}
			}
			Collection<CaricaVO> cariche = CaricaDelegate.getInstance()
					.getCariche();
			for (Iterator<CaricaVO> u = cariche.iterator(); u.hasNext();) {
				CaricaVO caricaVO = u.next();
				org.addCarica(caricaVO);
			}
			registroDelegate = RegistroDelegate.getInstance();
			for (Iterator<RegistroVO> i = registroDelegate.getRegistri()
					.iterator(); i.hasNext();) {
				org.addRegistro(i.next());
			}

		} catch (DataException de) {
			logger.error("OrganizzazioneDelegate failed.", de);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("OrganizzazioneDelegate failed.", e);
		}
	}

	private void gestisciDirectoryDoc(AreaOrganizzativa aoo, Organizzazione org)
			throws DataException {
		String dirDocProtocollo = org.getValueObject()
				.getPathDocumentiProtocollo()
				+ "/"
				+ "aoo_"
				+ String.valueOf(aoo.getValueObject().getId());
		if (FileUtil.gestionePathDoc(dirDocProtocollo) == ReturnValues.INVALID)
			throw new DataException(
					"Impossibile creare la directory per la aoo"
							+ aoo.getValueObject().getId());
	}

	public ArrayList<Integer> getIdentificativiUffici(int utenteId) {
		ArrayList<Integer> ufficiIds = new ArrayList<Integer>();
		try {
			ufficiIds = organizzazioneDAO.getIdentificativiUffici(utenteId);

		} catch (DataException de) {
			logger.error("getUfficiUtenteIds failed.", de);
		}
		return ufficiIds;
	}

	public ArrayList<Integer> getIdentificativiCariche(int utenteId) {
		ArrayList<Integer> caricheIds = new ArrayList<Integer>();
		try {
			caricheIds = organizzazioneDAO.getIdentificativiCariche(utenteId);
		} catch (DataException de) {
			logger.error("getCaricheUtenteIds failed.", de);
		}
		return caricheIds;
	}

	public AmministrazioneVO getAmministrazione() {
		AmministrazioneVO amministrazioneVO = null;
		try {
			amministrazioneVO = organizzazioneDAO.getAmministrazione();
		} catch (DataException de) {
			logger.error("", de);
		}
		return amministrazioneVO;
	}

	public Collection<AreaOrganizzativa> getAreeOrganizzative()
			throws DataException {
		Collection<AreaOrganizzativa> aoo = new ArrayList<AreaOrganizzativa>();
		try {
			aoo = AreaOrganizzativaDelegate.getInstance()
					.getAreeOrganizzative().values();
			logger.info("aoo trovate:" + aoo.size());
		} catch (DataException de) {
			logger.error("getAreeOrganizzative failed.", de);
			throw new DataException("cannot.load.aoolist");
		}
		return aoo;
	}

	public AmministrazioneVO updateAmministrazione(AmministrazioneVO ammVO) {
		try {
			return organizzazioneDAO.updateAmministrazione(ammVO);
		} catch (DataException de) {
			logger.error("updateAmministrazione failed.", de);
		}
		return null;

	}

	public AmministrazioneVO updateParametriFTP(AmministrazioneVO ammVO) {
		try {

			return organizzazioneDAO.updateParametriFTP(ammVO);
		} catch (DataException de) {
			logger.error("updateAmministrazione failed.", de);
		}
		return null;

	}

	public void visualizzaServiziSchedulati(Scheduler scheduler)
			throws SchedulerException {
		for (String groupName : scheduler.getJobGroupNames()) {
			for (String jobName : scheduler.getJobNames(groupName)) {
				Trigger[] triggers = scheduler.getTriggersOfJob(jobName,groupName);
				Date nextFireTime = triggers[0].getNextFireTime();
				System.out.println("OrganizzazioniDelegate: serviziSchedulati ->" + jobName + " - "+ groupName + " - " + nextFireTime);
				logger.debug("OrganizzazioniDelegate: serviziSchedulati ->" + jobName + " - "+ groupName + " - " + nextFireTime);
			}
		}
	}

	public void caricaServiziSchedulati(ServletContext context) {

		try {
			SchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();
			try {
				logger.debug("shutting down ScheduledServiceDaemon...");
				scheduler.shutdown(true);
				logger.debug("stopped.");
			} catch (SchedulerException e) {
				logger.error("caricaServiziSchedulati:", e);
			}

			scheduler = factory.getScheduler();

			Iterator<AreaOrganizzativa> aree = Organizzazione.getInstance()
					.getAreeOrganizzative().iterator();
			boolean startScheduler = false;
			while (aree.hasNext()) {
				AreaOrganizzativa aoo = (AreaOrganizzativa) aree.next();
				MailConfigVO mailConfigVO = AreaOrganizzativaDelegate
						.getInstance().getMailConfig(
								aoo.getValueObject().getId().intValue());
				// int index=1;

				if (Organizzazione.getInstance().getValueObject().getUnitaAmministrativa() == UnitaAmministrativaEnum.ERSU_PA
					|| Organizzazione.getInstance().getValueObject().getUnitaAmministrativa() == UnitaAmministrativaEnum.ERSU_CT
					|| Organizzazione.getInstance().getValueObject().getUnitaAmministrativa() == UnitaAmministrativaEnum.ERSU_ME) {
					startScheduler = true;
					JobDetail domandeErsu = new JobDetail("domandeErsu_"
							+ aoo.getValueObject().getId().intValue() + ""
							+ UUID.randomUUID().getLeastSignificantBits(),
							Constants.SCHEDULER_NAME, DomandeErsuJob.class);
					domandeErsu.getJobDataMap().put("aoo_id",
							aoo.getValueObject().getId().intValue());
					SimpleTrigger triggerErsu = new SimpleTrigger(
							"ErsuTrigger_"
									+ aoo.getValueObject().getId().intValue()
									+ ""
									+ UUID.randomUUID()
											.getLeastSignificantBits(),
							Constants.SCHEDULER_NAME, new Date());
					scheduler.scheduleJob(domandeErsu, triggerErsu);
				}
				if (aoo.getValueObject().isGaAbilitata()
						&& aoo.getValueObject().isGaFlagInvio()) {
					startScheduler = true;
					Date dataSchedulazione = DateUtil.addHourMinutesToToday(aoo
							.getValueObject().getGaTimer().getTime());
					if (dataSchedulazione.getTime()
							- System.currentTimeMillis() > 0) {
						// data schedulazione ancora ha da venire
						JobDetail gestioneArchivi = new JobDetail(
								"gestioneArchivi_"
										+ aoo.getValueObject().getId()
												.intValue()
										+ ""
										+ UUID.randomUUID()
												.getLeastSignificantBits(),
								Constants.SCHEDULER_NAME,
								GestioneArchiviJob.class);
						gestioneArchivi.getJobDataMap().put("aoo_id",
								aoo.getValueObject().getId().intValue());
						gestioneArchivi.getJobDataMap()
								.put("data_schedulazione",
										DateUtil.addDaysToDataOra(
												dataSchedulazione, 1));
						gestioneArchivi.getJobDataMap().put("real_path",
								context.getRealPath("/"));
						SimpleTrigger triggerGestioneArchivi = new SimpleTrigger(
								"triggerGestioneArchivi_"
										+ aoo.getValueObject().getId()
												.intValue()
										+ ""
										+ UUID.randomUUID()
												.getLeastSignificantBits(),
								Constants.SCHEDULER_NAME, dataSchedulazione);
						scheduler.scheduleJob(gestioneArchivi,
								triggerGestioneArchivi);
					} else {
						// data schedulazione già passata
						JobDetail gestioneArchivi = new JobDetail(
								"gestioneArchivi_"
										+ aoo.getValueObject().getId()
												.intValue()
										+ ""
										+ UUID.randomUUID()
												.getLeastSignificantBits(),
								Constants.SCHEDULER_NAME,
								GestioneArchiviJob.class);
						gestioneArchivi.getJobDataMap().put("aoo_id",
								aoo.getValueObject().getId().intValue());
						gestioneArchivi.getJobDataMap()
								.put("data_schedulazione",
										DateUtil.addDaysToDataOra(
												dataSchedulazione, 1));
						gestioneArchivi.getJobDataMap().put("real_path",
								context.getRealPath("/"));
						SimpleTrigger triggerGestioneArchivi = new SimpleTrigger(
								"triggerGestioneArchivi_"
										+ aoo.getValueObject().getId()
												.intValue()
										+ ""
										+ UUID.randomUUID()
												.getLeastSignificantBits(),
								Constants.SCHEDULER_NAME, new Date());
						scheduler.scheduleJob(gestioneArchivi,
								triggerGestioneArchivi);
					}

				}

				if (mailConfigVO.isPecAbilitata()) {
					System.setProperty("mail.mime.parameters.strict", "false");
					System.setProperty("mail.mime.decodeparameters", "true");
					startScheduler = true;
					String tmpPath = config.getServletContext()
							.getRealPath("/")
							+ config.getServletContext().getInitParameter(
									Constants.TEMP_PEC_PATH)
							+ mailConfigVO.getAooId();
					int intervallo = mailConfigVO.getMailTimer();
					JobDetail protocolloIngresso = new JobDetail("InEmailAoo_"
							+ aoo.getValueObject().getId().intValue() + ""
							+ UUID.randomUUID().getLeastSignificantBits(),
							Constants.SCHEDULER_NAME, FetchEmailJob.class);
					protocolloIngresso.getJobDataMap().put("aoo_id",
							mailConfigVO.getAooId());
					protocolloIngresso.getJobDataMap().put("mail.tempfolder",
							tmpPath);
					protocolloIngresso.getJobDataMap().put("intervallo",
							intervallo);
					SimpleTrigger triggerIngresso = new SimpleTrigger(
							"InTrigger_"
									+ mailConfigVO.getAooId()
									+ ""
									+ UUID.randomUUID()
											.getLeastSignificantBits(),
							Constants.SCHEDULER_NAME, new Date());
					scheduler.scheduleJob(protocolloIngresso, triggerIngresso);

					JobDetail protocolloUscita = new JobDetail("OutEmailAoo_"
							+ aoo.getValueObject().getId().intValue() + ""
							+ UUID.randomUUID().getLeastSignificantBits(),
							Constants.SCHEDULER_NAME, CodaInvioEmailJob.class);
					protocolloUscita.getJobDataMap().put("aoo_id",
							mailConfigVO.getAooId());
					protocolloUscita.getJobDataMap().put("mail.tempfolder",
							tmpPath);
					protocolloUscita.getJobDataMap().put("intervallo",
							intervallo);
					SimpleTrigger triggerUscita = new SimpleTrigger(
							"OutTrigger_"
									+ mailConfigVO.getAooId()
									+ ""
									+ UUID.randomUUID()
											.getLeastSignificantBits(),
							Constants.SCHEDULER_NAME, new Date());
					scheduler.scheduleJob(protocolloUscita, triggerUscita);

				} else if (mailConfigVO.isPnAbilitata()) {
					System.setProperty("mail.mime.parameters.strict", "false");
					System.setProperty("mail.mime.decodeparameters", "true");
					startScheduler = true;
					String tmpPath = config.getServletContext()
							.getRealPath("/")
							+ config.getServletContext().getInitParameter(
									Constants.TEMP_PEC_PATH)
							+ mailConfigVO.getAooId();
					// in minuti
					int intervallo = mailConfigVO.getMailTimer();
					JobDetail protocolliIngressoEmailUfficio = new JobDetail(
							"InEmailUffAoo_"
									+ aoo.getValueObject().getId().intValue()
									+ ""
									+ UUID.randomUUID()
											.getLeastSignificantBits(),
							Constants.SCHEDULER_NAME,
							FetchEmailUfficioJob.class);
					protocolliIngressoEmailUfficio.getJobDataMap().put(
							"aoo_id", mailConfigVO.getAooId());
					protocolliIngressoEmailUfficio.getJobDataMap().put(
							"mail.tempfolder", tmpPath);
					protocolliIngressoEmailUfficio.getJobDataMap().put(
							"intervallo", intervallo);
					SimpleTrigger triggerIngresso = new SimpleTrigger(
							"InEmailUffTrigger_"
									+ mailConfigVO.getAooId()
									+ ""
									+ UUID.randomUUID()
											.getLeastSignificantBits(),
							Constants.SCHEDULER_NAME, new Date());
					scheduler.scheduleJob(protocolliIngressoEmailUfficio,
							triggerIngresso);
				}

			}
			if (startScheduler)
				scheduler.start();
			visualizzaServiziSchedulati(scheduler);
		} catch (SchedulerException e) {
			logger.error("", e);
		}

	}

}
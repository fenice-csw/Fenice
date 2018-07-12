package it.compit.fenice.task.jobs;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.business.DomandaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.protocollo.DomandaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.RegistroDelegate;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.util.FileUtil;
import it.flosslab.mvc.business.OggettarioDelegate;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.StatefulJob;

public class DomandeErsuJob implements Serializable, StatefulJob {

	private static final long serialVersionUID = 1L;

	//in minuti
	private final static int TIMER_ERSU = 45;
	
	private final static int DA_LAVORARE = 1;
	
	static Logger logger = Logger.getLogger(DomandeErsuJob.class.getName());

	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		int aooId = dataMap.getInt("aoo_id");
		DomandaDelegate delegate = DomandaDelegate.getInstance();
		List<DomandaVO> domande = delegate.getDomandeVOByStato(DA_LAVORARE);
		if (domande.size() != 0) {
			Utente admin = Organizzazione.getInstance().getUtente("admin" + aooId);
			Ufficio ufficioRoot=Organizzazione.getInstance().getAreaOrganizzativa(aooId).getUfficioCentrale();
			CaricaVO carica=CaricaDelegate.getInstance().getCaricaByUtenteAndUfficio(admin.getValueObject().getId(), ufficioRoot.getValueObject().getId());	
			for (DomandaVO domanda : domande) {
				if( OggettarioDelegate.getInstance().isOggettoAssegnatariPresent(domanda.getOggetto())){
					ProtocolloIngresso protocollo = getDefaultProtocolloIngresso(admin, ufficioRoot.getValueObject().getId(),carica.getCaricaId());
					aggiorna(domanda, protocollo, admin, ufficioRoot.getValueObject().getId(), carica.getCaricaId());
					ProtocolloVO prot=ProtocolloDelegate.getInstance().registraProtocolloIngressoDaDomandaErsu(protocollo, admin, domanda);
					protocollo.setProtocollo(prot);
					protocollo.getProtocollo().setFlagTipoMittente("M");
					protocollo.getProtocollo().setMittenti(newMittenteErsu(domanda));
					ProtocolloDelegate.getInstance().aggiornaProtocolloIngresso(protocollo, admin);
				}
			}
		}
		JobDetail domandeErsu = new JobDetail("domandeErsu_"+ aooId+""+UUID.randomUUID().getLeastSignificantBits(),Constants.SCHEDULER_NAME, DomandeErsuJob.class);
		domandeErsu.getJobDataMap().put("aoo_id",aooId);
		SimpleTrigger triggerErsu =new SimpleTrigger("ErsuTrigger_"+ aooId+""+UUID.randomUUID().getLeastSignificantBits(), Constants.SCHEDULER_NAME,new Date(System.currentTimeMillis()+(TIMER_ERSU * 60L * 1000L)));
		try {
			context.getScheduler().scheduleJob(domandeErsu, triggerErsu);
		} catch (SchedulerException e) {
			logger.error("DomandeErsuJob:", e);
		}
	}

	private static ProtocolloIngresso getDefaultProtocolloIngresso(
			Utente utente, int ufficioId, int caricaId) {

		ProtocolloVO protocollo = new ProtocolloVO();
		RegistroVO reg = RegistroDelegate.getInstance()
				.getRegistroByCodAndAooId("RegUff",
						utente.getAreaOrganizzativa().getId());
		protocollo.setRegistroId(reg.getId().intValue());
		Timestamp dataReg = new Timestamp(System.currentTimeMillis());
		protocollo.setDataRegistrazione(dataReg);
		protocollo.setAnnoRegistrazione(reg.getAnnoCorrente());
		protocollo.setDataEffettivaRegistrazione(new Date());
		protocollo.setUfficioProtocollatoreId(ufficioId);
		protocollo.setCaricaProtocollatoreId(caricaId);
		protocollo.setRowCreatedUser(utente.getValueObject().getUsername());
		protocollo.setRowUpdatedUser(utente.getValueObject().getUsername());
		protocollo.setRowCreatedTime(new Date(System.currentTimeMillis()));
		protocollo.setRowUpdatedTime(new Date(System.currentTimeMillis()));
		protocollo.setAooId(utente.getValueObject().getAooId());
		protocollo.setStatoProtocollo("S");
		protocollo.setFlagTipo(ProtocolloVO.FLAG_TIPO_PROTOCOLLO_INGRESSO);
		protocollo.setFlagTipoMittente(LookupDelegate.tipiPersona[0].getTipo());
		ProtocolloIngresso pi = new ProtocolloIngresso();
		pi.setProtocollo(protocollo);
		return pi;
	}

	private static void aggiorna(DomandaVO domanda,
			ProtocolloIngresso protocollo, Utente utente, int ufficioId,
			int caricaId) {
		
		uploadFile(domanda, protocollo, utente);
		protocollo.getProtocollo().setDataDocumento(null);
		protocollo.getProtocollo().setDataRicezione(null);
		protocollo.getProtocollo().setOggetto(domanda.getOggetto());
		protocollo.getProtocollo().setNumProtocolloMittente(domanda.getIdDomanda());
		protocollo.getProtocollo().setDataProtocolloMittente(domanda.getDataIscrizione());
		protocollo.getProtocollo().setVersione(0);

		// MITTENTE
		protocollo.getProtocollo().setFlagTipoMittente("M");
		protocollo.getProtocollo().setMittenti(newMittenteErsu(domanda));

		aggiornaAssegnatariModel(domanda, protocollo, utente, ufficioId,
				caricaId);
	}

	private static List<SoggettoVO> newMittenteErsu(DomandaVO domanda) {
		List<SoggettoVO> mittenti = new ArrayList<SoggettoVO>();
		SoggettoVO mittente = null;
		mittente = new SoggettoVO('F');
		mittente.setCognome(domanda.getCognome());
		mittente.setNome(domanda.getNome());
		mittente.getIndirizzo().setToponimo(domanda.getIndirizzo());
		mittente.getIndirizzo().setComune(domanda.getComune());
		mittente.getIndirizzo().setProvinciaId(
				LookupDelegate.getInstance().getProvinciaIdFromCodiProv(
						domanda.getProvincia()));
		mittente.getIndirizzo().setCap(domanda.getCap());
		mittente.setComuneNascita(domanda.getComuneNascita());
		mittente.setDataNascita(domanda.getDataNascita());
		mittente.setCodiceFiscale(domanda.getMatricola());
		mittente.setIndirizzoEMail(domanda.getMail());
		mittenti.add(mittente);
		return mittenti;
	}

	private static void aggiornaAssegnatariModel(DomandaVO domanda,
			ProtocolloIngresso protocollo, Utente utente, int ufficioId,
			int caricaId) {
		protocollo.removeAssegnatari();
		UtenteVO uteVO = utente.getValueObject();
		Map<Integer, AssegnatarioVO> assegnatari = OggettarioDelegate
				.getInstance().getAssegnatari(domanda.getOggetto());
		if (assegnatari != null) {
			for (AssegnatarioVO ass : assegnatari.values()) {
				Date now = new Date();
				ass.setDataAssegnazione(now);
				ass.setDataOperazione(now);
				ass.setCaricaAssegnanteId(caricaId);
				ass.setUfficioAssegnanteId(ufficioId);
				ass.setRowCreatedUser(uteVO.getUsername());
				ass.setRowUpdatedUser(uteVO.getUsername());
				ass.setCompetente(true);
				protocollo.getProtocollo().setStatoProtocollo("S");
				ass.setPresaVisione(false);
				ass.setLavorato(false);
				protocollo.aggiungiAssegnatario(ass);

			}
		}

	}
	
	public static void uploadFile(DomandaVO domanda,ProtocolloIngresso protocollo,Utente utente) {
		String path = Organizzazione.getInstance().getValueObject().getPathDocumentiProtocollo()+ "/" + domanda.getPath();
		File file = new File(path);
		if(file.exists()){
			String fileName = file.getName();
			String contentType = new MimetypesFileTypeMap().getContentType( file );
			Long size = new Long(file.length());
			if (size > 0 && !"".equals(fileName)) {
				String impronta = FileUtil.calcolaDigest(file.getAbsolutePath(),FileConstants.SHA256); 
				DocumentoVO documento = new DocumentoVO();
				documento.setDescrizione(null);
				documento.setFileName(fileName);
				documento.setImpronta(impronta);
				documento.setPath(file.getAbsolutePath());
				documento.setSize(size.intValue());
				documento.setContentType(contentType);
				documento.setRowCreatedTime(new Date(System.currentTimeMillis()));
				documento.setRowUpdatedTime(new Date(System.currentTimeMillis()));
				documento.setRowCreatedUser(utente.getValueObject().getUsername());
				documento.setRowUpdatedUser(utente.getValueObject().getUsername());
				protocollo.setDocumentoPrincipale(documento);
				
				domanda.setPath(path);
			}
		}
	}
	
}

package it.finsiel.siged.mvc.business;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.EmailConstants;
import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.constant.PermessiConst;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.integration.LookupDAO;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.lookup.ParametriLdapVO;
import it.finsiel.siged.mvc.vo.lookup.ProvinciaVO;
import it.finsiel.siged.mvc.vo.lookup.StatoAssegnazioneProtocolloVO;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;
import it.finsiel.siged.mvc.vo.lookup.TipoPersonaVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProtocolloVO;
import it.finsiel.siged.mvc.vo.lookup.TipoUfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;
import it.flosslab.mvc.vo.OggettoVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

public class LookupDelegate implements ComponentStatus {

	private static Logger logger = Logger.getLogger(LookupDelegate.class
			.getName());

	private int status;

	private LookupDAO lookupDAO = null;

	private static LookupDelegate delegate = null;

	private static  Map<Integer,ArrayList<TipoDocumentoVO>> tipiDocumento = new HashMap<Integer,ArrayList<TipoDocumentoVO>>(8);

	private static Map<String,IdentityVO> statiDocumento = new HashMap<String,IdentityVO>(8);

	private static Map<Integer,ArrayList<SpedizioneVO>> mezziSpedizione = new HashMap<Integer,ArrayList<SpedizioneVO>>(8);

	private static Map<String,IdentityVO> tipiPermessiView = new HashMap<String,IdentityVO>(4);

	private static Map<String,IdentityVO> tipiPermessiBusiness = new HashMap<String,IdentityVO>(4);

	private static  Map<Integer,ArrayList<TipoProcedimentoVO>> tipiProcedimento = new HashMap<Integer,ArrayList<TipoProcedimentoVO>>(8);

	private static Map<String,IdentityVO> statiProcedimento =  null;

	private static Map<Integer,IdentityVO> statiFascicolo =  null;

	private static Map<Integer,IdentityVO> condizioniFascicolo =  null;

	private static Map<String,IdentityVO> tipiFinalitaProcedimento =  null;

	//private static Collection<IdentityVO> posizioneProcedimento = new ArrayList<IdentityVO>();

	private static IdentityVO tipoOwner = new IdentityVO(
			String.valueOf(PermessiConst.OWNER), "Owner");

	private static Map<Integer,IdentityVO> tipiFascicolo = new HashMap<Integer,IdentityVO>(2);

	private static Map<String,IdentityVO> posizioniProcedimento = new HashMap<String, IdentityVO>();

	private static Collection<IdentityVO>  destinazioniScarto = null;

	public static TipoPersonaVO[] tipiPersona = new TipoPersonaVO[] {
			new TipoPersonaVO("F", "Persona Fisica"),
			new TipoPersonaVO("G", "Persona Giuridica") };

	static {
		tipiPermessiView.put(String.valueOf(PermessiConst.SOLA_LETTURA),
				new IdentityVO(String.valueOf(PermessiConst.SOLA_LETTURA),
						"Lettura"));
		tipiPermessiView.put(String.valueOf(PermessiConst.LETTURA_STORIA),
				new IdentityVO(String.valueOf(PermessiConst.LETTURA_STORIA),
						"Lettura e Storia"));

		tipiPermessiView.put(String.valueOf(PermessiConst.MODIFICA),
				new IdentityVO(String.valueOf(PermessiConst.MODIFICA),
						"Modifica"));
		tipiPermessiView.put(String.valueOf(PermessiConst.MODIFICA_CANCELLA),
				new IdentityVO(String.valueOf(PermessiConst.MODIFICA_CANCELLA),
						"Modifica e cancella"));
		tipiPermessiBusiness.putAll(tipiPermessiView);
		tipiPermessiBusiness.put(tipoOwner.getCodice(), tipoOwner);
	}

	private LookupDelegate() {
		try {
			if (lookupDAO == null) {
				lookupDAO = (LookupDAO) DAOFactory
						.getDAO(Constants.LOOKUP_DAO_CLASS);
				logger.debug("UserDAO instantiated:"
						+ Constants.LOOKUP_DAO_CLASS);
				status = STATUS_OK;
			}
		} catch (Exception e) {
			logger.error("Exception while connecting to UserDAOjdbc!!", e);
			status = STATUS_ERROR;
		}

	}

	public static LookupDelegate getInstance() {
		if (delegate == null)
			delegate = new LookupDelegate();
		return delegate;
	}

	public static String getIdentifier() {
		return Constants.LOOKUP_DELEGATE;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int s) {
		this.status = s;
	}

	public void caricaTabelle(ServletContext context) {
		try {

			caricaTipiDocumento();

			logger.info("LookupDelegate: getting tipiDoc");

			context.setAttribute("tipiPersona", getTipiPersona());
			logger.info("LookupDelegate: getting tipiPersona");

			context.setAttribute("statiAssegnazioneProtocollo",
					getStatoAssegnazioneProtocollo());
			logger.info("LookupDelegate: getting getStatoAssegnazioneProtocollo");

			context.setAttribute("tipiProtocollo", getTipiProtocollo());
			logger.info("LookupDelegate: getting getTipiProtocollo");

			caricaMezziSpedizione();
			logger.info("LookupDelegate: getting mezziSpedizione");

			context.setAttribute("tipiUfficio", getTipiUfficio());
			logger.info("LookupDelegate: getting getTipiUfficio");

			context.setAttribute("tipiPermessiDocumenti",
					getTipiPermessiDocumentiView());
			logger.info("LookupDelegate: getting getTipiPermessiDocumenti");

		} catch (Exception de) {
			logger.error("LookupDelegate: failed getting caricaTabelle: ");
		}
	}

	public void caricaTipiDocumento() {
		Organizzazione org = Organizzazione.getInstance();
		Collection<AreaOrganizzativa> aoo = org.getAreeOrganizzative();
		try {
			tipiDocumento = lookupDAO.getTipiDocumento(aoo);
		} catch (DataException de) {
			logger.error("LookupDelegate: failed getting getTipiDocumento: ");
		}
	}

	public Collection<TipoDocumentoVO> getTipiDocumento(int aooId) {
		Collection<TipoDocumentoVO> tipi = new ArrayList<TipoDocumentoVO>();
		if (tipiDocumento.get(new Integer(aooId)) != null)
			tipi = tipiDocumento.get(new Integer(aooId));
		return tipi;
	}
	
	public Collection<IdentityVO> getTipiDocumentoMail() {
		ArrayList<IdentityVO> cat = new ArrayList<IdentityVO>();
		cat.add(new IdentityVO("*", ""));
		cat.add(new IdentityVO("FE", "Fattura elettronica"));
		cat.add(new IdentityVO("SE", "Scarto esito committente"));
		cat.add(new IdentityVO("DT", "Notifica di decorrenza termini"));
		return cat;
	}

	public void caricaTipiProcedimento() {
		Organizzazione org = Organizzazione.getInstance();
		Collection<AreaOrganizzativa> aoo = org.getAreeOrganizzative();
		try {
			tipiProcedimento = lookupDAO.getTipiProcedimento(aoo);
		} catch (DataException de) {
			logger.error("LookupDelegate: failed getting getTipiProcedimento: ");
		}
	}

	public Collection<TipoProcedimentoVO> getTipiProcedimento(int aooId) {
		Collection<TipoProcedimentoVO> tipi = new ArrayList<TipoProcedimentoVO>();
		if (tipiProcedimento.get(new Integer(aooId)) != null)
			tipi =tipiProcedimento.get(new Integer(aooId));
		return tipi;
	}

	public void caricaMezziSpedizione() {
		Organizzazione org = Organizzazione.getInstance();
		Collection<AreaOrganizzativa> aoo = org.getAreeOrganizzative();
		try {
			mezziSpedizione = lookupDAO.getMezziSpedizione(aoo);
		} catch (DataException de) {
			logger.error("LookupDelegate: failed getting caricaMezziSpedizione: ");
		}
	}

	public Collection<SpedizioneVO> getMezziSpedizione(int aooId) {
		Collection<SpedizioneVO> mezzi = new ArrayList<SpedizioneVO>();
		if (mezziSpedizione.get(new Integer(aooId)) != null)
			mezzi = mezziSpedizione.get(new Integer(aooId));
		return mezzi;
	}

	public SpedizioneVO getDescMezzoSpedizione(int mezzoSpedizioneId) {
		SpedizioneVO mezzo = new SpedizioneVO();
		try {
			mezzo = lookupDAO.getMezzoSpedizione(mezzoSpedizioneId);
		} catch (Exception e) {

		}

		return mezzo;
	}

	public Collection<ProvinciaVO> getProvince() {

		try {
			return lookupDAO.getProvince();
		} catch (DataException de) {
			logger.error("LookupDelegate: failed getting getProvince: ");
			return null;
		}
	}

	public int getProvinciaIdFromCodiProv(String codiProv) {
		try {
			return lookupDAO.getProvinciaIdFromCodiProv(codiProv);
		} catch (DataException de) {
			logger.error("LookupDelegate: failed getting getProvince: ");
			return 0;
		}
	}

	public Collection<IdentityVO> getTitoliDestinatario() {
		try {
			return lookupDAO.getTitoliDestinatario();
		} catch (DataException de) {
			logger.error("LookupDelegate: failed getting getTitoliDestinatario: ");
			return null;
		}
	}

	public Map<String, OggettoVO> getOggetti(int aooId) {

		try {
			return lookupDAO.getOggetti(aooId);
		} catch (DataException de) {
			logger.error("LookupDelegate: failed getting getOggetti: ");
			return null;
		}
	}

	public Collection<TipoPersonaVO> getTipiPersona() {
		ArrayList<TipoPersonaVO> list = new ArrayList<TipoPersonaVO>(2);
		list.add(tipiPersona[0]);
		list.add(tipiPersona[1]);
		return list;
	}

	public Collection<IdentityVO> getStatiDomanda() {
		ArrayList<IdentityVO> list = new ArrayList<IdentityVO>(3);
		list.add(new IdentityVO("1", "In Attesa"));
		list.add(new IdentityVO("2", "Eliminati"));
		list.add(new IdentityVO("3", "Protocollati"));
		return list;
	}

	private Collection<StatoAssegnazioneProtocolloVO> getStatoAssegnazioneProtocollo() {
		ArrayList<StatoAssegnazioneProtocolloVO> list = new ArrayList<StatoAssegnazioneProtocolloVO>(3);
		list.add(new StatoAssegnazioneProtocolloVO("N", "in Lavorazione"));
		list.add(new StatoAssegnazioneProtocolloVO("A", "agli Atti"));
		list.add(new StatoAssegnazioneProtocolloVO("R", "in Risposta"));
		return list;
	}

	private Collection<TipoUfficioVO> getTipiUfficio() {
		ArrayList<TipoUfficioVO> list = new ArrayList<TipoUfficioVO>(3);
		list.add(new TipoUfficioVO(UfficioVO.UFFICIO_NORMALE,
				UfficioVO.LABEL_UFFICIO_NORMALE));
		list.add(new TipoUfficioVO(UfficioVO.UFFICIO_CENTRALE,
				UfficioVO.LABEL_UFFICIO_CENTRALE));
		list.add(new TipoUfficioVO(UfficioVO.UFFICIO_SEMICENTRALE,
				UfficioVO.LABEL_UFFICIO_SEMICENTRALE));
		return list;
	}

	private Collection<TipoProtocolloVO> getTipiProtocollo() {
		ArrayList<TipoProtocolloVO> list = new ArrayList<TipoProtocolloVO>(4);
		list.add(new TipoProtocolloVO("I", "Ingresso"));
		list.add(new TipoProtocolloVO("U", "Uscita"));
		list.add(new TipoProtocolloVO("P", "Posta Interna"));
		if (Organizzazione.getInstance().getValueObject().getUnitaAmministrativa()==UnitaAmministrativaEnum.POLICLINICO_CT)
			list.add(new TipoProtocolloVO("F", "Fatture"));
		return list;
	}

	public Collection<TipoProtocolloVO> getStatiProtocollo(String tipoProtocollo) {
		ArrayList<TipoProtocolloVO> list = new ArrayList<TipoProtocolloVO>(2);
		list.add(new TipoProtocolloVO("C", "Annullato"));
		list.add(new TipoProtocolloVO("N", "Modificato"));
		return list;
	}
	
	public Collection<IdentityVO> getStatiEmailUscita() {
		ArrayList<IdentityVO> list = new ArrayList<IdentityVO>(2);
		list.add(new IdentityVO("0", "Da Inviare"));
		list.add(new IdentityVO("1", "Inviata"));
		return list;
	}
	
	public Collection<IdentityVO> getStatiListaEmail() {
		ArrayList<IdentityVO> list = new ArrayList<IdentityVO>(2);
		list.add(new IdentityVO(EmailConstants.EMAIL_INGRESSO_NON_PROTOCOLLATA, "Da Protocollare"));
		list.add(new IdentityVO(EmailConstants.EMAIL_INGRESSO_PROTOCOLLATA, "Protocollate"));
		list.add(new IdentityVO(EmailConstants.EMAIL_INGRESSO_ELIMINATA, "Eliminate"));
		return list;
	}

	public Collection<IdentityVO> getCategoriePA() {
		ArrayList<IdentityVO> cat = new ArrayList<IdentityVO>();
		cat.add(new IdentityVO("*", "scelta categoria"));
		cat.add(new IdentityVO("P", "ASL"));
		cat.add(new IdentityVO("E", "Autorita' Amministrative Indipendenti"));
		cat.add(new IdentityVO("R", "Camere di Commercio"));
		cat.add(new IdentityVO("O", "Comuni"));
		cat.add(new IdentityVO("U", "Comunità montane"));
		cat.add(new IdentityVO("F", "Enti a Struttura Associativa"));
		cat.add(new IdentityVO("Y",
				"Enti Autonomi Lirici ed Istituzioni Concertistiche Assimilate"));
		cat.add(new IdentityVO("C",
				"Enti di Regolazione dell'Attivit&agrave; Economica"));
		cat.add(new IdentityVO("Q", "Enti e Aziende Ospedaliere"));
		cat.add(new IdentityVO("I",
				"Enti ed Istituzioni di Ricerca non Strumentale"));
		cat.add(new IdentityVO("K",
				"Enti Nazionali di Previdenza e Assistenza Sociale"));
		cat.add(new IdentityVO("W", "Enti Parco"));
		cat.add(new IdentityVO("X", "Enti per il Diritto allo Studio"));
		cat.add(new IdentityVO("S", "Enti per il Turismo"));
		cat.add(new IdentityVO("T", "Enti Portuali"));
		cat.add(new IdentityVO("G", "Enti Produttori di Servizi Culturali"));
		cat.add(new IdentityVO("D", "Enti Produttori di Servizi Economici"));
		cat.add(new IdentityVO("V", "Enti Regionali di Sviluppo"));
		cat.add(new IdentityVO("J",
				"Enti Regionali per la Ricerca e per l'Ambiente"));
		cat.add(new IdentityVO("L",
				"Istituti e Stazioni Sperimentali per la Ricerca"));
		cat.add(new IdentityVO("A", "Ministeri e Presidenza del Consiglio"));
		cat.add(new IdentityVO("B",
				"Organi Costituzionali e di Rilievo Costituzionale"));
		cat.add(new IdentityVO("N", "Province"));
		cat.add(new IdentityVO("M", "Regioni"));
		cat.add(new IdentityVO("Z", "Università ed Istituti di Istruzione"));
		cat.add(new IdentityVO("H", "Altri Enti"));
		return cat;

	}

	public ParametriLdapVO getIndicePAParams() {
		ParametriLdapVO pa = new ParametriLdapVO();
		pa.setHost("indicepa.gov.it");
		pa.setPorta(389);
		pa.setDn("c=it");
		return pa;
	}

	public Collection<IdentityVO> getTipiPermessiDocumentiView() {
		return tipiPermessiView.values();
	}

	public Map<String,IdentityVO> getTipiPermessiDocumentiBusiness() {
		return tipiPermessiBusiness;
	}

	public IdentityVO getTipoPermessoOwner() {
		return tipoOwner;
	}

	public static Map<String,IdentityVO> getStatiDocumento() {
		statiDocumento.put(Parametri.STATO_CLASSIFICATO, new IdentityVO(
				Parametri.STATO_CLASSIFICATO, "Classificato"));
		statiDocumento.put(Parametri.STATO_INVIATO_PROTOCOLLO, new IdentityVO(
				Parametri.STATO_INVIATO_PROTOCOLLO, "Inviato a Protocollo"));
		statiDocumento.put(Parametri.STATO_LAVORAZIONE, new IdentityVO(
				Parametri.STATO_LAVORAZIONE, "In Lavorazione"));
		statiDocumento.put(Parametri.STATO_PROTOCOLLATO, new IdentityVO(
				Parametri.STATO_PROTOCOLLATO, "Protocollato"));
		return statiDocumento;
	}

	public static Map<String,IdentityVO> getPosizioniProcedimento() {
		/*
		if (posizioniProcedimento == null) {
			posizioniProcedimento = new HashMap(3);
			posizioniProcedimento.put("A", new IdentityVO("A", "Agli Atti"));
			posizioniProcedimento.put("T",new IdentityVO("T", "In Trattazione"));

		}
		*/
		return posizioniProcedimento;
	}

	
	
	public static Map<String,IdentityVO> getPosizioniProcedimentoULL(int stato) {
		posizioniProcedimento = new HashMap<String,IdentityVO>(4);
		//FASE ISTRUTTORIA 3
		//FASE RELATORIA 4
		//FASE PARERE CGA 5
		if (stato == 5) {
			posizioniProcedimento.put("I", new IdentityVO("I", "istruttore"));
			posizioniProcedimento.put("F", new IdentityVO("F", "funzionario"));
		}
		
		return posizioniProcedimento;
	}

	public static Map<Integer,IdentityVO> getTipiFascicolo() {
		tipiFascicolo.put(
				new Integer(Parametri.TIPO_FASCICOLO_ORDINARIO),
				new IdentityVO(String
						.valueOf(Parametri.TIPO_FASCICOLO_ORDINARIO),
						Parametri.LABEL_TIPO_FASCICOLO_ORDINARIO));
		tipiFascicolo.put(
				new Integer(Parametri.TIPO_FASCICOLO_VIRTUALE),
				new IdentityVO(String
						.valueOf(Parametri.TIPO_FASCICOLO_VIRTUALE),
						Parametri.LABEL_TIPO_FASCICOLO_VIRTUALE));
		return tipiFascicolo;
	}

	
	/**
	 * 
	 * @return map of IdentityVO
	 */
	public static Map<String,IdentityVO> getStatiProcedimento() {

		if (statiProcedimento == null) {
			statiProcedimento = new HashMap<String,IdentityVO>(2);
			statiProcedimento.put("0", new IdentityVO(0, "In Trattazione"));
			statiProcedimento.put("1", new IdentityVO(1, "Agli Atti"));
			statiProcedimento.put("2", new IdentityVO(2, "Archiviato"));
			
		}
		return statiProcedimento;
	}

	/**
	 * 
	 * @return map of IdentityVO
	 */
	public static Map<String,IdentityVO> getTipiFinalitaProcedimento() {
		if (tipiFinalitaProcedimento == null) {
			tipiFinalitaProcedimento = new HashMap<String,IdentityVO>(2);
			tipiFinalitaProcedimento.put("1", new IdentityVO(1,
					"Tipo Finalita 1"));
			tipiFinalitaProcedimento.put("2", new IdentityVO(2,
					"Tipo Finalita 2"));
		}
		return tipiFinalitaProcedimento;
	}

	public static Map<Integer,IdentityVO> getStatiFascicolo() {

		if (statiFascicolo == null) {
			statiFascicolo = new HashMap<Integer,IdentityVO>(2);
			statiFascicolo.put(new Integer(Parametri.STATO_FASCICOLO_APERTO),
					new IdentityVO(Parametri.STATO_FASCICOLO_APERTO,
							"in Trattazione"));
			statiFascicolo.put(new Integer(Parametri.STATO_FASCICOLO_CHIUSO),
					new IdentityVO(Parametri.STATO_FASCICOLO_CHIUSO,
							"agli Atti"));
			statiFascicolo.put(new Integer(
					Parametri.STATO_FASCICOLO_IN_DEPOSITO), new IdentityVO(
					Parametri.STATO_FASCICOLO_IN_DEPOSITO, "in Deposito"));
		}
		return statiFascicolo;
	}

	public static Collection<IdentityVO> getStatiFascicolo(int stato) {
		Collection<IdentityVO> statiCollection = new ArrayList<IdentityVO>();
		if (statiFascicolo == null) {
			statiFascicolo = new HashMap<Integer,IdentityVO>(2);
			statiFascicolo.put(new Integer(Parametri.STATO_FASCICOLO_APERTO),
					new IdentityVO(Parametri.STATO_FASCICOLO_APERTO,
							"in Trattazione"));
			statiFascicolo.put(new Integer(Parametri.STATO_FASCICOLO_CHIUSO),
					new IdentityVO(Parametri.STATO_FASCICOLO_CHIUSO,
							"agli Atti"));
			statiFascicolo.put(new Integer(
					Parametri.STATO_FASCICOLO_IN_DEPOSITO), new IdentityVO(
					Parametri.STATO_FASCICOLO_IN_DEPOSITO, "in Deposito"));
		}
		statiCollection.add(statiFascicolo.get(stato));
		return statiCollection;
	}

	public static IdentityVO getStatoFascicolo(int stato) {
		if (statiFascicolo == null) {
			statiFascicolo = new HashMap<Integer,IdentityVO>(2);
			statiFascicolo.put(new Integer(Parametri.STATO_FASCICOLO_APERTO),
					new IdentityVO(Parametri.STATO_FASCICOLO_APERTO,
							"in Trattazione"));
			statiFascicolo.put(new Integer(Parametri.STATO_FASCICOLO_CHIUSO),
					new IdentityVO(Parametri.STATO_FASCICOLO_CHIUSO,
							"agli Atti"));
			statiFascicolo.put(new Integer(
					Parametri.STATO_FASCICOLO_IN_DEPOSITO), new IdentityVO(
					Parametri.STATO_FASCICOLO_IN_DEPOSITO, "in Deposito"));
		}
		return (IdentityVO) statiFascicolo.get(stato);
	}

	public static Map<Integer,IdentityVO> getCondizioniFascicolo() {
		if (condizioniFascicolo == null) {
			condizioniFascicolo = new HashMap<Integer,IdentityVO>(2);
			condizioniFascicolo.put(new Integer(
					Parametri.CONDIZIONE_FASCICOLO_ASSOCIATO), new IdentityVO(
					String.valueOf(Parametri.CONDIZIONE_FASCICOLO_ASSOCIATO),
					Parametri.LABEL_CONDIZIONE_FASCICOLO_ASSOCIATO));
			condizioniFascicolo
					.put(new Integer(
							Parametri.CONDIZIONE_FASCICOLO_NON_ASSOCIATO),
							new IdentityVO(
									String.valueOf(Parametri.CONDIZIONE_FASCICOLO_NON_ASSOCIATO),
									Parametri.LABEL_CONDIZIONE_FASCICOLO_NON_ASSOCIATO));
		}
		return condizioniFascicolo;
	}

	public static Collection<IdentityVO> getDestinazioniScarto() {
		if (destinazioniScarto == null) {
			destinazioniScarto = new ArrayList<IdentityVO> ();
			IdentityVO iVO = new IdentityVO(0,
					Parametri.DESTINAZIONE_SCARTO_ARCHIVIO_STATO);
			destinazioniScarto.add(iVO);

			iVO = new IdentityVO(1, Parametri.DESTINAZIONE_SCARTO_MACERO);
			destinazioniScarto.add(iVO);
		}
		return destinazioniScarto;
	}

}
package it.finsiel.siged.mvc.bo;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.model.protocollo.ProtocolloRegistroEmergenza;
import it.compit.fenice.mvc.presentation.actionform.protocollo.PostaInternaForm;
import it.compit.fenice.mvc.vo.protocollo.DomandaVO;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.MenuDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloUscitaForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.organizzazione.AreaOrganizzativaVO;
import it.finsiel.siged.mvc.vo.organizzazione.MailConfigVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.ConfigurazioneUtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.mvc.vo.registro.RegistroVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;
import it.finsiel.siged.util.StringUtil;
import it.flosslab.mvc.business.OggettarioDelegate;
import it.flosslab.mvc.vo.OggettoVO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProtocolloBO {

	private static Map<String,String> statiProtocollo;
	static {
		statiProtocollo = new HashMap<String,String>(10);
		statiProtocollo.put("IA", "Fascicolato");
		statiProtocollo.put("IV", "Visionato");
		statiProtocollo.put("IL", "Lavorato");
		statiProtocollo.put("IC", "Annullato");
		statiProtocollo.put("IR", "Riassegnato");
		statiProtocollo.put("IF", "Rifiutato");
		statiProtocollo.put("IP", "Associato a Procedimento");
		
		statiProtocollo.put("UN", "non Spedito");
		statiProtocollo.put("US", "Spedito");
		statiProtocollo.put("UV", "Visionato");
		statiProtocollo.put("UC", "Annullato");
		statiProtocollo.put("UP", "Associato a Procedimento");

		statiProtocollo.put("PA", "Fascicolato");
		statiProtocollo.put("PC", "Annullato");
		statiProtocollo.put("PR", "Inoltrato");
		statiProtocollo.put("PF", "Rifiutato");
		statiProtocollo.put("PP", "Associato a Procedimento");
		statiProtocollo.put("PV", "Visionato");

		statiProtocollo.put("RN", "Emergenza");
		statiProtocollo.put("RS", "Emergenza");
	}

	public static String getStatoProtocollo(String tipo, String stato) {
		return (String) statiProtocollo.get(tipo + stato);
	}

	public static void putAllegato(DocumentoVO doc, Map<String,DocumentoVO> documenti) {
		int idx = doc.getIdx();
		if (idx == 0) {
			idx = getNextDocIdx(documenti);
		}
		doc.setIdx(idx);
		documenti.put(String.valueOf(idx), doc);
	}

	public static DocumentoVO getUltimoDocumentoAllegato(Map<String,DocumentoVO> documenti) {
		int idx = getNextDocIdx(documenti) - 1;
		return (DocumentoVO) documenti.get(String.valueOf(idx));
	}

	private static int getNextDocIdx(Map<String,DocumentoVO> allegati) {
		int max = 0;
		Iterator<String> it = allegati.keySet().iterator();
		while (it.hasNext()) {
			String id = (String) it.next();
			int cur = NumberUtil.getInt(id);
			if (cur > max)
				max = cur;
		}
		return max + 1;
	}

	private static String getSignature(ProtocolloVO protocollo) {
		Organizzazione org = Organizzazione.getInstance();
		AreaOrganizzativaVO aoo = org.getAreaOrganizzativa(protocollo.getAooId()).getValueObject();
		MailConfigVO mailConfigVO =Organizzazione.getInstance().getAreaOrganizzativa(protocollo.getAooId()).getMailConfig();
		
		StringBuffer str = new StringBuffer();
		str.append("<Identificatore>");
		str.append("<CodiceAmministrazione>");
		str.append(org.getValueObject().getCodice());
		str.append("</CodiceAmministrazione>");
		str.append("<CodiceAOO>");
		str.append(aoo.getCodice());
		str.append("</CodiceAOO>");
		str.append("<NumeroRegistrazione>");
		str.append(StringUtil.formattaNumeroProtocollo(""
				+ protocollo.getNumProtocollo(), 7));
		str.append("</NumeroRegistrazione>");
		str.append("<DataRegistrazione>");
		str.append(DateUtil.formattaData(protocollo.getDataRegistrazione()
				.getTime()));
		str.append("</DataRegistrazione>");
		str.append("</Identificatore>");
		str.append("<Origine>");
		str.append("<IndirizzoTelematico tipo=\"smtp\">");
		str.append(mailConfigVO.getPecSmtp());
		str.append("</IndirizzoTelematico>");
		str.append("<Mittente>");
		str.append("<Amministrazione>");
		str.append("<Denominazione>");
		str.append(org.getValueObject().getDescription());
		str.append("</Denominazione>");
		str.append("<CodiceAmministrazione>");
		str.append(org.getValueObject().getCodice());
		str.append("</CodiceAmministrazione>");
		str.append("<AOO>");
		str.append("<CodiceAOO>");
		str.append(aoo.getCodice());
		str.append("</CodiceAOO>");
		str.append("<Denominazione>");
		str.append(aoo.getDescription());
		str.append("</Denominazione>");
		str.append("</AOO>");
		str.append("</Amministrazione>");
		str.append("</Mittente>");
		str.append("</Origine>");
		return str.toString();
	}

	public static String getSignature(ProtocolloRegistroEmergenza protocollo) {
		StringBuffer str = new StringBuffer();

		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		str
				.append("<Segnatura xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		str.append(" xsi:noNamespaceSchemaLocation=\"Segnatura.dtd\"");
		str.append(" versione=\"2000-10-18\" lang=\"it\">");
		str.append("<Intestazione>");
		str.append(getSignature(protocollo.getProtocollo()));
		str.append("<Oggetto>");
		str.append(protocollo.getProtocollo().getOggetto());
		str.append("</Oggetto>");
		str.append("</Intestazione>");
		str.append("</Segnatura>");
		return str.toString();
	}

	public static String getSignature(ProtocolloIngresso protocollo) {
		StringBuffer str = new StringBuffer();

		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		str
				.append("<Segnatura xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		str.append(" xsi:noNamespaceSchemaLocation=\"Segnatura.dtd\"");
		str.append(" versione=\"2000-10-18\" lang=\"it\">");
		str.append("<Intestazione>");
		str.append(getSignature(protocollo.getProtocollo()));
		str.append("<Oggetto>");
		str.append(protocollo.getProtocollo().getOggetto());
		str.append("</Oggetto>");
		str.append("</Intestazione>");
		str.append(getSignatureDocumentiProtocollo(protocollo
				.getDocumentoPrincipale(), protocollo.getAllegatiCollection()));
		str.append("</Segnatura>");
		return str.toString();
	}

	public static String getSignature(ProtocolloUscita protocollo) {
		StringBuffer str = new StringBuffer();

		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		str
				.append("<Segnatura xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		str.append(" xsi:noNamespaceSchemaLocation=\"Segnatura.dtd\"");
		str.append(" versione=\"2000-10-18\" lang=\"it\">");
		str.append("<Intestazione>");
		str.append(getSignature(protocollo.getProtocollo()));
		Collection<DestinatarioVO> destinatari = protocollo.getDestinatari();
		if (destinatari != null) {
			Iterator<DestinatarioVO> it = destinatari.iterator();
			while (it.hasNext()) {
				str.append("<Destinazione>");
				DestinatarioVO destinatario =it.next();
				str.append("<IndirizzoTelematico tipo=\"smtp\">");
				str.append(destinatario.getEmail());
				str.append("</IndirizzoTelematico>");
				str.append("<Destinatario>");
				str.append("<Amministrazione>");
				str.append("<Denominazione>");
				str.append(destinatario.getDestinatario());
				str.append("</Denominazione>");
				str.append("<CodiceAmministrazione />");
				str.append("<UnitaOrganizzativa>");
				str.append("<Denominazione />");
				str.append("<Persona />");
				if (destinatario.getIndirizzo() != null) {
					str.append("<IndirizzoPostale>");
					str.append("<Toponimo dug=\"Via\">");
					str.append(destinatario.getIndirizzo());
					str.append("</Toponimo>");
					// str.append("<Civico>");
					str.append("<Civico />");
					// str.append("<CAP>");
					str.append("<CAP />");
					str.append("<Comune>");
					str.append(destinatario.getCitta());
					str.append("</Comune>");
					// str.append("<Provincia>");
					str.append("<Provincia />");
					str.append("</IndirizzoPostale>");
				} else {
					str.append("<IndirizzoPostale />");
				}

				str.append("<IndirizzoTelematico />");
				str.append("<Telefono />");
				str.append("<Fax />");
				str.append("</UnitaOrganizzativa>");
				str.append("</Amministrazione>");
				str.append("</Destinatario>");
				str.append("</Destinazione>");
			}

		} else {
			str.append("<Destinazione />");
		}

		str.append("<Oggetto>");
		str.append(protocollo.getProtocollo().getOggetto());
		str.append("</Oggetto>");
		str.append("</Intestazione>");
		str.append(getSignatureDocumentiProtocollo(protocollo
				.getDocumentoPrincipale(), protocollo.getAllegatiCollection()));
		str.append("</Segnatura>");
		return str.toString();
	}

	private static String getSignatureDocumentiProtocollo(
			DocumentoVO documentoPrincipale, Collection<DocumentoVO> allegati) {
		StringBuffer str = new StringBuffer();
		str.append("<Descrizione>");
		str.append("<TestoDelMessaggio />");

		if ((documentoPrincipale != null && documentoPrincipale.getFileName() != null)
				|| (allegati != null && allegati.size() > 0)) {

			if (documentoPrincipale != null) {
				str.append("<Documento id=\"");
				str.append(documentoPrincipale.getId());
				str.append("\"");
				str.append(" nome=\"");
				str.append(documentoPrincipale.getFileName());
				str.append("\"");
				str.append(" tipoMIME=\"");
				str.append(documentoPrincipale.getContentType());
				str.append("\" ");
				str.append(" tipoRiferimento=\"MIME\">");
				str.append("<Impronta algoritmo=\"SHA-1\">");
				str.append(documentoPrincipale.getImpronta());
				str.append("</Impronta>");
				str.append("</Documento>");
			}
			if (allegati != null) {
				Iterator<DocumentoVO> it = allegati.iterator();
				while (it.hasNext()) {
					DocumentoVO allegato = (DocumentoVO) it.next();
					str.append("<Allegati>");
					str.append("<Documento id=\"");
					str.append(allegato.getId());
					str.append("\"");
					str.append(" nome=\"");
					str.append(allegato.getFileName());
					str.append("\"");
					str.append(" tipoMIME=\"");
					str.append(allegato.getContentType());
					str.append("\" ");
					str.append(" tipoRiferimento=\"MIME\" />");
					str.append("</Allegati>");
				}
			}

		}
		str.append("</Descrizione>");
		return str.toString();
	}

	private static ProtocolloVO getDefaultProtocollo(Utente utente) {
		ProtocolloVO protocollo = new ProtocolloVO();
		RegistroVO reg = utente.getRegistroVOInUso();
		protocollo.setRegistroId(reg.getId().intValue());
		Timestamp dataReg = new Timestamp(System.currentTimeMillis());
		if (reg.getDataBloccata())
			dataReg = new Timestamp(RegistroBO.getDataAperturaRegistro(reg)
					.getTime());
		protocollo.setDataRegistrazione(dataReg);
		protocollo.setAnnoRegistrazione(reg.getAnnoCorrente());
		protocollo.setDataEffettivaRegistrazione(new Date());
		protocollo.setUfficioProtocollatoreId(utente.getUfficioInUso());
		protocollo.setCaricaProtocollatoreId(utente.getCaricaInUso());
		protocollo.setUtenteProtocollatoreId(utente.getValueObject().getId());
		protocollo.setRowCreatedUser(utente.getValueObject().getUsername());
		protocollo.setRowUpdatedUser(utente.getValueObject().getUsername());
		protocollo.setRowCreatedTime(new Date(System.currentTimeMillis()));
		protocollo.setRowUpdatedTime(new Date(System.currentTimeMillis()));
		protocollo.setAooId(utente.getValueObject().getAooId());
		protocollo.setStatoProtocollo("S");
		return protocollo;
	}

	public static ProtocolloIngresso getDefaultProtocolloIngresso(Utente utente) {
		ProtocolloVO protocollo = getDefaultProtocollo(utente);
		protocollo.setFlagTipo(ProtocolloVO.FLAG_TIPO_PROTOCOLLO_INGRESSO);
		protocollo.setFlagTipoMittente(LookupDelegate.tipiPersona[0].getTipo());
		ProtocolloIngresso pi = new ProtocolloIngresso();
		pi.setProtocollo(protocollo);
		return pi;
	}

	public static ProtocolloIngresso getDefaultFatture(Utente utente, int regId) {
		ProtocolloVO protocollo = getDefaultProtocollo(utente);
		protocollo.setDataRegistrazione(new Timestamp(System
				.currentTimeMillis()));
		protocollo.setAnnoRegistrazione(DateUtil.getAnnoCorrente());
		protocollo.setRegistroId(regId);
		protocollo.setFlagTipo(ProtocolloVO.FLAG_TIPO_PROTOCOLLO_INGRESSO);
		protocollo.setFlagTipoMittente(LookupDelegate.tipiPersona[0].getTipo());
		ProtocolloIngresso pi = new ProtocolloIngresso();
		pi.setProtocollo(protocollo);
		return pi;
	}

	public static ProtocolloUscita getDefaultProtocolloUscita(Utente utente) {
		ProtocolloVO protocollo = getDefaultProtocollo(utente);
		protocollo.setFlagTipo(ProtocolloVO.FLAG_TIPO_PROTOCOLLO_USCITA);

		ProtocolloUscita pu = new ProtocolloUscita();
		pu.setProtocollo(protocollo);
		return pu;
	}

	public static Collection<DestinatarioVO> getDestinatariViaEmail(Collection<DestinatarioVO> destinatari) {
		ArrayList<DestinatarioVO> retD = new ArrayList<DestinatarioVO>();
		if (destinatari == null)
			return retD;
		Iterator<DestinatarioVO> destinatariIt = destinatari.iterator();
		while (destinatariIt.hasNext()) {
			DestinatarioVO dest = (DestinatarioVO) destinatariIt.next();
			if (dest.getFlagPEC())
				retD.add(dest);
		}

		return retD;
	}

	public static String getTimbroUscita(Organizzazione org, ProtocolloVO protocollo) {
		String aooDesc = org.getAreaOrganizzativa(protocollo.getAooId())
				.getValueObject().getDescription();
		String ufficioDesc = org.getUfficio(protocollo.getUfficioMittenteId()).getValueObject().getDescription();
		return aooDesc
				+ " - "
				+ ufficioDesc
				+ " - "
				+ DateUtil.formattaData(protocollo.getDataRegistrazione()
						.getTime())
				+ " - "
				+ StringUtil.formattaNumeroProtocollo(String.valueOf(protocollo
						.getNumProtocollo()), 7);
	}
	
	public static String getTimbroIngresso(Organizzazione org, ProtocolloVO protocollo) {
		String aooDesc = org.getAreaOrganizzativa(protocollo.getAooId())
				.getValueObject().getDescription();
		return aooDesc
				+ " - "
				+ DateUtil.formattaData(protocollo.getDataRegistrazione()
						.getTime())
				+ " - "
				+ StringUtil.formattaNumeroProtocollo(String.valueOf(protocollo
						.getNumProtocollo()), 7);
	}
	
	public static String getTimbroDomandeProtocollate() {
		UnitaAmministrativaEnum uni=Organizzazione.getInstance().getValueObject().getUnitaAmministrativa();
		if(uni==UnitaAmministrativaEnum.ERSU_PA)
			return getTimbroErsuPA();
		else if(uni==UnitaAmministrativaEnum.ERSU_CT)
			return getTimbroErsuCT();
		else if(uni==UnitaAmministrativaEnum.ERSU_ME)
			return getTimbroErsuME();
		else return "";
	}
	
	public static String getCorpoMailDomandeProtocollate(ProtocolloVO protocollo,DomandaVO domanda) {
		UnitaAmministrativaEnum uni=Organizzazione.getInstance().getValueObject().getUnitaAmministrativa();
		if(uni==UnitaAmministrativaEnum.ERSU_PA)
			return getCorpoMailErsuPA(protocollo,domanda);
		else if(uni==UnitaAmministrativaEnum.ERSU_CT)
			return getCorpoMailErsuCT(protocollo,domanda);
		else if(uni==UnitaAmministrativaEnum.ERSU_ME)
			return getCorpoMailErsuME(protocollo,domanda);
		else return "";
	}
	
	private static String getTimbroErsuPA() {
		return "ERSU Palermo - Ricevuta Avvenuta  Protocollazione";
	}

	private static String getCorpoMailErsuPA(ProtocolloVO protocollo,DomandaVO domanda) {
		String body="<html lang=\"it\">";
		body+="<body><p style=\"font-size:12pt; text-align:center; font-weight:bold\">ERSU Palermo | Ente Regionale per il diritto allo Studio Universitario</p>" +
				"<p style=\"font-size:11pt; text-align:center; font-weight:bold;\">Ricevuta Avvenuta  Protocollazione</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">Oggetto: "+domanda.getOggetto()+"</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">Data protocollo: "+DateUtil.formattaData(protocollo.getDataRegistrazione().getTime())+"</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">Numero protocollo: "+StringUtil.formattaNumeroProtocollo(String.valueOf(protocollo.getNumProtocollo()), 7)+"</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">Mittente: "+domanda.getNome()+" "+domanda.getCognome()+" - "+" "+domanda.getIndirizzoCompleto()+"</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">ID pratica: "+domanda.getIdDomanda()+"</p>";
		body+="<p style=\"font-size:10pt; text-align:center;  text-decoration:underline; font-weight:bold;\">La presente ricevuta attesta la SOLA consegna della richiesta benefici.<br>Poich&egrave; non &egrave; stata ancora effettuata alcuna verifica dei dati autocertificati e della documentazione caricata, la stessa NON costituisce attestazione della regolarit&agrave; della richiesta benefici inviata.</p>";
		body+="<p style=\"font-size:8pt; text-align:center;\">*******************************************************************************<br/>" +
				"Email generata in automatico, eventuali risposte alla stessa non possono essere gestite. <br/>" +
				"Per informazioni e chiarimenti rivolgersi all'ufficio Concorsi e Benefici dell'ERSU <br/>" +
				"********************************************************************************</p>";
		body+="<p style=\"font-size:8pt; text-align:center;\">ERSU Palermo - Ente Regionale per il Diritto allo Studio Universitario<br/>" +
				"Viale delle Scienze, Edificio 1 Residenza Universitaria Santi Romano, 90128 Palermo<br/>" +
				"Codice Fiscale 80017160823-Partita I.V.A. 02795930821</p></body></html>";
		return body;
	}
	
	private static String getTimbroErsuCT() {
		return "ERSU Catania - Ricevuta Avvenuta  Protocollazione";
	}

	private static String getCorpoMailErsuCT(ProtocolloVO protocollo,DomandaVO domanda) {
		String body="<html lang=\"it\">";
		body+="<body><p style=\"font-size:12pt; text-align:center; font-weight:bold\">ERSU Catania | Ente Regionale per il diritto allo Studio Universitario</p>" +
				"<p style=\"font-size:11pt; text-align:center; font-weight:bold;\">Ricevuta Avvenuta  Protocollazione</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">Oggetto: "+domanda.getOggetto()+"</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">Data protocollo: "+DateUtil.formattaData(protocollo.getDataRegistrazione().getTime())+"</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">Numero protocollo: "+StringUtil.formattaNumeroProtocollo(String.valueOf(protocollo.getNumProtocollo()), 7)+"</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">Mittente: "+domanda.getNome()+" "+domanda.getCognome()+" - "+" "+domanda.getIndirizzoCompleto()+"</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">ID pratica: "+domanda.getIdDomanda()+"</p>";
		body+="<p style=\"font-size:10pt; text-align:center;  text-decoration:underline; font-weight:bold;\">La presente ricevuta attesta la SOLA consegna della richiesta benefici.<br>Poich&egrave; non &egrave; stata ancora effettuata alcuna verifica dei dati autocertificati e della documentazione caricata, la stessa NON costituisce attestazione della regolarit&agrave; della richiesta benefici inviata.</p>";
		body+="<p style=\"font-size:8pt; text-align:center;\">*******************************************************************************<br/>" +
				"Email generata in automatico, eventuali risposte alla stessa non possono essere gestite. <br/>" +
				"Per informazioni e chiarimenti rivolgersi all'ufficio Concorsi e Benefici dell'ERSU <br/>" +
				"********************************************************************************</p>";
		body+="<p style=\"font-size:8pt; text-align:center;\">ERSU Catania - Ente Regionale per il Diritto allo Studio Universitario<br/>" +
				"Via Etnea, 570 - 95128 Catania - Tel. 095/7517910 - Cod. Fisc. 80006770871</p></body></html>";
		return body;
	}
	
	private static String getTimbroErsuME() {
		return "ERSU Messina - Ricevuta Avvenuta  Protocollazione";
	}

	private static String getCorpoMailErsuME(ProtocolloVO protocollo,DomandaVO domanda) {
		String body="<html lang=\"it\">";
		body+="<body><p style=\"font-size:12pt; text-align:center; font-weight:bold\">ERSU Messina | Ente Regionale per il diritto allo Studio Universitario</p>" +
				"<p style=\"font-size:11pt; text-align:center; font-weight:bold;\">Ricevuta Avvenuta  Protocollazione</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">Oggetto: "+domanda.getOggetto()+"</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">Data protocollo: "+DateUtil.formattaData(protocollo.getDataRegistrazione().getTime())+"</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">Numero protocollo: "+StringUtil.formattaNumeroProtocollo(String.valueOf(protocollo.getNumProtocollo()), 7)+"</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">Mittente: "+domanda.getNome()+" "+domanda.getCognome()+" - "+" "+domanda.getIndirizzoCompleto()+"</p>";
		body+="<p style=\"font-size:12pt; text-align:justify;\">ID pratica: "+domanda.getIdDomanda()+"</p>";
		body+="<p style=\"font-size:10pt; text-align:center;  text-decoration:underline; font-weight:bold;\">La presente ricevuta attesta la SOLA consegna della richiesta benefici.<br>Poich&egrave; non &egrave; stata ancora effettuata alcuna verifica dei dati autocertificati e della documentazione caricata, la stessa NON costituisce attestazione della regolarit&agrave; della richiesta benefici inviata.</p>";
		body+="<p style=\"font-size:8pt; text-align:center;\">*******************************************************************************<br/>" +
				"Email generata in automatico, eventuali risposte alla stessa non possono essere gestite. <br/>" +
				"Per informazioni e chiarimenti rivolgersi all'ufficio Concorsi e Benefici dell'ERSU <br/>" +
				"********************************************************************************</p>";
		body+="<p style=\"font-size:8pt; text-align:center;\">ERSU Messina - Ente Regionale per il Diritto allo Studio Universitario<br/>" +
				"Via Ghibellina, 146 - 98123 Messina - Tel. 090/3718650 - Cod. Fisc. 80004290831</p></body></html>";
		return body;
	}
	
	public static String getTimbroDocPrincipale(Organizzazione org,
			ProtocolloVO pro) {
		String aooDesc = org.getAreaOrganizzativa(pro.getAooId())
				.getValueObject().getDescription();
		return aooDesc + " - Protocollo Nr." + pro.getNumProtocollo() + " - "
				+ DateUtil.formattaData(pro.getDataRegistrazione().getTime());
	}

	public static List<Integer> notificaAssegnatarioCompetenza(
			Collection<AssegnatarioVO> assegnatari) {
		List<Integer> res = new ArrayList<Integer>();
		if (assegnatari == null)
			return res;
		for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i.hasNext();) {
			AssegnatarioVO assegnatario = (AssegnatarioVO) i.next();
			if (assegnatario.isCompetente()
					&& assegnatario.getUtenteAssegnatarioId() > 0) {
				res.add(assegnatario.getUtenteAssegnatarioId());
			}
		}
		return res;
	}

	public static ProtocolloIngressoForm getProtocolloIngressoConfigurazioneUtente(
			ProtocolloIngressoForm pForm,
			ConfigurazioneUtenteVO configurazioneVO, int utenteId) {
		pForm.inizializzaFormToCopyProtocollo();
		Organizzazione organizzazione = Organizzazione.getInstance();

		long dataCorrente = System.currentTimeMillis();
		if (configurazioneVO.getReturnValue() == ReturnValues.FOUND) {
			if (configurazioneVO.getCheckAssegnatari().equals("1")) {
				if (configurazioneVO.getAssegnatarioUfficioId() > 0) {
					pForm.removeAssegnatari();
					UfficioVO ufficioVO = (organizzazione
							.getUfficio(configurazioneVO
									.getAssegnatarioUfficioId()))
							.getValueObject();

					AssegnatarioView ass = new AssegnatarioView();

					ass.setUfficioId(ufficioVO.getId().intValue());
					ass.setNomeUfficio(ufficioVO.getDescription());
					ass.setDescrizioneUfficio(ufficioVO.getName());
					pForm.aggiungiAssegnatario(ass);
					pForm.setAssegnatarioCompetente(ass.getKey());
					if (pForm.isDipTitolarioUfficio()) {
						pForm.setTitolario(null);
					}

				}

			}
			if (configurazioneVO.getCheckDataDocumento().equals("1")) {
				if ("0".equals(configurazioneVO.getDataDocumento())) {
					pForm.setDataDocumento(null);
				} else if ("1".equals(configurazioneVO.getDataDocumento())) {
					pForm.setDataDocumento(DateUtil.formattaData(dataCorrente));
				}

			}
			if (configurazioneVO.getCheckRicevutoIl().equals("1")) {
				if ("0".equals(configurazioneVO.getDataRicezione())) {
					pForm.setDataRicezione(null);
				} else if ("1".equals(configurazioneVO.getDataRicezione())) {
					pForm.setDataRicezione(DateUtil.formattaData(dataCorrente));
				}

			}
			if (configurazioneVO.getCheckOggetto().equals("1")) {
				if (configurazioneVO.getOggetto() != null
						&& !"".equals(configurazioneVO.getOggetto())) {
					pForm.setOggetto(configurazioneVO.getOggetto());
				} else {
					pForm.setOggetto(null);
				}

			}
			SoggettoVO soggettoVO;
			if (configurazioneVO.getCheckTipoMittente().equals("1")) {
				soggettoVO = new SoggettoVO(configurazioneVO.getTipoMittente());
			} else if (pForm.getMittente() != null) {
				soggettoVO = new SoggettoVO(pForm.getMittente().getTipo());
			} else {
				soggettoVO = new SoggettoVO("F");
			}

			if (configurazioneVO.getCheckMittente().equals("1")) {
				if ("F".equals(configurazioneVO.getTipoMittente()))
					soggettoVO.setCognome(configurazioneVO.getMittente());
				else if ("G".equals(configurazioneVO.getTipoMittente()))
					soggettoVO.setDescrizioneDitta(configurazioneVO
							.getMittente());

			}
			pForm.setMittente(soggettoVO);
			if (configurazioneVO.getCheckTipoDocumento().equals("1")) {
				pForm.setTipoDocumentoId(configurazioneVO.getTipoDocumentoId());
			}

			if (configurazioneVO.getCheckTitolario().equals("1")) {
				pForm.setTitolarioSelezionatoId(configurazioneVO
						.getTitolarioId());
				Utente utente = organizzazione.getUtente(utenteId);
				TitolarioBO.impostaTitolario(pForm, utente.getUfficioInUso(),
						configurazioneVO.getTitolario());
			} else {
				pForm.setTitolario(null);
			}

		}
		return pForm;
	}

	public static ProtocolloUscitaForm getProtocolloUscitaConfigurazioneUtente(
			ProtocolloUscitaForm pForm,
			ConfigurazioneUtenteVO configurazioneVO, int utenteId) {
		pForm.inizializzaFormToCopyProtocollo();
		Organizzazione organizzazione = Organizzazione.getInstance();
		long dataCorrente = System.currentTimeMillis();
		if (configurazioneVO.getReturnValue() == ReturnValues.FOUND) {
			if (configurazioneVO.getCheckDestinatari().equals("1")) {
				if (configurazioneVO.getAssegnatarioUfficioId() > 0) {
					pForm.rimuoviDestinatari();
					DestinatarioView destinatarioView = new DestinatarioView();
					destinatarioView.setDestinatario(configurazioneVO
							.getDestinatario());
					destinatarioView.setFlagTipoDestinatario("F");
					pForm.aggiungiDestinatario(destinatarioView);
				}
			}
			if (configurazioneVO.getCheckDataDocumento().equals("1")) {
				if ("0".equals(configurazioneVO.getDataDocumento())) {
					pForm.setDataDocumento(null);
				} else if ("1".equals(configurazioneVO.getDataDocumento())) {
					pForm.setDataDocumento(DateUtil.formattaData(dataCorrente));
				}

			}
			if (configurazioneVO.getCheckRicevutoIl().equals("1")) {
				if ("0".equals(configurazioneVO.getDataRicezione())) {
					pForm.setDataRicezione(null);
				} else if ("1".equals(configurazioneVO.getDataRicezione())) {
					pForm.setDataRicezione(DateUtil.formattaData(dataCorrente));
				}

			}
			if (configurazioneVO.getCheckOggetto().equals("1")) {
				if (configurazioneVO.getOggetto() != null
						&& !"".equals(configurazioneVO.getOggetto())) {
					pForm.setOggetto(configurazioneVO.getOggetto());
				} else {
					pForm.setOggetto(null);
				}

			}

			if (configurazioneVO.getCheckTipoDocumento().equals("1")) {
				pForm.setTipoDocumentoId(configurazioneVO.getTipoDocumentoId());
			}

			if (configurazioneVO.getCheckTitolario().equals("1")) {
				pForm.setTitolarioSelezionatoId(configurazioneVO
						.getTitolarioId());
				Utente utente = organizzazione.getUtente(utenteId);
				TitolarioBO.impostaTitolario(pForm, utente.getUfficioInUso(),
						configurazioneVO.getTitolario());
			} else {
				pForm.setTitolario(null);
			}

		}
		return pForm;
	}

	public static boolean isModificable(ProtocolloIngresso protocollo,
			Utente utente) {
		if (protocollo != null) {
			String statoProtocollo = protocollo.getProtocollo()
					.getStatoProtocollo();
			int utenteId = utente.getValueObject().getId().intValue();
			if (!"C".equals(statoProtocollo) && !"F".equals(statoProtocollo)) {
				AssegnatarioVO assegnatario = null;
				for (Iterator<AssegnatarioVO> i = protocollo.getAssegnatari().iterator(); i
						.hasNext();) {
					assegnatario = (AssegnatarioVO) i.next();
					if (assegnatario.getUtenteAssegnatarioId() == utenteId) {
						if (assegnatario.isCompetente())
							return true;
						if ("R".equals(statoProtocollo))
							return true;
					}
				}

				if (protocollo.getProtocollo().getRowCreatedUser().equals(
						utente.getValueObject().getUsername()))
					return true;
				if (protocollo.getProtocollo().getCaricaProtocollatoreId() == utente
						.getCaricaInUso())
					return true;
			}

		}
		return false;
	}

	public static boolean isAssegnatarioReserved(PostaInterna protocollo,
			Utente utente) {
		int caricaId = utente.getCaricaInUso();
		int ufficioId = utente.getUfficioInUso();
		boolean isReferente = Organizzazione.getInstance()
				.getUfficio(ufficioId).isCaricaReferente(
						utente.getCaricaInUso());
		if (MenuDelegate.getInstance().isChargeEnabledByUniqueName(utente,
				"read_reserved")) {
			AssegnatarioVO assegnatario = null;
			for (Iterator<AssegnatarioVO> i = protocollo.getDestinatari().iterator(); i
					.hasNext();) {
				assegnatario = (AssegnatarioVO) i.next();
				if (assegnatario.getCaricaAssegnatarioId() == caricaId)
					return true;
				if (isReferente
						&& assegnatario.getUfficioAssegnatarioId() == ufficioId
						&& assegnatario.getCaricaAssegnatarioId() == 0)
					return true;
			}
		}
		return false;
	}
	
	public static boolean isAssegnatarioReserved(ProtocolloIngresso protocollo,
			Utente utente) {
		int caricaId = utente.getCaricaInUso();
		int ufficioId = utente.getUfficioInUso();
		boolean isReferente = Organizzazione.getInstance()
				.getUfficio(ufficioId).isCaricaReferente(
						utente.getCaricaInUso());
		if (MenuDelegate.getInstance().isChargeEnabledByUniqueName(utente,
				"read_reserved")) {
			AssegnatarioVO assegnatario = null;
			for (Iterator<AssegnatarioVO> i = protocollo.getAssegnatari().iterator(); i
					.hasNext();) {
				assegnatario = (AssegnatarioVO) i.next();
				if (assegnatario.getCaricaAssegnatarioId() == caricaId)
					return true;
				if (isReferente
						&& assegnatario.getUfficioAssegnatarioId() == ufficioId
						&& assegnatario.getCaricaAssegnatarioId() == 0)
					return true;
			}
		}
		return false;
	}

	public static boolean isModificable(PostaInterna protocollo, Utente utente) {
		if (protocollo != null) {
			String statoProtocollo = protocollo.getProtocollo()
					.getStatoProtocollo();
			int utenteId = utente.getValueObject().getId().intValue();
			if (!"C".equals(statoProtocollo) && !"F".equals(statoProtocollo)) {
				AssegnatarioVO assegnatario = null;
				for (Iterator<AssegnatarioVO> i = protocollo.getDestinatari().iterator(); i
						.hasNext();) {
					assegnatario = (AssegnatarioVO) i.next();
					if (assegnatario.getUtenteAssegnatarioId() == utenteId) {
						if (assegnatario.isCompetente())
							return true;
						if ("R".equals(statoProtocollo))
							return true;
					}
				}
				if(protocollo.getProtocollo().getRowCreatedUser()!=null && protocollo.getProtocollo().getRowCreatedUser().equals(
						utente.getValueObject().getUsername()))
					return true;
				if (protocollo.getProtocollo().getCaricaProtocollatoreId() == utente
						.getCaricaInUso())
					return true;
			}

		}
		return false;
	}

	public static boolean isModificable(ProtocolloUscita protocollo,
			Utente utente) {
		if (!"C".equals(protocollo.getProtocollo().getStatoProtocollo())) {
			if (utente.getUfficioInUso()== protocollo.getProtocollo().getUfficioProtocollatoreId()
					|| utente.getUfficioInUso()== protocollo.getProtocollo().getUfficioMittenteId()) {
				return true;
			}
		/*
		if (!"C".equals(protocollo.getProtocollo().getStatoProtocollo())) {
			if (utente.isUtenteAbilitatoSuUfficio(protocollo.getProtocollo()
					.getUfficioProtocollatoreId())
					|| utente.isUtenteAbilitatoSuUfficio(protocollo
							.getProtocollo().getUfficioMittenteId())) {
				modificabile = true;
			}
		*/
		}
		return false;
	}

	public static PostaInternaForm getPostaInternaConfigurazioneUtente(
			PostaInternaForm pForm, ConfigurazioneUtenteVO configurazioneVO,
			int utenteId) {
		pForm.inizializzaFormToCopyProtocollo();
		Organizzazione organizzazione = Organizzazione.getInstance();
		long dataCorrente = System.currentTimeMillis();
		if (configurazioneVO.getReturnValue() == ReturnValues.FOUND) {
			if (configurazioneVO.getCheckAssegnatari().equals("1")) {
				if (configurazioneVO.getAssegnatarioUfficioId() > 0) {
					pForm.removeDestinatari();
					UfficioVO ufficioVO = (organizzazione
							.getUfficio(configurazioneVO
									.getAssegnatarioUfficioId()))
							.getValueObject();

					AssegnatarioView ass = new AssegnatarioView();

					ass.setUfficioId(ufficioVO.getId().intValue());
					ass.setNomeUfficio(ufficioVO.getDescription());
					ass.setDescrizioneUfficio(ufficioVO.getName());
					pForm.aggiungiDestinatario(ass);
					pForm.setDestinatarioCompetente(ass.getKey());
					if (pForm.isDipTitolarioUfficio()) {
						pForm.setTitolario(null);
					}

				}

			}
			if (configurazioneVO.getCheckDataDocumento().equals("1")) {
				if ("0".equals(configurazioneVO.getDataDocumento())) {
					pForm.setDataDocumento(null);
				} else if ("1".equals(configurazioneVO.getDataDocumento())) {
					pForm.setDataDocumento(DateUtil.formattaData(dataCorrente));
				}

			}
			if (configurazioneVO.getCheckRicevutoIl().equals("1")) {
				if ("0".equals(configurazioneVO.getDataRicezione())) {
					pForm.setDataRicezione(null);
				} else if ("1".equals(configurazioneVO.getDataRicezione())) {
					pForm.setDataRicezione(DateUtil.formattaData(dataCorrente));
				}

			}
			if (configurazioneVO.getCheckOggetto().equals("1")) {
				if (configurazioneVO.getOggetto() != null
						&& !"".equals(configurazioneVO.getOggetto())) {
					pForm.setOggetto(configurazioneVO.getOggetto());
				} else {
					pForm.setOggetto(null);
				}

			}

			AssegnatarioView mittente = new AssegnatarioView();
			Utente utente = organizzazione.getUtente(utenteId);
			UfficioVO ufficioMitt = utente.getUfficioVOInUso();
			mittente.setUfficioId(ufficioMitt.getId().intValue());
			mittente.setNomeUfficio(ufficioMitt.getDescription());
			mittente.setDescrizioneUfficio(ufficioMitt.getName());
			mittente.setNomeUtente(utente.getValueObject().getFullName());
			pForm.setMittente(mittente);
			if (configurazioneVO.getCheckTipoDocumento().equals("1")) {
				pForm.setTipoDocumentoId(configurazioneVO.getTipoDocumentoId());
			}

			if (configurazioneVO.getCheckTitolario().equals("1")) {
				pForm.setTitolarioSelezionatoId(configurazioneVO
						.getTitolarioId());

				TitolarioBO.impostaTitolario(pForm, utente.getUfficioInUso(),
						configurazioneVO.getTitolario());
			} else
				pForm.setTitolario(null);
		}
		return pForm;
	}

	public static PostaInterna getDefaultPostaInterna(Utente utente) {
		ProtocolloVO protocollo = getDefaultProtocollo(utente);
		protocollo.setFlagTipo(ProtocolloVO.FLAG_TIPO_POSTA_INTERNA);
		PostaInterna pu = new PostaInterna();
		pu.setProtocollo(protocollo);
		return pu;
	}

	public static ProtocolloRegistroEmergenza getDefaultProtocolloRegistroEmergenza(
			Utente utente) {
		ProtocolloVO protocollo = getDefaultProtocollo(utente);
		protocollo.setFlagTipo(ProtocolloVO.FLAG_TIPO_REG_EM);
		ProtocolloRegistroEmergenza p = new ProtocolloRegistroEmergenza();
		p.setProtocollo(protocollo);
		return p;
	}

	public static String formattaNumeroProtocollo(String numeroProtocollo) {
		int index = numeroProtocollo.indexOf("/");
		if (index != -1) {
			numeroProtocollo = numeroProtocollo.substring(0, index - 1);
		}
		long ran = 1 * 1000000000l + Integer.valueOf(numeroProtocollo);
		return String.valueOf(ran).substring(1);
	}

	public static List<Integer> getUfficiAssegnatariOggetto(ProtocolloForm form) {
		OggettoVO ogg = form.getOggettoFromOggettario();
		List<Integer> ids=new ArrayList<Integer>();
		if (ogg != null) {
			ids = OggettarioDelegate.getInstance().getUfficiAssegnatariId(Integer.valueOf(ogg.getOggettoId()));
			
		}
		return ids;
	}
}
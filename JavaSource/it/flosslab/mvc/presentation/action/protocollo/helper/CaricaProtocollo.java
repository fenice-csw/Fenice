package it.flosslab.mvc.presentation.action.protocollo.helper;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.business.DomandaDelegate;
import it.compit.fenice.mvc.presentation.action.protocollo.helper.form.AggiornaPostaInternaForm;
import it.compit.fenice.mvc.presentation.action.protocollo.helper.form.AggiornaPostaInternaFromUscitaForm;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.protocollo.DomandaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.StoriaProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.flosslab.mvc.presentation.action.protocollo.helper.form.AggiornaProtocolloIngressoForm;
import it.flosslab.mvc.presentation.action.protocollo.helper.form.AggiornaProtocolloUscitaForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CaricaProtocollo {

	public static final String PROTOCOLLO_USCITA = "Protocollo_uscita";
	public static final String PROTOCOLLO_INGRESSO = "Protocollo_ingresso";
	public static final String POSTA_INTERNA = "Posta_interna";
	public static final String DOMANDE_ERSU = "Domande_ersu";
	public static final String FATTURE = "Fatture";

	public static void caricaDomandaErsu(HttpServletRequest request,
			ProtocolloForm form) {
		HttpSession session = request.getSession();
		String domandaId = (String) request.getAttribute("domandaId");
		Integer allaccioId = (Integer) request.getAttribute("allaccioId");
		form.setDomandaErsu(true);
		if (domandaId != null) {
			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
				DomandaVO domanda = DomandaDelegate.getInstance()
						.getDomandaById(domandaId);
				form.setVersioneDefault(true);
				ProtocolloIngresso protocollo = ProtocolloBO
						.getDefaultProtocolloIngresso(utente);
				if (domanda != null)
					AggiornaProtocolloIngressoForm.aggiornaDaDomandaErsu(
							protocollo, form, domanda, session,utente);
				if (allaccioId != null) {
					Protocollo allaccio = ProtocolloDelegate.getInstance()
							.getProtocolloIngressoById(allaccioId);
					if (allaccio != null)
						AggiornaProtocolloIngressoForm.aggiornaAllaccioForm(
								form, allaccio, utente);
				}
		}
	}

	public static void caricaFatture(HttpServletRequest request,
			ProtocolloForm form) {
		HttpSession session = request.getSession();
		Integer protocolloId = (Integer) request.getAttribute("protocolloId");
		ProtocolloIngresso protocollo = null;
		if (protocolloId != null) {
			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (protocolloId != null) {
				int id = protocolloId.intValue();
				Integer versioneId = (Integer) request
						.getAttribute("versioneId");
				if (versioneId == null) {
					protocollo = ProtocolloDelegate.getInstance()
							.getProtocolloIngressoById(id);
					form.setVersioneDefault(true);
				} else {
					int versione = versioneId.intValue();
					protocollo = StoriaProtocolloDelegate.getInstance()
							.getVersioneProtocolloIngresso(id, versione);
					form.setVersioneDefault(false);
				}

				if (protocollo == null) {
					protocollo = ProtocolloBO.getDefaultProtocolloIngresso(utente);
					session.removeAttribute("protocolloIngresso");
					AggiornaProtocolloIngressoForm.aggiornaFatture(protocollo, form,session,utente);
				} else {
					session.setAttribute("protocolloIngresso", protocollo);
				}
			}
			if (request.getAttribute("presaVisione") != null)
				form.setVisionato(true);
			AggiornaProtocolloIngressoForm.aggiornaFatture(protocollo, form, session,utente);
			
		}
	}
	
	public static void caricaProtocolloIngresso(HttpServletRequest request,
			ProtocolloForm form) {
		HttpSession session = request.getSession();
		Integer protocolloId = (Integer) request.getAttribute("protocolloId");
		ProtocolloIngresso protocollo = (ProtocolloIngresso) request.getAttribute(Constants.PROTOCOLLO_INGRESSO_DA_EMAIL);
		boolean daMail = false;
		if(request.getAttribute("domandaErsu")!=null)
			form.setDomandaErsu(true);
		if(request.getAttribute("daRicerca")!=null)
			form.setDaRicerca(true);
		if (protocolloId != null || protocollo != null) {
			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			if (protocollo != null) {
				request.removeAttribute(Constants.PROTOCOLLO_INGRESSO_DA_EMAIL);
				daMail = true;
				session.setAttribute("protocolloIngresso", protocollo);
			} else if (protocolloId != null) {
				int id = protocolloId.intValue();
				Integer versioneId = (Integer) request
						.getAttribute("versioneId");
				if (versioneId == null) {
					protocollo = ProtocolloDelegate.getInstance()
							.getProtocolloIngressoById(id);
					form.setVersioneDefault(true);
				} else {
					int versione = versioneId.intValue();
					protocollo = StoriaProtocolloDelegate.getInstance()
							.getVersioneProtocolloIngresso(id, versione);
					form.setVersioneDefault(false);
				}

				if (protocollo == null) {
					protocollo = ProtocolloBO
							.getDefaultProtocolloIngresso(utente);
					session.removeAttribute("protocolloIngresso");
					AggiornaProtocolloIngressoForm.aggiorna(protocollo, form,
							session, daMail,utente);
				} else {
					session.setAttribute("protocolloIngresso", protocollo);
				}
			}
			AggiornaProtocolloIngressoForm.aggiorna(protocollo, form, session,
					daMail,utente);
			if (request.getAttribute("presaVisione") != null){
				form.setVisionato(true);
			}
		}
	}

	public static void caricaProtocolloUscita(HttpServletRequest request,
			ProtocolloForm form) {
		
		HttpSession session = request.getSession(true);
		Integer protocolloId = (Integer) request.getAttribute("protocolloId");
		ProtocolloUscita protocollo = (ProtocolloUscita) request.getAttribute(Constants.PROTOCOLLO_USCITA_ARCHIVIO);
		if(protocollo==null)
			protocollo = (ProtocolloUscita) request.getAttribute(Constants.PROTOCOLLO_DA_EDITOR);
		if(protocollo==null)
			protocollo = (ProtocolloUscita) request.getAttribute(Constants.PROTOCOLLO_DA_NOTIFICA);
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if(request.getAttribute("daRicerca")!=null)
			form.setDaRicerca(true);
		if (protocolloId != null || protocollo != null) {
			if (protocollo != null) {
				request.removeAttribute(Constants.PROTOCOLLO_USCITA_ARCHIVIO);
				request.removeAttribute(Constants.PROTOCOLLO_DA_EDITOR);
				request.removeAttribute(Constants.PROTOCOLLO_DA_NOTIFICA);
				protocollo.setFromArchivio(true);
			} else if (protocolloId != null) {

				int id = protocolloId.intValue();
				ProtocolloDelegate pd = ProtocolloDelegate.getInstance();
				ProtocolloVO puVO = null;
				Boolean risposta = (Boolean) request.getAttribute("risposta");
				protocollo = null;
				if (Boolean.TRUE.equals(risposta)) {
					protocollo = ProtocolloBO
							.getDefaultProtocolloUscita(utente);
					puVO = new ProtocolloVO();
					ProtocolloVO piVO = pd.getProtocolloVOById(id);
					puVO.setAooId(piVO.getAooId());
					puVO.setOggetto(piVO.getOggetto());
					puVO.setFlagTipo(ProtocolloVO.FLAG_TIPO_PROTOCOLLO_USCITA);
					puVO.setTipoDocumentoId(piVO.getTipoDocumentoId());
					puVO.setMozione(false);
					puVO.setRispostaId(id);
					puVO.setTitolarioId(piVO.getTitolarioId());
					DestinatarioVO destinatario = new DestinatarioVO();
					if ("F".equals(piVO.getFlagTipoMittente())) {
						destinatario.setFlagTipoDestinatario("F");
						if (piVO.getNomeMittente() != null) {
							destinatario.setDestinatario(piVO
									.getCognomeMittente()
									+ "" + piVO.getNomeMittente());
						} else {
							destinatario.setDestinatario(piVO
									.getCognomeMittente());
						}
					} else {
						destinatario.setFlagTipoDestinatario("G");
						destinatario.setDestinatario(piVO
								.getDenominazioneMittente());
					}
					destinatario.setFlagConoscenza(false);
					destinatario.setDataSpedizione(null);

					protocollo.addDestinatari(destinatario);
					AssegnatarioVO assegnatario = pd
							.getAssegnatarioPerCompetenza(id);
					protocollo.setMittente(assegnatario);
					AllaccioVO allaccioVo = new AllaccioVO();
					allaccioVo.setProtocolloAllacciatoId(id);
					allaccioVo.setAllaccioDescrizione(piVO.getNumProtocollo()
							+ "/" + piVO.getAnnoRegistrazione() + " ("
							+ piVO.getFlagTipo() + ")");
					protocollo.allacciaProtocollo(allaccioVo);
					protocollo.setProtocollo(puVO);
					
				} else {
					protocollo = new ProtocolloUscita();
					
					Integer versioneId = (Integer) request
							.getAttribute("versioneId");
					request.removeAttribute("versioneId");
					if (versioneId == null) {
						protocollo = pd.getProtocolloUscitaById(id);
						form.setVersioneDefault(true);
					} else {
						int versione = versioneId.intValue();
						protocollo = StoriaProtocolloDelegate.getInstance()
								.getVersioneProtocolloUscita(id, versione);
						form.setVersioneDefault(false);
					}

					if (protocollo == null) {
						protocollo = ProtocolloBO
								.getDefaultProtocolloUscita(utente);
						session.removeAttribute("protocolloUscita");
					} else {
						session.setAttribute("protocolloUscita", protocollo);
					}
				}
			}
			AggiornaProtocolloUscitaForm.aggiornaForm(protocollo, form,
					session, false,utente);
		}
	}
	
	public static void caricaPostaInterna(HttpServletRequest request,
			ProtocolloForm form) {
		HttpSession session = request.getSession();
		Integer protocolloId = (Integer) request.getAttribute("protocolloId");
		PostaInterna protocollo=null;
		if(request.getAttribute(Constants.PROTOCOLLO_DA_EDITOR)!=null){
			protocollo = (PostaInterna) request.getAttribute(Constants.PROTOCOLLO_DA_EDITOR);
			request.removeAttribute(Constants.PROTOCOLLO_DA_EDITOR);
		}
		if(request.getAttribute(Constants.PROTOCOLLO_DA_REPERTORIO)!=null){
			protocollo = (PostaInterna) request.getAttribute(Constants.PROTOCOLLO_DA_REPERTORIO);
			request.removeAttribute(Constants.PROTOCOLLO_DA_REPERTORIO);
		}
		if(request.getAttribute(Constants.POSTA_INTERNA_DA_PROCEDIMENTO)!=null){
			protocollo = (PostaInterna) request.getAttribute(Constants.POSTA_INTERNA_DA_PROCEDIMENTO);
			request.removeAttribute(Constants.POSTA_INTERNA_DA_PROCEDIMENTO);
		}
		if(request.getAttribute("daRicerca")!=null)
			form.setDaRicerca(true);
		if (protocolloId != null || protocollo != null) {
			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			if (protocollo == null && protocolloId != null) {
				int id = protocolloId.intValue();
				ProtocolloDelegate pd = ProtocolloDelegate.getInstance();
				ProtocolloVO puVO = null;
				Boolean risposta = (Boolean) request.getAttribute("risposta");
				if (Boolean.TRUE.equals(risposta)) {
					protocollo = ProtocolloBO.getDefaultPostaInterna(utente);
					puVO = new ProtocolloVO();
					PostaInterna pInt = pd.getPostaInternaById(id);
					ProtocolloVO piVO=pInt.getProtocollo();
					puVO.setAooId(piVO.getAooId());
					puVO.setOggetto("Re:"+piVO.getOggetto());
					puVO.setFlagTipo(ProtocolloVO.FLAG_TIPO_POSTA_INTERNA);
					puVO.setTipoDocumentoId(piVO.getTipoDocumentoId());
					puVO.setMozione(false);
					puVO.setRispostaId(id);
					puVO.setTitolarioId(piVO.getTitolarioId());
					AssegnatarioVO destinatario = new AssegnatarioVO();
					destinatario.setUfficioAssegnatarioId(piVO
							.getUfficioMittenteId());
					destinatario.setUtenteAssegnatarioId(piVO.getUtenteMittenteId());
					CaricaVO c=CaricaDelegate.getInstance().getCaricaByUtenteAndUfficio(piVO.getUtenteMittenteId(), piVO.getUfficioMittenteId());
					if(c!=null)
						destinatario.setCaricaAssegnatarioId(c.getCaricaId());
					destinatario.setCaricaAssegnanteId(utente.getCaricaInUso());
					destinatario.setUfficioAssegnanteId(utente.getUfficioInUso());
					destinatario.setCompetente(true);
					if (pInt.getProcedimenti().size()!=0){
						protocollo.setProcedimenti(pInt.getProcedimenti());
						protocollo.setFascicoliRisposta(pInt.getFascicoli());
						protocollo.setRispostaId(id);
						
					}
					protocollo.aggiungiDestinatario(destinatario);
					// il mittente è l'utente stesso
					AssegnatarioVO mittente = pd.getAssegnatarioPerCompetenza(id);
					mittente.setUfficioAssegnatarioId(utente.getUfficioInUso());
					mittente.setUtenteAssegnatarioId(utente.getValueObject()
							.getId().intValue());
					protocollo.setMittente(mittente);
					// imposto il protocollo in ingresso come allaccio
					AllaccioVO allaccioVo = new AllaccioVO();
					allaccioVo.setProtocolloAllacciatoId(id);
					allaccioVo.setAllaccioDescrizione(piVO.getNumProtocollo()
							+ "/" + piVO.getAnnoRegistrazione() + " ("
							+ piVO.getFlagTipo() + ")");
					protocollo.allacciaProtocollo(allaccioVo);
					protocollo.setProtocollo(puVO);

				} else {
					Integer versioneId = (Integer) request
							.getAttribute("versioneId");
					if (versioneId == null) {

						protocollo = ProtocolloDelegate.getInstance()
								.getPostaInternaById(id);
						form.setVersioneDefault(true);
					} else {

						int versione = versioneId.intValue();
						protocollo = StoriaProtocolloDelegate.getInstance()
								.getVersionePostaInterna(id, versione);
						form.setVersioneDefault(false);
					}
	
					if (protocollo == null) {
						protocollo = ProtocolloBO.getDefaultPostaInterna(utente);
						session.removeAttribute("postaInterna");
						AggiornaPostaInternaForm.aggiorna(protocollo, form,
								session, false,utente);
					} else {
						session.setAttribute("postaInterna", protocollo);
					}
				}			
			}
			if (request.getAttribute("presaVisione") != null){
				form.setVisionato(true);
			}
			if(request.getAttribute("inoltraUscita") != null)
				AggiornaPostaInternaFromUscitaForm.aggiorna(protocollo, form, session, false,utente);
			else
				AggiornaPostaInternaForm.aggiorna(protocollo, form, session, false,utente);
		}
	}

	
}

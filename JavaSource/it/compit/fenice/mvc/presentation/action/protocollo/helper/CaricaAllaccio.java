package it.compit.fenice.mvc.presentation.action.protocollo.helper;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.presentation.action.protocollo.helper.form.AggiornaAllaccioForm;
import it.compit.fenice.mvc.presentation.actionform.protocollo.VisualizzaProtocolloForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.StoriaProtocolloDelegate;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CaricaAllaccio {

	public static final String PROTOCOLLO_USCITA = "Protocollo_uscita";
	public static final String PROTOCOLLO_INGRESSO = "Protocollo_ingresso";
	public static final String POSTA_INTERNA = "Posta_interna";

	public static void caricaProtocolloIngresso(HttpServletRequest request,
			VisualizzaProtocolloForm form) {
		HttpSession session = request.getSession();
		Integer protocolloId = (Integer) request.getAttribute("protocolloId");
		ProtocolloIngresso protocollo = (ProtocolloIngresso) request
				.getAttribute(Constants.PROTOCOLLO_INGRESSO_DA_EMAIL);

		if (protocolloId != null || protocollo != null) {

			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

			if (protocollo != null) {
				request.removeAttribute(Constants.PROTOCOLLO_INGRESSO_DA_EMAIL);
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
					session.removeAttribute("visualizzaAllaccio");
					AggiornaAllaccioForm.aggiornaIngresso(protocollo, form,
							session);
				} else {
					session.setAttribute("visualizzaAllaccio", protocollo);
				}
			}
			AggiornaAllaccioForm.aggiornaIngresso(protocollo, form, session);
		}
	}

	public static void caricaProtocolloUscita(HttpServletRequest request,
			VisualizzaProtocolloForm form) {
		HttpSession session = request.getSession(true);
		Integer protocolloId = (Integer) request.getAttribute("protocolloId");
		ProtocolloUscita protocollo = (ProtocolloUscita) request
				.getAttribute(Constants.PROTOCOLLO_USCITA_ARCHIVIO);

		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (protocolloId != null || protocollo != null) {

			if (protocollo != null) {
				request.removeAttribute(Constants.PROTOCOLLO_USCITA_ARCHIVIO);
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
						protocollo = pd.getProtocolloUscitaById(id);// ???
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
						session.removeAttribute("visualizzaAllaccio");
					} else {
						session.setAttribute("visualizzaAllaccio", protocollo);
					}
				}
			}
			AggiornaAllaccioForm.aggiornaUscita(protocollo, form, session);
		}

	}

	public static void caricaPostaInterna(HttpServletRequest request,
			VisualizzaProtocolloForm form) {
		HttpSession session = request.getSession();
		Integer protocolloId = (Integer) request.getAttribute("protocolloId");
		PostaInterna protocollo = (PostaInterna) request
				.getAttribute(Constants.PROTOCOLLO_INGRESSO_DA_EMAIL);

		if (protocolloId != null || protocollo != null) {

			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);

			if (protocollo != null) {
				request.removeAttribute(Constants.PROTOCOLLO_INGRESSO_DA_EMAIL);
				session.setAttribute("postaInterna", protocollo);
			} else if (protocolloId != null) {
				int id = protocolloId.intValue();
				ProtocolloDelegate pd = ProtocolloDelegate.getInstance();
				ProtocolloVO puVO = null;
				Boolean risposta = (Boolean) request.getAttribute("risposta");
				if (Boolean.TRUE.equals(risposta)) {
					protocollo = ProtocolloBO.getDefaultPostaInterna(utente);
					puVO = new ProtocolloVO();
					ProtocolloVO piVO = pd.getProtocolloVOById(id);
					puVO.setAooId(piVO.getAooId());
					puVO.setOggetto(piVO.getOggetto());
					puVO.setFlagTipo(ProtocolloVO.FLAG_TIPO_POSTA_INTERNA);
					puVO.setTipoDocumentoId(piVO.getTipoDocumentoId());
					puVO.setMozione(false);
					puVO.setRispostaId(id);
					puVO.setTitolarioId(piVO.getTitolarioId());
					AssegnatarioVO destinatario = new AssegnatarioVO();
					destinatario.setUfficioAssegnatarioId(piVO
							.getUfficioMittenteId());
					destinatario.setUtenteAssegnatarioId(piVO
							.getUtenteMittenteId());
					protocollo.aggiungiDestinatario(destinatario);
					AssegnatarioVO mittente = pd
							.getAssegnatarioPerCompetenza(id);
					mittente.setUfficioAssegnatarioId(utente.getUfficioInUso());
					mittente.setUtenteAssegnatarioId(utente.getValueObject()
							.getId().intValue());
					protocollo.setMittente(mittente);
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
						protocollo = ProtocolloBO
								.getDefaultPostaInterna(utente);
						session.removeAttribute("visualizzaAllaccio");
						AggiornaAllaccioForm.aggiornaPosta(protocollo, form,
								session);
					} else {
						session.setAttribute("visualizzaAllaccio", protocollo);
					}
				}
			}
			AggiornaAllaccioForm.aggiornaPosta(protocollo, form, session);
		}
	}

}

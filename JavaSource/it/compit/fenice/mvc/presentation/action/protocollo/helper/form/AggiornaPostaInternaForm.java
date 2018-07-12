package it.compit.fenice.mvc.presentation.action.protocollo.helper.form;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.presentation.actionform.protocollo.PostaInternaForm;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.flosslab.mvc.presentation.action.protocollo.helper.form.AggiornaProtocolloForm;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

public class AggiornaPostaInternaForm extends AggiornaProtocolloForm {

	public static void aggiorna(Protocollo protocollo, ProtocolloForm form,
			HttpSession session, boolean mail,Utente utente) {
		AggiornaProtocolloForm.aggiorna(protocollo, form, session, mail,utente);
		PostaInterna postaInterna = (PostaInterna) protocollo;
		PostaInternaForm pForm = (PostaInternaForm) form;
		aggiornaStatoProtocolloForm(postaInterna, pForm, session);
		aggiornaMittenteForm(postaInterna, pForm);
		aggiornaDestinatariForm(postaInterna, pForm);
		aggiornaTitolarioForm(postaInterna.getProtocollo(), pForm,
				(Utente) session.getAttribute(Constants.UTENTE_KEY));
		pForm.setIntDocEditor(postaInterna.getIntDocEditor());
		aggiornaFascicoliRispostaForm(postaInterna, pForm);
		pForm.setRispostaId(postaInterna.getRispostaId());
		pForm.setDocRepertorioId(postaInterna.getDocRepertorioId());
		form.setDocumentoReadable(true);

	}

	private static void aggiornaMittenteForm(PostaInterna protocollo,
			PostaInternaForm form) {
		AssegnatarioVO mittente = protocollo.getMittente();
		if (mittente != null) {
			Organizzazione org = Organizzazione.getInstance();
			Ufficio uff = org.getUfficio(mittente.getUfficioAssegnatarioId());
			Utente ute = org.getUtente(mittente.getUtenteAssegnatarioId());
			form.setMittente(newMittente(uff, ute));
		}
	}

	private static void aggiornaFascicoliRispostaForm(PostaInterna protocollo,
			PostaInternaForm form) {
		if (protocollo != null && protocollo.getFascicoliRisposta().size()!=0) {
            for (Iterator<FascicoloVO> i = protocollo.getFascicoliRisposta().iterator(); i.hasNext();) {
                FascicoloVO fascicolo = i.next();
                form.aggiungiFascicolo(fascicolo);
            }
            for (Iterator<ProtocolloProcedimentoVO> i = form.getProcedimentiProtocollo().iterator(); i.hasNext();) {
            	ProtocolloProcedimentoVO procedimento = (ProtocolloProcedimentoVO) i
                        .next();
                procedimento.setAggiunto(false);
            }
            
        }
	}
	
	public static AssegnatarioView newMittente(Ufficio ufficio, Utente utente) {
		UfficioVO uffVO = ufficio.getValueObject();
		AssegnatarioView mittente = new AssegnatarioView();
		mittente.setUfficioId(uffVO.getId().intValue());
		mittente.setDescrizioneUfficio(ufficio.getPath());
		mittente.setNomeUfficio(uffVO.getDescription());
		if (utente != null) {
			UtenteVO uteVO = utente.getValueObject();
			mittente.setUtenteId(uteVO.getId().intValue());
			mittente.setNomeUtente(uteVO.getCaricaFullName());
		}
		return mittente;
	}

	private static void aggiornaDestinatariForm(PostaInterna protocollo,
			PostaInternaForm form) {
		Organizzazione org = Organizzazione.getInstance();
		for (Iterator<AssegnatarioVO> i = protocollo.getDestinatari().iterator(); i.hasNext();) {
			AssegnatarioVO assegnatario =  i.next();
			AssegnatarioView ass = new AssegnatarioView();
			int uffId = assegnatario.getUfficioAssegnatarioId();
			ass.setUfficioId(uffId);
			Ufficio uff = org.getUfficio(uffId);
			if (uff != null) {
				ass.setNomeUfficio(uff.getValueObject().getDescription());
			}
			if (assegnatario.getCaricaAssegnatarioId() != 0) {
				int caricaId = assegnatario.getCaricaAssegnatarioId();
				CaricaVO carica = org.getCarica(caricaId);
				ass.setUtenteId(carica.getUtenteId());
				Utente ute = org.getUtente(carica.getUtenteId());
				if (ute != null) {
					ass.setNomeUtente(ute.getValueObject().getFullName());
				}
			}
			ass.setStato(assegnatario.getStatoAssegnazione());
			ass.setCaricaAssegnanteId(assegnatario.getCaricaAssegnanteId());
			ass.setUfficioAssegnanteId(assegnatario.getUfficioAssegnanteId());
			form.aggiungiDestinatario(ass);
			if (assegnatario.isCompetente()) {
				form.setDestinatarioCompetente(ass.getKey());
				form.setMsgDestinatarioCompetente(assegnatario
						.getMsgAssegnatarioCompetente());
			}
			ass.setPresaVisione(assegnatario.isPresaVisione());
			ass.setLavorato(assegnatario.isLavorato());
		}

	}

	public static void aggiornaStatoProtocolloForm(PostaInterna protocollo,
			PostaInternaForm form, HttpSession session) {
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		form.setModificabile(ProtocolloBO.isModificable(protocollo, utente));
		if (form.isModificabile()
				|| !protocollo.getProtocollo().isRiservato()
				|| (protocollo.getProtocollo().isRiservato() && utente
						.getValueObject().getId().intValue() == protocollo
						.getProtocollo().getCaricaProtocollatoreId())
				|| (protocollo.getProtocollo().isRiservato() && ProtocolloBO.isAssegnatarioReserved(protocollo, utente))
				|| protocollo.getMittente().getUtenteAssegnatarioId()==utente.getValueObject().getId()) {
			form.setDocumentoVisibile(true);
		} else {
			form.setDocumentoVisibile(false);
		}
	}
	
	private static void aggiornaTitolarioForm(ProtocolloVO protocollo,
			PostaInternaForm form, Utente utente) {
		int titolarioId = protocollo.getTitolarioId();
		impostaTitolario(form, utente, titolarioId);
	}

	public static void impostaTitolario(PostaInternaForm form, Utente utente,
			int titolarioId) {
		int ufficioId = utente.getUfficioInUso();
		if (form.isDipTitolarioUfficio()) {
			Iterator<AssegnatarioView> i = form.getDestinatari().iterator();
			while (i.hasNext()) {
				AssegnatarioView assegnatario =  i.next();
				if (assegnatario.isCompetente()) {
					ufficioId = assegnatario.getUfficioId();
					break;
				}
			}
		}
		TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);
	}

	public static void aggiungiDestinatariListaDistribuzioneForm(
			Collection<AssegnatarioVO> soggetti, PostaInternaForm form,Utente utente) {
		Organizzazione org = Organizzazione.getInstance();
		for (Iterator<AssegnatarioVO> i = soggetti.iterator(); i.hasNext();) {
			AssegnatarioVO assegnatario = i.next();
			AssegnatarioView ass = new AssegnatarioView();
			int uffId = assegnatario.getUfficioAssegnatarioId();
			ass.setUfficioId(uffId);
			Ufficio uff = org.getUfficio(uffId);
			if (uff != null) {
				ass.setNomeUfficio(uff.getValueObject().getDescription());
			}
			if (assegnatario.getCaricaAssegnatarioId() != 0) {
				int caricaId = assegnatario.getCaricaAssegnatarioId();
				CaricaVO carica = org.getCarica(caricaId);
				ass.setUtenteId(carica.getUtenteId());
				Utente ute = org.getUtente(carica.getUtenteId());
				if (ute != null) {
					ute.getValueObject().setCarica(carica.getNome());
					if (carica.isAttivo())
						ass.setNomeUtente(ute.getValueObject()
								.getCaricaFullName());
					else
						ass.setNomeUtente(ute.getValueObject()
								.getCaricaFullNameNonAttivo());
				} else
					ass.setNomeUtente(carica.getNome());
			}
				ass.setStato('S');
				ass.setCompetente(true);
				ass.setCaricaAssegnanteId(utente.getCaricaInUso());
				ass.setUfficioAssegnanteId(utente.getUfficioInUso());
				form.aggiungiDestinatario(ass);
				form.setDestinatarioCompetente(ass.getKey());
			
		}
	}

}

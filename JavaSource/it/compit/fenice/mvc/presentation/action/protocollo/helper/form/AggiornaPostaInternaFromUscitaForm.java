package it.compit.fenice.mvc.presentation.action.protocollo.helper.form;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.presentation.actionform.protocollo.PostaInternaForm;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;
import it.flosslab.mvc.presentation.action.protocollo.helper.form.AggiornaProtocolloForm;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

public class AggiornaPostaInternaFromUscitaForm extends AggiornaProtocolloForm {

	public static void aggiorna(Protocollo protocollo, ProtocolloForm form,
			HttpSession session, boolean mail,Utente utente) {
		AggiornaProtocolloForm.aggiorna(protocollo, form, session, mail,utente);
		PostaInterna postaInterna = (PostaInterna) protocollo;
		PostaInternaForm pForm = (PostaInternaForm) form;
		pForm.inizializzaFromUscitaForm();
		form.setAutore(protocollo.getProtocollo().getRowCreatedUser());
		aggiornaDatiGeneraliForm(protocollo.getProtocollo(), pForm,utente);
		aggiornaAllacciForm(protocollo, pForm);
		aggiornaAllegatiForm(protocollo, pForm);
		aggiornaAnnotazioniForm(protocollo.getProtocollo(), pForm);
		aggiornaFascicoloForm(protocollo, pForm, utente);
		aggiornaProcedimentoForm(protocollo, pForm, utente);
		aggiornaStatoProtocolloForm(postaInterna, pForm, session);
		aggiornaTitolarioForm(postaInterna.getProtocollo(), pForm, utente);
		pForm.setIntDocEditor(postaInterna.getIntDocEditor());

	}

	private static void aggiornaFascicoloForm(Protocollo protocollo,
			PostaInternaForm form, Utente utente) {
       if (protocollo != null && protocollo.getFascicoli() != null) {
            for (Iterator<FascicoloVO> i = protocollo.getFascicoli().iterator(); i.hasNext();) {
                FascicoloVO fascicolo =  i.next();
                if(utente.getUfficioInUso()==fascicolo.getUfficioIntestatarioId())
                	fascicolo.setOwner(true);
                form.aggiungiFascicolo(fascicolo);
               
            }
        }
    }

    private static void aggiornaProcedimentoForm(Protocollo protocollo,
    		PostaInternaForm form, Utente utente) {
        if (protocollo != null) {
            Collection<ProtocolloProcedimentoVO> procedimenti = protocollo.getProcedimenti();
            if (procedimenti != null) {
                for (Iterator<ProtocolloProcedimentoVO> i = procedimenti.iterator(); i.hasNext();) {
                    ProtocolloProcedimentoVO procedimento =  i
                            .next();
                    form.aggiungiProcedimento(procedimento);
                }
            }
        }
    }
	
	 private static void aggiornaAnnotazioniForm(ProtocolloVO protocollo,
			 PostaInternaForm form) {
	        form.setDescrizioneAnnotazione(protocollo.getDescrizioneAnnotazione());
	        form.setPosizioneAnnotazione(protocollo.getPosizioneAnnotazione());
	        form.setChiaveAnnotazione(protocollo.getChiaveAnnotazione());
	    }
	
	private static void aggiornaAllacciForm(Protocollo protocollo,
			PostaInternaForm form) {
		for (Iterator<AllaccioVO> i = protocollo.getAllacci().iterator(); i.hasNext();) {
			AllaccioVO allaccio = i.next();
			form.allacciaProtocollo(allaccio);
		}
		AllaccioVO allaccioVO = new AllaccioVO();
		allaccioVO.setProtocolloAllacciatoId(protocollo.getProtocollo().getId());
		allaccioVO.setAllaccioDescrizione(protocollo.getProtocollo().getAnnoRegistrazione()
				+ "/" + protocollo.getProtocollo().getNumProtocollo() + " ("
				+ protocollo.getProtocollo().getFlagTipo() + ")");
		form.allacciaProtocollo(allaccioVO);
	}

	

	private static void aggiornaAllegatiForm(Protocollo protocollo,
			PostaInternaForm form) {
		for (Iterator<DocumentoVO> i = protocollo.getAllegati().values().iterator(); i
				.hasNext();) {
			DocumentoVO documentoVO = i.next();
			form.allegaDocumento(documentoVO);
		}
	}

	private static void aggiornaDatiGeneraliForm(ProtocolloVO protocollo,
			PostaInternaForm form,Utente utente) {
		
		Timestamp dataReg = new Timestamp(System.currentTimeMillis());
		
		form.setDataRegistrazione(DateUtil.formattaDataOra(dataReg.getTime()));
		form.setUfficioProtocollatoreId(utente.getUfficioInUso());
		form.setUtenteProtocollatoreId(utente.getCaricaInUso());
		form.setStato("S");
		 
		form.setAooId(protocollo.getAooId());
		form.setOggetto(protocollo.getOggetto());
		form.setOggettoGenerico("Fwd: "+protocollo.getOggetto());
		
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
			mittente.setNomeUtente(uteVO.getFullName());
		}
		return mittente;
	}

	private static void aggiornaStatoProtocolloForm(PostaInterna protocollo,
			PostaInternaForm form, HttpSession session) {
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		form.setModificabile(ProtocolloBO.isModificable(protocollo, utente));
		if (form.isModificabile()
				|| !protocollo.getProtocollo().isRiservato()
				|| (protocollo.getProtocollo().isRiservato() && utente
						.getValueObject().getId().intValue() == protocollo
						.getProtocollo().getCaricaProtocollatoreId())|| protocollo.getMittente().getUtenteAssegnatarioId()==utente.getValueObject().getId()) {
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
}
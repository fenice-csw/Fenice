package it.compit.fenice.mvc.presentation.action.soggetto;

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.AlberoUfficiBO;
import it.finsiel.siged.mvc.business.SoggettoDelegate;
import it.finsiel.siged.mvc.presentation.actionform.soggetto.ListaDistribuzioneForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.ListaDistribuzioneVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class ListaDistribuzioneInternaAction extends Action{

    static Logger logger = Logger.getLogger(ListaDistribuzioneInternaAction.class
            .getName());

    protected void assegnaAdUfficio(ListaDistribuzioneForm form, int ufficioId) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(ufficioId);
		Organizzazione org = Organizzazione.getInstance();
		Ufficio uff = org.getUfficio(ufficioId);
		ass.setNomeUfficio(uff.getValueObject().getDescription());
		ass.setDescrizioneUfficio(uff.getPath());
		form.aggiungiAssegnatario(ass);
	}

	protected void assegnaAdUtente(ListaDistribuzioneForm form) {
		AssegnatarioView ass = new AssegnatarioView();
		ass.setUfficioId(form.getUfficioCorrenteId());
		ass.setNomeUfficio(form.getUfficioCorrente().getDescription());
		ass.setDescrizioneUfficio(form.getUfficioCorrentePath());
		ass.setUtenteId(form.getUtenteSelezionatoId());
		UtenteVO ute = form.getUtente(form.getUtenteSelezionatoId());
		ass.setNomeUtente(ute.getCaricaFullName());
		form.aggiungiAssegnatario(ass);
		
	}
    
	private void rimuoviAssegnatari(ListaDistribuzioneForm form) {
		String[] assegnatari = form.getAssegnatariSelezionatiId();
		if (assegnatari != null) {
			for (int i = 0; i < assegnatari.length; i++) {
				String assegnatario = assegnatari[i];
				if (assegnatario != null) {
					form.rimuoviAssegnatario(assegnatario);
				}
			}
		}
	}
	
    // --------------------------------------------------------- Public Methods
    public ActionForward execute(ActionMapping mapping, ActionForm ldForm,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ActionMessages errors = new ActionMessages();
        SoggettoDelegate delegate = SoggettoDelegate.getInstance();
        HttpSession session = request.getSession();
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        ListaDistribuzioneVO listaDistribuzione = new ListaDistribuzioneVO(utente.getAreaOrganizzativa().getId().intValue());
        ListaDistribuzioneForm listaDistribuzioneForm = (ListaDistribuzioneForm) ldForm;
        if (ldForm == null) {
            listaDistribuzioneForm = new ListaDistribuzioneForm();
            session.setAttribute(mapping.getAttribute(),listaDistribuzioneForm);
        }if (listaDistribuzioneForm.getUfficioCorrenteId() == 0) {
        	listaDistribuzioneForm.setUfficioCorrenteId(utente.getUfficioInUso());
			AlberoUfficiBO.impostaUfficioUtenti(utente, listaDistribuzioneForm, true);
		}
		if (request.getParameter("assegnaUfficioAction") != null) {
			assegnaAdUfficio(listaDistribuzioneForm, listaDistribuzioneForm.getUfficioCorrenteId());
			
		} else if (request.getParameter("impostaUfficioAction") != null) {
			listaDistribuzioneForm.setUfficioCorrenteId(listaDistribuzioneForm
					.getUfficioSelezionatoId());
			AlberoUfficiBO.impostaUfficioUtenti(utente, listaDistribuzioneForm,
					true);
		} else if (request.getParameter("ufficioPrecedenteAction") != null) {
			AlberoUfficiBO.impostaUfficioUtenti(utente, listaDistribuzioneForm,
					true);
			listaDistribuzioneForm.setUfficioCorrenteId(listaDistribuzioneForm.getUfficioCorrente()
					.getParentId());
		} else if (request.getParameter("assegnaUtenteAction") != null) {
			assegnaAdUtente(listaDistribuzioneForm);
		} else if (request.getParameter("rimuoviAssegnatariAction") != null) {
			rimuoviAssegnatari(listaDistribuzioneForm);
		}
        else if (request.getParameter("annullaAction") != null) {
            if ("true".equals(request.getParameter("annullaAction"))
                    || listaDistribuzioneForm.getCodice() == 0) {
                session.removeAttribute("tornaProtocollo");
                listaDistribuzioneForm.inizializzaForm();
            }
            if (Boolean.TRUE.equals(session.getAttribute("tornaProtocollo"))) {
                session.removeAttribute("tornaProtocollo");
                return (mapping.findForward("tornaProtocollo"));
            }
            return mapping.findForward("input");
        }

        if (request.getParameter("cercaAction") != null) {
            String desc = listaDistribuzioneForm.getDescrizione();
            ArrayList<IdentityVO> elenco = null;
            if (desc == null || "".equals(desc)) {
                // mostra tutto
                elenco = delegate.getElencoListeDistribuzioneInterna();
            } else {
                // mostra con filtro su descrizione
                elenco = delegate.getElencoListaDistribuzioneInterna(desc, utente
                        .getAreaOrganizzativa().getId().intValue());
            }
            if (elenco != null) {
                listaDistribuzioneForm.setElencoListaDistribuzione(elenco);

            } else {
                listaDistribuzioneForm
                        .setElencoListaDistribuzione(new ArrayList<IdentityVO>());

            }
            session
                    .setAttribute(mapping.getAttribute(),
                            listaDistribuzioneForm);
            return (mapping.findForward("input"));
        }else if (request.getParameter("nuovaAction") != null) {
    			return mapping.findForward("nuova");
        } else if (request.getParameter("cercaDaProtocolloAction") != null
                || request.getAttribute("nomeLista") != null) {
            String desc = listaDistribuzioneForm.getDescrizione();
            String descLista = (String) request.getAttribute("nomeLista");
            if (descLista != null)
                desc = descLista;
            ArrayList<IdentityVO> elenco = null;
            elenco = delegate.getElencoListaDistribuzioneInterna(desc, utente
                    .getAreaOrganizzativa().getId().intValue());
            if (elenco != null) {
                listaDistribuzioneForm.setElencoListaDistribuzione(elenco);
            } else {
                listaDistribuzioneForm
                        .setElencoListaDistribuzione(new ArrayList<IdentityVO>());
            }
            listaDistribuzioneForm.setDescrizione("");
            session.setAttribute(mapping.getAttribute(),listaDistribuzioneForm);
            return (mapping.findForward("inputTornaProtocollo"));
        } else if (request.getParameter("indietroLD") != null) {
            {
                if (session.getAttribute("provenienza").equals("listaDistribuzioneProtocollo"))
                    return (mapping.findForward("tornaProtocollo"));
            }
        }
        else if (request.getParameter("salvaAction") != null) {
            readLdForm(listaDistribuzione, listaDistribuzioneForm);
            errors = listaDistribuzioneForm.validateDatiInserimento(mapping,
                    request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (mapping.findForward("input"));
            }

            ListaDistribuzioneVO listaSalvata = delegate.salvaListaDistribuzioneInterna(listaDistribuzione, utente,listaDistribuzione.getAssegnatari());
           
            if (listaSalvata.getReturnValue() != ReturnValues.SAVED
                    && listaSalvata.getReturnValue() != ReturnValues.EXIST_DESCRIPTION) {
                errors.add("registrazione_tipo", new ActionMessage(
                        "record_non_inseribile", "la lista di distribuzione,",
                        " esiste gi� con lo stesso nome "));
                saveErrors(request, errors);
                return (mapping.findForward("input"));

            } else {
                request.setAttribute("operazioneEffettuata", "true");

            }
            listaDistribuzioneForm.setCodice(listaSalvata.getId().intValue());
            return (mapping.findForward("input"));
        }

        else if (request.getParameter("parId") != null) {
            int parId = (new Integer(request.getParameter("parId"))).intValue();
            listaDistribuzione = delegate.getListaDistribuzione(parId);
            Collection<AssegnatarioVO> assegnatari = null;
            if (listaDistribuzione != null) {
                assegnatari = delegate.getAssegnatariListaDistribuzione(listaDistribuzione.getId().intValue());
            }
            writeLdForm(listaDistribuzioneForm, listaDistribuzione, assegnatari);
            return (mapping.findForward("input"));
        } 
        else if (request.getParameter("annulla") != null) {
            session.removeAttribute("tornaProtocollo");
            session.removeAttribute("tornaInvioFascicolo");
        } else if (request.getParameter("nuova") != null) {
            return (mapping.findForward("nuova"));

        } else if (listaDistribuzioneForm.getDeleteAction() != null) {
            
            delegate.cancellaListaDistribuzione(listaDistribuzioneForm
                    .getCodice());
            listaDistribuzioneForm.inizializzaForm();
            return (mapping.findForward("cerca"));
        } else if (request.getParameter("annulla") != null) {
            session.removeAttribute("tornaProtocollo");
            session.removeAttribute("tornaInvioFascicolo");
        } 
        return (mapping.findForward("input"));
    }

    public static void readLdForm(ListaDistribuzioneVO listaDistribuzione,
            ListaDistribuzioneForm ldForm) {
        listaDistribuzione.setId(ldForm.getCodice());
        listaDistribuzione.setDescription(ldForm.getDescrizione());
        aggiornaAssegnatariModel(ldForm, listaDistribuzione);
    }

    private static void aggiornaAssegnatariModel(ListaDistribuzioneForm form,
    		ListaDistribuzioneVO vo) {
		vo.removeCaricheAssegnatari();
		Collection<AssegnatarioView> assegnatari = form.getAssegnatari();
		if (assegnatari != null) {				
			for (AssegnatarioView ass : assegnatari) {
			AssegnatarioVO assegnatario = new AssegnatarioVO();
			assegnatario.setUfficioAssegnatarioId(ass.getUfficioId());
			if (ass.getUtenteId() != 0){
				CaricaVO carica = CaricaDelegate.getInstance().getCaricaByUtenteAndUfficio(ass.getUtenteId(),ass.getUfficioId());
				assegnatario.setCaricaAssegnatarioId(carica.getCaricaId());
			}
			vo.aggiungiAssegnatario(assegnatario);
			}
			
		}
	}
    
    public void caricaDatiNelVO(ListaDistribuzioneVO vo,
            ListaDistribuzioneForm ldForm, Utente utente) {
        vo.setId(ldForm.getCodice());
        vo.setDescription(ldForm.getDescrizione());    
    }
	
    public static void writeLdForm(ListaDistribuzioneForm ldForm,
            ListaDistribuzioneVO listaDistribuzione, Collection<AssegnatarioVO> assegnatari){
		Organizzazione org = Organizzazione.getInstance();
		 ldForm.setCodice(listaDistribuzione.getId().intValue());
	     ldForm.setDescrizione(listaDistribuzione.getDescription());
		for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i.hasNext();) {
				AssegnatarioVO assegnatario = (AssegnatarioVO) i.next();
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
			ldForm.aggiungiAssegnatario(ass);
			
		}

	}

}

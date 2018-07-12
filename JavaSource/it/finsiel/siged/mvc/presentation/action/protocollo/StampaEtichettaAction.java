package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.compit.fenice.enums.ReturnPageEnum;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.ConfigurazioneUtenteDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.StampaEtichettaForm;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.protocollo.ConfigurazioneUtenteVO;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

public class StampaEtichettaAction extends Action {

    static Logger logger = Logger.getLogger(StampaEtichettaAction.class
            .getName());

    final String IN_ALTO_DX = "In alto a destra";

    final String IN_ALTO_SX = "In alto a sinistra";

    final String IN_BASSO_DX = "In basso a destra";

    final String IN_BASSO_SX = "In basso a sinistra";

    final String STAMPA_FOGLIO_A4 = "S";
   

    private static void getInputPage(HttpServletRequest request, StampaEtichettaForm form) {
    	if (request.getParameter("DOCVIEW_IN") != null)
			form.setInputPage(ReturnPageEnum.DOCVIEW_IN);
		else if (request.getParameter("DOCVIEW_OUT") != null)
			form.setInputPage(ReturnPageEnum.DOCVIEW_OUT);
		else if (request.getParameter("DOCVIEW_PI") != null)
			form.setInputPage(ReturnPageEnum.DOCVIEW_PI);
    	
		else if (request.getParameter("DOC_IN") != null)
			form.setInputPage(ReturnPageEnum.DOC_IN);
		else if (request.getParameter("DOC_OUT") != null)
			form.setInputPage(ReturnPageEnum.DOC_OUT);
		else if (request.getParameter("DOC_PI") != null)
			form.setInputPage(ReturnPageEnum.DOC_PI);
		
		else if (request.getParameter("ATTACH_IN") != null)
			form.setInputPage(ReturnPageEnum.ATTACH_IN);
		else if (request.getParameter("ATTACH_OUT") != null)
			form.setInputPage(ReturnPageEnum.ATTACH_OUT);
		else if (request.getParameter("ATTACH_PI") != null)
			form.setInputPage(ReturnPageEnum.ATTACH_PI);
	}

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession(true);
        StampaEtichettaForm etichettaForm = (StampaEtichettaForm) form;
        ActionMessages errors = new ActionMessages();
        IdentityVO identityVO;
        Collection<IdentityVO> modalitaStampaA4 = new ArrayList<IdentityVO>();
        identityVO = new IdentityVO("0", IN_ALTO_DX);
        modalitaStampaA4.add(identityVO);
        identityVO = new IdentityVO("1", IN_ALTO_SX);
        modalitaStampaA4.add(identityVO);
        identityVO = new IdentityVO("2", IN_BASSO_DX);
        modalitaStampaA4.add(identityVO);
        identityVO = new IdentityVO("3", IN_BASSO_SX);
        modalitaStampaA4.add(identityVO);
        etichettaForm.setModalitaStampaA4(modalitaStampaA4);
        getInputPage(request, etichettaForm);
        if (request.getParameter("btnImpostaParametriStampa") != null) {
            errors = etichettaForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            } else {
                impostaOffSet(request, etichettaForm);
            }
        } else if (request.getParameter("btnSalvaConfigurazione") != null) {
            ConfigurazioneUtenteDelegate cd = ConfigurazioneUtenteDelegate
                    .getInstance();
            Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
            int caricaId = utente.getCaricaInUso();
            errors = etichettaForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            } else {
                ConfigurazioneUtenteVO configurazioneUtenteVO = cd
                        .getConfigurazione(caricaId);
                impostaOffSet(request, etichettaForm);
                if (configurazioneUtenteVO != null
                        && configurazioneUtenteVO.getReturnValue() == ReturnValues.FOUND) {
                    impostaParametriStampaVO(configurazioneUtenteVO,
                            etichettaForm);
                    configurazioneUtenteVO = cd
                            .aggiornaParametriStampante(configurazioneUtenteVO);
                } else {
                    impostaConfigurazioneVO(configurazioneUtenteVO,
                            etichettaForm);
                    configurazioneUtenteVO = cd.salvaConfigurazione(
                            configurazioneUtenteVO, caricaId);
                }

                if (configurazioneUtenteVO.getReturnValue() == ReturnValues.FOUND) {
                    errors.add("CONFIGURAZIONE_UTENTE", new ActionMessage(
                            "operazione_ok", "", ""));
                } else {
                    errors.add("CONFIGURAZIONE_UTENTE", new ActionMessage(
                            "errore_nel_salvataggio", "", ""));
                }

                session.setAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO",
                        configurazioneUtenteVO);

            }
        } else if (request.getParameter("btnAnnullaStampa") != null) {
            if (etichettaForm.getInputPage()==ReturnPageEnum.DOCVIEW_IN) {
                return (mapping.findForward("visualizzaProtocolloIngresso"));
            }else if (etichettaForm.getInputPage()==ReturnPageEnum.DOCVIEW_OUT) {
                return (mapping.findForward("visualizzaProtocolloUscita"));
            }else if (etichettaForm.getInputPage()==ReturnPageEnum.DOCVIEW_PI) {
                return (mapping.findForward("visualizzaPostaInterna"));
            }else if (etichettaForm.getInputPage()==ReturnPageEnum.ATTACH_IN) {
                return (mapping.findForward("allegaProtocolloIngresso"));
            }else if (etichettaForm.getInputPage()==ReturnPageEnum.ATTACH_OUT) {
                return (mapping.findForward("allegaProtocolloUscita"));
            }else if (etichettaForm.getInputPage()==ReturnPageEnum.ATTACH_PI) {
            	return (mapping.findForward("allegaPostaInterna"));
            }else if (etichettaForm.getInputPage()==ReturnPageEnum.DOC_IN) {
                return (mapping.findForward("modificaProtocolloIngresso"));
            }else if (etichettaForm.getInputPage()==ReturnPageEnum.DOC_OUT) {
                return (mapping.findForward("modificaProtocolloUscita"));
            }else if (etichettaForm.getInputPage()==ReturnPageEnum.DOC_PI) {
                return (mapping.findForward("modificaPostaInterna"));
            }
            

        }
        
        impostaForm(etichettaForm, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        logger.info("Execute StampaEtichettaAction");
        return (mapping.findForward("input"));

    }



    private void impostaForm(StampaEtichettaForm etichettaForm,
            HttpServletRequest request) {

        HttpSession session = request.getSession(true);
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        ConfigurazioneUtenteVO configurazioneVO = null;

        if (session.getAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO") == null) {
            configurazioneVO = ConfigurazioneUtenteDelegate.getInstance()
                    .getConfigurazione(
                            utente.getCaricaInUso());
            session.setAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO",
                    configurazioneVO);
        } else {
            configurazioneVO = (ConfigurazioneUtenteVO) session
                    .getAttribute("CONFIGURAZIONE_UTENTE_PROTOCOLLO");
        }

        if (configurazioneVO != null) {
            String parametriStampante = configurazioneVO
                    .getParametriStampante();
            impostaParametriStampante(request, etichettaForm,
                    parametriStampante);
        } else {

        }

    }


    private void impostaParametriStampante(HttpServletRequest request,
            StampaEtichettaForm etichettaForm, String parametri) {

        if (parametri != null) {
            String[] strings = parametri.split(";");
            if (strings != null && strings.length == 9) {
                etichettaForm.setMargineSinistro(strings[0]);
                etichettaForm.setMargineSuperiore(strings[1]);
                etichettaForm.setLarghezzaEtichetta(strings[2]);
                etichettaForm.setAltezzaEtichetta(strings[3]);
                etichettaForm.setTipoStampa(strings[4]);
                etichettaForm.setModoStampaA4(strings[5]);
                etichettaForm.setRotazione(strings[6]);
                etichettaForm.setDeltaXMM(Integer.parseInt(strings[7]));
                etichettaForm.setDeltaYMM(Integer.parseInt(strings[8]));
            }
        } else {
            MessageResources bundle = (MessageResources) request
                    .getAttribute(Globals.MESSAGES_KEY);

            etichettaForm.setModoStampaA4("1");
            etichettaForm.setTipoStampa("N");
            etichettaForm.setAltezzaEtichetta(bundle
                    .getMessage("protocollo.stampa.etichette.altezza"));
            etichettaForm.setLarghezzaEtichetta(bundle
                    .getMessage("protocollo.stampa.etichette.larghezza"));
            etichettaForm.setMargineSinistro(bundle
                    .getMessage("protocollo.stampa.etichette.margine.sx"));
            etichettaForm
                    .setMargineSuperiore(bundle
                            .getMessage("protocollo.stampa.etichette.margine.superiore"));
            impostaOffSet(request, etichettaForm);
        }
    }

    public void impostaConfigurazioneVO(ConfigurazioneUtenteVO vo,
            StampaEtichettaForm form) {
        vo.setOggetto(null);
        vo.setDataDocumento(null);
        vo.setDestinatario(null);
        vo.setMittente(null);
        vo.setTipoDocumentoId(0);
        vo.setTipoDocumentoId(0);
        vo.setTipoMittente("F");
        vo.setTitolario(0);
        vo.setTitolarioId(0);
        vo.setCheckAssegnatari(null);
        vo.setCheckDataDocumento(null);
        vo.setCheckDestinatari(null);
        vo.setCheckMittente(null);
        vo.setCheckOggetto(null);
        vo.setCheckRicevutoIl(null);
        vo.setCheckTipoDocumento(null);
        vo.setCheckTipoMittente(null);
        vo.setCheckTitolario(null);
        impostaParametriStampaVO(vo, form);

    }

    private void impostaOffSet(HttpServletRequest request,
            StampaEtichettaForm etichettaForm) {
        MessageResources bundle = (MessageResources) request
                .getAttribute(Globals.MESSAGES_KEY);

        if (STAMPA_FOGLIO_A4.equals(etichettaForm.getTipoStampa())) {
            if (etichettaForm.getModoStampaA4().equals("0")) {
                etichettaForm.setDeltaXMM(Integer.parseInt((String) bundle
                        .getMessage("protocollo.stampa.etichette.OFFSET.X")
                        .trim()));
                etichettaForm.setDeltaYMM(Integer.parseInt((String) bundle
                        .getMessage("protocollo.stampa.etichette.OFFSET")
                        .trim()));
                // in alto a sx
            } else if (etichettaForm.getModoStampaA4().equals("1")) {
                etichettaForm.setDeltaXMM(Integer.parseInt((String) bundle
                        .getMessage("protocollo.stampa.etichette.OFFSET")
                        .trim()));
                etichettaForm.setDeltaYMM(Integer.parseInt((String) bundle
                        .getMessage("protocollo.stampa.etichette.OFFSET")
                        .trim()));
                // in basso a dx
            } else if (etichettaForm.getModoStampaA4().equals("2")) {
                etichettaForm.setDeltaXMM(Integer.parseInt((String) bundle
                        .getMessage("protocollo.stampa.etichette.OFFSET.X")
                        .trim()));
                etichettaForm.setDeltaYMM(Integer.parseInt((String) bundle
                        .getMessage("protocollo.stampa.etichette.OFFSET.Y")
                        .trim()));

                // in basso a sx
            } else if (etichettaForm.getModoStampaA4().equals("3")) {
                etichettaForm.setDeltaXMM(Integer.parseInt((String) bundle
                        .getMessage("protocollo.stampa.etichette.OFFSET")
                        .trim()));
                etichettaForm.setDeltaYMM(Integer.parseInt((String) bundle
                        .getMessage("protocollo.stampa.etichette.OFFSET.Y")
                        .trim()));
            }
        } else {
            etichettaForm.setDeltaXMM(0);
            etichettaForm.setDeltaYMM(0);
        }

    }

    public void impostaParametriStampaVO(
            ConfigurazioneUtenteVO configurazioneVO, StampaEtichettaForm form) {
        StringBuffer parametriStampante = new StringBuffer("");
        parametriStampante.append(form.getMargineSinistro()).append(";");
        parametriStampante.append(form.getMargineSuperiore()).append(";");
        parametriStampante.append(form.getLarghezzaEtichetta()).append(";");
        parametriStampante.append(form.getAltezzaEtichetta()).append(";");
        parametriStampante.append(form.getTipoStampa()).append(";");
        parametriStampante.append(form.getModoStampaA4()).append(";");
        parametriStampante.append(form.getRotazione()).append(";");
        parametriStampante.append(form.getDeltaXMM()).append(";");
        parametriStampante.append(form.getDeltaYMM());

        configurazioneVO.setParametriStampante(parametriStampante.toString());

    }

}

package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.Menu;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.bo.MenuBO;
import it.finsiel.siged.mvc.business.AmministrazioneDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.ProfiloForm;
import it.finsiel.siged.mvc.presentation.helper.MenuView;
import it.finsiel.siged.mvc.vo.lookup.ProfiloVO;
import it.finsiel.siged.mvc.vo.organizzazione.MenuVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public class ProfiloAction extends Action {

    // ----------------------------------------------------- Instance Variables

    /**
     * The <code>Log</code> instance for this application.
     */
    static Logger logger = Logger.getLogger(ProfiloAction.class.getName());

    // --------------------------------------------------------- Public Methods

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     * 
     * @param mapping
     *            The ActionMapping used to select this instance
     * @param form
     *            The optional ActionForm bean for this request (if any)
     * @param request
     *            The HTTP request we are processing
     * @param response
     *            The HTTP response we are creating
     * 
     * @exception Exception
     *                if business logic throws an exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionMessages errors = new ActionMessages();// Report any errors we
        ActionMessages messages = new ActionMessages();
        HttpSession session = request.getSession(true); // we create one if does
        AmministrazioneDelegate delegate = AmministrazioneDelegate
                .getInstance();
        ProfiloForm profiloForm = (ProfiloForm) form;
        ProfiloVO profiloVO = new ProfiloVO();
        Utente utente = (Utente) request.getSession().getAttribute(
                Constants.UTENTE_KEY);
        if (form == null) {
            logger.info(" Creating new ProfiloAction");
            form = new ProfiloForm();
            session.setAttribute(mapping.getAttribute(), form);
        }
        if (profiloForm.getFunzioniMenu() == null) {
            Collection<MenuView> menuLista = new ArrayList<MenuView>();
            Menu rootMenu = (Menu) getServlet().getServletContext()
                    .getAttribute(Constants.MENU_ROOT);
            MenuBO.aggiungiMenu("", rootMenu, menuLista);
            profiloForm.setFunzioniMenu(menuLista);
        }
        profiloForm.setNuovo(null);
        if (request.getParameter("btnNuovo") != null) {
            profiloForm.setNuovo("true");
            profiloForm.inizializzaForm(utente.getRegistroVOInUso().getAooId());
            return (mapping.findForward("input"));
        } else if (request.getParameter("btnAnnulla") != null) {
            profiloForm.inizializzaForm(utente.getRegistroVOInUso().getAooId());
            return (mapping.findForward("input"));
        } else if (request.getParameter("btnConferma") != null) {
            errors = profiloForm.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return (mapping.findForward("input"));
            }
            aggiornaModel(profiloForm, profiloVO, utente, request);
            delegate.salvaProfilo(profiloVO,utente);
            profiloForm.setProfili(delegate.getProfili(utente
                    .getRegistroVOInUso().getAooId()));
            if (profiloVO.getReturnValue() == ReturnValues.SAVED) {
                // operazione avvenuta con successo
                request.setAttribute("profiloId", profiloVO.getId());
                aggiornaForm(profiloForm, profiloVO);
                //
                messages.add("profiloRegistrato", new ActionMessage("profilo_registrato"));
    			saveMessages(request, messages);

                //errors.add("profiloRegistrato", new ActionMessage("profilo_registrato"));
                
                //
            } else {
                errors.add("profiloRegistrato", new ActionMessage(
                        "errore_nel_salvataggio"));
            }
            saveErrors(request, errors);
            return (mapping.findForward("input"));
        } else if (request.getParameter("assegnaProtocollazione") != null) {
        	ArrayList<MenuVO> funzioni = AmministrazioneDelegate.getInstance().getFunzioniProtocollazioneMenu();
        	aggiornaModel(profiloForm, profiloVO, utente, request);
        	addPermessiToForm(funzioni, profiloForm);
        	profiloForm.setProfili(delegate.getProfili(utente
                    .getRegistroVOInUso().getAooId()));
        	 profiloForm.setId(profiloVO.getId().intValue());
             profiloForm.setCodiceProfilo(profiloVO.getCodProfilo());
             profiloForm.setDescrizioneProfilo(profiloVO.getDescrizioneProfilo());
            profiloForm.setNuovo("true");           
            return (mapping.findForward("input"));            
        }else if (request.getParameter("assegnaAmministrazione") != null) {
        	ArrayList<MenuVO> funzioni = AmministrazioneDelegate.getInstance().getFunzioniAmministrazioneMenu();
        	aggiornaModel(profiloForm, profiloVO, utente, request);
        	addPermessiToForm(funzioni, profiloForm);
        	profiloForm.setProfili(delegate.getProfili(utente
                    .getRegistroVOInUso().getAooId()));
        	 profiloForm.setId(profiloVO.getId().intValue());
             profiloForm.setCodiceProfilo(profiloVO.getCodProfilo());
             profiloForm.setDescrizioneProfilo(profiloVO.getDescrizioneProfilo());
            profiloForm.setNuovo("true");           
            return (mapping.findForward("input"));           
        }else if (request.getParameter("assegnaDocumentale") != null) {
        	ArrayList<MenuVO> funzioni = AmministrazioneDelegate.getInstance().getFunzioniDocumentaleMenu();
        	aggiornaModel(profiloForm, profiloVO, utente, request);
        	addPermessiToForm(funzioni, profiloForm);
        	profiloForm.setProfili(delegate.getProfili(utente
                    .getRegistroVOInUso().getAooId()));
        	 profiloForm.setId(profiloVO.getId().intValue());
             profiloForm.setCodiceProfilo(profiloVO.getCodProfilo());
             profiloForm.setDescrizioneProfilo(profiloVO.getDescrizioneProfilo());
            profiloForm.setNuovo("true");
            return (mapping.findForward("input"));   
        }else if (request.getParameter("assegnaReport") != null) {
        	ArrayList<MenuVO> funzioni = AmministrazioneDelegate.getInstance().getFunzioniReportMenu();
        	aggiornaModel(profiloForm, profiloVO, utente, request);
        	addPermessiToForm(funzioni, profiloForm);
        	profiloForm.setProfili(delegate.getProfili(utente
                    .getRegistroVOInUso().getAooId()));
        	 profiloForm.setId(profiloVO.getId().intValue());
             profiloForm.setCodiceProfilo(profiloVO.getCodProfilo());
             profiloForm.setDescrizioneProfilo(profiloVO.getDescrizioneProfilo());
            profiloForm.setNuovo("true");
            return (mapping.findForward("input"));
            
        } else if (request.getParameter("btnCancella") != null) {
            if (profiloForm.getId() > 0) {
                delegate.cancellaProfilo(profiloForm.getId());
                errors.add("profiloCancellato", new ActionMessage(
                        "profilo_cancellato"));
                saveErrors(request, errors);
            }
            profiloForm.inizializzaForm(utente.getRegistroVOInUso().getAooId());
            profiloForm.setProfili(delegate.getProfili(utente
                    .getRegistroVOInUso().getAooId()));
            return (mapping.findForward("input"));
        } else if (request.getParameter("parId") != null) {
            profiloVO = delegate.getProfilo(Integer.parseInt(request
                    .getParameter("parId")));
            aggiornaForm(profiloForm, profiloVO);
            return (mapping.findForward("input"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return (mapping.findForward("input"));
        }
        logger.info("Execute ProfiloAction");
        profiloForm.inizializzaForm(utente.getRegistroVOInUso().getAooId());
        return (mapping.findForward("input")); // 
    }

    private void addPermessiToForm(ArrayList<MenuVO> funzioni, ProfiloForm profiloForm) {
		Set<String> permessiSet = new HashSet<String>();
    	ArrayList<String> toAdd = new ArrayList<String>();
    	for (Iterator<MenuVO> iter = funzioni.iterator(); iter.hasNext();) {
            MenuVO element =  iter.next();
            toAdd.add((element.getId()).toString());
        }
		for(String elemento : profiloForm.getProfiliMenu()){
    		permessiSet.add(elemento);
    	}
    	permessiSet.addAll(toAdd);
    	String[] permessiNew = new String[permessiSet.size()];
    	permessiSet.toArray(permessiNew);
    	profiloForm.setProfiliMenu(permessiNew);
	}

	private void aggiornaForm(ProfiloForm profiloForm, ProfiloVO profiloVO) {
        profiloForm.setId(profiloVO.getId().intValue());
        profiloForm.setCodiceProfilo(profiloVO.getCodProfilo());
        profiloForm.setDescrizioneProfilo(profiloVO.getDescrizioneProfilo());
        profiloForm.setProfiliMenu(profiloVO.getMenuProfili());
    }

    private void aggiornaModel(ProfiloForm profiloForm, ProfiloVO profiloVO,
            Utente utente, HttpServletRequest request) {
        profiloVO.setId(profiloForm.getId());
        profiloVO.setAooId(utente.getRegistroVOInUso().getAooId());
        profiloVO.setCodProfilo(profiloForm.getCodiceProfilo());
        profiloVO.setDescrizioneProfilo(profiloForm.getDescrizioneProfilo());
        if (profiloForm.getId() == 0) {
            profiloVO.setRowCreatedUser(utente.getValueObject().getUsername());
        }
        profiloVO.setRowUpdatedUser(utente.getValueObject().getUsername());
        if (request.getParameterValues("profiliMenu") != null) {
            profiloVO.setMenuProfili(request.getParameterValues("profiliMenu"));
        }

    }

}

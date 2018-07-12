package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.business.StoriaProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.StoriaProtocolloForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;

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
import org.apache.struts.action.ActionMessages;

public class StoriaProtocolloAction extends Action {

	// ----------------------------------------------------- Instance Variables

	/**
	 * The <code>Log</code> instance for this application.
	 */
	static Logger logger = Logger.getLogger(StoriaProtocolloAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {    	
		ActionMessages errors = new ActionMessages();// Report any errors we
		HttpSession session = request.getSession();
		StoriaProtocolloForm storiaProtocolloForm = (StoriaProtocolloForm) form;
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		if (form == null) {
			logger.info(" Creating new StoriaProtocolloAction");
			form = new StoriaProtocolloForm();
			session.setAttribute(mapping.getAttribute(), form);
		}
		if (request.getAttribute("protocolloId") != null) {
			ProtocolloForm pF = new ProtocolloForm();
			pF = (ProtocolloForm) session.getAttribute("protocolloForm");
			if (!"U".equals(pF.getFlagTipo())) {
				Collection<AssegnatarioVO> listAssegnatari = ProtocolloDelegate.getInstance().getAssegnatariProtocollo(pF.getProtocolloId());
				impostaAssegnatari(listAssegnatari, storiaProtocolloForm);
				
			}
			ProtocolloVO protVO = ProtocolloDelegate.getInstance().getProtocolloVOById(pF.getProtocolloId());
			storiaProtocolloForm.setVersione(protVO.getVersione());
			storiaProtocolloForm.setProtocolloId(pF.getProtocolloId());
			storiaProtocolloForm.setFlagTipo(pF.getFlagTipo());
			if(protVO.getFlagTipoMittente()!=null){
				if (protVO.getFlagTipoMittente().equals("F")) {
					storiaProtocolloForm.setCognomeMittente(protVO.getCognomeMittente()+ " "+ it.finsiel.siged.util.StringUtil.getStringa(protVO.getNomeMittente()));
				} else if (protVO.getFlagTipoMittente().equals("G")) {
					storiaProtocolloForm.setCognomeMittente(protVO.getDenominazioneMittente());
				}
			}
			storiaProtocolloForm.setDocumentoId(pF.getDocumentoId());
			storiaProtocolloForm.setNumeroProtocollo(pF.getNumeroProtocollo());
			storiaProtocolloForm.setDataRegistrazione(DateUtil.formattaDataOra(protVO.getRowCreatedTime().getTime()));
			storiaProtocolloForm.setOggetto(pF.getOggetto());
			storiaProtocolloForm.setStato(pF.getStato());
			storiaProtocolloForm.setUfficioProtocollatoreId(pF.getUfficioProtocollatoreId());
			storiaProtocolloForm.setUtenteProtocollatoreId(pF.getUtenteProtocollatoreId());
			storiaProtocolloForm.setUserUpdate(protVO.getRowUpdatedUser());
			int protocolloId = Integer.parseInt(request.getAttribute("protocolloId").toString());
			storiaProtocolloForm.setScartato(StoriaProtocolloDelegate.getInstance().isScartato(protocolloId));
			storiaProtocolloForm.setEstremiAutorizzazione(pF.getEstremiAutorizzazione());
			storiaProtocolloForm.setVersioniProtocollo(StoriaProtocolloDelegate.getInstance().getStoriaProtocollo(utente, protocolloId, pF.getFlagTipo()));
		} else if (request.getParameter("versioneSelezionata") != null) {
			ProtocolloForm pF = new ProtocolloForm();
			pF = (ProtocolloForm) session.getAttribute("protocolloForm");
			pF.setModificabile(false);
			request.setAttribute("protocolloId", new Integer(pF.getProtocolloId()));
			request.setAttribute("versioneId", new Integer(request.getParameter("versioneSelezionata")));
			if ("I".equals(pF.getFlagTipo())) {
				return (mapping.findForward("visualizzaProtocolloIngresso"));
			} else if ("U".equals(pF.getFlagTipo())) {
				return (mapping.findForward("visualizzaProtocolloUscita"));
			} else {
				return (mapping.findForward("visualizzaPostaInterna"));
			}
		} else if (request.getParameter("versioneCorrente") != null) {
			ProtocolloForm pF = new ProtocolloForm();
			pF = (ProtocolloForm) session.getAttribute("protocolloForm");
			pF.setModificabile(false);
			request.setAttribute("protocolloId", new Integer(pF
					.getProtocolloId()));
			if ("I".equals(pF.getFlagTipo())) {
				return (mapping.findForward("visualizzaProtocolloIngresso"));
			} else if ("U".equals(pF.getFlagTipo())) {
				return (mapping.findForward("visualizzaProtocolloUscita"));
			} else {
				return (mapping.findForward("visualizzaPostaInterna"));
			}

		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		logger.info("Execute StoriaProtocolloAction");

		return mapping.getInputForward();
	}

	private void impostaAssegnatari(Collection<AssegnatarioVO> assegnatari,
			StoriaProtocolloForm form) {
		form.initAssegnatari();
		//Organizzazione org = Organizzazione.getInstance();
		for (Iterator<AssegnatarioVO> i = assegnatari.iterator(); i.hasNext();) {
		AssegnatarioVO assegnatario = (AssegnatarioVO) i.next();
		AssegnatarioView ass=newAssegnatario(assegnatario.getUfficioAssegnatarioId(), assegnatario.getCaricaAssegnatarioId());
		if (assegnatario.isCompetente()) {
			
		}
		form.aggiungiAssegnatario(ass);
		}
	}
	
	  private AssegnatarioView newAssegnatario(int uffId,int caricaId) {
		Organizzazione org = Organizzazione.getInstance();
			AssegnatarioView ass = new AssegnatarioView();
			ass.setUfficioId(uffId);
			Ufficio uff = org.getUfficio(uffId);
			if (uff != null) {
				ass.setNomeUfficio(uff.getValueObject().getDescription());
			}
			if (caricaId != 0) {
				CaricaVO carica = org.getCarica(caricaId);
				ass.setUtenteId(carica.getUtenteId());
				Utente ute = org.getUtente(carica.getUtenteId());
				if (ute != null) {
					ute.getValueObject().setCarica(carica.getNome());
					if (carica.isAttivo())
						ass.setNomeUtente(ute.getValueObject().getCaricaFullName());
					else
						ass.setNomeUtente(ute.getValueObject().getCaricaFullNameNonAttivo());
				} else
					ass.setNomeUtente(carica.getNome());
			}
			return ass;
			
	}
	
}

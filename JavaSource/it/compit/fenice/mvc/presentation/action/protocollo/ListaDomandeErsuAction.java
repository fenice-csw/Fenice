package it.compit.fenice.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.business.DomandaDelegate;
import it.compit.fenice.mvc.presentation.actionform.protocollo.ListaDomandeForm;
import it.compit.fenice.mvc.presentation.helper.DomandaView;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class ListaDomandeErsuAction extends Action {

	static Logger logger = Logger.getLogger(ListaDomandeErsuAction.class
			.getName());

	//private final static int DA_LAVORARE = 1;

	//private final static int ACCETTATO = 3;

	private final static int ELIMINATO = 2;

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession(true);
		DomandaDelegate delegate = DomandaDelegate.getInstance();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		ListaDomandeForm listaForm = (ListaDomandeForm) form;
		String numeroDomanda = "";
		if (request.getParameter("numeroDomanda") != null
				&& !"".equals(request.getParameter("numeroDomanda"))) {
			numeroDomanda = request.getParameter("numeroDomanda");
		}

		if (form == null) {
			logger.info(" Creating new ListaDomandeErsuAction");
			form = new ListaDomandeForm();
			session.setAttribute(mapping.getAttribute(), form);
		}

		if (request.getParameter("impostaStatoAction") != null) {
			listaForm.removeDomande();
			listaForm.setDomande(delegate.getDomande(listaForm.getStatoId(),utente.getUfficioInUso()));
			return (mapping.findForward("input"));
		}

		if (request.getParameter("caricaLista") != null) {
			listaForm.removeDomande();
			listaForm.setDomande(delegate.getDomande(listaForm.getStatoId(),utente.getUfficioInUso()));
			return (mapping.findForward("input"));
		} else if (request.getParameter("btnElimina") != null) {
			String[] domandeDaEliminare = listaForm.getDomandaChkBox();
			for (String i : domandeDaEliminare) {
				DomandaView d = delegate.getDomandaViewById(i);
				if (d != null && d.getStato() == 1)
					delegate.updateStato(ELIMINATO, d.getDomandaId());
			}
			return (mapping.findForward("edit"));
		} else if (request.getParameter("accettaDomanda") != null) {
			request.setAttribute("domandaId", request
					.getParameter("accettaDomanda"));
			return (mapping.findForward("accettaDomanda"));

		} else if (request.getParameter("eliminaDomanda") != null) {
			String nd = (String) request.getParameter("eliminaDomanda");
			DomandaView domanda = delegate.getDomandaViewById(nd);
			if (domanda != null)
				delegate.updateStato(ELIMINATO, domanda.getDomandaId());
			return (mapping.findForward("edit"));
		} else if (request.getParameter("visualizzaProtocollo") != null) {
			String domandaId = request.getParameter("visualizzaProtocollo");
			Integer protocolloId = DomandaDelegate.getInstance().getProtocolloIdByDomandaId(domandaId);
			request.setAttribute("protocolloId", protocolloId);
			request.setAttribute("domandaErsu", true);
			return (mapping.findForward("visualizzaProtocolloIngresso"));
		} else if (request.getParameter("ripetiDomanda") != null) {
			String domandaId = request.getParameter("ripetiDomanda");
			request.setAttribute("domandaId", domandaId);
			Integer protocolloId = DomandaDelegate.getInstance()
					.getProtocolloIdByDomandaId(domandaId);
			request.setAttribute("allaccioId", protocolloId);
			request.setAttribute("domandaErsu", true);
			return (mapping.findForward("accettaDomanda"));

		}

		else if (request.getParameter("cercaDomandaAction") != null) {
			listaForm.setDomanda(null);
			listaForm.setNumeroDomanda(null);
			listaForm.removeDomande();
			if(numeroDomanda!=null && !"".equals(numeroDomanda)){
				DomandaView domanda = delegate.getDomandaViewById(numeroDomanda);
				if (domanda != null) {
					listaForm.addDomanda(domanda);
				}
			}else{
				listaForm.setDomande(delegate.getDomande(listaForm.getStatoId(),utente.getUfficioInUso()));
			}
			return (mapping.findForward("input"));
		}
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
		}
		logger.info("Execute ListaDomandeErsuAction");
		return (mapping.findForward("input"));
	}

}

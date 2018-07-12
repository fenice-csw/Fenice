package it.finsiel.siged.mvc.presentation.action.protocollo;

import it.compit.fenice.mvc.presentation.actionform.protocollo.EmailUscitaForm;
import it.compit.fenice.mvc.presentation.helper.CodaInvioView;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.EmailDelegate;
import it.finsiel.siged.util.DateUtil;

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

public class ListaEmailLogAction extends Action {

	// ----------------------------------------------------- Instance Variables

	static Logger logger = Logger
			.getLogger(ListaEmailLogAction.class.getName());

	// --------------------------------------------------------- Public Methods

	private void aggiornaEmailUscitaForm(CodaInvioView msg, EmailUscitaForm form) {
		form.setMailId(msg.getMailId());
		form.setDestinatari(msg.getDestinatari());
		form.setNumeroProtocollo("" + msg.getNumeroProtocollo());
		form.setDataCreazione(DateUtil.formattaData(msg.getDataCreazione()
				.getTime()));
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		EmailDelegate delegate = EmailDelegate.getInstance();
		int maxRighe=Integer.parseInt(utente.getAreaOrganizzativa().getMaxRighe());
		EmailUscitaForm emailUscitaForm = (EmailUscitaForm) form;
		emailUscitaForm.inizialize();
		
		if (form == null) {
			logger.info(" Creating new ListaEmailLogAction");
			form = new EmailUscitaForm();
			session.setAttribute(mapping.getAttribute(), form);
		} else if (request.getParameter("cercaAction") != null) {
			errors = emailUscitaForm.validate(mapping, request);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			} else {
				int contaRighe = delegate.countListaMessaggiUscita(utente
						.getValueObject().getAooId(), DateUtil
						.toDate(emailUscitaForm.getDataInizio()), DateUtil
						.toDate(emailUscitaForm.getDataFine()), emailUscitaForm
						.getStatoMail());
				if (contaRighe <= maxRighe) {
					if (contaRighe > 0) {
						emailUscitaForm.setMailUscita(delegate
								.getListaMessaggiUscitaView(utente
										.getValueObject().getAooId(),
										DateUtil.toDate(emailUscitaForm
												.getDataInizio()), DateUtil
												.toDate(emailUscitaForm
														.getDataFine()),
										emailUscitaForm.getStatoMail()));
						return (mapping.findForward("input"));
					} else {
						errors.add("nessun_dato", new ActionMessage(
								"nessun_dato"));
						saveErrors(request, errors);
					}

				} else {
					errors.add("controllo.maxrighe", new ActionMessage(
							"controllo.maxrighe", "" + contaRighe,
							"protocolli", "" + maxRighe));
					saveErrors(request, errors);
				}
			}
		} else if (request.getParameter("elimina") != null) {
			int mailId = new Integer(request.getParameter("elimina"));
			boolean cancellata= delegate.eliminaEmailUscita(mailId);
			if(cancellata){
				emailUscitaForm.getMailUscita().remove(mailId);
				if(emailUscitaForm.getMailUscita().size()==0){
					errors.add("nessun_dato", new ActionMessage("nessun_dato"));
					saveErrors(request, errors);
				}
			}
		} else if (request.getParameter("modifica") != null) {
			int mailId = new Integer(request.getParameter("modifica"));
			CodaInvioView msg=emailUscitaForm.getMailUscita().get(mailId);
			aggiornaEmailUscitaForm(msg, emailUscitaForm);
			return (mapping.findForward("modifica"));
		} else if (request.getParameter("aggiornaDestinatari") != null) {
			boolean aggiornata=delegate.aggiornaMessaggioUscita(emailUscitaForm.getMailId(), emailUscitaForm.getDestinatari());
			if(aggiornata){
				CodaInvioView msg = delegate.getMessaggioUscitaView(emailUscitaForm.getMailId());
				emailUscitaForm.getMailUscita().get(emailUscitaForm.getMailId()).setDestinatari(msg.getDestinatari());
			}
			return (mapping.findForward("success"));
		}
		return (mapping.findForward("input"));
	}
}

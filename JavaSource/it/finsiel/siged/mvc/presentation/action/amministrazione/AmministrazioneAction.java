package it.finsiel.siged.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.constant.ReturnValues;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.OrganizzazioneDelegate;
import it.finsiel.siged.mvc.presentation.actionform.amministrazione.AmministrazioneForm;
import it.finsiel.siged.mvc.vo.lookup.ParametriLdapVO;
import it.finsiel.siged.mvc.vo.organizzazione.AmministrazioneVO;
import it.finsiel.siged.util.FileUtil;

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

/**
 * Implementation of <strong>Action </strong> to create a new E-Photo Utente.
 * 
 * @author Almaviva sud.
 * 
 */

public class AmministrazioneAction extends Action {

	static Logger logger = Logger.getLogger(AmministrazioneAction.class
			.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		ActionMessages messages = new ActionMessages();
		HttpSession session = request.getSession();

		AmministrazioneForm amministrazioneForm = (AmministrazioneForm) form;
		String username = ((Utente) session.getAttribute(Constants.UTENTE_KEY))
				.getValueObject().getUsername();
		if (form == null) {
			amministrazioneForm = new AmministrazioneForm();
			request.setAttribute(mapping.getAttribute(), amministrazioneForm);
		}
		Organizzazione org = Organizzazione.getInstance();
		if (request.getParameter("btnSalva") != null) {
			errors = amministrazioneForm.validate(mapping, request);
			if (amministrazioneForm.getPathDocAquisMassiva() != null
					&& !"".equals(amministrazioneForm.getPathDocAquisMassiva())
					&& FileUtil.gestionePathDoc(amministrazioneForm
							.getPathDocAquisMassiva()) == ReturnValues.INVALID) {
				errors.add("amministrazione", new ActionMessage(
						"errore_nel_salvataggio"));
			} else {
				Iterator<AreaOrganizzativa> iterator = org.getAreeOrganizzative().iterator();

				while (iterator.hasNext()) {
					AreaOrganizzativa aoo =  iterator.next();
					if (amministrazioneForm.getPathDocAquisMassiva() != null
							&& !"".equals(amministrazioneForm.getPathDocAquisMassiva())
							&& FileUtil.gestionePathDoc(amministrazioneForm
									.getPathDocAquisMassiva()
									+ "/aoo_"
									+ String.valueOf(aoo.getValueObject()
											.getId())) == ReturnValues.INVALID) {
						errors.add("amministrazione", new ActionMessage(
								"errore_nel_salvataggio"));
						break;
					}

				}
			}

			if (!errors.isEmpty()) {
				saveErrors(request, errors);
				return (mapping.findForward("input"));
			}
			AmministrazioneVO ammVO = new AmministrazioneVO();
			caricaDatiNelVO(ammVO, amministrazioneForm, username);
			OrganizzazioneDelegate.getInstance().updateAmministrazione(ammVO);
			if (ammVO.isRepertoriEnabled())
				OrganizzazioneDelegate.getInstance().updateParametriFTP(ammVO);
			org.setValueObject(ammVO);
			messages.add("msg", new ActionMessage("operazione_ok"));
			saveMessages(request, messages);
			if (!errors.isEmpty()) {
				saveErrors(request, errors);
			}

		}
		AmministrazioneVO amministrazioneVO = org.getValueObject();
		caricaDatiNelForm(amministrazioneForm, amministrazioneVO);

		return (mapping.findForward("input"));
	}

	public void caricaDatiNelVO(AmministrazioneVO vo, AmministrazioneForm form,
			String username) {
		vo.setId(form.getId());
		if (form.getId() == 0) {
			vo.setRowCreatedUser(username);
		} else {
			vo.setRowUpdatedUser(username);
		}
		vo.setCodice(form.getCodice());
		vo.setDescription(form.getDescrizione());
		vo.setFlagLdap(form.getFlagLdap());
		vo.setFlagRegistroPostaSeparato(form.getFlagRegistroPostaSeparato());
		vo.setWebSocketEnabled("1".equals(form.getWebSocketEnabled())? true: false);
		
		if ("0".equals(form.getFlagLdap())) {
			ParametriLdapVO pLdap = new ParametriLdapVO();
			form.setParametriLDap(pLdap);
		}
		vo.setParametriLdap(form.getParametriLDap());
		vo.setPathDocAquisMassiva(form.getPathDocAquisMassiva());
		vo.setPathDocumentiProtocollo(form.getPathDocumentiProtocollo());
		vo.setRepertoriEnabled(form.isRepertoriEnabled());
		if (!form.getPortaFtp().equals(""))
			vo.setPortaFtp(Integer.valueOf(form.getPortaFtp()));
		else
			vo.setPortaFtp(0);
		vo.setUserFtp(form.getUserFtp());
		vo.setPassFtp(form.getPassFtp());
		vo.setHostFtp(form.getHostFtp());
		vo.setFolderFtp(form.getFolderFtp());
		vo.setRepertoriEnabled(form.isRepertoriEnabled());
	}

	public void caricaDatiNelForm(AmministrazioneForm form, AmministrazioneVO vo) {
		form.setId(vo.getId().intValue());
		form.setCodice(vo.getCodice());
		form.setDescrizione(vo.getDescription());
		form.setFlagLdap(vo.getFlagLdap());
		form.setFlagRegistroPostaSeparato(vo.getFlagRegistroPostaSeparato());
		form.setParametriLDap(vo.getParametriLdap());
		form.setRepertoriEnabled(vo.isRepertoriEnabled());
		form.setWebSocketEnabled(vo.isWebSocketEnabled()?"1":"0");
		form.setPathDocAquisMassiva(vo.getPathDocAquisMassiva());
		form.setPathDocumentiProtocollo(vo.getPathDocumentiProtocollo());
		form.setUserFtp(vo.getUserFtp());
		form.setPassFtp(vo.getPassFtp());
		form.setHostFtp(vo.getHostFtp());
		form.setFolderFtp(vo.getFolderFtp());
		if (vo.getPortaFtp() != 0)
			form.setPortaFtp("" + vo.getPortaFtp());
	}
}
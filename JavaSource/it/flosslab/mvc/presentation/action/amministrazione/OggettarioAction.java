

package it.flosslab.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.flosslab.mvc.business.OggettarioDelegate;
import it.flosslab.mvc.presentation.actionform.amministrazione.OggettiActionForm;
import it.flosslab.mvc.vo.OggettoVO;

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

public class OggettarioAction extends Action {

	static Logger logger = Logger.getLogger(OggettarioAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		OggettiActionForm oForm = (OggettiActionForm) form;
		setOggettario(utente.getAreaOrganizzativa().getId(),oForm);
		if (request.getParameter("oggettoSelezionato") != null) {
			int id = Integer.valueOf(request.getParameter("oggettoSelezionato"));
			try {
				OggettoVO oggetto = OggettarioDelegate.getInstance().getOggetto(id);
				aggiornaOggettoForm(oggetto, oForm);
				return mapping.findForward("modificaOggetto");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (request.getParameter("nuovoAction") != null) {
			try {
				OggettoVO oggetto = new OggettoVO();
				aggiornaOggettoForm(oggetto, oForm);
				return mapping.findForward("nuovoOggetto");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (request.getParameter("elimina") != null) {
		}
		return mapping.findForward("success");

	}

	

	private void setOggettario(int aooId, OggettiActionForm oForm) {
		Collection<OggettoVO> oggetti = LookupDelegate.getInstance().getOggetti(aooId).values();
		oForm.setOggettiCollection(oggetti);
		//request.setAttribute("oggettario", oggetti);
	}

	private void aggiornaOggettoForm(OggettoVO vo, OggettiActionForm form) {
		form.setOggettoId(vo.getOggettoId());
		form.setDescrizione(vo.getDescrizione());
		form.setUfficioCorrenteId(0);
		
		aggiornaAssegnatariForm(vo, form);
	}

	private static void aggiornaAssegnatariForm(OggettoVO vo,
			OggettiActionForm form) {
		form.getAssegnatari().clear();
		Organizzazione org = Organizzazione.getInstance();
		for (Iterator<AssegnatarioVO> i = vo.getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioVO assegnatario = i.next();
			AssegnatarioView ass = new AssegnatarioView();
			int uffId = assegnatario.getUfficioAssegnatarioId();
			ass.setUfficioId(uffId);
			Ufficio uff = org.getUfficio(uffId);
			if (uff != null) {
				ass.setNomeUfficio(uff.getValueObject().getDescription());
			}
			form.aggiungiAssegnatario(ass);

		}

	}
}
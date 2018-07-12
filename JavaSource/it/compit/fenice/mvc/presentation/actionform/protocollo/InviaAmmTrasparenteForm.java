package it.compit.fenice.mvc.presentation.actionform.protocollo;

import it.compit.fenice.mvc.business.AmmTrasparenteDelegate;
import it.compit.fenice.mvc.vo.ammtrasparente.AmmTrasparenteVO;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class InviaAmmTrasparenteForm extends ProtocolloForm {

	private static final long serialVersionUID = 1L;
	
	private Collection<AmmTrasparenteVO> sezioni;

	private int sezId;

	public InviaAmmTrasparenteForm() {
		inizializzaForm();
	}
	
	public Collection<AmmTrasparenteVO> getSezioni() {
		return sezioni;
	}

	public void setSezioni(Collection<AmmTrasparenteVO> sezioni) {
		this.sezioni = sezioni;
	}

	public int getSezId() {
		return sezId;
	}

	public void setSezId(int sezId) {
		this.sezId = sezId;
	}
	
	public String getNomeSezione() {
		if (sezId != 0) {
			AmmTrasparenteVO sez = AmmTrasparenteDelegate.getInstance().getSezione(sezId);
			return (sez != null ? sez.getDescrizione() : "");
		}
		return "";
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);

		if (request.getParameter("btnInvioSezione") != null) {
			if (sezId == 0) {
				errors.add("sezione", new ActionMessage("sezione_obbligatorio"));
			}
		}
		return errors;
	}

}

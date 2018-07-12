package it.compit.fenice.mvc.presentation.actionform.protocollo;

import it.compit.fenice.mvc.business.RepertorioDelegate;
import it.compit.fenice.mvc.vo.repertori.RepertorioVO;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class InviaRepertorioForm extends ProtocolloForm {

	private static final long serialVersionUID = 1L;
	
	private Collection<RepertorioVO> repertori;

	private int repertorioId;

	public InviaRepertorioForm() {
		inizializzaForm();
	}
	
	public Collection<RepertorioVO> getRepertori() {
		return repertori;
	}

	public void setRepertori(Collection<RepertorioVO> repertori) {
		this.repertori = repertori;
	}

	public int getRepertorioId() {
		return repertorioId;
	}

	public void setRepertorioId(int repertorioId) {
		this.repertorioId = repertorioId;
	}
	
	public String getNomeRepertorio() {
		if (repertorioId != 0) {
			RepertorioVO rep = RepertorioDelegate.getInstance().getRepertorio(
					repertorioId);
			return (rep != null ? rep.getDescrizione() : "");
		}
		return "";
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);

		if (request.getParameter("btnInvioRepertorio") != null) {
			if (repertorioId == 0) {
				errors.add("repertorio", new ActionMessage(
						"repertorio_obbligatorio"));
			}
		}
		return errors;
	}

}

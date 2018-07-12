package it.flosslab.mvc.presentation.tag;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.MenuDelegate;
import it.finsiel.siged.util.NumberUtil;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class AuthorizationTag extends TagSupport {

	private String _permission;

	private static final long serialVersionUID = 976905070446336754L;

	public int doStartTag() throws JspException {
		HttpSession session = this.pageContext.getSession();
		try {
			boolean isAuthorized = false;
			Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
			MenuDelegate delegate = MenuDelegate.getInstance();
			int caricaId = utente.getCaricaInUso();
			if (NumberUtil.isInteger(_permission)) {
				isAuthorized = delegate.isChargeEnabled(caricaId,
						Integer.parseInt(_permission));
			} else {
				isAuthorized = delegate.isChargeEnabled(caricaId, _permission);
			}
			if (isAuthorized) {
				return EVAL_BODY_INCLUDE;
			} else {
				return SKIP_BODY;
			}
		} catch (Throwable t) {
			throw new JspException("Errore inizializzazione tag", t);
		}
	}

	public String getPermission() {
		return _permission;
	}

	public void setPermission(String permission) {
		this._permission = permission;
	}

}

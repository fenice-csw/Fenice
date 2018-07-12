package it.finsiel.siged.mvc.presentation.action;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Menu;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.presentation.helper.NavBarElement;
import it.finsiel.siged.mvc.vo.organizzazione.MenuVO;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Implementation of <strong>Action </strong> that validates a user logon.
 * 
 * @author Almaviva sud.
 */

public final class MenuAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		int menuId = -1;
		try {
			menuId = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			menuId = 1;
		}
		Organizzazione org = Organizzazione.getInstance();
		Menu menu = org.getMenu(menuId);
		if (menu != null) {
			String menuLink = menu.getLink();
			session.setAttribute(Constants.CURRENT_MENU, menu);
			ArrayList<NavBarElement> navBar = new ArrayList<NavBarElement>();
			while (menu != null && menu.getValueObject().getId().intValue() > 0) {
				MenuVO vo = menu.getValueObject();
				String link = menu.getLink();
				if (link == null) {
					link = "/menu.do?id=" + vo.getId();
				} else {
					int p = link.indexOf('?');
					if (p > 0) {
						link = link.substring(0, p);
					}
				}
				// link = request.getContextPath() + link;
				NavBarElement elem = new NavBarElement();
				elem.setValue(vo.getTitle());
				elem.setTitle(vo.getDescription());
				
				elem.setLink(link);
				navBar.add(0, elem);
				menu = menu.getParent();
			}
			session.setAttribute(Constants.NAV_BAR, navBar);
			cleaOggettiSession(session);
			if (menuLink != null && !menuLink.trim().equals("")) {
				request.getRequestDispatcher(menuLink).forward(request,
						response);
				return null;
			}
		}
		return mapping.findForward("emptyMenu");
	}

	private void cleaOggettiSession(HttpSession session) {
		
		for (Enumeration<?> e = session.getAttributeNames(); e.hasMoreElements();) {
			String attributo = (String) e.nextElement();
			
			if (!attributo.startsWith("org.")
					&& !attributo.equals(Constants.NAV_BAR)
					&& !attributo.equals(Constants.CURRENT_MENU)
					&& !attributo.equals(Constants.UTENTE_KEY)
					&& !attributo.equals(Constants.SESSION_NOTIFIER)
					&& !attributo
							.equals(Constants.CONFIGURAZIONE_UTENTE_PROTOCOLLO)) {
				session.removeAttribute(attributo);

			}
		}
	}
}
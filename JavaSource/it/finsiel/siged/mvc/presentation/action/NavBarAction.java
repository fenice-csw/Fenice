package it.finsiel.siged.mvc.presentation.action;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.mvc.presentation.helper.NavBarElement;

import java.util.ArrayList;

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

public final class NavBarAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession();
        int position = -1;
        try {
            position = Integer.parseInt(request.getParameter("position"));
        } catch (NumberFormatException e) {
            position = 1;
        }
        ArrayList<?> navBar = (ArrayList<?>) session.getAttribute(Constants.NAV_BAR);

        if (navBar != null) {
            if (position > 0 && position <= navBar.size()) {
                while (position < navBar.size()) {
                    navBar.remove(position);
                }
                NavBarElement elem = (NavBarElement) navBar.get(position - 1);
                String link = elem.getLink();
                if (link != null && !link.trim().equals("")) {
                    request.getRequestDispatcher(link).forward(request,
                            response);
                    return null;
                }
            }
        }
        return mapping.findForward("emptyMenu");
    }
}
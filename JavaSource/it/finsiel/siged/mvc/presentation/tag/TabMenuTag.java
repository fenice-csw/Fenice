package it.finsiel.siged.mvc.presentation.tag;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Menu;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.MenuDelegate;
import it.finsiel.siged.mvc.vo.organizzazione.MenuVO;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Marcello Spadafora
 * 
 */

public class TabMenuTag extends TagSupport {

	private static final long serialVersionUID = 1L;

    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        HttpSession session = req.getSession();
        Menu rootMenu = (Menu) pageContext.getServletContext().getAttribute(
                Constants.MENU_ROOT);

        if (rootMenu != null) {
            Menu currentMenu = (Menu) session
                    .getAttribute(Constants.CURRENT_MENU);
            if (currentMenu != null) {
                Menu parent;
                while ((parent = currentMenu.getParent()) != null
                        && parent != rootMenu) {
                    currentMenu = parent;
                }
            }
            Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
            MenuDelegate delegate = MenuDelegate.getInstance();
            try {
                for (Iterator<Menu> i = rootMenu.getChildren().iterator(); i
                        .hasNext();) {
                    Menu menu = (Menu) i.next();
                    MenuVO vo = menu.getValueObject();
                    if (delegate.isChargeEnabled(utente, menu)) {
                        String url = req.getContextPath() + "/menu.do?id="
                                + vo.getId();
                        out.print("<a");
                        if (menu == currentMenu) {
                            out.print(" class='selected'");
                        }
                        out.print(" href='" + url + "'");
                        out.print(" title =\"" + vo.getDescription() + "\">");
                        out.println(vo.getTitle() + "</a>");
                    }
                }
            } catch (IOException e) {
            }
        }
        return 0;
    }

    public int doEndTag() throws JspException {
        return 0;
    }
}
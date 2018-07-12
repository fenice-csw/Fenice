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

public class SideMenuTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletRequest request = (HttpServletRequest) pageContext
                .getRequest();
        HttpSession session = request.getSession();
        Menu currentMenu = (Menu) session.getAttribute(Constants.CURRENT_MENU);
        if (currentMenu != null) {
            if (currentMenu.getChildren().size() == 0) {
                currentMenu = currentMenu.getParent();
            }
            Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
            MenuDelegate delegate = MenuDelegate.getInstance();
            try {
                for (Iterator<Menu> i = currentMenu.getChildren().iterator(); i
                        .hasNext();) {
                    Menu child = i.next();
                    MenuVO vo = child.getValueObject();
                    if (delegate.isChargeEnabled(utente, child)) {
                        String url = request.getContextPath() + "/menu.do?id="
                                + vo.getId();
                        out.print("<a href='" + url + "'");
                        out.print(" title=\"" + vo.getDescription() + "\">");
                        if (child.getChildren().size() > 0) {
                            out.print("<strong>" + vo.getTitle() + "</strong>");
                        } else {
                            out.print(vo.getTitle());
                        }
                        out.println("</a>");
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
package it.finsiel.siged.mvc.presentation.tag;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.mvc.presentation.helper.NavBarElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class NavBarTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        HttpSession session = req.getSession();
        ArrayList navBar = (ArrayList) session.getAttribute(Constants.NAV_BAR);

        if (navBar != null) {
            int n = 0;
            for (Iterator i = navBar.iterator(); i.hasNext();) {
                NavBarElement elem = (NavBarElement) i.next();
                try {
                    if (++n == navBar.size()) {
                        out.print("<span title=\"");
                        out.print(elem.getTitle());
                        out.print("\">");
                        out.print(elem.getValue());
                        out.println("</span>");
                    } else {
                        out.print("<a href=\"" + req.getContextPath());
                        out.print("/navbar.do?position=" + n);
                        out.print("\" title=\"");
                        out.print(elem.getTitle());
                        out.print("\">");
                        out.print(elem.getValue());
                        out.print("</a>");
                        out.println("<span> &gt; </span>");
                    }
                } catch (IOException e) {
                }
            }
        }
        return 0;
    }

    public int doEndTag() throws JspException {
        return 0;
    }
}
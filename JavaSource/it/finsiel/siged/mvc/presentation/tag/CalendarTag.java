package it.finsiel.siged.mvc.presentation.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class CalendarTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	
	private String textField;
    
    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }

    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();

        try {
            out.println("<script type='text/javascript'>");
            out.println("<!--");
            out.print("  document.write(\"<input type='image' id='");
            out.print(getTextField());
            out.print("Button' alt='' src='");
            out.print(req.getContextPath());
            out.print("/images/calendar/calendario.gif' ");
            out.println("title='Seleziona la data' />\");");
            out.print("  Calendar.setup({");
            out.print("inputField : '");
            out.print(getTextField());
            out.print("', button : '");
            out.print(getTextField());
            out.print("Button', ");
            out.print("ifFormat : '%d/%m/%Y', ");
            out.print("firstDay : 1, ");
            out.print("weekNumbers : false, ");
            out.print("cache : true");
            out.println("});");
            out.println("// -->");
            out.println("</script>");
        } catch (IOException e) {
        }
        return 0;
    }

    public int doEndTag() throws JspException {
        return 0;
    }
}
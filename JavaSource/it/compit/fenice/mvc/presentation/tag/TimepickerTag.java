package it.compit.fenice.mvc.presentation.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class TimepickerTag extends TagSupport {

	private static final long serialVersionUID = 1L;

	private String textField;
	private String timeFormat;
	private String step;

	public String getTextField() {
		return textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		// HttpServletRequest req = (HttpServletRequest)
		// pageContext.getRequest();
		try {
			out.println("<script type='text/javascript'>");
			out.println("$(function() {");
			out.print(" $('#" + getTextField()
					+ "').timepicker({ 'timeFormat': '" + getTimeFormat()
					+ "', 'step': " + getStep() + " });");
			out.print(getTextField());
			out.println("});");
			out.println("</script>");
		} catch (IOException e) {
		}
		return 0;
	}

	public int doEndTag() throws JspException {
		return 0;
	}
}

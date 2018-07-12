package it.finsiel.siged.mvc.presentation.tag;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Menu;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.MenuDelegate;
import it.finsiel.siged.mvc.presentation.helper.NavBarElement;
import it.finsiel.siged.mvc.vo.organizzazione.MenuVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;


public class PageTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void tabMenu() throws IOException {
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
			if (utente.getValueObject().getUsername().equals("admin")) {
				out
						.print("<li><a target='_self' class='unselected' href='/feniceWeb/gestisciAOO.do' title ='Gestione AOO'>Gestione AOO</a></li>");
			} else {
				MenuDelegate delegate = MenuDelegate.getInstance();
				for (Iterator<Menu> i = rootMenu.getChildren().iterator(); i
						.hasNext();) {
					Menu menu = (Menu) i.next();
					MenuVO vo = menu.getValueObject();
					if (utente.isUtenteResponsabileConnesso()) {
						if (delegate.isUtenteResponsabileEnabled( menu)) {
							String url = req.getContextPath() + "/menu.do?id="
									+ vo.getId();
							out.print("<li><a target=\"_self\"");
							if (menu == currentMenu) {
								out.print(" class='selected'");
							} else
								out.print(" class='unselected'");
							out.print(" href='" + url + "'");
							out.print(" title =\"" + vo.getDescription()
									+ "\">");
							out.println(vo.getTitle() + "</a></li>");
						}
					} else {
						if (delegate.isChargeEnabled(utente, menu)) {
							String url = req.getContextPath() + "/menu.do?id="
									+ vo.getId();
							out.print("<li><a target=\"_self\"");
							if (menu == currentMenu) {
								out.print(" class='selected'");
							} else
								out.print(" class='unselected'");
							out.print(" href='" + url + "'");
							out.print(" title =\"" + vo.getDescription()
									+ "\">");
							out.println(vo.getTitle() + "</a></li>");
						}
					}
				}
			}
		}
	}

	public void sideMenu() throws IOException {
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
			for (Iterator<Menu> i = currentMenu.getChildren().iterator(); i.hasNext();) {
				Menu child = (Menu) i.next();
				MenuVO vo = child.getValueObject();
				if (utente.isUtenteResponsabileConnesso()) {
					if (delegate.isUtenteResponsabileEnabled(child)) {
						String url = request.getContextPath() + "/menu.do?id="
								+ vo.getId();
						out.print("<li><a href='" + url + "'");
						out.print(" title=\"" + vo.getDescription() + "\">");
						if (child.getChildren().size() > 0) {
							out.print("<strong>" + vo.getTitle() + "</strong>");
						} else {
							out.print(vo.getTitle());
						}
						out.println("</a></li>");
					}
				} else if (delegate.isChargeEnabled(utente, child)) {
					if (vo.getId().intValue() == 49
							|| vo.getId().intValue() == 50
							|| vo.getId().intValue() == 60
							|| vo.getId().intValue() == 122
							|| vo.getId().intValue() == 131
							|| vo.getId().intValue() == 65
							|| vo.getId().intValue() == 25
							|| vo.getId().intValue() == 26
							|| vo.getId().intValue() == 133
							|| vo.getId().intValue() == 145
							|| vo.getId().intValue() == 146
							|| vo.getId().intValue() == 132
							|| vo.getUniqueName().equals("isert_reserved")
							|| vo.getUniqueName().equals("read_reserved")
							|| vo.getTitle().equals("Leggi Allegati")
							|| vo.getUniqueName().equals("reassign")
							|| vo.getUniqueName().equals("invio_repertori")
							|| vo.getUniqueName().equals("change_sender")
							|| vo.getId().intValue() == 140
							|| vo.getId().intValue() == 117
							|| vo.getId().intValue() == 11
							|| vo.getId().intValue() == 28
							|| vo.getId().intValue() == 45
							|| vo.getId().intValue() == 47
							|| vo.getId().intValue() == 130
							|| vo.getUniqueName().equals("new_ld_int")
							|| vo.getUniqueName().equals("email_ufficio")
							|| vo.getUniqueName().equals("serch_folders_all_offices")
							|| vo.getUniqueName().equals("modify_folders")
							|| vo.getUniqueName().equals("modify_pi")
							|| vo.getUniqueName().equals("access_all_folders")
							|| vo.getUniqueName().equals("mail_log")
							|| vo.getUniqueName().equals("result_notification_client")
							|| vo.getUniqueName().equals("ersu_scholarship_application")
							|| vo.getUniqueName().equals("enable_send_pec")
							) {
						String url = request.getContextPath() + "/menu.do?id="
								+ vo.getId();
						out.print("<!-- <li ><a href='" + url + "'");
						out.print(" title=\"" + vo.getDescription() + "\">");
						if (child.getChildren().size() > 0) {
							out.print("<strong>" + vo.getTitle() + "</strong>");
						} else {
							out.print(vo.getTitle());
						}
						out.println("</a></li> -->");
					} else {
						String url = request.getContextPath() + "/menu.do?id="
								+ vo.getId();
						out.print("<li><a href='" + url + "'");
						out.print(" title=\"" + vo.getDescription() + "\">");
						if (child.getChildren().size() > 0) {
							out.print("<strong>" + vo.getTitle() + "</strong>");
						} else {
							out.print(vo.getTitle());
						}
						out.println("</a></li>");

					}
				}
			}
		}
	}

	public void navBar() throws IOException {
		JspWriter out = pageContext.getOut();
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = req.getSession();
		ArrayList navBar = (ArrayList) session.getAttribute(Constants.NAV_BAR);

		if (navBar != null) {
			int n = 0;
			for (Iterator i = navBar.iterator(); i.hasNext();) {
				NavBarElement elem = (NavBarElement) i.next();
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
					// out.println("<span> &gt; </span>");
					out.println("&nbsp; &raquo; &nbsp;");
				}
			}
		}
	}

	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = req.getSession();
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		try {
			String scriptPrefix = "<script type='text/javascript' src='"
					+ req.getContextPath() + "/script/";
			out
					.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
			out.println("<html>");
			out
					.println("<title>Sistema di Protocollo Informatico</title>\n"
							+ "<meta name=\"Author\" content=\"Compit Srl\">\n"
							+ "<meta name=\"keywords\" content=\"protocollo, pubbliche amministrazioni, aipa, cnipa\">\n"
							+ "<meta http-equiv=\"Cache-Control\" content=\"no-cache\">\n"
							+ "<meta http-equiv=\"Pragma\" content=\"no-cache\">\n"
							+ "<meta http-equiv=\"Expires\" content=\"-1\">\n"
							+ "<meta http-equiv=\"Pragma-directive\" content=\"no-cache\">\n"
							+ "<meta http-equiv=\"cache-directive\" content=\"no-cache\">\n"
							+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
							
							+ "<link rel='stylesheet' type='text/css' href='"
							+ req.getContextPath()
							+ "/style/style.css' />\n"
							+ "<link rel='stylesheet' type='text/css' href='"
							+ req.getContextPath()
							+ "/style/compit.css' />\n"
							+ "<style type='text/css'>@import url('"
							+ req.getContextPath()
							+ "/script/calendar/calendar-blue.css');</style>\n"
							
							+ "<style type='text/css'>@import url('"
							+ req.getContextPath()
							+ "/script/timepicker/jquery.timepicker.css');</style>\n"
							
							+ scriptPrefix
							+ "calendar/calendar.js'></script>\n"
							+ scriptPrefix
							+ "calendar/lang/calendar-it.js'></script>\n"
							+ scriptPrefix
							+ "fenice.js'></script>\n"
							+ scriptPrefix
							+ "calendar/calendar-setup.js'></script>\n"
							
							+ scriptPrefix
							+ "timepicker/jquery.v1.11.1.min.js'></script>\n"
							+ scriptPrefix
							+ "timepicker/jquery.timepicker.js'></script>\n"
							
							+"<script type='text/javascript'>javascript:window.history.forward(1);</script>\n"
							+"</head>\n<body>");

			// main menu
			out.println("<div id=\"mainMenu\">");
			out.println("<a id=\"logout\" target=\"_top\" href=\""
					+ req.getContextPath()
					+ "/logoff.do\">Esci dal protocollo</a>");
			out.println("<ul>");
			tabMenu();
			out.println("</ul>");
			out.println("</div>");

			// sub menu
			out.println("<div id=\"subMenu\">");
			out.println("<ul>");
			out.println("<img src=\"" + req.getContextPath()
					+ "/images/compit/acts_menu.gif\" />");
			sideMenu();
			out.println("</ul>");
			out.println("</div>");

			out.println("<div>");

			// info utente
			if (!utente.getValueObject().getUsername().equals("admin")) {

				// navbar
				out.println("<div id=\"navBar\">");
				out.println("&raquo;");
				navBar();
				out.println("</div>");

				// info utente
				out.println("<div id=\"changeCharge\">");
				out.println("<ul>");
				out.println("<li><strong>["
						+ utente.getAreaOrganizzativa().getDescription()
						+ "]</strong>&nbsp;</li>");
				if (!utente.isUtenteResponsabileConnesso()) {
					out.println("<li>"+ utente.getUfficioVOInUso().getDescription() + "/");
					out.println(" <a target=\"_top\" href=\""
							+ req.getContextPath() + "/cambioPwd.do\">"
							+ utente.getValueObject().getFullName()
							+ "</a></li>");
				} else
					out
							.println(" <a target=\"_top\" href=\""
									+ req.getContextPath()
									+ "/cambioPwd.do\">Responsabile Area Organizzativa</a></li>");
				out.println("</ul>");
				out.println("</div>");
			} else {
				out.println("<div id=\"changeCharge\">");
				out.println("<ul>");
				out
						.println("<li><strong>[GESTIONE DELLE AREE ORGANIZZATIVE]</strong>&nbsp;</li>");
				out.println("<li> <a target=\"_top\" href=\""
						+ req.getContextPath() + "/cambioPwd.do\">"
						+ utente.getValueObject().getFullName() + "</a></li>");
				out.println("</ul>");
				out.println("</div>");
			}
			out.println("</div>");

			// break
			out.println("<br /><br />");

			// page content
			out.println("<div>");

		} catch (IOException e) {
		}
		return Tag.EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		JspWriter out = pageContext.getOut();
		try {
			out.println("</div>");
			out.println("</body>");
			out.println("</html>");
		} catch (IOException e) {
		}
		return 0;
	}
}

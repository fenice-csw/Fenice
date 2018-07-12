package it.finsiel.siged.util;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.servlet.SessionTimeoutNotifier;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class ServletUtil {

	public static String getContextPath(HttpSession session) {
		return session.getServletContext().getRealPath("/");
	}

	public static String getTempUserPath(HttpSession session) {
		return ((SessionTimeoutNotifier) session
				.getAttribute(Constants.SESSION_NOTIFIER)).getTempPath();
	}

	public static void ForwardTo(HttpServletRequest req,
			HttpServletResponse resp, String forwardUrl) throws IOException,
			ServletException {
		RequestDispatcher dispatcher = req.getSession().getServletContext()
				.getRequestDispatcher(forwardUrl);
		if (dispatcher != null) {
			dispatcher.forward(req, resp);
		} else {
			resp.sendRedirect(forwardUrl);
		}
	}

	public static void include(HttpServletRequest req,
			HttpServletResponse resp, String url) throws IOException,
			ServletException {
		RequestDispatcher dispatcher = req.getSession().getServletContext()
				.getRequestDispatcher(url);
		if (dispatcher != null) {
			dispatcher.include(req, resp);
		} else {
			resp.sendRedirect(url);
		}
	}

	public void printAttributes(HttpServletRequest request) {
		Enumeration<?> e = request.getAttributeNames();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			System.out.println("Servlet Attribute Name:" + name + " - value: "
					+ request.getAttribute(name));
		}
	}

	public void printFormVars(HttpServletRequest request) {
		Enumeration<?> enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paramName = (String) enu.nextElement();
			System.out.println("Servlet Parameter Name:" + paramName + "- value: "
					+ request.getParameter(paramName));
		}
	}

}

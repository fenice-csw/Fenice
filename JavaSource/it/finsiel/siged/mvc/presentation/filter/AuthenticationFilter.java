package it.finsiel.siged.mvc.presentation.filter;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/*
 * @author Almaviva sud.
 */

public class AuthenticationFilter implements Filter {
    protected Logger logger = Logger.getLogger(AuthenticationFilter.class
            .getName());

    private FilterConfig filterConfig = null;

    private ServletContext ctx = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        ctx = filterConfig.getServletContext();
    }

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        if (req != null) {
        	 HttpServletRequest request = (HttpServletRequest) req;
            Utente utente = (Utente) request.getSession().getAttribute(Constants.UTENTE_KEY);
            Boolean loggedIn = new Boolean(utente != null);
            if (Boolean.TRUE.equals(loggedIn)) {
                chain.doFilter(req, res);
            } else {
                logger.debug("Utente non autenticato >> login");
                RequestDispatcher ds = ctx.getRequestDispatcher("/logon.jsp");
                ds.forward(request, res);
            }
        }
    }

    public void destroy() {
        // nothing to destroy yet
    }

    /**
     * @return Returns the filterConfig.
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * @param filterConfig
     *            The filterConfig to set.
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

}
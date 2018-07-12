package it.finsiel.siged.mvc.presentation.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

/*
 * @author Almaviva sud.
 */

public class DownloadDocumentiFilter implements Filter {

    /**
     * The logger - it is visible to all subclasses.
     */
    protected Logger logger = Logger.getLogger(DownloadDocumentiFilter.class
            .getName());

    private FilterConfig filterConfig = null;

    private ServletContext ctx = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.setCtx(filterConfig.getServletContext());
    }

    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {

        if (req != null) {
            //HttpServletRequest request = (HttpServletRequest) req;

            if (true) {
                logger.debug("utente autorizzato");

                // Logged in - Let's pass thru the user
                chain.doFilter(req, res);
            }
            /*
            else {
                String paginaProvenienza = "";
                logger.debug("utente non autorizzato");
                RequestDispatcher ds = ctx
                        .getRequestDispatcher(paginaProvenienza);
                ds.forward(request, res);
            }
            */
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

	public ServletContext getCtx() {
		return ctx;
	}

	public void setCtx(ServletContext ctx) {
		this.ctx = ctx;
	}

}
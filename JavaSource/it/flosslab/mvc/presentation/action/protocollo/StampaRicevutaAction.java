/*
*
* Copyright 2008 Flosslab s.r.l. (http://www.flosslab.it) All rights reserved.
*
* This file is part of e-prot 1.1 software.
* e-prot 1.1 is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2008 Flosslab s.r.l. (http://www.flosslab.it) All rights reserved.
* Version: e-prot 1.1
*/

package it.flosslab.mvc.presentation.action.protocollo;

import it.flosslab.report.utility.ReportHelper;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StampaRicevutaAction extends Action {

    static Logger logger = Logger.getLogger(StampaRicevutaAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionForward myforward = null;
		String myaction = mapping.getParameter();
		if ("".equalsIgnoreCase(myaction)) {
			myforward = mapping.findForward("failure");
		}else {
			myforward = performReport(mapping, form, request, response);
		} 
        return myforward;

    }

	private ActionForward performReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ServletContext context = getServlet().getServletContext();
        OutputStream os = response.getOutputStream();
        File reportFile = new File(context.getRealPath("/") + ReportHelper.STAMPA_RICEVUTA_PROTOCOLLO_TEMPLATE);
        
        try {
			ReportHelper.reportToOutputStream(form, response, os, reportFile,context.getRealPath("/") + ReportHelper.STAMPA_RICEVUTA_PROTOCOLLO_TEMPLATE);
		} catch (Exception e) {
            logger.error("", e);
            response.setContentType("text/plain");
            e.printStackTrace(new PrintStream(os));
        } finally {
            os.close();
        }
        return (mapping.findForward("input"));
	}
	
}

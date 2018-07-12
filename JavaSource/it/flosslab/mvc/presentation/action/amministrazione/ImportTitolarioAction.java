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

package it.flosslab.mvc.presentation.action.amministrazione;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.flosslab.mvc.presentation.actionform.amministrazione.ImportTitolarioActionForm;
import it.flosslab.parser.CreateTitolario;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class ImportTitolarioAction extends Action {

    static Logger logger = Logger.getLogger(OggettarioAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        ActionForward myforward = null;
		String myaction = mapping.getParameter();
		if ("".equalsIgnoreCase(myaction)) {
			myforward = mapping.findForward("failure");
		}else if ("NEW".equalsIgnoreCase(myaction)) {
			myforward = performNew(mapping, form, request, response);
		} else if ("EXECUTE".equalsIgnoreCase(myaction)) {
			myforward = performExecute(mapping, form, request, response);
		} else if ("FORCE".equalsIgnoreCase(myaction)) {
			myforward = performForce(mapping, form, request, response);
		}
        return myforward;

    }

	private ActionForward performForce(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Utente utente = (Utente) request.getSession().getAttribute(Constants.UTENTE_KEY);
	    CreateTitolario parser = new CreateTitolario(utente.getValueObject().getAooId());
		
		try {
			boolean result = parser.forceImportTitolario((FormFile) request.getSession().getAttribute("file"));
			request.getSession().removeAttribute("file");
			if(!result){
				request.setAttribute("dependencies", "true");
				return mapping.findForward("notImported");
			}
		} catch (BiffException e) {
			e.printStackTrace();
			return mapping.findForward("failure");
		} catch (IOException e) {
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
	}

	private ActionForward performExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Utente utente = (Utente) request.getSession().getAttribute(Constants.UTENTE_KEY);
	    CreateTitolario parser = new CreateTitolario(utente.getValueObject().getAooId());
		ImportTitolarioActionForm importForm = (ImportTitolarioActionForm)form;
		try {
			boolean isTitolarioEmpty = parser.create(importForm.getFile());
			if(!isTitolarioEmpty){
				request.setAttribute("imported", "false");
				request.getSession().setAttribute("file", importForm.getFile());
				return mapping.findForward("notImported");
			}
		  }
			/*
		 catch (BiffException e) {
			e.printStackTrace();
			return mapping.findForward("failure");
		} catch (IOException e) {
			e.printStackTrace();
			return mapping.findForward("failure");
		}
		*/
		catch(Exception e){
			e.printStackTrace();
			return mapping.findForward("failure");}
		
		return mapping.findForward("success");
		
	}

	
	
	private ActionForward performNew(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("file");
		return mapping.findForward("success");
	}
}

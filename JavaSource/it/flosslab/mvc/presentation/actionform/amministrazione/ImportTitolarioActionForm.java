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

package it.flosslab.mvc.presentation.actionform.amministrazione;

import java.io.IOException;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.flosslab.parser.CreateTitolario;

import javax.servlet.http.HttpServletRequest;

import jxl.read.biff.BiffException;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;


public class ImportTitolarioActionForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	FormFile _file;

	public FormFile getFile() {
		return _file;
	}

	
	public void setFile(FormFile _file) {
		this._file = _file;
	}
	
	 public ActionErrors validate(ActionMapping mapping,
	            HttpServletRequest request) {
		 Utente utente = (Utente) request.getSession().getAttribute(Constants.UTENTE_KEY);
		 CreateTitolario parser = new CreateTitolario(utente.getValueObject().getAooId());
		 ActionErrors errors = new ActionErrors();
	        if(!_file.getContentType().equals("application/vnd.ms-excel")){
	        	errors.add("noFileExcel", new ActionMessage("noFileExcel"));
	        }
			try {
				if(!parser.isParsable(_file)){
					errors.add("noCorrectFormat", new ActionMessage("noCorrectFormat"));
				}
			} catch (BiffException e) {
				e.printStackTrace();
				errors.add("generalNotimported", new ActionMessage("generalNotimported"));
			} catch (IOException e) {
				e.printStackTrace();
				errors.add("generalNotimported", new ActionMessage("generalNotimported"));
			}
				
		 return errors;
	 }
}
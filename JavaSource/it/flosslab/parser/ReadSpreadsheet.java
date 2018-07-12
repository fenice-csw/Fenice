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

package it.flosslab.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import org.apache.struts.upload.FormFile;

import common.Logger;

public class ReadSpreadsheet {
		private static Logger logger = Logger.getLogger(ReadSpreadsheet.class);
		private boolean status;
		private InputStream inputWorkbook;


		public ReadSpreadsheet(FormFile file) throws IOException{
			inputWorkbook = new ByteArrayInputStream(file.getFileData());
			if (inputWorkbook.read() != 0){
				logger.info("Input file:  " + file.getFileName());
				setStatus(true);
			}
			else{
				logger.info("File non leggibile o inesistente");
				setStatus(false);
			}
				
		}

		public Sheet reading(FormFile file) throws BiffException, IOException{
			logger.info("Reading...");
			WorkbookSettings settings = new WorkbookSettings();
			settings.setEncoding("UTF-8");
		    Workbook w1 = Workbook.getWorkbook(file.getInputStream(), settings);
		    Sheet sheet = w1.getSheet(0);
		    return sheet;
		}

		/**
		 * @return the status
		 */
		public boolean isReadable() {
			return status;
		}

		/**
		 * @param status the status to set
		 */
		public void setStatus(boolean status) {
			this.status = status;
		}
}

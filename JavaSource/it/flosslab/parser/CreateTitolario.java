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

import it.finsiel.siged.mvc.business.TitolarioDelegate;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.read.biff.BiffException;

import org.apache.struts.upload.FormFile;
/**
 * 
 * @author daniele
 *
 */
public class CreateTitolario {

	private int aoo;
	
	
	/**
	 * 
	 * @param aoo
	 */
	public CreateTitolario(int aoo){
		this.aoo = aoo;
	}
	
	
	/**
	 * 
	 * @param file
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public boolean create(FormFile file) throws BiffException, IOException{
		ReadSpreadsheet reader = new ReadSpreadsheet(file);
		if (!reader.isReadable()){
			System.out.print("impossibile leggere il file");
		} else{
			Sheet sheet = reader.reading(file);
			TitolarioDelegate titManager = TitolarioDelegate.getInstance();
			Collection<TitolarioVO> titolari = titManager.getTitolariByParent(0, this.aoo);
			if(titolari.size() == 0){
				List<TitolarioVO> toSave = createVociTitolario(sheet);
				for(TitolarioVO titolario : toSave){
					titManager.salvaArgomento(titolario);
					titManager.associaTuttiGliUfficiTitolario(titolario, this.aoo);
				}
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public boolean forceImportTitolario(FormFile file) throws BiffException, IOException{
		ReadSpreadsheet reader = new ReadSpreadsheet(file);
		Sheet sheet = reader.reading(file);
		TitolarioDelegate titManager = TitolarioDelegate.getInstance();
		List<TitolarioVO> toSave = createVociTitolario(sheet);
		return titManager.salvaArgomenti(toSave);
	}


	/**
	 * @param sheet
	 * @param nrVoci
	 * @param parents
	 * @param toSave
	 */
	public List<TitolarioVO> createVociTitolario(Sheet sheet) {
		int deep = sheet.getColumns();
		int nrVoci = sheet.getRows();
		Integer[] parents = new Integer[deep];
		List<TitolarioVO> toSave = new ArrayList<TitolarioVO>();
		for(int i=0; i<nrVoci;i++){
			Cell[] row = sheet.getRow(i);
			String voce = row[row.length-1].getContents();
			parents[row.length-1] = i+1;
			toSave.add(this.createTitolarioVO(voce, parents, row.length, i));
		}
		return toSave;
	}
	
	
	private TitolarioVO createTitolarioVO(String voce, Integer[] parents, int length, int codice) {
		TitolarioVO titolario = new TitolarioVO();
		titolario.setAooId(this.aoo);
		titolario.setCodice(voce);
		titolario.setDescrizione(voce);
		int parentId = 0;
		if(length != 1){
			parentId = parents[length-2];
		}
		
		titolario.setParentId(parentId);
		return titolario;
		
	}
	
	public boolean isParsable(FormFile file) throws IOException, BiffException{
		ReadSpreadsheet reader = new ReadSpreadsheet(file);
		Sheet sheet = reader.reading(file);
		int nrVoci = sheet.getRows();
		for(int i=0; i<nrVoci;i++){
			Cell[] row = sheet.getRow(i);
			for(int j=0;j<row.length-2;j++){
				if(!row[j].getContents().equals("")){
					return false;
				}
			}
		}
	
		return true;
	}
	/**
	 * @return the aoo
	 */
	public int getAoo() {
		return aoo;
	}
	/**
	 * @param aoo the aoo to set
	 */
	public void setAoo(int aoo) {
		this.aoo = aoo;
	}
}

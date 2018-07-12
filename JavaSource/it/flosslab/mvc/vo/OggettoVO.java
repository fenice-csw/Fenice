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

package it.flosslab.mvc.vo;

import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;

import java.util.ArrayList;
import java.util.Collection;

public class OggettoVO {

	private String oggettoId;
	private String descrizione;
	private int giorniAlert;
	private int aooId;
	private Collection<AssegnatarioVO> assegnatari = new ArrayList<AssegnatarioVO>();

	public OggettoVO(String id, String descrizione) {
		this.oggettoId = id;
		this.descrizione = descrizione;
	}

	public OggettoVO() {
	}

	public int getAooId() {
		return aooId;
	}

	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public String getOggettoId() {
		if (oggettoId != null) {
			return oggettoId.toString();
		}
		return "0";
	}

	public void setOggettoId(String id) {
		this.oggettoId = id;
	}

	public void setOggettoId(Integer id) {
		this.oggettoId = id.toString();
	}

	public int getGiorniAlert() {
		return giorniAlert;
	}

	public void setGiorniAlert(int giorniAlert) {
		this.giorniAlert = giorniAlert;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Collection<AssegnatarioVO> getAssegnatari() {
		return new ArrayList<AssegnatarioVO>(assegnatari);
	}

	public void aggiungiAssegnatario(AssegnatarioVO assegnatario) {
		if (assegnatario != null) {
			assegnatari.add(assegnatario);
		}
	}

	public void aggiungiAssegnatari(Collection<AssegnatarioVO> assegnatari) {
		this.assegnatari.addAll(assegnatari);
	}

	public void removeAssegnatari() {
		assegnatari.clear();
	}

}

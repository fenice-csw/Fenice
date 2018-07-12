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

package it.flosslab.mvc.presentation.action.protocollo.helper.model;

import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;

import java.util.Date;

public class AggiornaProtocolloModel {

	
	public static void aggiorna(ProtocolloForm form, Protocollo protocollo){
        ProtocolloVO protocolloVO = protocollo.getProtocollo();

        // dati generali
        aggiornaDatiGeneraliModel(form, protocolloVO);
        protocollo.setVisionato(form.isVisionato());
        
        // protocolli allacciati
        protocollo.setAllacci(form.getProtocolliAllacciati());

        // documenti allegati
        protocollo.setAllegati(form.getDocumentiAllegati());
        protocollo.setAddOggetto(form.isOggettoToAdd());
        protocollo.setAddFisica(form.isFisicaToAdd());
        protocollo.setAddGiuridica(form.isGiuridicaToAdd());
        
        // documento principale
        protocollo.setDocumentoPrincipale(form.getDocumentoPrincipale());
        
        // annotazione
        aggiornaAnnotazioniModel(form, protocolloVO);

        // titolario
        if (form.getTitolario() != null) {
            protocolloVO.setTitolarioId(form.getTitolario().getId().intValue());
        } else {
            protocolloVO.setTitolarioId(0);
        }
        // fascicolo
        protocollo.setLavorato(form.isLavorato());
        protocollo.setFascicoli(form.getFascicoliProtocollo());
        protocollo.setFascicoliEliminatiId(form.getFascicoliEliminatiId());
       
        // procedimenti
        protocollo.setProcedimentiEliminatiId(form.getProcedimentiRimossi());
        protocollo.setProcedimenti(form.getProcedimentiProtocollo());
    }
	
	 private static void aggiornaDatiGeneraliModel(ProtocolloForm form,
	            ProtocolloVO protocollo) {
	        protocollo.setId(form.getProtocolloId());
	        protocollo.setTipoDocumentoId(form.getTipoDocumentoId());
	        protocollo.setRiservato(form.getRiservato());
	        protocollo.setFatturaElettronica(form.getFatturaElettronica());
	        protocollo.setFlagAnomalia(form.isFlagAnomalia());

	        protocollo.setFlagRepertorio(form.getFlagRepertorio());
	        Date dataDoc = DateUtil.toDate(form.getDataDocumento());
	        if (dataDoc != null) {
	            protocollo.setDataDocumento(new java.sql.Date(dataDoc.getTime()));
	        } else {
	            protocollo.setDataDocumento(null);
	        }
	        Date dataRic = DateUtil.toDate(form.getDataRicezione());
	        if (dataRic != null) {
	            protocollo.setDataRicezione(new java.sql.Date(dataRic.getTime()));
	        } else {
	            protocollo.setDataRicezione(null);
	        }
	      
	        protocollo.setNumProtocolloEmergenza(form.getNumProtocolloEmergenza());
	        protocollo.setOggetto(form.getOggettoGenerico());
	        protocollo.setGiorniAlert(form.getGiorniAlert());
	        if (protocollo.getId().intValue() > 0) {
	            protocollo.setStatoProtocollo(form.getStato());
	        }
	        protocollo.setVersione(form.getVersione());
	        protocollo.setEstermiAutorizzazione(form.getEstremiAutorizzazione());
	        
	      
	    }
	 
	  	
	 	private static void aggiornaAnnotazioniModel(ProtocolloForm form,
	            ProtocolloVO protocollo) {
	        protocollo.setDescrizioneAnnotazione(form.getDescrizioneAnnotazione());
	        protocollo.setPosizioneAnnotazione(form.getPosizioneAnnotazione());
	        protocollo.setChiaveAnnotazione(form.getChiaveAnnotazione());
	    }


}

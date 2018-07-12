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

package it.flosslab.mvc.presentation.action.protocollo.helper.form;

import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.mvc.business.MenuDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpSession;


public class AggiornaProtocolloForm {

	
    public static void aggiorna(Protocollo protocollo, ProtocolloForm form,
            HttpSession session,boolean mail,Utente utente) {
        ProtocolloVO protocolloVO = protocollo.getProtocollo();

        form.inizializzaForm();

        form.setAutore(protocolloVO.getRowCreatedUser());
        
        // dati generali
        aggiornaDatiGeneraliForm(protocollo.getProtocollo(), form);

        // protocolli allacciati
        aggiornaAllacciForm(protocollo, form);

        // documenti allegati
        aggiornaAllegatiForm(protocollo, form);

        // allegato principale
        form.setDocumentoPrincipale(protocollo.getDocumentoPrincipale());

        // annotazione
        aggiornaAnnotazioniForm(protocolloVO, form);

        
        // Fascicolo
        aggiornaFascicoloForm(protocollo, form, utente);

        //ProtocolloProcedimentoVO 
        
        // Procedimento
        aggiornaProcedimentoForm(protocollo, form, utente);
        
    }
    

    
    public static void aggiornaDatiGeneraliForm(ProtocolloVO protocollo,
            ProtocolloForm form) {
        Integer id = protocollo.getId();
        if (id != null) {
            form.setProtocolloId(id.intValue());
            form.setDataRegistrazione(DateUtil.formattaDataOra(protocollo
                    .getDataRegistrazione().getTime()));
            form.setUfficioProtocollatoreId(protocollo
                    .getUfficioProtocollatoreId());
            form.setUtenteProtocollatoreId(protocollo.getUtenteProtocollatoreId());
            form.setCaricaProtocollatoreId(protocollo.getCaricaProtocollatoreId());
            form.setStato(protocollo.getStatoProtocollo());
        } else {
            form.setProtocolloId(0);
        }
        
        form.setAooId(protocollo.getAooId());
        form.setFlagTipo(protocollo.getFlagTipo());
        form.setNumero(protocollo.getNumProtocollo());
        form.setNumProtocolloEmergenza(protocollo.getNumProtocolloEmergenza());
        form.setStato(protocollo.getStatoProtocollo());
        form.setNumeroProtocollo(protocollo.getNumProtocollo() + "");
       
        form.setTipoDocumentoId(protocollo.getTipoDocumentoId());
        Date dataDoc = protocollo.getDataDocumento();
        form.setDataDocumento(dataDoc == null ? null : DateUtil
                .formattaData(dataDoc.getTime()));
        Date dataRic = protocollo.getDataRicezione();
        form.setDataRicezione(dataRic == null ? null : DateUtil
                .formattaData(dataRic.getTime()));
        form.setRiservato(protocollo.isRiservato());
        form.setFatturaElettronica(protocollo.isFatturaElettronica());
        form.setFlagAnomalia(protocollo.isFlagAnomalia());
        form.setFlagRepertorio(protocollo.isFlagRepertorio());
        form.setOggetto(protocollo.getOggetto());
        form.setOggettoGenerico(protocollo.getOggetto());

        form.setChiaveAnnotazione(protocollo.getChiaveAnnotazione());
        form.setPosizioneAnnotazione(protocollo.getPosizioneAnnotazione());
        form.setDescrizioneAnnotazione(protocollo.getDescrizioneAnnotazione());
        // dati protocollo annullato
        Date dataAnnullamento = protocollo.getDataAnnullamento();
        form.setDataAnnullamento(dataAnnullamento == null ? null : DateUtil
                .formattaData(dataAnnullamento.getTime()));
        form.setProvvedimentoAnnullamento(protocollo
                .getProvvedimentoAnnullamento());
        form.setNotaAnnullamento(protocollo.getNotaAnnullamento());
        form.setVersione(protocollo.getVersione());
        form.setEstremiAutorizzazione(protocollo.getEstermiAutorizzazione());
    }

    private static void aggiornaAllacciForm(Protocollo protocollo, ProtocolloForm form) {
        for (Iterator<AllaccioVO> i = protocollo.getAllacci().iterator(); i.hasNext();) {
            AllaccioVO allaccio =  i.next();
            form.allacciaProtocollo(allaccio);
        }
    }

    private static void aggiornaAllegatiForm(Protocollo protocollo, ProtocolloForm form) {
        for (Iterator<DocumentoVO> i = protocollo.getAllegati().values().iterator(); i
                .hasNext();) {
            DocumentoVO documentoVO =i.next();
            form.allegaDocumento(documentoVO);
        }
    }

    private static void aggiornaAnnotazioniForm(ProtocolloVO protocollo,
            ProtocolloForm form) {
        form.setDescrizioneAnnotazione(protocollo.getDescrizioneAnnotazione());
        form.setPosizioneAnnotazione(protocollo.getPosizioneAnnotazione());
        form.setChiaveAnnotazione(protocollo.getChiaveAnnotazione());
    }

    private static void aggiornaFascicoloForm(Protocollo protocollo,
            ProtocolloForm form, Utente utente) {
       if (protocollo != null && protocollo.getFascicoli() != null) {
            for (Iterator<FascicoloVO> i = protocollo.getFascicoli().iterator(); i.hasNext();) {
                FascicoloVO fascicolo =  i.next();
                if(utente.getUfficioInUso()==fascicolo.getUfficioIntestatarioId() || MenuDelegate.getInstance().isChargeEnabledByUniqueName(utente, "access_all_folders"))
                	fascicolo.setOwner(true);
                form.aggiungiFascicoloOld(fascicolo);
               
            }
        }
    }

    private static void aggiornaProcedimentoForm(Protocollo protocollo,
            ProtocolloForm form, Utente utente) {
    	
        if (protocollo != null) {
            Collection<ProtocolloProcedimentoVO> procedimenti = protocollo.getProcedimenti();
            if (procedimenti != null) {
                for (Iterator<ProtocolloProcedimentoVO> i = procedimenti.iterator(); i.hasNext();) {
                    ProtocolloProcedimentoVO procedimento =  i
                       .next();
                    form.aggiungiProcedimento(procedimento);
                }
            }
        }
    }
}

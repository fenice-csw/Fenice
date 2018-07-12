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

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.TitoliDestinatarioDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloUscitaForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.StringUtil;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;


public class AggiornaProtocolloUscitaForm extends AggiornaProtocolloForm {

	 /**
     * Aggiorna il form prendendo i dati dal model
     */
    public static void aggiornaForm(Protocollo protocollo, ProtocolloForm form,
            HttpSession session,boolean daMail,Utente utente) {
    	//if()
    	AggiornaProtocolloForm.aggiorna(protocollo, form, session,daMail,utente);

        ProtocolloUscita protocolloUscita = (ProtocolloUscita) protocollo;
        ProtocolloUscitaForm puForm = (ProtocolloUscitaForm) form;

        // mittente
        aggiornaMittenteForm(protocolloUscita, puForm);

        // destinatari
        aggiornaDestinatariForm(protocolloUscita, puForm);

        // stato protocollo
        aggiornaStatoProtocolloForm(protocolloUscita, puForm, session);

        // argomento titolario
        aggiornaTitolarioForm(protocollo.getProtocollo(), puForm,
                (Utente) session.getAttribute(Constants.UTENTE_KEY));

        // aggiorna dati protocollo in risposta
        puForm.setRispostaId(protocollo.getProtocollo().getRispostaId());
        puForm.setFlagStatoScarico(protocollo.getProtocollo()
                .getStatoProtocollo());
        puForm.setFascicoloInvioId(protocolloUscita.getFascicoloInvioId());
        puForm.setDocumentoInvioId(protocolloUscita.getDocumentoInvioId());
        
        if(protocolloUscita.getIntDocEditor()!=0)
        	puForm.setIntDocEditor(protocolloUscita.getIntDocEditor());
        
        if(protocolloUscita.isFromArchivio()){
        	form.removeFascicoliOld();
        	aggiornaFascicoloDaArchivioForm(protocollo, puForm,
                    (Utente) session.getAttribute(Constants.UTENTE_KEY));
        }
        if(protocolloUscita.getProtocollo().getDataScadenza()!=null)
        	puForm.setDataScadenza(DateUtil.formattaData(protocolloUscita.getProtocollo().getDataScadenza().getTime()));
        puForm.setEstremiAutorizzazione(protocolloUscita.getProtocollo().getEstermiAutorizzazione());
       // isDocumentoReadable(utente, form);
       form.setDocumentoReadable(true);
        
    }
    
    
    
    // Metodi che aggionano il Form a partire dal model
    private static void aggiornaFascicoloDaArchivioForm(Protocollo protocollo,
            ProtocolloUscitaForm form, Utente utente) {
       if (protocollo != null && protocollo.getFascicoli() != null) {
            for (Iterator<FascicoloVO> i = protocollo.getFascicoli().iterator(); i.hasNext();) {
                FascicoloVO fascicolo = i.next();
                fascicolo.setOwner(true);
                form.aggiungiFascicolo(fascicolo);
               
            }
        }
    }


    private static void aggiornaMittenteForm(ProtocolloUscita protocollo,
            ProtocolloUscitaForm form) {
        AssegnatarioVO mittente = protocollo.getMittente();
        if (mittente != null && !"R".equals(protocollo.getProtocollo().getFlagTipo())) {
            Organizzazione org = Organizzazione.getInstance();
            Ufficio uff = org.getUfficio(mittente.getUfficioAssegnatarioId());
            Utente ute = org.getUtente(mittente.getUtenteAssegnatarioId());
            form.setMittente(newMittente(uff, ute));
        }
    }

    public static void aggiornaDestinatarioForm(String id, ProtocolloUscitaForm form) {
        DestinatarioView destinatario = new DestinatarioView();
        form.setFlagConoscenza(false);
        destinatario = form.getDestinatario(id);
        form.setIdx(Integer.parseInt(id));
        form.setTipoDestinatario(destinatario.getFlagTipoDestinatario());
        form.setNominativoDestinatario(destinatario.getDestinatario());
        String citta = "";
        if (destinatario.getCapDestinatario() != null) {
            citta = destinatario.getCapDestinatario();
            form.setCapDestinatario(destinatario.getCapDestinatario());
        }
        if (destinatario.getCitta() != null) {
            if (citta.equals("")) {
                citta = destinatario.getCitta();
            } else {
                citta = citta + ' ' + destinatario.getCitta();
            }
        }
        form.setCitta(StringUtil.getStringa(destinatario.getCitta()));
        form.setIndirizzoCompleto(StringUtil.getStringa(citta));
        // }
        form.setEmailDestinatario(destinatario.getEmail());
        form.setIndirizzoDestinatario(destinatario.getIndirizzo());
        form.setCapDestinatario(destinatario.getCapDestinatario());
        form.setMezzoSpedizioneId(destinatario.getMezzoSpedizioneId());
        form.setTitoloDestinatario(TitoliDestinatarioDelegate.getInstance()
                .getTitoloDestinatario(destinatario.getTitoloId())
                .getDescription());
        form.setTitoloId(destinatario.getTitoloId());
        form.setDataSpedizione(destinatario.getDataSpedizione());
        form.setFlagConoscenza(destinatario.getFlagConoscenza());
        form.setNote(destinatario.getNote());
        form.setNoteDestinatario(destinatario.getNote());
        form.setDestinatarioMezzoId(destinatario.getDestinatarioMezzoId());
    }

    private static void aggiornaDestinatariForm(ProtocolloUscita protocollo,
            ProtocolloUscitaForm form) {
        for (Iterator<DestinatarioVO> i = protocollo.getDestinatari().iterator(); i.hasNext();) {
            DestinatarioVO destinatario = i.next();
            DestinatarioView dest = new DestinatarioView();
            dest.setTitoloId(destinatario.getTitoloId());
            if (destinatario.getTitoloId() > 0) {
                dest.setTitoloDestinatario(TitoliDestinatarioDelegate
                        .getInstance().getTitoloDestinatario(
                                destinatario.getTitoloId()).getDescription());
            }
            dest.setDestinatario(destinatario.getDestinatario());
            dest
                    .setFlagTipoDestinatario(destinatario
                            .getFlagTipoDestinatario());
            if (destinatario.getCitta() != null) {
                dest.setCitta(destinatario.getCitta());
            }

            dest.setEmail(destinatario.getEmail());
            if (destinatario.getCodicePostale() != null) {
                dest.setCapDestinatario(destinatario.getCodicePostale());
            }
            if (destinatario.getIndirizzo() != null) {
                dest.setIndirizzo(destinatario.getIndirizzo());
            }
            dest.setMezzoSpedizioneId(destinatario.getMezzoSpedizioneId());
            dest.setMezzoDesc(destinatario.getMezzoDesc());

            Date dataSped = destinatario.getDataSpedizione();
            if (dataSped != null) {
                dest.setDataSpedizione(DateUtil
                        .formattaData(dataSped.getTime()));
            }
            dest.setFlagConoscenza(destinatario.getFlagConoscenza());
            dest.setFlagPresso(destinatario.getFlagPresso());
            dest.setFlagPEC(destinatario.getFlagPEC());
            dest.setNote(destinatario.getNote());
            dest.setPrezzoSpedizione(destinatario.getPrezzo());
            form.aggiungiDestinatario(dest);
        }
    }

    public static void aggiungiDestinatariListaDistribuzioneForm(List<SoggettoVO> soggetti,
            ProtocolloUscitaForm form) {
        for (Iterator<SoggettoVO> i = soggetti.iterator(); i.hasNext();) {
            SoggettoVO destinatario =  i.next();
            DestinatarioView dest = new DestinatarioView();
            LookupDelegate.getInstance();
			if (LookupDelegate.tipiPersona[0].getTipo().equals(
                    destinatario.getTipo())) {
                dest.setDestinatario(destinatario.getCognome() + " "
                        + destinatario.getNome());
            } else {
                dest.setDestinatario(destinatario.getDescrizioneDitta());
            }
            dest.setFlagTipoDestinatario(destinatario.getTipo());
            dest.setCitta(destinatario.getIndirizzo().getComune());
            dest.setEmail(destinatario.getIndirizzoEMail());
            dest.setIndirizzo(destinatario.getIndirizzo().getToponimo());
            dest.setCapDestinatario(destinatario.getIndirizzo().getCap());
            dest.setNote(destinatario.getNote());
            form.aggiungiDestinatario(dest);
        }
    }

    private static void aggiornaStatoProtocolloForm(ProtocolloUscita protocollo,
            ProtocolloUscitaForm form, HttpSession session) {
        Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
        form.setModificabile(ProtocolloBO.isModificable(protocollo, utente));
        if (form.isModificabile()
                || !protocollo.getProtocollo().isRiservato()
                || (protocollo.getProtocollo().isRiservato() && utente
                        .getCaricaInUso()== protocollo
                        .getProtocollo().getCaricaProtocollatoreId()) || protocollo.getMittente().getUtenteAssegnatarioId()==utente.getValueObject().getId()) {
            form.setDocumentoVisibile(true);
        } else {
            form.setDocumentoVisibile(false);
        }

    }
    
    public static AssegnatarioView newMittente(Ufficio ufficio, Utente utente) {
        UfficioVO uffVO = ufficio.getValueObject();
        AssegnatarioView mittente = new AssegnatarioView();
        mittente.setUfficioId(uffVO.getId().intValue());
        mittente.setDescrizioneUfficio(ufficio.getPath());
        mittente.setNomeUfficio(uffVO.getDescription());
        if (utente != null) {
            UtenteVO uteVO = utente.getValueObject();
            mittente.setUtenteId(uteVO.getId().intValue());
            mittente.setNomeUtente(uteVO.getFullName());
        }
        return mittente;
    }
    
    private static void aggiornaTitolarioForm(ProtocolloVO protocollo,
            ProtocolloUscitaForm form, Utente utente) {
        int titolarioId = protocollo.getTitolarioId();
        impostaTitolario(form, utente, titolarioId);
    }
    
    public static void impostaTitolario(ProtocolloUscitaForm form, Utente utente,
            int titolarioId) {
        int ufficioId = utente.getUfficioInUso();
        /*
        if (utente.getAreaOrganizzativa().getDipendenzaTitolarioUfficio() == 1) {
            ufficioId = form.getMittente().getUfficioId();
        }
        */
        TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);
    }

}

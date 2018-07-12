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

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.compit.fenice.mvc.vo.protocollo.DomandaVO;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.bo.TitolarioBO;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.helper.AllaccioView;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.lookup.SoggettoVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;
import it.flosslab.mvc.business.OggettarioDelegate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class AggiornaProtocolloIngressoForm extends AggiornaProtocolloForm {

	
	public static void aggiorna(Protocollo protocollo, ProtocolloForm form,
			HttpSession session, boolean mail,Utente utente) {
		AggiornaProtocolloForm.aggiorna(protocollo, form, session,mail,utente);
		ProtocolloIngresso protocolloIngresso = (ProtocolloIngresso) protocollo;
		ProtocolloIngressoForm piForm = (ProtocolloIngressoForm) form;
		piForm.setMessaggioEmailId(protocolloIngresso.getProtocollo().getEmailId());
		piForm.setMessaggioEmailUfficioId(protocolloIngresso.getProtocollo().getEmailUfficioId());
		// stato protocollo
		aggiornaStatoProtocolloForm(protocolloIngresso, piForm, session);
		if (mail)
			aggiornaMittenteMail(protocolloIngresso, piForm);
		else
			aggiornaMittenteForm(protocolloIngresso, piForm);
		
		// assegnatari
		aggiornaAssegnatariForm(protocolloIngresso, piForm, utente);
		// titolario
		aggiornaTitolarioForm(protocolloIngresso.getProtocollo(), piForm,
				(Utente) session.getAttribute(Constants.UTENTE_KEY));
		form.setDocumentoReadable(true);
		
	}

	
	
	
	public static void aggiornaFatture(Protocollo protocollo, ProtocolloForm form,
			HttpSession session,Utente utente) {
		AggiornaProtocolloForm.aggiorna(protocollo, form, session,false,utente);
		ProtocolloIngresso protocolloIngresso = (ProtocolloIngresso) protocollo;
		ProtocolloIngressoForm piForm = (ProtocolloIngressoForm) form;
		piForm.setMessaggioEmailId(protocolloIngresso.getProtocollo().getEmailId());
		// stato protocollo
		aggiornaStatoProtocolloForm(protocolloIngresso, piForm, session);
		// mittente
		aggiornaMittenteForm(protocolloIngresso, piForm);
		// assegnatari
		aggiornaAssegnatariForm(protocolloIngresso, piForm, utente);
		// titolario
		aggiornaTitolarioForm(protocolloIngresso.getProtocollo(), piForm,
				(Utente) session.getAttribute(Constants.UTENTE_KEY));
		//piForm.set
	}

	
	public static void aggiornaDaDomandaErsu(Protocollo protocollo, ProtocolloForm form,DomandaVO domanda,HttpSession session,Utente utente) {
		AggiornaProtocolloForm.aggiorna(protocollo, form, session,false,utente);
		ProtocolloIngresso protocolloIngresso = (ProtocolloIngresso) protocollo;
		ProtocolloIngressoForm piForm = (ProtocolloIngressoForm) form;

		aggiornaStatoProtocolloForm(protocolloIngresso, piForm, session);
		piForm.setDataDocumento(DateUtil.formattaData(DateUtil.getUltimoGiornoAnnoCorrente().getTime()));
		piForm.setOggettoGenerico(domanda.getOggetto());
		piForm.setOggettoSelezionato(0);
		piForm.setNumProtocolloMittente(domanda.getIdDomanda());
		piForm.setDataProtocolloMittente(DateUtil.formattaData(domanda.getDataIscrizione().getTime()));
		
		aggiornaMittenteErsu(domanda, piForm);
		
		Map<Integer, AssegnatarioVO> assegnatari=OggettarioDelegate.getInstance().getAssegnatari(domanda.getOggetto());
		Organizzazione org = Organizzazione.getInstance();
		List<String> assCompetenti = new ArrayList<String>();

		for(AssegnatarioVO vo:assegnatari.values()){
			
			
			AssegnatarioView ass = new AssegnatarioView();
			Ufficio uff = org.getUfficio(vo.getUfficioAssegnatarioId());
			ass.setCaricaAssegnanteId(utente.getCaricaInUso());
			ass.setUfficioAssegnanteId(utente.getUfficioInUso());
			ass.setUfficioId(vo.getUfficioAssegnatarioId());
			ass.setNomeUfficio(uff.getValueObject().getDescription());
			ass.setDescrizioneUfficio(uff.getPath());
			//ProtocolloIngressoForm pForm = (ProtocolloIngressoForm) form;
			ass.setStato('S');
			piForm.setUfficioSelezionatoId(0);
			piForm.setTitolario(null);
			piForm.aggiungiAssegnatario(ass);
			piForm.setAssegnatarioCompetente(ass.getKey());
			assCompetenti.add(ass.getKey());
			ass.setPresaVisione(false);
			ass.setLavorato(false);
			
		}
		piForm.setAssegnatariCompetenti(assCompetenti.toArray(new String[assCompetenti.size()]));
		
	}
	
	private static void aggiornaMittenteForm(ProtocolloIngresso protocollo,
			ProtocolloIngressoForm form) {
		form.getMittente().setTipo(protocollo.getProtocollo().getFlagTipoMittente());
		form.setMittenti(protocollo.getProtocollo().getMittenti());
		SoggettoVO mittente = new SoggettoVO('M');
		if(protocollo.getProtocollo().getDataProtocolloMittente()!=null)
			form.setDataProtocolloMittente(DateUtil.formattaData(protocollo.getProtocollo().getDataProtocolloMittente().getTime()));
		form.setNumProtocolloMittente(protocollo.getProtocollo().getNumProtocolloMittente());
		mittente.getIndirizzo().setToponimo(protocollo.getProtocollo().getMittenteIndirizzo());
		mittente.getIndirizzo().setComune(protocollo.getProtocollo().getMittenteComune());
		mittente.getIndirizzo().setProvinciaId(protocollo.getProtocollo().getMittenteProvinciaId());
		mittente.getIndirizzo().setCap(protocollo.getProtocollo().getMittenteCap());
		
		form.setMittente(mittente);
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
	
	private static void aggiornaMittenteErsu(DomandaVO domanda,
			ProtocolloIngressoForm form) {
		form.getMittente().setTipo("M");
		SoggettoVO mittente = null;
		mittente = new SoggettoVO('F');
		mittente.setCognome(domanda.getCognome());
		mittente.setNome(domanda.getNome());
		mittente.getIndirizzo().setToponimo(
				domanda.getIndirizzo());
		mittente.getIndirizzo().setComune(
				domanda.getComune());
		mittente.getIndirizzo().setProvinciaId(
				LookupDelegate.getInstance().getProvinciaIdFromCodiProv(domanda.getProvincia()));
		mittente.getIndirizzo().setCap(
				domanda.getCap());
		mittente.setComuneNascita(domanda.getComuneNascita());
		mittente.setDataNascita(domanda.getDataNascita());
		mittente.setCodiceFiscale(domanda.getMatricola());
		form.getMittenti().add(mittente);
	}
	
	private static void aggiornaMittenteMail(ProtocolloIngresso protocollo,
			ProtocolloIngressoForm form) {
		form.getMittente().setTipo("M");
		form.setNumProtocolloMittente(protocollo.getProtocollo().getNumProtocolloMittente());
		if(protocollo.getProtocollo().getDataProtocolloMittente()!=null)
			form.setDataProtocolloMittente(DateUtil.formattaData(protocollo.getProtocollo().getDataProtocolloMittente().getTime()));
		form.setMittenti(protocollo.getProtocollo().getMittenti());
	}
	
	private static void aggiornaAssegnatariForm(ProtocolloIngresso protocollo,
			ProtocolloIngressoForm form, Utente utente) {
		form.setInUfficioAssegnatario(false);
		Organizzazione org = Organizzazione.getInstance();
		for (Iterator<AssegnatarioVO> i = protocollo.getAssegnatari().iterator(); i.hasNext();) {
			AssegnatarioVO assegnatario =  i.next();
			AssegnatarioView ass = new AssegnatarioView();
			int uffId = assegnatario.getUfficioAssegnatarioId();
			ass.setUfficioId(uffId);
			Ufficio uff = org.getUfficio(uffId);
			if (uff != null) {
				ass.setNomeUfficio(uff.getValueObject().getDescription());
				if(!form.isInUfficioAssegnatario())
					form.setInUfficioAssegnatario(uff.getValueObject().getId()==utente.getUfficioInUso());
			}
			if (assegnatario.getCaricaAssegnatarioId() != 0) {
				int caricaId = assegnatario.getCaricaAssegnatarioId();
				CaricaVO carica = org.getCarica(caricaId);
				ass.setUtenteId(carica.getUtenteId());
				Utente ute = org.getUtente(carica.getUtenteId());
				if (ute != null) {
					if (carica.isAttivo())
						ass.setNomeUtente(ute.getValueObject()
								.getCaricaFullName());
					else
						ass.setNomeUtente(ute.getValueObject()
								.getCaricaFullNameNonAttivo());
				} else
					ass.setNomeUtente(carica.getNome());
			}
			ass.setStato(assegnatario.getStatoAssegnazione());
			ass.setCaricaAssegnanteId(assegnatario.getCaricaAssegnanteId());
			ass.setUfficioAssegnanteId(assegnatario.getUfficioAssegnanteId());
			form.aggiungiAssegnatario(ass);
			if (assegnatario.isCompetente()) {
				form.setAssegnatarioCompetente(ass.getKey());
				form.setMsgAssegnatarioCompetente(assegnatario
						.getMsgAssegnatarioCompetente());
			}
			ass.setPresaVisione(assegnatario.isPresaVisione());
			ass.setLavorato(assegnatario.isLavorato());
			ass.setTitolareProcedimento(assegnatario.isTitolareProcedimento());
			if(assegnatario.isTitolareProcedimento()){
				form.setTitolareProcedimento(ass.getKey());
			}
		}

	}
	
	public static void aggiornaAllaccioForm(ProtocolloForm form, Protocollo allaccio,Utente utente){
			ProtocolloDelegate delegate = ProtocolloDelegate.getInstance();
			Collection<AllaccioView> c = delegate.getProtocolliAllacciabili(utente, allaccio.getProtocollo().getNumProtocollo(), allaccio.getProtocollo().getNumProtocollo(),allaccio.getProtocollo().getAnnoRegistrazione() , 0);
			if (c != null && c.size() > 0) {
				Iterator<AllaccioView> it = c.iterator();
				while (it.hasNext()) {
					AllaccioView allaccioView =  it.next();
					AllaccioVO allaccioVO = new AllaccioVO();
					if (allaccioView != null && allaccioView.getNumProtAllacciato() > 0) {
						allaccioVO.setProtocolloAllacciatoId(allaccioView
								.getProtAllacciatoId());
						allaccioVO.setAllaccioDescrizione(allaccio.getProtocollo().getNumProtocollo()
								+ "/"
								+ allaccio.getProtocollo().getAnnoRegistrazione()
								+ " ("
								+ allaccioView.getTipoProtocollo() + ")");
						form.allacciaProtocollo(allaccioVO);
					}
				}
			} 
	}
	
	
	private static void aggiornaStatoProtocolloForm(
			ProtocolloIngresso protocollo, ProtocolloIngressoForm form,
			HttpSession session) {
		Utente utente = (Utente) session.getAttribute(Constants.UTENTE_KEY);
		form.setModificabile(ProtocolloBO.isModificable(protocollo, utente));
		if (form.isModificabile()|| !protocollo.getProtocollo().isRiservato()
				|| (protocollo.getProtocollo().isRiservato() && utente.getCaricaInUso() == protocollo.getProtocollo().getCaricaProtocollatoreId())
				|| (protocollo.getProtocollo().isRiservato() && ProtocolloBO.isAssegnatarioReserved(protocollo, utente))) {
			form.setDocumentoVisibile(true);
		} else {
			form.setDocumentoVisibile(false);
		}

	}

	private static void aggiornaTitolarioForm(ProtocolloVO protocollo,
			ProtocolloIngressoForm form, Utente utente) {
		int titolarioId = protocollo.getTitolarioId();
		impostaTitolario(form, utente, titolarioId);
	}

	public static void impostaTitolario(ProtocolloIngressoForm form,
			Utente utente, int titolarioId) {
		int ufficioId = utente.getUfficioInUso();
		if (form.isDipTitolarioUfficio()) {
			Iterator<AssegnatarioView> i = form.getAssegnatari().iterator();
			while (i.hasNext()) {
				AssegnatarioView assegnatario = i.next();
				if (assegnatario.isCompetente()) {
					ufficioId = assegnatario.getUfficioId();
					break;
				}
			}
		}
		TitolarioBO.impostaTitolario(form, ufficioId, titolarioId);
	}

}

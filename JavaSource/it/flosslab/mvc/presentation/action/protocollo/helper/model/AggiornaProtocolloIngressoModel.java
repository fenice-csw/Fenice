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

import it.compit.fenice.mvc.business.CaricaDelegate;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloIngressoForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.finsiel.siged.util.DateUtil;

import java.util.Collection;
import java.util.Date;

public class AggiornaProtocolloIngressoModel extends AggiornaProtocolloModel {

	/**
	 * Aggiorna il model prendendo i dati dal form. Da invocare solo dopo la
	 * validazione
	 */

	public static void aggiorna(ProtocolloIngressoForm form,
			ProtocolloIngresso protocollo, Utente utente) {
		AggiornaProtocolloModel.aggiorna(form, protocollo);
		protocollo.setEstremiAutorizzazione(form.getEstremiAutorizzazione());
		if (form.getMessaggioEmailId() != null)
			protocollo.getProtocollo().setEmailId(form.getMessaggioEmailId().intValue());
		if (form.getMessaggioEmailUfficioId() != null)
			protocollo.getProtocollo().setEmailUfficioId(form.getMessaggioEmailUfficioId().intValue());
		aggiornaMittenteModel(form, protocollo.getProtocollo());
		aggiornaAssegnatariModel(form, protocollo, utente);
	}

	
	private static void aggiornaMittenteModel(ProtocolloIngressoForm form,
			ProtocolloVO protocollo) {
		protocollo.setFlagTipoMittente("M");
		protocollo.setDataProtocolloMittente(DateUtil.toDate(form
				.getDataProtocolloMittente()));
		protocollo.setNumProtocolloMittente(form.getNumProtocolloMittente());
		protocollo.setMittenti(form.getMittenti());
	}

	private static void aggiornaAssegnatariModel(ProtocolloIngressoForm form,
			ProtocolloIngresso protocollo, Utente utente) {
		protocollo.removeAssegnatari();
		protocollo.setMsgAssegnatarioCompetente(form
				.getMsgAssegnatarioCompetente());
		UtenteVO uteVO = utente.getValueObject();
		Collection<AssegnatarioView> assegnatari = form.getAssegnatari();
		if (assegnatari != null) {
			for (AssegnatarioView ass : assegnatari) {
				AssegnatarioVO assegnatario = new AssegnatarioVO();
				Date now = new Date();
				assegnatario.setDataAssegnazione(now);
				assegnatario.setDataOperazione(now);
				assegnatario.setRowCreatedUser(uteVO.getUsername());
				assegnatario.setRowUpdatedUser(uteVO.getUsername());
				assegnatario.setUfficioAssegnanteId(ass.getUfficioAssegnanteId());
				assegnatario.setCaricaAssegnanteId(ass.getCaricaAssegnanteId());
				assegnatario.setUfficioAssegnatarioId(ass.getUfficioId());
				if (ass.getUtenteId() != 0) {
					assegnatario.setCaricaAssegnatarioId(CaricaDelegate.getInstance()
							.getCaricaByUtenteAndUfficio(ass.getUtenteId(),
									ass.getUfficioId()).getCaricaId());
					assegnatario.setUtenteAssegnatarioId(ass.getUtenteId());
				} else {
					assegnatario.setCaricaAssegnatarioId(0);
					assegnatario.setUtenteAssegnatarioId(0);
				}
				if (form.isCompetente(ass)) {
					assegnatario.setCompetente(true);
					if (ass.getStato() != 'R') {
						assegnatario.setMsgAssegnatarioCompetente(form
								.getMsgAssegnatarioCompetente());
						assegnatario.setStatoAssegnazione('S');
					} else {
						assegnatario.setStatoAssegnazione('R');
						assegnatario.setMsgAssegnatarioCompetente(ass.getMsg());
					}
					if (ass.getUtenteId() > 0)
						protocollo.getProtocollo().setStatoProtocollo("N");
					else
						protocollo.getProtocollo().setStatoProtocollo("S");
					if (protocollo.getFascicoli().size() > 0)
						protocollo.getProtocollo().setStatoProtocollo("A");

				}
				assegnatario.setPresaVisione(ass.isPresaVisione());
				assegnatario.setLavorato(ass.isLavorato());
				if(form.getTitolareProcedimento()!=null && form.getTitolareProcedimento().equals(ass.getKey()))
					assegnatario.setTitolareProcedimento(true);
				protocollo.aggiungiAssegnatario(assegnatario);

			}
		}
		if(form.isVisionato())
			protocollo.getProtocollo().setStatoProtocollo("V");
	}
}

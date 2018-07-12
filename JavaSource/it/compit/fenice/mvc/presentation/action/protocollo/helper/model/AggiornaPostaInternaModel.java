package it.compit.fenice.mvc.presentation.action.protocollo.helper.model;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.compit.fenice.mvc.business.CaricaDelegate;
import it.compit.fenice.mvc.presentation.actionform.protocollo.PostaInternaForm;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;
import it.flosslab.mvc.presentation.action.protocollo.helper.model.AggiornaProtocolloModel;

import java.util.Collection;
import java.util.Date;

public class AggiornaPostaInternaModel extends AggiornaProtocolloModel {

	public static void aggiorna(PostaInternaForm form, PostaInterna protocollo,
			Utente utente) {
		AggiornaProtocolloModel.aggiorna(form, protocollo);
		aggiornaMittenteModel(form, protocollo.getProtocollo());
		aggiornaAssegnatariModel(form, protocollo, utente);
		protocollo.setIntDocEditor(form.getIntDocEditor());
		protocollo.setRispostaId(form.getRispostaId());
		protocollo.setDocRepertorioId(form.getDocRepertorioId());

	}

	
	
	private static void aggiornaMittenteModel(PostaInternaForm form,
			ProtocolloVO protocollo) {
		AssegnatarioView mittente = form.getMittente();
		if (mittente != null) {
			protocollo.setUfficioMittenteId(mittente.getUfficioId());
			protocollo.setUtenteMittenteId(mittente.getUtenteId());
			Organizzazione org = Organizzazione.getInstance();
			Utente ute = org.getUtente(mittente.getUtenteId());
			protocollo.setCognomeMittente(ute.getValueObject().getFullName());
			protocollo.setFlagTipoMittente("F");

		}
	}

	private static void aggiornaAssegnatariModel(PostaInternaForm form,
			PostaInterna protocollo, Utente utente) {
		protocollo.removeDestinatari();
		protocollo.setMsgAssegnatarioCompetente(form
				.getMsgDestinatarioCompetente());
		UtenteVO uteVO = utente.getValueObject();
		Collection<AssegnatarioView> assegnatari = form.getDestinatari();
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
					assegnatario.setMsgAssegnatarioCompetente(form.getMsgDestinatarioCompetente());
					
					if (ass.getUtenteId() > 0)
						protocollo.getProtocollo().setStatoProtocollo("N");
					else
						protocollo.getProtocollo().setStatoProtocollo("S");
					if (protocollo.getFascicoli().size() > 0)
						protocollo.getProtocollo().setStatoProtocollo("A");
				}
				assegnatario.setPresaVisione(ass.isPresaVisione());
				assegnatario.setLavorato(ass.isLavorato());
				protocollo.aggiungiDestinatario(assegnatario);
			}
		}
	}
}

package it.compit.fenice.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.mvc.presentation.actionform.protocollo.ProtocolloForm;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class PostaInternaForm extends ProtocolloForm {

	private static final long serialVersionUID = 1L;

	private Map<String,AssegnatarioView> destinatari;

	private Collection<AssegnatarioView> destinatariPrecedenti; 

	private String[] destinatariSelezionatiId;

	private String destinatarioCompetente;

	private String[] destinatariCompetenti;

	private String dataProtocolloMittente;

	private String msgDestinatarioCompetente;

	private AssegnatarioView mittente;

	private String returnPage;

	private int intDocEditor;
	
	private int rispostaId;
	
	private int docRepertorioId;

	public int getDocRepertorioId() {
		return docRepertorioId;
	}

	public void setDocRepertorioId(int docRepertorioId) {
		this.docRepertorioId = docRepertorioId;
	}

	public int getIntDocEditor() {
		return intDocEditor;
	}

	public void setIntDocEditor(int intDocEditor) {
		this.intDocEditor = intDocEditor;
	}

	public String getReturnPage() {
		return returnPage;
	}

	public void setReturnPage(String retPage) {
		this.returnPage = retPage;
	}

	public PostaInternaForm() {
		super();
		inizializzaForm();
	}

	public void inizializzaForm() {
		super.inizializzaForm();
		super.setFlagTipo("P");
		destinatari = new HashMap<String,AssegnatarioView>();
		destinatariPrecedenti = new ArrayList<AssegnatarioView>();

		setDestinatarioCompetente(null);
		setDestinatariSelezionatiId(null);
		setDataProtocolloMittente(null);
		setMsgDestinatarioCompetente(null);
		getElencoSezioni().add(0, new Sezione("Mittente", true));
		getElencoSezioni()
				.add(1, new Sezione("Destinatari", destinatari, true));
	}

	public void inizializzaFromUscitaForm() {
		super.inizializzaForm();
		super.setFlagTipo("P");
		destinatari = new HashMap<String,AssegnatarioView>();
		destinatariPrecedenti = new ArrayList<AssegnatarioView>();
		setDestinatarioCompetente(null);
		setDestinatariSelezionatiId(null);
		setDataProtocolloMittente(null);
		setMsgDestinatarioCompetente(null);
		getElencoSezioni().add(0, new Sezione("Mittente", true));
		getElencoSezioni()
				.add(1, new Sezione("Destinatari", destinatari, true));
		setSezioneVisualizzata("Destinatari");
	}

	public void inizializzaRipetiForm() {
		super.inizializzaRipetiForm();
		super.setFlagTipo("P");
		getElencoSezioni().add(0, new Sezione("Mittente", true));
		getElencoSezioni()
				.add(1, new Sezione("Destinatari", destinatari, true));

		if ((getDestinatarioCompetente() == null)
				&& (getDestinatariSelezionatiId() == null)) {
			destinatari = new HashMap<String,AssegnatarioView>();
		}
		setMsgDestinatarioCompetente(null);
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);

		if (request.getParameter("salvaAction") != null) {
			if (getDestinatari().size() == 0) {
				errors.add("assegnatari", new ActionMessage(
						"assegnatari_obbligatorio"));
			}
			if (getProtocolloId() != 0 && getVersione() != 0) {
				if (!request.getParameter("salvaAction").equals("Fascicola")
						&& !request.getParameter("salvaAction").equals(
								"Aggiungi il Procedimento")) {
					if (getEstremiAutorizzazione() == null
							|| "".equals(getEstremiAutorizzazione().trim())) {
						errors.add("estremiAutorizzazione", new ActionMessage(
								"campo.obbligatorio", "Estremi autorizzazione",
								""));
					}
				} else if (request.getParameter("salvaAction").equals(
						"Fascicola")) {
					if (getFascicoliProtocollo().size() == 0
							&& getFascicoliProtocolloOld().size() == 0) {
						errors.add("fascicolo", new ActionMessage(
								"fascicolo_obbligatorio"));
					}
				}

			}
		}
		return errors;
	}

	public AssegnatarioView getMittente() {
		return mittente;
	}

	public void setMittente(AssegnatarioView mittente) {
		this.mittente = mittente;
	}

	
	public Collection<AssegnatarioView> getDestinatari() {
		return destinatari.values();
	}

	public void setDestinatari(Map<String,AssegnatarioView> destinatari) {
		this.destinatari = destinatari;
	}

	public String[] getDestinatariSelezionatiId() {
		return destinatariSelezionatiId;
	}

	public void setDestinatariSelezionatiId(String[] destinatariSelezionatiId) {
		this.destinatariSelezionatiId = destinatariSelezionatiId;
	}

	public String getDataProtocolloMittente() {
		return dataProtocolloMittente;
	}

	public void setDataProtocolloMittente(String dataProtocolloMittente) {
		this.dataProtocolloMittente = dataProtocolloMittente;
	}

	public String getDestinatarioCompetente() {
		return destinatarioCompetente;
	}

	public String[] getDestinatariCompetenti() {
		return destinatariCompetenti;
	}

	public String getMsgDestinatarioCompetente() {
		return msgDestinatarioCompetente;
	}

	public void setMsgDestinatarioCompetente(String msgDestinatarioCompetente) {
		this.msgDestinatarioCompetente = msgDestinatarioCompetente;
	}

	public void rimuoviDestinatario(String key) {
		destinatari.remove(key);
	}

	public void aggiungiDestinatario(AssegnatarioView ass) {
		destinatari.put(ass.getKey(), ass);
	}

	public void removeDestinatari() {
		if (destinatari != null)
			destinatari.clear();
	}

	public void setDestinatarioCompetente(String assegnatarioCompetente) {
		this.destinatarioCompetente = assegnatarioCompetente;
		for (Iterator<AssegnatarioView> i = getDestinatari().iterator(); i.hasNext();) {
			AssegnatarioView ass = i.next();
			if (ass.getKey().equals(assegnatarioCompetente)) {
				ass.setCompetente(ass.getKey().equals(assegnatarioCompetente));
			}
		}

	}

	public void setDestinatariCompetenti(String[] assegnatariCompetenti) {
		this.destinatariCompetenti = assegnatariCompetenti;
		for (Iterator<AssegnatarioView> i = getDestinatari().iterator(); i.hasNext();) {
			AssegnatarioView ass =  i.next();
			ass.setCompetente(this.isCompetente(ass));
		}
	}

	public boolean isCompetente(AssegnatarioView ass) {
		if (this.destinatariCompetenti == null && this.destinatari.size() == 1)
			return true;
		for (String assCompetente : this.destinatariCompetenti) {
			if (assCompetente.equals(ass.getKey())) {
				return true;
			}
		}
		return false;
	}

	public Collection<AssegnatarioView> getDestinatariPrecedenti() {
		return destinatariPrecedenti;
	}

	public void setDestinatariPrecedenti(Collection<AssegnatarioView> assPrec) {
		destinatariPrecedenti = assPrec;
	}

	public void aggiungiDestinatarioPrecedente(AssegnatarioView ass) {
		destinatariPrecedenti.add(ass);
	}

	public void rimuoviDestinatarioPrecedente(AssegnatarioView ass) {
		destinatariPrecedenti.removeAll(Collections.singleton(ass));
	}

	public void resetDestinatariPrecedenti() {
		destinatariPrecedenti.clear();
	}

	public int getRispostaId() {
		return rispostaId;
	}

	public void setRispostaId(int rispostaId) {
		this.rispostaId = rispostaId;
	}
	
	

}

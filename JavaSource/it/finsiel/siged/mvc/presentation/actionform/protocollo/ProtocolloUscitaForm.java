package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.NumberUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class ProtocolloUscitaForm extends ProtocolloForm {

	private static final long serialVersionUID = 1L;

	private String nomeDestinatario;
	
	private String cognomeDestinatario;

	public String getNomeDestinatario() {
		return nomeDestinatario;
	}
	
	public String getCognomeDestinatario() {
		return cognomeDestinatario;
	}

	
	public void setNomeDestinatario(String nomeDestinatario) {
		this.nomeDestinatario = nomeDestinatario;
	}

	
	public void setCognomeDestinatario(String cognomeDestinatario) {
		this.cognomeDestinatario = cognomeDestinatario;
	}

	private AssegnatarioView mittente;

    private String dataProtocolloMittente;

    private String numProtocolloMittente;

    // variabili per la gestione del protocollo in Risposta
    private int rispostaId;

    private String flagStatoScarico;

    // DESTINATARIO
    private String destinatarioMezzoId;

    private String tipoDestinatario = "F";

    private String nominativoDestinatario;

    private String citta;

    private String titoloDestinatario;

    private String emailDestinatario;

    private String indirizzoDestinatario;
    
    private String indirizzoCompleto;

    private int idx;

    private String capDestinatario;

    private String dataSpedizione;

    private boolean flagConoscenza = false;
    
    private boolean flagPresso = false;
    
    private boolean flagPEC = false;

    private String[] destinatariSelezionatiId;

    private Map<String,DestinatarioView> destinatari;

    private Map<String,Integer> destinatariIds;

    private int fascicoloInvioId;

    private int documentoInvioId;

    private int titoloId;

    private int mezzoSpedizioneId;

    private String note;
    
    private String noteDestinatario;
  
    private int intDocEditor;
    
    private boolean inoltrabile;
    
    private String dataScadenza;

   // private String returnPage;
    
    /*
    public String getReturnPage() {
		return returnPage;
	}

	public void setReturnPage(String retPage) {
		this.returnPage = retPage;
	}
*/
    
    public String getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}


	public boolean isInoltrabile() {
		return inoltrabile;
	}

	public void setInoltrabile(boolean inoltrabile) {
		this.inoltrabile = inoltrabile;
	}


	public int getIntDocEditor() {
		return intDocEditor;
	}

	public void setIntDocEditor(int intDocEditor) {
		this.intDocEditor = intDocEditor;
	}


	public boolean getFlagPEC() {
		return flagPEC;
	}

	public void setFlagPEC(boolean flagPEC) {
		this.flagPEC = flagPEC;
	}


	public boolean getFlagPresso() {
		return flagPresso;
	}

	public void setFlagPresso(boolean flagPresso) {
		this.flagPresso = flagPresso;
	}

	public String getNoteDestinatario() {
        return noteDestinatario;
    }

    public void setNoteDestinatario(String noteDestinatario) {
        this.noteDestinatario = noteDestinatario;
    }

    public int getTitoloId() {
        return titoloId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTitoloId(int titoloId) {
        this.titoloId = titoloId;
    }

    /**
     * @return Returns the protocolliAllacciati.
     */
    public Collection<DestinatarioView> getDestinatari() {
        return destinatari.values();
    }

    public DestinatarioView getDestinatario(String id) {
        return (DestinatarioView) destinatari.get(id);
    }

    public void aggiungiDestinatario(DestinatarioView destinatario) {
        if (destinatario != null) {
            if (destinatario.getIdx() == 0) {
                int idx = getNextId(destinatariIds);
                destinatario.setIdx(idx);
                destinatari.put(String.valueOf(idx), destinatario);
                destinatariIds.put(String.valueOf(idx), new Integer(idx));
            } else {
                destinatari.put(String.valueOf(destinatario.getIdx()),
                        destinatario);
            }

        }
    }

    private static int getNextId(Map<String,Integer> m) {
        int max = 0;
        Iterator<String> it = m.keySet().iterator();
        while (it.hasNext()) {
            String id =  it.next();
            int cur = NumberUtil.getInt(id);
            if (cur > max)
                max = cur;
        }
        return max + 1;
    }

    public void rimuoviDestinatario(String id) {
        DestinatarioView removed = (DestinatarioView) destinatari.get(id);
        int idx = removed.getIdx();
        destinatari.remove(String.valueOf(removed.getIdx()));
        destinatariIds.remove(String.valueOf(idx));
    }

    public void rimuoviDestinatari() {
        if (destinatari != null) {
            this.destinatari.clear();
            this.destinatariIds.clear();
        }
    }

    public AssegnatarioView getMittente() {
        return mittente;
    }

    public void setMittente(AssegnatarioView assegnatario) {
        this.mittente = assegnatario;
    }

    /**
     * @return Returns the dataProtocolloMittente.
     */
    public String getDataProtocolloMittente() {
        return dataProtocolloMittente;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    /**
     * @param dataProtocolloMittente
     *            The dataProtocolloMittente to set.
     */
    public void setDataProtocolloMittente(String dataProtocolloMittente) {
        this.dataProtocolloMittente = dataProtocolloMittente;
    }

    /**
     * @return Returns the numProtocolloMittente.
     */
    public String getNumProtocolloMittente() {
        return numProtocolloMittente;
    }

    /**
     * @param numProtocolloMittente
     *            The numProtocolloMittente to set.
     */
    public void setNumProtocolloMittente(String numProtocolloMittente) {
        this.numProtocolloMittente = numProtocolloMittente;
    }

    /**
     * @return Returns the destinatarioSelezionatoId.
     */
    public String[] getDestinatariSelezionatiId() {
        return destinatariSelezionatiId;
    }

    /**
     * @param destinatarioSelezionatoId
     *            The destinatarioSelezionatoId to set.
     */
    public void setDestinatariSelezionatiId(String[] destinatarioSelezionatoId) {
        this.destinatariSelezionatiId = destinatarioSelezionatoId;
    }

    /**
     * @return Returns the dataSpedizione.
     */
    public String getDataSpedizione() {
        return dataSpedizione;
    }

    /**
     * @param dataSpedizione
     *            The dataSpedizione to set.
     */
    public void setDataSpedizione(String dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    /**
     * @return Returns the emailDestinatario.
     */
    public String getEmailDestinatario() {
        return emailDestinatario;
    }

    /**
     * @param emailDestinatario
     *            The emailDestinatario to set.
     */
    public void setEmailDestinatario(String emailDestinatario) {
        this.emailDestinatario = emailDestinatario;
    }

    /**
     * @return Returns the flagConoscenza.
     */
    public boolean getFlagConoscenza() {
        return flagConoscenza;
    }

    /**
     * @param flagConoscenza
     *            The flagConoscenza to set.
     */
    public void setFlagConoscenza(boolean flagConoscenza) {
        this.flagConoscenza = flagConoscenza;
    }

    /**
     * @return Returns the nominativoDestinatario.
     */
    public String getNominativoDestinatario() {
    	if(tipoDestinatario.equals("F")){
    		return nomeDestinatario +" "+cognomeDestinatario;
    	}else{
    		return nominativoDestinatario;
    	}
        
    }

    /**
     * @param nominativoDestinatario
     *            The nominativoDestinatario to set.
     */
    public void setNominativoDestinatario(String nominativoDestinatario) {
        this.nominativoDestinatario = nominativoDestinatario;
    }

    /**
     * @return Returns the tipoDestinatario.
     */
    public String getTipoDestinatario() {
        return tipoDestinatario;
    }

    /**
     * @param tipoDestinatario
     *            The tipoDestinatario to set.
     */
    public void setTipoDestinatario(String tipoDestinatario) {
        this.tipoDestinatario = tipoDestinatario;
    }

    /**
     * @return Returns the TitoloDestinatario.
     */
    public String getTitoloDestinatario() {
        return titoloDestinatario;
    }

    /**
     * @param titoloDestinatario
     *            The titoloDestinatario to set.
     */
    public void setTitoloDestinatario(String titoloDestinatario) {
        this.titoloDestinatario = titoloDestinatario;
    }

    /**
     * @param destinatari
     *            The destinatari to set.
     */
    public void setDestinatari(Map<String,DestinatarioView> destinatari) {
        this.destinatari = destinatari;
    }

    public int getRispostaId() {
        return rispostaId;
    }

    public void setRispostaId(int rispostaId) {
        this.rispostaId = rispostaId;
    }

    /**
     * @return Returns the flagStatoScarico.
     */
    public String getFlagStatoScarico() {
        return flagStatoScarico;
    }

    /**
     * @param flagStatoScarico
     *            The flagStatoScarico to set.
     */
    public void setFlagStatoScarico(String flagStatoScarico) {
        this.flagStatoScarico = flagStatoScarico;
    }

    public String getIndirizzoDestinatario() {
        return indirizzoDestinatario;
    }

    public void setIndirizzoDestinatario(String indirizzoDestinatario) {
        this.indirizzoDestinatario = indirizzoDestinatario;
    }

    public int getFascicoloInvioId() {
        return fascicoloInvioId;
    }

    public void setFascicoloInvioId(int fascicoloInvioId) {
        this.fascicoloInvioId = fascicoloInvioId;
    }

    public int getDocumentoInvioId() {
        return documentoInvioId;
    }

    public void setDocumentoInvioId(int documentoInvioId) {
        this.documentoInvioId = documentoInvioId;
    }

    public Collection<SpedizioneVO> getMezziSpedizione() {
        return getLookupDelegateDelegate().getMezziSpedizione(getAooId());
    }

    public Collection<IdentityVO> getTitoliDestinatario() {
        return getLookupDelegateDelegate().getTitoliDestinatario();
    }

    private LookupDelegate getLookupDelegateDelegate() {
        return LookupDelegate.getInstance();
    }

    // ================================================================ //
    // =================== VALIDAZIONE ============================== //
    // ================================================================ //
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setFlagConoscenza(false);
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);

        if (request.getParameter("aggiungiDestinatario") != null) {
        	if(tipoDestinatario.equals("F")){
        		if ("".equals(nomeDestinatario) || "".equals(cognomeDestinatario)) {
	                errors.add("nominativoDestinatario", new ActionMessage(
	                        "destinatario_nome_obbligatorio"));
	            } else if (!"".equals(dataSpedizione)
	                    && !DateUtil.isData(dataSpedizione)) {
	                errors.add("dataSpedizione", new ActionMessage(
	                        "formato.data.errato", "data spedizione", ""));
	            }
        	}else{
	            if ("".equals(nominativoDestinatario)) {
	                errors.add("nominativoDestinatario", new ActionMessage(
	                        "destinatario_nome_obbligatorio"));
	            } else if (!"".equals(dataSpedizione)
	                    && !DateUtil.isData(dataSpedizione)) {
	                errors.add("dataSpedizione", new ActionMessage(
	                        "formato.data.errato", "data spedizione", ""));
	            }
        	}
        } else if (request.getParameter("salvaAction") != null) {
        	
        	if(getFlagTipo().equals("R")){
        		if(getNumProtocolloEmergenza()<=0)
        			errors.add("numProtocolloEmergenza", new ActionMessage("campo.maggiore.zero" ,"Numero protocollo d'emergenza"));
        	}
        	if (getProtocolloId() != 0 && getVersione() != 0) {
				if (!request.getParameter("salvaAction").equals("Fascicola") && !request.getParameter("salvaAction").equals("Aggiungi il Procedimento"))
					if (getEstremiAutorizzazione() == null
							|| "".equals(getEstremiAutorizzazione().trim())) {
						errors.add("estremiAutorizzazione", new ActionMessage(
								"campo.obbligatorio", "Estremi autorizzazione",
								""));
					}

			}
        	
            if (getDestinatari().size() == 0) {
                // ci deve essere almeno un destinatario
                errors.add("destinatari", new ActionMessage(
                        "destinatari_obbligatorio"));
            } else if (getMittente() == null || "".equals(getMittente())) {
                // ci deve essere almeno un mittente
                errors.add("mittente", new ActionMessage(
                        "mittente_obbligatorio"));
            }else if(getFascicoliProtocollo().size()==0 && getFascicoliProtocolloOld().size()==0){
            	 errors.add("fascicolo", new ActionMessage(
                 "fascicolo_obbligatorio"));
            	
            }
        }
        return errors;
    }

    public ActionErrors validateDestinatario(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
      
        if(tipoDestinatario.equals("F")){
    		if ("".equals(nomeDestinatario) || "".equals(cognomeDestinatario)) {
                errors.add("nominativoDestinatario", new ActionMessage(
                        "destinatario_nome_obbligatorio"));
            } else if (!"".equals(dataSpedizione)
                    && !DateUtil.isData(dataSpedizione)) {
                errors.add("dataSpedizione", new ActionMessage(
                        "formato.data.errato", "data spedizione", ""));
            }
    	}else{
            if ("".equals(nominativoDestinatario)) {
                errors.add("nominativoDestinatario", new ActionMessage(
                        "destinatario_nome_obbligatorio"));
            } else if (!"".equals(dataSpedizione)
                    && !DateUtil.isData(dataSpedizione)) {
                errors.add("dataSpedizione", new ActionMessage(
                        "formato.data.errato", "data spedizione", ""));
            }
    	} 
        if (!"".equals(dataSpedizione) && (getMezzoSpedizioneId() == 0)) {
            errors.add("dataSpedizione", new ActionMessage(
                    "selezione.obbligatoria", "il mezzo spedizione",
                    "in presenza della data spedizione"));
        } else if (getMezzoSpedizioneId() != 0
                && (getDataSpedizione() == null || ""
                        .equals(getDataSpedizione().trim()))) {
            errors.add("dataSpedizione", new ActionMessage(
                    "selezione.obbligatoria", "la data spedizione",
                    "in presenza del mezzo spedizione"));
        }
        return errors;
    }

    public ActionErrors validateDestinatari(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        if (destinatari.isEmpty()) {
            errors.add("nominativoDestinatario", new ActionMessage(
                    "destinatario_nome_obbligatorio"));
        } else {
            boolean trovato = false;
            for (Iterator<DestinatarioView> i = destinatari.values().iterator(); i.hasNext();) {
                DestinatarioView dest =  i.next();
                if (dest.getFlagConoscenza()) {
                    trovato = true;
                    break;
                }
            }
            if (!trovato) {
                errors.add("nominativoDestinatario", new ActionMessage(
                        "destinatario_principale"));
            }

        }

        return errors;
    }

    public void inizializzaForm() {
        super.inizializzaForm();
        super.setFlagTipo("U");
        destinatariIds = new HashMap<String,Integer>(2);
        setNumProtocolloMittente(null);
        destinatari = new HashMap<String,DestinatarioView>();
        setDataProtocolloMittente(null);
        inizializzaDestinatarioForm();
        getElencoSezioni().add(0,new Sezione("Mittente", true));
        getElencoSezioni()
                .add(1, new Sezione("Destinatari", destinatari, true));
    }

    public void inizializzaRipetiForm() {
        super.inizializzaRipetiForm();
        super.setFlagTipo("U");
        getElencoSezioni().add(0,new Sezione("Mittente", true));
        getElencoSezioni()
                .add(1, new Sezione("Destinatari", destinatari, true));
        setNumProtocolloMittente(null);
        if (destinatari == null) {
            destinatari = new HashMap<String,DestinatarioView>();
        }
        setDataProtocolloMittente(null);
        inizializzaDestinatarioForm();
    }

    public void inizializzaDestinatarioForm() {
        setDataSpedizione(null);
        setEmailDestinatario(null);
        setCitta(null);
        setIndirizzoDestinatario(null);
        setIndirizzoCompleto(null);
        setFlagConoscenza(false);
        setFlagPresso(false);
        setFlagPEC(false);
        setTitoloDestinatario(null);
        setNominativoDestinatario(null);
        setDestinatarioMezzoId(null);
        setCapDestinatario(null);
        setMezzoSpedizioneId(0);
        setIdx(0);
        setTipoDestinatario("F");
        setIndirizzoDestinatario(null);
        setCapDestinatario(null);
        setNote(null);
        setNoteDestinatario(null);
        setDestinatariSelezionatiId(null);
    }

    // todo: Destinatari mezzo spedizione
    // public String[] getMezzoSpedizioneDestinatarioSelezionatiId() {
    // return mezzoSpedizioneDestinatarioSelezionatiId;
    // }
    //
    // public void setMezzoSpedizioneDestinatarioSelezionatiId(
    // String[] mezzoSpedizioneDestinatarioSelezionatiId) {
    // this.mezzoSpedizioneDestinatarioSelezionatiId =
    // mezzoSpedizioneDestinatarioSelezionatiId;
    // }

    public int getMezzoSpedizioneId() {
        return mezzoSpedizioneId;
    }

    public void setMezzoSpedizioneId(int mezzoId) {
        this.mezzoSpedizioneId = mezzoId;
    }

    public String getDestinatarioMezzoId() {
        return destinatarioMezzoId;
    }

    public void setDestinatarioMezzoId(String destinatarioMezzoId) {
        this.destinatarioMezzoId = destinatarioMezzoId;
    }


    public String getCapDestinatario() {
        return capDestinatario;
    }

    public void setCapDestinatario(String capDestinatario) {
        this.capDestinatario = capDestinatario;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getIndirizzoCompleto() {
        return indirizzoCompleto;
    }

    public void setIndirizzoCompleto(String indirizzoCompleto) {
        this.indirizzoCompleto = indirizzoCompleto;
    }
    
    private String[] destinatariToSaveId;

	/**
	 * @return the destinatariToSaveId
	 */
	public String[] getDestinatariToSaveId() {
		return destinatariToSaveId;
	}

	/**
	 * @param destinatariToSaveId the destinatariToSaveId to set
	 */
	public void setDestinatariToSaveId(String[] destinatariToSaveId) {
		this.destinatariToSaveId = destinatariToSaveId;
	}

}
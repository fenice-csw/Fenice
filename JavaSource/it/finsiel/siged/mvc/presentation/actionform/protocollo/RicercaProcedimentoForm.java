package it.finsiel.siged.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class RicercaProcedimentoForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(RicercaProcedimentoForm.class
            .getName());

    private String anno;

    private String numero;

    private String dataAvvioInizio;

    private String dataAvvioFine;

    private int statoId;

    private Map statiProcedimento = new HashMap(2);

    private String oggettoProcedimento;

    private String posizione;

    private Map posizioniProcedimento = new HashMap(2);
    
    private String note;

    private int aooId;

    // uffici - titolario

    private int utenteSelezionatoId;
    
    private Collection utenti;

    private TitolarioVO titolario;

    private int titolarioPrecedenteId;

    private int titolarioSelezionatoId;

    private Collection titolariFigli;

    private Collection procedimenti;

    private String[] procedimentiSelezionati;
    
    private boolean indietroVisibile;
    
    private String dataScadenzaInizio;

    private String dataScadenzaFine;
    
    private int ufficioRicercaId;
    
    private Utente utenteCorrente;

    private String descrizioneInteressatoDelegato;
    
	private ArrayList<TipoProcedimentoVO> tipiProcedimento;
    
    private int tipoProcedimentoSelezionatoId;
    
    public int getTipoProcedimentoSelezionatoId() {
		return tipoProcedimentoSelezionatoId;
	}
    
    public String getIdTipiProcedimento(){
    	String ids="0,";
    	for(TipoProcedimentoVO t:this.tipiProcedimento)
    		ids+=t.getIdTipo()+",";
    	return ids.substring(0,ids.length()-1);
    }

	public void setTipoProcedimentoSelezionatoId(int tipoProcedimentoSelezionatoId) {
		this.tipoProcedimentoSelezionatoId = tipoProcedimentoSelezionatoId;
	}

	public void setTipiProcedimento(ArrayList<TipoProcedimentoVO> tipiProcedimento) {
		this.tipiProcedimento = tipiProcedimento;
	}

	public ArrayList<TipoProcedimentoVO> getTipiProcedimento() {
		return tipiProcedimento;
	}
    
    public String getDescrizioneInteressatoDelegato() {
		return descrizioneInteressatoDelegato;
	}

	public void setDescrizioneInteressatoDelegato(
			String descrizioneInteressatoDelegato) {
		this.descrizioneInteressatoDelegato = descrizioneInteressatoDelegato;
	}
    
    public int getUfficioRicercaId() {
        return ufficioRicercaId;
    }

    public void setUfficioRicercaId(int ufficioRicercaId) {
        this.ufficioRicercaId = ufficioRicercaId;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.utenteCorrente = (Utente) request.getSession().getAttribute(
                Constants.UTENTE_KEY);
    }
    
    /* ************************* */
    
    public String getNote() {
        return note;
    }

    public int getAooId() {
        return aooId;
    }

    public void setAooId(int aooId) {
        this.aooId = aooId;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOggettoProcedimento() {
        return oggettoProcedimento;
    }

    public void setOggettoProcedimento(String oggetto) {
        this.oggettoProcedimento = oggetto;
    }

    public int getUtenteSelezionatoId() {
        return utenteSelezionatoId;
    }

    public void setUtenteSelezionatoId(int utenteSelezionatoId) {
        this.utenteSelezionatoId = utenteSelezionatoId;
    }

    public Collection getUtenti() {
        return utenti;
    }

    public void setUtenti(Collection utenti) {
        this.utenti = utenti;
    }

    public Collection getTitolariFigli() {
        return titolariFigli;
    }

    public void setTitolariFigli(Collection titolariFigli) {
        this.titolariFigli = titolariFigli;
    }

    public TitolarioVO getTitolario() {
        return titolario;
    }

    public void setTitolario(TitolarioVO titolario) {
        this.titolario = titolario;
    }

    public int getTitolarioPrecedenteId() {
        return titolarioPrecedenteId;
    }

    public void setTitolarioPrecedenteId(int titolarioPrecedenteId) {
        this.titolarioPrecedenteId = titolarioPrecedenteId;
    }

    public int getTitolarioSelezionatoId() {
        return titolarioSelezionatoId;
    }

    public void setTitolarioSelezionatoId(int titolarioSelezionatoId) {
        this.titolarioSelezionatoId = titolarioSelezionatoId;
    }

    public int getStatoId() {
        return statoId;
    }

    public void setStatoId(int statoId) {
        this.statoId = statoId;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }

    public String getPosizione() {
        return posizione;
    }

    public Map getStatiProcedimento() {
        return statiProcedimento;
    }

    public Collection getStatiProcedimentoCollection() {
        return statiProcedimento.values();
    }

    public void setStatiProcedimento(Map statiProcedimento) {
        this.statiProcedimento = statiProcedimento;
    }

    public Map getPosizioniProcedimento() {
        return posizioniProcedimento;
    }

    public void setPosizioniProcedimento(Map posizioniProcedimento) {
        this.posizioniProcedimento = posizioniProcedimento;
    }

    public Collection getPosizioniProcedimentoCollection() {
        return posizioniProcedimento.values();
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getDataAvvioFine() {
        return dataAvvioFine;
    }

    public void setDataAvvioFine(String dataAvvioFine) {
        this.dataAvvioFine = dataAvvioFine;
    }

    public String getDataAvvioInizio() {
        return dataAvvioInizio;
    }

    public void setDataAvvioInizio(String dataAvvioInizio) {
        this.dataAvvioInizio = dataAvvioInizio;
    }

    public String getDataScadenzaFine() {
        return dataScadenzaFine;
    }

    public void setDataScadenzaFine(String dataScadenzaFine) {
        this.dataScadenzaFine = dataScadenzaFine;
    }

    public String getDataScadenzaInizio() {
        return dataScadenzaInizio;
    }

    public void setDataScadenzaInizio(String dataScadenzaInizio) {
        this.dataScadenzaInizio = dataScadenzaInizio;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Collection getProcedimenti() {
        return procedimenti;
    }

    public void setProcedimenti(Collection procedimenti) {
        this.procedimenti = procedimenti;
    }

    public String[] getProcedimentiSelezionati() {
        return procedimentiSelezionati;
    }

    public void setProcedimentiSelezionati(String[] procedimentiSelezionati) {
        this.procedimentiSelezionati = procedimentiSelezionati;
    }

    public void inizializzaForm() {
        setAnno(null);
        setDescrizioneInteressatoDelegato(null);
        setDataAvvioInizio(null);
        setDataAvvioFine(null);
        setDataScadenzaInizio(null);
        setDataScadenzaFine(null);
        setNote(null);
        setNumero(null);
        setOggettoProcedimento(null);
        setPosizione("T");
        setStatoId(0);
        setTitolariFigli(null);
        setTitolario(null);
        setTitolarioPrecedenteId(0);
        setTitolarioSelezionatoId(0);
        
        setUtenteSelezionatoId(0);
        setUtenti(null);
        setProcedimenti(null);
        setProcedimentiSelezionati(null);
        setPosizione(null);
    }
    
    public void resetForm() {
        setAnno(null);
        setDataAvvioInizio(null);
        setDataAvvioFine(null);
        setDataScadenzaInizio(null);
        setDataScadenzaFine(null);
        setNote(null);
        setNumero(null);
        setOggettoProcedimento(null);
        setPosizione("T");
        setStatoId(0);
        setUtenteSelezionatoId(0);
        setUtenti(null);
        setProcedimenti(null);
        setProcedimentiSelezionati(null);
        setPosizione(null);
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    public boolean isIndietroVisibile() {
        return indietroVisibile;
    }

    public void setIndietroVisibile(boolean indietroVisibile) {
        this.indietroVisibile = indietroVisibile;
    }

}
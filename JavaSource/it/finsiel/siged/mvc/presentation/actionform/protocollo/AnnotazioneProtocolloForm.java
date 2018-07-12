package it.finsiel.siged.mvc.presentation.actionform.protocollo;

/*
 * import it.finsiel.siged.mvc.vo.lookup.Capitolo; import
 * it.finsiel.siged.mvc.vo.lookup.Soggetto; import
 * it.finsiel.siged.mvc.vo.lookup.TipoDocumento; import
 * it.finsiel.siged.mvc.vo.lookup.Titolario; import
 * it.finsiel.siged.mvc.vo.organizzazione.Ufficio; import
 * it.finsiel.siged.mvc.vo.organizzazione.Utente; import
 * it.finsiel.siged.mvc.vo.registro.Registro; import java.util.Date; import
 * java.util.HashMap;
 */

import it.finsiel.siged.mvc.presentation.action.protocollo.AnnotazioneProtocolloAction;
import it.finsiel.siged.mvc.vo.protocollo.AnnotazioneVO;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class AnnotazioneProtocolloForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(AnnotazioneProtocolloAction.class
            .getName());

    private Collection<AnnotazioneVO> annotazioniCollection = new ArrayList<AnnotazioneVO>();

    private long annotazioneProtocolloId;

    private String checkAnnotazione;

    private AnnotazioneVO fkProtocollo;

    private String noteAnnotazione;

    private String btnCercaAnnotazioni;

    private String btnSelezionaAnnotazioni;

    private String btnInserisciAnnotazione;

    private String btnAnnotazioni;

    private String btnAnnulla;

    public long getAnnotazioneProtocolloId() {
        return annotazioneProtocolloId;
    }

    public void setAnnotazioneProtocolloId(long annotazioneProtocolloId) {
        this.annotazioneProtocolloId = annotazioneProtocolloId;
    }

    public String getBtnCercaAnnotazioni() {
        return btnCercaAnnotazioni;
    }

    public void setBtnCercaAnnotazioni(String btnListaAnnotazioni) {
        this.btnCercaAnnotazioni = btnListaAnnotazioni;
    }

    public String getBtnAnnulla() {
        return btnAnnulla;
    }

    public void setBtnAnnulla(String btnAnnulla) {
        this.btnAnnulla = btnAnnulla;
    }

    public String getBtnSelezionaAnnotazioni() {
        return btnSelezionaAnnotazioni;
    }

    public void setBtnSelezionaAnnotazioni(String btnSelezionaAnnotazioni) {
        this.btnSelezionaAnnotazioni = btnSelezionaAnnotazioni;
    }

    public String getCheckAnnotazione() {
        return checkAnnotazione;
    }

    public void setCheckAnnotazione(String checkAnnotazione) {
        this.checkAnnotazione = checkAnnotazione;
    }

    public void setAnnotazioniCollection(Collection<AnnotazioneVO> annotazioneProtocollo) {
        this.annotazioniCollection = annotazioneProtocollo;
    }

    public Collection<AnnotazioneVO> getAnnotazioniCollection() {
        return annotazioniCollection;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (btnCercaAnnotazioni != null) {
            // controllare i campi da, A, Anno
            if (noteAnnotazione != null && !"".equals(noteAnnotazione)
                    && !(NumberUtil.isInteger(noteAnnotazione))) {
                errors.add("annotazioni note", new ActionMessage(
                        "error.noteAnnotazioni.required"));
            }

        }

        return errors;

    }

    /**
     * @return Returns the btnAnnotazioni.
     */
    public String getBtnAnnotazioni() {
        return btnAnnotazioni;
    }

    /**
     * @param btnAnnotazioni
     *            The btnAnnotazioni to set.
     */
    public void setBtnAnnotazioni(String btnAnnotazioni) {
        this.btnAnnotazioni = btnAnnotazioni;
    }

    /**
     * @return Returns the fkProtocollo.
     */
    public AnnotazioneVO getFkProtocollo() {
        return fkProtocollo;
    }

    /**
     * @param fkProtocollo
     *            The fkProtocollo to set.
     */
    public void setFkProtocollo(AnnotazioneVO fkProtocollo) {
        this.fkProtocollo = fkProtocollo;
    }

    /**
     * @return Returns the noteAnnotazione.
     */
    public String getNoteAnnotazione() {
        return noteAnnotazione;
    }

    /**
     * @param noteAnnotazione
     *            The noteAnnotazione to set.
     */
    public void setNoteAnnotazione(String noteAnnotazione) {
        this.noteAnnotazione = noteAnnotazione;
    }

    /**
     * @return Returns the btnInserisciAnnotazione.
     */
    public String getBtnInserisciAnnotazione() {
        return btnInserisciAnnotazione;
    }

    /**
     * @param btnInserisciAnnotazione
     *            The btnInserisciAnnotazione to set.
     */
    public void setBtnInserisciAnnotazione(String btnInserisciAnnotazione) {
        this.btnInserisciAnnotazione = btnInserisciAnnotazione;
    }
}
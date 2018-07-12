package it.finsiel.siged.mvc.presentation.actionform.amministrazione;

import it.compit.fenice.mvc.bo.TipoProcedimentoBO;
import it.compit.fenice.mvc.vo.protocollo.UfficioPartecipanteVO;
import it.finsiel.siged.mvc.presentation.action.amministrazione.TipoProcedimentoAction;
import it.finsiel.siged.mvc.presentation.actionform.UploaderForm;
import it.finsiel.siged.mvc.presentation.actionform.protocollo.AlberoUfficiUtentiForm;
import it.finsiel.siged.mvc.vo.lookup.AmministrazionePartecipanteVO;
import it.finsiel.siged.mvc.vo.lookup.TitolarioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.NumberUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class TipoProcedimentoForm extends UploaderForm implements
AlberoUfficiUtentiForm{

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(TipoProcedimentoAction.class.getName());

	private Collection tipiProcedimento = new ArrayList();

	private int idTipo;

	private String descrizione;

	private int aooId;

	private String giorniMax;

	private Map<String, DocumentoVO> documentiAllegati;

	private String[] allegatiSelezionatiId;

	private String giorniAlert;

	private String[] amministrazioniSelezionateId;

	private Map<String,AmministrazionePartecipanteVO> amministrazioniPartecipanti;

	private Map<String,Integer> amministrazioniIds =new HashMap<String, Integer>();

	private String nominativoAmministrazione;

	private int idx;

	private int amministrazioneId;
	
	private TitolarioVO titolario;

	private int titolarioPrecedenteId;

	private int titolarioSelezionatoId;

	private Collection titolariFigli;
	
	// AGGIUNTA UFFICI
	private int ufficioCorrenteId;

	private String ufficioCorrentePath;

	private int ufficioSelezionatoId;

	private UfficioVO ufficioCorrente;

	private Collection<UfficioVO> ufficiDipendenti;
	
	private int utenteSelezionatoId;
	
	private Collection<UtenteVO> utenti;
	
	private String[] ufficiPartecipantiSelezionatiId = null;

	private Map<String,UfficioPartecipanteVO> ufficiPartecipanti = new HashMap<String,UfficioPartecipanteVO>(2);

	private int ufficioPrincipaleId;
	
	public int getUfficioPrincipaleId() {
		return ufficioPrincipaleId;
	}

	public void setUfficioPrincipaleId(int ufficioPrincipaleId) {
		this.ufficioPrincipaleId = ufficioPrincipaleId;
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
	
	public String getNominativoAmministrazione() {
		return nominativoAmministrazione;
	}

	public void setNominativoAmministrazione(String nominativoAmministrazione) {
		this.nominativoAmministrazione = nominativoAmministrazione;
	}

	public int getAmministrazioneId() {
		return amministrazioneId;
	}

	public void setAmministrazioneId(int amministrazioneId) {
		this.amministrazioneId = amministrazioneId;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getGiorniAlert() {
		return giorniAlert;
	}

	public void setGiorniAlert(String giorniAlert) {
		this.giorniAlert = giorniAlert;
	}

	public Collection<AmministrazionePartecipanteVO> getAmministrazioni() {
		return amministrazioniPartecipanti.values();
	}

	public AmministrazionePartecipanteVO getAmministrazione(String id) {
		return amministrazioniPartecipanti.get(id);
	}

	public void aggiungiAmministrazione(AmministrazionePartecipanteVO destinatario) {
		if (destinatario != null) {

			if (destinatario.getIdx() == 0) {
				int idx = getNextId(amministrazioniIds);
				destinatario.setIdx(idx);
				amministrazioniPartecipanti.put(String.valueOf(idx),
						destinatario);
				amministrazioniIds.put(String.valueOf(idx), new Integer(idx));
			} else {
				amministrazioniPartecipanti.put(String.valueOf(destinatario.getIdx()),
						destinatario);
			}

		}
	}

	private static int getNextId(Map m) {
		int max = 0;
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String id = (String) it.next();
			int cur = NumberUtil.getInt(id);
			if (cur > max)
				max = cur;
		}
		return max + 1;
	}

	public void rimuoviAmministrazione(String id) {
		AmministrazionePartecipanteVO removed = (AmministrazionePartecipanteVO) amministrazioniPartecipanti.get(id);
		int idx = removed.getIdx();
		amministrazioniPartecipanti.remove(String.valueOf(removed.getIdx()));
		amministrazioniPartecipanti.remove(String.valueOf(idx));
	}

	public void rimuoviAmministrazioni() {
		if (amministrazioniPartecipanti != null) {
			this.amministrazioniPartecipanti.clear();
			this.amministrazioniPartecipanti.clear();
		}
	}

	public void setAmministrazioni(Map amministrazioni) {
		this.amministrazioniPartecipanti = amministrazioni;
	}

	public String[] getAmministrazioniSelezionateId() {
		return amministrazioniSelezionateId;
	}

	public void setAmministrazioniSelezionateId(
			String[] amministrazioniSelezionateId) {
		this.amministrazioniSelezionateId = amministrazioniSelezionateId;
	}

	public String getGiorniMax() {
		return giorniMax;
	}

	public void setGiorniMax(String giornimax) {
		this.giorniMax = giornimax;
	}

	public Collection<DocumentoVO> getDocumentiAllegatiCollection() {
		return documentiAllegati.values();
	}

	public void setDocumentiAllegati(Map<String, DocumentoVO> documenti) {
		this.documentiAllegati = documenti;
	}

	public Map<String, DocumentoVO> getDocumentiAllegati() {
		return documentiAllegati;
	}

	public void allegaDocumento(DocumentoVO doc) {
		TipoProcedimentoBO.putAllegato(doc, this.documentiAllegati);
	}

	public void rimuoviAllegato(String allegatoId) {
		documentiAllegati.remove(allegatoId);
	}

	public DocumentoVO getDocumentoAllegato(Object key) {
		return (DocumentoVO) this.documentiAllegati.get(key);
	}

	public DocumentoVO getDocumentoAllegato(int idx) {
		return (DocumentoVO) this.documentiAllegati.get(String.valueOf(idx));
	}

	public String[] getAllegatiSelezionatiId() {
		return allegatiSelezionatiId;
	}

	public void setAllegatiSelezionatiId(String[] allegatiSelezionatoId) {
		this.allegatiSelezionatiId = allegatiSelezionatoId;
	}

	public int getAooId() {
		return aooId;
	}

	public void setAooId(int aooId) {
		this.aooId = aooId;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(int idTipo) {
		this.idTipo = idTipo;
	}

	public Collection getTipiProcedimento() {
		return tipiProcedimento;
	}

	public void setTipiProcedimento(ArrayList tipiProcedimento) {
		this.tipiProcedimento = tipiProcedimento;
	}
	
	public void setUfficioCorrente(UfficioVO ufficioCorrente) {
		this.ufficioCorrente = ufficioCorrente;
	}
	
	public UfficioVO getUfficioCorrente() {
		return ufficioCorrente;
	}

	public int getUfficioCorrenteId() {
		return ufficioCorrenteId;
	}

	public void setUfficioCorrenteId(int ufficioCorrenteId) {
		this.ufficioCorrenteId = ufficioCorrenteId;
	}

	public String getUfficioCorrentePath() {
		return ufficioCorrentePath;
	}

	public void setUfficioCorrentePath(String ufficioCorrentePath) {
		this.ufficioCorrentePath = ufficioCorrentePath;
	}
	
	public int getUfficioSelezionatoId() {
		return ufficioSelezionatoId;
	}

	public void setUfficioSelezionatoId(int ufficioSelezionatoId) {
		this.ufficioSelezionatoId = ufficioSelezionatoId;
	}

	public Collection<UfficioVO> getUfficiDipendenti() {
		return ufficiDipendenti;
	}

	public void setUfficiDipendenti(Collection<UfficioVO> ufficiDipendenti) {
		this.ufficiDipendenti = ufficiDipendenti;
	}
	
	public int getUtenteSelezionatoId() {
		return utenteSelezionatoId;
	}

	public void setUtenteSelezionatoId(int utenteSelezionatoId) {
		this.utenteSelezionatoId = utenteSelezionatoId;
	}

	public Collection<UtenteVO> getUtenti() {
		return utenti;
	}

	public void setUtenti(Collection<UtenteVO> utenti) {
		this.utenti = utenti;
	}
	
	public String[] getUfficiPartecipantiId() {
		return ufficiPartecipantiSelezionatiId;
	}

	public void setUfficiPartecipantiId(String[] istruttori) {
		this.ufficiPartecipantiSelezionatiId = istruttori;
	}
	
	
	public void aggiungiUfficioPartecipante(UfficioPartecipanteVO uff) {
		ufficiPartecipanti.put(String.valueOf(uff.getUfficioId()), uff);
	}

	public void removeUfficiPartecipanti() {
		if (ufficiPartecipanti != null)
			ufficiPartecipanti.clear();
	}

	public Collection<UfficioPartecipanteVO> getUfficiPartecipanti() {
		return ufficiPartecipanti.values();
	}

	public void rimuoviUfficioPartecipante(String key) {
		ufficiPartecipanti.remove(key);
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
	}

	public ActionErrors validateDatiInserimento(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (getDescrizione() == null
				|| "".equals(getDescrizione().trim())) {
			errors.add("descrizione", new ActionMessage("campo.obbligatorio",
					"descrizione", ""));
		} if (getTitolario() == null) {
			errors.add("classificazione", new ActionMessage("campo.obbligatorio",
					"classificazione", ""));
		}
		if (getGiorniMax() == null
					|| "".equals(getGiorniMax().trim())) {
				errors.add("giorniMax", new ActionMessage("campo.obbligatorio",
						"Numero di giorni (massimo)", ""));
		}
		if (getGiorniAlert() == null
				|| "".equals(getGiorniAlert().trim())) {
			errors.add("giorniAlert", new ActionMessage("campo.obbligatorio",
					"Numero di giorni (alert)", ""));
		}
		if (getGiorniAlert() != null && !"".equals(getGiorniAlert())
                && !(NumberUtil.isInteger(getGiorniAlert()))) {
            errors.add("giorniAlert", new ActionMessage(
                    "formato.numerico.errato", "Numero di giorni (alert)"));
        }if (getGiorniMax() != null && !"".equals(getGiorniMax())
                && !(NumberUtil.isInteger(getGiorniMax()))) {
            errors.add("giorniMax", new ActionMessage(
                    "formato.numerico.errato", "Numero di giorni (massimo)"));
        }if ((getGiorniMax() != null && !"".equals(getGiorniMax())&& (NumberUtil.isInteger(getGiorniMax()))) && (getGiorniAlert() != null && !"".equals(getGiorniAlert()) && (NumberUtil.isInteger(getGiorniAlert())))) {
           if(Integer.valueOf(getGiorniMax())<Integer.valueOf(getGiorniAlert()))
        	errors.add("giorni", new ActionMessage(
                    "giorni_max.minore.giorni_alert"));
        }
		

		return errors;
	}

	public void inizializzaForm() {
		setIdTipo(0);
		setDescrizione(null);
		setTipiProcedimento(null);
	    setGiorniMax(null);
	    setGiorniAlert(null);
	    setAllegatiSelezionatiId(null);
		documentiAllegati = new HashMap<String, DocumentoVO>(2);
		amministrazioniIds = new HashMap(2);
		amministrazioniPartecipanti = new HashMap();
		setTitolario(null);
	    setTitolarioPrecedenteId(0);
	    setTitolarioSelezionatoId(0);
	    removeUfficiPartecipanti();
	}

}
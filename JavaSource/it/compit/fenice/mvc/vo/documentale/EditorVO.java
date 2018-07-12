package it.compit.fenice.mvc.vo.documentale;

import it.compit.fenice.mvc.vo.organizzazione.CaricaVO;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Ufficio;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.vo.VersioneVO;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.util.DateUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EditorVO extends VersioneVO{

	private static final long serialVersionUID = 1L;

	private int documentoId;

	private int caricaId;

	private String txt;

	private String nome;

	private String oggetto;

	private int flagStato;

	private int flagTipo;
	
	private int versione;

	private Timestamp dataCreazione;

	private Map<Integer,DestinatarioVO> destinatari = new HashMap<Integer,DestinatarioVO>();

	private Collection<AllaccioVO> allacci = new ArrayList<AllaccioVO>();

	private Collection<FascicoloVO> fascicoli = new ArrayList<FascicoloVO>();
	
	private Collection<AssegnatarioVO> assegnatari = new ArrayList<AssegnatarioVO>();
	
	private String msgCarica;
	
	private String mittente;
	
	public EditorVO() {
	}
	

	public String getMittente() {
		return mittente;
	}

	public void setMittente(String mittente) {
		this.mittente = mittente;
	}

	public String getMsgCarica() {
		return msgCarica;
	}

	public void setMsgCarica(String msgCarica) {
		this.msgCarica = msgCarica;
	}
	
    public Collection<AssegnatarioVO> getAssegnatari() {
        return new ArrayList<AssegnatarioVO>(assegnatari);
    }

    public void aggiungiAssegnatario(AssegnatarioVO assegnatario) {
        if (assegnatario != null) {
            assegnatari.add(assegnatario);
        }
    }

    public void setAssegnatari(Collection<AssegnatarioVO> assegnatari) {
        this.assegnatari.addAll(assegnatari);
    }

    public void removeAssegnatari() {
        assegnatari.clear();
    }
	public int getFlagTipo() {
		return flagTipo;
	}

	public void setFlagTipo(int flagTipo) {
		this.flagTipo = flagTipo;
	}

	public Collection<FascicoloVO> getFascicoli() {
		return fascicoli;
	}

	public void setFascicoli(Collection<FascicoloVO> fascicoli) {
		this.fascicoli = fascicoli;
	}

	public Collection<AllaccioVO> getAllacci() {
		return new ArrayList<AllaccioVO>(allacci);
	}

	public void allacciaProtocollo(AllaccioVO allaccio) {
		if (allaccio != null) {
			allacci.add(allaccio);
		}
	}

	public void setAllacci(Collection<AllaccioVO> allacci) {
		this.allacci.addAll(allacci);
		
	}
	
	public void setDestinatari(Map<Integer,DestinatarioVO> destinatari) {
		this.destinatari = destinatari;
	}

	public DestinatarioVO getDestinatario(Integer destinatarioId) {
		return (DestinatarioVO) destinatari.get(destinatarioId);
	}

	public DestinatarioVO getDestinatario(int destinatarioId) {
		return getDestinatario(new Integer(destinatarioId));
	}

	public Collection<DestinatarioVO> getDestinatari() {
		return destinatari.values();
	}

	public void addDestinatari(DestinatarioVO destinatario) {
		if (destinatario != null) {
			destinatari.put(destinatario.getIdx(), destinatario);

		}
	}

	public void removeDestinatario(Integer destinatarioId) {
		destinatari.remove(destinatarioId);
	}

	public void removeDestinatario(int destinatarioId) {
		removeDestinatario(new Integer(destinatarioId));
	}

	public void removeDestinatario(DestinatarioVO destinatario) {
		if (destinatario != null) {
			removeDestinatario(destinatario.getId());
		}
	}

	public void removeDestinatari() {
		if (destinatari != null) {
			destinatari.clear();
		}
	}

	public String getOggetto() {
		return oggetto;
	}

	public void setOggetto(String oggetto) {
		this.oggetto = oggetto;
	}

	public int getDocumentoId() {
		return documentoId;
	}

	public int getCaricaId() {
		return caricaId;
	}

	public String getTxt() {
		return txt;
	}

	public String getNome() {
		return nome;
	}

	public int getFlagStato() {
		return flagStato;
	}

	public String getStato() {
		if (flagStato == 0)
			return "in lavorazione";
		if (flagStato == 1)
			return "Protocollato";
		if (flagStato == 2)
			return "Inviato al Protocollo";
		if (flagStato == 3)
			return "Inviato al altro utente";
		else
			return " ";
	}

	public int getVersione() {
		return versione;
	}

	public void setDocumentoId(int documentoId) {
		this.documentoId = documentoId;
	}

	public void setCaricaId(int caricaId) {
		this.caricaId = caricaId;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

	public void setNome(String nomeFile) {
		this.nome = nomeFile;
	}

	public void setFlagStato(int flagStato) {
		this.flagStato = flagStato;
	}

	public void setVersione(int versione) {
		this.versione = versione;
	}

	public String getIntestatario() {
		Organizzazione org = Organizzazione.getInstance();
		String responsabile = "";
		if (getCaricaId() != 0) {
			CaricaVO carica = org.getCarica(getCaricaId());
			if (carica != null) {
				Ufficio uff = org.getUfficio(carica.getUfficioId());
				if (uff != null)
					responsabile = uff.getPath();
				Utente ute = org.getUtente(carica.getUtenteId());
				if (ute == null)
					return uff.getPath() + carica.getNome();
				ute.getValueObject().setCarica(carica.getNome());
				if (carica.isAttivo())
					responsabile = uff.getPath()
							+ ute.getValueObject().getCaricaFullName();
				else
					responsabile = uff.getPath()
							+ ute.getValueObject().getCaricaFullNameNonAttivo();
			}

		}
		return responsabile;
	}

	public Timestamp getDataCreazione() {
		return dataCreazione;
	}

	public String getData() {
		return DateUtil.formattaDataOra(dataCreazione.getTime());
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataCreazione == null) ? 0 : dataCreazione.hashCode());
		result = prime * result + documentoId;
		result = prime * result + flagTipo;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((oggetto == null) ? 0 : oggetto.hashCode());
		result = prime * result + ((txt == null) ? 0 : txt.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EditorVO other = (EditorVO) obj;
		if (dataCreazione == null) {
			if (other.dataCreazione != null)
				return false;
		} else if (!dataCreazione.equals(other.dataCreazione))
			return false;
		if (documentoId != other.documentoId)
			return false;
		if (flagTipo != other.flagTipo)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (oggetto == null) {
			if (other.oggetto != null)
				return false;
		} else if (!oggetto.equals(other.oggetto))
			return false;
		if (txt == null) {
			if (other.txt != null)
				return false;
		} else if (!txt.equals(other.txt))
			return false;
		return true;
	}

}

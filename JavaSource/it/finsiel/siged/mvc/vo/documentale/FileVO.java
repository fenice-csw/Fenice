package it.finsiel.siged.mvc.vo.documentale;

import it.finsiel.siged.constant.Parametri;
import it.finsiel.siged.mvc.vo.VersioneVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public class FileVO extends VersioneVO {

	private static final long serialVersionUID = 1L;

	private int repositoryFileId;

    private int cartellaId;

    private String nomeFile;

    private DocumentoVO documentoVO;

    private Date dataDocumento;
    
    private String dataDocumentoC;

    private String oggetto;

    private String note;

    private String descrizione;

    private String descrizioneArgomento;

    private int tipoDocumentoId;

    private int titolarioId;

    private String statoDocumento = String.valueOf(Parametri.CHECKED_IN);

    private String statoArchivio = String.valueOf(Parametri.STATO_LAVORAZIONE);

    private int caricaLavId;

    private String caricaLav;

    private int ownerCaricaId;
    
    private String ownerCarica;
    
    private String assegnatoDa;
    
    private String assegnatario;
    
    private String messaggio;

    private Collection<FascicoloVO> fascicoli;

    private FascicoloVO fascicoloVO;

    private int procedimentoId;
    
    private boolean visibileDaFascicolo=false;
    
    private int ufficioProprietarioId;
    
    public FileVO() {
    }

	public int getUfficioProprietarioId() {
		return ufficioProprietarioId;
	}

	public void setUfficioProprietarioId(int ufficioProprietarioId) {
		this.ufficioProprietarioId = ufficioProprietarioId;
	}
   
	public boolean isVisibileDaFascicolo() {
		return visibileDaFascicolo;
	}

	public void setVisibileDaFascicolo(boolean visibileDaFascicolo) {
		this.visibileDaFascicolo = visibileDaFascicolo;
	}

	public int getProcedimentoId() {
		return procedimentoId;
	}

	public void setProcedimentoId(int procedimentoId) {
		this.procedimentoId = procedimentoId;
	}




	public int getCaricaLavId() {
		return caricaLavId;
	}



	public void setCaricaLavId(int caricaLavId) {
		this.caricaLavId = caricaLavId;
	}



	public String getCaricaLav() {
		return caricaLav;
	}



	public void setCaricaLav(String caricaLav) {
		this.caricaLav = caricaLav;
	}



	public int getOwnerCaricaId() {
		return ownerCaricaId;
	}



	public void setOwnerCaricaId(int ownerCaricaId) {
		this.ownerCaricaId = ownerCaricaId;
	}



	public String getOwnerCarica() {
		return ownerCarica;
	}



	public void setOwnerCarica(String ownerCarica) {
		this.ownerCarica = ownerCarica;
	}



	public int getCartellaId() {
        return cartellaId;
    }

    public void setCartellaId(int cartellaId) {
        this.cartellaId = cartellaId;
    }

    public Date getDataDocumento() {
        return dataDocumento;
    }

    public String getDataDocumentoView() {
        return DateUtil.formattaData(dataDocumento.getTime());
    }
    
    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizioneArgomento() {
        return descrizioneArgomento;
    }

    public void setDescrizioneArgomento(String descrizioneArgomento) {
        this.descrizioneArgomento = descrizioneArgomento;
    }

    public DocumentoVO getDocumentoVO() {
        return documentoVO;
    }

    public void setDocumentoVO(DocumentoVO documento) {
        this.documentoVO = documento;
    }

    public String getNomeFile() {
        return nomeFile;
    }

    public void setNomeFile(String nomeFile) {
        this.nomeFile = nomeFile;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getStatoArchivio() {
        return statoArchivio;
    }

    public void setStatoArchivio(String statoArc) {
        this.statoArchivio = statoArc;
    }

    public String getStatoDocumento() {
        return statoDocumento;
    }

    public void setStatoDocumento(String statoDocumento) {
        this.statoDocumento = statoDocumento;
    }

    public int getTipoDocumentoId() {
        return tipoDocumentoId;
    }

    public void setTipoDocumentoId(int tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId;
    }

    public int getTitolarioId() {
        return titolarioId;
    }

    public void setTitolarioId(int titolarioId) {
        this.titolarioId = titolarioId;
    }

    public void aggiungiFascicolo(FascicoloVO f) {
        if (fascicoli == null) {
        	fascicoli=new ArrayList<FascicoloVO>();
        }
    	fascicoli.add(f);
    }

    public Collection<FascicoloVO> getFascicoli() {
        return fascicoli;
    }

    public void setFascicoli(Collection<FascicoloVO> fascicoli) {
        this.fascicoli = fascicoli;
    }

    public FascicoloVO getFascicoloVO() {
        return fascicoloVO;
    }

    public void setFascicoloVO(FascicoloVO fascicoloVO) {
        this.fascicoloVO = fascicoloVO;
    }

    public int getRepositoryFileId() {
        return repositoryFileId;
    }

    public void setRepositoryFileId(int repositoryFileId) {
        this.repositoryFileId = repositoryFileId;
    }

    
    public String getAssegnatario() {
        return assegnatario;
    }

    public void setAssegnatario(String assegnatario) {
        this.assegnatario = assegnatario;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    

    public String getAssegnatoDa() {
        return assegnatoDa;
    }

    public void setAssegnatoDa(String assegnatoDa) {
        this.assegnatoDa = assegnatoDa;
    }

    public String getDataDocumentoC() {
        return dataDocumentoC;
    }

    public void setDataDocumentoC(String dataDocumentoC) {
        this.dataDocumentoC = dataDocumentoC;
    }
}
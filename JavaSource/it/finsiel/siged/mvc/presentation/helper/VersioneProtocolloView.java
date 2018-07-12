package it.finsiel.siged.mvc.presentation.helper;

import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.util.DateUtil;

public class VersioneProtocolloView implements Comparable<VersioneProtocolloView>{

    public VersioneProtocolloView() {
    }

    private int protocolloId;

    private int versione;

    private String tipoProtocollo;

    private String oggetto;
    
    private String cognomeMittente;

    private String statoProtocollo;

    private String dateUpdated;

    private String userUpdated;
    
    private int documentoId;
    
    private String riservato;
   
    private String estremiAutorizzazione;
  
    private String assegnatario;
    
    public String getStatoProtocollo() {
    	if(!this.statoProtocollo.equals("N") && !this.statoProtocollo.equals("S"))
    		return ProtocolloBO.getStatoProtocollo(this.tipoProtocollo,this.statoProtocollo);
    	if(getVersione()<=1)
			return "Registrato";
		if(getVersione()>1)
			return "Modificato";
		
		return "non classificato";
    }
    
    public String getEstremiAutorizzazione() {
		return estremiAutorizzazione;
	}

	public void setEstremiAutorizzazione(String estremiAutorizzazione) {
		this.estremiAutorizzazione = estremiAutorizzazione;
	}

	public int getProtocolloId() {
        return protocolloId;
    }

    public void setProtocolloId(int protocolloId) {
        this.protocolloId = protocolloId;
    }

    public void setStatoProtocollo(String statoProtocollo) {
        this.statoProtocollo = statoProtocollo;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getTipoProtocollo() {
        return tipoProtocollo;
    }

    public void setTipoProtocollo(String tipoProtocollo) {
        this.tipoProtocollo = tipoProtocollo;
    }

    public int getVersione() {
        return versione;
    }

    public void setVersione(int versione) {
        this.versione = versione;
    }

    public String getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getUserUpdated() {
        return userUpdated;
    }

    public void setUserUpdated(String userUpdated) {
        this.userUpdated = userUpdated;
    }

    public String getCognomeMittente() {
        return cognomeMittente;
    }

    public void setCognomeMittente(String cognomeMittente) {
        this.cognomeMittente = cognomeMittente;
    }

    public int getDocumentoId() {
        return documentoId;
    }

    public void setDocumentoId(int documentoId) {
        this.documentoId = documentoId;
    }

    public String getRiservato() {
        return riservato;
    }

    public void setRiservato(String riservato) {
        this.riservato = riservato;
    }

    
	public String getAssegnatario() {
		return assegnatario;
	}

	public void setAssegnatario(String assegnatario) {
		this.assegnatario = assegnatario;
	}

	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + protocolloId;
		result = prime * result + versione;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersioneProtocolloView other = (VersioneProtocolloView) obj;
		if (protocolloId != other.protocolloId)
			return false;
		if (versione != other.versione)
			return false;
		return true;
	}

	
	public int compareTo(VersioneProtocolloView other) {
		return DateUtil.getDataOra(dateUpdated).compareTo(DateUtil.getDataOra(other.getDateUpdated()));
	}

    
    
    
}
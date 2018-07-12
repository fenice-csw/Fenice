package it.finsiel.siged.mvc.vo.organizzazione;

import it.compit.fenice.enums.UnitaAmministrativaEnum;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.lookup.ParametriLdapVO;

public class AmministrazioneVO extends IdentityVO {

  
	private static final long serialVersionUID = 1L;

	public AmministrazioneVO() {
    }

    private String flagLdap;
    
    private String flagRegistroPostaSeparato;

    private ParametriLdapVO parametriLdap;

    private String pathDocAquisMassiva;
    
    private String pathDocumentiProtocollo;

    private int portaFtp;

    private String hostFtp;

    private String userFtp;

    private String passFtp;
    
    private String folderFtp;

    private boolean repertoriEnabled;
    
    private UnitaAmministrativaEnum unitaAmministrativa ;
    
    private String versioneFenice ;
    
    private boolean webSocketEnabled ;
    
	public String getVersioneFenice() {
		return versioneFenice;
	}

	public void setVersioneFenice(String versioneFenice) {
		this.versioneFenice = versioneFenice;
	}

	public boolean isWebSocketEnabled() {
		return webSocketEnabled;
	}

	public void setWebSocketEnabled(boolean webSocketEnabled) {
		this.webSocketEnabled = webSocketEnabled;
	}

	public String getFlagRegistroPostaSeparato() {
		return flagRegistroPostaSeparato;
	}

	public void setFlagRegistroPostaSeparato(String flagRegistroPostaSeparato) {
		this.flagRegistroPostaSeparato = flagRegistroPostaSeparato;
	}

	public String getPathDocumentiProtocollo() {
		return pathDocumentiProtocollo;
	}

	public void setPathDocumentiProtocollo(String pathDocumentiProtocollo) {
		this.pathDocumentiProtocollo = pathDocumentiProtocollo;
	}

	public String getFlagLdap() {
        return flagLdap;
    }

    public void setFlagLdap(String flagLdap) {
        this.flagLdap = flagLdap;
    }

    public ParametriLdapVO getParametriLdap() {
        return parametriLdap;
    }

    public void setParametriLdap(ParametriLdapVO parametriLdap) {
        this.parametriLdap = parametriLdap;
    }

    public String getPathDocAquisMassiva() {
        return pathDocAquisMassiva;
    }

    public void setPathDocAquisMassiva(String dirDocumenti) {
        this.pathDocAquisMassiva = dirDocumenti;
    }

	public int getPortaFtp() {
		return portaFtp;
	}

	public String getHostFtp() {
		return hostFtp;
	}

	public String getUserFtp() {
		return userFtp;
	}

	public String getPassFtp() {
		return passFtp;
	}

	public void setPortaFtp(int portaFtp) {
		this.portaFtp = portaFtp;
	}

	public void setHostFtp(String hostFtp) {
		this.hostFtp = hostFtp;
	}

	public void setUserFtp(String userFtp) {
		this.userFtp = userFtp;
	}

	public void setPassFtp(String passFtp) {
		this.passFtp = passFtp;
	}

	public boolean isRepertoriEnabled() {
		return repertoriEnabled;
	}

	public void setRepertoriEnabled(boolean repertoriEnabled) {
		this.repertoriEnabled = repertoriEnabled;
	}

	public UnitaAmministrativaEnum getUnitaAmministrativa() {
		return unitaAmministrativa ;
	}

	public void setUnitaAmministrativa(UnitaAmministrativaEnum unitaAmministrativa) {
		this.unitaAmministrativa  = unitaAmministrativa;
	}

	public String getFolderFtp() {
		return folderFtp;
	}

	public void setFolderFtp(String folderFtp) {
		this.folderFtp = folderFtp;
	}
	
}
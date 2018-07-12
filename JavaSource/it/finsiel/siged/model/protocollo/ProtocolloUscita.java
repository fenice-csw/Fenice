
package it.finsiel.siged.model.protocollo;

import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class ProtocolloUscita extends Protocollo {
    
	private AssegnatarioVO mittente;

    private Map<String,DestinatarioVO> destinatari = new HashMap<String,DestinatarioVO>(); // 

    private int fascicoloInvioId;

    private int documentoInvioId;
    
    private int intDocEditor;

    private boolean fromArchivio;
	
    
    
	
	public boolean isFromArchivio() {
		return fromArchivio;
	}

	public void setFromArchivio(boolean fromArchivio) {
		this.fromArchivio = fromArchivio;
	}

	public int getIntDocEditor() {
		return intDocEditor;
	}

	public void setIntDocEditor(int intDocEditor) {
		this.intDocEditor = intDocEditor;
	}

	public Collection<DestinatarioVO> getDestinatari() {
        return destinatari.values();
    }

    public void addDestinatari(DestinatarioVO destinatario) {
        if (destinatario != null) {
            destinatari.put(String.valueOf(destinatario.getIdx()), destinatario);
            
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

    /**
     * @param destinatari
     *            The destinatari to set.
     */
    public void setDestinatari(Map<String,DestinatarioVO> destinatari) {
        this.destinatari = destinatari;
    }

    public DestinatarioVO getDestinatario(Integer destinatarioId) {
        return (DestinatarioVO) destinatari.get(destinatarioId);
    }

    public DestinatarioVO getDestinatario(int destinatarioId) {
        return getDestinatario(new Integer(destinatarioId));
    }

    public AssegnatarioVO getMittente() {
        return mittente;
    }

    public void setMittente(AssegnatarioVO mittente) {
        this.mittente = mittente;
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
/* Generated by Together */

package it.compit.fenice.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class DocumentoFascicoloVO  extends VersioneVO implements Comparable<DocumentoFascicoloVO>{
    
	private static final long serialVersionUID = 1L;

	private int fascicoloId;

    private int documentoId;
    
    private int ufficioProprietarioId;

    public DocumentoFascicoloVO() {
    }

    public int getFascicoloId() {
        return fascicoloId;
    }

    public void setFascicoloId(int fascicoloId) {
        this.fascicoloId = fascicoloId;
    }

	public int getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(int documentoId) {
		this.documentoId = documentoId;
	}

	public int getUfficioProprietarioId() {
		return ufficioProprietarioId;
	}

	public void setUfficioProprietarioId(int ufficioProprietarioId) {
		this.ufficioProprietarioId = ufficioProprietarioId;
	}
    
    public int compareTo(DocumentoFascicoloVO r) {
		return (documentoId > r.getDocumentoId()) ? -1
				: (documentoId == r.getDocumentoId() ? 0 : 1);
	}
    
}
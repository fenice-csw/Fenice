package it.finsiel.siged.mvc.vo.protocollo;

import it.finsiel.siged.mvc.vo.VersioneVO;

public class DocumentoVO extends VersioneVO {

	private static final long serialVersionUID = 1L;

	public DocumentoVO() {
    }

    private String descrizione;

    private String contentType;

    private String path;

    private String impronta;

    private int size;

    private String fileName;

    private int idx;

    private boolean mustCreateNew;
    
    private boolean riservato;
        
    //Usati solo dal repertorio
    private int type;
    
    private boolean principale;
    
    private boolean pubblicabile;
    
    private boolean timbrato;

    public boolean getPrincipale() {
		return principale;
	}

	public void setPrincipale(boolean principale) {
		this.principale = principale;
	}
	
	public boolean getPubblicabile() {
		return pubblicabile;
	}

	public void setPubblicabile(boolean pubblicabile) {
		this.pubblicabile = pubblicabile;
	}

	public boolean getRiservato() {
		return riservato;
	}

	public void setRiservato(boolean riservato) {
		this.riservato = riservato;
	}

	public boolean getTimbrato() {
		return timbrato;
	}

	public void setTimbrato(boolean timbrato) {
		this.timbrato = timbrato;
	}

	public String getContentType() {
        return contentType;
    }

    public void setContentType(String content) {
        this.contentType = content;
    }

    
    public String getDescrizione() {
    	
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
	


    public int getSize() {
        return size;
    }

    
    public void setSize(int size) {
        this.size = size;
    }

    
    public String getImpronta() {
        return impronta;
    }

   
    public void setImpronta(String impronta) {
        this.impronta = impronta;
    }

    public String getFileName() {
    	return fileName;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

   
    public boolean isMustCreateNew() {
        return mustCreateNew;
    }

    public void setMustCreateNew(boolean mustCreateNew) {
        this.mustCreateNew = mustCreateNew;
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
    
}
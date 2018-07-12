package it.compit.fenice.mvc.vo.organizzazione;

import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.mvc.vo.VersioneVO;

public class CaricaVO extends VersioneVO{

	private static final long serialVersionUID = 1L;

	private Integer caricaId;

    private String nome;

    private String nomeUfficio;
    
    private int ufficioId;
    
    private Integer profiloId;

    private Integer utenteId;
    
    private boolean attivo;
    
    private boolean dirigente;
    
    private boolean referente;
    
    private boolean responsabileEnte;

    private boolean responsabileUfficioProtocollo;

    public CaricaVO() {
		this.caricaId=0;
		this.ufficioId=0;
		this.utenteId=0;
		this.profiloId=0;
		this.nome=null;
		this.attivo=true;
		this.nomeUfficio=null;
		this.dirigente=false;
		this.responsabileEnte=false;
		this.responsabileUfficioProtocollo=false;
	}
    
    public String getPathUfficio(){
 	   if(ufficioId!=0)
 		   return Organizzazione.getInstance().getUfficio(ufficioId).getPath();
 	   else
 		   return "";
    }
    
	public boolean isResponsabileUfficioProtocollo() {
		return responsabileUfficioProtocollo;
	}

	public void setResponsabileUfficioProtocollo(
			boolean responsabileUfficioProtocollo) {
		this.responsabileUfficioProtocollo = responsabileUfficioProtocollo;
	}

	public boolean isResponsabileEnte() {
		return responsabileEnte;
	}

	public void setResponsabileEnte(boolean responsabileEnte) {
		this.responsabileEnte = responsabileEnte;
	}

	public boolean isReferente() {
		return referente;
	}

	public void setReferente(boolean referente) {
		this.referente = referente;
	}

	public boolean isDirigente() {
		return dirigente;
	}

	public void setDirigente(boolean dirigente) {
		this.dirigente = dirigente;
	}

	public Integer getUtenteId() {
		return utenteId;
	}

	public void setUtenteId(Integer utenteId) {
		this.utenteId = utenteId;
	}

	public Integer getCaricaId() {
		return caricaId;
	}

	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}

	public void setCaricaId(Integer caricaId) {
		this.caricaId = caricaId;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getUfficioId() {
		return ufficioId;
	}

	public void setUfficioId(int ufficoId) {
		this.ufficioId = ufficoId;
	}

	public Integer getProfiloId() {
		return profiloId;
	}

	public void setProfiloId(Integer profiloId) {
		this.profiloId = profiloId;
	}

	public String getNomeUfficio() {
		return nomeUfficio;
	}

	public void setNomeUfficio(String nomeUfficio) {
		this.nomeUfficio = nomeUfficio;
	}

	public String getDescrizioneCaricaUfficio() {
		if(nomeUfficio!=null)
			return nomeUfficio+"("+nome+")";
		else
			return nome;
	}
}

/* Generated by Together */

package it.finsiel.siged.mvc.presentation.helper;

public class UtenteView {
    public UtenteView() {
    }

    private String carica;
    
    private int caricaId;
    
    private int id;

    private String cognome;

    private String nome;

    private boolean referente;
    
    private boolean attivo;
    
    private boolean abilitato;
    
    public String getCarica() {
		return carica;
	}

    
   
	public boolean isAbilitato() {
		return abilitato;
	}


	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}



	public boolean isAttivo() {
		return attivo;
	}



	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}



	public void setCarica(String carica) {
		this.carica = carica;
	}

	public int getCaricaId() {
		return caricaId;
	}

	public void setCaricaId(int caricaId) {
		this.caricaId = caricaId;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean getReferente() {
        return referente;
    }

    public void setReferente(boolean referente) {
        this.referente = referente;
    }

}
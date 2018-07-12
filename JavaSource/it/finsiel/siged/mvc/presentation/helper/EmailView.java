
package it.finsiel.siged.mvc.presentation.helper;

import it.finsiel.siged.mvc.vo.BaseVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;

import java.util.Collection;


public class EmailView extends BaseVO {
 
	private static final long serialVersionUID = 1L;

	public EmailView() {
    }

    private String testoMessaggio;

    private String nomeMittente;

    private String emailMittente;

    private String dataSpedizione;
    
    private String dataRicezione;

    private int numeroAllegati;
    
    private int flagAnomalia;

    private Collection<DocumentoVO> descrizioneAllegati;
    
    
    
	
	public Collection<DocumentoVO> getDescrizioneAllegati() {
		return descrizioneAllegati;
	}

	
	public void setDescrizioneAllegati(Collection<DocumentoVO> descrizioneAllegati) {
		this.descrizioneAllegati = descrizioneAllegati;
	}

	public int getFlagAnomalia() {
		return flagAnomalia;
	}

	public void setFlagAnomalia(int flagAnomalia) {
		this.flagAnomalia = flagAnomalia;
	}

	public String getDataSpedizione() {
        return dataSpedizione;
    }

    public void setDataSpedizione(String dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    public String getEmailMittente() {
        return emailMittente;
    }

    public void setEmailMittente(String emailMittente) {
        this.emailMittente = emailMittente;
    }

    public String getNomeMittente() {
        return nomeMittente;
    }

    public void setNomeMittente(String nomeMittente) {
        this.nomeMittente = nomeMittente;
    }

    public int getNumeroAllegati() {
        return numeroAllegati;
    }

    public void setNumeroAllegati(int numeroAllegati) {
        this.numeroAllegati = numeroAllegati;
    }

    public String getTestoMessaggio() {
        return testoMessaggio;
    }

    public void setTestoMessaggio(String testoMessaggio) {
        this.testoMessaggio = testoMessaggio;
    }


	public String getDataRicezione() {
		return dataRicezione;
	}

	public void setDataRicezione(String dataRicezione) {
		this.dataRicezione = dataRicezione;
	}
    
}
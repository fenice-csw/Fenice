package it.compit.fenice.model.protocollo;

import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;

import java.util.ArrayList;
import java.util.Collection;

public class PostaInterna extends Protocollo {


    private AssegnatarioVO mittente;

    private Collection<AssegnatarioVO> destinatari = new ArrayList<AssegnatarioVO>();

    private String msgDestinatarioCompetente;

    private int intDocEditor;

    private Collection<FascicoloVO> fascicoliRisposta = new ArrayList<FascicoloVO>();
    
    private int rispostaId;
   
    private int docRepertorioId;

	public int getDocRepertorioId() {
		return docRepertorioId;
	}

	public void setDocRepertorioId(int docRepertorioId) {
		this.docRepertorioId = docRepertorioId;
	}


	public int getIntDocEditor() {
		return intDocEditor;
	}

	public void setIntDocEditor(int intDocEditor) {
		this.intDocEditor = intDocEditor;
	}
    
    public AssegnatarioVO getMittente() {
        return mittente;
    }

    public void setMittente(AssegnatarioVO mittente) {
        this.mittente = mittente;
    }

    public Collection<AssegnatarioVO> getDestinatari() {
		return destinatari;
	}

	public void setDestinatari(Collection<AssegnatarioVO> destinatari) {
		this.destinatari = destinatari;
	}

	public void aggiungiDestinatario(AssegnatarioVO assegnatario) {
        if (assegnatario != null) {
        	destinatari.add(assegnatario);
        }
    }
	
    public void aggiungiDestinatari(Collection<AssegnatarioVO> assegnatari) {
        this.destinatari.addAll(assegnatari);
    }

    public void removeDestinatari() {
    	destinatari.clear();
    }
    
    public String getMsgDestinatarioCompetente() {
        return msgDestinatarioCompetente;
    }

    public void setMsgAssegnatarioCompetente(String msgDestinatarioCompetente) {
        this.msgDestinatarioCompetente = msgDestinatarioCompetente;
    }

	public Collection<FascicoloVO> getFascicoliRisposta() {
		return fascicoliRisposta;
	}

	public void setFascicoliRisposta(Collection<FascicoloVO> fascicoliRisposta) {
		this.fascicoliRisposta = fascicoliRisposta;
	}

	public int getRispostaId() {
		return rispostaId;
	}

	public void setRispostaId(int rispostaId) {
		this.rispostaId = rispostaId;
	}
	
}

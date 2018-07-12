package it.compit.fenice.mvc.presentation.actionform.protocollo;

import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.presentation.helper.FascicoloView;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;

public class CruscottoForm extends ActionForm {


	private static final long serialVersionUID = 1L;

	private UfficioVO ufficioCorrente;

	private Collection<UfficioVO> ufficiDipendenti;

	private Collection<UtenteVO> utenti;
	
	private int ufficioCorrenteId;

	private String ufficioCorrentePath;

	private int ufficioSelezionatoId;

	private int utenteSelezionatoId;
	
	private AssegnatarioView sottoposto;
	
	private Map<Integer,ReportProtocolloView> protocolli = new HashMap<Integer,ReportProtocolloView>();
	
	private Map<Integer,ReportProtocolloView> posta = new HashMap<Integer,ReportProtocolloView>();
	
	private Map<Integer, FascicoloView> fascicoliReferente = new HashMap<Integer, FascicoloView>();

	private Map<Integer, FascicoloView> fascicoliIstruttore = new HashMap<Integer, FascicoloView>();

	private boolean abilitato = false;
	
	private String[] protocolloChkBox;

	private String[] postaChkBox;
	
	private String[] fascicoloReferenteChkBox;
	
	private String[] fascicoloIstruttoreChkBox;
	
	public String[] getFascicoloReferenteChkBox() {
		return fascicoloReferenteChkBox;
	}

	public void setFascicoloReferenteChkBox(String[] fascicoloReferenteChkBox) {
		this.fascicoloReferenteChkBox = fascicoloReferenteChkBox;
	}

	public String[] getFascicoloIstruttoreChkBox() {
		return fascicoloIstruttoreChkBox;
	}

	public void setFascicoloIstruttoreChkBox(String[] fascicoloIstruttoreChkBox) {
		this.fascicoloIstruttoreChkBox = fascicoloIstruttoreChkBox;
	}

	public String[] getProtocolloChkBox() {
		return protocolloChkBox;
	}

	public String[] getPostaChkBox() {
		return postaChkBox;
	}

	public void setProtocolloChkBox(String[] protocolloChkBox) {
		this.protocolloChkBox = protocolloChkBox;
	}

	public void setPostaChkBox(String[] postaChkBox) {
		this.postaChkBox = postaChkBox;
	}

	public boolean isAbilitato() {
		return abilitato;
	}

	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	public Collection<UfficioVO> getUfficiDipendenti() {
		return ufficiDipendenti;
	}
	
	public void setUfficiDipendenti(Collection<UfficioVO> ufficiDipendenti) {
		this.ufficiDipendenti = ufficiDipendenti;
	}

	public Collection<UtenteVO> getUtenti() {
		return utenti;
	}

	public UtenteVO getUtente(int utenteId) {
		for (Iterator<UtenteVO> i = getUtenti().iterator(); i.hasNext();) {
			UtenteVO ute = i.next();
			if (ute.getId().intValue() == utenteId) {
				return ute;
			}
		}
		return null;
	}

	public void setUtenti(Collection<UtenteVO> utenti) {
		this.utenti = utenti;
	}
	
	public int getUfficioCorrenteId() {
		return ufficioCorrenteId;
	}

	public void setUfficioCorrenteId(int ufficioCorrenteId) {
		this.ufficioCorrenteId = ufficioCorrenteId;
	}
	
	public int getUfficioSelezionatoId() {
		return ufficioSelezionatoId;
	}

	public void setUfficioSelezionatoId(int ufficioCorrenteId) {
		this.ufficioSelezionatoId = ufficioCorrenteId;
	}
	
	public int getUtenteSelezionatoId() {
		return utenteSelezionatoId;
	}

	public void setUtenteSelezionatoId(int utenteCorrenteId) {
		this.utenteSelezionatoId = utenteCorrenteId;
	}
	
	public UfficioVO getUfficioCorrente() {
		return ufficioCorrente;
	}

	public void setUfficioCorrente(UfficioVO ufficioCorrente) {
		this.ufficioCorrente = ufficioCorrente;
	}

	public String getUfficioCorrentePath() {
		return ufficioCorrentePath;
	}

	public void setUfficioCorrentePath(String ufficioCorrentePath) {
		this.ufficioCorrentePath = ufficioCorrentePath;
	}
	//
	public Map<Integer,ReportProtocolloView> getAllPosta() {
        return posta;
    }

    public Collection<ReportProtocolloView> getPostaCollection() {
        if (posta != null) {
        	List<ReportProtocolloView> values = new ArrayList<ReportProtocolloView>(posta.values());
        	Collections.sort(values);
            return values;
        } else
            return null;
    }
    
    public void removePosta(Integer postaId) {
        protocolli.remove(postaId);
    }

    public void removePosta(int postaId) {
        removePosta(new Integer(postaId));
    }

    public void removeAllPosta() {
        if (posta != null) {
            posta.clear();
        }
    }

    public void setPosta(Map<Integer, ReportProtocolloView>  posta) {
        this.posta = posta;
    }
	
	public Map<Integer,ReportProtocolloView> getProtocolli() {
        return protocolli;
    }
	
    public Collection<ReportProtocolloView> getProtocolliCollection() {
        if (protocolli != null) {
        	List<ReportProtocolloView> values = new ArrayList<ReportProtocolloView>(protocolli.values());
        	Collections.sort(values);
            return values;
        } else
            return null;
    }
    
    public void removeProtocollo(Integer protocolloId) {
        protocolli.remove(protocolloId);
    }

    public void removeProtocolli(int protocolloId) {
        removeProtocollo(new Integer(protocolloId));
    }

    public void removeProtocolli() {
        if (protocolli != null) {
            protocolli.clear();
        }
    }

    public void setProtocolli(Map<Integer,ReportProtocolloView> protocolli) {
        this.protocolli = protocolli;
    }

	public AssegnatarioView getSottoposto() {
		return sottoposto;
	}

	public void setSottoposto(AssegnatarioView sottoposto) {
		this.sottoposto = sottoposto;
	}

	public Map<Integer, FascicoloView> getFascicoliReferente() {
		return fascicoliReferente;
	}

	public void setFascicoliReferente(Map<Integer, FascicoloView> fascicoliReferente) {
		this.fascicoliReferente = fascicoliReferente;
	}
	
	public Collection<FascicoloView> getFascicoliReferenteCollection() {
        	return fascicoliReferente.values();
	}
	
	public Map<Integer, FascicoloView> getFascicoliIstruttore() {
		return fascicoliIstruttore;
	}

	public void setFascicoliIstruttore(Map<Integer, FascicoloView> fascicoliIstruttore) {
		this.fascicoliIstruttore = fascicoliIstruttore;
	}
	
	public Collection<FascicoloView> getFascicoliIstruttoreCollection() {
        	return fascicoliIstruttore.values();
	}
}

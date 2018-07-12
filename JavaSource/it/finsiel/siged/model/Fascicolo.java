
package it.finsiel.siged.model;

import it.compit.fenice.enums.TipoVisibilitaUfficioEnum;
import it.compit.fenice.mvc.vo.protocollo.DocumentoFascicoloVO;
import it.finsiel.siged.mvc.vo.documentale.FileVO;
import it.finsiel.siged.mvc.vo.protocollo.FaldoneVO;
import it.finsiel.siged.mvc.vo.protocollo.FascicoloVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;

import java.util.ArrayList;
import java.util.Collection;


public class Fascicolo {
	private FascicoloVO fascicoloVO = new FascicoloVO();

	//di tipo ProtocolloFascicoloVO o ReportProtocolloView
	private Collection protocolli = new ArrayList();

	private Collection<DocumentoFascicoloVO> documenti = new ArrayList<DocumentoFascicoloVO>(); 

	private Collection<FaldoneVO> faldoni = new ArrayList<FaldoneVO>(); 

	private Collection<ProcedimentoVO> procedimenti = new ArrayList<ProcedimentoVO>();
	
	private TipoVisibilitaUfficioEnum visibilita;

	
	public TipoVisibilitaUfficioEnum getVisibilita() {
		return visibilita;
	}

	public void setVisibilita(TipoVisibilitaUfficioEnum visibilita) {
		this.visibilita = visibilita;
	}

	public Collection<DocumentoFascicoloVO> getDocumenti() {
		return documenti;
	}

	public void setDocumenti(Collection<DocumentoFascicoloVO> documenti) {
		this.documenti = documenti;
	}

	public FascicoloVO getFascicoloVO() {
		return fascicoloVO;
	}

	public void setFascicoloVO(FascicoloVO fascicolo) {
		this.fascicoloVO = fascicolo;
	}

	public Collection getProtocolli() {
		return protocolli;
	}

	public void setProtocolli(Collection protocolli) {
		this.protocolli = protocolli;
	}

	public Collection<FaldoneVO> getFaldoni() {
		return faldoni;
	}

	public void setFaldoni(Collection<FaldoneVO> faldoni) {
		this.faldoni = faldoni;
	}

	public Collection<ProcedimentoVO> getProcedimenti() {
		return procedimenti;
	}

	public void setProcedimenti(Collection<ProcedimentoVO> procedimenti) {
		this.procedimenti = procedimenti;
	}
}

package it.finsiel.siged.mvc.presentation.actionform.documentale;

import it.finsiel.siged.mvc.presentation.helper.AssegnatarioView;
import it.finsiel.siged.mvc.vo.documentale.CartellaVO;
import it.finsiel.siged.mvc.vo.documentale.FileVO;
import it.finsiel.siged.mvc.vo.organizzazione.UfficioVO;
import it.finsiel.siged.mvc.vo.organizzazione.UtenteVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


public class CartelleForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private Collection<CartellaVO> sottoCartelle = new ArrayList<CartellaVO>();

	private Collection<CartellaVO> pathCartella = new ArrayList<CartellaVO>();

	private int cartellaCorrenteId;

	private String nomeCartella;

	private String curNomeCartella;

	private int cartellaId;

	private int documentoId;

	private Collection<FileVO> files = new ArrayList<FileVO>();

	private int ufficioCorrenteId;

	private String ufficioCorrentePath;

	private int ufficioSelezionatoId;

	private int utenteSelezionatoId;

	private UfficioVO ufficioCorrente;

	private Collection ufficiDipendenti;

	private Collection<UtenteVO> utenti;

	private AssegnatarioView utenteDestinatario;
	
	private boolean fromProcedimento;

	private int procedimentoId;
	
	
	public int getProcedimentoId() {
		return procedimentoId;
	}

	public void setProcedimentoId(int procedimentoId) {
		this.procedimentoId = procedimentoId;
	}

	public AssegnatarioView getUtenteDestinatario() {
		return utenteDestinatario;
	}

	public void setUtenteDestinatario(AssegnatarioView utenteDestinatario) {
		this.utenteDestinatario = utenteDestinatario;
	}

	public String getUtenteDestinatarioName() {
		return utenteDestinatario.getNomeUtente();
	};

	public boolean isFromProcedimento() {
		return fromProcedimento;
	}

	public void setFromProcedimento(boolean indietroVisibile) {
		this.fromProcedimento = indietroVisibile;
	}

	public Collection<FileVO> getFiles() {
		return files;
	}

	public int getDocumentoId() {
		return documentoId;
	}

	public void setDocumentoId(int documentoId) {
		this.documentoId = documentoId;
	}

	public void setFiles(Collection<FileVO> files) {
		this.files = files;
	}

	public int getCartellaId() {
		return cartellaId;
	}

	public void setCartellaId(int cartellaId) {
		this.cartellaId = cartellaId;
	}

	public String getNomeCartella() {
		return nomeCartella;
	}

	public void setNomeCartella(String nomeCartella) {
		this.nomeCartella = nomeCartella;
	}

	public int getCartellaCorrenteId() {
		return cartellaCorrenteId;
	}

	public void setCartellaCorrenteId(int cartellaCorrenteId) {
		this.cartellaCorrenteId = cartellaCorrenteId;
	}

	public Collection<CartellaVO> getPathCartella() {
		return pathCartella;
	}

	public void setPathCartella(Collection<CartellaVO> pathCartella) {
		this.pathCartella = pathCartella;
	}

	public Collection<CartellaVO> getSottoCartelle() {
		return sottoCartelle;
	}

	public void setSottoCartelle(Collection<CartellaVO> sottoCartelle) {
		this.sottoCartelle = sottoCartelle;
	}

	public String getCurNomeCartella() {
		return curNomeCartella;
	}

	public void setCurNomeCartella(String curNomeCartella) {
		this.curNomeCartella = curNomeCartella;
	}

	//
	public Collection getUfficiDipendenti() {
		return ufficiDipendenti;
	}

	public void setUfficiDipendenti(Collection ufficiDipendenti) {
		this.ufficiDipendenti = ufficiDipendenti;
	}

	public Collection<UtenteVO> getUtenti() {
		return utenti;
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

	public UtenteVO getUtente(int utenteId) {
		for (Iterator<UtenteVO> i = getUtenti().iterator(); i.hasNext();) {
			UtenteVO ute = i.next();
			if (ute.getId().intValue() == utenteId) {
				return ute;
			}
		}
		return null;
	}

	//

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		if (request.getParameter("salvaCartella") != null) {
			if (getNomeCartella() == null
					|| "".equals(getNomeCartella().trim())) {
				errors.add("salvaCartella", new ActionMessage(
						"selezione.obbligatoria", "nome cartella", ""));
			}
		}
		return errors;

	}

}

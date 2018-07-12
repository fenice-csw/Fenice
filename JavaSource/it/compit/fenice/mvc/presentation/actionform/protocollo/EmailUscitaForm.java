package it.compit.fenice.mvc.presentation.actionform.protocollo;

import it.compit.fenice.mvc.presentation.helper.CodaInvioView;
import it.finsiel.siged.mvc.business.LookupDelegate;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.posta.PecDestVO;
import it.finsiel.siged.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class EmailUscitaForm extends ActionForm {
	   
		private static final long serialVersionUID = 1L;

		static Logger logger = Logger.getLogger(EmailUscitaForm.class.getName());

		private Map<Integer, CodaInvioView> mailUscita;
		
		 private List<PecDestVO> destinatari;
		    
		private String dataCreazione;
		    
		private String numeroProtocollo;
		
		private int mailId;

		private String dataInizio;

	    private String dataFine;
	    
	    private Collection<IdentityVO> statiMail;

		private String statoMail;

		public Map<Integer, CodaInvioView> getMailUscita() {
			return mailUscita;
		}

		public void setMailUscita(Map<Integer, CodaInvioView> mailUscita) {
			this.mailUscita = mailUscita;
		}

		public Collection<CodaInvioView> getMailUscitaCollection() {
			return mailUscita.values();
		}
		
		public void resetMailUscita() {
			this.mailUscita.clear();
		}
		
		public List<PecDestVO> getDestinatari() {
			return destinatari;
		}
		
		public void setDestinatari(List<PecDestVO> destinatari) {
			this.destinatari = destinatari;
		}
		
		public String getDataCreazione() {
			return dataCreazione;
		}

		public void setDataCreazione(String dataCreazione) {
			this.dataCreazione = dataCreazione;
		}

		public String getNumeroProtocollo() {
			return numeroProtocollo;
		}

		public void setNumeroProtocollo(String numeroProtocollo) {
			this.numeroProtocollo = numeroProtocollo;
		}

		public int getMailId() {
			return mailId;
		}

		public void setMailId(int mailId) {
			this.mailId = mailId;
		}
		
		public Collection<IdentityVO>  getStatiMail() {
			return statiMail;
		}

		public void setStatiMail(Collection<IdentityVO> statiMail) {
			this.statiMail = statiMail;
		}

		public String getStatoMail() {
			return statoMail;
		}

		public void setStatoMail(String statoMail) {
			this.statoMail = statoMail;
		}

		public String getDataInizio() {
			return dataInizio;
		}

		public void setDataInizio(String dataInizio) {
			this.dataInizio = dataInizio;
		}

		public String getDataFine() {
			return dataFine;
		}

		public void setDataFine(String dataFine) {
			this.dataFine = dataFine;
		}

		public PecDestVO getOrderItem(int index)
	    {
	        if(this.destinatari == null)
	        {
	            this.destinatari = new ArrayList<PecDestVO>();
	        }
	 
	        while(index >= this.destinatari.size())
	        {
	            this.destinatari.add(new PecDestVO());
	        }
	 
	        return (PecDestVO) destinatari.get(index);
	    }
		
		public ActionErrors validate(ActionMapping mapping,
				HttpServletRequest request) {
			ActionErrors errors = new ActionErrors();

			if (getDataInizio() == null || "".equals(getDataInizio())) {
				errors.add("dataInizio", new ActionMessage("campo.obbligatorio",
						"Data Creazione dal", ""));
			} else if (!DateUtil.isData(getDataInizio())) {
				errors.add("dataInizio", new ActionMessage("formato.data.errato",
						"Data Creazione dal", ""));
			}
			if (getDataFine() == null || "".equals(getDataFine())) {
				errors.add("dataFine", new ActionMessage("campo.obbligatorio",
						"Data Creazione al", ""));
			} else if (!DateUtil.isData(getDataFine())) {
				errors.add("dataFine", new ActionMessage("formato.data.errato",
						"Data Creazione al", ""));
			}
			if (getDataFine() != null
					&& !"".equals(getDataFine().trim())
					&& getDataInizio() != null
					&& !"".equals(getDataInizio().trim())
					&& DateUtil.toDate(getDataFine()).before(
							DateUtil.toDate(getDataInizio()))) {
				errors.add("dataInizio", new ActionMessage("date_incongruenti"));
			}

			return errors;
		}

		public void inizialize() {
			setStatiMail(LookupDelegate.getInstance().getStatiEmailUscita());
			if (getDataInizio() == null)
				setDataInizio(DateUtil.formattaData(System.currentTimeMillis()));
			if (getDataFine() == null)
				setDataFine(DateUtil.formattaData(System.currentTimeMillis()));
			
		}
		
		
		//
		
}

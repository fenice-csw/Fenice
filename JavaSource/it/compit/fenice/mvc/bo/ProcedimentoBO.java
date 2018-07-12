package it.compit.fenice.mvc.bo;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.mvc.bo.ProtocolloBO;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.ProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;

import javax.servlet.http.HttpServletRequest;

public class ProcedimentoBO {

	public static void preparaPostaInterna(HttpServletRequest request,
			ProcedimentoVO procedimento, Utente utente) throws DataException {
		PostaInterna pi = ProtocolloBO.getDefaultPostaInterna(utente);
		try {
			ProtocolloVO protocollo = pi.getProtocollo();
			AssegnatarioVO mittente = new AssegnatarioVO();
			mittente.setUfficioAssegnatarioId(utente.getUfficioInUso());
			mittente.setUtenteAssegnatarioId(utente.getValueObject().getId());
			pi.setMittente(mittente);
			pi.setProtocollo(protocollo);
			ProtocolloIngresso protocolloIngresso = ProtocolloDelegate.getInstance().getProtocolloIngressoById(procedimento.getProtocolloId());
			pi.getProtocollo().setStatoProtocollo("P");
			AllaccioVO allaccioVo = new AllaccioVO();
			allaccioVo
					.setProtocolloAllacciatoId(procedimento.getProtocolloId());
			allaccioVo.setAllaccioDescrizione(protocolloIngresso.getProtocollo().getNumProtocollo()+ "/"+ protocolloIngresso.getProtocollo().getAnnoRegistrazione()
					+ " ("+ protocolloIngresso.getProtocollo().getFlagTipo()+ ")");
			pi.allacciaProtocollo(allaccioVo);
			aggiornaAssegnatariPostaInternaModel(procedimento, pi, utente);
			pi.setProcedimenti(protocolloIngresso.getProcedimenti());
			pi.setFascicoliRisposta(protocolloIngresso
					.getFascicoli());
			request.setAttribute(Constants.POSTA_INTERNA_DA_PROCEDIMENTO, pi);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException("Errore nella generazione del Protocollo");
		}
	}

	private static void aggiornaAssegnatariPostaInternaModel(
			ProcedimentoVO procedimento, PostaInterna protocollo, Utente utente) {
		protocollo.removeDestinatari();
		AssegnatarioVO destinatario = new AssegnatarioVO();
		destinatario.setUfficioAssegnatarioId(procedimento.getUfficioId());
		destinatario.setCaricaAssegnatarioId(procedimento.getResponsabileId());
		destinatario.setCaricaAssegnanteId(utente.getCaricaInUso());
		destinatario.setUfficioAssegnanteId(utente.getUfficioInUso());
		destinatario.setCompetente(true);
		protocollo.aggiungiDestinatario(destinatario);

	}

}

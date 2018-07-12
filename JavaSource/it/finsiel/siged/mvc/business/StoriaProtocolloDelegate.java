package it.finsiel.siged.mvc.business;

import it.compit.fenice.model.protocollo.PostaInterna;
import it.finsiel.siged.constant.Constants;
import it.finsiel.siged.dao.DAOFactory;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.model.protocollo.Protocollo;
import it.finsiel.siged.model.protocollo.ProtocolloIngresso;
import it.finsiel.siged.model.protocollo.ProtocolloUscita;
import it.finsiel.siged.mvc.integration.StoriaProtocolloDAO;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.mvc.presentation.helper.VersioneProtocolloView;
import it.finsiel.siged.mvc.vo.protocollo.AllaccioVO;
import it.finsiel.siged.mvc.vo.protocollo.AssegnatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.DestinatarioVO;
import it.finsiel.siged.mvc.vo.protocollo.ProtocolloVO;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;

public class StoriaProtocolloDelegate {

    private static Logger logger = Logger
            .getLogger(StoriaProtocolloDelegate.class.getName());

    private StoriaProtocolloDAO storiaProtocolloDAO = null;

    //private ServletConfig config = null;

    private static StoriaProtocolloDelegate delegate = null;

    private StoriaProtocolloDelegate() {
        try {
            if (storiaProtocolloDAO == null) {
                storiaProtocolloDAO = (StoriaProtocolloDAO) DAOFactory
                        .getDAO(Constants.STORIA_PROTOCOLLO_DAO_CLASS);

                logger.debug("storiaProtocolloDAO instantiated:"
                        + Constants.STORIA_PROTOCOLLO_DAO_CLASS);
            }
        } catch (Exception e) {
            logger.error("Exception while connecting to UserDAOjdbc!!", e);
        }
    }

    public Collection<VersioneProtocolloView> getStoriaProtocollo(Utente utente, int protocolloId) {
        try {
            return storiaProtocolloDAO
                    .getStoriaProtocollo(utente, protocolloId);
        } catch (DataException de) {
            logger
                    .error("StoriaProtocolloDelegate: failed getting getStoriaProtocollo: ");
            return null;
        }
    }

    public Collection<VersioneProtocolloView> getStoriaProtocollo(Utente utente, int protocolloId,
            String flagTipo) {
        try {
            return storiaProtocolloDAO.getStoriaProtocollo(utente,
                    protocolloId, flagTipo);
        } catch (DataException de) {
            logger.error("StoriaProtocolloDelegate: failed getting getStoriaProtocolloS: "+de);
            return null;
        }
    }

    public static StoriaProtocolloDelegate getInstance() {
        if (delegate == null)
            delegate = new StoriaProtocolloDelegate();
        return delegate;
    }

    public static String getIdentifier() {
        return Constants.STORIA_PROTOCOLLO_DELEGATE;
    }

    public ProtocolloVO getVersioneProtocollo(int id, int versione) {
        logger.info("StoriaProtocolloDelegate:getProtocolloById");
        try {
            return storiaProtocolloDAO.getVersioneProtocollo(id, versione);
        } catch (DataException de) {
            logger
                    .error("StoriaProtocolloDelegate: failed getProtocolloById: ");
            return null;
        }
    }

    public ProtocolloIngresso getVersioneProtocolloIngresso(int id, int versione) {
        ProtocolloIngresso pi = new ProtocolloIngresso();
        try {
            pi.setProtocollo(storiaProtocolloDAO.getVersioneProtocollo(id,
                    versione));
            pi.setAllegati(storiaProtocolloDAO.getAllegatiVersioneProtocollo(
                    id, versione));
            if (pi.getProtocollo().getDocumentoPrincipaleId() != null) {
                pi.setDocumentoPrincipale(DocumentoDelegate.getInstance().getDocumento(pi.getProtocollo().getDocumentoPrincipaleId().intValue()));
            }
            
            pi.aggiungiAssegnatari(getAssegnatariVersioneProtocollo(id,versione));
            pi.setAllacci(getAllacciVersioneProtocollo(id, versione));
            pi.setProcedimenti(storiaProtocolloDAO.getVersioneProcedimentiProtocollo(id, versione));
            setFascicoliProtocollo(pi, id);
            return pi;
        } catch (DataException de) {
            logger
                    .error("StoriaProtocolloDelegate: failed getProtocolloById: ");
            return null;
        }
    }

    public ProtocolloUscita getVersioneProtocolloUscita(int id, int versione) {
        ProtocolloUscita pu = new ProtocolloUscita();
        try {
            pu.setProtocollo(storiaProtocolloDAO.getVersioneProtocollo(id,
                    versione));
            pu.setAllegati(storiaProtocolloDAO.getAllegatiVersioneProtocollo(
                    id, versione));
            if (pu.getProtocollo().getDocumentoPrincipaleId() != null) {
                pu.setDocumentoPrincipale(DocumentoDelegate.getInstance()
                        .getDocumento(
                                pu.getProtocollo().getDocumentoPrincipaleId()
                                        .intValue()));
            }
            pu.setDestinatari(getDestinatariVersioneProtocollo(id, versione));
            // imposto il mittente con i dati relativi all'assegnatario per
            // competenza
            AssegnatarioVO assegnatario = new AssegnatarioVO();
            assegnatario.setUfficioAssegnatarioId(pu.getProtocollo()
                    .getUfficioMittenteId());
            assegnatario.setUtenteAssegnatarioId(pu.getProtocollo()
                    .getUtenteMittenteId());
            pu.setMittente(assegnatario);
            pu.setAllacci(getAllacciVersioneProtocollo(id, versione));
            pu.setProcedimenti(storiaProtocolloDAO
                    .getVersioneProcedimentiProtocollo(id, versione));
            setFascicoliProtocollo(pu, id);
            return pu;
        } catch (DataException de) {
            logger
                    .error("StoriaProtocolloDelegate: failed getProtocolloById: ");
            return null;
        }
    }

    public Map<String,DestinatarioVO> getDestinatariVersioneProtocollo(int protocolloId, int versione) {
        try {
            return storiaProtocolloDAO.getDestinatariVersioneProtocollo(
                    protocolloId, versione);
        } catch (DataException de) {
            logger
                    .error("StoriaProtocolloDelegate: failed getting getDestinatariVersioneProtocollo: ");
            return null;
        }
    }

    private void setFascicoliProtocollo(Protocollo p, int protocolloId) {
        p.setFascicoli(
                FascicoloDelegate.getInstance().getFascicoliByProtocolloId(
                        protocolloId));
    }

    public Collection<AssegnatarioVO> getAssegnatariVersioneProtocollo(int protocolloId,
            int versione) {
        try {
            return storiaProtocolloDAO.getAssegnatariVersioneProtocollo(
                    protocolloId, versione);
        } catch (DataException de) {
            logger
                    .error("StoriaProtocolloDelegate: failed getting getAssegnatariVersioneProtocollo: ");
            return null;
        }
    }

    public Collection<AllaccioVO> getAllacciVersioneProtocollo(int protocolloId,
            int versione) {
        try {
            return storiaProtocolloDAO.getAllacciVersioneProtocollo(
                    protocolloId, versione);
        } catch (DataException de) {
            logger
                    .error("StoriaProtocolloDelegate: failed getting getAllacciVersioneProtocollo: ");
            return null;
        }
    }

    public Collection<String> getAnniScartabili(int registroId) {
        try {
            return storiaProtocolloDAO.getAnniScartabili(registroId);
        } catch (DataException de) {
            logger
                    .error("StoriaProtocolloDelegate: failed getting getAnniScartabili: ");
            return null;
        }
    }

    public int getNumProcolliNonScartabili(int registroId, int anno) {
        try {
            return storiaProtocolloDAO.getNumProcolliNonScartabili(registroId,
                    anno);
        } catch (DataException de) {
            logger
                    .error("StoriaProtocolloDelegate: failed getting getNumProcolliNonScartabili: ");
            return 0;
        }
    }

    public int scarto(Utente utente, int anno) {
        try {
            return storiaProtocolloDAO.scarto(utente, anno);
        } catch (DataException de) {
            logger.error("StoriaProtocolloDelegate: failed getting scarto: ");
        }
        return 0;
    }

    public boolean isScartato(int protocolloId) {
        try {
            return storiaProtocolloDAO.isScartato(protocolloId);
        } catch (DataException de) {
            logger
                    .error("StoriaProtocolloDelegate: failed getting isScartato: ");
        }
        return false;
    }

    public SortedMap<Integer,ReportProtocolloView> cercaScartoProtocolli(Utente utente, String tipo,
            String mozione, String stato, String riservato, Date dataRegDa,
            Date dataRegA, int NumeroDa, int NumeroA, int AnnoDa, int AnnoA) {
        try {

            return storiaProtocolloDAO.cercaScartoProtocolli(utente, tipo,
                    mozione, stato, riservato, dataRegDa, dataRegA, NumeroDa,
                    NumeroA, AnnoDa, AnnoA);
        } catch (DataException de) {
            logger
                    .error("ProtocolloDelegate: failed getting cercaScartoProtocolli: ");
            return null;
        }

    }

    public SortedMap<Integer,ReportProtocolloView> cercaProtocolliDaScartare(Utente utente, int idUfficio,
            int idServizio, Date dataRegistrazioneDa, Date dataRegistrazioneA) {
        try {

            return storiaProtocolloDAO.cercaProtocolliDaScartare(utente,
                    idUfficio, idServizio, dataRegistrazioneDa,
                    dataRegistrazioneA);
        } catch (DataException de) {
            logger
                    .error("ProtocolloDelegate: failed getting cercaProtocolliDaScartare: ");
            return null;
        }
    }

	public PostaInterna getVersionePostaInterna(int id, int versione) {
		PostaInterna pi = new PostaInterna();
	        try {
	            pi.setProtocollo(storiaProtocolloDAO.getVersioneProtocollo(id,
	                    versione));
	            pi.setAllegati(storiaProtocolloDAO.getAllegatiVersioneProtocollo(
	                    id, versione));
	            if (pi.getProtocollo().getDocumentoPrincipaleId() != null) {
	                pi.setDocumentoPrincipale(DocumentoDelegate.getInstance()
	                        .getDocumento(
	                                pi.getProtocollo().getDocumentoPrincipaleId()
	                                        .intValue()));
	            }
	            pi.aggiungiDestinatari(getAssegnatariVersioneProtocollo(id,
	                    versione));
	            pi.setAllacci(getAllacciVersioneProtocollo(id, versione));
	            pi.setProcedimenti(storiaProtocolloDAO
	                    .getVersioneProcedimentiProtocollo(id, versione));
	            setFascicoliProtocollo(pi, id);
	            return pi;
	        } catch (DataException de) {
	            logger
	                    .error("StoriaProtocolloDelegate: failed getProtocolloById: ");
	            return null;
	        }
	}
}
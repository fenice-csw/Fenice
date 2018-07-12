package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.CRLNonAggiornataException;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.firma.CaVO;
import it.finsiel.siged.mvc.vo.firma.CrlUrlVO;

import java.security.cert.X509CRLEntry;
import java.sql.Connection;
import java.sql.Date;
import java.util.Collection;
import java.util.Set;

/*
 * @author Almaviva sud
 */

public interface FirmaDigitaleDAO {

    public CaVO salvaCertificateAuthotority(CaVO ca) throws DataException;

    public CaVO salvaCertificateAuthotority(Connection connection, CaVO ca)
            throws DataException;

    public CrlUrlVO salvaCaCRLPuntoDistribuzione(Connection connection,
            CrlUrlVO vo) throws DataException;

    public CrlUrlVO salvaCaCRLPuntoDistribuzione(CrlUrlVO vo)
            throws DataException;

    public Collection<CrlUrlVO> getPuntiDistribuzioneCRL() throws DataException;

    public void salvaListaCertificatiRevocati(Set<X509CRLEntry>revokedCerts, int caId,
            int crlId) throws DataException;

    public void setStatoErroreCRL(Connection connection, int caCrlId,
            int errorCode) throws Exception;

    public void setStatoErroreCRL(int caCrlId, int errorCode);

    public void cancellaCA(Connection connection, int caId)
            throws DataException;

    public CaVO getCAByCN(Connection connection, String cn)
            throws DataException;

    public CaVO getCA(int id) throws DataException;

    public CaVO getCA(Connection connection, int id) throws DataException;

    public Collection<CaVO> getAllCA() throws DataException;

    public Date getDataSeRevocato(String issuerDN, String serial)
            throws DataException, CRLNonAggiornataException;

    public void cancellaTutteCA() throws DataException;
}
package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.model.organizzazione.AreaOrganizzativa;
import it.finsiel.siged.mvc.vo.IdentityVO;
import it.finsiel.siged.mvc.vo.lookup.ProvinciaVO;
import it.finsiel.siged.mvc.vo.lookup.TipoDocumentoVO;
import it.finsiel.siged.mvc.vo.lookup.TipoProcedimentoVO;
import it.finsiel.siged.mvc.vo.protocollo.SpedizioneVO;
import it.flosslab.mvc.vo.OggettoVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/*
 * @author Almaviva sud.
 */

public interface LookupDAO {
    public Map<Integer,ArrayList<TipoDocumentoVO>> getTipiDocumento(Collection<AreaOrganizzativa> aoo) throws DataException;
    
    public Map<Integer,ArrayList<TipoProcedimentoVO>> getTipiProcedimento(Collection<AreaOrganizzativa> aoo) throws DataException;

    public Collection<ProvinciaVO> getProvince() throws DataException;

    public int getProvinciaIdFromCodiProv(String codi) throws DataException;
    
    public Map<Integer,ArrayList<SpedizioneVO>> getMezziSpedizione(Collection<AreaOrganizzativa> aoo) throws DataException;

    public SpedizioneVO getMezzoSpedizione(int mezzoId) throws DataException;

    public SpedizioneVO getMezzoSpedizioneDaMezzo(String m) throws DataException;

    public Collection<IdentityVO> getTitoliDestinatario() throws DataException;
    
    public Map<String,OggettoVO> getOggetti(int aooId)throws DataException;

}
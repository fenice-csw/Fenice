package it.finsiel.siged.mvc.integration;


import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.protocollo.AnnotazioneVO;

import java.sql.Connection;
import java.util.ArrayList;

public interface AnnotazioneDAO {

    public AnnotazioneVO getAnnotazione(int id) throws DataException;

    public AnnotazioneVO getAnnotazione(Connection connection, int id)
            throws DataException;

    public AnnotazioneVO newAnnotazioneVO(int protocolloId,
            AnnotazioneVO annotazione) throws DataException;

    public AnnotazioneVO newAnnotazioneVO(Connection connection,
            int protocolloId, AnnotazioneVO annotazione) throws DataException;

    public AnnotazioneVO newAnnotazioneVO(AnnotazioneVO annotazione)
            throws DataException;

    public AnnotazioneVO updateAnnotazioneVO(Connection connection,
            AnnotazioneVO ann) throws DataException;

    public ArrayList<AnnotazioneVO> getArrayAnnotazioneVO(int idProtocollo)
            throws DataException;

}
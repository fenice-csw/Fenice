package it.finsiel.siged.mvc.integration;

import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.mvc.vo.protocollo.ConfigurazioneUtenteVO;

import java.sql.Connection;

public interface ConfigurazioneUtenteDAO {

    public ConfigurazioneUtenteVO getConfigurazione(int utenteId)
            throws DataException;

    public void salvaConfigurazione(Connection connection,
            ConfigurazioneUtenteVO vo) throws Exception;

    public ConfigurazioneUtenteVO aggiornaParametriStampante(
            Connection connection, ConfigurazioneUtenteVO vo)
            throws DataException;

    public ConfigurazioneUtenteVO aggiornaConfigurazione(Connection connection,
            ConfigurazioneUtenteVO vo) throws DataException;

}
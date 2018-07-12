package it.finsiel.siged.mvc.integration;

// import java.sql.Connection;

import it.finsiel.siged.exception.DataException;

import java.sql.Connection;

/*
 * @author G.Calli.
 */

public interface IdentificativiDAO {
    public int insertNewId(Connection connection, String tableName, int id)
            throws DataException;

    public int updateId(Connection connection, String tableName, int newId,
            int oldId) throws DataException;

    public int getCurrentId(String tableName) throws DataException;

    public int getNextId(Connection connection, String tableName)
            throws DataException;

}
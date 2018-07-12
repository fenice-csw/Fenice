package it.finsiel.siged.rdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class JDBCManager {
    private static Logger logger = Logger
            .getLogger(JDBCManager.class.getName());

    private static DataSource dataSource;

    /**
	 * @param dataSource the dataSource to set
	 */
	public static void setDataSource(DataSource dataSource) {
		if(JDBCManager.dataSource == null){
			JDBCManager.dataSource = dataSource;
		}
	}

	public JDBCManager() {
    };

    private DataSource getDataSource() {
        if (dataSource == null) {
            ResourceBundle bundle = ResourceBundle.getBundle("DbConfig");
            String dsName = bundle.getString("datasource.jndiName");
            //String dsName = "java:eProtDS";
            logger.info("JNDI lookup per: "+dsName);
            Connection conn = null;
            try {
                Context ctx = new InitialContext();
                DataSource ds = (DataSource) ctx.lookup(dsName);
                conn = ds.getConnection();
                dataSource = ds;
                logger.info("Istanziato il DataSource: " + dsName);
            } catch (Throwable e) {
                String msg = "Impossibile istanziare il DataSource: " + dsName;
                logger.error(msg, e);
            } finally {
                close(conn);
            }
        }
        return dataSource;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            conn = getDataSource().getConnection();
        } catch (Throwable e) {
            String msg = "Impossibile ottenere una connessione al DB.";
            logger.error(msg, e);
            throw new SQLException(msg);
        }
        return conn;
    }

    public void rollback(Connection c) {
        if (c != null) {
            try {
                c.rollback();
            } catch (Exception e) {
            }
        }
    }

    public void close(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
            }
        }
    }

    public void close(PreparedStatement prepStmt) {
        if (prepStmt != null) {
            try {
                prepStmt.close();
            } catch (Exception e) {
            }
        }
    }

    public void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }
    }

    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void closeResultSetAndStatement(ResultSet rs, PreparedStatement prepStmt){
    	close(rs);
    	close(prepStmt);
    }
    
    public void closeAll(ResultSet rs, PreparedStatement prepStmt, Connection c){
    	close(rs);
    	close(prepStmt);
    	close(c);
    }
}

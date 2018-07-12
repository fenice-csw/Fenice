package it.finsiel.siged.dao.FileSystem;

import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.exception.DataException;
import it.finsiel.siged.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class MigrazioneDati {

    static Logger logger = Logger.getLogger(MigrazioneDati.class.getName());

    public static ArrayList<String> LeggiDatiDaFile(String pathfile)
            throws DataException, IOException {
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(pathfile),
                    FileConstants.BUFFER_SIZE);
            ObjectInputStream riga = new ObjectInputStream(is);

            ArrayList<String> righe = new ArrayList<String>();
            while (riga.available() != 0) {
                righe.add((String) riga.readObject());
            }

            return righe;
        } catch (FileNotFoundException e) {
            throw new DataException("File non trovato." + pathfile);
        } catch (Exception e) {
            throw new IOException("Errore" + e.getLocalizedMessage());
        } finally {
            FileUtil.closeIS(is);
        }
    }

}

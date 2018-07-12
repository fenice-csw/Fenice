package it.finsiel.siged.mvc.presentation.actionform.amministrazione.migrazionedati;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

public final class MigrazioneDatiForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(MigrazioneDatiForm.class.getName());

    private FormFile fileUffici;

    private FormFile fileUtenti;

    private FormFile filePermessi;

    private FormFile fileUteRegistri;

    private FormFile fileTitolario;

    private FormFile fileProtocolli;

    private FormFile fileProtocolliAss;

    private FormFile fileProtocolliDest;

    private FormFile fileStoriaProtocolli;

    private FormFile fileFaldoni;

    private FormFile fileReferentiUfficio;

    private FormFile fileProcedimenti;

    private FormFile fileStoriaProcedimenti;

    private FormFile fileTipiProcedimenti;

    private FormFile fileTitolariUffici;

    private FormFile fileFascicoli;

    private FormFile fileStoriaFascicoli;

    private FormFile fileFaldoniFascicoli;

    private FormFile fileFascicoliProtocolli;

    private FormFile fileProcedimentiFaldone;

    private FormFile fileProcedimentiFascicoli;

    private FormFile fileProtocolliProcedimenti;

    private FormFile fileRubrica;

    private FormFile fileListaDistribuzione;

    private FormFile fileRubricaListaDistribuzione;

    private String dirFilesMigrazione;

    public FormFile getFilePermessi() {
        return filePermessi;
    }

    public void setFilePermessi(FormFile filePermessi) {
        this.filePermessi = filePermessi;
    }

    public FormFile getFileProtocolli() {
        return fileProtocolli;
    }

    public void setFileProtocolli(FormFile fileProtocolli) {
        this.fileProtocolli = fileProtocolli;
    }

    public FormFile getFileProtocolliAss() {
        return fileProtocolliAss;
    }

    public void setFileProtocolliAss(FormFile fileProtocolliAss) {
        this.fileProtocolliAss = fileProtocolliAss;
    }

    public FormFile getFileProtocolliDest() {
        return fileProtocolliDest;
    }

    public void setFileProtocolliDest(FormFile fileProtocolliDest) {
        this.fileProtocolliDest = fileProtocolliDest;
    }

    public FormFile getFileStoriaProtocolli() {
        return fileStoriaProtocolli;
    }

    public void setFileStoriaProtocolli(FormFile fileStoriaProtocolli) {
        this.fileStoriaProtocolli = fileStoriaProtocolli;
    }

    public FormFile getFileTitolario() {
        return fileTitolario;
    }

    public void setFileTitolario(FormFile fileTitolario) {
        this.fileTitolario = fileTitolario;
    }

    public FormFile getFileUtenti() {
        return fileUtenti;
    }

    public void setFileUtenti(FormFile fileUtenti) {
        this.fileUtenti = fileUtenti;
    }

    public FormFile getFileUteRegistri() {
        return fileUteRegistri;
    }

    public void setFileUteRegistri(FormFile fileUteRegistri) {
        this.fileUteRegistri = fileUteRegistri;
    }

    public FormFile getFileUffici() {
        return fileUffici;
    }

    public void setFileUffici(FormFile fileUffici) {
        this.fileUffici = fileUffici;
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        if (request.getParameter("SalvaAction") != null) {
            if ((fileUffici.getFileName() == null)
                    || (fileUffici.getFileName().equals(""))) {
                errors.add("FileUffici", new ActionMessage("Path_File_Uffici"));
            }
            if ((fileUtenti.getFileName() == null)
                    || (fileUtenti.getFileName().equals(""))) {
                errors.add("fileUtenti", new ActionMessage("Path_File_Utenti"));
            }
            if ((filePermessi.getFileName() == null)
                    || (filePermessi.getFileName().equals(""))) {
                errors.add("filePermessi", new ActionMessage(
                        "Path_File_Permessi"));
            }
            if ((fileUteRegistri.getFileName() == null)
                    || (fileUteRegistri.getFileName().equals(""))) {
                errors.add("fileUteRegistri", new ActionMessage(
                        "Path_File_UteRegistri"));
            }
            if ((fileTitolario.getFileName() == null)
                    || (fileTitolario.getFileName().equals(""))) {
                errors.add("fileTitolario", new ActionMessage(
                        "Path_File_Titolario"));
            }
            if ((fileProtocolli.getFileName() == null)
                    || (fileProtocolli.getFileName().equals(""))) {
                errors.add("fileProtocolli", new ActionMessage(
                        "Path_File_Protocolli"));
            }
            if ((fileProtocolliAss.getFileName() == null)
                    || (fileProtocolliAss.getFileName().equals(""))) {
                errors.add("fileProtocolliAss", new ActionMessage(
                        "Path_File_ProtocolliAss"));
            }
            if ((fileProtocolliDest.getFileName() == null)
                    || (fileProtocolliDest.getFileName().equals(""))) {
                errors.add("fileProtocolliDest", new ActionMessage(
                        "Path_File_ProtocolliDest"));
            }
            if ((fileStoriaProtocolli.getFileName() == null)
                    || (fileStoriaProtocolli.getFileName().equals(""))) {
                errors.add("fileStoriaProtocolli", new ActionMessage(
                        "Path_File_ProtocolliStoria"));
            }

        }
        if (request.getParameter("CaricaDaCartellaAction") != null) {
            if (getDirFilesMigrazione() == null
                    || "".equals(getDirFilesMigrazione())) {
                errors.add("dir", new ActionMessage(
                        "errors.required","La directory dove di trovano i files"));
            }

        }
        return errors;
    }

    public FormFile getFileFaldoni() {
        return fileFaldoni;
    }

    public void setFileFaldoni(FormFile fileFaldoni) {
        this.fileFaldoni = fileFaldoni;
    }

    public FormFile getFileReferentiUfficio() {
        return fileReferentiUfficio;
    }

    public void setFileReferentiUfficio(FormFile fileReferentiUfficio) {
        this.fileReferentiUfficio = fileReferentiUfficio;
    }

    public FormFile getFileProcedimenti() {
        return fileProcedimenti;
    }

    public void setFileProcedimenti(FormFile fileProcedimenti) {
        this.fileProcedimenti = fileProcedimenti;
    }

    public FormFile getFileTipiProcedimenti() {
        return fileTipiProcedimenti;
    }

    public void setFileTipiProcedimenti(FormFile fileTipiProcedimenti) {
        this.fileTipiProcedimenti = fileTipiProcedimenti;
    }

    public FormFile getFileTitolariUffici() {
        return fileTitolariUffici;
    }

    public void setFileTitolariUffici(FormFile fileTitolariUffici) {
        this.fileTitolariUffici = fileTitolariUffici;
    }

    public FormFile getFileFascicoli() {
        return fileFascicoli;
    }

    public void setFileFascicoli(FormFile fileFascicoli) {
        this.fileFascicoli = fileFascicoli;
    }

    public FormFile getFileFaldoniFascicoli() {
        return fileFaldoniFascicoli;
    }

    public void setFileFaldoniFascicoli(FormFile fileFaldoniFascicoli) {
        this.fileFaldoniFascicoli = fileFaldoniFascicoli;
    }

    public FormFile getFileStoriaFascicoli() {
        return fileStoriaFascicoli;
    }

    public void setFileStoriaFascicoli(FormFile fileStoriaFascicoli) {
        this.fileStoriaFascicoli = fileStoriaFascicoli;
    }

    public FormFile getFileFascicoliProtocolli() {
        return fileFascicoliProtocolli;
    }

    public void setFileFascicoliProtocolli(FormFile fileFascicoliProtocolli) {
        this.fileFascicoliProtocolli = fileFascicoliProtocolli;
    }

    public FormFile getFileProcedimentiFaldone() {
        return fileProcedimentiFaldone;
    }

    public void setFileProcedimentiFaldone(FormFile fileProcedimentiFaldone) {
        this.fileProcedimentiFaldone = fileProcedimentiFaldone;
    }

    public FormFile getFileProcedimentiFascicoli() {
        return fileProcedimentiFascicoli;
    }

    public void setFileProcedimentiFascicoli(FormFile fileProcedimentiFascicoli) {
        this.fileProcedimentiFascicoli = fileProcedimentiFascicoli;
    }

    public FormFile getFileProtocolliProcedimenti() {
        return fileProtocolliProcedimenti;
    }

    public void setFileProtocolliProcedimenti(
            FormFile fileProtocolliProcedimenti) {
        this.fileProtocolliProcedimenti = fileProtocolliProcedimenti;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        MigrazioneDatiForm.logger = logger;
    }

    public FormFile getFileListaDistribuzione() {
        return fileListaDistribuzione;
    }

    public void setFileListaDistribuzione(FormFile fileListaDistribuzione) {
        this.fileListaDistribuzione = fileListaDistribuzione;
    }

    public FormFile getFileRubrica() {
        return fileRubrica;
    }

    public void setFileRubrica(FormFile fileRubrica) {
        this.fileRubrica = fileRubrica;
    }

    public FormFile getFileRubricaListaDistribuzione() {
        return fileRubricaListaDistribuzione;
    }

    public void setFileRubricaListaDistribuzione(
            FormFile fileRubricaListaDistribuzione) {
        this.fileRubricaListaDistribuzione = fileRubricaListaDistribuzione;
    }

    public FormFile getFileStoriaProcedimenti() {
        return fileStoriaProcedimenti;
    }

    public void setFileStoriaProcedimenti(FormFile fileStoriaProcedimenti) {
        this.fileStoriaProcedimenti = fileStoriaProcedimenti;
    }

    public String getDirFilesMigrazione() {
        return dirFilesMigrazione;
    }

    public void setDirFilesMigrazione(String dirFilesMigrazione) {
        this.dirFilesMigrazione = dirFilesMigrazione;
    }

}
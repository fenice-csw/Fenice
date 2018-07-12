package it.finsiel.siged.mvc.presentation.actionform;

import java.util.Collection;
import java.util.HashMap;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public abstract class UploaderForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	protected String nomeFileUpload;

    protected FormFile formFileUpload;

    protected FormFile filePrincipaleUpload;
    
    protected HashMap<String, FormFile> images = new HashMap<String, FormFile>();
    
	public void putImage(String key, FormFile value) {
    	this.images.put(key, value);
    }
    
    public FormFile getImage(String key) {
    	return images.get(key);
    }
  
    public Collection<FormFile> getImagesList() {
    	return images.values();
    }
    
    public void clearImages() {
    	this.images.clear();
    }
    public FormFile getFormFileUpload() {
        return formFileUpload;
    }

    public void setFormFileUpload(FormFile formFileUpload) {
        this.formFileUpload = formFileUpload;
    }

    public String getNomeFileUpload() {
        return nomeFileUpload;
    }

    public void setNomeFileUpload(String nomeFileUpload) {
        this.nomeFileUpload = nomeFileUpload;
    }

    public FormFile getFilePrincipaleUpload() {
        return filePrincipaleUpload;
    }

    public void setFilePrincipaleUpload(FormFile filePrincipaleUpload) {
        this.filePrincipaleUpload = filePrincipaleUpload;
    }
}
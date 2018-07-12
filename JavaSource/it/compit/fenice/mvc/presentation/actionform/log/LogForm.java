package it.compit.fenice.mvc.presentation.actionform.log;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.struts.action.ActionForm;

public class LogForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	private Map<String,File> logFile = new HashMap<String,File>();

	public Collection<File> getLogFile() {
		return logFile.values();
	}

	public Set<String> getLogKey() {
		return logFile.keySet();
	}
	
	public void resetLogFile() {
		this.logFile.clear();
	}
	
	public void setLogFile(Map<String,File> logFile) {
		this.logFile = logFile;
	}
	
	public void addLogFile(File f){
		this.logFile.put(f.getAbsolutePath(), f);
	}
	
	public void removeLogFile(String key){
		this.logFile.remove(key);
	}
}

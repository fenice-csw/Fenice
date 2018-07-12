package it.compit.fenice.mvc.presentation.actionform.log;

import it.finsiel.siged.mvc.vo.log.EventoVO;

import java.util.Collection;

import org.apache.struts.action.ActionForm;

public class JobScheduledLogForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	
	private Integer jsId;
	
	private String message;
	
	private Integer status;
	
	private int aooId;
	
	private String dataLog;
	
	private Collection<EventoVO> logs;

	
	public JobScheduledLogForm() {
		
	}
	
	public Collection<EventoVO> getLogs() {
		return logs;
	}

	public void setLogs(Collection<EventoVO> logs) {
		this.logs = logs;
	}

	public Integer getJsId() {
		return jsId;
	}
	
	public void setJsId(Integer jsId) {
		this.jsId = jsId;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public int getAooId() {
		return aooId;
	}
	
	public void setAooId(int aooId) {
		this.aooId = aooId;
	}
	
	public String getDataLog() {
		return dataLog;
	}
	
	public void setDataLog(String dataLog) {
		this.dataLog = dataLog;
	}
	
}

package it.finsiel.siged.mvc.presentation.helper;

import java.util.ArrayList;
import java.util.Collection;

public class SessioniEmergenzaView {

	public SessioniEmergenzaView() {
		protocolliPrenotati = new ArrayList<ReportProtocolloView>();
	}

	//private int protocolloId;
	
	private String dataProtocollo;

	private Collection<ReportProtocolloView> protocolliPrenotati;

	private String intervallo;

	//private int dimensione;
	
	
	
	public int getDimensione() {
		if(protocolliPrenotati==null)
		return 0;
		else
			return protocolliPrenotati.size();
	}

	public String getDataProtocollo() {
		return dataProtocollo;
	}

	public void setDataProtocollo(String dataProtocollo) {
		this.dataProtocollo = dataProtocollo;
	}

	public Collection<ReportProtocolloView> getProtocolliPrenotati() {
		return protocolliPrenotati;
	}

	public void setProtocolliPrenotati(Collection<ReportProtocolloView> protocolliPrenotati) {
		this.protocolliPrenotati = protocolliPrenotati;
	}

	public String getIntervallo() {
		return intervallo;
	}

	public void setIntervallo(String intervallo) {
		this.intervallo = intervallo;
	}

	public int getProtocolloId() {
		int min = 0;
		for (ReportProtocolloView r : getProtocolliPrenotati()) {
			if (min == 0) 
				min = r.getProtocolloId();
			else if (r.getProtocolloId() < min) 
				min = r.getProtocolloId();
		}
		return min;
	}

	

}

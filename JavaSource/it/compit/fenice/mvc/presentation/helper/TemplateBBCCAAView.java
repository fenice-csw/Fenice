package it.compit.fenice.mvc.presentation.helper;

import it.finsiel.siged.mvc.presentation.helper.DestinatarioView;

public class TemplateBBCCAAView {

	private String destinatario;

	private String testo;

	public TemplateBBCCAAView(DestinatarioView d) {
		this.destinatario = d.getDestinatario()
				+ (d.getIndirizzo() == null
						|| d.getIndirizzo().trim().equals("") ? "" : "\r\n"
						+ d.getIndirizzo())
				+ (d.getCapDestinatario() == null
						|| d.getCapDestinatario().trim().equals("") ? ""
						: "\r\n" + d.getCapDestinatario())
				+ (d.getCitta() == null || d.getCitta().trim().equals("") ? ""
						: "\r\n" + d.getCitta());
	}

	public TemplateBBCCAAView() {
		this.destinatario = " ";
	}
	
	public String getDestinatario() {
		return destinatario;
	}

	public String getTesto() {
		return testo;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

}

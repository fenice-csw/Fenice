package it.compit.fenice.ftpHelper;

import it.compit.fenice.mvc.presentation.helper.DocumentoAmmTrasparenteView;
import it.compit.fenice.mvc.presentation.helper.DocumentoRepertorioView;
import it.compit.fenice.mvc.vo.ammtrasparente.AmmTrasparenteVO;
import it.compit.fenice.mvc.vo.ammtrasparente.DocumentoAmmTrasparenteVO;
import it.compit.fenice.mvc.vo.repertori.DocumentoRepertorioVO;
import it.compit.fenice.mvc.vo.repertori.RepertorioVO;
import it.finsiel.siged.mvc.vo.organizzazione.AreaOrganizzativaVO;
import it.finsiel.siged.mvc.vo.protocollo.DocumentoVO;
import it.finsiel.siged.util.DateUtil;

import java.io.InputStream;
import java.util.Collection;

public class ScuoleHtmlUtility extends HtmlUtility{
	
	private static final String FILE_SCRIPT_DIR="/";
	
	public ScuoleHtmlUtility() {
	}

	public InputStream creaListaRepertoriHTML(Collection<RepertorioVO> list) {
		if (list != null) {
			
			String page = "<div class=\"Grid Grid--withGutter u-padding-all-l u-layout-centerContent\">"
					+ "<div class=\"Grid-cell u-size1of2 u-lg-size1of2\">"
					+ "<ul class=\"Linklist  u-layout-prose u-text-r-xs\">";
			for (RepertorioVO vo : list) {
				page += "<li><a class=\"Linklist-link Linklist-link\" href=\"" + vo.getRepertorioId()+ "/repertorio.html\">" + vo.getDescrizione()+ "</a></li>";
			}
			page += "</ul></div></div>";
			return parseStringToIS(getCharSetUtf8() + getCssLink("") + page);
		} else
			return null;
	}

	public InputStream creaDocumentoRepertorioHTML(DocumentoRepertorioVO vo, int flagPubblicazione) {
		if (vo != null) {
			String page = getJavascriptlink("../../");
			page += "<div class=\"doc-repertorio\"><h1 class=\"u-text-h1\">"+vo.getOggetto()+"</h1>";
			page += "<table lass=\"Table Table--striped Table--hover Table--withBorder\">";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft\">Documento Nr.:</th><td class=\"u-textLeft\">"+ vo.getNumeroDocumentoRepertorio()+ "</td></tr>";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft\">Pubblicato il:</th><td class=\"u-textLeft\">"+ DateUtil.formattaData(vo.getDataRepertorio().getTime()) + "</td></tr>";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft\">Pubblicabile dal:</th><td class=\"u-textLeft\">"+ DateUtil.formattaData(vo.getDataValiditaInizio().getTime())+ " al "+ DateUtil.formattaData(vo.getDataValiditaFine().getTime()) + "</td></tr>";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft\">Importo:</th><td class=\"u-textLeft\">"+ ((vo.getImporto()!=null && vo.getImporto().intValue()!=0)?vo.getImporto():"/") + "</td></tr>";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft\">Descrizione:</th><td class=\"u-textLeft\">"+ (vo.getDescrizione()!=null?vo.getDescrizione():"/") + "</td></tr>";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft field-label\" colspan=\"2\">Documenti da scaricare:</th></tr></table>";
			page += "<div class=\"field-items Grid Grid--withGutter u-padding-all-l\">"
					+ "<div class=\"Grid-cell u-size1of12 u-lg-size1of12\">&nbsp;</div>"
					+ "<div class=\"Grid-cell u-size1of3 u-lg-size1of3\">";
			if (vo.getDocumentiCollection().size() != 0) {
				page += "<ul class=\"Linklist  u-layout-prose u-text-r-xs\">";
				for (Object o : vo.getDocumentiCollection()) {
					DocumentoVO docVO = (DocumentoVO) o;
					if(docVO.getPubblicabile()){
						if (!docVO.getRiservato()){
							if(flagPubblicazione==AreaOrganizzativaVO.PUBBLICA_ORIGINALI){
								page += "<li><a class=\"Linklist-link Linklist-link\" href=\"" + docVO.getFileName()+ "\" target=\"_blank\">" + docVO.getFileName()+ "</a></li>";
							}else if(flagPubblicazione==AreaOrganizzativaVO.PUBBLICA_COPIA){
								if(!docVO.getFileName().contains(".p7m"))
									page += "<li><a class=\"Linklist-link Linklist-link\" href=\"" + docVO.getFileName()+ "\" target=\"_blank\">" + docVO.getFileName()+ "</a></li>";
								else
									page += "<li><a class=\"Linklist-link Linklist-link\" href=\"" + docVO.getFileName().replace(".p7m", "")+ "\" target=\"_blank\">" + docVO.getFileName().replace(".p7m", "")+ "(Copia comforme firmata digitalmente)</a></li>";
							}else if(flagPubblicazione==AreaOrganizzativaVO.PUBBLICA_ENTRAMBI){
								page += "<li><a class=\"Linklist-link Linklist-link\" href=\"" + docVO.getFileName()+ "\" target=\"_blank\">" + docVO.getFileName()+ "</a></li>";
								if(docVO.getFileName().contains(".p7m"))
									page += "<li><a class=\"Linklist-link Linklist-link\" href=\"" + docVO.getFileName().replace(".p7m", "")+ "\" target=\"_blank\">" + docVO.getFileName().replace(".p7m", "")+ "(Copia comforme firmata digitalmente)</a></li>";
							}
						} else
							page += "<li>" + docVO.getFileName()+ " <strong><sup>(1)</sup></strong></li>";
					}
				}
				page += "</ul></div></div>";
			}
			page +="<span class=\"reserved\"><strong><sup>(1)</sup></strong>Non visualizzabile ai sensi del DLGS 196/2003</span></div>";
			page +="<div style=\"float:left;\"><p><button id=\"indietro_action\" type=\"button\" class=\"Button Button--info u-text-r-xs\" onClick=\"javascript:window.location='../repertorio.html';\">INDIETRO</button></p></div>";
			return parseStringToIS(getCharSetUtf8() + getCssLink("../../") + page);
		} else
			return null;
	}

	public InputStream creaRepertorioHTML(RepertorioVO vo, int numeroDocumentoRepertorio, 
			Collection<DocumentoRepertorioView> docList) {
		if (vo != null) {
			String page = getJavascriptlink("../");
			page += "<h1 class=\"u-text-h1\">" + vo.getDescrizione() + "</h1>";
			page += "<table id=\"repertorio\" class=\"Table Table--striped Table--hover Table--withBorder\"><caption class=\"u-hiddenVisually\">maiores eius et</caption><thead>";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft\"></th><th class=\"u-textLeft\">Nr.</th><th class=\"u-textLeft\">Data Documento</th><th class=\"u-textLeft\">Oggetto</th><th class=\"u-textLeft\">Pubblicato il</th><th class=\"u-textLeft\">Scadenza</th><th class=\"u-textLeft\">Importo</th></tr></thead><tbody>";
			for (DocumentoRepertorioView doc : docList) {
				if(doc.getStato()==DocumentoRepertorioVO.PUBBLICATO || doc.getStato()==DocumentoRepertorioVO.PUBBLICATO_PROTOCOLLATO ||doc.getNumeroDocumentoRepertorio()==numeroDocumentoRepertorio){
					page += "<tr><td class=\"u-textLeft\">"+doc.getOrderedAnnoNumero()+"</td>";
					page += "<td class=\"u-textLeft\"><a href=\"" + doc.getDocRepertorioId()+ "/doc_repertorio.html\">"+ doc.getNumeroDocumentoRepertorio() + "</a></td>";
					if (doc.getDataDocumento() != null)
						page += "<td class=\"u-textLeft\">"+ DateUtil.formattaData(doc.getDataDocumento().getTime()) + "</td>";
					else
						page += "<td class=\"u-textLeft\"></td>";
					page += "<td class=\"u-textLeft\">" + doc.getOggetto() + "</td>";
					page += "<td class=\"u-textLeft\">"+ DateUtil.formattaData(doc.getDataValiditaInizio().getTime()) + "</td>";
					
					page += "<td class=\"u-textLeft\">"+ DateUtil.formattaData(doc.getDataValiditaFine().getTime()) + "</td>";
					page += "<td class=\"u-textLeft\">"+ ((doc.getImporto()!=null && doc.getImporto().intValue()!=0)?doc.getImportoEuro()+"":"")  + "</td></tr>";
				}
			}
			page += "</tbody></table>";
			page += "<div style=\"float:left;\"><p><button id=\"indietro_action\" type=\"button\" class=\"Button Button--info u-text-r-xs\" onClick=\"javascript:window.location='../lista_repertori.html';\">INDIETRO</button>";
			page += "<button id=\"export_action\" type=\"button\" class=\"Button Button--info u-text-r-xs export\">CSV</button></p></div>";
			return parseStringToIS(getCharSetUtf8() + getCssLink("../") + page);
		} else
			return null;
	}

	
	@Override
	protected String getCssLink(String prefix) {
		return "<link href=\""+prefix+FILE_SCRIPT_DIR+"style.css\" rel=\"stylesheet\" type=\"text/css\">" 
			 	+"<link href=\""+prefix+FILE_SCRIPT_DIR+"build.css\" rel=\"stylesheet\" type=\"text/css\">";
	}
	
	@Override
	protected String getJavascriptlink(String prefix) {
		return "<script type=\"text/javascript\" language=\"javascript\" src=\""+prefix+FILE_SCRIPT_DIR+"modernizr.js\"></script>"
				+"<script type=\"text/javascript\" language=\"javascript\" src=\""+prefix+FILE_SCRIPT_DIR+"jquery.min.js\"></script>"
				+"<script type=\"text/javascript\" language=\"javascript\" src=\""+prefix+FILE_SCRIPT_DIR+"IWT.min.js\"></script>";
	}
	
	public InputStream creaListaSezioniHTML(Collection<AmmTrasparenteVO> list) {
		if (list != null) {
			String page = "<div class=\"Grid Grid--withGutter u-padding-all-l u-layout-centerContent\">"
					+ "<div class=\"Grid-cell u-size1of2 u-lg-size1of2\">"
					+ "<ul class=\"Linklist  u-layout-prose u-text-r-xs\">";
			for (AmmTrasparenteVO vo : list) {
				page += "<li><a class=\"Linklist-link Linklist-link\" href=\"" + vo.getSezioneId()+ "/sezione.html\">" + vo.getDescrizione()+ "</a></li>";
			}
			page += "</ul></div></div>";
			return parseStringToIS(getCharSetUtf8() + getCssLink("") + page);
		} else
			return null;
	}

	public InputStream creaDocumentoAmmTrasparenteHTML(DocumentoAmmTrasparenteVO vo, int flagPubblicazione) {
		if (vo != null) {
			String page = getJavascriptlink("../../");
			page += "<div class=\"doc-sezione\"><h1 class=\"u-text-h1\">"+vo.getOggetto()+"</h1>";
			page += "<table lass=\"Table Table--striped Table--hover Table--withBorder\">";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft\">Documento Nr.:</th><td class=\"u-textLeft\">"+ vo.getNumeroDocumentoSezione()+ "</td></tr>";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft\">Pubblicato il:</th><td class=\"u-textLeft\">"+ DateUtil.formattaData(vo.getDataSezione().getTime()) + "</td></tr>";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft\">Pubblicabile dal:</th><td class=\"u-textLeft\">"+ DateUtil.formattaData(vo.getDataValiditaInizio().getTime())+ " al "+ DateUtil.formattaData(vo.getDataValiditaFine().getTime()) + "</td></tr>";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft\">Importo:</th><td class=\"u-textLeft\">"+ ((vo.getImporto()!=null && vo.getImporto().intValue()!=0)?vo.getImporto():"/") + "</td></tr>";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft\">Descrizione:</th><td class=\"u-textLeft\">"+ (vo.getDescrizione()!=null?vo.getDescrizione():"/") + "</td></tr>";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft field-label\" colspan=\"2\">Documenti da scaricare:</th></tr></table>";
			page += "<div class=\"field-items Grid Grid--withGutter u-padding-all-l\">"
					+ "<div class=\"Grid-cell u-size1of12 u-lg-size1of12\">&nbsp;</div>"
					+ "<div class=\"Grid-cell u-size1of3 u-lg-size1of3\">";
			if (vo.getDocumentiCollection().size() != 0) {
				page += "<ul class=\"Linklist  u-layout-prose u-text-r-xs\">";
				for (Object o : vo.getDocumentiCollection()) {
					DocumentoVO docVO = (DocumentoVO) o;
					if(docVO.getPubblicabile()){
						if (!docVO.getRiservato()){
							if(flagPubblicazione==AreaOrganizzativaVO.PUBBLICA_ORIGINALI){
								page += "<li><a class=\"Linklist-link Linklist-link\" href=\"" + docVO.getFileName()+ "\" target=\"_blank\">" + docVO.getFileName()+ "</a></li>";
							}else if(flagPubblicazione==AreaOrganizzativaVO.PUBBLICA_COPIA){
								if(!docVO.getFileName().contains(".p7m"))
									page += "<li><a class=\"Linklist-link Linklist-link\" href=\"" + docVO.getFileName()+ "\" target=\"_blank\">" + docVO.getFileName()+ "</a></li>";
								else
									page += "<li><a class=\"Linklist-link Linklist-link\" href=\"" + docVO.getFileName().replace(".p7m", "")+ "\" target=\"_blank\">" + docVO.getFileName().replace(".p7m", "")+ "(Copia comforme firmata digitalmente)</a></li>";
							}else if(flagPubblicazione==AreaOrganizzativaVO.PUBBLICA_ENTRAMBI){
								page += "<li><a class=\"Linklist-link Linklist-link\" href=\"" + docVO.getFileName()+ "\" target=\"_blank\">" + docVO.getFileName()+ "</a></li>";
								if(docVO.getFileName().contains(".p7m"))
									page += "<li><a class=\"Linklist-link Linklist-link\" href=\"" + docVO.getFileName().replace(".p7m", "")+ "\" target=\"_blank\">" + docVO.getFileName().replace(".p7m", "")+ "(Copia comforme firmata digitalmente)</a></li>";
							}
						} else
							page += "<li>" + docVO.getFileName()+ " <strong><sup>(1)</sup></strong></li>";
					}
				}
				page += "</ul></div></div>";
			}
			page +="<span class=\"reserved\"><strong><sup>(1)</sup></strong>Non visualizzabile ai sensi del DLGS 196/2003</span></div>";
			page +="<div style=\"float:left;\"><p><button id=\"indietro_action\" type=\"button\" class=\"Button Button--info u-text-r-xs\" onClick=\"javascript:window.location='../sezione.html';\">INDIETRO</button></p></div>";
			return parseStringToIS(getCharSetUtf8() + getCssLink("../../") + page);
		} else
			return null;
	}

	public InputStream creaAmmTrasparenteHTML(AmmTrasparenteVO vo, int numeroDocumentoAmmTrasparente, Collection<DocumentoAmmTrasparenteView> docList) {
		if (vo != null) {
			String page = getJavascriptlink("../");
			page += "<h1 class=\"u-text-h1\">" + vo.getDescrizione() + "</h1>";
			page += "<table id=\"sezione\" class=\"Table Table--striped Table--hover Table--withBorder\"><caption class=\"u-hiddenVisually\">maiores eius et</caption><thead>";
			page += "<tr class=\"u-border-bottom-xs\"><th class=\"u-textLeft\"></th><th class=\"u-textLeft\">Nr.</th><th class=\"u-textLeft\">Data Documento</th><th class=\"u-textLeft\">Oggetto</th><th class=\"u-textLeft\">Pubblicato il</th><th class=\"u-textLeft\">Scadenza</th><th class=\"u-textLeft\">Importo</th></tr></thead><tbody>";
			for (DocumentoAmmTrasparenteView doc : docList) {
				if(doc.getStato()==DocumentoRepertorioVO.PUBBLICATO || doc.getStato()==DocumentoRepertorioVO.PUBBLICATO_PROTOCOLLATO ||doc.getNumeroDocumentoSezione()==numeroDocumentoAmmTrasparente){
					page += "<tr><td class=\"u-textLeft\">"+doc.getOrderedAnnoNumero()+"</td>";
					page += "<td class=\"u-textLeft\"><a href=\"" + doc.getDocSezioneId()+ "/doc_sezione.html\">"+ doc.getNumeroDocumentoSezione() + "</a></td>";
					if (doc.getDataDocumento() != null)
						page += "<td class=\"u-textLeft\">"+ DateUtil.formattaData(doc.getDataDocumento().getTime()) + "</td>";
					else
						page += "<td class=\"u-textLeft\"></td>";
					page += "<td class=\"u-textLeft\">" + doc.getOggetto() + "</td>";
					page += "<td class=\"u-textLeft\">"+ DateUtil.formattaData(doc.getDataValiditaInizio().getTime()) + "</td>";
					
					page += "<td class=\"u-textLeft\">"+ DateUtil.formattaData(doc.getDataValiditaFine().getTime()) + "</td>";
					page += "<td class=\"u-textLeft\">"+ ((doc.getImporto()!=null && doc.getImporto().intValue()!=0)?doc.getImportoEuro()+"":"")  + "</td></tr>";
				}
			}
			page += "</tbody></table>";
			page += "<div style=\"float:left;\"><p><button id=\"indietro_action\" type=\"button\" class=\"Button Button--info u-text-r-xs\" onClick=\"javascript:window.location='../lista_sezioni.html';\">INDIETRO</button>";
			page += "<button id=\"export_action\" type=\"button\" class=\"Button Button--info u-text-r-xs export\">CSV</button></p></div>";
			return parseStringToIS(getCharSetUtf8() + getCssLink("../") + page);
		} else
			return null;
	}
}


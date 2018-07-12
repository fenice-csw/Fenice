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

public class ErsuCataniaHtmlUtility extends HtmlUtility{
	
	public ErsuCataniaHtmlUtility() {
	}

	public InputStream creaListaRepertoriHTML(Collection<RepertorioVO> list) {
		if (list != null) {
			
			String page = "<div class=\"container\"><ul class=\"list-group\">";
			for (RepertorioVO vo : list) {
				page += "<li class=\"list-group-item\"><h4><a href=\"" + vo.getRepertorioId()+ "/repertorio.html\">" + vo.getDescrizione()+ "</a></h4></li>";
			}
			page += "</ul></div>";
			return parseStringToIS(getCharSetUtf8() + getCssLink("") + page);
		} else
			return null;
	}

	public InputStream creaDocumentoRepertorioHTML(DocumentoRepertorioVO vo, int flagPubblicazione) {
		if (vo != null) {
			String page = getJavascriptlink("../../");
			page += "<div class=\"container\"><div class=\"doc-repertorio\"><h1>"+vo.getOggetto()+"</h1>";
			String riservato = "<span class=\"reserved\"><strong><sup>(1)</sup></strong>Non visualizzabile ai sensi del DLGS 196/2003</span>";
			page += "<p><strong>Documento Nr. </strong>"+ vo.getNumeroDocumentoRepertorio()+ " <strong>pubblicato il</strong> "+ DateUtil.formattaData(vo.getDataRepertorio().getTime()) + "</p>";
			page += "<p><strong>Pubblicabile dal</strong> "+ DateUtil.formattaData(vo.getDataValiditaInizio().getTime())+ " <strong>al</strong> "+ DateUtil.formattaData(vo.getDataValiditaFine().getTime()) + "</p>";
			page += "<p><strong>Importo: </strong>"+ ((vo.getImporto()!=null && vo.getImporto().intValue()!=0)?vo.getImporto():"/") + "</p>";
			page += "<p><strong>Descrizione: </strong> "+ (vo.getDescrizione()!=null?vo.getDescrizione():"/") + "</p>";
			page += "<div class =\"field-label\">Documenti da scaricare</div><div class =\"field-items\">";
			
			if (vo.getDocumentiCollection().size() != 0) {
				page += "<ul>";
				for (Object o : vo.getDocumentiCollection()) {
					DocumentoVO docVO = (DocumentoVO) o;
					if(docVO.getPubblicabile()){
						if (!docVO.getRiservato()){
							if(flagPubblicazione==AreaOrganizzativaVO.PUBBLICA_ORIGINALI){
								page += "<li><a href=\"" + docVO.getFileName()+ "\" target=\"_blank\">" + docVO.getFileName()+ "</a>";
							}else if(flagPubblicazione==AreaOrganizzativaVO.PUBBLICA_COPIA){
								if(!docVO.getFileName().contains(".p7m"))
									page += "<li><a href=\"" + docVO.getFileName()+ "\" target=\"_blank\">" + docVO.getFileName()+ "</a>";
								else
									page += "<li><a href=\"" + docVO.getFileName().replace(".p7m", "")+ "\" target=\"_blank\">" + docVO.getFileName().replace(".p7m", "")+ "(Copia comforme firmata digitalmente)</a>";
							}else if(flagPubblicazione==AreaOrganizzativaVO.PUBBLICA_ENTRAMBI){
								page += "<li><a href=\"" + docVO.getFileName()+ "\" target=\"_blank\">" + docVO.getFileName()+ "</a>";
								if(docVO.getFileName().contains(".p7m"))
									page += "<li><a href=\"" + docVO.getFileName().replace(".p7m", "")+ "\" target=\"_blank\">" + docVO.getFileName().replace(".p7m", "")+ "(Copia comforme firmata digitalmente)</a>";
							}
						} else
							page += "<li>" + docVO.getFileName()+ " <strong><sup>(1)</sup></strong>";
					}
				}
				page += "</ul>";
			}
			
			page += "</div>"+riservato+"</div><p><a class=\"btn btn-default pull-left\" href=\"../repertorio.html\" id=\"indietro_action\" data-toggle=\"tooltip\" data-original-title=\"Torna alla lista dei Documenti\"><span class=\"glyphicon glyphicon-arrow-left\"></span> INDIETRO</a></p></div>";
			return parseStringToIS(getCharSetUtf8() + getCssLink("../../") + page);
		} else
			return null;
	}

	public InputStream creaRepertorioHTML(RepertorioVO vo, int numeroDocumentoRepertorio, 
			Collection<DocumentoRepertorioView> docList) {
		if (vo != null) {
			String page = getJavascriptlink("../");
			page += " <div id=\"no-more-tables\" class=\"container\">";
			page += "<h1>" + vo.getDescrizione() + "</h1>";
			page += "<table id=\"repertorio\" class=\"table table-bordered\"><thead>";
			page += "<tr><th class=\"hidden\"></th><th>Nr.</th><th>Data Documento</th><th>Oggetto</th><th>Pubblicato il</th><th>Scadenza</th><th>Importo</th></tr></thead><tbody>";
			for (DocumentoRepertorioView doc : docList) {
				if(doc.getStato()==DocumentoRepertorioVO.PUBBLICATO || doc.getStato()==DocumentoRepertorioVO.PUBBLICATO_PROTOCOLLATO ||doc.getNumeroDocumentoRepertorio()==numeroDocumentoRepertorio){
					page += "<tr><td class=\"hidden\">"+doc.getOrderedAnnoNumero()+"</td>";
					page += "<td><a href=\"" + doc.getDocRepertorioId()+ "/doc_repertorio.html\">"+ doc.getNumeroDocumentoRepertorio() + "</a></td>";
					if (doc.getDataDocumento() != null)
						page += "<td>"+ DateUtil.formattaData(doc.getDataDocumento().getTime()) + "</td>";
					else
						page += "<td></td>";
					page += "<td>" + doc.getOggetto() + "</td>";
					page += "<td>"+ DateUtil.formattaData(doc.getDataValiditaInizio().getTime()) + "</td>";
					
					page += "<td>"+ DateUtil.formattaData(doc.getDataValiditaFine().getTime()) + "</td>";
					page += "<td>"+ ((doc.getImporto()!=null && doc.getImporto().intValue()!=0)?doc.getImportoEuro()+"":"")  + "</td></tr>";
				}
			}
			page += "</tbody></table>";
			page +="<div class=\"pull-left\"><p><a href=\"../lista_repertori.html\" id=\"indietro_action\" class=\"btn btn-default\" data-toggle=\"tooltip\" data-original-title=\"Torna alla Home\">"
					+"<span class=\"glyphicon glyphicon-arrow-left\"></span>INDIETRO</a></p><p><a href=\"#\" id=\"export_action\" class=\"btn btn-default export\" data-toggle=\"tooltip\" data-original-title=\"Scarica in Open Data\">"
					+"<span class=\"glyphicon glyphicon-download\"></span>CSV</a></p><div>";
			return parseStringToIS(getCharSetUtf8() + getCssLink("../") + page);
		} else
			return null;
	}

	@Override
	public InputStream creaAmmTrasparenteHTML(AmmTrasparenteVO ammTraspVO,
			int numeroDocumentoAmmTrasparente,
			Collection<DocumentoAmmTrasparenteView> listDoc) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public InputStream creaDocumentoAmmTrasparenteHTML(
			DocumentoAmmTrasparenteVO vo, int flagPubblicazione) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public InputStream creaListaSezioniHTML(
			Collection<AmmTrasparenteVO> listSezioni) {
		// TODO Auto-generated method stub
		return null;
	}
}


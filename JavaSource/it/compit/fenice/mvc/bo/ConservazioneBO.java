package it.compit.fenice.mvc.bo;

import it.compit.fenice.enums.TagConservazioneEnum;
import it.compit.fenice.util.ZipUtil;
import it.finsiel.siged.constant.FileConstants;
import it.finsiel.siged.model.organizzazione.Organizzazione;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.ReportProtocolloDelegate;
import it.finsiel.siged.mvc.presentation.helper.ReportProtocolloView;
import it.finsiel.siged.report.protocollo.CommonReportDS;
import it.finsiel.siged.util.DateUtil;
import it.finsiel.siged.util.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class ConservazioneBO {
	
	private static final String REGISTRO_GIORNALIERO_TITLE="Registro Giornaliero";
	private static final String REGISTRO_GIORNALIERO_MOD_TITLE="Registro Giornaliero Modifiche/Annullate";
	
	TreeMap<TagConservazioneEnum,String> metadati;

	public ConservazioneBO() {
		this.metadati =new TreeMap<TagConservazioneEnum, String>();
	}
	
	public TreeMap<TagConservazioneEnum, String> getMetadati() {
		return metadati;
	}

	
	public static File getFileIndice(File directory){
		for (File file : directory.listFiles()) {
		    if (file.getName().endsWith((".csv"))) {
		    	return file;
		    }
		  }
		return null;
	};
	
	
	
	public byte[] getReportAndZip(Date dataReg, String tipoProtocollo, int totalReg, int totalMod, String realPath, Utente utente, File directory) {
		JasperPrint jasperPrint=null;
		JasperPrint jasperPrintMod=null;
		List<String> fileNames=new ArrayList<String>();
		try {
			File reportFile = new File(
					realPath + FileConstants.STAMPA_CONSERVAZIONE_REGISTRO_GIORNALIERO_TEMPLATE);
			File reportFileMod = new File(
					realPath + FileConstants.STAMPA_CONSERVAZIONE_REGISTRO_MODIFICHE_GIORNALIERO_TEMPLATE);

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ReportTitle", utente.getAreaOrganizzativa().getDescription());
			parameters.put("DataReg", DateUtil.formattaData(dataReg.getTime()));
			metadati=getCommonMetadati(utente, Organizzazione.getInstance(), dataReg,dataReg);
			File indice=null;
			
			if(totalReg!=0){
				parameters.put("BaseDir", reportFile.getParentFile());
				parameters.put("ReportSubTitle", REGISTRO_GIORNALIERO_TITLE);
				JasperDesign jasperDesign = JRXmlLoader
					.load(realPath + FileConstants.STAMPA_CONSERVAZIONE_REGISTRO_GIORNALIERO_TEMPLATE);
				JasperReport jasperReport = JasperCompileManager
					.compileReport(jasperDesign);

				Collection<ReportProtocolloView> c = ReportProtocolloDelegate
						.getInstance().stampaRegistro(utente, tipoProtocollo,
								dataReg, dataReg, 0);

				CommonReportDS ds = new CommonReportDS(c);
				jasperPrint = JasperFillManager.fillReport(
						jasperReport, parameters, ds);
				long uniqueNumberReg=System.currentTimeMillis();
				File fileReg=new File(directory, "RegistroGiornaliero"+metadati.get(TagConservazioneEnum.CODICEENTE)+uniqueNumberReg+".pdf");
				JasperExportManager.exportReportToPdfFile(jasperPrint, fileReg.getAbsolutePath());
				ConservazioneBO.getCompleteRegistroGiornalieroMetadati(metadati, uniqueNumberReg, "REGISTRO GIORNALIERO", fileReg, "Registro giornaliero di protocollo", c,c.size(),0);
 				indice=new File(directory, metadati.get(TagConservazioneEnum.NUMERO)+".csv");
				ConservazioneBO.writeCSV(metadati,indice, true);
				fileNames.add(fileReg.getName());
			}
			
			if(totalMod!=0){
				parameters.put("BaseDir", reportFileMod.getParentFile());
				parameters.put("ReportSubTitle",REGISTRO_GIORNALIERO_MOD_TITLE);
				JasperDesign jasperDesignMod = JRXmlLoader
					.load(realPath + FileConstants.STAMPA_CONSERVAZIONE_REGISTRO_MODIFICHE_GIORNALIERO_TEMPLATE);
				JasperReport jasperReportMod = JasperCompileManager
					.compileReport(jasperDesignMod);
				
				Collection<ReportProtocolloView> cMod = ReportProtocolloDelegate
						.getInstance().getProtocolliModificatiReport(utente,
								tipoProtocollo, dataReg, dataReg, 0);
				
				CommonReportDS ds = new CommonReportDS(cMod);
				jasperPrintMod = JasperFillManager.fillReport(
						jasperReportMod, parameters, ds);
				
				long uniqueNumberMod=System.currentTimeMillis();
				File filemod=new File(directory, "RegistroModifiche"+metadati.get(TagConservazioneEnum.CODICEENTE)+uniqueNumberMod+".pdf");
				JasperExportManager.exportReportToPdfFile(jasperPrintMod, filemod.getAbsolutePath());
				
				int totalAnn=ReportProtocolloDelegate.getInstance().countProtocolliAnnullatiReport(utente,
						tipoProtocollo, dataReg,dataReg, 0);
				ConservazioneBO.getCompleteRegistroGiornalieroMetadati(metadati, uniqueNumberMod, "REGISTRO MODIFICHE", filemod, "Registro delle modifiche", cMod,0,totalAnn);
				ConservazioneBO.writeCSV(metadati,indice, false);
				fileNames.add(filemod.getName());
			}
			
			return ZipUtil.zipFiles(directory, fileNames);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static TreeMap<TagConservazioneEnum, String> getCommonMetadati(Utente utente, Organizzazione org,Date dataInizio,Date dataFine){
		TreeMap<TagConservazioneEnum,String> m=new TreeMap<TagConservazioneEnum, String>();
		m.put(TagConservazioneEnum.CONSERVATORE, "Gestione Archivi S.r.l.");
		m.put(TagConservazioneEnum.ENTE, org.getValueObject().getDescription());
		m.put(TagConservazioneEnum.CODICEENTE, org.getValueObject().getCodice());
		m.put(TagConservazioneEnum.STRUTTURA, utente.getAreaOrganizzativa().getDescription());
		m.put(TagConservazioneEnum.USERID, org.getValueObject().getCodice());
		m.put(TagConservazioneEnum.ANNO, String.valueOf(DateUtil.getAnnoCorrente()));
		m.put(TagConservazioneEnum.CODIFICAHASH, FileConstants.SHA256);
		m.put(TagConservazioneEnum.CODICETIPOREGISTRO, utente.getRegistroUfficiale().getCodRegistro());
		m.put(TagConservazioneEnum.DATA, DateUtil.formattaData(System.currentTimeMillis()));
		m.put(TagConservazioneEnum.DATAINIZIOREGISTRAZIONI, DateUtil.formattaData(dataInizio.getTime()));
		m.put(TagConservazioneEnum.DATAFINEREGISTRAZIONI, DateUtil.formattaData(dataFine.getTime()));
		m.put(TagConservazioneEnum.ORIGINATORE, org.getUfficio(org.getCaricaResponsabileUfficioProtocollo().getUfficioId()).getValueObject().getDescription());
		m.put(TagConservazioneEnum.RESPONSABILE, org.getUtente(org.getCaricaResponsabileUfficioProtocollo().getUtenteId()).getValueObject().getCognomeNome());
		m.put(TagConservazioneEnum.OPERATORE, utente.getValueObject().getCognomeNome());
		m.put(TagConservazioneEnum.DENOMINAZIONEAPPLICATIVO, "Fenice");
		m.put(TagConservazioneEnum.VERSIONEAPPLICATIVO, org.getValueObject().getVersioneFenice());
		m.put(TagConservazioneEnum.PRODUTTOREAPPLICATIVO, "Cswservizi S.r.l.s");
		m.put(TagConservazioneEnum.TEMPOCONSERVAZIONE, "ILLIMITATO");
		return m;
	}
	
	public static void getCompleteRegistroGiornalieroMetadati(TreeMap<TagConservazioneEnum, String> m, long uniqueNumber,String tipologiaUnitaDocumentaria,
			File file, String tipoDocumento, Collection<ReportProtocolloView> protocolli,int protoReg, int protoAnn) {
		List<ReportProtocolloView> list = new ArrayList<ReportProtocolloView>(protocolli);
		m.put(TagConservazioneEnum.NOMEFILE, file.getName());
		m.put(TagConservazioneEnum.NUMERO, String.valueOf(uniqueNumber));
		m.put(TagConservazioneEnum.TIPOLOGIAUNITADOCUMENTARIA, tipologiaUnitaDocumentaria);
		m.put(TagConservazioneEnum.HASH, FileUtil.calcolaDigest(file.getAbsolutePath(),FileConstants.SHA256));
		m.put(TagConservazioneEnum.TIPODOCUMENTO, tipoDocumento);
		m.put(TagConservazioneEnum.OGGETTO, tipoDocumento+" del "+m.get(TagConservazioneEnum.DATAINIZIOREGISTRAZIONI));
		m.put(TagConservazioneEnum.NUMEROINIZIALE, String.valueOf(list.get(0).getNumeroProtocollo()));
		m.put(TagConservazioneEnum.NUMEROFINALE, String.valueOf(list.get(list.size()-1).getNumeroProtocollo()));
		m.put(TagConservazioneEnum.NUMERODOCUMENTIREGISTRATI, String.valueOf(protoReg));
		m.put(TagConservazioneEnum.NUMERODOCUMENTIANNULLATI, String.valueOf(protoAnn));
	}  

	
	public static void writeCSV(TreeMap<TagConservazioneEnum, String> m,File file, boolean empty){
		FileWriter writer;
		try {
			if(empty)
				writer = new FileWriter(file);
			else{
				writer = new FileWriter(file,true);
				writer.append("\n");
			}
			for(TagConservazioneEnum key:m.keySet()){
				writer.append(m.get(key));
				if(key!=TagConservazioneEnum.TEMPOCONSERVAZIONE)
					writer.append('|');
			}
			writer.flush();
		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

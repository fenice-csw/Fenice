package it.compit.fenice.util;

import it.compit.fenice.mvc.presentation.actionform.documentale.EditorForm;
import it.compit.fenice.mvc.presentation.actionform.protocollo.NotificaEsitoCommittenteForm;
import it.finsiel.siged.model.organizzazione.Utente;
import it.finsiel.siged.mvc.business.ProtocolloDelegate;
import it.finsiel.siged.util.DateUtil;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

public class VelocityTemplateUtils{

	private static VelocityEngine velocityEngine;
	
	static Logger logger = Logger.getLogger(VelocityTemplateUtils.class.getName());
	
	private static void initVelocityEngine(){
		Properties p=new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine = new VelocityEngine(p);
		velocityEngine.init();

	}
	
	public static String createBBCCAADocument(EditorForm form,Utente utente,String contextPath) {
		int numProt = ProtocolloDelegate.getInstance().getUltimoProtocollo(DateUtil.getAnnoCorrente(), utente.getRegistroPostaInterna());
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("ctxPath", contextPath);
		model.put("editor", form);
		model.put("utente", utente);
		model.put("ufficio", utente.getUfficioVOInUso());
		
		if (form.getTipo().equals("IN")) {
			model.put("prot", String.valueOf(numProt+1));
			model.put("data", DateUtil.formattaData(new Date().getTime()));
		}else{
			model.put("prot", "___________");
			model.put("data", "___________");
		}
		if (form.getAllaccio().getProtocolloAllacciatoId() != 0) {
			model.put("protAllaccio", form.getAllaccio().getNumeroProtocollo());
			model.put("dataAllaccio", DateUtil.formattaData(form.getAllaccio().getDataRegistrazione().getTime()));
		}else{
			model.put("protAllaccio", "___________");
			model.put("dataAllaccio", "___________");
		}
		initVelocityEngine();
		return mergeTemplateIntoString(velocityEngine, "velocity/BBCCAATemplateBody.vm", model);
	}

	public static String createULLDocument(String testo,Utente utente,String contextPath) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("ctxPath", contextPath);
		model.put("testo", testo);
		initVelocityEngine();
		return mergeTemplateIntoString(velocityEngine, "velocity/ULLTemplateBody.vm", model);
	}
	
	public static String createNotificaEsitoCommittente(NotificaEsitoCommittenteForm form) {
		try{
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("IdentificativoSDI", form.getIdentificativoSDI());
			model.put("fattura", form.getNumeroFattura());
			model.put("esito", form.getEsito());
			model.put("descrizione", form.getDescrizione());
			//model.put("messageIdCommittente", form.getIdCommittente());
			model.put("anno", form.getAnno());
			initVelocityEngine();
			return mergeTemplateIntoString(velocityEngine, "velocity/notificaEsitoCommittente.vm", model);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String mergeTemplateIntoString(
			VelocityEngine velocityEngine, String templateLocation, Map<String, Object> model)
			throws VelocityException {
		StringWriter result = new StringWriter();
		mergeTemplate(velocityEngine, templateLocation, model, result);
		return result.toString();
	}

	
	public static void mergeTemplate(
			VelocityEngine velocityEngine, String templateLocation, Map<String, Object> model, Writer writer)
			throws VelocityException {
			
		try {
			VelocityContext velocityContext = new VelocityContext(model);
			velocityEngine.mergeTemplate(templateLocation, "UTF-8", velocityContext, writer);
		}
		catch (VelocityException ex) {
			ex.printStackTrace();
			throw ex;
			
		}
		catch (RuntimeException ex) {
			ex.printStackTrace();
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Why does VelocityEngine throw a generic checked exception, after all?", ex);
			throw new VelocityException(ex.toString());
		}
	}

}

package it.compit.fenice.mvc.presentation.action.log;

import it.compit.fenice.mvc.presentation.actionform.log.LogForm;
import it.compit.fenice.mvc.presentation.filter.FileNameFilter;
import it.finsiel.siged.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public final class LogAction extends Action {
	
	static Logger logger = Logger.getLogger(LogAction.class.getName());

	public ActionForward downloadDocumento(ActionMapping mapping,
			String pathFile, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		InputStream is = null;
		OutputStream os = null;
		try {
			File f = new File(pathFile);
			os = response.getOutputStream();
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ f.getName());
			response.setHeader("Cache-control", "");
			is = new FileInputStream(pathFile);
			FileUtil.writeFile(is, os);
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			FileUtil.closeIS(is);
			FileUtil.closeOS(os);
		}
		return null;
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionMessages errors = new ActionMessages();
		LogForm lForm = (LogForm) form;
		String type = request.getParameter("type");
		if (type != null) {
			String cartella = System.getProperty("jboss.home.url").substring(5)
					+ "bin";
			lForm.resetLogFile();
			File dir = new File(cartella);
			File[] files = null;
			if (type.equals("debug"))
				files = dir.listFiles(new FileNameFilter("debug", "*"));
			else if (type.equals("error"))
				files = dir.listFiles(new FileNameFilter("error", "*"));
			else if (type.equals("console"))
				files = dir.listFiles(new FileNameFilter("nohup", "out"));
			for (File f : files)
				if (!f.isDirectory())
					lForm.addLogFile(f);
			if (lForm.getLogFile().size() == 0) {
				errors.add("lo_log_file", new ActionMessage("no.log.file",
						type, ""));
				saveErrors(request, errors);
				return mapping.findForward("input");
			}
			return mapping.findForward("lista");
		} else if (request.getParameter("logSelezionato") != null) {
			String path = request.getParameter("logSelezionato");
			downloadDocumento(mapping, path, request, response);
		} else if (request.getParameter("cancellaLogSelezionato") != null) {
			String path = request.getParameter("cancellaLogSelezionato");
			File f = new File(path);
			if (f.isFile()) {
				if (f.delete()) {
					lForm.removeLogFile(path);
				} else {
					errors.add("operazione_fallita", new ActionMessage(
							"operazione_fallita", "", ""));
					saveErrors(request, errors);
				}
			}
			return mapping.findForward("lista");
		} else if (request.getParameter("cancellaTutti") != null) {
			Set<String> keySet=lForm.getLogKey();
			Object[] objs = keySet.toArray();
			for(Object o:objs){
				String path=(String) o;
				File f = new File(path);
				if (f.isFile()) {
					if (f.delete()) 
						lForm.removeLogFile(path);
				}
			}
			if(lForm.getLogFile().size()!=0){
				errors.add("operazione_fallita", new ActionMessage("operazione_fallita", "", ""));
				saveErrors(request, errors);
				return mapping.findForward("lista");
			}
			return mapping.findForward("input");
			
		}

		else if (request.getParameter("indietro") != null) {
			lForm.resetLogFile();
			return mapping.findForward("input");
		}
		return mapping.findForward("input");
	}

}
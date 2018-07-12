package it.compit.fenice.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ScannerJnlpGeneratorServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
    public ScannerJnlpGeneratorServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File f = new File("/home/br4wn/eprot/eprotDyna/WebContent/scanagent/launch.jnlp");
		String jsess_id = request.getParameter("jsess_id");
		String url = request.getParameter("url");
		String nprot ="--";
		if(request.getParameter("nprot")!=null){
			nprot = request.getParameter("nprot");
		}
				
		SAXReader reader = new SAXReader();
        Document document;
		try {
			document = reader.read(new File(System.getProperty("jboss.home.dir")+"/jnlp/launch.jnlp"));
			
			Element jnlp = (Element) document.selectSingleNode( "//jnlp");
			
			Element appDesc = (Element) document.selectSingleNode( "//jnlp/application-desc");
			
			String requestURL = ""+request.getRequestURL();
			jnlp.addAttribute("codebase", requestURL.substring(0,requestURL.lastIndexOf("/")+1));
			jnlp.addAttribute("href", requestURL.substring(requestURL.lastIndexOf("/")+1)+"?"+StringEscapeUtils.unescapeHtml(request.getQueryString()));
			appDesc.addElement("argument").addText(jsess_id);
	        appDesc.addElement("argument").addText(url);
	        appDesc.addElement("argument").addText(nprot);
	        String xml = document.asXML();
	        response.setContentType( "application/x-java-jnlp-file" );
	        response.setHeader( "Content-Disposition", "attachment; filename=\"" + f.getName() + "\"" );
	        response.getWriter().write(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
		}    
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}

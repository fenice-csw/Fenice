package it.compit.fenice.ftpHelper;

import it.compit.fenice.mvc.presentation.helper.DocumentoAmmTrasparenteView;
import it.compit.fenice.mvc.presentation.helper.DocumentoRepertorioView;
import it.compit.fenice.mvc.vo.ammtrasparente.AmmTrasparenteVO;
import it.compit.fenice.mvc.vo.ammtrasparente.DocumentoAmmTrasparenteVO;
import it.compit.fenice.mvc.vo.repertori.DocumentoRepertorioVO;
import it.compit.fenice.mvc.vo.repertori.RepertorioVO;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;

public abstract class HtmlUtility {

	
	public HtmlUtility() {
	}
	
	protected String getCharSetUtf8() {
		return "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">";
	}

	
	protected String getCssLink(String prefix) {
		return "<link href=\""+prefix+"css/style.css\" rel=\"stylesheet\" type=\"text/css\">" 
		 	+"<link href=\""+prefix+"bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\" type=\"text/css\">" 
		 	+"<link href=\""+prefix+"bootstrap/bootstrap-theme.min.css\" rel=\"stylesheet\" type=\"text/css\">"
		 	+"<link href=\""+prefix+"css/dataTables.bootstrap.css\" rel=\"stylesheet\" type=\"text/css\">";
	}
	
	
	protected String getJavascriptlink(String prefix) {
		return "<script type=\"text/javascript\" language=\"javascript\" src=\""+prefix+"js/jquery.js\"></script>"
		+"<script type=\"text/javascript\" language=\"javascript\" src=\""+prefix+"js/jquery-ui.js\"></script>"
		+"<script type=\"text/javascript\" language=\"javascript\" src=\""+prefix+"js/jquery.dataTables.js\"></script>"
		+"<script type=\"text/javascript\" language=\"javascript\" src=\""+prefix+"js/dataTables.bootstrap.js\"></script>"
		+"<script type=\"text/javascript\" language=\"javascript\"src=\""+prefix+"/bootstrap/js/bootstrap.min.js\"></script>"
		+"<script type=\"text/javascript\" language=\"javascript\" src=\""+prefix+"js/init.js\"></script>";
		
					
	}

	public InputStream parseStringToIS(String str) {
		if (str == null)
			return null;
		str = str.trim();
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(str.getBytes("UTF-8"));
		} catch (Exception e) {
		}
		return in;
	}

	public abstract InputStream creaListaRepertoriHTML(Collection<RepertorioVO> listRepertori);

	public abstract InputStream creaRepertorioHTML(RepertorioVO repVO, int numeroDocumentoRepertorio, Collection<DocumentoRepertorioView> listDoc);
	
	public abstract InputStream creaDocumentoRepertorioHTML(DocumentoRepertorioVO vo, int flagPubblicazione);
	
	// AMMIISTRAZIONE TRASPARENTE
	public abstract InputStream creaListaSezioniHTML(Collection<AmmTrasparenteVO> listSezioni);

	public abstract InputStream creaAmmTrasparenteHTML(AmmTrasparenteVO ammTraspVO, int numeroDocumentoAmmTrasparente, Collection<DocumentoAmmTrasparenteView> listDoc);

	public abstract InputStream creaDocumentoAmmTrasparenteHTML(DocumentoAmmTrasparenteVO vo, int flagPubblicazione);
}

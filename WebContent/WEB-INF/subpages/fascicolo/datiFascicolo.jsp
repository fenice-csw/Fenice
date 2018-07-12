<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html:xhtml />

<div class="sezione">

	<span class="title"><strong><bean:message key="fascicolo.datifascicolo"/><bean:write name="fascicoloForm" property="progressivo"/></strong></span>	    
	<table summary="">
		<tr>  
	    	<td class="label">
	    		<label for="codice"><bean:message key="fascicolo.progressivo"/>:</label>
	    	</td>  
	    	<td>
		    	<span><strong><bean:write name="fascicoloForm" property="annoProgressivo"/></strong></span>
	    	</td>  
		</tr>
		<tr>  
	    	<td class="label">
	    		<label for="oggettoFascicolo"><bean:message key="fascicolo.oggetto"/>111:</label>
	    	</td>  
	    	<td>
		    	<span><strong><bean:write name="fascicoloForm" property="oggettoFascicolo"/></strong></span>
	    	</td>  
		</tr>
		<tr>  
	    	<td class="label">
				<label for="stato"><bean:message key="fascicolo.stato"/>:</label>
	    	</td>  
	    	<td>
	    		<span><strong><bean:write name="fascicoloForm" property="descrizioneStato"/></strong></span>
		    </td>
		</tr>
		
		
	</table>
</div>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<div class="sezione">
	<span class="title">
	<strong><bean:message key="protocollo.fascicolazioneMultipla"/></strong>
	</span>
	<p>
	Numero di protocolli da fascicolare:<%= request.getAttribute("protocolliIdsSize") %>
	
		
	</p>

</div>

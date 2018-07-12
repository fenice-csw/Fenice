<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Ricerca Protocolli da associare al Fascicolo">

	<div id="protocollo-errori">
		<html:errors bundle="bundleErroriProtocollo" property="protocolloSelezionato" />
	</div>
	<html:form action="/page/fascicolo.do">
		<div></div>
		<div>
		<logic:notEmpty name="fascicoloForm" property="protocolli">
			<display:table class="simple" width="100%"
				requestURI="/page/fascicolo.do?btnRicercaProtocolliDaFascicoli=true"
				name="sessionScope.fascicoloForm.protocolli" export="false"
				sort="list" pagesize="15" id="row">
				<display:column title="">
				    <input type="checkbox" name="protocolliSelezionati" value="<bean:write name='row' property='protocolloId'/>" />
				</display:column>
				<display:column title="Numero">
					<bean:write name="row" property="annoNumeroProtocollo"/>
				</display:column>	
				<%-- display:column property="annoProtocollo" title="Anno" /  --%>
				<display:column property="tipoProtocollo" title="Tipo" />
				<display:column property="dataProtocollo" title="Registrato il"/>
				<display:column property="mittente" title="Mittente" />
				<display:column property="oggetto" title="Oggetto" />
				
				<display:column title="Doc">
					  <html:link action="/page/protocollo/ricerca.do" 
			             paramId="downloadDocprotocolloSelezionato" 
			             paramName="row" 
			             paramProperty="protocolloId" 
			             target="_blank"
			             title="Download File">

				 	<%--html:link 
				 	action="/page/protocollo/ingresso/documento.do" 
				 	paramName="row" 
				 	paramId="downloadDocumentoPrincipale"  
				 	paramProperty="filename" --%>
				 	
   					<span><bean:write name="row" property="pdf" /></span>
  				  </html:link>
				</display:column>	
				<display:column property="statoProtocollo" title="Stato" />
				</display:table>
		</logic:notEmpty>
	
		<logic:empty name="fascicoloForm" property="protocolli">
			<br /><span><strong>Nessun protocollo per gli estremi di ricerca impostati.</strong></span><br /><br />
		</logic:empty>
		<div>
			<html:submit styleClass="submit" property="btnSelezionaProtocolli" value="Seleziona" alt="Seleziona" />
			<html:submit styleClass="submit" property="btnAnnulla" value="Annulla" alt="Annulla" />
		</div>
	</div>
	</html:form>
	
</eprot:page>


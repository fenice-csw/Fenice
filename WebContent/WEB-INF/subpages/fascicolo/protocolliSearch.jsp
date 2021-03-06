<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />

<div>
		<label for="numeroProtocolloDaFascicolo">Cerca numero protocollo: </label> 
		<html:text property="numeroProtocolloDaFascicolo" styleId="numeroProtocolloDaFascicolo" size="10" maxlength="10" /> 
		<html:submit styleClass="submit" property="btnCercaDaFascicolo" value="Cerca" alt="Cerca protocollo" />
		<hr></hr>
	</div>
<div>

<logic:notEmpty name="fascicoloForm" property="protocolliFascicoloSearchCollection">
	
	<display:table class="simple" width="95%" requestURI="/page/fascicoli.do"
		name="sessionScope.fascicoloForm.protocolliFascicoloSearch"
		export="false" sort="list" pagesize="10" id="row">
				<display:column title="Numero">
					<html:link action="/page/fascicoli.do" paramName="row" paramId="protocolloSelezionato" paramProperty="protocolloId">
						<bean:write name="row" property="annoNumeroProtocollo" />
					</html:link>
				</display:column>	
				
				<display:column property="tipoProtocollo" title="Tipo" />
				<display:column property="dataProtocollo" title="Registrato il" />
				
				
				<display:column title="Mittente">
					<logic:notEqual name="row" property="tipoMittente" value="M">
						<bean:write name="row" property="mittente" />
					</logic:notEqual>
					<logic:equal name="row" property="tipoMittente" value="M">
						<ul>
							<logic:iterate id="mittente" name="row" property="mittenti">
								<li><bean:write name="mittente" property="descrizione"/></li>
							</logic:iterate>
						</ul>
					</logic:equal>
				</display:column>

				<display:column property="oggetto" title="Oggetto" />
				<display:column title="Doc">
					  <html:link action="/page/fascicolo.do" 
			             paramId="downloadDocprotocolloSelezionato" 
			             paramName="row" 
			             paramProperty="protocolloId" 
			             title="Download File">		 	
   					<span><bean:write name="row" property="pdf" /></span>
  				  </html:link>
				</display:column>	
			
				<display:column title="Azioni">
				[<html:link  action="/page/fascicolo.do" 
					paramId="duplicaFascicoloProtocollo" paramProperty="protocolloId" paramName="row">cambia/aggiungi fascicolo</html:link>]
			</display:column>	
	</display:table>
</logic:notEmpty>
<logic:empty name="fascicoloForm" property="protocolliFascicoloSearchCollection">
	<span>
	<span><bean:message key="fascicolo.messaggio5"/></span>
	</span>
</logic:empty>
</div>

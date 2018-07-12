<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<html:xhtml />
<div>
	<logic:notEmpty name="scaricoForm" property="protocolliScaricoCollection">
		<display:table class="simple" width="100%" requestURI="/page/protocollo/dashboard/conoscenza.do"
			name="sessionScope.scaricoForm.protocolliScaricoCollection"
			export="false" sort="list" pagesize="30" id="row" >
			<display:column title="N. protocollo">
				<html:link  action="/page/protocollo/scarico.do" 
					paramId="protocolloSelezionato" paramProperty="protocolloId" paramName="row">
					<bean:write name="row" property="annoNumeroProtocollo" />
				</html:link>	
				
			</display:column>	
			<display:column property="dataProtocollo" title="Registrato il" />
			<!-- MITTENTI modifica Daniele Sanna 15/09/2008 -->
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
				
			<display:column title="Oggetto" property="oggetto" />		

			<display:column title="DOC" >
					  <html:link action="/page/protocollo/scarico.do" 
			             paramId="downloadDocprotocolloSelezionato" 
			             paramName="row" 
			             paramProperty="annoNumero" 
			             target="_blank"
			             title="Download File">
				<bean:write name="row" property="pdf"/>
				  </html:link>
			</display:column>
			<display:column title="Azioni">
					[<html:link  action="/page/protocollo/scarico.do"
						paramId="presaVisione" paramProperty="protocolloId" paramName="row">Fascicola</html:link>]
				[<html:link  action="/page/protocollo/scarico.do"
						paramId="visionaProtocollo" paramProperty="protocolloId" paramName="row">Presa Visione</html:link>]
			</display:column>
		</display:table>
		</logic:notEmpty>
</div>


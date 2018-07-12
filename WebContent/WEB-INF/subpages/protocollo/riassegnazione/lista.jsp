<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<html:xhtml />

<div><logic:notEmpty property="protocolliRifiutatiCollection"
	name="respintiForm">
	<display:table class="simple" width="100%"
		name="sessionScope.respintiForm.protocolliRifiutatiCollection"
		requestURI="/page/protocollo/dashboard/respinti.do" export="false"
		sort="list" pagesize="30" id="row">
		<%-- 
		<display:column title="">
			<html:radio property="protocolloRifiutato" value="protocolloId" idName="row" ></html:radio>
		</display:column>
		--%>
		<display:column title="N. protocollo"> 
			<html:link action="/page/protocollo/ingresso/respinti.do" paramId="protocolloSelezionato"  
				paramName="row" paramProperty="protocolloId">
				<bean:write name="row" property="annoNumeroProtocollo" />
			</html:link> 
		</display:column>			
		<display:column property="dataProtocollo" title="Registrato il"
			sortable="true" group="2" headerClass="sortable" />
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
		<display:column title="DOC" >
					  <html:link action="/page/protocollo/ingresso/respinti.do" 
			             paramId="downloadDocprotocolloSelezionato" 
			             paramName="row" 
			             paramProperty="annoNumero" 
			             target="_blank"
			             title="Download File">
				<bean:write name="row" property="pdf"/>
				  </html:link>
			</display:column>		
		<display:column property="messaggio" title="Messaggio" />
			
		<display:column title="Azioni">
		<logic:equal name="row" property="rifiutabile" value="true">
				[<html:link  action="/page/protocollo/ingresso/respinti.do"  
					paramId="rifiutaProtocollo" paramProperty="protocolloId" paramName="row">Rifiuta</html:link>]
		</logic:equal>
		[<html:link  action="/page/protocollo/ingresso/respinti.do"  
					paramId="riassegnaProtocollo" paramProperty="protocolloId" paramName="row">Riassegna</html:link>]
		</display:column>
		
	</display:table>
	<p>
	<%-- 
	<html:submit styleClass="submit" property="btnRiassegna"
		value="Riassegna" alt="Riassegna il protocollo selezionato" />
	--%>
		<html:submit styleClass="button" property="btnAnnulla" value="Annulla"
		alt="Annulla" /></p>
</logic:notEmpty></div>


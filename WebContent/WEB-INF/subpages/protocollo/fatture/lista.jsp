<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>



<html:xhtml />


<div>
	<logic:notEmpty name="scaricoForm" property="protocolliScaricoCollection">
	
	<display:table class="simple" width="100%" requestURI="/page/protocollo/dashboard/scarico.do"
			name="sessionScope.scaricoForm.protocolliScaricoCollection"
			export="false" sort="list" pagesize="30" id="row">
			
			<display:column title=""> 
			<%-- 
			<input type="checkbox" name="protocolloScaricoChkBox" value="<bean:write name='row' property='protocolloId'/>" id="Liked" onclick="checkLikes();" />
			--%>
			<input type="checkbox" name="protocolloScaricoChkBox" value="<bean:write name='row' property='protocolloId'/>"/>
			</display:column>
			<display:column title="N. protocollo">
				<html:link  action="/page/protocollo/fatture.do" 
					paramId="protocolloSelezionato" paramProperty="protocolloId" paramName="row">
					<bean:write name="row" property="annoNumeroProtocollo" />
				</html:link>	
			</display:column>	
		
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
			<display:column title="Oggetto" property="oggetto" />
			<display:column title="Messaggio" property="messaggio" />
			<display:column title="DOC" >
					  <html:link action="/page/protocollo/fatture.do" 
			             paramId="downloadDocprotocolloSelezionato" 
			             paramName="row" 
			             paramProperty="registroAnnoNumero" 
			             target="_blank"
			             title="Download File">
				<bean:write name="row" property="pdf"/>
				  </html:link>
			</display:column>
			<display:column title="Azioni">
			
				<logic:notEqual name="scaricoForm" property="tipoUtenteUfficio" value="R">
					[<html:link  action="/page/protocollo/fatture.do" 
						paramId="daFascicolare" paramProperty="protocolloId" paramName="row" >Fascicola</html:link>]
				</logic:notEqual>
				<eprot:ifAuthorized permission="reassign">
					[<html:link  action="/page/protocollo/fatture.do" 
						paramId="riassegnaProtocollo" paramProperty="protocolloId" paramName="row">Riassegna</html:link>]
				</eprot:ifAuthorized>
				<logic:equal name="row" property="rifiutabile" value="true">
					[<html:link  action="/page/protocollo/fatture.do"  
						paramId="rifiutaProtocollo" paramProperty="protocolloId" paramName="row">Rifiuta</html:link>]
					</logic:equal>
			</display:column>	
		</display:table>
		<p>
		
		 <html:submit styleClass="submit" property="btnFascicola" value="Fascicola i Selezionati"	alt="Fascicola i protocolli" />
		 <eprot:ifAuthorized permission="reassign">
		 	<html:submit styleClass="submit" property="btnRiassegna" value="Riassegna i Selezionati"	alt="Riassegna i protocolli" />
		 </eprot:ifAuthorized>
		</p>
	</logic:notEmpty>
</div>


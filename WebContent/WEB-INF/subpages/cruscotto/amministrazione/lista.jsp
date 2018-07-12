<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>


<html:xhtml />
<div>
	<logic:notEmpty name="cruscottoForm" property="protocolliCollection">
		<display:table class="simple" width="100%" requestURI="/page/cruscotto/amministrazione.do"
			name="sessionScope.cruscottoForm.protocolliCollection"
			export="false" sort="list" pagesize="10" id="row">
			<display:column title=""> 
				<input type="checkbox" name="protocolloChkBox" value="<bean:write name='row' property='protocolloId'/>"/>
			</display:column>
			<display:column title="N. protocollo">
				<html:link  action="/page/cruscotto/struttura.do" 
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
			<display:column title="Giorni in Giacenza" property="giorniPermanenza" />
			<display:column title="DOC" >
					  <html:link action="/page/cruscotto/amministrazione.do" 
			             paramId="downloadDocprotocolloSelezionato" 
			             paramName="row" 
			             paramProperty="protocolloId" 
			             target="_blank"
			             title="Download File">
				<bean:write name="row" property="pdf"/>
				  </html:link>
			</display:column>
			<display:column title="Azioni">
					[<html:link  action="/page/cruscotto/amministrazione.do" 
					paramId="riassegnaProtocollo" paramProperty="protocolloId" paramName="row">Riassegna</html:link>]
			</display:column>	
		</display:table>
		<p>
			<eprot:ifAuthorized permission="reassign">
		 		<html:submit styleClass="submit" property="btnRiassegna" value="Riassegna i Selezionati"	alt="Riassegna i protocolli" />
			 </eprot:ifAuthorized>
		</p>
	</logic:notEmpty>
	<logic:empty name="cruscottoForm" property="protocolliCollection">
	<span><strong>
    	Nessun protocollo in lavorazione
    </strong></span>
	</logic:empty>
</div>


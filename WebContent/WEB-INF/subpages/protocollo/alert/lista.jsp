<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>


<html:xhtml />
<p>
</p>
<br/>
<p>
<div>
	<logic:notEmpty name="alertProtocolloForm" property="protocolliCollection">
		<display:table class="simple" width="95%" requestURI="/page/protocollo/dashboard/alertProtocollo.do"
			name="sessionScope.alertProtocolloForm.protocolliCollection"
			export="false" sort="list" pagesize="30" id="row">
			<display:column title="Numero Protocollo">
				<html:link action="/page/protocollo/alert.do" paramName="row" paramId="visualizzaProtocolloId" paramProperty="protocolloId" >
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
				<!-- FINE MODIFICA -->
			<display:column title="Oggetto" property="oggetto" />
			<display:column title="Messaggio" property="messaggio" />
			<display:column property="giorniPermanenza" title="Giorni in Giacenza" />
			<display:column title="Azioni">
				[<html:link  action="/page/protocollo/alert.do" 
					paramId="daFascicolare" paramProperty="protocolloId" paramName="row" >Fascicola</html:link>]
			<eprot:ifAuthorized permission="reassign">
				[<html:link  action="/page/protocollo/alert.do" 
					paramId="riassegnaProtocollo" paramProperty="protocolloId" paramName="row">Riassegna</html:link>]
			</eprot:ifAuthorized>
			</display:column>	
		</display:table>
	</logic:notEmpty>
</div>
</p>

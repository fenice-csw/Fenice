<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html:xhtml />
	
	
		<logic:notEmpty name="procedimentoForm"	property="protocolliCollection">
			<display:table class="simple" width="100%"
				requestURI="procedimento.do"
				name="sessionScope.procedimentoForm.protocolliCollection" export="false"
				sort="list" pagesize="10" id="row">
				<display:column title="">
				<logic:equal name="row" property="isModificabile" value="true">
					<input type="checkbox" name="protocolliSelezionati" value="<bean:write name='row' property='protocolloId'/>" />
				</logic:equal>
					</display:column>	
				<display:column title="Numero">
					<html:link action="/page/protocollo/ricerca.do" paramName="row" paramId="protocolloSelezionato" paramProperty="protocolloId">
						<bean:write name="row" property="annoNumeroProtocollo" />
					</html:link>
				</display:column>	
				<display:column property="tipoProtocollo" title="Tipo" />
				<display:column property="dataProtocollo" title="Registrato il" />
				<display:column property="mittente" title="Mittente" />
				<display:column property="oggetto" title="Oggetto" />
				<display:column property="pdf" title="Doc" />
				<display:column property="statoProtocollo" title="Stato" />
			</display:table>
			<br/>
			<html:submit styleClass="submit" property="rimuoviProtocolli" value="Rimuovi" title="Rimuovi i protocolli selezionati"/>
		</logic:notEmpty>
	

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html:xhtml />
	<logic:notEmpty name="procedimentoForm" property="faldoniCollection">
		<display:table class="simple" width="100%"
			name="sessionScope.procedimentoForm.faldoniCollection" export="false" sort="list"
			requestURI="/page/procedimento.do" pagesize="10" id="row">
			<display:column title="">
				<input type="checkbox" name="faldoniSelezionati" value="<bean:write name='row' property='id'/>" />
			</display:column>
			<display:column title="Numero Faldone">
			<html:link action="/page/procedimento.do" 
					paramName="row" paramId="visualizzaFaldoneId" paramProperty="id" >
					<bean:write name="row" property="numeroFaldone"/>
			</html:link>
			</display:column>	
			<display:column property="oggetto" title="Oggetto"/>
			<display:column property="dataCreazione" title="Data Creazione"/>
			<display:column property="descUfficio" title="Ufficio"/>
			<display:column property="pathTitolario" titleKey="protocollo.titolario.argomento"/>
			<display:column property="codiceLocale" title="Codice Locale"/>
		</display:table>
		<br/>
		<html:submit styleClass="submit" property="rimuoviFaldoni" value="Rimuovi" title="Rimuovi i faldoni selezionati"/>
	</logic:notEmpty>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml /> 
<eprot:page title="Storia Faldone">

<html:form action="/page/faldone/storia.do">
<span>
	<bean:message key="faldone.progressivo"/> : <strong><bean:write name="faldoneForm" property="numeroFaldone"/></strong> <br />
	<bean:message key="faldone.versione"/> : <strong>
	<html:link action="/page/faldone/storia.do?versioneCorrente=true">
		<bean:write name="faldoneForm" property="versione"/>
	</html:link>	
	</strong> <br />
	
	<bean:message key="protocollo.faldone.ufficio"/> :
	<strong><bean:write name="faldoneForm" property="ufficioCorrentePath"/></strong> 
	<br />

	<bean:message key="protocollo.titolario.argomento"/> :
	<logic:notEmpty name="faldoneForm" property="titolario">
		<strong><bean:write name="faldoneForm" property="titolario.descrizione"/></strong> 
	</logic:notEmpty>
	<br />
	<bean:message key="faldone.datacreazione"/> : <strong><bean:write name="faldoneForm" property="dataCreazione"/></strong> <br />		
	<bean:message key="faldone.oggetto"/> : <strong><bean:write name="faldoneForm" property="oggetto"/></strong> <br />		
	<bean:message key="faldone.stato"/> : <strong><bean:write name="faldoneForm" property="statoFaldone"/></strong> <br />	
	<bean:message key="faldone.dataevidenza"/> : <strong><bean:write name="faldoneForm" property="dataEvidenza"/></strong> <br />	
	<bean:message key="faldone.dataultimomovimento"/> : <strong><bean:write name="faldoneForm" property="dataMovimento"/></strong> <br />	<br />	
</span>
<logic:notEmpty name="storiaFaldoneForm" property="versioniFaldone">
<div>
	<display:table class="simple" width="100%"
		name="requestScope.storiaFaldoneForm.versioniFaldone"
		requestURI="/page/faldone/storia.do" export="false"
		sort="list" pagesize="20" id="row">
		<display:column title="Versione">
		<html:link action="/page/faldone/storia.do" paramId="versioneSelezionata" paramName="row" paramProperty="versione">
			<bean:write name="row" property="versione" />
		</html:link>
		</display:column>
		<display:column property="ufficio" title="Ufficio" />		
		<display:column property="pathTitolario" title="Titolario" />						
		<display:column property="posizioneSelezionata" title="Stato" />				
<%--		<display:column property="dataCreazione" title="Data creazione" />--%>
		<display:column property="dataCarico" title="Data carico" />
		<display:column property="dataScarico" title="Data scarico" />
		<display:column property="dataEvidenza" title="Data evidenza" />
		<display:column property="rowUpdatedUser" title="Modificato da" />		

	</display:table>
	<br /><br />
	
	<html:submit styleClass="submit" property="btnFaldone"
		value="Ritorna al faldone" alt="Ritorna al faldone" />
</div>
</logic:notEmpty>
</html:form>

</eprot:page>





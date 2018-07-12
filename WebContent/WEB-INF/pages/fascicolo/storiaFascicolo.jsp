<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />

<eprot:page title="Storia Fascicolo">

<html:form action="/page/fascicolo/storia.do">
<span>
	<bean:message key="fascicolo.progressivo"/> : <strong><bean:write name="fascicoloForm" property="annoProgressivo"/></strong> <br />
	<bean:message key="fascicolo.versione"/> : <strong>
	<html:link action="/page/fascicolo/storia.do?versioneCorrente=true">
		<bean:write name="fascicoloForm" property="versione"/>
	</html:link>	
	</strong> <br />			
	<bean:message key="fascicolo.ufficio"/> :
	<strong><bean:write name="fascicoloForm" property="ufficioCorrentePath"/></strong> 
	<br />

	<bean:message key="fascicolo.livelli.titolario"/> :
	<logic:notEmpty name="fascicoloForm" property="titolario">
		<strong><bean:write name="fascicoloForm" property="titolario.descrizione"/></strong> 
	</logic:notEmpty>
	<br />
	<bean:message key="fascicolo.oggetto"/>: <strong><bean:write name="fascicoloForm" property="oggettoFascicolo"/></strong> <br />		
	<bean:message key="fascicolo.stato"/>: <strong><bean:write name="fascicoloForm" property="descrizioneStato"/></strong> <br />	
	<bean:message key="fascicolo.data.carico"/>: <strong><bean:write name="fascicoloForm" property="dataCarico"/></strong> <br />		
	<bean:message key="fascicolo.data.movimento"/>: <strong><bean:write name="fascicoloForm" property="dataUltimoMovimento"/></strong> <br />				
	<br />	
</span>
<logic:notEmpty name="storiaFascicoloForm" property="versioniFascicolo">
<div>
	<display:table class="simple" width="100%"
		name="requestScope.storiaFascicoloForm.versioniFascicolo"
		requestURI="/page/fascicolo/storia.do" export="false"
		sort="list" pagesize="15" id="row">
		<display:column title="Versione">
		<html:link action="/page/fascicolo/storia.do" paramId="versioneSelezionata" paramName="row" paramProperty="versione">
			<bean:write name="row" property="versione" />
		</html:link>
		</display:column>
		<display:column property="descrizioneUfficioIntestatario" title="Ufficio" />		
		<display:column property="pathTitolario" title="Classificazione" />						
		<display:column property="descrizioneStato" title="Stato" />				
		<display:column  title="Trattato da">
			<bean:write name="row" property="descrizioneUfficioResponsabile" />
			<bean:write name="row" property="descrizioneUtenteResponsabile" />		
		</display:column>
		<display:column property="dataUltimoMovimento" title="Data ultimo Movimento" />
		
	</display:table>
	<br /><br />
	
	<html:submit styleClass="submit" property="btnFascicolo"
		value="Ritorna al fascicolo" alt="Ritorna al fascicolo" />
</div>
</logic:notEmpty>
</html:form>

</eprot:page>





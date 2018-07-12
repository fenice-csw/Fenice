<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Storia Procedimento">
 	<html:form action="/page/procedimento/storia.do"> 
	<br />
	<br />
	<span>
		<bean:message key="procedimento.progressivo"/> : <strong><bean:write name="storiaProcedimentoForm" property="numeroProcedimento"/></strong> <br />
		<bean:message key="procedimento.versione"/> : <strong>
		<html:link action="/page/procedimento/storia.do?versioneCorrente=true">
			<bean:write name="storiaProcedimentoForm" property="versione"/>
		</html:link>
		</strong> <br />		
		<bean:message key="procedimento.dataavvio"/>  : <strong><bean:write name="storiaProcedimentoForm" property="dataAvvio"/></strong> <br />		
		<bean:message key="procedimento.oggetto"/> : <strong><bean:write name="storiaProcedimentoForm" property="oggettoProcedimento"/></strong> <br />		
	</span>
	<br />	
	<br />
	<div>
		<display:table class="simple" width="100%"
			name="requestScope.storiaProcedimentoForm.versioniProcedimento"
			requestURI="/page/procedimento/storia.do" export="false"
			sort="list" pagesize="20" id="row">
			<display:column title="Versione">
			<html:link action="/page/procedimento/storia.do" paramId="versioneSelezionata" paramName="row" paramProperty="versione">
				<bean:write name="row" property="versione" />
			</html:link>
			</display:column>
			<display:column property="oggetto" title="Oggetto" />
			<display:column property="dataAvvioView" title="Data Avvio" />
			<display:column property="descUfficioId" title="Ufficio" />
	
		</display:table>
		<br /><br />
	</div>
	<html:submit styleClass="submit" property="btnStoriaProcedimento"
			value="Ritorna al procedimento" alt="Ritorna al procedimento" />
	
	</html:form>
</eprot:page>
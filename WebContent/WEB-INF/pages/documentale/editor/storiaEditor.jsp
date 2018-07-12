<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />

<eprot:page title="Storia Documento da Editor">
<br /><br />
<html:form action="/page/editor/storia.do">
<span>
	Id : 
	<strong><bean:write name="editorForm" property="documentoId"/></strong> <br />
	<bean:message key="fascicolo.versione"/> : <strong>
	<html:link action="/page/documentale/editor.do" paramId="docId" paramName="editorForm" paramProperty="documentoId">
		<bean:write name="editorForm" property="versione"/>
	</html:link>	
	</strong> <br />			
	Nome File :
	<strong><bean:write name="editorForm" property="nomeFile"/></strong> 
	<br />
	Trattato da :
	<strong><bean:write name="editorForm" property="intestatario"/></strong> 
	<br />
	Stato :
	<strong><bean:write name="editorForm" property="stato"/></strong> 
	<br />
</span>
<logic:notEmpty name="storiaEditorForm" property="versioniEditor">
	<display:table class="simple" width="100%"
		name="requestScope.storiaEditorForm.versioniEditor"
		requestURI="/page/editor/storia.do" export="false"
		sort="list" pagesize="15" id="row">
		<display:column title="Versione">
		<html:link action="/page/documentale/editor.do" paramId="versioneSelezionata" paramName="row" paramProperty="versione">
			<bean:write name="row" property="versione" />
		</html:link>
		</display:column>
		<display:column property="stato" title="Stato" />
		<display:column property="data" title="Data" />
		<display:column property="nome" title="Nome File" />
		<display:column  property="intestatario" title="Trattato da"/>
	</display:table>
	<br /><br />
</logic:notEmpty>
<html:link action="/page/documentale/editor.do" paramName="editorForm" paramId="docId"paramProperty="documentoId" styleClass="button">
					Ritorna al Documento
			</html:link>
</html:form>

</eprot:page>





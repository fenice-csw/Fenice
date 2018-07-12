<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Storia Titolario">

<html:form action="/page/amministrazione/titolario.do">
<div>
<logic:notEmpty name="titolarioForm" property="storiaTitolario">
	<display:table class="simple" 
		width="100%"
		name="sessionScope.titolarioForm.storiaTitolario"
		requestURI="/page/amministrazione/titolario.do" 
		export="false"
		sort="list" 
		pagesize="20" 
		id="row">
		<display:column property="versione" title="Versione" />
		<display:column property="rowCreatedTime" title="Data" decorator="it.finsiel.siged.mvc.presentation.helper.DateDecorator" />
		<display:column property="descrizione" title="Descrizione" />
		<display:column property="fullPathDescription" title="Full path" />
		<display:column property="massimario" title="Massimario" />			
	</display:table>
	<p>
		<html:submit styleClass="submit" property="btnAnnulla" value="Annulla" alt="annulla" />
	</p>
</logic:notEmpty>
</div>
</html:form>

</eprot:page>





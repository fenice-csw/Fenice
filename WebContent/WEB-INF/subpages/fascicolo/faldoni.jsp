<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />

<logic:empty name="fascicoloForm" property="faldoniFascicolo">
<span><bean:message key="fascicolo.messaggio.faldoni"/></span><br /><br />
</logic:empty>

<logic:notEmpty name="fascicoloForm" property="faldoniFascicolo">
	<display:table class="simple" width="95%" requestURI="/page/fascicoli.do"
		name="sessionScope.fascicoloForm.faldoniFascicolo"
		export="false" sort="list" pagesize="15" id="row">
	
		<display:column property="numeroFaldone" title="Numero" />
		<display:column property="oggetto" title="Oggetto" />
		<display:column property="codiceLocale" title="Codice Locale" />
		
	</display:table>
</logic:notEmpty>

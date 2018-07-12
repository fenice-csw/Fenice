<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />

<logic:empty name="fascicoloForm" property="procedimentiFascicolo">
<span><bean:message key="fascicolo.messaggio.procedimenti"/></span><br /><br />
</logic:empty>

<logic:notEmpty name="fascicoloForm" property="procedimentiFascicolo">
	<display:table class="simple" width="95%" requestURI="/page/fascicolo/allaccioview.do"
		name="sessionScope.fascicoloForm.procedimentiFascicolo"
		export="false" sort="list" pagesize="10" id="row">	
		<display:column property="numeroProcedimento" title="Numero" />
		<display:column property="oggetto" title="Oggetto" />
	</display:table>
</logic:notEmpty>
<div>
</div>

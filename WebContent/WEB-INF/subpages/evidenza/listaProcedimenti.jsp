<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />

<logic:notEmpty name="ricercaEvidenzaForm" property="evidenzeProcedimenti">
<div>

	<display:table class="simple" width="100%" requestURI="/page/evidenza/cerca.do"
		name="sessionScope.ricercaEvidenzaForm.evidenzeProcedimenti"
		export="false" sort="list" pagesize="10" id="row">
		<display:column property="numeroProcedimento" title="N. Procedimento" />
			<display:column property="dataEvidenza" title="Data evidenza" />
			<display:column property="descUfficioId" title="Ufficio" />
		<display:column property="oggetto" title="Oggetto procedimento" />
<%--		<display:column property="referente" title="Referente" />
		<display:column property="id" title="ID documento collegato al fascicolo/procedimento" />--%>
	</display:table>

</div>
</logic:notEmpty>

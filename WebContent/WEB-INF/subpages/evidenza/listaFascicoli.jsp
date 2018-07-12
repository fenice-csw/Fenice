<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />

<logic:notEmpty name="ricercaEvidenzaForm" property="evidenzeFascicoli">
<div>

	<display:table class="simple" width="100%" requestURI="/page/evidenza/cerca.do"
		name="sessionScope.ricercaEvidenzaForm.evidenzeFascicoli"
		export="false" sort="list" pagesize="10" id="row">
		<display:column property="annoProgressivo" title="Fascicolo" />
		<display:column property="dataEvidenza" title="Data evidenza" />
		<display:column property="descrizioneUfficioIntestatario" title="Ufficio" />
		<display:column property="oggetto" title="Oggetto fascicolo" />
<%--		<display:column property="referente" title="Referente" />
		<display:column property="id" title="ID documento collegato al fascicolo/procedimento" />--%>
	</display:table>
	
</div>
</logic:notEmpty>

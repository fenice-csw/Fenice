<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />

<logic:notEmpty name="evidenzaForm" property="evidenze">
<div>
	<display:table class="simple" width="95%" requestURI="/page/nuovo.do"
		name="sessionScope.evidenzaForm.evidenze"
		export="false" sort="list" pagesize="10" id="row">
<!--		<display:column title="">-->
<!--			<html:radio property="fascicoloSelezionato" idName="row" value="id"></html:radio>-->
<!--		</display:column>-->
		<display:column property="numero" title="Numero fascicolo/procedimento" />
		<display:column property="data" title="Data evidenza" />
		<display:column property="ufficio" title="Ufficio" />
		<display:column property="oggetto" title="Oggetto fascicolo/procedimento" />
		<display:column property="referente" title="Referente" />
		<display:column property="id" title="ID documento collegato al fascicolo/procedimento" />
	</display:table>
	<p>
		<html:submit styleClass="submit" property="btnSeleziona" value="Seleziona" alt="Seleziona" />
		<%--html:submit styleClass="button" property="btnAnnullaRicerca" value="Annulla" alt="Annulla"/--%>
	</p>
</div>
</logic:notEmpty>

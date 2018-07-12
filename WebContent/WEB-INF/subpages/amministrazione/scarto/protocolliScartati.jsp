<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />
<div>
<bean:define value="/page/amministrazione/archivioScarto.do?protocolloSelezionato=" scope="request" id="url" />
	<display:table class="simple" width="98%" requestURI="/page/amministrazione/archivioScarto.do"
		name="requestScope.archivioScartoProtocolliForm.protocolliCollection" export="false"
		sort="list" pagesize="15" id="row">
		<bean:define id="protocolloId" name="row" property="protocolloId" />
		<bean:define id="tipoProtocollo" name="row" property="tipoProtocollo" />
		<bean:define id="versione" name="row" property="versione" />		
		<display:column title="Numero">
			<a href="/page/amministrazione/archivioScarto.do?"
			protocolloSelezionato=<bean:write name="row" property="protocolloId" /> &amp;
 			tipoProtocollo=<bean:write name="row" property="tipoProtocollo"/>&amp;
 			versioneSelezionata=<bean:write name="row" property="versione"/> >
 			<bean:write name="row" property="numeroProtocollo" /></a>
		</display:column>
		<display:column property="tipoProtocollo" title="Tipo" />
		<display:column property="dataProtocollo" title="Registrato il" />
		<display:column property="mittente" title="Mittente" />
		<display:column property="destinatario" title="Assegnatari/Destinatari" />
		<display:column property="oggetto" title="Oggetto" />
		<display:column property="pdf" title="PDF" />
		<display:column property="statoProtocollo" title="Stato" />
	</display:table>
</div>

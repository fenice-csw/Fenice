<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display" %>

<html:xhtml />
<logic:notEmpty name="protocolloDaScartareForm" property="protocolliCollection">
<div class="Sezione">
<bean:define value="/page/amministrazione/report/protocolliDaScartare.do?protocolloSelezionato=" scope="request" id="url" />
	<display:table class="simple" width="98%" requestURI="/page/amministrazione/report/protocolliDaScartare.do"
		name="requestScope.protocolloDaScartareForm.protocolliCollection" export="false"
		sort="list" pagesize="15" id="row">
		<bean:define id="protocolliCollection" name="protocolloDaScartareForm" property="protocolliCollection" />
<%--		<bean:define id="protocolloId" name="protocolliCollection" property="protocolloId" />-->
<!--		<bean:define id="tipoProtocollo" name="protocolliCollection" property="tipoProtocollo" />-->
<!--		<bean:define id="versione" name="protocolliCollection" property="versione" />		-->
<!--		<display:column title="Numero">-->
<!--			<a href="/page/amministrazione/archivioScarto.do?"-->
<!--			protocolloSelezionato='<bean:write name="protocolliCollection" property="protocolloId" />' &amp;-->
<!-- 			tipoProtocollo=<bean:write name="protocolliCollection" property="tipoProtocollo"/>&amp;-->
<!-- 			versioneSelezionata=<bean:write name="protocolliCollection" property="versione"/> >-->
<!-- 			<bean:write name="protocolliCollection" property="numeroProtocollo" /></a>-->
<!--		</display:column>--%>
		<display:column property="numeroProtocollo" title="Numero" />
		<display:column property="tipoProtocollo" title="Tipo" />
		<display:column property="dataProtocollo" title="Data Registrazione" />
		<display:column property="mittente" title="Mittente" />
		<display:column property="destinatario" title="Assegnatari/Destinatari" />
		<display:column property="oggetto" title="Oggetto" />
		<display:column property="massimario" title="Massimario di scarto" />
	</display:table>
</div>
</logic:notEmpty>	

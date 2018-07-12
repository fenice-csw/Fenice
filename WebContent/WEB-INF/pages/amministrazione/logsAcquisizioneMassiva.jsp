<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<eprot:page title="Logs acquisizione massiva">
	
	<bean:define value="/page/amministrazione/acquisizione.do" scope="request" id="url" />
	<div id="protocollo-errori">
	<html:errors bundle="bundleErroriProtocollo" />
	</div>
	<logic:notEmpty name="acquisizioneMassivaForm" property="documentiAcquisiti">
	<br />
	<bean:message key="amministrazione.acquisizionelog.messaggio1"/><strong>
	<bean:write name="acquisizioneMassivaForm" property="documentiAcquisiti" />
	</strong> <bean:message key="amministrazione.acquisizionelog.messaggio2"/> .
	</logic:notEmpty>
	<br />
	<logic:empty name="acquisizioneMassivaForm" property="logsAcquisizione">
	<br />
	<span><strong><bean:message key="amministrazione.acquisizionelog.messaggio3"/>.</strong></span>
	</logic:empty>
	<logic:notEmpty name="acquisizioneMassivaForm" property="logsAcquisizione">
	<br />
	<span><strong><bean:message key="amministrazione.acquisizionelog.messaggio4"/>Documenti scartati.</strong></span>
	<html:form action="/page/amministrazione/acquisizioneMassiva.do">
		<div>
			<display:table class="simple" width="100%"
				requestURI="/page/amministrazione/acquisizioneMassiva.do"
				name="requestScope.acquisizioneMassivaForm.logsAcquisizione" export="false"
				sort="list" pagesize="15" id="row">
				<display:column property="data" decorator="it.finsiel.siged.mvc.presentation.helper.DateDecorator" title="Data acquisizione" />
				<display:column property="fileName" title="Nome File" />
				<display:column property="errore" title="Motivo scarto" />
			</display:table>
			<p>
				<br />
				<html:submit styleClass="submit" property="btnAnnulla"	value="Annulla" alt="Ritorna alla lista dei documenti da acquisire" />
				<html:submit styleClass="submit" property="btnCancellaLogs"	value="Cancella tabella dei logs" alt="Cancella il contenuto della tabella dei log" />
			</p>
		</div>
	</html:form>
	</logic:notEmpty>
</eprot:page>


<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Registro d'emergenza">	
	
<%--	<bean:define id="url" page="/page/protocollo/registroEmergenza.do" scope="request"/>-->
<!--	<div id="protocollo-errori">-->
<!--	<html:errors bundle="bundleErroriProtocollo" />-->
<!--	</div>--%>
	<logic:empty name="registroEmergenzaForm" property="protocolliToExport">
	<br />
	<span><strong><bean:message key="protocollo.registroemergenza.messaggio"/>.</strong></span>
	</logic:empty>
	<html:form action="/page/protocollo/registroEmergenza.do">

		<div>
		<logic:notEmpty name="registroEmergenzaForm" property="protocolliToExport">
			<display:table class="simple" width="100%"
				requestURI="/page/protocollo/registroEmergenza.do"
				name="requestScope.registroEmergenzaForm.protocolliToExport" export="false"
				sort="list" pagesize="15" id="row">
				<display:column title="Numero" property="numProtocollo"
					href="/eprot/page/protocollo/registroEmergenza.do" paramId="protocolloId"
					paramProperty="id" />
				<display:column property="annoRegistrazione" title="Anno" />
				<display:column property="flagTipo" title="Tipo" />
				<display:column decorator="it.finsiel.siged.mvc.presentation.helper.DateDecorator" 
					property="dataRegistrazione" title="Registrato il" />
				<display:column property="oggetto" title="Oggetto" />
			</display:table>
			<p>
				<html:submit styleClass="submit" property="btnConferma"	value="Esporta i protocolli" 
					alt="Exporta i protocolli" />
			</p>
		</logic:notEmpty>

		</div>
	</html:form>

</eprot:page>


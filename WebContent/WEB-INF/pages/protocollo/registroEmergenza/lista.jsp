<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />
<eprot:page title="Protocolli prenotati ">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriProtocollo" /></div>
	<div id="messaggi"><jsp:include
		page="/WEB-INF/subpages/documentale/common/messaggi.jsp" /></div>
	<div>
	
		<div>
    		<jsp:include page="/WEB-INF/subpages/protocollo/registroEmergenza/lista.jsp" />
		</div>
	<!-- 
	<logic:notEmpty name="registroEmergenzaForm" property="protocolliPrenotati">
		<display:table class="simple" width="100%" list="protocolliPrenotati"
			requestURI="/page/protocollo/emergenza.do"
			name="requestScope.registroEmergenzaForm.protocolliPrenotati"
			export="false" sort="list" pagesize="15" id="row">
			<display:column title="Numero/anno">
				<a
					href="emergenza.do?tipoProtocollo=
		<bean:write name='row' property='tipoProtocollo'/>&amp;
		protocolloSelezionato=<bean:write name='row' property='protocolloId'/>		
		">
				<bean:write name="row" property="annoNumeroProtocollo" /> </a>
			</display:column>

			<display:column property="tipoProtocollo" title="Tipo" />
			<display:column property="dataProtocollo" title="Data registrazione" />
			<display:column property="oggetto" title="Oggetto" />
		</display:table>
	</logic:notEmpty>
	 -->
	
	</div>

</eprot:page>


<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<html:xhtml />

<div class="sezione">
<span class="title">
	<strong><bean:message key="protocollo.registroemergenza.titolo"/></strong>
	</span>
	<logic:notEmpty name="registroEmergenzaForm" property="sessioniAperte">
		<display:table class="simple" width="50%" list="sessioniAperte"
			requestURI="/page/protocollo/emergenza.do"
			name="requestScope.registroEmergenzaForm.sessioniAperte"
			export="false" sort="list" pagesize="15" id="row">
			<display:column property="dataProtocollo" title="Data apertura sessione" />
			<display:column property="intervallo" title="intervallo numero protocolli" />
			<display:column property="dimensione" title="numero di protocolli da inserire" />
		</display:table>
	</logic:notEmpty>
	
	<logic:empty name="registroEmergenzaForm" property="sessioniAperte">
	<strong><span>Nessuna sessione d'emergenza aperta.</span></strong>
	</logic:empty>
</div>

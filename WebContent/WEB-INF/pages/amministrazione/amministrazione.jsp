<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Gestione amministrazione">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriProtocollo" /></div>
	<div id="protocollo-messaggi"><logic:messagesPresent
		message="true">
		<ul>
			<html:messages id="actionMessage" message="true"
				bundle="bundleMessaggiAmministrazione">
				<li><bean:write name="actionMessage" /></li>
			</html:messages>
		</ul>
	</logic:messagesPresent></div>
	<html:form action="/page/amministrazione.do">
		<div class="sezione"><span class="title"><strong><bean:message
			key="amministrazione.datigenarali" /></strong></span><br />
		<jsp:include
			page="/WEB-INF/subpages/amministrazione/dati_generali.jsp" /></div>
		<div class="sezione"><span class="title"><strong><bean:message
			key="amministrazione.parametri" /></strong></span><br />
		<jsp:include page="/WEB-INF/subpages/amministrazione/dati_ldap.jsp" />
		</div>
		
		<div class="sezione"><span class="title"><strong><bean:message
			key="amministrazione.parametri_ftp" /></strong></span><br />
		<jsp:include page="/WEB-INF/subpages/amministrazione/dati_ftp.jsp" />
		</div>
		
		<p><html:submit styleClass="submit" property="btnSalva"
			value="Salva" alt="Salva" /> <html:submit styleClass="button"
			property="btnAnnulla" value="Annulla" alt="Annulla" /></p>
	</html:form>
</eprot:page>

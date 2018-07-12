<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Oggettario">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" /></div>
	<div id="protocollo-messaggi"><logic:messagesPresent
		message="true">
		<ul>
			<html:messages id="actionMessage" message="true"
				bundle="bundleMessaggiAmministrazione">
				<li><bean:write name="actionMessage" /></li>
			</html:messages>
		</ul>
	</logic:messagesPresent></div>
		<div class="sezione">
		<span class="title"> <strong>Lista oggetti</strong> </span>
		<jsp:include page="/WEB-INF/subpages/amministrazione/oggetto/lista.jsp" />
		</div>
		<html:form action="/page/amministrazione/oggettario.do">
		<html:submit value="Nuovo" alt="Nuovo Oggetto" styleClass="submit" property="nuovoAction"/>
		</html:form>
		
</eprot:page>

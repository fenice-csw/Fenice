<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Gestione uffici">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" />
	</div>

	<logic:messagesPresent message="true">
		<div id="protocollo-messaggi">
		<ul>
			<html:messages id="actionMessage" message="true"
				bundle="bundleErroriAmministrazione">
				<li><bean:write name="actionMessage" /></li>
			</html:messages>
		</ul>
		</div>
	</logic:messagesPresent>


	<html:form
		action="/page/amministrazione/organizzazione/selezionaUfficio.do">

		<jsp:include
			page="/WEB-INF/subpages/amministrazione/organizzazione/uffici/uffici.jsp" />
		<br />
		<br />
		<html:submit styleClass="submit" property="btnStoria" value="Storia Organigramma" title="Storico dell'organigramma" /> 
	</html:form>
</eprot:page>

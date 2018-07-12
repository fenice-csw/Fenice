<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Titolario">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriProtocollo" />
	</div>
	<div id="protocollo-messaggi"><html:messages id="msg"
		message="true">
		<ul>
			<li><bean:write name="msg" /></li>
		</ul>
		</html:messages>
	</div>
	
	<html:form action="/page/amministrazione/titolario.do">
		<div id="root_titolario"><jsp:include
			page="/WEB-INF/subpages/amministrazione/titolario/titolario.jsp" />
		</div>

	</html:form>
</eprot:page>
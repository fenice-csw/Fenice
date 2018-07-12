<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione registro">
<div id="protocollo-errori">
<jsp:include page="/WEB-INF/subpages/amministrazione/common/errori.jsp" />
</div>
<html:form action="/page/amministrazione/registro.do">
<div class="sezione">
	<span class="title"><strong><bean:message key="amministrazione.registro.datiregistro"/></strong></span><br />
	<jsp:include page="/WEB-INF/subpages/amministrazione/registro/datiRegistro.jsp" />
</div>
<logic:present name="registroForm" property="id"> 
<%--<bean:parameter id="id" name="id"/>--%>
<logic:greaterThan name="id" value="0">
	<div class="sezione">
		<span class="title"><strong><bean:message key="amministrazione.registro.utentiabilitati"/></strong></span><br />
		<jsp:include page="/WEB-INF/subpages/amministrazione/registro/utentiAbilitati.jsp" />
	</div>
	<div class="sezione">
		<span class="title"><strong><bean:message key="amministrazione.registro.utentinoabilitati"/></strong></span><br />
		<jsp:include page="/WEB-INF/subpages/amministrazione/registro/utentiNonAbilitati.jsp" />
	</div>
</logic:greaterThan>
</logic:present>

</html:form>
</eprot:page>

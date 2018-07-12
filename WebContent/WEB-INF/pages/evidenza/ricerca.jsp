<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />

<eprot:page title="Ricerca evidenze">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>
<html:form action="/page/evidenza/cerca.do">
<div class="sezione">
	<span class="title"><strong><bean:message key="protocollo.evidenze.ricercaevidenze"/></strong></span>
		<jsp:include page="/WEB-INF/subpages/evidenza/cerca.jsp" />
		
</div>
<logic:notEmpty name="ricercaEvidenzaForm" property="evidenzeFascicoli">
<div class="sezione">
	<span class="title"><strong><bean:message key="evidenza.evidenzeFascicoli"/></strong></span>
<jsp:include page="/WEB-INF/subpages/evidenza/listaFascicoli.jsp" />
</div>
</logic:notEmpty>
<logic:notEmpty name="ricercaEvidenzaForm" property="evidenzeProcedimenti">
<div class="sezione">
	<span class="title"><strong><bean:message key="evidenza.evidenzeProcedimenti"/></strong></span>
<jsp:include page="/WEB-INF/subpages/evidenza/listaProcedimenti.jsp" />
</div>
</logic:notEmpty>

</html:form>

</eprot:page>
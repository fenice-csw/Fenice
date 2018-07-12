<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione evidenze">

    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />


<html:form action="/page/evidenza/cerca.do">
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

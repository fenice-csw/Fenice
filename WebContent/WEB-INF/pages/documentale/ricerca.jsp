<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Ricerca documenti">

<html:form action="/page/documentale/cerca.do">

    <jsp:include page="/WEB-INF/subpages/documentale/common/messaggi.jsp" />

<div id="protocollo-errori">
    <jsp:include page="/WEB-INF/subpages/documentale/common/errori.jsp" />
</div>

<div class="sezione">
	<span class="title"><strong><bean:message key="documentale.ricercadocumenti"/></strong></span>
	<jsp:include page="/WEB-INF/subpages/documentale/ricerca/cerca.jsp" />
</div>


<logic:notEmpty name="documentoForm" property="listaDocumenti" >
<div class="sezione">
  <span class="title"><strong><bean:message key="documentale.listadocumenti"/></strong></span>
	<jsp:include page="/WEB-INF/subpages/documentale/ricerca/listaDocumenti.jsp" />
</div>
</logic:notEmpty>

</html:form>
</eprot:page>

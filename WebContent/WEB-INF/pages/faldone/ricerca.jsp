<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />

<eprot:page title="Ricerca faldoni">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>

<html:form action="/page/faldone/cerca.do">
<div class="sezione">
	<span class="title"><strong><bean:message key="protocollo.faldone.ricercafaldoni"/></strong></span>
		<jsp:include page="/WEB-INF/subpages/protocollo/faldone/cerca.jsp" />
</div><hr />
<div id="elenco">
	<jsp:include page="/WEB-INF/subpages/protocollo/faldone/listaFaldoni.jsp" />
</div>

</html:form>

</eprot:page>
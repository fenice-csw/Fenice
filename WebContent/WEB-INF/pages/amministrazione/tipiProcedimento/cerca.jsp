<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Cerca Tipi Procedimento">

<html:form action="/page/amministrazione/tipiProcedimento.do" enctype="multipart/form-data">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>

<div class="sezione">
	<jsp:include page="/WEB-INF/subpages/amministrazione/tipiProcedimento/cerca.jsp" />

</div>

<div class="sezione">
	<jsp:include page="/WEB-INF/subpages/amministrazione/tipiProcedimento/elenco.jsp" />
</div>

</html:form>

</eprot:page>
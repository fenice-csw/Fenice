<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Permessi utenti">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriAmministrazione" />
</div>

<html:form action="/page/amministrazione/organizzazione/utenti/cerca.do" >
	<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/utenti/cerca.jsp" />
</html:form>

<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/utenti/lista.jsp" />

</eprot:page>
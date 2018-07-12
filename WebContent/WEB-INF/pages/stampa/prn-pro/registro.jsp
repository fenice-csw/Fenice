<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Stampa Registro">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>

<html:form action="/page/stampa/prn-pro/registro.do">
<div>
	<jsp:include page="/WEB-INF/subpages/stampa/prn-pro/registro/cerca.jsp" />
	<hr></hr>		
</div>
<div>
	<jsp:include page="/WEB-INF/subpages/stampa/prn-pro/registro/lista.jsp" />
</div>
</html:form>
</eprot:page>




<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Cerca Lista Distribuzione">




<html:form action="/page/protocollo/anagrafica/listaDistribuzione">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>

<div id="cerca">
	<jsp:include page="/WEB-INF/subpages/protocollo/anagrafica/listaDistribuzioni/cercaDaProtocollo.jsp" />

</div>
<hr />
<div id="elenco">
	<jsp:include page="/WEB-INF/subpages/protocollo/anagrafica/listaDistribuzioni/elencoLinkProtocollo.jsp" />
</div>

</html:form>


</eprot:page>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione documentale">

<bean:define id="baseUrl" value="/page/documentale/documento.do" scope="request" />
<html:form action="/page/documentale/documento.do" enctype="multipart/form-data">
<jsp:include page="/WEB-INF/subpages/documentale/common/pathSenzaLink.jsp" />
<div id="documento">

  <jsp:include page="/WEB-INF/subpages/documentale/common/messaggi.jsp" />

<div id="protocollo-errori">
    <jsp:include page="/WEB-INF/subpages/documentale/common/errori.jsp" />
</div>

<jsp:include page="/WEB-INF/subpages/documentale/datiDocumento.jsp" />

<logic:equal name="documentoForm" property="statoArchivio" value="C">
	<br class="hidden" />
	<jsp:include page="/WEB-INF/subpages/documentale/datiFascicoli.jsp" />
</logic:equal>
<br class="hidden" />
<%--  
<jsp:include page="/WEB-INF/subpages/documentale/permessi.jsp" />
--%>
<div>
<br />
<logic:equal name="documentoForm" property="documentoId" value="0">
	<html:submit styleClass="submit" property="salvaAction" value="Salva" alt="Salva documento" />
	<html:submit styleClass="submit" property="annullaAction" value="Annulla" alt="Annulla l'operazione" />
</logic:equal>
<logic:notEqual name="documentoForm" property="documentoId" value="0">
	<html:submit styleClass="submit" property="salvaAction" value="Salva" alt="Salva documento" />
	<html:submit styleClass="submit" property="annullaAction" value="Annulla" alt="Annulla l'operazione" />
</logic:notEqual>
</div>
</div>
</html:form>
</eprot:page>

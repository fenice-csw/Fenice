<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione documentale">

<html:form action="/page/documentale/inviaDocumentoUtente.do">


    <jsp:include page="/WEB-INF/subpages/documentale/common/messaggi.jsp" />

<div id="protocollo-errori">
    <jsp:include page="/WEB-INF/subpages/documentale/common/errori.jsp" />
</div>
<div class="sezione">
<span class="title"> <strong>Utente Destinatario</strong></span>
<jsp:include page="/WEB-INF/subpages/documentale/invia/uffici.jsp" />
</div>
<%-- 
<jsp:include page="/WEB-INF/subpages/documentale/common/filesSposta.jsp" />
--%>
<html:submit styleClass="submit" property="inviaDocumento" value="Invia" alt="Sposta qui" />
</html:form>
</eprot:page>

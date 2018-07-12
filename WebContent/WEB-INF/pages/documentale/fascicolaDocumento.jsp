<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione documentale">
<jsp:include page="/WEB-INF/subpages/documentale/common/pathSenzaLink.jsp" />
<html:form action="/page/documentale/documento.do">


    <jsp:include page="/WEB-INF/subpages/documentale/common/messaggi.jsp" />

<div id="protocollo-errori">
    <jsp:include page="/WEB-INF/subpages/documentale/common/errori.jsp" />
</div>

	<br class="hidden" />
	<jsp:include page="/WEB-INF/subpages/documentale/datiDocumentoView.jsp" />

	
	<br class="hidden" />
		<jsp:include page="/WEB-INF/subpages/documentale/datiFascicoli.jsp" />
	
	<html:submit styleClass="submit" property="fascicolaDocumentoAction" value="Fascicola" alt="Fascicola il documento" />
    <html:submit styleClass="submit" property="annullaAction" value="Annulla" alt="Torna al documentale" />
</html:form>
</eprot:page>

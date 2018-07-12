<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Invio documenti classificati al protocollo">
<jsp:include page="/WEB-INF/subpages/documentale/common/pathSenzaLink.jsp" />
<html:form action="/page/documentale/inviaDocumento.do" >

<div id="protocollo">


    <jsp:include page="/WEB-INF/subpages/documentale/common/messaggi.jsp" />

<div id="protocollo-errori">
    <jsp:include page="/WEB-INF/subpages/documentale/common/errori.jsp" />
</div>

<br class="hidden" />
<jsp:include page="/WEB-INF/subpages/documentale/datiDocumentoView.jsp" />
	<logic:equal name="documentoForm" property="statoArchivio" value="C" >
	<br class="hidden" />
	<div class="sezione">
		<span class="title"><strong><bean:message key="documentale.destinatari"/></strong></span>	
		<jsp:include page="/WEB-INF/subpages/documentale/destinatari.jsp" />
	</div>
	</logic:equal>
	<br class="hidden" />
<div>
<html:submit styleClass="submit" property="btnInvioProtocollo" value="Conferma invio" title="Conferma invio documento al protocollo" />
<html:submit styleClass="button" property="btnAnnullaSelezione" value="Annulla" title="Annulla l'operazione" />
</div>
</div>
</html:form>
</eprot:page>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Invio fascicoli al protocollo">

<html:form action="/page/invioFascicolo.do" >

<div id="protocollo">


    <jsp:include page="/WEB-INF/subpages/fascicolo/errori.jsp" />


<br class="hidden" />
<html:hidden property="id" />
<jsp:include page="/WEB-INF/subpages/fascicolo/datiFascicolo.jsp" />
<br class="hidden" />
    <jsp:include page="/WEB-INF/subpages/fascicolo/invioProtocollo/listaDocumentiInvio.jsp" />
<br class="hidden" />
<div class="sezione">
	<span class="title"><strong><bean:message key="fascicolo.destinatari" /></strong></span>	    
    <jsp:include page="/WEB-INF/subpages/fascicolo/invioProtocollo/destinatari.jsp" />
</div>
<br class="hidden" />
<div>
<html:submit styleClass="submit" property="btnInvioProtocollo" value="Conferma invio" title="Conferma invio fascicolo al protocollo" />
<html:submit styleClass="button" property="btnAnnullaInvioProtocollo" value="Annulla" title="Annulla l'operazione" />
</div>
</div>
</html:form>
</eprot:page>

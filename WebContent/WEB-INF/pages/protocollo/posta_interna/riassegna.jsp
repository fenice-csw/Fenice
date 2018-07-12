<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Riassegna protocollo">


<html:form action="/page/protocollo/posta_interna/riassegna.do">

<div id="protocollo">


    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />

<br class="hidden" />

<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/datiProtocollo.jsp" />

<br class="hidden" />

<jsp:include page="/WEB-INF/subpages/protocollo/common/datiDocumentoView.jsp" />

<br class="hidden" />

<div id="sezioni-protocolli" class="sezione">
    <jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/destinatariRiassegnazione.jsp" />
</div>

<br class="hidden" />
<div>
	<html:submit styleClass="submit" property="salvaAction" value="Riassegna" alt="Riassegna protocollo" />
	<html:submit styleClass="submit" property="indietroAction" value="Indietro" alt="Annulla l'operazione" />
</div>

</div>

</html:form>

</eprot:page>
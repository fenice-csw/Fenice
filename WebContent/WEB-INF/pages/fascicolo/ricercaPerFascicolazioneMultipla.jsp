<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione fascicoli">

    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />

<html:form action="/page/ricercaPerFascicolazioneMultipla.do">
<div class="sezione">
	<span class="title"><strong><bean:message key="fascicolo.datiricerca"/></strong></span>
	<jsp:include page="/WEB-INF/subpages/fascicolo/cerca.jsp" />
</div>
<logic:notEmpty name="ricercaFascicoliForm" property="fascicoli">
<div class="sezione">
	<span class="title"><strong><bean:message key="fascicolo.fascicoli"/></strong></span>
	<jsp:include page="/WEB-INF/subpages/fascicolo/lista.jsp" />
</div>
</logic:notEmpty>
</html:form>
</eprot:page>


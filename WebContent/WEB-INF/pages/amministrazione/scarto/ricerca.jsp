<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Archivio scarto protocolli">

<html:form action="/page/amministrazione/archivioScarto.do">

<div id="protocollo-errori">
	<jsp:include page="/WEB-INF/subpages/amministrazione/common/errori.jsp" />
</div>

<div class="sezione">
	<span class="title"><strong><bean:message key="amministrazione.scarto.datigenerali"/></strong></span>
	<jsp:include page="/WEB-INF/subpages/protocollo/ricerca/datiProtocollo.jsp" />
</div>

<div id="bottoni_salva">
<html:submit styleClass="submit" property="cerca" value="Cerca" alt="Ricerca in archivio scarto protocolli" />
<html:submit styleClass="submit" property="annulla" value="Annulla" alt="Ripulisce il form" />
</div>

<logic:notEmpty name="archivioScartoProtocolliForm" property="protocolliCollection">
<div class="sezione">	
	<span class="title"><strong><bean:message key="amministrazione.scarto.archivioscarto"/></strong></span>
	<jsp:include page="/WEB-INF/subpages/amministrazione/scarto/protocolliScartati.jsp" />
</div>
</logic:notEmpty>
</html:form>

</eprot:page>

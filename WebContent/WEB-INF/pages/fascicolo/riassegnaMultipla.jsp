<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />

<eprot:page title="Riassegna fascicoli">

<html:form action="/page/riassegnaFascicoli.do">
<jsp:include page="/WEB-INF/subpages/fascicolo/errori.jsp" />
<span style="color: #00f"><strong>Seleziona il nuovo referente dei fascicoli</strong></span>:
<table summary="">
	<jsp:include page="/WEB-INF/subpages/fascicolo/destinatariRiassegnazione.jsp" />
</table>
<div>
	<html:submit styleClass="submit" property="riassegnaAction" value="Riassegna i Fascicoli" alt="Riassegna i Fascicoli" />
	<html:submit styleClass="submit" property="indietroAction" value="Indietro" alt="Annulla l'operazione" />
</div>

</html:form>

</eprot:page>
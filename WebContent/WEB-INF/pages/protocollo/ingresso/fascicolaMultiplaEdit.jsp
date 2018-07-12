<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Fascicolazione Multipla Documenti in Ingresso">
<html:form action="/page/protocollo/ingresso/fascicolaMultiplaEdit.do" enctype="multipart/form-data">

<div id="protocollo">
    <jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />
<br class="hidden" />

<div id="sezioni-protocolli" class="sezione">
	<span class="title">
	<strong>Fascicoli</strong>
	</span>
<jsp:include page="/WEB-INF/subpages/protocollo/common/datiFascicoli.jsp" />
</div>

<br class="hidden" />

<div>

	<html:submit styleClass="submit" property="fascicolazioneMultiplaAction" value="Salva" alt="Salva protocollo" />
	<html:submit styleClass="submit" property="annullaAction" value="Annulla" alt="Annulla l'operazione" />
	<html:submit styleClass="submit" property="indietroAction" value="Indietro" alt="Torna ai protocolli in lavorazione" />
	

</div>

</div>

</html:form>

</eprot:page>
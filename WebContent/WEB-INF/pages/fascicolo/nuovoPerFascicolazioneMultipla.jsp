<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione fascicoli">
<html:form action="/page/nuovoPerFascicolazioneMultipla.do" enctype="multipart/form-data">

<div id="protocollo">

    <jsp:include page="/WEB-INF/subpages/fascicolo/errori.jsp" />


<br class="hidden" />
<html:hidden property="id" />
<jsp:include page="/WEB-INF/subpages/fascicolo/fascicoloView.jsp" />


<br class="hidden" />


</div>

</html:form>
</eprot:page>

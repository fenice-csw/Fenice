<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Cerca Soggetto">



<html:form action="/page/protocollo/anagrafica/persona-fisica/cerca.do">
<jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />

<div id="cerca">
	<jsp:include page="/WEB-INF/subpages/protocollo/anagrafica/persona-fisica/cerca.jsp" />
</div>
<hr />
<div id="elenco">
	<jsp:include page="/WEB-INF/subpages/protocollo/anagrafica/persona-fisica/elenco.jsp" />
</div>

</html:form>

</eprot:page>
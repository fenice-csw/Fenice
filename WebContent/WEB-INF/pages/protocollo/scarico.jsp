<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Scarico Protocollo">


<html:form action="/page/protocollo/scarico.do" focus="numeroProtocolloDa">
<div>
	<jsp:include page="/WEB-INF/subpages/protocollo/scarico/cerca.jsp" />
	<hr></hr>
</div>
<div>
	<jsp:include page="/WEB-INF/subpages/protocollo/scarico/lista.jsp" />
</div>


</html:form>

</eprot:page>





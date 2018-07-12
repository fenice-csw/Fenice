<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />

<jsp:include page="/WEB-INF/subpages/protocollo/common/uffici.jsp" />

<br />


<logic:notEmpty name="protocolloForm" property="mittente">
<span>
	<bean:message key="protocollo.mittente"/>: <strong>
    <bean:write name="protocolloForm" property="mittente.descrizioneUfficio"/>
    <bean:write name="protocolloForm" property="mittente.nomeUtente"/>
    </strong>
</span>
<br />
</logic:notEmpty>


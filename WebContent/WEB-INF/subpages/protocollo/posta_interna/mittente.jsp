<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<!--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>-->
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<eprot:ifAuthorized permission="change_sender">
	<jsp:include page="/WEB-INF/subpages/protocollo/posta_interna/ufficiMittente.jsp" />
</eprot:ifAuthorized>

<br />


<logic:notEmpty name="protocolloForm" property="mittente">
<span><bean:message key="protocollo.mittente"/>: <strong>
    <bean:write name="protocolloForm" property="mittente.descrizioneUfficio"/>
    <bean:write name="protocolloForm" property="mittente.nomeUtente"/>
    </strong></span>
<br />
</logic:notEmpty>


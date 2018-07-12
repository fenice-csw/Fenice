<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />


<jsp:include page="/WEB-INF/subpages/amm_trasparente/uffici_responsabile.jsp" />

<br />


<logic:notEmpty name="ammTrasparenteForm" property="responsabile">
<span><strong>
    <bean:write name="ammTrasparenteForm" property="responsabile.descrizioneUfficio"/>
    <bean:write name="ammTrasparenteForm" property="responsabile.nomeUtente"/>
    </strong></span>
<br />
</logic:notEmpty>


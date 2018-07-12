<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<!--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>-->
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />

<logic:notEmpty name="visualizzaProtocolloForm" property="mittenteUscita">
<span>
	<strong>
	<bean:write name="visualizzaProtocolloForm" property="mittenteUscita.descrizioneUfficio"/>
	<bean:write name="visualizzaProtocolloForm" property="mittenteUscita.nomeUtente"/>
	</strong>
</span>
<br/>
</logic:notEmpty>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<p>
	<span><bean:message key="protocollo.titolario.argomento"/>: </span>
	<span>
		<strong>
			<logic:notEmpty name="protocolloForm" property="titolario" >
			        <bean:write name="protocolloForm" property="titolario.descrizione" />
			</logic:notEmpty>
		</strong>
	</span>
</p>

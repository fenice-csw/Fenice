<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<div>  
<logic:notEmpty name="ricercaForm" property="argomenti">
<table>
<tr>
    <td><bean:message key="protocollo.argomento.codice"/></td>
    <td><bean:message key="protocollo.argomento.descrizione"/></td>
</tr>
<logic:iterate id="currentRecord" property="argomenti" name="ricercaForm">
<tr>
    <td>
   	<html:link action="/page/protocollo/ricerca.do" paramName="currentRecord" paramId="parArgomento" paramProperty="id">
	<bean:write name="currentRecord" property="codTitolario"/>
	</html:link >
	</td>
    <td>
	<bean:write name="currentRecord" property="descrizioneTitolario"/>
	</td>
</tr>	
</logic:iterate>     
</table>
</logic:notEmpty>    
</div>


<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<div>  
<logic:messagesPresent property="recordNotFound" message="true">
<bean:message key="report.messaggio1"/>.
</logic:messagesPresent>
<logic:messagesNotPresent property="recordNotFound" message="true">
<logic:notEmpty name="reportScaricatiForm" property="reportFormatsCollection">
<bean:message key="report.messaggio2"/>:
<br />
<ul>
<logic:iterate id="currentRecord" name="reportScaricatiForm" property="reportFormatsCollection" >
  <li>
  <html:link action="/page/stampa/prn-pro/scaricati" name="currentRecord" property="parameters" target="_blank">
	<input type="button" class="submit" value='<bean:write name="currentRecord" property="descReport"/>'></input>
  </html:link >
  </li>
</logic:iterate>
</ul>
</logic:notEmpty>
</logic:messagesNotPresent>
</div>


<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<div>  
<logic:messagesPresent property="recordNotFound" message="true">
<bean:message key="report.messaggio1"/>.
</logic:messagesPresent>
<logic:messagesNotPresent property="recordNotFound" message="true">
<logic:notEmpty name="reportDaScartareForm" property="reportFormatsCollection">
<bean:message key="report.messaggio2"/>:
<br />
<logic:iterate id="currentRecord" name="reportDaScartareForm" property="reportFormatsCollection" >
  <html:link action="/page/stampa/prn-pro/daScartare" name="currentRecord" property="parameters" target="_blank">
	<input type="button" class="submit" value='<bean:write name="currentRecord" property="descReport"/>'></input>
  </html:link >
  
</logic:iterate>     
</logic:notEmpty>  
</logic:messagesNotPresent>
</div>

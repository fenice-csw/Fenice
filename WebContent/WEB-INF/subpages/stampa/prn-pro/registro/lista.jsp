<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<div>  
<logic:messagesPresent property="recordNotFound" message="true">
<bean:message key="report.messaggio1"/>.
</logic:messagesPresent>
<logic:notEmpty name="reportRegistroForm" property="reportFormatsCollection">
<bean:message key="report.messaggio2"/>:
<br />
<ul>
<logic:iterate id="currentRecord" name="reportRegistroForm" property="reportFormatsCollection" >
  <li>
  <input type="button" class="submit" onclick="location.href=this.parentNode.getElementsByTagName ('a').item(0).href" value='<bean:write name="currentRecord" property="descReport"/>'/>
  	<html:link action="/page/stampa/prn-pro/registro" name="currentRecord" property="parameters" target="_blank" style="display:none"  /> 
  
  </li>
</logic:iterate>
</ul>
</logic:notEmpty>  

</div>


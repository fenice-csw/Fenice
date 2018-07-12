<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<div>  
<logic:messagesPresent property="recordNotFound" message="true">
Nessun dato da visualizzare.
</logic:messagesPresent>
<logic:notEmpty name="reportForm" property="reportFormatsCollection">
Selezionare il formato del Report:
<br />
<logic:iterate id="currentRecord" name="reportForm" property="reportFormatsCollection" >
  <html:link action="/page/stampa/prn-pro/etichetteDestinatari" name="currentRecord" property="parameters">
	<bean:write name="currentRecord" property="descReport"/>
  </html:link>
  <br />
  <br />
</logic:iterate>     
</logic:notEmpty>  
</div>

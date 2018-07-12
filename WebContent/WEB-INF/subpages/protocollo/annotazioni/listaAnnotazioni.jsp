<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<div id = "corpo">  
<logic:notEmpty name="annotazioneProtocolloForm" property="annotazioniCollection">
<TABLE class="centrale">
<tr>
    <td><bean:message key="protocollo.annotazioni.codice"/></td>
    <td><bean:message key="protocollo.annotazioni.descrizione"/></td>
	<td><bean:message key="protocollo.annotazioni.codiceutente"/></td>
    <td><bean:message key="protocollo.annotazioni.data"/></td> 	
</tr>
<logic:iterate id="currentRecord" name="annotazioneProtocolloForm" property="annotazioniCollection">
<tr>
	<td><html:multibox property="checkAnnotazione"><bean:write name="currentRecord" property="fkProtocollo"/>_<bean:write name="currentRecord" property="codiceAnnotazione"/>/<bean:write name="currentRecord" property="dataAnnotazione"/> (<bean:write name="currentRecord" property="codiceUserId"/>)</html:multibox>
	<bean:write name="currentRecord" property="codiceAnnotazione"/></td>
	<td><bean:write name="currentRecord" property="descrizione"/></td>
	<td><bean:write name="currentRecord" property="codiceUserId"/></td>
	<td><bean:write name="currentRecord" property="dataAnnotazione"/></td>   
</tr>
</logic:iterate>     
</TABLE>
<p>
<html:submit property="btnAnnulla" value="Annulla" alt="Annulla"/>
</p>
</logic:notEmpty>    
</div>
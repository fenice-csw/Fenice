<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" property="dataInizio"/>
  <html:errors bundle="bundleErroriProtocollo" property="dataFine"/>
</div>
<table>
<tr>
	<td colspan="6">
		<jsp:include page="../uffici.jsp" />
	</td>
</tr>
<tr>
	<td>
		<label for="dataInizio">
			<bean:message key="report.datainizio"/>
		</label>
	</td>
	<td>
		<html:text property="dataInizio" size="10" styleId="dataInizio" styleClass="obbligatorio" maxlength="10" />
		    <eprot:calendar textField="dataInizio" />&nbsp;
	 </td>
	 <td>
	   	<label for="dataFine">
			<bean:message key="report.datafine"/>
		</label>
	</td>
	<td>	
		<html:text property="dataFine" styleClass="obbligatorio" styleId="dataFine" size="10" maxlength="10" />
		   <eprot:calendar textField="dataFine" />&nbsp;
	 </td>
	 <td>  
		<html:submit styleClass="submit" property="Cerca" value="Visualizza" alt="Visualizza statistiche protocolli"/>
	 </td>
	 <td>
		<html:submit styleClass="submit" property="btnStampa" value="Stampa" alt="Stampa statistiche protocolli"/>
	 </td>
</tr>	
</table>
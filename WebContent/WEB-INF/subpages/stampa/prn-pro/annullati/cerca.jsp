<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:xhtml />
<div id="protocollo-errori"><html:errors
	bundle="bundleErroriProtocollo" /></div>
<div>
<table>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	
	<tr>
		<td></td>
		<td><jsp:include page="uffici.jsp" /><br />
		</td>
	</tr>
	<tr>
		<td><html:submit styleClass="submit" property="btnStampa"
			value="Stampa" alt="Stampa registro" /></td>
	</tr>
</table>
</div>

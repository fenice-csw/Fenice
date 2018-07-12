<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<div class="sezione"><span class="title"><strong><bean:message
	key="protocollo.estremiautorizzazione" /></strong></span>
<table>
<tr>
	<td class="label">
		<label for="provvedimentoAnnullamento">
		<bean:message key="protocollo.estremiautorizzazione"/>
		<span class="obbligatorio"> * </span>:</label>
	</td>
	<td>
		<html:textarea  styleClass="obbligatorio" property="estremiAutorizzazione" rows="4" cols="60%"></html:textarea>	
	</td>
</tr>
</table>
</div>
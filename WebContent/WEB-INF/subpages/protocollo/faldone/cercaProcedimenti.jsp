<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<table summary="">
	<tr>
		<td class="label">
			<label for="cercaProcedimentoNome"><bean:message key="protocollo.faldone.procedimento.oggetto"/>:</label>
			<html:text property="cercaProcedimentoNome" styleId="cercaProcedimentoNome" size="50" maxlength="100"></html:text>&nbsp;
		</td>
		<td>				
			<html:submit styleClass="submit" property="btnCercaProcedimentiDaFaldoni" value="Cerca" title="Cerca Procedimenti"/>
		</td>
	</tr>
	
</table>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<table summary="">
	<tr>
		<td class="label">
			<label for="cercaFascicoloNome"><bean:message key="protocollo.fascicolo.oggettofascicolo"/>:</label>
			<html:text property="cercaFascicoloNome" styleId="cercaFascicoloNome" size="50" maxlength="100"></html:text>&nbsp;
		</td>
		<td>				
			<html:submit styleClass="submit" property="btnCercaFascicoliDaProcedimento" value="Cerca" title="Cerca Fascicoli"/>
		</td>
	</tr>
</table>

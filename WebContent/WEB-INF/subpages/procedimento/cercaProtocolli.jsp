<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<table summary="">
	<tr>
		<td>		
			<label for="cercaProtocolloOggetto"><bean:message key="procedimento.protocolli.oggetto"/>:</label>
			<html:text property="cercaProtocolloOggetto" styleId="cercaProtocolloOggetto" size="40" maxlength="100"></html:text>
		</td>
		<td>
			<html:submit styleClass="submit" property="btnCercaProtocolliDaProcedimento" value="Cerca" title="Cerca Protocolli"/>        
        </td>
	</tr>
</table>
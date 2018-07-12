<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<div id="cerca">
<table>

<tr >
    <td>
		<label for="descrizione"><bean:message key="soggetto.lista.denominazione" />:</label>
		<html:text property="descrizione" styleId="descrizione" size="30" maxlength="100" />
		<eprot:ifAuthorized permission="130">
			<html:submit styleClass ="submit" property="nuovaAction" value="Nuova" alt="Nuova" />
		</eprot:ifAuthorized>
		<html:submit styleClass ="submit" property="cercaAction" value="Cerca" alt="Cerca" />
    </td>
    
</tr>
</table>
</div>


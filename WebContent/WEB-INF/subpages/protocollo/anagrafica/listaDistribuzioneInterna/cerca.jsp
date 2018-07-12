<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
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
		<html:submit styleClass ="submit" property="cercaAction" value="Cerca" alt="Cerca" />
		<eprot:ifAuthorized permission="new_ld_int">
			<html:submit styleClass ="submit" property="nuovaAction" value="Nuova" alt="Nuova" />
		</eprot:ifAuthorized>
    </td>
    
</tr>
</table>
</div>


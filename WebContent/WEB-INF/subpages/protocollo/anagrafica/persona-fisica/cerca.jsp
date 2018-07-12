<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<table>
  <tr>
    <th><label for="cognome"><bean:message key="soggetto.fisica.cognome"/>:</label></th>
    <th><label for="nome"><bean:message key="soggetto.fisica.nome"/>:</label></th>
    <th><label for="soggetto.indirizzo.comune"><bean:message key="soggetto.localita" />:</label> </th>
    </tr>
  <tr>
    <td><html:text property="cognome" styleId="cognome" size="30" maxlength="100" /></td>
    <td><html:text property="nome" styleId="nome" size="25" maxlength="40" /></td>
	<td><html:text property="soggetto.indirizzo.comune" styleId="soggetto.indirizzo.comune" size="30" maxlength="100"/></td>
  </tr>
  <tr>
    <td>
		<html:submit styleClass ="submit" property="cerca" value="Cerca" alt="Cerca" />
		
		<logic:equal name="personaFisicaForm" property="indietroVisibile" value="true" >
			<html:submit styleClass ="submit" property="indietroPF" value="Indietro" alt="Indietro" />
		</logic:equal>
		
		<eprot:ifAuthorized permission="45">
			<html:submit styleClass ="submit" property="nuovaPF" value="Nuovo" alt="Nuovo" />
		</eprot:ifAuthorized>
	</td>
  </tr>
</table>




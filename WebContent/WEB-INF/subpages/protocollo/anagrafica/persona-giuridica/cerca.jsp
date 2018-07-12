<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<div id="cerca">
<table>
<tr >
	<th><label for="descrizioneDitta"><bean:message key="soggetto.giuridica.denominazione" />:</label></th>
	<th><label for="partitaIva"><bean:message key="soggetto.giuridica.piva" />:</label> </th>
	<th><label for="soggetto.indirizzo.comune"><bean:message key="soggetto.localita" />:</label> </th>
</tr> 
<tr >  
    <td><html:text property="descrizioneDitta" styleId="descrizioneDitta" size="20" maxlength="100" /></td>
	<td><html:text property="partitaIva" styleId="partitaIva" size="16" maxlength="16"/>&nbsp;</td>
	<td><html:text property="soggetto.indirizzo.comune" styleId="soggetto.indirizzo.comune" size="30" maxlength="100"/></td>
    </tr>
  <tr>
    <td>
		<html:submit styleClass ="submit" property="cerca" value="Cerca" alt="Cerca" />
		<logic:equal name="personaGiuridicaForm" property="indietroVisibile" value="true" >
		<html:submit styleClass ="submit" property="indietroPG" value="Indietro" alt="Indietro" />
		</logic:equal>
		<eprot:ifAuthorized permission="47">
			<html:submit styleClass ="submit" property="nuovaPG" value="Nuovo" alt="Nuovo" />
		</eprot:ifAuthorized>
	</td>
</tr>
</table>
</div>


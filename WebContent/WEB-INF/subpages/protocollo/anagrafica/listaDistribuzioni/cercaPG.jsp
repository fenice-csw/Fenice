<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<div id="cerca">
<table>
<tr >
    <td>
		<span><bean:message key="soggetto.lista.personagiuridica" />:</span>
		<html:text property="descrizioneDitta" size="30" maxlength="100" />
    </td>
    <td>
	    <span><bean:message key="soggetto.lista.piva" />:</span>  
 	     	<html:text property="partitaIva" size="16" maxlength="16"/>&nbsp;
		<html:submit styleClass ="submit" property="cercaPG" value="Cerca" alt="Cerca" />
		<html:submit styleClass ="submit" property="indietroPG" value="Indietro" alt="Indietro" />
	</td>
</tr>
</table>
</div>


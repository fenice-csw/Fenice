<%@ page contentType="text/html;charset=iso-8859-1" language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<div id="cerca">
<table>

<tr >
    <td>
		<span><bean:message key="soggetto.fisica.cognome" />:</span>
		<html:text property="cognome" size="30" maxlength="100" />
    </td>
    <td>
	    <span><bean:message key="soggetto.fisica.nome" />:</span>  
 	     	<html:text property="nome" size="16" maxlength="16"/>&nbsp;
	<html:submit styleClass ="submit" property="cercaPF" value="Cerca" alt="Cerca" />
	<html:submit styleClass ="submit" property="indietroPF" value="Indietro" alt="Indietro" />
	</td>
	
	
	
	
</tr>
</table>
</div>


<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<div id="cerca">
<table>
<tr >
    <td>
		<label for="descrizione"><bean:message key="amministrazione.tipoProcedimento.descrizione" />:</label>
		<html:text property="descrizione" styleId="descrizione" size="30" maxlength="100" />
		<html:submit styleClass ="submit" property="cercaAction" value="Cerca" alt="Cerca" />
		<html:submit styleClass="submit" property="btnNuovoTipoProcedimento" value="Nuovo" title="Inserisce un nuovo tipo di procedimento"/>
    </td>
    
</tr>
</table>
</div>


<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<div class="sezione">
<span class="title"><strong><bean:message key="fascicolo.fascicolo"/></strong></span>
<table summary="">
	<tr>
		<td class="label">
			<label for="nomeFascicolo"><bean:message key="fascicolo.nomefascicolo"/>:</label>
			<html:hidden property="fascicolo.id" />
			<html:text property="fascicolo.nome" size="50" maxlength="100"></html:text>&nbsp;
		</td>
		<td>				
			<html:submit styleClass="submit" property="btnCercaFascicoli" value="Cerca" title="Cerca Fascicoli"/>
			<html:submit styleClass="submit" property="btnNuovoFascicolo" value="Nuovo" title="Nuovo Fascicolo"/>
		</td>
	</tr>
	<logic:notEmpty name="documentoForm" property="fascicoliDocumento">
	<tr>
		<td>
			<label for="nomeFascicolo"><bean:message key="fascicolo.associato"/>:</label>
		</td>
	</tr>
	<tr>		
		<td>
		<logic:iterate id="currentRecord" property="fascicoliDocumento" name="documentoForm">
		<html:multibox property="fascicoloSelezionatoId"><bean:write name="currentRecord" property="id" /></html:multibox>
		<span><bean:write name="currentRecord" property="annoProgressivo"/> - <bean:write name="currentRecord" property="oggetto"/></span><br/>
		</logic:iterate>
		<html:submit styleClass="submit" property="rimuoviFascicoli" value="Rimuovi" title="Rimuovi i fascicoli selezionati"/>
		</td>
	</tr>
	</logic:notEmpty>
</table>
</div>
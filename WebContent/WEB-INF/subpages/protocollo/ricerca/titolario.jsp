<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<table summary="">
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.ricerca.titolario.argomento"/>:</span>
    </td>
    <td colspan="2">
      
		<logic:notEmpty name="ricercaForm" property="titolario">
			<span>
				<strong>
					<bean:write name="ricercaForm" property="titolario.path" /> - <bean:write name="ricercaForm" property="titolario.descrizione" />
				</strong>
			</span>
		</logic:notEmpty>
		<html:hidden property="titolarioPrecedenteId" />
		
		<logic:notEmpty name="ricercaForm" property="titolario" >
		      <html:submit styleClass="button" property="titolarioPrecedenteAction" value=".." title="Imposta il titolario precedente come corrente" />
		</logic:notEmpty>
		

    </td>
  </tr>
  

<logic:notEmpty name="ricercaForm" property="titolariFigli">
  <tr>
    <td class="label">
      <label for="titolarioSelezionatoId"><bean:message key="protocollo.ricerca.titolario"/>:</label>
    </td>
    <td>
      <html:select property="titolarioSelezionatoId">
		<logic:iterate id="tit" name="ricercaForm" property="titolariFigli">
        	<option value='<bean:write name="tit" property="id"/>'>
        		<bean:write name="tit" property="path"/> - <bean:write name="tit" property="descrizione" />
        	</option>
		</logic:iterate>
      </html:select>
    </td>
    <td>
      <html:submit styleClass="button" property="impostaTitolarioAction" value="Imposta" title="Imposta il titolario selezionato come corrente" />
    </td>
  </tr>
</logic:notEmpty>  

</table>

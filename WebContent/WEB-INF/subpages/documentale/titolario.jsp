<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<logic:greaterThan name="documentoForm" property="documentoId" value="0">
<table summary="">
  <tr>
    <td class="label">
      <span><bean:message key="documentale.argomento"/>:</span>
    </td>
    <td colspan="2">
      <span><strong>

		<logic:notEmpty name="documentoForm" property="titolario">

        	<bean:write name="documentoForm" property="titolario.path" /> - <bean:write name="documentoForm" property="titolario.descrizione" /> 
		</logic:notEmpty>        

      </strong></span>
      <html:hidden property="titolarioPrecedenteId" />

		<logic:notEmpty name="documentoForm" property="titolario">
		      <html:submit styleClass="button" property="titolarioPrecedenteAction" value=".." title="Imposta il titolario precedente come corrente" />
		</logic:notEmpty>

    </td>
  </tr>
  

<logic:notEmpty name="documentoForm" property="titolariFigli">
  <tr>
    <td class="label">
      <label for="titolarioSelezionatoId"><bean:message key="documentale.titolario"/>:</label>
    </td>
    <td>
      <html:select property="titolarioSelezionatoId">
      
		<logic:iterate id="tit" name="documentoForm" property="titolariFigli">
        	<option value="<bean:write name='tit' property='id'/>"><bean:write name="tit" property="codice" /> - <bean:write name="tit" property="descrizione"/></option>
        </logic:iterate>	
      </html:select>
    </td>
    <td>
      <html:submit styleClass="button" property="impostaTitolarioAction" value="Imposta" title="Imposta il titolario selezionato come corrente" />
    </td>
  </tr>
</logic:notEmpty>  

</table>
</logic:greaterThan>



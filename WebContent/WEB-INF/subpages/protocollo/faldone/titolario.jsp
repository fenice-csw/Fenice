<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<table summary="">
  <tr>
    <td>
      <logic:equal name="faldoneForm" property="dipendenzaTitolarioUfficio" value="1">
      <label for="titolarioSelezionatoId"><bean:message key="protocollo.titolario.argomento"/></label><span class="obbligatorio"> * </span>:
      </logic:equal>
            <logic:equal name="faldoneForm" property="dipendenzaTitolarioUfficio" value="0">
      <label for="titolarioSelezionatoId"><bean:message key="protocollo.titolario.argomento"/></label><span>  </span>:
      </logic:equal>
    </td>
    <td colspan="2">
      <span><strong>
<logic:notEmpty name="faldoneForm" property="titolario">
        <bean:write name="faldoneForm" property="titolario.descrizione"/>
</logic:notEmpty>        
      </strong></span>
      <html:hidden property="titolarioPrecedenteId" />

<logic:notEmpty name="faldoneForm" property="titolario">
      <html:submit styleClass="button" property="titolarioPrecedenteAction" value=".." title="Imposta il titolario precedente come corrente" />
</logic:notEmpty>      

    </td>
  </tr>
  
<logic:equal name="faldoneForm" property="dipendenzaTitolarioUfficio" value="1">
<logic:notEmpty name="faldoneForm" property="titolariFigli">
  <tr>
    <td colspan="2">
      <html:select property="titolarioSelezionatoId" styleId="titolarioSelezionatoId" styleClass="obbligatorio">
		<logic:iterate id="tit" name="faldoneForm" property="titolariFigli">
        	<option value='<bean:write name="tit" property="id" />'><bean:write name="tit" property="codice"/> - <bean:write name="tit" property="descrizione"/></option>
		</logic:iterate>
      </html:select>
    </td>
    <td>
      <html:submit styleClass="button" property="impostaTitolarioAction" value="Imposta" title="Imposta il titolario selezionato come corrente" />
    </td>
  </tr>

</logic:notEmpty>
</logic:equal>
<logic:equal name="faldoneForm" property="dipendenzaTitolarioUfficio" value="0">
<logic:notEmpty name="faldoneForm" property="titolariFigli">
  <tr>
    <td colspan="2">
      <html:select property="titolarioSelezionatoId" styleId="titolarioSelezionatoId" >
		<logic:iterate id="tit" name="faldoneForm" property="titolariFigli">
        	<option value='<bean:write name="tit" property="id" />'><bean:write name="tit" property="codice"/> - <bean:write name="tit" property="descrizione"/></option>
		</logic:iterate>
      </html:select>
    </td>
    <td>
      <html:submit styleClass="button" property="impostaTitolarioAction" value="Imposta" title="Imposta il titolario selezionato come corrente" />
    </td>
  </tr>

</logic:notEmpty>
</logic:equal>
</table>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />

<table summary="">

  <tr>
    <td class="label">
      <span><bean:message key="protocollo.ricerca.assegnatario.ufficioCorrente"/> : <html:hidden property="ufficioCorrenteId" /></span>
    </td>
    <td>
    <logic:notEmpty name="configurazioneForm" property="ufficioCorrentePath">
	    <bean:define id="ufficioCorrentePath" name="configurazioneForm" property="ufficioCorrentePath"/>
			<span title='<bean:write name="configurazioneForm" property="ufficioCorrentePath" />'> <strong>
				<bean:write name="configurazioneForm" property="ufficioCorrente.description" />
			</strong></span>
	</logic:notEmpty>	
      <html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
    </td>
  </tr>

<logic:notEmpty name="configurazioneForm" property="ufficiDipendenti">
  <tr>
    <td class="label">
      <label for="ufficioSelezionatoId"><bean:message key="protocollo.ricerca.assegnatario.ufficio"/> :</label>
    </td>
    <td>
 	    <html:select property="ufficioSelezionatoId">
			<logic:iterate id="ufficio" name="configurazioneForm" property="ufficiDipendenti">
		        <option value='<bean:write name="ufficio" property="id"/>'>
		        <bean:write name="ufficio" property="description"/></option>
			</logic:iterate>
 	    </html:select>
		<html:submit styleClass="button" property="impostaUfficioAction" value="Vai" title="Imposta l'ufficio come corrente" />
    </td>
  </tr>
</logic:notEmpty>


<logic:notEmpty name="configurazioneForm" property="utenti">	
  <tr>
    <td class="label">
      <label for="utenteSelezionatoId"><bean:message key="protocollo.ricerca.assegnatario.utente"/> :</label>
    </td>
    <td>
 	    <html:select property="utenteSelezionatoId" >
 	    	<option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
			<html:optionsCollection property="utenti" value="id" label="fullName" />
 	    </html:select>
    </td>
  </tr>
</logic:notEmpty>  

</table>

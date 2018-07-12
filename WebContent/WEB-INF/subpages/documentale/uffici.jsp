<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>


<html:xhtml />

<table summary="">
  <tr>
    <td class="label">
      <span><bean:message key="documentale.ufficiocorrente"/>: <html:hidden property="ufficioCorrenteId" /></span>
    </td>
    <td>
		<bean:define id="ufficioCorrentePath" name="documentoForm" property="ufficioCorrentePath"/>
		<span title="<bean:write name='documentoForm' property='ufficioCorrentePath'/>"><strong>
			<bean:write name='documentoForm' property='ufficioCorrente.description' />
		</strong></span>
      <html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
    </td>
    <td>
      <html:submit styleClass="button" property="assegnaUfficioCorrenteAction" value="Assegna" title="Assegna l'ufficio corrente" />
    </td>
  </tr>


<logic:notEmpty name="documentoForm" property="ufficiDipendenti">
  <tr>
    <td class="label">
      <label for="ufficioSelezionatoId"><bean:message key="documentale.ufficio"/>:</label>
    </td>
    <td>
 	    <html:select property="ufficioSelezionatoId">
			<logic:iterate id="ufficio" name="documentoForm" property="ufficiDipendenti">
			        <bean:define id="id" name="ufficio" property="id" />
			        <option value="<bean:write name='ufficio' property='id'/>"><bean:write name="ufficio" property="nomeUfficio"/></option>
			</logic:iterate>
 	    </html:select>
      <html:submit styleClass="button" property="impostaUfficioAction" value="Vai" title="Imposta l'ufficio come corrente" />
    </td>
    <td>
      <html:submit styleClass="button" property="assegnaUfficioSelezionatoAction" value="Assegna" title="Assegna l'ufficio selezionato" />
    </td>
  </tr>
</logic:notEmpty>  




<logic:notEmpty name="documentoForm" property="utenti">
  <tr>
    <td class="label">
      <label for="utenteSelezionatoId"><bean:message key="documentale.utente"/>:</label>
    </td>
    <td>
 	    <html:select property="utenteSelezionatoId" >
			<logic:iterate id="utente" name="documentoForm" property="utenti" >
					<bean:define id="idUt" name="utente" property="id" />
			        <option value="<bean:write name='utente' property='id'/>"><bean:write name="utente" property="fullName"/></option>
			</logic:iterate>
 	    </html:select>
    </td>
    <td>
      <html:submit styleClass="button" property="assegnaUtenteAction" value="Assegna" title="Assegna l'utente" />
    </td>
  </tr>
</logic:notEmpty>  

</table>

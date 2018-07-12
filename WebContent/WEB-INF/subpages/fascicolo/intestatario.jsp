<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />

<table summary="">
  <tr>
    <td class="label">
      <span>Ufficio corrente : <html:hidden property="ufficioCorrenteId" /></span>
    </td>
    <td>
      <bean:define id="ufficioCorrentePath" name="fascicoloForm" property="ufficioCorrentePath" />
      <bean:define id="ufficioCorrenteDes" name="fascicoloForm" property="ufficioCorrente.description" /> 
      <span title="<bean:write name='fascicoloForm' property='ufficioCorrentePath'/>"><strong><bean:write name='fascicoloForm' property='ufficioCorrente.description'/></strong></span>
      <html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
    </td>
    <td>
      <html:submit styleClass="button" property="assegnaUfficioCorrenteAction" value="Imposta" title="Imposta l'ufficio corrente" />
    </td>
  </tr>


<logic:notEmpty name="fascicoloForm" property="ufficiDipendenti">
  <tr>
    <td class="label">
      <label for="ufficioSelezionatoId">Ufficio :</label>
    </td>
    <td>
 	    <html:select property="ufficioSelezionatoId">
		
<%--		<bean:define id="ufficioD" name="fascicoloForm" property="ufficiDipendenti"/>--%>
		<logic:iterate id="ufficioD" name="fascicoloForm" property="ufficiDipendenti">
						<bean:define id="id" name="ufficioD" property="id" />
		        <option value="<bean:write name="ufficioD" property="id" />"><bean:write name="ufficioD" property="description"/></option>
		</logic:iterate>

 	    </html:select>
      <html:submit styleClass="button" property="impostaUfficioAction" value="Vai" title="Imposta l'ufficio come corrente" />
    </td>
    <td>
      <html:submit styleClass="button" property="assegnaUfficioSelezionatoAction" value="Imposta" title="Imposta l'ufficio selezionato" />
    </td>
    
  </tr>
</logic:notEmpty>  



<logic:notEmpty name="fascicoloForm" property="utenti">
  <tr>
    <td class="label">
      <label for="utenteSelezionatoId">Utente :</label>
    </td>
    <td>
 	    <html:select property="utenteSelezionatoId" >
			<html:optionsCollection property="utenti" value="id" label="fullName" />
 	    </html:select>
    </td>
    <td>
      <html:submit styleClass="button" property="assegnaUtenteAction" value="Imposta" title="Imposta l'utente" />
    </td>
    
  </tr>
</logic:notEmpty>  

</table>
<br />
<logic:notEmpty name="fascicoloForm" property="mittente">
	<span>Referente: <strong>
	<bean:define id="mittente" name="fascicoloForm" property="mittente"/>
	<logic:notEmpty name="mittente" property="descrizioneUfficio">
	<bean:define id="descrizioneUfficio" name="mittente" property="descrizioneUfficio"/>
		<bean:write name="mittente" property="descrizioneUfficio"/>
	</logic:notEmpty>
	<logic:notEmpty name="mittente" property="nomeUtente">
	<bean:define id="nomeUtente" name="mittente" property="nomeUtente"/>
		<bean:write name="mittente" property="nomeUtente"/>
	</logic:notEmpty>

    </strong></span>
<br />
</logic:notEmpty>





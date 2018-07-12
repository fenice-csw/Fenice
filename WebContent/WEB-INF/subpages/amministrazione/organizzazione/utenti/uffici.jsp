<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html:xhtml />

<table summary="">
  <tr>
    <td class="label">
      <span><bean:message key="amministrazione.organizzazione.uffici.ufficiocorrente"/> : <html:hidden property="ufficioCorrenteId" /></span>
    </td>
    
    <td>
      <bean:define id="ufficioCorrentePath" name="profiloUtenteForm" property="ufficioCorrentePath" />
      <bean:define id="ufficioCorrente" name="profiloUtenteForm" property="ufficioCorrente" />	
      <span title="">
      	<strong>
      		<bean:write name="ufficioCorrente" property="description" />
      	</strong>
      </span>
      <html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
		<logic:greaterThan name="profiloUtenteForm" property="id" value="0">
			<html:submit styleClass="button" property="btnPermessiUffici" value="Gestione dei permessi" title="Associazione utente con uffici e funzioni"></html:submit>
		</logic:greaterThan>			
    </td>
    <td>
		<logic:equal name="profiloUtenteForm" property="id" value="0">
		      <html:submit styleClass="button" property="assegnaUfficioCorrenteAction" value="Assegna" title="Assegna l'ufficio corrente" />
		</logic:equal>      
    </td>
  </tr>

<logic:notEmpty name="profiloUtenteForm" property="ufficiDipendenti">
  <tr>
    <td class="label">
      <label for="ufficioSelezionatoId"><bean:message key="amministrazione.organizzazione.uffici.ufficio"/>:</label>
    </td>
    <td>
 	    <html:select property="ufficioSelezionatoId">
			
			
			<logic:iterate id="ufficio" name="profiloUtenteForm" property="ufficiDipendenti">
			   <bean:define id="ufficioId" name="ufficio" property="id"/>
			   <option value="<bean:write name='ufficio' property='id'/>"><bean:write name="ufficio" property="nomeUfficio"/>
			</logic:iterate>
 	    </html:select>
      <html:submit styleClass="button" property="impostaUfficioAction" value="Vai" title="Imposta l'ufficio come corrente" />
    </td>

	<logic:equal name="profiloUtenteForm" property="id" value="0">
    <td>
      <html:submit styleClass="button" property="assegnaUfficioSelezionatoAction" value="Assegna" title="Assegna l'ufficio selezionato" />
    </td>
	</logic:equal>    
  </tr>
</logic:notEmpty>  

</table>

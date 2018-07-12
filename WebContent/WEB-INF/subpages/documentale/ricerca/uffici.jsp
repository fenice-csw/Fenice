<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />

<table summary="">
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.ricerca.assegnatario.ufficioCorrente"/>: </span>
    </td>
    <td>    
		<html:select property="ufficioCorrenteId">
			<logic:equal name="documentoForm" property="tuttiUffici" value="true">
	    	    <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
	        </logic:equal>
	        <option value='<bean:write name="documentoForm" property="ufficioCorrenteId" />'>
				<bean:write name="documentoForm" property="ufficioCorrente.description" />
			</option>
	 	</html:select>
      	<html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
    </td>
  </tr>

<logic:notEmpty name="documentoForm" property="ufficiDipendenti">
	<tr>  
	    <td class="label">
	      <label for="ufficioSelezionatoId"><bean:message key="protocollo.ricerca.assegnatario.ufficio"/> :</label>
	    </td>
	    <td>
	 	    <html:select property="ufficioSelezionatoId">
		        <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
		        <bean:define id="ufficiDipendenti" name="documentoForm" property="ufficiDipendenti"/>
				<html:optionsCollection name="ufficiDipendenti" value="id" label="description" />
	 	    </html:select>
	      <html:submit styleClass="button" property="impostaUfficioAction" value="Imposta" title="Imposta l'ufficio come corrente" />
	    </td>
	 </tr> 
</logic:notEmpty>  

  <tr>
    <td class="label">
      <label for="utenteSelezionatoId"><bean:message key="protocollo.ricerca.assegnatario.utente"/> :</label>
    </td>
    <td>
		<logic:notEmpty name="documentoForm" property="utenti">
			<bean:define id="utenti" name="documentoForm" property="utenti"/>
		</logic:notEmpty>	
    	<html:select property="utenteSelezionatoId">
		        <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
		        <logic:notEmpty name="documentoForm" property="utenti">
					<html:optionsCollection name="utenti" value="id" label="fullName" />
				</logic:notEmpty>	
	 	</html:select>
    </td>
  </tr>
  
</table>
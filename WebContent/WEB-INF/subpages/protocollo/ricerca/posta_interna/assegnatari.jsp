<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />

<table summary="">
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.ricerca.assegnatario.ufficioCorrente"/>:</span>
    </td>
    <td>
		<html:select property="ufficioRicercaPIDestId">
			<logic:equal name="ricercaForm" property="tuttiUfficiAssegnatariPIDest" value="true">
		        <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
		    </logic:equal>    
			<option value='<bean:write name="ricercaForm" property="ufficioCorrentePIDest.id"/>'>
	        	<bean:write name="ricercaForm" property="ufficioCorrentePIDest.description" />
	        </option>
 	    </html:select>
      	<html:submit styleClass="button" property="ufficioPrecedenteActionPIDest" value=".." title="Vai al livello superiore" />
    </td>
  </tr>

<logic:notEqual name="ricercaForm" property="tipoUfficioRicerca" value="N">
	<logic:notEmpty name="ricercaForm" property="ufficiDipendentiPIDest">
	  <tr>
	    <td class="label">
	      <label for="ufficioSelezionatoPIDestId"><bean:message key="protocollo.ricerca.assegnatario.ufficio"/>:</label>
	    </td>
	    <td>
	 	    <logic:notEmpty name="ricercaForm" property="ufficiDipendentiPIDest" >	
	 	    	<bean:define id="ufficiDipendentiPIDest" name="ricercaForm" property="ufficiDipendentiPIDest"/>
	 	    </logic:notEmpty>
	 	    <html:select property="ufficioSelezionatoPIDestId">
	        	<option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
	        	<logic:notEmpty name="ricercaForm" property="ufficiDipendentiPIDest" >	
		        	<html:optionsCollection name="ufficiDipendentiPIDest" value="id" label="description" />
		        </logic:notEmpty>	
	 	    </html:select>
	      <html:submit styleClass="button" property="impostaUfficioActionPIDest" value="Vai" title="Imposta l'ufficio come corrente" />
	    </td>
	  </tr>
	</logic:notEmpty>
</logic:notEqual>  
  
  <tr>
    <td class="label">
      <label for="utenteSelezionatoPIDestId"><bean:message key="protocollo.ricerca.assegnatario.utente"/>:</label>
    </td>
    <td>
		<logic:notEmpty name="ricercaForm" property="utentiPIDest">
			<bean:define id="utenti" name="ricercaForm" property="utentiPIDest"/>
		</logic:notEmpty>	
    	<html:select property="utenteSelezionatoPIDestId">
		        <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
		        <logic:notEmpty name="ricercaForm" property="utentiPIDest">
					<html:optionsCollection name="utenti" value="id" label="fullName" />
				</logic:notEmpty>	
	 	</html:select>
    </td>
  </tr>
  
  
</table>

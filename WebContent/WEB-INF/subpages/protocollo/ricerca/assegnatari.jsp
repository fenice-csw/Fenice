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
		<html:select property="ufficioRicercaId">
			
		    <logic:equal name="ricercaForm" property="tuttiUfficiAssegnatari" value="true">
		        <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
		    </logic:equal>
		    
			<option value='<bean:write name="ricercaForm" property="ufficioCorrente.id"/>'>
	        	<bean:write name="ricercaForm" property="ufficioCorrente.description" />
	        </option>
 	    </html:select>
 	    <logic:equal name="ricercaForm" property="tipoUfficioRicerca" value="C">
      		<html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
   		</logic:equal>
    </td>
  </tr>


<logic:notEqual name="ricercaForm" property="tipoUfficioRicerca" value="N">
	<logic:notEmpty name="ricercaForm" property="ufficiDipendenti">
		<tr>
    	<td class="label">
      		<label for="ufficioSelezionatoId"><bean:message key="protocollo.ricerca.assegnatario.ufficio"/>:</label>
    	</td>
    	<td>
 	    	<logic:notEmpty name="ricercaForm" property="ufficiDipendenti" >	
 	    		<bean:define id="ufficiDipendenti" name="ricercaForm" property="ufficiDipendenti"/>
 	    	</logic:notEmpty>
 	    	<html:select property="ufficioSelezionatoId">
        		<option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
        		<logic:notEmpty name="ricercaForm" property="ufficiDipendenti" >	
	        		<html:optionsCollection name="ufficiDipendenti" value="id" label="description" />
	        	</logic:notEmpty>	
 	    	</html:select>
      		<html:submit styleClass="button" property="impostaUfficioAction" value="Vai" title="Imposta l'ufficio come corrente" />
    	</td>
 		</tr>
	</logic:notEmpty>  
</logic:notEqual>
  
  <tr>
    <td class="label">
      <label for="utenteSelezionatoId"><bean:message key="protocollo.ricerca.assegnatario.utente"/>:</label>
    </td>
    <td>
		<logic:notEmpty name="ricercaForm" property="utenti">
			<bean:define id="utenti" name="ricercaForm" property="utenti"/>
		</logic:notEmpty>	
    	<html:select property="utenteSelezionatoId">
		        <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
		        <logic:notEmpty name="ricercaForm" property="utenti">
					<html:optionsCollection name="utenti" value="id" label="caricaFullName" />
				</logic:notEmpty>	
	 	</html:select>
    </td>
  </tr>
  
  
</table>

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
		<html:select property="ufficioProtRicercaId">
			 
			<logic:equal name="ricercaForm" property="tuttiUffici" value="true">
		    	<option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
		    </logic:equal>
	        <option value='<bean:write name="ricercaForm" property="ufficioProtCorrenteId" />'>
				<bean:write name="ricercaForm" property="ufficioProtCorrente.description" />
			</option>
	 	</html:select>

      	<logic:equal name="ricercaForm" property="tipoUfficioRicerca" value="C">
      		<html:submit styleClass="button" property="ufficioProtPrecedenteAction" value=".." title="Vai al livello superiore" />
   		</logic:equal>
    </td>
  </tr>

<logic:notEqual name="ricercaForm" property="tipoUfficioRicerca" value="N">
	<logic:notEmpty name="ricercaForm" property="ufficiProtDipendenti">
		<tr>  
		    <td class="label">
		      <label for="ufficioProtSelezionatoId"><bean:message key="protocollo.ricerca.assegnatario.ufficio"/> :</label>
		    </td>
		    <td>
		 	    <html:select property="ufficioProtSelezionatoId">
			        <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
			        <bean:define id="ufficiProtDipendenti" name="ricercaForm" property="ufficiProtDipendenti"/>
					<html:optionsCollection name="ufficiProtDipendenti" value="id" label="description" />
		 	    </html:select>
		      <html:submit styleClass="button" property="impostaUfficioProtAction" value="Vai" title="Imposta l'ufficio come corrente" />
		    </td>
		 </tr> 
	</logic:notEmpty>  
</logic:notEqual>
  <tr>
    <td class="label">
      <label for="utenteProtSelezionatoId"><bean:message key="protocollo.ricerca.assegnatario.utente"/> :</label>
    </td>
    <td>
		<logic:notEmpty name="ricercaForm" property="utentiProt">
			<bean:define id="utentiProt" name="ricercaForm" property="utentiProt"/>
		</logic:notEmpty>	
    	<html:select property="utenteProtSelezionatoId">
		        <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
		        <logic:notEmpty name="ricercaForm" property="utentiProt">
					<html:optionsCollection name="utentiProt" value="id" label="caricaFullName" />
				</logic:notEmpty>	
	 	</html:select>
    </td>
  </tr>
  
</table>

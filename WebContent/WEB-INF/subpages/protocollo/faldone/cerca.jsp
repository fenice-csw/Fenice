<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<table summary="">

  <tr>
    <td>
      <span><bean:message key="protocollo.ricerca.assegnatario.ufficioCorrente"/>: </span>
    </td>
    <td>
		<html:select property="ufficioRicercaId">
			<logic:equal name="ricercaFaldoneForm" property="tuttiUffici" value="true">
	    	    <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
	        </logic:equal>
	        <option value='<bean:write name="ricercaFaldoneForm" property="ufficioCorrenteId" />'>
				<bean:write name="ricercaFaldoneForm" property="ufficioCorrente.description" />
			</option>
	 	</html:select>
      	<html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
    </td>
  </tr>

<logic:notEmpty name="ricercaFaldoneForm" property="ufficiDipendenti">
	<tr>  
	    <td>
	      <label for="ufficioSelezionatoId"><bean:message key="protocollo.ricerca.assegnatario.ufficio"/> :</label>
	    </td>
	    <td>
	 	    <html:select property="ufficioSelezionatoId">
		        <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
		        <bean:define id="ufficiDipendenti" name="ricercaFaldoneForm" property="ufficiDipendenti"/>
				<html:optionsCollection name="ufficiDipendenti" value="id" label="description" />
	 	    </html:select>
	      <html:submit styleClass="button" property="impostaUfficioAction" value="Vai" title="Imposta l'ufficio come corrente" />
	    </td>
	 </tr> 
</logic:notEmpty> 

	<tr>
		<td colspan="3">
		<%-- <div><jsp:include page="/WEB-INF/subpages/protocollo/faldone/cerca/uffici.jsp"/></div> --%>
		<div><jsp:include page="/WEB-INF/subpages/protocollo/faldone/cerca/titolario.jsp"/></div>
		</td>	
	</tr> 
	<tr>  
	    <td>
			<label for="anno"><bean:message key="protocollo.faldone.Anno"/> &nbsp;:</label>
		</td>
		<td colspan="2">
			<html:text name="ricercaFaldoneForm" property="anno" styleId="anno" size="6" maxlength="4"/>
	    </td>
	<tr>  
	<tr>  
	    <td>
			<label for="numero"><bean:message key="protocollo.faldone.numero"/> &nbsp;:</label>
		</td>
		<td colspan="2">
		<html:text name="ricercaFaldoneForm" property="numero" styleId="numero" size="10" maxlength="6"/>
	    </td>
	<tr> 
	<tr>
  		<td>
  		   	<label for="dataCreazioneInizio"><bean:message key="protocollo.faldone.datacreazione_Da"/>
				&nbsp;:
			</label>
		</td>
		<td colspan="2">
			<html:text name="ricercaFaldoneForm" property="dataCreazioneInizio" styleId="dataCreazioneInizio" size="10" maxlength="10" />
		    <eprot:calendar textField="dataCreazioneInizio" />
		</td>
	<tr> 
	<tr>
  		<td>
	    	<label for="dataCreazioneFine">
				<bean:message key="protocollo.faldone.datacreazione_a"/> &nbsp;:
			</label>
		</td>
		<td colspan="2">
			<html:text name="ricercaFaldoneForm" property="dataCreazioneFine" styleId="dataCreazioneFine" size="10" maxlength="10" />
			<eprot:calendar textField="dataCreazioneFine" />
		</td>   
	</tr>
	
 	<tr>
	    <td >
	      <label for="oggetto"><bean:message key="protocollo.faldone.oggetto"/> </label>&nbsp;:
	    </td>
		<td colspan="2">
	      <html:text name="ricercaFaldoneForm" property="oggetto" styleId="oggetto" size="60" maxlength="250" />
	    </td>
	</tr>
	<tr>
	    <td >
	      <label for="sottocategoria"><bean:message key="protocollo.faldone.sotto_categoria"/></label>&nbsp;:
	    </td>
		<td colspan="2">
	      <html:text name="ricercaFaldoneForm" property="sottocategoria" styleId="sottocategoria" size="20" maxlength="20" />
	    </td>
	</tr>
	<tr>
	    <td >
	      <label for="codiceLocale"><bean:message key="protocollo.faldone.codice_locale"/></label>&nbsp;:
	    </td>
		<td colspan="2">
	      <html:text name="ricercaFaldoneForm" property="codiceLocale" styleId="codiceLocale" size="20" maxlength="20" />
	    </td>
	</tr>
	<tr>
		<td><label for="nota"> <bean:message
			key="protocollo.faldone.nota" />&nbsp;: </label></td>
		<td colspan="2"><html:textarea name="ricercaFaldoneForm" property="nota" styleId="nota"
			rows="1" cols="50" /></td>
	</tr>
	<tr>
		<td>
			<html:submit styleClass="submit" property="btnCerca" value="Cerca" title="Cerca Faldoni"/>     
		<%--	<html:submit styleClass="button" property="btnReset" value="Annulla" title="Azzera i campi della ricerca"/> --%>
		<%--	<html:submit styleClass="button" property="annullaAction" value="Reset" title="Azzera i campi della ricerca"/> --%>   
			<html:reset styleClass="submit"	property="ResetAction" value="Annulla" alt="Azzera i campi della ricerca" />
		 <logic:equal name="ricercaFaldoneForm" property="indietroVisibile" value="true" >
		<html:submit styleClass ="submit" property="btnAnnulla" value="Indietro" alt="Indietro" />
	</logic:equal>
        </td>
    </tr>
</table>

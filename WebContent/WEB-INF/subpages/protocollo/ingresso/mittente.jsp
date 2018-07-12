<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />


<table summary="">
	<tr>
		<td class="label"><span><bean:message
			key="protocollo.mittente.tipo" />&nbsp;:</span></td>
		<td><html:radio property="mittente.tipo" styleId="personaFisica"
			value="F" onclick="document.forms[0].submit()">
			<label for="personaFisica"><bean:message
				key="protocollo.mittente.personafisica" /></label>
		</html:radio> &nbsp;&nbsp; <html:radio property="mittente.tipo"
			styleId="personaGiuridica" value="G"
			onclick="document.forms[0].submit()">
			<label for="personaGiuridica"><bean:message
				key="protocollo.mittente.personagiuridica" /></label>
		</html:radio> &nbsp;&nbsp; <!-- 
		
		 --> &nbsp;&nbsp; <script type="text/javascript"></script>
		<noscript>
		<div><html:submit styleClass="button" value="Imposta"
			title="Imposta il tipo di Mittente" /> &nbsp;&nbsp;</div>
		</noscript>
		<html:submit styleClass="button" property="cercaMittenteAction"
			value="Seleziona" title="Seleziona il mittente dalla rubrica" /></td>
	</tr>
		<logic:equal name="protocolloForm" property="mittente.tipo" value="F">

			<tr>
				<td class="label"><span><bean:message
					key="protocollo.mittente.cognome" />&nbsp;:</span></td>
				<td><span><strong> <bean:write
					name="protocolloForm" property="mittente.cognome" /> </strong></span></td>
			</tr>
			<tr>
				<td class="label"><span><bean:message
					key="protocollo.mittente.nome" />&nbsp;:</span></td>
				<td><span><strong> <bean:write
					name="protocolloForm" property="mittente.nome" /> </strong></span></td>
			</tr>
		</logic:equal>
		<logic:equal name="protocolloForm" property="mittente.tipo" value="G">
			<tr>
			<td class="label"><label for="numProtocolloMittente"><bean:message
				key="protocollo.mittente.protocolloid" />&nbsp;:</label></td>
			<td><html:text property="numProtocolloMittente"
				styleId="numProtocolloMittente" size="20" maxlength="50" />
			&nbsp;&nbsp; <html:submit styleClass="button"
				property="btnCercaProtMitt" value="Cerca"
				title="Cerca il Protocollo del Mittente" /></td>
			 <td class="label"><label title="Data del documento"
                 for="dataProtocolloMittente"><bean:message key="protocollo.documento.data" /></label>&nbsp;:
             </td>
             <td><html:text styleClass="text" property="dataProtocolloMittente" styleId="dataProtocolloMittente" size="10"
                        maxlength="10" />
                 <eprot:calendar textField="dataProtocolloMittente" />
                &nbsp;
             </td>
		</tr>
			<tr>
				<td class="label"><span><bean:message
					key="protocollo.mittente.denominazione" />&nbsp;:</span></td>
				<td><span><strong> <bean:write
					name="protocolloForm" property="mittente.descrizioneDitta" /> </strong></span></td>
			</tr>
		</logic:equal>

		<tr>
			<td class="label"><span><bean:message
				key="soggetto.indirizzo" />&nbsp;:</span></td>
			<td><span><strong> <bean:write
				name="protocolloForm" property="mittente.indirizzoNumCivico" /> </strong></span></td>
		</tr>
		<tr>
			<td class="label"><span><bean:message
				key="soggetto.localita" />&nbsp;:</span></td>
			<td><span><strong> <bean:write
				name="protocolloForm" property="mittente.indirizzo.comune" /> </strong></span></td>
		</tr>
		<tr>
			<td class="label"><span title="Codice di Avviamento Postale"><bean:message
				key="soggetto.cap" />&nbsp;:</span></td>
			<td><span><strong> <bean:write
				name="protocolloForm" property="mittente.indirizzo.cap" />
			&nbsp;&nbsp;</strong></span> <span><bean:message key="soggetto.provincia" />&nbsp;:</span>
			<span><strong><html:select disabled="true"
				property="mittente.indirizzo.provinciaId">
				<html:optionsCollection property="province" value="provinciaId"
					label="descrizioneProvincia" />
			</html:select> </strong></span></td>
		</tr>
		  <tr>
    <td class="label">
      <span title="EMail"><bean:message key="soggetto.email" />&nbsp;:</span>
    </td>
    <td><span>
		<strong><bean:write name="protocolloForm" property="mittente.indirizzoEMail"/></strong> 
    	</span></td>
   </tr>
	 
</table>

<br>
<html:submit styleClass="button" property="assegnaMittenteAction" value="Aggiungi" title="Assegna nuovo mittente" />
<br/>
<br/>


<logic:notEmpty name="protocolloForm" property="mittenti">

<fieldset>
<legend>Mittenti inseriti</legend>
<table summary="" border="1" id="tabella_mittenti">
<tr>
<th></th>
<th>Descrizione</th>
<th>Indirizzo</th>
<th>EMail</th>
</tr>
<logic:iterate indexId="index" id="mittente" name="protocolloForm" property="mittenti">
      
     <tr> 

		  <td>
	    	<html:multibox property="mittentiSelezionatiId">
	    		<bean:write name="index"/>
	    	</html:multibox>
	      </td>

		  <td><bean:write name="mittente" property="descrizione"/></td>
		  <td><bean:write name="mittente" property="indirizzoCompleto"/></td>
		  <td><bean:write name="mittente" property="indirizzoEMail"/></td>
	</tr>
</logic:iterate>
</table>

<br>
<html:submit styleClass="button" property="rimuoviMultiMittentiAction" value="Rimuovi" title="Rimuove gli assegnatari selezionati dall'elenco" />

</fieldset>
</logic:notEmpty>

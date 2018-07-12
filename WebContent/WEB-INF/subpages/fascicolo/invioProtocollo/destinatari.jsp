<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<html:errors bundle="bundleErroriProtocollo"
	property="destinatario_nome_obbligatorio" />
<html:errors bundle="bundleErroriProtocollo" property="formato_data" />

<table summary="" >
	<tr>
		<td class="label"><span><bean:message key="protocollo.mittente.tipo" />
		:</span></td>
		<td>
		<table summary="">
			<tr>
				<td><html:radio property="tipoDestinatario" styleId="personaFisica" value="F"
					onclick="document.forms[0].submit()">
					<label for="personaFisica"><bean:message
						key="protocollo.mittente.personafisica" /></label>
				</html:radio></td>
				<td>&nbsp;&nbsp;</td>
				<td><html:radio property="tipoDestinatario" styleId="personaGiuridica" value="G"
					onclick="document.forms[0].submit()">
					<label for="personaGiuridica"><bean:message
						key="protocollo.mittente.personagiuridica" /></label>
				</html:radio></td>
				<td><script type="text/javascript"></script>
				<noscript>
				<div><%--html:submit styleClass="button" value="Imposta" title="Imposta il tipo di Mittente"/--%>
				&nbsp;&nbsp;</div>
				</noscript>
				<html:submit styleClass="button" property="cercaDestinatari"
					value="Seleziona" title="Seleziona i destinatari della rubrica" />
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
	    
		<td class="label">
		<label for="nominativoDestinatario"> <bean:message
			key="protocollo.destinatario.nominativo" /> </label> <span
			class="obbligatorio">*</span> :</td>
		    <td><html:text property="nominativoDestinatario"
			styleClass="obbligatorio" styleId="nominativoDestinatario" maxlength="160">
			</html:text>
			</td>
			<td> &nbsp; <label
			title="Per conoscenza" for="flagConoscenza"> <bean:message
			key="protocollo.destinatario.pc" />: </label> <html:checkbox
			property="flagConoscenza" styleId="flagConoscenza"></html:checkbox></td>
	</tr>
	
	<tr>
		<td class="label"><label for="emailDestinatario"> <bean:message
			key="protocollo.destinatario.email" />: </label></td>
		<td><html:text property="emailDestinatario" styleId="emailDestinatario"></html:text> &nbsp; <label
			for="citta"> <bean:message key="protocollo.destinatario.citta" />: </label>
		<html:text property="citta" styleId="citta"></html:text></td>
	</tr>
	<tr>
		<td class="label"><label for="indirizzoDestinatario"> <bean:message
			key="protocollo.destinatario.indirizzo" />: </label></td>
		<td><html:text property="indirizzoDestinatario" styleId="indirizzoDestinatario"></html:text>
		
		</td>
	</tr>
	<tr>
		<td class="label"><label for="tipoSpedizione"> <bean:message
			key="protocollo.destinatario.tipospedizione" />: </label></td>
		<td><html:select property="mezzoSpedizioneId">
			<option value="0" > </option>
			<html:optionsCollection property="mezziSpedizione" value="id" label="descrizioneSpedizione" />
		</html:select> 
		<html:hidden property="destinatarioMezzoId" />
		&nbsp; 
		<label title="Data spedizione"
			for="dataSpedizione"> <bean:message
			key="protocollo.destinatario.dataspedizione" />: </label> 
			<html:text property="dataSpedizione" styleId="dataSpedizione" size="10" maxlength="10" /> 
			<eprot:calendar textField="dataSpedizione" />
		</td>
	</tr>
	<tr>
		<td><html:submit styleClass="button" property="aggiungiDestinatario"
			value="Aggiungi" title="Aggiunge il destinatario alla lista" /></td>
	</tr>
</table>

<logic:notEmpty name="fascicoloForm" property="destinatari">
	<hr></hr>
	<table summary="" border="1" width="98%">
		<tr>
			<th></th>
			<th><span> <bean:message key="protocollo.destinatario.tipo" /> </span>
			</th>
			<th><span> <bean:message key="protocollo.destinatario.nominativo" />
			</span></th>
			<th><span> <bean:message key="protocollo.destinatario.indirizzo" /> </span>
			</th>
			<th><span> <bean:message key="protocollo.destinatario.email" /> </span>
			</th>
			<th><span> <bean:message key="protocollo.destinatario.tipospedizione" />
			</span></th>
			<th><span> <bean:message key="protocollo.destinatario.dataspedizione" />
			</span></th>
			<th><span> <bean:message key="protocollo.destinatario.pc" /> </span>
			</th>
		</tr>

		<logic:iterate id="currentRecord" name="fascicoloForm" property="destinatari">
			<tr>
				<td>
					<html:multibox property="destinatarioSelezionatoId">
						<bean:write name="currentRecord" property="destinatarioMezzoId" />
					</html:multibox>
				</td>
				<td><span> <bean:write name="currentRecord" property="flagTipoDestinatario" /> </span></td>
				<td>
					<span> <html:link action="/page/invioFascicolo.do" paramId="destinatarioId" 
						paramName="currentRecord" paramProperty="idx">
					<bean:write name="currentRecord" property="destinatario" />
					</html:link> </span>
				</td>
				<td>
					<logic:notEmpty name="currentRecord" property="indirizzo">
						<span> 
							<bean:write name="currentRecord" property="indirizzo" /><br />
						</span>
					</logic:notEmpty>
					<logic:notEmpty name="currentRecord" property="capDestinatario">
						<span> 
							<bean:write name="currentRecord" property="capDestinatario" />&nbsp;
						</span>
					</logic:notEmpty>					
					<span> 
						<bean:write name="currentRecord" property="citta" /> 
					</span>
				</td>
				<td><span> <bean:write name="currentRecord" property="email" /> </span>
				</td>
				<td><span> <bean:write name="currentRecord" property="mezzoDesc" /><br>
				</span></td>
				<td><span> <bean:write name="currentRecord" property="dataSpedizione" /><br>
				</span></td>
				<td><span> <bean:write name="currentRecord" property="conoscenza" />
				</span></td>
			</tr>
		</logic:iterate>

	</table>
</logic:notEmpty>

<p><logic:notEmpty name="fascicoloForm" property="destinatari">
	<html:submit styleClass="button" property="rimuoviDestinatari"
		value="Rimuovi"
		title="Rimuovi i destinatari selezionati dall'elenco dei destinatari" />
</logic:notEmpty></p>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<html:errors bundle="bundleErroriProtocollo"
	property="destinatario_nome_obbligatorio" />
<html:errors bundle="bundleErroriProtocollo" property="formato_data" />

<table summary="">
	<tr>
		<td class="label"><span><strong><bean:message key="protocollo.mittente.tipo" />
		:</strong></span></td>
		<td>
		<table summary="">
			<tr>
				<td><html:radio property="tipoDestinatario" styleId="personaFisica"  value="F"
					onclick="document.forms[0].submit()">
					<label for="personaFisica"><bean:message
						key="protocollo.destinatario.personafisica" /></label>
				</html:radio></td>
				<td>&nbsp;&nbsp;</td>
				<td><html:radio property="tipoDestinatario" styleId="personaGiuridica" value="G"
					onclick="document.forms[0].submit()">
					<label for="personaGiuridica"><bean:message
						key="protocollo.destinatario.personagiuridica" /></label>
				</html:radio></td>
				<td><script type="text/javascript"></script>
				<noscript>
				<div>
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
		<td class="label"><label for="nominativoDestinatario"> <bean:message
			key="protocollo.destinatario.nominativo" />:</label></td>
		<td> <strong><bean:write name="editorForm" property="nominativoDestinatario"/></strong> &nbsp; <label
			title="Per conoscenza" for="flagConoscenza"><bean:message
			key="protocollo.destinatario.pc" />: </label> <html:checkbox
			property="flagConoscenza" styleId="flagConoscenza"></html:checkbox></td>
	</tr>
	<tr>
		<td class="label"><label for="emailDestinatario"> <bean:message
			key="protocollo.destinatario.email" />: </label></td>
		<td><strong><bean:write name="editorForm" property="emailDestinatario"/></strong> &nbsp; <label
			for="citta"> <bean:message key="protocollo.destinatario.citta" />: </label>
		<strong><bean:write name="editorForm" property="citta"/></strong></td>
	</tr>
	<tr>
		<td class="label"><label for="indirizzoDestinatario"> <bean:message
			key="protocollo.destinatario.indirizzo" />: </label></td>
		<td><strong> <bean:write name="editorForm" property="indirizzoDestinatario" /></strong>
		<label title="Codice di Avviamento Postale" for="capDestinatario"> <bean:message
			key="soggetto.cap" />: </label>
		<strong><bean:write name="editorForm" property="capDestinatario" /></strong></td>
	</tr>
	<tr>
		<td class="label"><label for="tipoSpedizione"> <bean:message
			key="protocollo.destinatario.tipospedizione" />: </label></td>
		<td>
			<html:select property="mezzoSpedizione" styleId="tipoSpedizione">
			 <option value="" ></option> 
			<html:optionsCollection property="mezziSpedizione" value="id" label="descrizioneSpedizione" />
			</html:select> 
			<html:hidden property="destinatarioMezzoId" />
			&nbsp; 
			<label title="Data spedizione" for="dataSpedizione"> 
			
			<bean:message key="protocollo.destinatario.dataspedizione" />:</label> 
			<html:text property="dataSpedizione" styleId="dataSpedizione" size="10" maxlength="10" /> 
			 <eprot:calendar textField="dataSpedizione" />
			 <!--  -->
			 <label title="Per conoscenza" for="flagConoscenza"> 
			<bean:message key="protocollo.destinatario.pc" />: </label> <html:checkbox property="flagConoscenza" styleId="flagConoscenza"></html:checkbox>
			<label title="presso" for="flagPresso"> 
			<bean:message key="protocollo.destinatario.presso" />: </label> <html:checkbox property="flagPresso" styleId="flagPresso"></html:checkbox>
			 <!--  -->
		</td>
	</tr>
	<!--  -->
	<tr>
		<td class="label">
			<label for="flagPEC"> 
				Invia tramite PEC: 
			</label>
		</td>
		<td>
		<html:checkbox property="flagPEC" styleId="flagPEC"></html:checkbox>
		</td>
	</tr>
	<!--  -->
	<tr>
		<td><html:submit styleClass="button" property="aggiungiDestinatario"
			value="Aggiungi" title="Aggiunge il destinatario alla lista" /></td>
	</tr>
</table>
<logic:notEmpty name="editorForm" property="destinatari">
	<bean:define id="destinatari" name="editorForm" property="destinatari" />
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
			<th><span> <bean:message key="protocollo.destinatario.presso" />
			</span></th>
			<th><span> PEC
			</span></th>
		</tr>

		<logic:iterate id="currentRecord" name="editorForm"
			property="destinatari">
			<tr>
				<td><html:multibox property="destinatarioSelezionatoId">
					<bean:write name="currentRecord" property="idx" />
				</html:multibox></td>
				<td><span><bean:write name="currentRecord"
					property="flagTipoDestinatario" /></span></td>
				<td>
				<span> 
					<bean:write name="currentRecord" property="destinatario" /> </span></td>
				
				<td><span><bean:write name="currentRecord" property="indirizzoView" /></span></td>
				
				<td><span><bean:write name="currentRecord" property="email" /></span>
				</td>
				<td><span><bean:write name="currentRecord" property="mezzoDesc" /></span>
				</td>
				<td><span><bean:write name="currentRecord" property="dataSpedizione" /></span>
				</td>
				<td><span><bean:write name="currentRecord" property="conoscenza" /></span>
				</td>
				<td><span> <bean:write name="currentRecord"
					property="presso" /> </span></td>
				<td><span> <bean:write name="currentRecord"
					property="PEC" /> </span></td>
			</tr>
		</logic:iterate>

	</table>
</logic:notEmpty>

<p><logic:notEmpty name="editorForm" property="destinatari">
	<html:submit styleClass="button" property="rimuoviDestinatari"
		value="Rimuovi"
		title="Rimuovi i destinatari selezionati dall'elenco dei destinatari" />
</logic:notEmpty></p>

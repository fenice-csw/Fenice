<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml />

<tr>
	<td class="label">Riferimenti Legislativi:</td>
	<td>
	<logic:notEmpty property="titolario" name="fascicoloForm">
		<div class="sezione">
		<br>
		<label for="nomeFileUpload"> <bean:message
			key="protocollo.allegati.nome" />: </label> <html:text
			property="nomeFileUpload" styleId="nomeFileUpload" /> &nbsp; <label
			for="formFileUpload"> <bean:message
			key="protocollo.allegati.file" />: </label> <html:file
			property="formFileUpload" styleId="formFileUpload" /> &nbsp; <html:submit
			styleClass="button" property="allegaRiferimentoAction"
			value="Integra" title="Allega il file selezionato" />
		<table>
			<logic:iterate id="recordDocumento"
				property="titolario.riferimentiCollection" name="fascicoloForm">
				<tr>
					<td><html:link action="/page/fascicolo.do"
						paramId="downloadAllegatoTitolarioId" paramName="recordDocumento"
						paramProperty="idx" target="_blank" title="Download File">
						<bean:write name="recordDocumento" property="descrizione" />
					</html:link> &nbsp;&nbsp;</td>
				</tr>
			</logic:iterate>
			<logic:notEmpty property="riferimentiCollection"
				name="fascicoloForm">
				<logic:iterate id="recordDocumento" property="riferimentiCollection"
					name="fascicoloForm">
					<tr>
						<td><bean:define id="idx" name="recordDocumento"
							property="idx" /> <bean:define id="descrizione"
							name="recordDocumento" property="descrizione" /> <html:multibox
							property="riferimentiLegislativiId">
							<bean:write name="idx" />
						</html:multibox> <html:link action="/page/fascicolo.do" paramId="downloadAllegatoId"
							paramName="recordDocumento" paramProperty="idx" target="_blank"
							title="Download File">
							<bean:write name="recordDocumento" property="descrizione" />
						</html:link> (<bean:write name="recordDocumento" property="size" /> bytes)</td>
					</tr>
		&nbsp;&nbsp;
	</logic:iterate>
			</logic:notEmpty>
		</table>
		<logic:notEmpty name="fascicoloForm" property="riferimentiCollection">
			<html:submit styleClass="button"
				property="rimuoviRiferimentiAction" value="Rimuovi selezionati"
				alt="Rimuovi gli allegati selezionati" />
		</logic:notEmpty></div>
	</logic:notEmpty></td>
</tr>

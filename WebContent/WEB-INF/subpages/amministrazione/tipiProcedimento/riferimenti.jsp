<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml />

<p><label for="nomeFileUpload"> <bean:message
	key="protocollo.allegati.nome" />: </label> <html:text
	property="nomeFileUpload" styleId="nomeFileUpload" size="20"/> &nbsp; <label
	for="formFileUpload"> <bean:message
	key="protocollo.allegati.file" />: </label> <html:file
	property="formFileUpload" styleId="formFileUpload" /> &nbsp; <html:submit
	styleClass="button" property="allegaDocumentoAction" value="Allega"
	title="Allega il file selezionato" /></p>

<br />

<p><logic:notEmpty property="documentiAllegatiCollection"
	name="tipoProcedimentoForm">
	<logic:iterate id="recordDocumento"
		property="documentiAllegatiCollection" name="tipoProcedimentoForm">
		<span> <bean:define id="idx" name="recordDocumento"
			property="idx" /> <bean:define id="descrizione"
			name="recordDocumento" property="descrizione" /> <html:multibox
			property="allegatiSelezionatiId">
			<bean:write name="idx" />
		</html:multibox> <html:link action="/page/amministrazione/tipiProcedimento.do" paramId="downloadAllegatoId"
			paramName="recordDocumento" paramProperty="idx" target="_blank"
			title="Download File">
			<bean:write name="recordDocumento" property="descrizione" />
		</html:link> (<bean:write name="recordDocumento" property="size" /> bytes)
		&nbsp;&nbsp;</span>
	</logic:iterate>
</logic:notEmpty></p>

<logic:notEmpty name="tipoProcedimentoForm"
	property="documentiAllegatiCollection">
	<br />
	<p><html:submit styleClass="button"
		property="rimuoviAllegatiAction" value="Rimuovi selezionati"
		alt="Rimuovi gli allegati selezionati" /></p>
</logic:notEmpty>

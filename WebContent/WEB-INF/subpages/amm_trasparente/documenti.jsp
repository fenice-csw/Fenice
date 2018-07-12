<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml />

<p><label for="nomeFileUpload"> <bean:message
	key="protocollo.allegati.nome" />: </label> <html:text
	property="nomeFileUpload" styleId="nomeFileUpload" /> &nbsp; <label
	for="formFileUpload"> <bean:message
	key="protocollo.allegati.file" />: </label> <html:file
	property="formFileUpload" styleId="formFileUpload" /> <br />

<label for="riservato">Riservato</label>: <html:checkbox property="riservato" styleId="riservato" />
<br />
<label for="principale">Principale</label>: <html:checkbox property="principale" styleId="principale" />
<br />


<logic:equal name="documentoRepertorioForm" property="unitaAmministrativa.denominazione" value="POLICLINICO_CT">
	<label for="tipoAllegato">Ex. Art. 12</label>: <html:checkbox property="tipoAllegato" styleId="tipoAllegato" /><br />
</logic:equal>

<html:submit styleClass="button" property="allegaDocumentoAction"
	value="Allega" title="Allega il file selezionato" /></p>
<br />

<p><logic:notEmpty property="documentiAllegatiCollection"
	name="documentoRepertorioForm">
	<fieldset><legend>Documenti inseriti</legend>
	<table summary=""  border="1" id="tabella_mittenti">
		<tr>
			<th></th>
			<th>Nome file</th>
			<th>Dimensione</th>
			<th>Riservato</th>
			<th>Principale</th>
			<th>Pubblicabile</th>
			<logic:equal name="documentoRepertorioForm" property="unitaAmministrativa.denominazione" value="POLICLINICO_CT">
				<th>Ex. Art. 12</th>
			</logic:equal>
		</tr>
		<logic:iterate id="recordDocumento"
			property="documentiAllegatiCollection" name="documentoRepertorioForm">
			<tr>
				<td><html:multibox property="allegatiSelezionatiId">
					<bean:write name="recordDocumento" property="idx" />
				</html:multibox></td>
				<td><html:link
					action="/page/repertorio/documento_repertorio.do"
					paramId="downloadAllegatoId" paramName="recordDocumento"
					paramProperty="idx" target="_blank" title="Download File">
					<bean:write name="recordDocumento" property="descrizione" />
				</html:link></td>
				<td><bean:write name="recordDocumento" property="size" />
					bytes
				</td>
				<td><logic:equal property="riservato" name="recordDocumento"
						value="true">
		  				Si
		  			</logic:equal> 
		  			<logic:equal property="riservato" name="recordDocumento"
						value="false">
		  				No
		  			</logic:equal>
		  		</td>
		  		<td><logic:equal property="principale" name="recordDocumento"
						value="true">
		  				Si
		  			</logic:equal> 
		  			<logic:equal property="principale" name="recordDocumento"
						value="false">
		  				No
		  			</logic:equal>
		  		</td>
		  		<logic:equal name="documentoRepertorioForm" property="btnPubblicaVisibile" value="true">
		  			<td>
	    				<html:multibox property="documentiPubblicabili" >
	    					<bean:write name="recordDocumento" property="idx"/>
	    				</html:multibox>
		  			</td>
		  		</logic:equal>
		  		<logic:equal name="documentoRepertorioForm" property="unitaAmministrativa.denominazione" value="POLICLINICO_CT">
		  		<td><logic:equal property="type" name="recordDocumento"
						value="0">
		  				No
		  			</logic:equal> 
		  			<logic:equal property="type" name="recordDocumento"
						value="1">
		  				Si
		  			</logic:equal>
		  		</td>
		  		</logic:equal>
			</tr>
		</logic:iterate>
	</table>
	<br />
	<p><html:submit styleClass="button"
		property="rimuoviAllegatiAction" value="Rimuovi selezionati"
		alt="Rimuovi gli allegati selezionati" /></p>
	</fieldset>
</logic:notEmpty></p>



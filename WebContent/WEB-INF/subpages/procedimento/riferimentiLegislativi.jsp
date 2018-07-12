<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml />

<tr>
	
	<td class="label">
		<logic:equal name="procedimentoForm"
				property="tipoProcedimento.ull"
				value="true">
		Provvedimento/i Impugnato/i
		</logic:equal>
		<logic:notEqual name="procedimentoForm"
				property="tipoProcedimento.ull"
				value="true">
		Riferimenti Legislativi:
		</logic:notEqual>
	</td>
	<td>
	<logic:notEmpty property="tipoProcedimento" name="procedimentoForm">
		<div class="sezione">
		<logic:equal name="procedimentoForm"
				property="tipoProcedimento.ull"
				value="true">
		<p>		
		<label for="nomeFileUpload"> Estremi Provvedimento: </label> 
			<html:text property="estremiProvvedimento"  size="50"/> &nbsp; 
		</p>
		</logic:equal>
		
		<logic:notEqual name="procedimentoForm"
				property="tipoProcedimento.ull"
				value="true">
		<label for="nomeFileUpload"> <bean:message
			key="protocollo.allegati.nome" />: </label> 
			<html:text property="nomeFileUpload" styleId="nomeFileUpload" /> &nbsp; 
		</logic:notEqual>
		
			<label for="formFileUpload"> <bean:message
			key="protocollo.allegati.file" />: </label> <html:file
			property="formFileUpload" styleId="formFileUpload" /> &nbsp; <html:submit
			styleClass="button" property="allegaRiferimentoAction"
			value="Integra" title="Allega il file selezionato" />
		<table>
		
			<logic:iterate id="recordDocumento"
				property="tipoProcedimento.riferimentiCollection" name="procedimentoForm">
				<tr>
					<td><html:link action="/page/procedimento.do"
						paramId="downloadAllegatoProcedimentoId" paramName="recordDocumento"
						paramProperty="idx" target="_blank" title="Download File">
						<bean:write name="recordDocumento" property="descrizione" />
					</html:link> &nbsp;&nbsp;</td>
				</tr>
			</logic:iterate>
			
			<logic:notEmpty property="riferimentiCollection" name="procedimentoForm">
				<logic:iterate id="recordDocumento" property="riferimentiCollection"
					name="procedimentoForm">
					<tr>
						<td><bean:define id="idx" name="recordDocumento"property="idx" /> 
						<bean:define id="descrizione" name="recordDocumento" property="descrizione" /> 
						<html:multibox property="riferimentiLegislativiId">
							<bean:write name="idx" />
						</html:multibox> <html:link action="/page/procedimento.do"
							paramId="downloadAllegatoId" paramName="recordDocumento"
							paramProperty="idx" target="_blank" title="Download File">
							<bean:write name="recordDocumento" property="descrizione" />
						</html:link> (<bean:write name="recordDocumento" property="size" /> bytes)</td>
					</tr>
			&nbsp;&nbsp;
			</logic:iterate>
			</logic:notEmpty>
		</table>
		 
		<logic:notEmpty name="procedimentoForm" property="riferimentiCollection">
			<html:submit styleClass="button"
				property="rimuoviRiferimentiAction" value="Rimuovi selezionati"
				alt="Rimuovi gli allegati selezionati" />
		</logic:notEmpty>
		
		</div>
	</logic:notEmpty></td>
</tr>

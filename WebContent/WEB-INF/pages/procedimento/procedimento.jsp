<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />

<script>
	function selectTipoProcedimento() {
		document.getElementById("selezionaTipoProcedimentoButton").click();
	}
</script>

<eprot:page title="Procedimento">
	<span
		class="title"> <strong> <bean:message
		key="procedimento.procedimento" /></strong></span> <html:form
		action="/page/procedimento.do" enctype="multipart/form-data">
		<div id="protocollo-errori"><html:errors
			bundle="bundleErroriProtocollo" /></div>
		<logic:messagesPresent message="true">
			<div id="protocollo-messaggi">
			<ul>
				<html:messages id="actionMessage" message="true"
					bundle="bundleErroriProtocollo">
					<li><bean:write name="actionMessage" /></li>
				</html:messages>
			</ul>
			</div>
		</logic:messagesPresent>
		<div>
		<table summary="">

			<tr>
				<td class="label"><label for="tipoProcedimento"> <bean:message
					key="procedimento.tipo_procedimento" />&nbsp;<span
					class="obbligatorio">*</span>&nbsp;: </label></td>
				<logic:greaterThan name="procedimentoForm" property="procedimentoId"
					value="0">
					<td colspan="2"><html:select styleClass="obbligatorio"
						styleId="tipoProcedimento" name="procedimentoForm"
						property="tipoProcedimentoId" disabled="true">
						<html:optionsCollection name="procedimentoForm"
							property="tipiProcedimento" value="idTipo" label="descrizione" />
					</html:select></td>
				</logic:greaterThan>
				<logic:equal name="procedimentoForm" property="procedimentoId"
					value="0">
					<td colspan="2"><html:select styleClass="obbligatorio"
						styleId="tipoProcedimento" name="procedimentoForm"
						property="tipoProcedimentoId" onchange="selectTipoProcedimento();">
						<html:optionsCollection name="procedimentoForm"
							property="tipiProcedimento" value="idTipo" label="descrizione" />
					</html:select></td>
				</logic:equal>
			</tr>

			<jsp:include
				page="/WEB-INF/subpages/procedimento/proprietaTipoProcedimento.jsp" />
			<logic:equal name="procedimentoForm"
				property="tipoProcedimento.ull"
				value="true">
				<jsp:include
					page="/WEB-INF/subpages/procedimento/datiProcedimentoULL.jsp" />
			</logic:equal>
			<logic:notEqual name="procedimentoForm"
				property="tipoProcedimento.ull"
				value="true">
				<jsp:include
					page="/WEB-INF/subpages/procedimento/datiProcedimento.jsp" />
			</logic:notEqual>

		</table>
		</div>
		<logic:notEqual name="procedimentoForm" property="procedimentoId" value="0">
			<div class="sezione"><span class="title"><strong><bean:message
				key="procedimento.fascicoli" /></strong></span> <jsp:include
				page="/WEB-INF/subpages/procedimento/listaFascicoli.jsp" />  	
			</div>
		</logic:notEqual>
		<div>
		<logic:equal name="procedimentoForm" property="modificabile" value="true">
			<html:submit styleClass="submit"
				property="salvaProcedimentoAction" value="Salva" alt="Salva" /> 
			<logic:equal name="procedimentoForm" property="indietroVisibile" value="true">
			<html:submit styleClass="submit" property="btnAnnulla"
				value="Indietro" alt="Indietro" />		
			</logic:equal> 
		</logic:equal>
		
		<logic:equal name="procedimentoForm" property="modificabile" value="false">
		<logic:equal name="procedimentoForm" property="procedimentoId" value="0">
		<html:submit styleClass="submit"
			property="salvaProcedimentoAction" value="Salva" alt="Salva" /> 
			<logic:equal name="procedimentoForm" property="indietroVisibile" value="true">
			<html:submit styleClass="submit" property="btnAnnulla"
				value="Indietro" alt="Indietro" />		
			</logic:equal> 
			</logic:equal>
		</logic:equal>
		
		<logic:greaterThan name="procedimentoForm" property="procedimentoId" value="0">
		
			<logic:equal name="procedimentoForm" property="tipoProcedimento.ull" value="true">
				<html:submit styleClass="submit" property="btnStampa" value="Stampa Frontespizio" alt="Stampa Frontespizio" />
				<html:submit styleClass="submit" property="btnNuovoDocumento" value="Inserisci Documento" alt="Inserisci Documento" />
				<html:submit styleClass="submit" property="btnCompilaLettera" value="Compila Lettera in Uscita" alt="Compila Lettera in Uscita" />
				<html:submit styleClass="submit" property="btnEditor" value="Prepara Documento" alt="Prepara Documento" />
				<html:submit styleClass="submit" property="btnRiassegnaULL" value="Riassegna Procedimento" alt="Riassegna Procedimento" />
			</logic:equal>
			
			<logic:equal name="procedimentoForm" property="modificabile" value="true">
				<logic:equal name="procedimentoForm" property="sospeso" value="false">
					<html:submit styleClass="submit" property="btnSospensione" value="Sospensione" title="Sospensione del Procedimento" />
					<html:submit styleClass="submit" property="btnChiudi" value="Chiudi" title="Chiudi il Procedimento" />
				</logic:equal>
				<logic:equal name="procedimentoForm" property="sospeso" value="true">
					<html:submit styleClass="submit" property="btnRiavvio" value="Riavvio" title="Riavvio del Procedimento" />
				</logic:equal>
			</logic:equal>
			
		</logic:greaterThan>
		</div>
	</html:form>
</eprot:page>

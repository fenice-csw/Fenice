<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />

<eprot:page title="Procedimento">

	<html:form action="/page/procedimentoview.do">


		<div id="protocollo-errori">
			<html:errors bundle="bundleErroriProtocollo" />
		</div>
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
								key="procedimento.tipo_procedimento" />:
					</label></td>
					<td colspan="2"><html:select styleClass="obbligatorio"
							styleId="tipoProcedimento" name="procedimentoForm"
							property="tipoProcedimentoId" disabled="true">
							<html:optionsCollection name="procedimentoForm"
								property="tipiProcedimento" value="idTipo" label="descrizione" />
						</html:select></td>
				</tr>

				<jsp:include
					page="/WEB-INF/subpages/procedimento/proprietaTipoProcedimentoView.jsp" />

				<logic:equal name="procedimentoForm" property="tipoProcedimento.ull"
					value="true">
					<jsp:include
						page="/WEB-INF/subpages/procedimento/datiProcedimentoULLView.jsp" />
				</logic:equal>

				<logic:notEqual name="procedimentoForm"
					property="tipoProcedimento.ull" value="true">
					<jsp:include
						page="/WEB-INF/subpages/procedimento/datiProcedimentoView.jsp" />
				</logic:notEqual>

			</table>
		</div>

		<div class="sezione">
			<span class="title"><strong><bean:message
						key="procedimento.fascicoli" /></strong></span>
			<jsp:include page="/WEB-INF/subpages/procedimento/listaFascicoli.jsp" />
		</div>
		<div>
			<html:submit styleClass="submit" property="btnStoria" value="Storia" title="Storia del Procedimento" />
			<logic:notEqual name="procedimentoForm" property="statoId" value="1">
				<logic:notEqual name="procedimentoForm" property="statoId" value="2">
					<logic:equal name="procedimentoForm" property="modificabile" value="true">
						<html:submit styleClass="submit" property="btnModifica" value="Modifica" alt="Modifica il Procedimento" />
					</logic:equal>
				</logic:notEqual>
			</logic:notEqual>
			<logic:equal name="procedimentoForm" property="statoId" value="1">
				<logic:equal name="procedimentoForm" property="modificabile" value="true">
					<html:submit styleClass="submit" property="btnRiapri" value="Riapri" alt="Riapri il Procedimento" />
				</logic:equal>
			</logic:equal>
			<html:submit styleClass="submit" property="stampaComunicazioneAvvioAction" value="Stampa Comunicazione Avvio" title="Stampa la lettera di comunicazione avvio" />
		</div>
		
	</html:form>
</eprot:page>
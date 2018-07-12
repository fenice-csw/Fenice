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

<eprot:page title="Sospensione">
	<div class="sezione" styleClass="obbligatorio">
	<span class="title"> <strong> <bean:message
		key="procedimento.procedimento" /></strong></span> 
		<html:form action="/page/procedimento.do" enctype="multipart/form-data">
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
		<jsp:include page="/WEB-INF/subpages/procedimento/gestioneAllaccioSospensione.jsp" />
		
		<table summary="">

			<tr>
				<td class="label">
					<label title="Data" for="dataSospensione">Data</label>:
				</td>
				<td colspan="2">
					<strong>	
						<bean:write name="procedimentoForm" property="dataSospensione" />
					</strong>
				</td>
			</tr>
			<tr>
				<td class="label">
					<label for="note"> 
						Estremi: 
					</label>
				</td>
				<td colspan="2">
					<html:textarea name="procedimentoForm" styleId="estremiSospensione" property="estremiSospensione" rows="10" cols="50" />
				</td>
			</tr>

		</table>
		</div>
			<logic:equal name="procedimentoForm" property="sospeso" value="true">
				<html:submit styleClass="submit" property="riavviaProcedimentoAction" value="Riavvia" title="Riavvia il Procedimento" />
				<html:submit styleClass="submit" property="annullaSospensioneAction" value="Annulla Sospensione" title="Annulla la Sospensione " />
				<html:submit styleClass="submit" property="annullaOperazioneAction" value="Indietro" title="Indietro " />
			</logic:equal>
			
			<logic:equal name="procedimentoForm" property="sospeso" value="false">
				<html:submit styleClass="submit" property="sospensioneAction" value="Sospendi" title="Sospendi il Procedimento" />
				<html:submit styleClass="submit" property="annullaOperazioneAction" value="Indietro" title="Indietro " />
			</logic:equal>
	</html:form>
	</div>
</eprot:page>

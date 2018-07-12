<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Lista Sezioni Amministrazione Trasparente">

	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" /></div>
	<div id="protocollo-messaggi"><html:messages id="msg"
		message="true">
		<ul>
			<li><bean:write name="msg" /></li>
		</ul>
	</html:messages></div>


	<html:form action="/page/ammTrasparenteView">

		<div>
		<table summary="">
			<tr>
				<td class="label"><label for="sezioneId">Registro di
				sezione: </label></td>
				<td><span class="obbligatorio"><bean:write
					name="sezioneForm" property="descrizione" /></span></td>
			</tr>
			<tr>
				<td class="label"><label for="responsabile">Responsabile:
				</label></td>
				<td>
					<bean:write name="sezioneForm" property="responsabile.nomeUfficio" />
				</td>
			</tr>
			<tr>
				<td class="label"><label for="flagWebAsString">Visibilità Web:
				</label></td>
				<td>
 								<logic:equal name="sezioneForm" property="flagWeb" value="1">
 									<strong>SI</strong>
 								</logic:equal>
 								<logic:equal name="sezioneForm" property="flagWeb" value="0">
 									<strong>NO</strong>
 								</logic:equal>
 							</td>
			</tr>
		</table>
		</div>

		<div class="sezione"><span class="title"> <strong>Documenti</strong>
		</span> <logic:notEmpty name="sezioneForm" property="documentiSezione">
			<display:table class="simple" width="100%"
				name="requestScope.sezioneForm.documentiSezione"
				export="false" sort="page" pagesize="15" id="sezione"
				requestURI="/page/ammTrasparenteView.do">
				<display:column title="Id">
					<html:link action="/page/amm_trasparente/documento_ammTrasparenteView.do"
						paramName="sezione" paramId="docId"
						paramProperty="docSezioneId">
						<bean:write name="sezione" property="numeroDocumentoSezione" />
					</html:link>
				</display:column>
				<display:column property="oggetto" title="Oggetto" />
				<display:column property="dataValiditaInizio" title="Pubblicabile dal" />
				<display:column property="dataValiditaFine" title="al" />
				<display:column property="ufficio" title="Responsabile" />
				<logic:equal name="sezioneForm" property="unitaAmministrativa.denominazione" value="POLICLINICO_CT">
					<display:column property="settoreProponente" title="Settore Proponente" />
				</logic:equal>
			</display:table>
		</logic:notEmpty> <logic:empty name="sezioneForm" property="documentiSezione">
			Nessun documento presente nella sezione
		</logic:empty></div>

		<div id="bottoni_salva">
			<html:submit styleClass="submit" property="btnIndietro" value="Indietro"alt="Indietro" />
		</div>
	</html:form>
</eprot:page>
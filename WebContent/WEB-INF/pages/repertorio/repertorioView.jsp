<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Lista Repertori">

	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" /></div>
	<div id="protocollo-messaggi"><html:messages id="msg"
		message="true">
		<ul>
			<li><bean:write name="msg" /></li>
		</ul>
	</html:messages></div>


	<html:form action="/page/repertorioView">

		<div>
		<table summary="">
			<tr>
				<td class="label"><label for="repertorioId">Registro di
				repertorio: </label></td>
				<td><span class="obbligatorio"><bean:write
					name="repertorioForm" property="descrizione" /></span></td>
			</tr>
			<tr>
				<td class="label"><label for="responsabile">Responsabile:
				</label></td>
				<td>
					<bean:write name="repertorioForm" property="responsabile.nomeUfficio" />
				</td>
			</tr>
			<tr>
				<td class="label"><label for="flagWebAsString">Visibilità Web:
				</label></td>
				<td>
 								<logic:equal name="repertorioForm" property="flagWeb" value="1">
 									<strong>SI</strong>
 								</logic:equal>
 								<logic:equal name="repertorioForm" property="flagWeb" value="0">
 									<strong>NO</strong>
 								</logic:equal>
 							</td>
			</tr>
		</table>
		</div>

		<div class="sezione"><span class="title"> <strong>Documenti</strong>
		</span> <logic:notEmpty name="repertorioForm" property="documentiRepertorio">
			<display:table class="simple" width="100%"
				name="requestScope.repertorioForm.documentiRepertorio"
				export="false" sort="page" pagesize="15" id="repertorio"
				requestURI="/page/repertorioView.do">
				<display:column title="Id">
					<html:link action="/page/repertorio/documento_repertorioView.do"
						paramName="repertorio" paramId="docId"
						paramProperty="docRepertorioId">
						<bean:write name="repertorio" property="numeroDocumentoRepertorio" />
					</html:link>
				</display:column>
				<display:column property="oggetto" title="Oggetto" />
				<display:column property="dataValiditaInizio" title="Pubblicabile dal" />
				<display:column property="dataValiditaFine" title="al" />
				<display:column property="ufficio" title="Responsabile" />
				<logic:equal name="repertorioForm" property="unitaAmministrativa.denominazione" value="POLICLINICO_CT">
					<display:column property="settoreProponente" title="Settore Proponente" />
				</logic:equal>
			</display:table>
		</logic:notEmpty> <logic:empty name="repertorioForm" property="documentiRepertorio">
			Nessun documento presente nel repertorio
		</logic:empty></div>

		<div id="bottoni_salva">
			<html:submit styleClass="submit" property="btnIndietro" value="Indietro"alt="Indietro" />
		</div>
	</html:form>
</eprot:page>
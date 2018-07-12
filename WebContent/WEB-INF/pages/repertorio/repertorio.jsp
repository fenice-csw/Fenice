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
	<div id="protocollo-messaggi">
		<html:messages id="msg" message="true">
			<ul>
				<li><bean:write name="msg" /></li>
			</ul>
		</html:messages>
	</div>


	<html:form action="/page/repertorio">

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
					<bean:write name="repertorioForm" property="responsabile.descrizioneUfficio" />
					<bean:write name="repertorioForm" property="responsabile.nomeUtente" />
				</td>
			</tr>
			<!--  -->
  			<tr>
 				<td>
 					<label for="flagWeb">Visibilità Web</label>:
 				</td>
 				<td>
 					<table>
 						<tr>
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
 				</td>
 			</tr>
 		<!--  -->
		</table>
		</div>

		<div class="sezione"><span class="title"> <strong>Documenti</strong>
		</span> <logic:notEmpty name="repertorioForm" property="documentiRepertorio">
			<display:table class="simple" width="100%"
				name="requestScope.repertorioForm.documentiRepertorio"
				export="false" sort="page" pagesize="15" id="repertorio"
				requestURI="/page/repertorio.do">
				<display:column title="Id">
					<html:link action="/page/repertorio/documento_repertorio.do"paramName="repertorio" paramId="docId"paramProperty="docRepertorioId">
						<bean:write name="repertorio" property="annoNumero" />
					</html:link>
				</display:column>
				<display:column property="oggetto" title="Oggetto" />
				<display:column property="dataValiditaInizio" title="Pubblicabile dal" />
				<display:column property="dataValiditaFine" title="al" />
				<display:column property="ufficio" title="Responsabile" />
				<logic:equal name="repertorioForm" property="unitaAmministrativa.denominazione" value="POLICLINICO_CT">
					<display:column property="settoreProponente" title="Settore Proponente" />
				</logic:equal>
				<%-- 
				<display:column title="Stato">
					<logic:equal name="repertorio" property="stato" value="4">
						Protocollato e Pubblicato
					</logic:equal>
					<logic:equal name="repertorio" property="stato" value="3">
						Protocollato
					</logic:equal>
					<logic:equal name="repertorio" property="stato" value="2">
						Pubblicato
					</logic:equal>
					<logic:equal name="repertorio" property="stato" value="0">
						Registrato
					</logic:equal>
				</display:column>
				--%>
				<display:column title="Pubblicato" >
					<logic:equal name="repertorio" property="pubblicato" value="true">
					  	<img title="check_pubblicato"  border="0" src="<html:rewrite page='/images/compit/check.gif'/>"/>
					</logic:equal>
				</display:column>
				<display:column title="Protocollato" >
					<logic:equal name="repertorio" property="protocollato" value="true">
					  	<img title="check_protocollato"  border="0" src="<html:rewrite page='/images/compit/check.gif'/>"/>
					</logic:equal>
				</display:column>				
			</display:table>
		</logic:notEmpty> <logic:empty name="repertorioForm" property="documentiDaRepertoriale">
			Nessun documento presente nel repertorio
		</logic:empty></div>

 <logic:notEmpty name="repertorioForm" property="documentiDaRepertoriale">
		<div class="sezione"><span class="title"> <strong>Da Repertoriale</strong>
		</span>
			<display:table class="simple" width="100%"
				name="requestScope.repertorioForm.documentiDaRepertoriale"
				export="false" sort="page" pagesize="15" id="repertorio"
				requestURI="/page/repertorio.do">
				< 
				<display:column title="Id">
					<html:link action="/page/repertorio/documento_repertorio.do"
						paramName="repertorio" paramId="docRepertorialeId"
						paramProperty="docRepertorioId">
						<bean:write name="repertorio" property="oggetto" />
					</html:link>
				</display:column>
				<display:column property="dataDocumento" title="Data" />
				<display:column property="note" title="Note" />
			</display:table>
			</div>
		</logic:notEmpty> 


		<div id="bottoni_salva">
			<html:submit styleClass="submit"property="btnNuovoDocumentoRepertorio" value="Nuovo"alt="Inserisce un nuovo Repertorio" />
			<html:submit styleClass="submit"property="btnStampa" value="Stampa"alt="Stampa il Repertorio" /> 
			<html:submit styleClass="submit" property="btnIndietro" value="Indietro"alt="Indietro" />
		</div>
	</html:form>
</eprot:page>
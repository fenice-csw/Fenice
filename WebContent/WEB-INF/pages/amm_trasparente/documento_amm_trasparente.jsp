<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />
<eprot:page title="Amministrazione Trasparente">
	<div id="protocollo-errori">
		<html:errors bundle="bundleErroriProtocollo" />
	</div>
	<div id="protocollo-messaggi">
		<html:messages id="msg" message="true">
			<ul>
				<li><bean:write name="msg" /></li>
			</ul>
		</html:messages>
	</div>
	<br />
	
	<html:form action="/page/amm_trasparente/documento_amm_trasparente.do"
		enctype="multipart/form-data">
		<table summary="">

			<tr>
				<td><html:hidden property="docSezioneId" /></td>
			</tr>
			
			<tr>
				<td class="label"><label for="numeroDocumentoSezione">N&#186;:</label><span class="obbligatorio">*</span> </td>
				<td><strong>
					<bean:write name="documentoSezioneForm" property="numeroDocumentoSezione" />
					</strong>
				</td>
			</tr>
			 <tr>
				<td class="label"><label for="numeroDocumento">N&#186; Documento:</label></td>
				<td>
					<html:text property="numeroDocumento" size="10" maxlength="32" />
				</td>
			</tr>
			<tr>
				<td class="label"><label for="oggetto"><bean:message
					key="ammtrasparente.oggetto" /></label> <span class="obbligatorio">
				* </span>:</td>
				<td><html:text property="oggetto"
					disabled="false" size="60" maxlength="500">
				</html:text></td>
			</tr>
			<tr>
				<td class="label"><label for="descrizione">Descrizione</label> :</td>
				<td><html:textarea property="descrizione" rows="4" cols="70%"></html:textarea>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="capitolo"><bean:message
					key="ammtrasparentecapitolo" /></label> :</td>
				<td><html:text property="capitolo" size="60" maxlength="150"></html:text></td>
			</tr>
			<tr>
				<td class="label">
					<label title="Data del documento" for="dataSezione">Data Documento</label><span class="obbligatorio">
				* </span>:
				</td>
				<td>
					<html:text styleClass="text" property="dataSezione" styleId="dataSezione" size="10" maxlength="10" /> 
		 			<eprot:calendar textField="dataSezione" />
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="label">
					Pubblicabile dal<span class="obbligatorio">
				* </span>:
				</td>
				<td>
					<html:text styleClass="text" property="dataValiditaInizio" styleId="dataValiditaInizio" size="10" maxlength="10" /> 
		 			<eprot:calendar textField="dataValiditaInizio" />
					&nbsp;
					al<span class="obbligatorio">
				* </span>: <html:text styleClass="text" property="dataValiditaFine" styleId="dataValiditaFine" size="10" maxlength="10" /> 
		 			<eprot:calendar textField="dataValiditaFine" />
				</td>
			</tr>
			<tr>
				<td class="label"><label for="importo"><bean:message
					key="ammtrasparente.importo" /></label> :</td>
				<td><html:text property="importo" size="10" maxlength="10"/> 
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
				<div class="sezione"><span class="title"> <strong>Ufficio Responsabile</strong> </span> <jsp:include
					page="/WEB-INF/subpages/amm_trasparente/uffici.jsp" />
				</div>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
					<div class="sezione"><span class="title"> <strong>Documenti Allegati</strong> </span> <jsp:include
						page="/WEB-INF/subpages/amm_trasparente/documenti.jsp" />
					</div>
				</td>
			</tr>
			<%-- 
			<tr>
				<td>
				</td>
				<td>
					<div class="sezione"><span class="title"> <strong>Documenti Allegati Timbrati</strong> </span> <jsp:include
						page="/WEB-INF/subpages/amm_trasparente/documenti_timbrati.jsp" />
					</div>
				</td>
			</tr>
			--%>
			<logic:equal name="documentoSezioneForm" property="unitaAmministrativa.denominazione" value="POLICLINICO_CT">
			<tr>
				<td>
				</td>
				<td>
				<div class="sezione"><span class="title"> <strong>Settore Proponente</strong> </span> <jsp:include
					page="/WEB-INF/subpages/amm_trasparente/settori.jsp" />
				</div>
				</td>
			</tr>
			</logic:equal>
			<tr>
				<td class="label"><label for="note"><bean:message
					key="ammtrasparente.note" /></label> :</td>
				<td><html:textarea property="note" rows="4" cols="70%"></html:textarea>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
					<html:submit styleClass="submit" property="btnConferma" value="Salva" title="Salva il documento di sezione" />
					<%--  
					<html:submit styleClass="submit" property="btnToPDF" value="Marca Allegati" title="" />
					--%>
					<logic:equal name="documentoSezioneForm" property="btnPubblicaVisibile" value="true">
						<html:submit styleClass="submit" property="btnPubblica" value="Pubblica" title="Pubblica il documento di sezione" /> 
					</logic:equal>
					<logic:equal name="documentoSezioneForm" property="btnProtocollaVisibile" value="true">
						<html:submit styleClass="submit" property="btnProtocolla" value="Protocolla" title="Protocolla come Posta Interna" />
					</logic:equal>
					<html:submit styleClass="submit" property="btnIndietro" value="Indietro" title="Annulla l'operazione" />
				</td>
			</tr>
		</table>
	</html:form>
</eprot:page>

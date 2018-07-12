<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />
<eprot:page title="Amm Trasparente">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriProtocollo" /></div>
	<br />
	<html:form action="/page/amm_trasparente/documento_ammTrasparenteView.do"
		enctype="multipart/form-data">
		<table summary="">
			<tr>
				<td class="label"><label for="numeroDocumentoSezione">N&#186;:</label> </td>
				<td>
					<strong>
					<bean:write name="documentoSezioneForm" property="numeroDocumentoSezione" />
					</strong>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="numeroDocumento">N&#186; Documento:</label></td>
				<td><strong>
					<bean:write name="documentoSezioneForm" property="numeroDocumento" />
					</strong>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="oggetto"><bean:message
					key="ammtrasparente.oggetto" /></label> :</td>
				<td><strong><bean:write name="documentoSezioneForm" property="oggetto" /></strong></td>
			</tr>
			<tr>
				<td class="label"><label for="descrizione">Descrizione</label> :</td>
				<td><strong><bean:write name="documentoSezioneForm" property="descrizione" /></strong>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="capitolo"><bean:message
					key="ammtrasparente.capitolo" /></label> :</td>
				<td><strong><bean:write name="documentoSezioneForm" property="capitolo" /></strong></td>
			</tr>
			<tr>
				<td class="label">
					<label title="Data del documento" for="dataSezione">Data Documento</label>&nbsp;:
				</td>
				<td>
				<strong>
					<bean:write name="documentoSezioneForm" property="dataSezione" />
					</strong>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="label">
					Pubblicabile' dal:
				</td>
				<td>
					<strong>
					<bean:write name="documentoSezioneForm" property="dataValiditaInizio" />
					</strong>
					&nbsp;
					al: 
					<strong>
					<bean:write name="documentoSezioneForm" property="dataValiditaFine" />
					</strong>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="importo"><bean:message
					key="ammtrasparente.importo" /></label> :</td>
				<td>
				<strong>
				<bean:write name="documentoSezioneForm" property="importo" />
				</strong>
				</td>
			</tr>
			<tr>
				<td class="label">Responsabile :</td>
				<td>
				<logic:notEmpty name="documentoSezioneForm" property="ufficioResponsabile">
				<span>
				<strong>
    				<bean:write name="documentoSezioneForm" property="ufficioResponsabile.nomeUfficio"/>
    			</strong>
    			</span>
    			</logic:notEmpty>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
				<div class="sezione"><span class="title"> <strong>Documenti Allegati</strong> </span> <jsp:include
					page="/WEB-INF/subpages/amm_trasparente/documentiView.jsp" />
				</div>
				</td>
			</tr>
			<logic:equal name="documentoSezioneForm" property="unitaAmministrativa.denominazione" value="POLICLINICO_CT">
			<tr>
				<td class="label">Settore Proponente :</td>
				<td>
					<strong>
    					<bean:write name="documentoSezioneForm" property="settoreProponente"/>
    				</strong>
    			</td>
			</tr>
			</logic:equal>
			<tr>
				<td class="label"><label for="note"><bean:message
					key="ammtrasparente.note" /></label> :</td>
				<td><strong><bean:write name="documentoSezioneForm" property="note" /></strong>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
					<html:submit styleClass="submit" property="btnIndietro" value="Indietro" title="Annulla l'operazione" />
				</td>
			</tr>
		</table>
	</html:form>
</eprot:page>

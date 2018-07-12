<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />
<eprot:page title="Repertorio">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriProtocollo" /></div>
	<br />
	<html:form action="/page/repertorio/documento_repertorioView.do"
		enctype="multipart/form-data">
		<table summary="">
			<tr>
				<td class="label"><label for="numeroDocumentoRepertorio">N&#186;:</label> </td>
				<td>
					<strong>
					<bean:write name="documentoRepertorioForm" property="numeroDocumentoRepertorio" />
					</strong>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="numeroDocumento">N&#186; Documento:</label></td>
				<td><strong>
					<bean:write name="documentoRepertorioForm" property="numeroDocumento" />
					</strong>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="oggetto"><bean:message
					key="repertorio.oggetto" /></label> :</td>
				<td><strong><bean:write name="documentoRepertorioForm" property="oggetto" /></strong></td>
			</tr>
			<tr>
				<td class="label"><label for="descrizione">Descrizione</label> :</td>
				<td><strong><bean:write name="documentoRepertorioForm" property="descrizione" /></strong>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="capitolo"><bean:message
					key="repertorio.capitolo" /></label> :</td>
				<td><strong><bean:write name="documentoRepertorioForm" property="capitolo" /></strong></td>
			</tr>
			<tr>
				<td class="label">
					<label title="Data del documento" for="dataRepertorio">Data Documento</label>&nbsp;:
				</td>
				<td>
				<strong>
					<bean:write name="documentoRepertorioForm" property="dataRepertorio" />
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
					<bean:write name="documentoRepertorioForm" property="dataValiditaInizio" />
					</strong>
					&nbsp;
					al: 
					<strong>
					<bean:write name="documentoRepertorioForm" property="dataValiditaFine" />
					</strong>
				</td>
			</tr>
			<tr>
				<td class="label"><label for="importo"><bean:message
					key="repertorio.importo" /></label> :</td>
				<td>
				<strong>
				<bean:write name="documentoRepertorioForm" property="importo" />
				</strong>
				</td>
			</tr>
			<tr>
				<td class="label">Responsabile :</td>
				<td>
				<logic:notEmpty name="documentoRepertorioForm" property="ufficioResponsabile">
				<span>
				<strong>
    				<bean:write name="documentoRepertorioForm" property="ufficioResponsabile.nomeUfficio"/>
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
					page="/WEB-INF/subpages/repertorio/documentiView.jsp" />
				</div>
				</td>
			</tr>
			<logic:equal name="documentoRepertorioForm" property="unitaAmministrativa.denominazione" value="POLICLINICO_CT">
			<tr>
				<td class="label">Settore Proponente :</td>
				<td>
					<strong>
    					<bean:write name="documentoRepertorioForm" property="settoreProponente"/>
    				</strong>
    			</td>
			</tr>
			</logic:equal>
			<tr>
				<td class="label"><label for="note"><bean:message
					key="repertorio.note" /></label> :</td>
				<td><strong><bean:write name="documentoRepertorioForm" property="note" /></strong>
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

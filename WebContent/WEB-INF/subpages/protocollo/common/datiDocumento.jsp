<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<script>
function selectOggetto(){
	document.getElementById("selezionaOggettoButton").click();
}

</script>

<html:submit styleClass="button" property="impostaOggettoAction"
				value="Vai" title="Imposta l'oggetto come corrente" styleId="selezionaOggettoButton" style="display: none;"/>
<div class="sezione"><span class="title"><strong><bean:message
	key="protocollo.documento" /></strong></span>
<table summary="">
	<logic:equal name="protocolloForm" property="flagTipo" value="I">
	  	<logic:notEqual name="protocolloForm" property="messaggioEmailId" value="0">
		  	<tr>
		  		<td></td>
		  		<td colspan="2">
		  			<logic:equal name="protocolloForm" property="flagAnomalia" value="true">
		  				<span class="obbligatorio"><strong>Proveniente da Posta Elettronica non Certificata</strong></span>
		  			</logic:equal>
		  			<logic:equal name="protocolloForm" property="flagAnomalia" value="false">
		  				<span><strong>Proveniente da Posta Elettronica Certificata</strong></span>
		  			</logic:equal>
		  		</td>
		  	</tr>
	  	</logic:notEqual>
  	</logic:equal>
	<tr>
		<td class="label">
			<label for="tipoDocumentoId">
				<bean:message key="protocollo.documento.tipo" />
			</label>
		</td>
		<td>
			<html:select property="tipoDocumentoId" styleClass="obbligatorio" disabled="false">
				<html:option value="0">(vuoto)</html:option>
				<html:optionsCollection property="tipiDocumento" value="id" label="descrizione" />
			</html:select>
		
		&nbsp;&nbsp; 
		<eprot:ifAuthorized permission="isert_reserved">
			<label for="protocolloRiservato">
				<bean:message key="protocollo.mittente.riservato" />:
			</label>
			<html:checkbox property="riservato" styleId="protocolloRiservato" disabled="false" />
		</eprot:ifAuthorized>
		<logic:equal name="protocolloForm" property="flagTipo" value="I">
			<label for="fatturaElettronica">
				<bean:message key="protocollo.fatturaelettronica" />:
			</label>
			<html:checkbox property="fatturaElettronica" styleId="fatturaElettronica" disabled="false" />
		</logic:equal>
		
		<logic:equal name="protocolloForm" property="flagTipo" value="P">
			<label for="flagRepertorio">
				<bean:message key="protocollo.flagrepertorio" />:
			</label> 
			<html:checkbox property="flagRepertorio" styleId="flagRepertorio" disabled="false" />
		</logic:equal>
		
		</td>
	</tr>
	<tr>
                <td class="label"><label title="Data del documento"
                        for="dataDocumento"><bean:message key="protocollo.documento.data" /></label>&nbsp;:
                </td>
                <td><html:text styleClass="text" property="dataDocumento" styleId="dataDocumento" size="10"
                        maxlength="10" />
                 <eprot:calendar textField="dataDocumento" />
                &nbsp;

                <logic:equal name="protocolloForm" property="flagTipo" value="I">
                        <label
                                title="Data in cui il documento &egrave; stato ricevuto"
                                for="dataRicezione"><bean:message
                                key="protocollo.documento.ricevuto" /> </label>:
                        <html:text styleClass="text" property="dataRicezione" styleId="dataRicezione" size="10"
                                maxlength="10" />
                <eprot:calendar textField="dataRicezione" />
                        </td>
                </logic:equal>
                
                 <logic:equal name="protocolloForm" property="flagTipo" value="U">
                        <label
                                title="Data di Evidenza"
                                for="dataRicezione"><bean:message
                                key="protocollo.documento.evidenza" /> </label>:
                        <html:text styleClass="text" property="dataRicezione" styleId="dataRicezione" size="10"
                                maxlength="10" />
                <eprot:calendar textField="dataRicezione" />
                        </td>
                </logic:equal>
        </tr>
	<tr>
		<td class="label">
			<label for="oggettoProtocollo">
				<bean:message key="protocollo.oggetto" />
			</label>&nbsp;<span class="obbligatorio">*</span>&nbsp;:
		</td>
		<logic:equal name="protocolloForm" property="protocolloId" value="0">
			<td colspan="3">
			    <html:select styleClass="obbligatorio" disabled="false" property="oggetto"  onchange="selectOggetto();">
					<html:option value="Altro" ></html:option>
					<html:optionsCollection name="protocolloForm" property="oggettario" value="descrizione" label="descrizione" />	
				</html:select>
			</td>
		</tr> 
			<tr><td></td>
				<td id="oggetto">Specifica l'oggetto in caso di opzione "Altro"</td>
			<tr><td></td>				
				<td>
				<html:textarea property="oggettoGenerico" rows="3" cols="90%"/></td>
			</tr>
		</logic:equal>
			<logic:notEqual name="protocolloForm" property="protocolloId" value="0"> 
			<td colspan="3">
				<span><strong>
				<html:textarea property="oggettoGenerico" cols="30"/></td>
			    </strong></span>
			</td></tr>
			</logic:notEqual>
		
			<logic:greaterThan name="protocolloForm" property="protocolloId" value="0">
			<tr>

	
		<logic:empty name="protocolloForm" property="documentoPrincipale.fileName">
			<td>
				<label
				title="Path del file da allegare"
				for="filePrincipaleUpload"><bean:message
				key="protocollo.documento.file.nome" /></label>&nbsp;: 
			</td>
			<td>
				<html:text property="nomeFilePrincipaleUpload" styleId="nomeFilePrincipaleUpload"/>			
			</td>
			
			<td class="label"><label
				title="Path del file da allegare (deve essere di tipo PDF)"
				for="filePrincipaleUpload"><bean:message
				key="protocollo.documento.file" /></label>&nbsp;:</td>
				
			<logic:equal name="protocolloForm" property="versione" value="0">
				<td colspan="4">
					<html:file styleId="filePrincipaleUpload"
						property="filePrincipaleUpload" /> 
						<html:submit styleClass="button" property="allegaDocumentoPrincipaleAction" value="Allega" title="Allega il file" />
					<logic:equal name="protocolloForm" property="flagTipo" value="I">
						<a id="various3" class="button"  href="<%= request.getContextPath()%>/scanagent2/websocket.jsp?formFileName=filePrincipaleUpload&actionProperties=allegaDocumentoPrincipaleScannAction&jSessID=<%=request.getSession().getId()%>&url=<%= request.getContextPath()%>/page/protocollo/ingresso/documento.do&nProt=<bean:write name='protocolloForm' property='numeroProtocolloScanner' />" >Scanner</a>
					</logic:equal>
					<logic:equal name="protocolloForm" property="flagTipo" value="U">
						<a id="various3" class="button"  href="<%= request.getContextPath()%>/scanagent2/websocket.jsp?formFileName=filePrincipaleUpload&actionProperties=allegaDocumentoPrincipaleScannAction&jSessID=<%=request.getSession().getId()%>&url=<%= request.getContextPath()%>/page/protocollo/uscita/documento.do&nProt=<bean:write name='protocolloForm' property='numeroProtocolloScanner' />" >Scanner</a>		
					</logic:equal>
					<logic:equal name="protocolloForm" property="flagTipo" value="P">
						<a id="various3" class="button"  href="<%= request.getContextPath()%>/scanagent2/websocket.jsp?formFileName=filePrincipaleUpload&actionProperties=allegaDocumentoPrincipaleScannAction&jSessID=<%=request.getSession().getId()%>&url=<%= request.getContextPath()%>/page/protocollo/posta_interna/documento.do&nProt=<bean:write name='protocolloForm' property='numeroProtocolloScanner' />" >Scanner</a>
					</logic:equal>		
				</td>
			</logic:equal>
			
			<logic:notEqual name="protocolloForm" property="versione" value="0">
			
				<td colspan="4">
				
				<html:file styleId="filePrincipaleUpload"
					property="filePrincipaleUpload" /> <html:submit styleClass="button"
					property="allegaDocumentoPrincipaleAction" value="Allega"
					title="Allega il file" />
				
			<logic:equal name="protocolloForm" property="flagTipo" value="I">
				<a id="various3" class="button"  href="<%= request.getContextPath()%>/scanagent2/websocket.jsp?formFileName=filePrincipaleUpload&actionProperties=allegaDocumentoPrincipaleScannAction&jSessID=<%=request.getSession().getId()%>&url=<%= request.getContextPath()%>/page/protocollo/ingresso/documento.do&nProt=<bean:write name='protocolloForm' property='numeroProtocolloScanner' />" >Scanner</a>
			</logic:equal>
			<logic:equal name="protocolloForm" property="flagTipo" value="U">
				<a id="various3" class="button"  href="<%= request.getContextPath()%>/scanagent2/websocket.jsp?formFileName=filePrincipaleUpload&actionProperties=allegaDocumentoPrincipaleScannAction&jSessID=<%=request.getSession().getId()%>&url=<%= request.getContextPath()%>/page/protocollo/uscita/documento.do&nProt=<bean:write name='protocolloForm' property='numeroProtocolloScanner' />" >Scanner</a>
			</logic:equal>
			<logic:equal name="protocolloForm" property="flagTipo" value="P">
				<a id="various3" class="button"  href="<%= request.getContextPath()%>/scanagent2/websocket.jsp?formFileName=filePrincipaleUpload&actionProperties=allegaDocumentoPrincipaleScannAction&jSessID=<%=request.getSession().getId()%>&url=<%= request.getContextPath()%>/page/protocollo/posta_interna/documento.do&nProt=<bean:write name='protocolloForm' property='numeroProtocolloScanner' />" >Scanner</a>
			</logic:equal>
			</logic:notEqual>	
		</logic:empty>
		 
		
		<logic:notEmpty name="protocolloForm" property="documentoPrincipale.fileName">
		<div id="doc_file_name" style="display:none;">OK</div>
			<td class="label"><span title="File allegato"><bean:message
				key="protocollo.documento.file" /></span>&nbsp;:</td>
			<td colspan="3">
				<logic:equal name="protocolloForm" property="flagTipo" value="I">
					<html:link action="/page/protocollo/ingresso/documento.do"
						paramId="downloadDocumentoPrincipale" paramName="protocolloForm"
						paramProperty="documentoPrincipale.fileName" title="Download File">
						<span><strong> <bean:write name="protocolloForm"
							property="documentoPrincipale.fileName" /> </strong></span>
					</html:link>
				</logic:equal> 
			 
				<logic:equal name="protocolloForm" property="flagTipo" value="U">
					<html:link action="/page/protocollo/uscita/documento.do"
						paramId="downloadDocumentoPrincipale" paramName="protocolloForm"
						paramProperty="documentoPrincipale.fileName" title="Download File">
						<span><strong> <bean:write name="protocolloForm"
							property="documentoPrincipale.fileName" /> </strong></span>
					</html:link>
				</logic:equal> 
				
				<logic:equal name="protocolloForm" property="flagTipo" value="P">
					<html:link action="/page/protocollo/posta_interna/documento.do"
						paramId="downloadDocumentoPrincipale" paramName="protocolloForm"
						paramProperty="documentoPrincipale.fileName" title="Download File">
						<span><strong> <bean:write name="protocolloForm"
							property="documentoPrincipale.fileName" /> </strong></span>
					</html:link>
				</logic:equal> 			
				
				<logic:equal name="protocolloForm" property="versione" value="0">
					<html:submit styleClass="button" property="rimuoviDocumentoPrincipaleScannAction" value="Rimuovi" title="Rimuove il documento" />
				</logic:equal>
				
				<logic:notEqual name="protocolloForm" property="versione" value="0">
					<html:submit styleClass="button" property="rimuoviDocumentoPrincipaleAction" value="Rimuovi" title="Rimuove il documento" />
				</logic:notEqual>
		 </td>
		</logic:notEmpty>

	</tr>
		</logic:greaterThan> 
	<tr>
		<jsp:include page="/scanagent/scan_import.jsp" />
	 </tr>
	
</table>
</div>

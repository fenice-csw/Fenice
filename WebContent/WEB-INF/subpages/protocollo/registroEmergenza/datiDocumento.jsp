<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<div class="sezione"><span class="title"><strong><bean:message
	key="protocollo.documento" /></strong></span>
<table summary="">

	<tr>
		<td class="label">
			<label for="tipoDocumentoId">
				<bean:message key="protocollo.documento.tipo" />
			</label>
		<td>
			<html:select property="tipoDocumentoId" styleClass="obbligatorio" disabled="false">
				<html:option value="0">(vuoto)</html:option>
				<html:optionsCollection property="tipiDocumento" value="id" label="descrizione" />
			</html:select>
			&nbsp;&nbsp; 
			<label for="protocolloRiservato">
				<bean:message key="protocollo.mittente.riservato" />
			</label>&nbsp;: 
			<html:checkbox property="riservato" styleId="protocolloRiservato" disabled="false" />
		</td>
	</tr>
	<tr>
		<td class="label"><label title="Data del documento"
			for="dataDocumento"><bean:message key="protocollo.documento.data" /></label>&nbsp;:
		</td>
		<td><html:text styleClass="text" property="dataDocumento" styleId="dataDocumento" size="10"
			maxlength="10" /> 
		 <eprot:calendar textField="dataDocumento" />
		&nbsp;</td>

		<logic:equal name="protocolloForm" property="flagTipo" value="I">
			<td class="label"><label
				title="Data in cui il documento &egrave; stato ricevuto"
				for="dataRicezione"><bean:message
				key="protocollo.documento.ricevuto" /> </label>:</td>
			<td><html:text styleClass="text" property="dataRicezione" styleId="dataRicezione" size="10"
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
		
			
			
			<td colspan="3">
				<span><strong>
				<html:textarea property="oggettoGenerico" cols="30"/></td>
			    </strong></span>
			</td>
		</tr>
		
		
			<logic:greaterThan name="protocolloForm" property="protocolloId" value="0">
			<tr>
			<logic:empty name="protocolloForm" property="documentoPrincipale.fileName">
			
			<td class="label"><label
				title="Path del file da allegare (deve essere di tipo PDF)"
				for="filePrincipaleUpload"><bean:message
				key="protocollo.documento.file" /></label>&nbsp;:</td>
				
			
			
		
				<td colspan="3"><html:file styleId="filePrincipaleUpload"
					property="filePrincipaleUpload" /> <html:submit styleClass="button"
					property="allegaDocumentoPrincipaleAction" value="Allega"
					title="Allega il file" /><input class="button" type="button" value="Da scanner" onClick="javascript:window.location.href='<% out.print(request.getContextPath()+"/scanagent/ScannerJnlpGeneratorServlet?jsess_id="+request.getSession().getId()+"&url="
							+"http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()
							+"/page/protocollo/ingresso/documento.do"
							+"&nprot='+document.getElementById('mio_protnum').value"); %>;" />&nbsp;<input type="button" value="Aggiorna" class="button" onClick="javascript:window.location.reload();"></td>

		
		
		</logic:empty>
		 
		
		<logic:notEmpty name="protocolloForm" property="documentoPrincipale.fileName">
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
				
					<html:submit styleClass="button" property="rimuoviDocumentoPrincipaleAction" value="Rimuovi" title="Rimuove il documento" />
		 </td>
		</logic:notEmpty>

	</tr>
		</logic:greaterThan> 
		
	
</table>
</div>

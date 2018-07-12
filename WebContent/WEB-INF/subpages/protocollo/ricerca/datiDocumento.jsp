<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<table  summary="">
<logic:equal name="ricercaForm" property="tipoProtocollo" value="I">
<tr>
	 <td>
	 		<label title="Data in cui il documento e' stato ricevuto" for="dataRicevutoDa">
		    	<bean:message key="protocollo.documento.ricevuto"/>&nbsp;
		   		<bean:message key="protocollo.da"/>: 
		    </label>
		   	<html:text property="dataRicevutoDa" styleId="dataRicevutoDa" size="10" maxlength="10" />
		    <eprot:calendar textField="dataRicevutoDa" />
			<label  for="dataRicevutoA">
		   		<bean:message key="protocollo.a"/>:
		    </label>
		   	<html:text property="dataRicevutoA" styleId="dataRicevutoA" size="10" maxlength="10" />
		   <eprot:calendar textField="dataRicevutoA" />
	</td>
</tr>
</logic:equal>
<tr>
	<td>
		<label for="dataDocumentoDa"><bean:message key="protocollo.documento.data"/>&nbsp;
		<bean:message key="protocollo.da"/>:</label>
		<html:text property="dataDocumentoDa" styleId="dataDocumentoDa" size="10" maxlength="10" />
		<eprot:calendar textField="dataDocumentoDa" />
		<label for="dataDocumentoA"><bean:message key="protocollo.a"/>:</label>
		<html:text property="dataDocumentoA" styleId="dataDocumentoA" size="10" maxlength="10" />
	    <eprot:calendar textField="dataDocumentoA" />
    </td>
</tr>
<tr>
	<td>
		 <label  for="oggetto"><bean:message key="protocollo.oggetto"/>:</label>
		 <html:text name="ricercaForm" property="oggetto" size="60" />
	</td>
</tr>

<tr>
		<td class="label">
			<label for="tipoDocumentoId">
				<bean:message key="protocollo.documento.tipo" />
			</label>
			<html:select property="tipoDocumentoId" styleClass="obbligatorio" disabled="false">
				<html:option value="0">(vuoto)</html:option>
				<html:optionsCollection property="tipiDocumento" value="id" label="descrizione" />
			</html:select>	
		</td>
	</tr>
  </table>
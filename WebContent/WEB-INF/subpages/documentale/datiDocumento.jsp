<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<html:hidden property="cartellaId"/>

<div class="sezione">
<span class="title"><strong><bean:message key="documentale.documento"/></strong></span>
<table summary="">
  <tr>
    <td class="label">
      <label for="tipoDocumentoId"><bean:message
          key="protocollo.documento.tipo"/></label><span
          class="obbligatorio"> * </span>:
    </td>
    <td>
      <html:select styleClass="obbligatorio" property="tipoDocumentoId">
        <html:optionsCollection property="tipiDocumento" value="id" label="descrizione" />
      </html:select>
    </td>
  </tr>
  <tr>


<logic:empty name="documentoForm" property="documentoPrincipale.fileName">
    <td class="label">
      <label title="Path del file da allegare" for="filePrincipaleUpload"><bean:message key="documentale.file"/></label><span
          class="obbligatorio"> * </span>:
    </td>
    <td colspan="3">
      <html:file styleClass="obbligatorio" styleId="filePrincipaleUpload" property="filePrincipaleUpload"/>
      <html:submit styleClass="button" property="allegaDocumentoPrincipaleAction" value="Allega" title="Allega il file" />
    </td>
</logic:empty>    


<logic:notEmpty name="documentoForm" property="documentoPrincipale.fileName">
    <td class="label">
      <span title="File allegato"><bean:message key="documentale.file"/>: </span>
    </td>
    <td colspan="3">
	  <html:link page="/page/documentale/documento.do" 
	             paramId="downloadDocumentoPrincipale" 
	             paramName="documentoForm" 
	             paramProperty="documentoPrincipale.fileName" 
	             title="Download File">
        <span><strong><bean:write name="documentoForm" property="documentoPrincipale.fileName" /></strong></span>
      </html:link>
	  <html:submit styleClass="button" property="rimuoviDocumentoPrincipaleAction" value="Rimuovi" title="Rimuove il documento" />
    </td>
</logic:notEmpty>    

  </tr>
  <tr>
    <td class="label">
      <label title="Data del documento" for="dataDocumento"><bean:message key="protocollo.documento.data"/><span
          class="obbligatorio"> * </span>:
    </td>
    <td>
      <html:text styleClass="obbligatorio" property="dataDocumento" styleId="dataDocumento" size="10" maxlength="10" />
      <eprot:calendar textField="dataDocumento" />
      &nbsp;
    </td>
  </tr>
	<tr>
	    <td class="label">
	      <label for="descrizione"><bean:message key="documentale.descrizione"/></label>&nbsp;:
	    </td>
	    <td>
			<html:text property="descrizione" styleId="descrizione" size="60" maxlength="255" />
	    </td>
	</tr> 

	<tr>
	    <td class="label">
	      <label for="descrizioneArgomento"><bean:message key="documentale.argomento"/></label>&nbsp;:
	    </td>
	    <td>
			<html:text property="descrizioneArgomento" styleId="descrizioneArgomento" size="60" maxlength="255" />
	    </td>
	</tr> 
	<tr>
	    <td class="label">
	      <label for="oggettoDocumento"><bean:message
	          key="protocollo.oggetto"/><span class="obbligatorio"> * </span></label>&nbsp;:
	          <!--  
	          <br/>
	          <span><html:link action="/page/unicode.do?campo=oggettoDocumento" target="_blank" >segni diacritici</html:link></span>
	          -->
	          
	    </td>
	    <td colspan="3">
	      <html:textarea styleClass="obbligatorio" property="oggetto" styleId="oggettoDocumento" cols="52" rows="2"></html:textarea>
	    </td>
	</tr>
	<tr>
	    <td class="label">
	      <label for="noteDocumento"><bean:message key="documentale.note"/></label>&nbsp;:
	      <!-- 
	      <br/>
	          <span><html:link action="/page/unicode.do?campo=noteDocumento" target="_blank" >segni diacritici</html:link></span>
	           -->
	    </td>
	    <td colspan="3">
	      <html:textarea property="note" styleId="noteDocumento" cols="52" rows="2"></html:textarea>
	    </td>
	</tr>
</table>
</div>

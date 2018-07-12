<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<div class="sezione">
<span class="title"><strong><bean:message key="documentale.documento"/></strong></span>
<table summary="">
  <tr>
    <td class="label">
      <label for="tipoDocumentoId"><bean:message key="protocollo.documento.tipo"/></label>&nbsp;:
    </td>
    <td>
      <html:select styleClass="obbligatorio" disabled="true" property="tipoDocumentoId">
        <html:optionsCollection property="tipiDocumento" value="id" label="descrizione" />
      </html:select>
    </td>
  </tr>
  <tr>
    <td class="label">
      	<span title="Data del documento"><bean:message key="protocollo.documento.data"/></span>&nbsp;:
    </td>
    <td>
    	<span><strong><bean:write name="documentoForm" property="dataDocumento" /></strong></span>&nbsp;
    </td>
  </tr>
  <tr>
    <td class="label">
      <span><bean:message key="documentale.descrizione"/></span>&nbsp;:
    </td>
    <td>
	    <span><strong><bean:write name="documentoForm" property="descrizione"/></strong></span>
    </td>
  </tr>
  <tr>
    <td class="label">
      <span><bean:message key="documentale.argomento"/>:</span>
    </td>
    <td>
		<strong><span><bean:write name="documentoForm" property="descrizioneArgomento" /></span></strong>
    </td>
  </tr> 
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.oggetto"/></span>&nbsp;:
    </td>
    <td>
	    <span><strong><bean:write name="documentoForm" property="oggetto" filter="false"/></strong></span>
    </td>
  </tr>
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.annotazioni.note"/></span>&nbsp;:
    </td>
    <td>
	    <span><strong><bean:write name="documentoForm" property="note" filter="false"/></strong></span>
    </td>
  </tr>

<logic:notEmpty name="documentoForm" property="documentoPrincipale.fileName">
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.documento.file"/></span>:
    </td>
    <td>
      <html:link page="/page/documentale/documentoView.do" paramId="downloadDocumentoPrincipale"
				paramName="documentoForm"
				paramProperty="documentoPrincipale.fileName" target="_blank"
				title="Download File">		
		<span><strong>
			<bean:write name="documentoForm" property="documentoPrincipale.fileName"/>
			
			</strong></span>
	  </html:link>
    </td>
  </tr>
</logic:notEmpty>  
  <tr>
    <td class="label">
      <span><bean:message key="documentale.stato"/></span>:
    </td>
    <td>
    	<span>	    
		<logic:equal name="documentoForm" property="statoArchivio" value="L">
		<strong>Lavorazione</strong>
		<logic:notEqual name="documentoForm" property="statoDocumento" value="0">
			&nbsp;(<bean:message key="documentale.checkedout"/>: <bean:write name="documentoForm" property="usernameLav"/>)
		 </logic:notEqual>
		
		</logic:equal>
		<logic:equal name="documentoForm" property="statoArchivio" value="C"><bean:message key="documentale.classificato"/></logic:equal>
		<logic:equal name="documentoForm" property="statoArchivio" value="I"><bean:message key="documentale.inviatoprotocollo"/></logic:equal>
		<logic:equal name="documentoForm" property="statoArchivio" value="R"><bean:message key="documentale.inviatorepertorio"/></logic:equal>
		</span>
    </td>
  </tr>

<logic:notEmpty name="documentoForm" property="titolario">
  <tr>
    <td class="label">
      <span><bean:message key="documentale.classificazione"/>:</span>
    </td>
    <td>
		<strong>
		<span><bean:write name="documentoForm" property="titolario.path"/> - <bean:write name="documentoForm" property="titolario.descrizione" /></span>
		</strong>

    </td>
  </tr> 
</logic:notEmpty>
<logic:notEmpty name="documentoForm" property="fascicoliDocumento">
  <tr>
    <td class="label">
      <span><bean:message key="documentale.fascicoli"/>:</span>
    </td>
    <td>  
    	<ul>
		<logic:iterate id="currentRecord" property="fascicoliDocumento" name="documentoForm">
			<li><span><strong><bean:write name="currentRecord" property="annoProgressivo" /> - <bean:write name="currentRecord" property="oggetto"/></strong></span></li>
		</logic:iterate>		
		</ul>
    </td>
  </tr>
</logic:notEmpty>   

</table>
</div>
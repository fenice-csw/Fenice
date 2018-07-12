<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<div class="sezione">
<span class="title"><strong><bean:message key="protocollo.documento"/></strong></span>
<table summary="">

  <tr>
    <td class="label">
      <label for="tipoDocumentoId"><bean:message key="protocollo.documento.tipo"/></label>&nbsp;:
    </td>
    <td>
      <html:select styleClass="obbligatorio" disabled="true" property="tipoDocumentoId">
    	<logic:notEmpty name="visualizzaProtocolloForm" property="tipoDocumentoId">
        <html:optionsCollection property="tipiDocumento" value="id" label="descrizione" />
        </logic:notEmpty>
        <logic:empty name="visualizzaProtocolloForm" property="tipoDocumentoId">
        <html:option value="0">(vuoto)</html:option>
        </logic:empty>
      </html:select>
		&nbsp;&nbsp;
		<label for="protocolloRiservato"><bean:message key="protocollo.mittente.riservato"/></label>&nbsp;:
		<html:checkbox property="riservato" styleId="protocolloRiservato" disabled="true" />

    </td>
  </tr>
  <tr>
    <td class="label">
      	<span title="Data del documento"><bean:message key="protocollo.documento.data"/></span>&nbsp;:
    </td>
    <td>
    	<span><strong>
    	<bean:write name="visualizzaProtocolloForm" property="dataDocumento" />
    	</strong></span>&nbsp;
    </td>

    <logic:equal name="visualizzaProtocolloForm" property="flagTipo" value="I">
	    <td class="label">
	    	<span title="Data in cui il documento &egrave; stato ricevuto"><bean:message key="protocollo.documento.ricevuto"/> </span>:
	    </td>
	    <td>
			<span><strong>
			<bean:write name="visualizzaProtocolloForm" property="dataRicezione" />
			</strong></span>
		</td>    
	</logic:equal>

  </tr>
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.oggetto"/></span>&nbsp;:
    </td>
    <td colspan="3">
	    <span><strong>
		<input type="hidden" value="<bean:write name="visualizzaProtocolloForm" property="oggetto"/>" name="oggetto"></input>
	    <bean:write name="visualizzaProtocolloForm" property="oggetto" filter="false" />
	    </strong></span>
    </td>
  </tr>
<logic:equal name="visualizzaProtocolloForm"  property= "documentoVisibile" value="false">
	<tr>
	    <td class="label">
	      <span><bean:message key="protocollo.documento.file"/></span>:
	    </td>
	    <td colspan="3">
			<span><strong>*** Riservato ***</strong></span>
		</td>
	</tr>
</logic:equal>
<logic:equal name="visualizzaProtocolloForm"  property= "documentoVisibile" value="true">
	<logic:notEmpty name="visualizzaProtocolloForm" property="documentoPrincipale.fileName">	
	  <tr>
	    <td class="label">
	      <span><bean:message key="protocollo.documento.file"/></span>:
	    </td>
	    <td colspan="3">
	   
		  <html:link action="/page/protocollo/allaccioview.do" 
		             paramId="downloadDocumentoPrincipale" 
		             paramName="visualizzaProtocolloForm" 
		             paramProperty="documentoPrincipale.fileName" 
		             target="_blank"
		             title="Download File">
	        	<span><strong>
					<bean:write name="visualizzaProtocolloForm" property="documentoPrincipale.fileName" />
	        	</strong></span>
	      </html:link>
	    </td>
	  </tr>
	</logic:notEmpty>  
</logic:equal>

</table>
</div>
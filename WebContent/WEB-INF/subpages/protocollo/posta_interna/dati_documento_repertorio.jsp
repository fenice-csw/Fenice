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
    	<logic:notEmpty name="protocolloForm" property="tipoDocumentoId">
        <html:optionsCollection property="tipiDocumento" value="id" label="descrizione" />
        </logic:notEmpty>
        <logic:empty name="protocolloForm" property="tipoDocumentoId">
        <html:option value="0">(vuoto)</html:option>
        </logic:empty>
      </html:select>
    </td>
    <td>
		&nbsp;&nbsp;
		<label for="protocolloRiservato"><bean:message key="protocollo.mittente.riservato"/>:</label>
		<html:checkbox property="riservato" styleId="protocolloRiservato" disabled="true" />
		<logic:equal name="protocolloForm" property="flagTipo" value="I">
			<label for="fatturaElettronica"><bean:message key="protocollo.fatturaelettronica"/>:</label>
			<html:checkbox property="fatturaElettronica" styleId="fatturaElettronica" disabled="true" />
    	 </logic:equal>
    	 <logic:equal name="protocolloForm" property="flagTipo" value="P">
			<label for="flagRepertorio"><bean:message key="protocollo.flagrepertorio"/>:</label>
			<html:checkbox property="flagRepertorio" styleId="flagRepertorio" disabled="true" />
    	 </logic:equal>
    </td>
   
  </tr>
  <tr>
    <td class="label">
      	<span title="Data del documento"><bean:message key="protocollo.documento.data"/></span>&nbsp;:
    </td>
    <td>
    	<span><strong>
    	<bean:write name="protocolloForm" property="dataDocumento" />
    	</strong></span>&nbsp;
    </td>

  </tr>
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.oggetto"/></span>&nbsp;:
    </td>
    <td colspan="3">
	    <span><strong>
		<input type="hidden" value="<bean:write name="protocolloForm" property="oggetto"/>" name="oggetto"></input>
	    <bean:write name="protocolloForm" property="oggetto" filter="false" />
	    </strong></span>
    </td>
  </tr>
<logic:equal name="protocolloForm"  property= "documentoVisibile" value="false">
	<tr>
	    <td class="label">
	      <span><bean:message key="protocollo.documento.file"/></span>:
	    </td>
	    <td colspan="3">
			<span><strong>*** Riservato ***</strong></span>
		</td>
	</tr>
</logic:equal>
<logic:equal name="protocolloForm"  property= "documentoVisibile" value="true">
	<logic:notEmpty name="protocolloForm" property="documentoPrincipale.fileName">	
	  <tr>
	    <td class="label">
	      <span><bean:message key="protocollo.documento.file"/></span>:
	    </td>
	    <td colspan="3">

			<logic:equal name="protocolloForm" property="flagTipo" value="P">
				<html:link action="/page/protocollo/posta_interna/invio_repertorio.do" 
		             paramId="downloadDocumentoPrincipale" 
		             paramName="protocolloForm" 
		             paramProperty="documentoPrincipale.fileName" 
		             title="Download File">
	        		<span><strong>
					<bean:write name="protocolloForm" property="documentoPrincipale.fileName" />
	        		</strong></span>
				</html:link>
			</logic:equal> 
 
	    </td>
	  </tr>
	</logic:notEmpty>  
</logic:equal>

</table>
</div>
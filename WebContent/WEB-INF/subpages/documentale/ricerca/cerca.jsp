<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>


<html:xhtml />
<table>
	<tr>  
	    <td align="left" colspan="2">
		<label for="descrizioneArgomento"><bean:message key="documentale.file.argomento"/>&nbsp;:</label>
		<html:text property="descrizioneArgomento" styleId="descrizioneArgomento" size="50" maxlength="255"/>
	    </td>
	<tr>  	
	<tr>
  		<td align="left" colspan="2">
    	<span><bean:message key="protocollo.documento.data"/></span>
    	<label for="dataDocumentoDa">
			<bean:message key="protocollo.da"/>&nbsp;:
		</label>
		<html:text property="dataDocumentoDa" styleId="dataDocumentoDa" size="10" maxlength="10" />
		 <eprot:calendar textField="dataDocumentoDa" />
		&nbsp;&nbsp;

    	<label for="dataDocumentoA">
			<bean:message key="protocollo.a"/>&nbsp;:
		</label>
		<html:text property="dataDocumentoA" styleId="dataDocumentoA" size="10" maxlength="10" />
		<eprot:calendar textField="dataDocumentoA" />
    	</td>   
	</tr>
	<tr>
		<td align="left">
			<label for="nome">
				<bean:message key="documentale.file.name"/>&nbsp;:
			</label>
			<html:text property="nomeFile" styleId="nome" size="25" maxlength="100" />
			&nbsp;&nbsp;
			<label for="oggetto">			
				<bean:message key="documentale.file.oggetto"/>&nbsp;:
			</label>
			<html:text property="oggetto" styleId="oggetto" size="50" maxlength="100" />
		</td>
			
	</tr> 
 	<tr>
  		<td align="left">
 		<label for="tipoDocumentoId">
				<bean:message key="documentale.tipo_documento"/>&nbsp;:
			</label>
       		<html:select styleClass="" property="tipoDocumentoId">
       			<html:option value="-1"><bean:message key="documentale.file.tutti"/></html:option>
        		<html:optionsCollection property="tipiDocumento" value="id" label="descrizione" />
      		</html:select>
		</td> 
 	</tr>
 	
 	<tr>
	    <td align="left" >
	      <label for="descrizione"><bean:message key="documentale.descrizione"/></label>&nbsp;:
	      <html:text property="descrizione" styleId="descrizione" size="30" maxlength="255" /> 
	      &nbsp;
	      <label for="noteDocumento"><bean:message key="documentale.note"/></label>&nbsp;:
			<html:text property="note" styleId="noteDocumento" size="30" maxlength="255" />	      
	    </td>
	</tr>

 	<tr>
	    <td align="left" >
	      <label for="ownerId"><bean:message key="documentale.utente_owner"/></label>&nbsp;:
	      <html:select property="ownerId">
   	    		<html:option value="0"><bean:message key="documentale.file.tutti"/></html:option>
   	    		<html:optionsCollection property="owners" value="id" label="cognomeNome" />
     		</html:select>	      
	    </td>
	</tr>
 	<tr>
	    <td align="left">
			<jsp:include page="/WEB-INF/subpages/documentale/ricerca/uffici.jsp" />
	    </td>
	</tr>
	
	<tr>
	    <td align="left" >
	    </td>
	</tr> 
	
	<tr>
 		<td align="left">
 		<label for="stato">
				<bean:message key="documentale.stato_documento"/>&nbsp;:
			</label>
 			<html:select property="statoDocumento">
   	    		<html:option value=""><bean:message key="documentale.file.tutti"/></html:option>
   	    		<html:optionsCollection property="statiDocumentoCollection" value="codice" label="description" />
     		</html:select>

     		<html:submit styleClass="submit" property="btnCerca" value="Cerca" alt="Cerca"/>
		<html:reset styleClass="submit"
			property="ResetAction" value="Annulla" alt="Annulla i parametri di ricerca" />
<%--		    <html:submit styleClass="button" property="btnAnnulla" value="Annulla" alt="Annulla i parametri di ricerca"/> 		    --%>
       		<logic:equal name="documentoForm" property="indietroVisibile" value="true" >
			<html:submit styleClass ="submit" property="indietro" value="Indietro" alt="Indietro" />
		</logic:equal>	  
		</td>  
 	</tr> 
	<logic:equal name="documentoForm" property="ricercaFullText" value="true" >
	 	<tr>
			<td align="left" colspan="2">
				<hr />
				<label for="testo">
					<bean:message key="documentale.file.testo"/>&nbsp;:
				</label>
				<html:text property="testo" styleId="testo" size="60" maxlength="255" />
			    <html:submit styleClass="submit" property="btnRicercaFullText" value="Ricerca full text" alt="Ricerca full text"/>
		    </td>  
		</tr>
	</logic:equal>
</table>	

  


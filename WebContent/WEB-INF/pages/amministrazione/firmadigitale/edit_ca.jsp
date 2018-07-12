<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<eprot:page title="Gestione Firma Digitale">
	<bean:define id="url" value="/page/amministrazione/firmadigitale/edit_ca.do" scope="request"/>
	<logic:messagesPresent message="true">
   		<html:messages id="actionMessage" property="salvato" message="true" bundle="bundleMessaggiAmministrazione">
     		<li>
     		 <bean:write name="actionMessage"/>
      		</li>
   		</html:messages> 
    </logic:messagesPresent>
	<div id="protocollo-errori">
    	<html:errors bundle="bundleErroriAmministrazione"/>
    </div>
	<html:form action="/page/amministrazione/firmadigitale/edit_ca.do" enctype="multipart/form-data">
	    <table summary="">
		  <tr>  
		    <td class="label">
		      <span><bean:message key="ca.issuercn" bundle="bundleMessaggiAmministrazione"/></span>
		    </td>  
		    <td>
		    	<span><strong>
		    		<bean:write name="certificateAuthorityForm" property="issuerCN" />
		    	</strong></span>
		    </td>
		  </tr>
		  <tr>  
		    <td class="label">
		      <span><bean:message key="ca.valido_dal" bundle="bundleMessaggiAmministrazione"/></span>
		    </td>  
		    <td>
		    	<span><strong>
		    		<bean:write name="certificateAuthorityForm" property="validoDal" />
		    	</strong></span>
		    </td>
		  </tr>
		  <tr>  
		    <td class="label">
		      <span><bean:message key="ca.valido_al" bundle="bundleMessaggiAmministrazione"/>:</span>
		    </td>  
		    <td>
		    	<span><strong>
			    	<bean:write name="certificateAuthorityForm" property="validoAl" />
		    	</strong></span>
		    </td>
		  </tr>
		  <tr>  
		    <td class="label">
		      <span><bean:message key="ca.import.file" bundle="bundleMessaggiAmministrazione"/>:</span>
		    </td>
		    <td>
		      <html:file property="formFileUpload" />
		      <html:submit styleClass="submit" property="importa" value="Importa" alt="Importa Ca da Certificato" />
		    </td>
		  </tr>
		</table>
		
		<table summary="">
		  <tr>  
		    <td colspan="2">	      
		      <span><bean:message key="ca.crl.dplink" bundle="bundleMessaggiAmministrazione"/> :</span>
		    </td>
		  </tr>
		  <logic:notEmpty property="crlsCollection" name="certificateAuthorityForm">
		  <logic:iterate id="recordCRL" property="crlsCollection" name="certificateAuthorityForm">
		  <tr>  
		    <td class="label">
		     <bean:define id="recordCRLUrl" name="recordCRL" property="url" />
		     <html:multibox property="crlSelezionatiId"><bean:write name="recordCRL" property="url" /></html:multibox>
		    </td>
		    <td class="label">
		     <html:text name="recordCRL" property="url" maxlength="500" size="70"/>
		    </td>
		  </tr>
		  </logic:iterate> 
		  </logic:notEmpty>
		  <tr>  
			<td colspan="2" class="label">
				<hr width="100%" height="1"/>
			</td>
		  </tr>
		  <tr>  
			<td colspan="2" class="label">
			   <label for="addCrlUrl"><bean:message key="ca.crl.link" bundle="bundleMessaggiAmministrazione"/></label>
				<html:text name="certificateAuthorityForm" property="addCrlUrl" styleId="addCrlUrl" maxlength="500" size="60"/><html:submit styleClass="button" property="aggiungiCRL" value="Aggiungi" alt="Aggiungi CRL" />
			</td>
		  </tr>
	    </table>
    	<logic:notEmpty name="certificateAuthorityForm" property="crlsCollection">
  			<html:submit styleClass="button" property="rimuoviCRL" value="Rimuovi selezionati" alt="Rimuovi le CRL selezionate" />
		</logic:notEmpty>
		<html:submit styleClass="button" property="salva" value="Salva" alt="Salva" />
	</html:form>
</eprot:page>

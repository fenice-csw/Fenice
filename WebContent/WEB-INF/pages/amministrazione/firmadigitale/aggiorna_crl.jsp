<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione Firma Digitale">

	<logic:messagesPresent message="true">
   		<html:messages id="actionMessage" property="salvato" message="true" bundle="bundleMessaggiAmministrazione">
     		<li><span><strong><bean:write name="actionMessage"/></strong></span>
      		</li>
   		</html:messages> 
    </logic:messagesPresent>
	<div id="protocollo-errori">
    	<html:errors bundle="bundleErroriAmministrazione"/>
	</div>
	<html:form action="/page/amministrazione/firmadigitale/aggiorna_crl.do" enctype="multipart/form-data">
		<div class="sezione">
		<span class="title"><strong><bean:message key="amministrazione.firmadigitale.certificateauthority"/>:</strong></span>
		<table summary="">
		  <tr>
		    <td colspan="2" class="label">
		      <label for="formFileUpload"><bean:message
		          key="firmadigitale.ca.db_file" bundle="bundleMessaggiAmministrazione"/></label><span
		          class="obbligatorio"> * </span>:
		    </td>
		  </tr>
		  <tr>
		    <td colspan="2">
		      <html:file styleId="formFileUpload" property="formFileUpload"/>
		      <html:submit styleClass="submit" property="importaCa" value="Aggiorna" alt="Aggiungi/Aggiorna CA Ora!"/>
		    </td>
		  </tr>
		 </table>
		</div>
		<div class="sezione">
		<span class="title"><strong><bean:message key="amministrazione.firmadigitale.certificaterevocationlists"/>:</strong></span>
		<table summary="">
		  <tr>
		    <td class="label">
		      <label for="aggiorna"><bean:message
		          key="firmadigitale.crl.aggiorna" bundle="bundleMessaggiAmministrazione"/></label>
		    </td>
		    <td>
		      <html:submit styleClass="submit" property="aggiorna" value="Aggiorna" alt="Aggiorna Ora!"/>
		    </td>
		  </tr>
		 </table>
		      <span><br /><bean:message key="amministrazione.firmadigitale.attenzione"/>!!!<br /><bean:message key="amministrazione.firmadigitale.messaggio"/>.</span>
		</div>
	</html:form>
	
	
	
</eprot:page>

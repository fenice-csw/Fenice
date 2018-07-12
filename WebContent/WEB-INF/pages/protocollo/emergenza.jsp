<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Registro d'emergenza - prenota protocolli">	
	<div id="protocollo-errori">
	<html:errors bundle="bundleErroriProtocollo" />
	</div>
	<html:form action="/page/protocollo/emergenza.do">
	<table summary="">
	  <tr>
	    <td class="label">
	      <label title="Data Registrazione" for="dataRegistrazioneProtocollo">
	      <bean:message key="protocollo.dataregistrazione"/></label>&nbsp;:
	    </td>
	    <td>
	      <html:text styleClass="text" property="dataRegistrazioneProtocollo" styleId="dataRegistrazioneProtocollo" size="10" maxlength="10" />
	     <eprot:calendar textField="dataRegistrazioneProtocollo" />
	      &nbsp;
	    </td>
	
	
	  </tr>
	</table>

	</html:form>

</eprot:page>


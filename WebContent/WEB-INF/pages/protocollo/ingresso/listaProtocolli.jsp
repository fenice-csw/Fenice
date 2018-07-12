<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<eprot:page title="Protocolli per mittente">

<html:form action="/page/protocollo/ingresso/documento.do">
<div>  
<logic:notEmpty name="protocolloIngressoForm" property="protocolliMittente">
<p>
<strong><span><bean:message key="protocollo.ricerca.trovati"/></span></strong>: 
<!--<span>${protocolloIngressoForm.numeroProtocolliMittente}</span>-->
	<span><bean:write name="protocolloIngressoForm" property="numeroProtocolliMittente"/></span>
</p>

<table summary="" border="1">
  <tr>
    <th><span><bean:message key="protocollo.numero"/></span></th>
    <th><span><bean:message key="protocollo.anno"/></span></th>	
    <th><span><bean:message key="protocollo.tipo"/></span></th>	    
    <th><span><bean:message key="protocollo.data"/></span></th>
    <th><span><bean:message key="protocollo.mittente"/></span></th>
    <th><span><bean:message key="protocollo.oggetto"/></span></th>
</tr>
<logic:notEmpty property="protocolliMittente" name="protocolloIngressoForm">
<logic:iterate id="currentRecord" property="protocolliMittente" name="protocolloIngressoForm">
<tr>
    <td>
		<span><bean:write name="currentRecord" property="numeroProtocollo"/></span>
	</td>
    <td>
		<span><bean:write name="currentRecord" property="annoProtocollo"/></span>
	</td>
    <td>
		<span><bean:write name="currentRecord" property="tipoProtocollo"/></span>
	</td>
    <td>
		<span><bean:write name="currentRecord" property="dataProtocollo"/></span>
	</td>
    <td>
		<span><bean:write name="currentRecord" property="mittente"/></span>
	</td>
    <td>
		<span><bean:write name="currentRecord" property="oggetto"/></span>
	</td>
</tr>	
</logic:iterate>  
</logic:notEmpty>   
</table>
<br />
<p>	
	<html:submit styleClass="submit" property="btnAnnulla" value="Indietro" alt="Torna alla pagina di protocollo"/>
</p>
</logic:notEmpty>
</div>
</html:form>
</eprot:page>


<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Ricerca Protocolli">

<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" property="protocolloSelezionato"/>
</div>
<span><strong><bean:message key="protocollo.ricerca.trovati"/></strong>: 
	<bean:write name="ricercaForm" property="numeroProtocolli"/>
</span>

<html:form action="/page/protocollo/ricerca.do">

	<div>  
	
	<logic:notEmpty name="ricercaForm" property="protocolliCollection">
	
		<table summary="">
		<tr>
		    <th><span><bean:message key="protocollo.numero"/></span></th>
		    <th><span><bean:message key="protocollo.anno"/></span></th>	
		    <th><span><bean:message key="protocollo.tipo"/></span></th>	    
		    <th><span><bean:message key="protocollo.data"/></span></th>
		    <th><span><bean:message key="protocollo.mittente"/></span></th>
		    <th><span><bean:message key="protocollo.oggetto" /></span></th>
			<th><span><bean:message key="protocollo.pdf"/></span></th>
			<th><span><bean:message key="protocollo.stato"/></span></th>
		</tr>
		
		<logic:iterate id="currentRecord" property="protocolliCollection" name="ricercaForm">
		<tr>
		    <td><span>
				<html:radio property="protocolloSelezionato" value="protocolloId" idName="currentRecord">
		    	</html:radio>
				<bean:write name="currentRecord" property="numeroProtocollo"/></span>
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
		    	<span><bean:write name="currentRecord" property="oggetto" /></span>
			</td>
		    <td>
		    	<span><bean:write name="currentRecord" property="pdf"/></span>
			</td>
		    <td>
		    	<span><bean:write name="currentRecord" property="statoProtocollo"/></span>
			</td>
		</tr>	
		</logic:iterate>     
		</table>
	</logic:notEmpty>
	<p>	
	<logic:notEmpty name="ricercaForm" property="protocolliCollection">
		<html:submit styleClass="submit" property="btnModificaProtocollo" value="Modifica" alt="Modifica protocollo"/>
		<html:submit styleClass="submit" property="btnAnnullaProtocollo" value="Annulla protocollo" alt="Annulla protocollo"/>
	</logic:notEmpty>
		<html:submit styleClass="submit" property="btnAnnulla" value="Nuova ricerca" alt="Nuova ricerca"/>
	</p>
	
	</div>
</html:form>
</eprot:page>
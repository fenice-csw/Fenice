<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Gestione allacci">


<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" property="allaccioProtocolloDa"/>
  <html:errors bundle="bundleErroriProtocollo" property="allaccioProtocolloA"/>
  <html:errors bundle="bundleErroriProtocollo" property="allaccioIntervallo"/>
  <html:errors bundle="bundleErroriProtocollo" property="allaccioProtocolloAnno"/>
  <html:errors
			bundle="bundleErroriProtocollo" />
</div>

<html:form action="/allacciProtocollo.do">
<table summary="">
	<tr>
		<td class="label">
			<label for="allaccioProtocolloDa"><bean:message key="protocollo.allacci.allaccioDa"/>:</label>
		</td>
		<td>					
			<html:text property="allaccioProtocolloDa" size="8" maxlength="10"></html:text> 
		</td>
		<td class="label">			
			<label for="allaccioProtocolloA"><bean:message key="protocollo.allacci.allaccioA"/>: </label>
		</td>			
		<td>			
			<html:text property="allaccioProtocolloA" size="8" maxlength="10"></html:text> 
		</td>
		<td class="label">			
			<label for="allaccioProtocolloAnno"><bean:message key="protocollo.allacci.anno"/>: </label>
		</td>
		<td>		
			<html:text property="allaccioProtocolloAnno" size="4" maxlength="10"></html:text> 
		</td>
		<td>		
			<html:submit styleClass="submit" property="btnCercaAllacci" value="Cerca" title="Cerca Protocolli Allacciabili"/>
		</td>
	</tr>
</table>
<hr></hr>
<jsp:include page="/WEB-INF/subpages/protocollo/allacci/listaAllacci.jsp" />
</html:form>

</eprot:page>

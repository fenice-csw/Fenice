<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione mezzi di spedizione">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>

<html:form action="/page/amministrazione/mezzispedizione/spedizione.do">
<table summary="">
	<tr>
		<td>		
			<html:text property="descrizioneSpedizione"></html:text> 
		</td>
		<td>		
			<html:submit styleClass="submit" property="btnCercaSpedizioni" value="Cerca" title="Cerca mezzi di spedizione"/>
			<html:submit styleClass="submit" property="btnNuovaSpedizione" value="Nuovo" title="Inserisce un nuovo mezzo di spedizione"/>
		</td>
	</tr>
</table>
<hr></hr>
<logic:notEmpty name="spedizioneForm" property="mezziSpedizione">
<table summary=""  cellpadding="2" cellspacing="2" border="1">
<tr>
  <th><span><bean:message key="spedizione.descrizione"/></span></th>
   <th><span><bean:message key="spedizione.prezzo"/></span></th>
  <th><span><bean:message key="spedizione.abilitato"/></span></th>
  <th><span><bean:message key="spedizione.cancellabile"/></span></th>  
</tr>
<logic:iterate id="currentRecord" property="mezziSpedizione" name="spedizioneForm">
<tr>
  <td>
	<span>
	<html:link action="/page/amministrazione/mezzispedizione/spedizione.do" paramId="parId" paramName="currentRecord" paramProperty="id">
	<bean:write name="currentRecord" property="descrizioneSpedizione"/>
	</html:link >
	</span>
  </td>
    <td>
	<span>
	<bean:write name="currentRecord" property="prezzo"/>
	</span>
  </td>
  <th>
	<logic:equal value="true" name="currentRecord" property="flagAbilitato">
	<span><bean:message key="spedizione.si"/></span>
	</logic:equal> 
	<logic:equal value="false" name="currentRecord" property="flagAbilitato">
	<span><bean:message key="spedizione.no"/></span>
	</logic:equal> 
  </th>
  <th>
	<logic:equal value="true" name="currentRecord" property="flagCancellabile">
	<span><bean:message key="spedizione.si"/></span>
	</logic:equal> 
	<logic:equal value="false" name="currentRecord" property="flagCancellabile">
	<span><bean:message key="spedizione.no"/></span>
	</logic:equal> 
  </th>
</tr>
</logic:iterate>     
</table>
</logic:notEmpty>
</html:form>
</eprot:page>

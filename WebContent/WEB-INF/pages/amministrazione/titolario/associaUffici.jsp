<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Titolario">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>
<html:form action="/page/amministrazione/titolario.do">
<table summary="">
	<tr>  
		<td class="label">
			<label for="codice"><bean:message key="protocollo.argomento.codice"/></label>:
		</td>  
		<td><span><strong>
			<bean:write name="titolarioForm" property="codice" />
			</strong></span>
			<html:hidden property="id"/>
		</td>  
	</tr>
	<tr>  
    	<td class="label">
      		<label for="descrizione"><bean:message key="protocollo.argomento.descrizione"/></label>:
    	</td>  
		<td><span><strong>
		
		<bean:write name="titolarioForm" property="descrizione" />
		</strong></span>
    	</td>  
	</tr>
</table>
<br />
<div id="content">
<table>	
	<tr>  
		<td class="label">
			<span><bean:message key="protocollo.argomento.uffici"/>:</span>
		</td>  
		<td>
			<span>
			<logic:notEmpty name="titolarioForm" property="ufficiDipendenti">
			<logic:iterate id="currentRecord" property="ufficiDipendenti" name="titolarioForm">
				<html:multibox property="ufficiTitolario">
				<bean:write name="currentRecord" property="valueObject.id" />
				</html:multibox>
				<bean:write name="currentRecord" property="path"/><br/>
			</logic:iterate>    
			</logic:notEmpty>    
			</span>				    
	    </td>
	</tr>
</table>
</div>
<br />
<div id="bottoni_salva">
	<logic:greaterThan value="0" name="titolarioForm" property="id">
		<html:submit styleClass="submit" property="btnConfermaAssociazione" value="Associa gli uffici" title="Associa l'argomento selezionato agli uffici" />
	</logic:greaterThan>
	<html:submit styleClass="submit" property="btnAnnulla" value="Annulla" title="Annulla l'operazione" />
</div>
</html:form>

</eprot:page>

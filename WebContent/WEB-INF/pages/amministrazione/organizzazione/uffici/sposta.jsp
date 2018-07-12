<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Gestione uffici">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" />
	</div>

	<logic:messagesPresent message="true">
		<div id="protocollo-messaggi">
		<ul>
			<html:messages id="actionMessage" message="true"
				bundle="bundleErroriAmministrazione">
				<li><bean:write name="actionMessage" /></li>
			</html:messages>
		</ul>
		</div>
	</logic:messagesPresent>
	<!-- ufficioDaSpostare.ufficioDiAppartenenza -->
	

<table summary="">
	<tr>
    	
    	<td class="label">
      		<span><bean:message key="amministrazione.organizzazione.uffici.ufficiopadre"/>: </span>
    	</td>
    	<td>
	      		<span title="<bean:write name='ufficioForm' property='ufficioDaSpostare.ufficioDiAppartenenza.path' />">
	      		<strong>
					<bean:write name="ufficioForm" property="ufficioDaSpostare.ufficioDiAppartenenza.path"/>
	      		</strong></span>
	    </td>
	</tr>
	<tr>  
    	<td class="label">
    		<label for="description"><bean:message bundle="bundleMessaggiAmministrazione" key="descrizione"/>
      		</label>:
    	</td>  
    	<td>
      		<bean:write name="ufficioForm" property="ufficioDaSpostare.valueObject.description"/>
    	</td>  
	</tr>
	</table>

	<html:form
		action="/page/amministrazione/organizzazione/spostaUfficio.do">
<div class="sezione">
<span class="title"><strong><bean:message key="protocollo.argomento.sposta"/></strong><br/></span>
		<jsp:include
			page="/WEB-INF/subpages/amministrazione/organizzazione/uffici/spostaUfficio.jsp" />
</div>
<html:submit styleClass="submit" property="btnConfermaSposta" value="Sposta" title="Sposta l'ufficio selezionato" />
<html:submit styleClass="submit" property="btnAnnullaSposta" value="Annulla" title="Annulla l'operazione" />

	</html:form>
</eprot:page>

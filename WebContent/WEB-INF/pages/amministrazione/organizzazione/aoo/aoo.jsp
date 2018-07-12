<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<eprot:page title="Area Organizzativa">
	<div id="protocollo-errori">
		<html:errors bundle="bundleErroriProtocollo" />
	</div>
	<div id="protocollo-messaggi">
	 	<logic:messagesPresent message="true">
  			<ul>
	   			<html:messages id="actionMessage" message="true" bundle="bundleErroriProtocollo">
		      		<li>
		      			<bean:write name="actionMessage"/>
		      		</li>
	   			</html:messages> 
  	 		</ul>
  	 	</logic:messagesPresent>
	</div>
	
	<html:form action="/page/amministrazione/organizzazione/aoo/aoo.do">
		<table summary="">
			
				<logic:empty name="areaOrganizzativaForm" property="areeOrganizzative">
					<p><span><strong><bean:message key="amministrazione.organizzazione.aoo.messaggio"/>.</strong></span></p>
				</logic:empty>
				<logic:notEmpty name="areaOrganizzativaForm" property="areeOrganizzative">
				<logic:iterate id="aoo" name="areaOrganizzativaForm" property="areeOrganizzative">
					<tr>
					<td>
					<html:radio property="id" value="valueObject.id" idName="aoo">
						<span><bean:write name="aoo" property="valueObject.description" /></span>
					</html:radio>
					</td>
					</tr>
					
				</logic:iterate>
				</logic:notEmpty>	
				
		</table>
		<br />
		<br />
		<p>
		    <html:submit styleClass="submit" property="btnModifica" value="Modifica" alt="Modifica" /> 
			<html:submit styleClass="submit" property="btnNuovo" value="Nuovo" alt="Inserisce una nuova area organizzativa" /> 
			<html:submit styleClass="submit" property="btnCancella" value="Cancella" alt="Cancella l'area organizzativa" />
		</p>
	</html:form>
</eprot:page>

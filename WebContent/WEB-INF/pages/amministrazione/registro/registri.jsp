<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Registri">
	<div id="protocollo-errori">
	<jsp:include page="/WEB-INF/subpages/amministrazione/common/errori.jsp" />
	</div>
	<html:form action="/page/amministrazione/registri.do">

	
	<logic:empty name="registroForm" property="registri" >
		<p><span><strong><bean:message key="amministrazione.registro.messaggio"/>.</strong></span></p>
	</logic:empty>	
	
	
	<logic:notEmpty name="registroForm" property="registri" >
		<table summary="">
			<tr>
				<th></th>
				<th><span><bean:message key="amministrazione.registro.registro"/></span></th>
				<th><span><bean:message key="amministrazione.registro.descrizione"/></span></th>				
			</tr>
			
			<logic:iterate id="registro" name="registroForm" property="registri">
			<tr>
				<td>
					<span>
						<bean:define id="id" name="registro" property="id" />
						<html:radio property="id" value="id" idName="registro" />
					</span>
				</td>
				<td>
					<span>
						<bean:write name="registro" property="codRegistro" />
					</span>
				</td>
				<td>
					<span>
						<bean:write name="registro" property="descrizioneRegistro" />
					</span>
				</td>
			</tr>
			</logic:iterate>
			
		</table>
		<br />
		<p>
		    <html:submit styleClass="submit" property="btnModifica" value="Seleziona" alt="Seleziona il registro" /> 
			<html:submit styleClass="submit" property="btnNuovo" value="Nuovo" alt="Inserisce un nuovo registro" /> 
		</p>
	</logic:notEmpty>
	
	</html:form>
</eprot:page>

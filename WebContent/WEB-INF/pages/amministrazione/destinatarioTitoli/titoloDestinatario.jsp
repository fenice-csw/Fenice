<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione Titoli Destinatario">

	<div id="protocollo-errori">
		<jsp:include page="/WEB-INF/subpages/amministrazione/common/errori.jsp" />
	</div>
	<html:form action="/page/amministrazione/titoliDestinatario.do">
		<html:hidden property="id"/>
		<table summary="">
			<tr>  
		    	<td class="label">
		    		<logic:greaterThan name="titoloDestinatarioForm" property="id" value="0" >
		    			<label for="titolo"><bean:message bundle="bundleMessaggiAmministrazione" key="id"/>
		      			<span class="obbligatorio"> * </span></label>:
		      		</logic:greaterThan>
		    	</td>  
		    	<td>
		      		<logic:greaterThan name="titoloDestinatarioForm" property="id" value="0" >
		      			<html:text disabled="true" property="id" styleClass="obbligatorio" size="32" maxlength="32">
		      			</html:text>
		      		</logic:greaterThan>
		      		<logic:equal name="titoloDestinatarioForm" property="id" value="0">
		      			<html:hidden disabled="false" property="id"/>
		      		</logic:equal>
		      			
		    	</td>  
			</tr>
			<tr>
				<td class="label">
		    		<label for="titolo"><bean:message bundle="bundleMessaggiAmministrazione" key="descrizione"/>
		      		<span class="obbligatorio"> * </span></label>:
		    	</td>  
		    	<td>
					<html:text property="descrizione" styleClass="obbligatorio" styleId="titolo" size="32" maxlength="32"></html:text>
				</td>
			</tr>
			<tr>
				<td colspan="2">		
					<html:submit styleClass="submit" property="btnSalva" value="Salva" title="Inserisce il nuovo titolo"></html:submit>
					<html:submit styleClass="button" property="btnIndietro" value="Annulla" title="Annulla l'operazione"></html:submit>
				</td>
			</tr>	
		</table>	
	</html:form>
</eprot:page>

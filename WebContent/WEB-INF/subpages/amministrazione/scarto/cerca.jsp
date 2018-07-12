<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<div>  

<logic:empty name="scartoProtocolliForm" property="anniScartabili">
<span><strong><bean:message key="amministrazione.scarto.messaggio1"/>.</strong></span>

</logic:empty>

<logic:notEmpty name="scartoProtocolliForm" property="anniScartabili">
	<span><bean:message key="amministrazione.scarto.messaggio2"/>: <span class="obbligatorio"> * </span></span>
	<html:select styleClass="obbligatorio" property="anno">
        <html:options property ="anniScartabili" />
	</html:select>&nbsp;
	
    <html:submit styleClass="submit" property="btnConferma" value="Conferma" alt="Scarto protocolli" /> 
	<html:submit styleClass="button" property="btnAnnulla" value="Annulla" alt="Annulla" />
</logic:notEmpty>	

</div>


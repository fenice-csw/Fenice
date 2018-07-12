<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<div>
<logic:equal name="reportRegistroForm" property="recordNotFound" value="true">
	<span>Il registro di protocollo del <strong><bean:write name="reportRegistroForm" property="anno" /></strong> non contiene record</span>
</logic:equal>

<logic:equal name="reportRegistroForm" property="showReportDownload" value="true">

	<span>Il registro di protocollo del <strong><bean:write name="reportRegistroForm" property="anno" /></strong> contiene <strong><bean:write name="reportRegistroForm" property="totalReg" /></strong> record.</span>
	<p>
		<html:submit styleClass="submit" property="btnDownload" value="Scarica" alt="Scarica il file zip del Registro Annuale"/>
		<%--  
		<logic:equal name="reportRegistroForm" property="gaAbilitata" value="true">
			<html:submit styleClass="submit" property="btnInviaGestioneArchivi" value="Invia a Conservazione" alt="Invio del Registro Annuale a GestioneArchivi"/>
		</logic:equal>
		--%>
	</p>
</logic:equal>  

</div>


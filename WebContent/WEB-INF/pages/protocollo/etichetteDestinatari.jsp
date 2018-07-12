<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<head>
<html:base />
</head>

<%--fmt:formatNumber value="${protocolloForm.numero}" maxIntegerDigits="7"
	minIntegerDigits="7" groupingUsed="false" /--%>

<br />

<br />

<applet name="barcode" width="200" height="300"
	align="middle"
	archive="barcode-print-applet.jar,barcode4j.jar"
	code="it.finsiel.siged.mvc.presentation.applet.BarCodePrintApplet.class"
	codebase="../../../download/">
	<param name="MargineSinistro" value="5">
	<param name="MargineSuperiore" value="0">
	<param name="LarghezzaEtichetta" value="50">
	<param name="AltezzaEtichetta" value="35">
	<bean:define id="protocolloId" name="protocolloForm" property="protocolloId" />
	<param name="CodiceBarre" value="<bean:write name='protocolloForm' property='protocolloId' />">
	<bean:define id="description" name="UTENTE_KEY" property="areaOrganizzativa.description" />
<%--	<param name="Riga_1" value="<bean:write name='UTENTE_KEY' property='areaOrganizzativa.description' />">--%>
	<logic:iterate id="destinatari" name="protocolloForm" property="destinatari">
		<param name="Riga_1" value="<bean:write name='destinatari' property='destinatario' />">
	</logic:iterate>	
	<bean:define id="ufficioCorrente" name="protocolloForm" property="ufficioCorrente.description"/>
	<bean:define id="dataRegistrazioneToEtichetta" name="protocolloForm" property="dataRegistrazioneToEtichetta"/>
	<%-- param name="Riga_2" name="protocolloForm" property="ufficioCorrente.description" --%>
	<logic:equal name="protocolloForm" property="flagTipo" value="U" >
		<param name="Riga_3" value="Uscita del <bean:write name='protocolloForm' property='dataRegistrazioneToEtichetta'/>"/>
	</logic:equal>
	<logic:equal name="protocolloForm" property="flagTipo" value="I" >	
		<param name="Riga_3" value="Ingresso del <bean:write name='protocolloForm' property='dataRegistrazioneToEtichetta'/>"/>
	</logic:equal>
	<logic:equal name="protocolloForm" property="flagTipo" value="P" >	
		<param name="Riga_3" value="Posta Interna del <bean:write name='protocolloForm' property='dataRegistrazioneToEtichetta'/>"/>
	</logic:equal>	
	<bean:define id="etichetta" name="protocolloForm" property="dataRegistrazioneToEtichetta" />
	
	<bean:define id="numeroEtichetta" name="protocolloForm" property="numeroEtichetta"/> 
	<param name="Riga_4" value="<bean:write name='protocolloForm' property='numeroEtichetta'/>"/>
	
	<logic:notEmpty name="protocolloForm" property="titolario">
		<bean:define id="titolario" name="protocolloForm" property="titolario"/>
	</logic:notEmpty>	
	
	<logic:notEmpty name="protocolloForm" property="titolario">	
		<param name="Riga_5" value="titolario"/> Installare Java 1.4 o superiore.
	</logic:notEmpty>
		
	<logic:empty name="protocolloForm" property="titolario">		
		<param name="Riga_5" value=""/> Installare Java 1.4 o superiore.
	</logic:empty>
	


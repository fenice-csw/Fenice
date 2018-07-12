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
<applet name="barcode" 
	archive="barcode-print-applet.jar,barcode4j.jar"
	code="it.finsiel.siged.mvc.presentation.applet.BarcodePrintApplet.class"
	codebase="../../../download/">
	<param name="MargineSinistro" value="<bean:write name="PARAMETRI_ETICHETTA" scope="request" property="margineSinistro"/>">
	<param name="MargineSuperiore" value="<bean:write name="PARAMETRI_ETICHETTA" scope="request" property="margineSuperiore"/>">
	<param name="LarghezzaEtichetta" value="<bean:write name="PARAMETRI_ETICHETTA" scope="request" property="larghezzaEtichetta"/>">
	<param name="AltezzaEtichetta" value="<bean:write name="PARAMETRI_ETICHETTA" scope="request" property="altezzaEtichetta"/>">
	<bean:define id="protocolloId" name="protocolloForm" property="protocolloId" />
	<param name="CodiceBarre" value="<bean:write name='protocolloForm' property='protocolloId' />">
	<param name="Riga_1" value="<bean:write name='UTENTE_KEY' property='areaOrganizzativa.description' />">
	<logic:equal name="protocolloForm" property="flagTipo" value="U" >
		<param name="Riga_3" value="Uscita del <bean:write name='protocolloForm' property='dataRegistrazioneToEtichetta'/>"/>
	</logic:equal>
	<logic:equal name="protocolloForm" property="flagTipo" value="I" >	
		<param name="Riga_3" value="Ingresso del <bean:write name='protocolloForm' property='dataRegistrazioneToEtichetta'/>"/>
	</logic:equal>	
	<logic:equal name="protocolloForm" property="flagTipo" value="P" >	
		<param name="Riga_3" value="Posta Interna del <bean:write name='protocolloForm' property='dataRegistrazioneToEtichetta'/>"/>
	</logic:equal>	
	<param name="Riga_4" value="<bean:write name='protocolloForm' property='numeroEtichetta'/>"/>
	<%--  
	<logic:notEmpty name="protocolloForm" property="titolario">	
		<param name="Riga_5" value="<bean:write name='protocolloForm' property='titolario.codice'/>"/> 
	</logic:notEmpty>
	--%>
Installare Java 1.4 o superiore.	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
</applet>

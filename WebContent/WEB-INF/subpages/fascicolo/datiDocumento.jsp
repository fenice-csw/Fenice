<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<div>
	
	<label><bean:message key="protocollo.documento.tipo"/>:</label>
	<html:select name="fascicoloForm" property="tipoDocumento">
		<option value="">Tutti</option>
		<bean:define id="tipiDocumento" name="fascicoloForm" property="tipiDocumento" />
		<html:optionsCollection name="tipiDocumento" value="id" label="descrizione"/>
	</html:select>&nbsp;&nbsp;
	
	
	<logic:equal name="fascicoloForm" property="tipoProtocollo" value="I">
		<label title="Data in cui il documento e' stato ricevuto" for="dataRicevutoDa">
	    	<bean:message key="protocollo.documento.ricevuto"/>&nbsp;
	   		<bean:message key="protocollo.da"/>: 
	    </label>
	   	<html:text property="dataRicevutoDa" styleId="dataRicevutoDa" size="10" maxlength="10" />
	    <eprot:calendar textField="dataRicevutoDa" />
	    &nbsp;
		<label  for="dataRicevutoA">
	   		<bean:message key="protocollo.a"/>:
	    </label>
	   	<html:text property="dataRicevutoA" styleId="dataRicevutoA" size="10" maxlength="10" />
	    <eprot:calendar textField="dataRicevutoA" />
	</logic:equal>

<br />
<%--	<label><bean:message key="protocollo.documento.data"/>&nbsp;-->
<!--	<bean:message key="protocollo.da"/>:</label>-->
<!--	<html:text property="dataDocumentoDa" size="10" maxlength="10" />-->
<!--    <script type="text/javascript">-->
<!--    -->
<!--      document.write("<img id='imgdataDocumentoDa' src=\"<html:rewrite page='/images/calendar/calendario.gif'/> \" alt='' title='Seleziona la data' />");-->
<!--      Calendar.setup({-->
<!--        inputField     : "dataDocumentoDa", // id of the input field-->
<!--        ifFormat       : "%d/%m/%Y",      // format of the input field-->
<!--        button         : "imgdataDocumentoDa",    // trigger for the calendar (button ID)-->
<!--        firstDay       : 1,-->
<!--        weekNumbers    : false-->
<!--      });-->
<!--    // -->
<!--    </script>-->
<!--    <noscript></noscript>-->
<!--	&nbsp;&nbsp;-->
<!--	<label><bean:message key="protocollo.a"/>:</label>-->
<!--	<html:text property="dataDocumentoA" size="10" maxlength="10" />-->
<!--    <script type="text/javascript">-->
<!--    -->
<!--      document.write("<img id='imgdataDocumentoA' src=\"<html:rewrite page='/images/calendar/calendario.gif'/> \" alt='' title='Seleziona la data' />");-->
<!--      Calendar.setup({-->
<!--        inputField     : "dataDocumentoA", // id of the input field-->
<!--        ifFormat       : "%d/%m/%Y",      // format of the input field-->
<!--        button         : "imgdataDocumentoA",    // trigger for the calendar (button ID)-->
<!--        firstDay       : 1,-->
<!--        weekNumbers    : false-->
<!--      });-->
<!--    // -->
<!--    </script>-->
<!--    <noscript></noscript>-->
<!--<br />-->
<!--	<label><bean:message key="protocollo.oggetto"/>:</label>-->
<!--	<html:text property="oggetto" size="60" />-->
<!---->
<!--<br />-->
<!--	<label><bean:message key="fascicolo.progressivo"/>:</label>-->
<!--	<html:text property="progressivoFascicolo" size="15" />--%>
</div>
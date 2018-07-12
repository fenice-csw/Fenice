<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<html:errors  bundle="bundleErroriProtocollo" property="sezione_allaccio"/>

<jsp:include page="/WEB-INF/subpages/protocollo/allacci/testataGestioneAllacci.jsp" />





<br />

<logic:notEqual name="editorForm" property="allaccio.protocolloAllacciatoId" value="0">
	<p>
  	  	
		<span><strong>Collegato: </strong><bean:write name="editorForm" property="allaccio.allaccioDescrizione" /></span>	
  	<html:submit styleClass="button" property="rimuoviAllacciAction" value="Rimuovi" title="Rimuovi i protocolli selezionati dall'elenco degli allacci"/>
</p>
</logic:notEqual>



<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />


<bean:define value="/page/fascicoli.do?documentoSelezionato=" scope="request" id="urlDocumento" />
<bean:define value="/page/fascicoli.do?" scope="request" id="url" />
<logic:empty name="fascicoloForm" property="documentiFascicolo">
<span><bean:message key="fascicolo.messaggio3"/></span><br /><br />
</logic:empty>

<logic:notEmpty name="fascicoloForm" property="documentiFascicolo">
	<display:table class="simple" width="95%" requestURI="/page/fascicolo/allaccioview.do"
		name="sessionScope.fascicoloForm.documentiFascicolo"
		export="false" sort="list" pagesize="10" id="row">
		<display:column title="File name" property="nomeFile"/>
		<display:column property="descrizione" title="Descrizione"  />
		<display:column property="dataDocumento" title="Data" decorator="it.finsiel.siged.mvc.presentation.helper.DateDecorator"/>
		<display:column title="Stato">		
		<logic:equal name="row" property="statoArchivio" value="L"><bean:message key="fascicolo.lavorazione"/></logic:equal>
		<logic:equal name="row" property="statoArchivio" value="C"><bean:message key="fascicolo.classificato"/></logic:equal>
		</display:column>				
	</display:table>
</logic:notEmpty>
<div>

</div>

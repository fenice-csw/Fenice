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
	<display:table class="simple" width="95%" requestURI="/page/fascicoli.do"
		name="sessionScope.fascicoloForm.documentiFascicolo"
		export="false" sort="list" pagesize="10" id="row">
		
		<logic:equal name="fascicoloForm" property="statoFascicolo" value="0">
			<logic:equal name="row" property="visibileDaFascicolo" value="true">
				<logic:equal name="fascicoloForm" property="versioneDefault" value="true">
					<display:column title="">
						<html:radio property="documentoSelezionato" idName="row" value="id"></html:radio>
					</display:column>
				</logic:equal>
			</logic:equal>
		</logic:equal>	
		<display:column title="File name">
			<logic:equal name="row" property="visibileDaFascicolo" value="true">
				<html:link paramId="documentoSelezionato" paramName="row"
					paramProperty="id" page="/page/documentale/cerca.do">
					<span><bean:write name="row" property="nomeFile" /></span>
				</html:link>
			</logic:equal>
			<logic:equal name="row" property="visibileDaFascicolo" value="false">
				<span><bean:write name="row" property="nomeFile" /></span>
			</logic:equal>
		</display:column>		
		<display:column property="descrizione" title="Descrizione"  />
		<display:column property="dataDocumento" title="Data"  decorator="it.finsiel.siged.mvc.presentation.helper.DateDecorator"/>
		<display:column title="Stato">		
			<logic:equal name="row" property="statoArchivio" value="L"><bean:message key="fascicolo.lavorazione"/></logic:equal>
			<logic:equal name="row" property="statoArchivio" value="C"><bean:message key="fascicolo.classificato"/></logic:equal>
		</display:column>				
	</display:table>
</logic:notEmpty>
<div>

<logic:notEqual name="fascicoloForm" property="visibilita" value="NESSUNA">
<br class="hidden" />
	<logic:equal name="fascicoloForm" property="versioneDefault" value="true">
		<logic:equal name="fascicoloForm" property="statoFascicolo" value="0">
			<html:submit styleClass="submit" property="btnAggiungiDocumenti" value="Aggiungi" title="Inserisce un documento dell'archivio corrente nel fascicolo" />
			<logic:notEmpty name="fascicoloForm" property="documentiFascicolo">
				<html:submit styleClass="submit" property="btnRimuoviDocumento" value="Rimuovi" title="Rimuove il documento selezionato dal fascicolo" />
			</logic:notEmpty>	
		</logic:equal>
	</logic:equal>
</logic:notEqual>

</div>

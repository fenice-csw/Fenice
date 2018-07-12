<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Ricerca per Archivio Deposito">

<jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />

<html:form action="/page/ricerca_deposito.do">
<table>
	<tr>
		<td>
			<span><bean:message key="fascicolo.data.apertura" /></span>
		
			<label title="Data chiusura minima" for="dataChiusuraDa"><bean:message key="protocollo.da" /></label>: 
			<html:text property="dataChiusuraDa"styleId="dataChiusuraDa" size="10" maxlength="10" /> 
			<eprot:calendar textField="dataChiusuraDa" /> &nbsp; 
		
			<label title="Data chiusura massima" for="dataChiusuraA"><bean:message key="protocollo.a" /></label>: 
			<html:text property="dataChiusuraA" styleId="dataChiusuraA" size="10" maxlength="10" /> 
			<eprot:calendar textField="dataChiusuraA" />
		</td>
	</tr>
	<tr>
	<td>
		<html:submit styleClass="submit" property="btnFiltra" value="Filtra" title="Filtra Fascicoli Chiusi" />
	</td>
	</tr>
</table>
	<div class="sezione"><span class="title"> <strong>Fascicoli Chiusi</strong> </span> 
		<logic:notEmpty name="archivioDepositoForm" property="fascicoliCollection">
			<display:table class="simple" width="100%"
				name="sessionScope.archivioDepositoForm.fascicoliCollection" export="false"
				sort="page" pagesize="15" id="fascicolo"
				requestURI="/page/ricerca_deposito.do">
				<display:column title=""> 
					<input type="checkbox" name="fascicoloChkBox" value="<bean:write name='fascicolo' property='id'/>"/>
				</display:column>
				<display:column property="annoProgressivo" title="N.fascicolo" />
				<display:column property="oggetto" title="Oggetto" />
				<display:column property="dataApertura" title="Data Apertura" />
				<display:column property="dataChiusura" title="Data Chiusura" />
				<display:column property="descrizioneUfficioIntestatario" title="Ufficio" />
				<display:column property="pathTitolario" title="Classificazione" />
			</display:table>
			<br/>
	<html:submit styleClass="button" property="btnAggiungi" value="Aggiungi" title="Aggiungi i fascicoli selezionati all'elenco" />
		</logic:notEmpty> 
		<logic:empty name="archivioDepositoForm" property="fascicoliCollection">
			Nessun fascicolo chiuso presente nell'archivio Permanente
		</logic:empty>
		
	</div>

<logic:notEmpty name="archivioDepositoForm" property="fascicoliSelezionatiCollection">
	<div class="sezione"><span class="title"> <strong>Elenco Versamento Deposito</strong> </span>
	<display:table class="simple" width="100%"
				name="sessionScope.archivioDepositoForm.fascicoliSelezionatiCollection" export="false"
				sort="page" pagesize="15" id="fasc"
				requestURI="/page/ricerca_deposito.do">
				<display:column title=""> 
					<input type="checkbox" name="fascicoliIds" value="<bean:write name='fasc' property='id'/>"/>
				</display:column>
				<display:column property="annoProgressivo" title="N.fascicolo" />
				<display:column property="oggetto" title="Oggetto" />
				<display:column property="dataApertura" title="Data Apertura" />
				<display:column property="dataChiusura" title="Data Chiusura" />
				<display:column property="descrizioneUfficioIntestatario" title="Ufficio" />
				<display:column property="pathTitolario" title="Classificazione" />
			</display:table>
	<br/>
	<html:submit styleClass="button" property="btnRimuovi" value="Rimuovi dall'elenco" title="Rimuove i fascicoli selezionati dall'elenco" />
	</div>
	<html:submit styleClass="button" property="btnConferma" value="Conferma Versamento" title="Sposta i fascicoli selezionati nell'archivio Deposito" />
	
</logic:notEmpty>
</html:form>
</eprot:page>
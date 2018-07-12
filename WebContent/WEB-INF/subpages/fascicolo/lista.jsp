<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<html:xhtml />
<logic:notEmpty name="ricercaFascicoliForm" property="fascicoli">
	<div>
	<display:table class="simple" width="95%"
		requestURI="/page/fascicoli.do"
		name="sessionScope.ricercaFascicoliForm.fascicoli" export="false" sort="list"
		pagesize="40" id="row">
		<display:column title="">
			<logic:notEqual scope="session" name="tornaFaldone" value="true">
				<logic:notEqual scope="session" name="tornaProcedimento"
					value="true">
					<html:radio property="fascicoloSelezionato" idName="row" value="id"></html:radio>
				</logic:notEqual>
			</logic:notEqual>
			<logic:equal scope="session" name="tornaFaldone" value="true">
				<input type="checkbox" name="fascicoliSelezionati"
					value="<bean:write name='row' property='id'/>" />
			</logic:equal>
			<logic:equal scope="session" name="tornaProcedimento" value="true">
				<input type="checkbox" name="fascicoliSelezionati"
					value="<bean:write name='row' property='id'/>" />
			</logic:equal>
		</display:column>
		<display:column property="annoProgressivo" title="N.fascicolo" />
		<display:column property="oggetto" title="Oggetto" />
		<display:column property="dataApertura" title="Data Creazione" />
		<display:column property="descrizioneUfficioIntestatario" title="Ufficio" />
		<display:column property="pathTitolario" title="Classificazione" />
		<display:column property="descrizioneStato" title="Stato" />
	</display:table>
	<p><html:submit styleClass="submit" property="btnSeleziona"
		value="Seleziona" alt="Seleziona" /> 
		<html:submit styleClass="button" property="btnAnnullaRicerca" value="Annulla" alt="Annulla"/>
		<html:submit styleClass="submit" property="btnStampa" value="Stampa" alt="Stampa la ricerca" />			
	</p>
	
	</div>
</logic:notEmpty>

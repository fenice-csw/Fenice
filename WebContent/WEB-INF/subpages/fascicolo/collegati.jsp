<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<html:xhtml />				
	<p>	
		<label for="oggettoFascicoloCollegato"><bean:message key="fascicolo.oggetto"/>:</label>
		<html:text property="oggettoFascicoloCollegato" styleId="oggettoFascicoloCollegato" size="40" maxlength="100"></html:text>
		<html:submit styleClass="submit" property="cercaFascicoloCollegatoAction" value="Cerca" title="Cerca Fascicolo Collegato"/>
	</p>
				
<logic:notEmpty name="fascicoloForm" property="fascicoliCollegati">
	
	<div>
		<display:table class="simple" width="95%"
			requestURI="/page/fascicoli.do"
			name="sessionScope.fascicoloForm.fascicoliCollegati" export="false" sort="list"
			pagesize="40" id="row">
			<display:column title="">
				<html:multibox property="collegamentiSelezionatiId">
		    		<bean:write name="row" property="id"/>
		    	</html:multibox>
			</display:column>
			<display:column title="N.fascicolo">
				<html:link action="/page/fascicolo.do" paramId="visualizzaCollegamentoId" paramName="row" paramProperty="id">
 					<bean:write name="row" property="annoProgressivo" />
				</html:link >
			</display:column>
			<display:column property="oggetto" title="Oggetto" />
			<display:column property="dataApertura" title="Data Creazione" />
			<display:column property="descrizioneUfficioIntestatario" title="Ufficio" />
			<display:column property="pathTitolario" title="Classificazione" />
			<display:column property="descrizioneStato" title="Stato" />
		</display:table>
	</div>
	<br />
	<html:submit styleClass="button" property="rimuoviCollegatiAction" value="Rimuovi" title="Rimuove i fascicoli collegati selezionati" />
</logic:notEmpty>
<logic:empty name="fascicoloForm" property="fascicoliCollegati">
	<span><bean:message key="fascicolo.messaggio6"/></span>
</logic:empty>
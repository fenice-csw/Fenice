<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<html:xhtml />				
				
<logic:notEmpty name="fascicoloForm" property="sottoFascicoli">
	
	<div>
		<display:table class="simple" width="95%"
			requestURI="/page/fascicoli.do"
			name="sessionScope.fascicoloForm.sottoFascicoli" export="false" sort="list"
			pagesize="40" id="row">
			
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
</logic:notEmpty>
<logic:empty name="fascicoloForm" property="sottoFascicoli">
	<span><bean:message key="fascicolo.messaggio7"/></span>
</logic:empty>
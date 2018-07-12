<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />

<logic:notEmpty name="faldoneForm" property="fascicoliFaldoneCollection">
<div>
	<display:table class="simple" width="95%" requestURI="/page/faldone.do?Paginazione=true"
		name="sessionScope.faldoneForm.fascicoliFaldoneCollection"
		export="false" sort="list" pagesize="10" id="row">
		<display:column title="">
			<input type="checkbox" name="fascicoliSelezionati"
				value="<bean:write name='row' property='fascicoloVO.id'/>" />
		</display:column>
		<display:column title="Numero Fascicolo">
			<html:link action="/page/faldone.do" paramName="row" paramId="visualizzaFascicoloId" paramProperty="fascicoloVO.id">
				<bean:write name="row" property="fascicoloVO.annoProgressivo" />
			</html:link>
		</display:column>	
		<display:column property="fascicoloVO.oggetto" title="Oggetto" />
		<display:column property="fascicoloVO.dataApertura" title="Data Apertura" decorator="it.finsiel.siged.mvc.presentation.helper.DateDecorator"/>
		<display:column property="fascicoloVO.dataEvidenza" title="Data Evidenza" decorator="it.finsiel.siged.mvc.presentation.helper.DateDecorator"/>
		<display:column property="fascicoloVO.descUfficioIntestatarioId" title="Ufficio" />
		<display:column property="fascicoloVO.pathTitolario" titleKey="protocollo.titolario.argomento" />
	</display:table>
	
	<br/>
	<html:submit styleClass="submit" property="rimuoviFascicoli" value="Rimuovi" title="Rimuovi i fascicoli selezionati"/>
</div>
</logic:notEmpty>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>


<html:xhtml />
<div>
	<logic:notEmpty name="cruscottoForm" property="fascicoliReferenteCollection">
		<display:table class="simple" width="100%"requestURI="/page/cruscotto/amministrazione.do"
			name="sessionScope.cruscottoForm.fascicoliReferenteCollection" export="false"
			sort="list" pagesize="10" id="row">
			<display:column title=""> 
				<input type="checkbox" name="fascicoloReferenteChkBox" value="<bean:write name='row' property='id'/>"/>
			</display:column>
			<display:column title="N. fascicolo">
				
					<html:link action="/page/cruscotto/amministrazione.do"
						paramId="fascicoloSelezionato" paramProperty="id"
						paramName="row">
						<bean:write name="row" property="annoProgressivo" />
					</html:link>
			</display:column>
			<display:column property="oggetto" title="Oggetto" />
			<display:column property="dataApertura" title="Data Creazione" />
			<display:column property="pathTitolario" title="Classificazione" />
			<display:column property="descrizioneStato" title="Stato" />
		</display:table>
		<p>
			<html:submit styleClass="submit" property="btnRiassegnaFascicoli" value="Riassegna i Selezionati"	alt="Riassegna i fascicoli" />
		</p>
		
	</logic:notEmpty>
	<logic:empty name="cruscottoForm" property="fascicoliReferenteCollection">
		<span><strong> Nessun fascicolo assegnato </strong></span>
	</logic:empty></div>
	
	


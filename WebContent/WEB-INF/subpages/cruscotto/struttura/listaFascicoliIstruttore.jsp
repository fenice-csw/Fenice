<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>


<html:xhtml />
<div>
	<logic:notEmpty name="cruscottoForm" property="fascicoliIstruttoreCollection">
		<display:table class="simple" width="100%"requestURI="/page/cruscotto/struttura.do"
			name="sessionScope.cruscottoForm.fascicoliIstruttoreCollection" export="false"
			sort="list" pagesize="10" id="row">
			<display:column title=""> 
				<input type="checkbox" name="fascicoloIstruttoreChkBox" value="<bean:write name='row' property='id'/>"/>
			</display:column>
			<display:column title="N. fascicolo">
				
					<html:link action="/page/cruscotto/struttura.do"
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
			<html:submit styleClass="submit" property="btnResetIstruttore" value="Reset Istruttore"	alt="Reset Istruttore" />
		</p>
		
	</logic:notEmpty>
	<logic:empty name="cruscottoForm" property="fascicoliIstruttoreCollection">
		<span><strong> Nessun fascicolo assegnato </strong></span>
	</logic:empty></div>
	
	


<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>


<html:xhtml />
<p>
</p>
<br/>
<p>
<div>
	<logic:notEmpty name="jobScheduledLogForm" property="logs">
		<display:table class="simple" width="95%" requestURI="/page/stampa/conservazione/lista_logs.do"
			name="sessionScope.jobScheduledLogForm.logs"
			export="false" sort="list" pagesize="30" id="row">
			<display:column title="Data log" property="dataStringForConservazione" />
			<display:column title="Messaggio" property="message" />
			<display:column title="Azioni">
				[<html:link  action="/page/stampa/conservazione/lista_logs.do" 
					paramId="inviaRegistro" paramProperty="eventoId" paramName="row" >Riprova</html:link>]
			</display:column>	
		</display:table>
	</logic:notEmpty>
</div>
</p>
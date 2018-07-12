<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Lista Log">

<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" />
	</div>

<html:form action="/page/log.do">
<div>
	<display:table class="simple" width="100%" requestURI="/page/log.do"
			name="sessionScope.logForm.logFile"
			export="false" sort="list" pagesize="30" id="row">
			<display:column>
				<html:link  action="/page/log.do" 
					paramId="logSelezionato" paramProperty="absolutePath" paramName="row">
					<bean:write name="row" property="name" />
				</html:link>	
			</display:column>
			<display:column>
				<html:link  action="/page/log.do" 
					paramId="cancellaLogSelezionato" paramProperty="absolutePath" paramName="row">
					[CANCELLA]
				</html:link>	
			</display:column>	
		</display:table>
	<p>
		<html:submit styleClass="submit" property="indietro"
					value="Indietro" title="Indietro" />
		<html:submit styleClass="submit" property="cancellaTutti"
					value="Cancella Tutti" title="Cancella Tutti" />
	</p>
</div>


</html:form>

</eprot:page>





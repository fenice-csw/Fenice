<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Stampa Registro Modifiche Documenti">

<html:form action="/page/amministrazione/registroModifiche.do">
	<div>  
		<logic:messagesPresent property="recordNotFound" message="true">
			Nessun dato da visualizzare.
		</logic:messagesPresent>
		<logic:notEmpty name="reportForm" property="reportFormatsCollection">
			Selezionare il formato della stampa:
			<br /><br />
			<logic:iterate id="currentRecord" name="reportForm" property="reportFormatsCollection" >
			<li>
			  <html:link action="/page/amministrazione/registroModifiche" name="currentRecord" property="parameters" target="_blank">
				<bean:write name="currentRecord" property="descReport"/>
			  </html:link >
			</li>  
			  <br />
			</logic:iterate>     
		</logic:notEmpty>  
	</div>
</html:form>

</eprot:page>




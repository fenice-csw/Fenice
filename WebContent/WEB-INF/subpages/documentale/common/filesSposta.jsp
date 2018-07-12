<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<html:xhtml />
<div>
	<display:table class="simple" width="100%" 
	name="sessionScope.cartelleForm" property="files" 
	export="false" sort="list" pagesize="10" id="currentRecord">
		<display:column property="nomeFile" title="Nome file" />
		<display:column property="oggetto" title="Oggetto" />
		<display:column property="descrizioneArgomento" title="Argomento" />
		<display:column title="Stato del documento">
		 <logic:equal name="currentRecord" property="statoArchivio" value="L">
		 <bean:message key="documentale.lavorazione"/>
		 </logic:equal>
		 <logic:equal name="currentRecord" property="statoArchivio" value="C">
		 <bean:message key="documentale.classificato"/>
		 </logic:equal>
		 <logic:notEqual name="currentRecord" property="statoDocumento" value="0">
		<bean:message key="documentale.checkedout"/> : <bean:write name="currentRecord" property="usernameLav"/>
		 </logic:notEqual>
		</display:column>
	</display:table>
</div>


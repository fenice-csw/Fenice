<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<p>
<logic:notEmpty name="protocolloForm" property="documentiAllegatiCollection" >
<logic:iterate id="recordDocumento" name="protocolloForm" property="documentiAllegatiCollection" >
  <logic:equal name="protocolloForm" property="documentoReadable" value="true">
  	<span>
		<html:link href="./documentoview.do"   
			paramId="downloadAllegatoId" 
    		paramName="recordDocumento" 
    		paramProperty="idx"  
			title="Download File">
			<bean:write name="recordDocumento" property="descrizione"/>
		</html:link>
		(<bean:write name="recordDocumento" property="size"/> bytes)
  		&nbsp;&nbsp;
  	</span>
  </logic:equal>
  <logic:equal name="protocolloForm" property="documentoReadable" value="false">
  		<span>
  			<bean:write name="recordDocumento" property="descrizione"/> (<bean:write name="recordDocumento" property="size"/> bytes)
  		&nbsp;&nbsp;
  		</span>
  </logic:equal>
</logic:iterate>   
</logic:notEmpty>
</p>

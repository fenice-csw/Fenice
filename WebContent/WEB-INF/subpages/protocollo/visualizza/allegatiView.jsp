<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<p>

<logic:iterate id="recordDocumento" name="visualizzaProtocolloForm" property="documentiAllegatiCollection" >
  <span>
  
	<html:link action="/page/protocollo/allaccioview.do"   
		paramId="downloadAllegatoId" 
    	paramName="recordDocumento" 
    	paramProperty="idx"  
		target="_blank"
		title="Download File">
		<bean:write name="recordDocumento" property="descrizione"/>
	</html:link>
	(<bean:write name="recordDocumento" property="size"/> bytes)
  &nbsp;&nbsp;</span>
</logic:iterate>   
</p>

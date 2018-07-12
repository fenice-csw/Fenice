<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html:xhtml />

<div id="protocollo-errori">
	<html:errors bundle="bundleErroriProtocollo" />
</div>

  <logic:messagesPresent message="true">
  <div id="protocollo-messaggi">
   <ul>
   <html:messages id="actionMessage" message="true" bundle="bundleErroriProtocollo">
      <li>
      <bean:write name="actionMessage"/>
      </li>
   </html:messages> 
   </ul>
   </div>
  </logic:messagesPresent>

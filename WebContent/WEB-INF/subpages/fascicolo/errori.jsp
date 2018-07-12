<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />

<div id="protocollo-errori">
	<html:errors bundle="bundleErroriProtocollo" />
</div>

<div id="protocollo-messaggi">
  <logic:messagesPresent message="true">
   <ul>
   <html:messages id="actionMessage" message="true" bundle="bundleErroriProtocollo">
      <li>
      <bean:write name="actionMessage"/>
      </li>
   </html:messages> 
   </ul>
  </logic:messagesPresent>
</div>
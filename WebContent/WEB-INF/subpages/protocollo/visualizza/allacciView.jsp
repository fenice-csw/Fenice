<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />


<p>
  <logic:iterate id="currentRecord" property="protocolliAllacciati" name="visualizzaProtocolloForm">
	  
	  	<span><bean:write name="currentRecord" property="allaccioDescrizione" /></span>&nbsp;&nbsp;
	  
  </logic:iterate>
</p>

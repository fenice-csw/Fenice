<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<ul>
	<logic:iterate id="currentRecord" property="fascicoliProtocollo" name="visualizzaProtocolloForm">
		<li>
		<span>
			<bean:write name="currentRecord" property="annoProgressivo"/>
			- 
			<bean:write name="currentRecord" property="oggetto"/>
		</span>
		
		</li><br/>
	</logic:iterate>
</ul>


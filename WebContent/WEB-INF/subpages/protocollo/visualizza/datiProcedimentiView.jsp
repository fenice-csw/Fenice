<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<ul>
	<logic:iterate id="proc" property="procedimentiProtocollo" name="visualizzaProtocolloForm">
		<li>
			<span>
		<bean:write name="proc" property="numeroProcedimento"/>-
			</span>
			<span>
		<bean:write name="proc" property="oggetto"/>
			</span>
		</li>
	</logic:iterate>
</ul>

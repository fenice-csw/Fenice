<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<div class="sezione">
<span class="title"><strong><bean:message key="fascicolo.fascicolo"/></strong></span>
<table summary="">
	<logic:notEmpty name="documentoForm" property="fascicoliDocumento">
	<tr>
		<td>
			<label for="nomeFascicolo"><bean:message key="fascicolo.associato"/>:</label>
		</td>
	</tr>
	<tr>		
		<td>
		<ul>
		<logic:iterate id="currentRecord" property="fascicoliDocumento" name="documentoForm">
		<li>
		   	<bean:write name="currentRecord" property="annoProgressivo"/> -
		   	<bean:write name="currentRecord" property="oggetto"/>
		</li>
		</logic:iterate>
		</ul>
		</td>
	</tr>
	</logic:notEmpty>
</table>
</div>
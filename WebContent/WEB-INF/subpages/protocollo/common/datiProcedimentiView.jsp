<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<logic:notEmpty name="protocolloForm" property="procedimentiProtocollo">
<div>
<ul>
	<logic:iterate id="proc" property="procedimentiProtocollo" name="protocolloForm">
		<li>
			<span>
			<html:link paramId="visualizzaProcedimentoId" paramName="proc" paramProperty="procedimentoId" page="/page/procedimentoview.do">
				<bean:write name="proc" property="numeroProcedimento"/>
			</html:link>
			</span>
			<span>
				-	<bean:write name="proc" property="oggetto"/>
			</span>
		</li>
	</logic:iterate>
</ul>
</div>
</logic:notEmpty>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<html:xhtml />
<logic:notEmpty name="cartelleForm">
	<logic:equal name="documentoForm" property="permessoCorrente" value="4">
		<span><bean:message key="documentale.cartella"/>: </span>
		
		<logic:iterate id="record"  name="cartelleForm" property="pathCartella">
		  /<span><bean:write name="record" property="nome" /></span>
		  &nbsp;
		</logic:iterate>
	</logic:equal>
	<logic:notEqual name="documentoForm" property="permessoCorrente" value="4">
		<logic:equal name="documentoForm" property="documentoId" value="0">
			<span><bean:message key="documentale.cartella"/>: </span>
			<logic:iterate id="record"  name="cartelleForm" property="pathCartella">
		  	/	<span><bean:write name="record" property="nome" /></span>
		  	&nbsp;
			</logic:iterate>
		</logic:equal>	
		<logic:notEqual name="documentoForm" property="documentoId" value="0">
			<span><bean:message key="documentale.documeni_condivisi"/></span>
		</logic:notEqual>
	</logic:notEqual>
</logic:notEmpty>
<hr widht="100%"/>




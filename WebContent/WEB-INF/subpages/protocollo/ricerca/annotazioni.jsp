<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:xhtml />
<table summary="">
	<tr>
		<td class="label">
			<label for="chiaveAnnotazione"><bean:message key="protocollo.annotazioni.chiave"/>:</label>
		</td>
		<td>
			<html:text property="chiaveAnnotazione" styleId="chiaveAnnotazione" size="30" maxlength="255"></html:text>
			<label for="posizioneAnnotazione"><bean:message key="protocollo.annotazioni.posizione"/>:</label>
			<html:text property="posizioneAnnotazione" styleId="posizioneAnnotazione" size="15" maxlength="10"></html:text>
		</td>
	</tr>					
	<tr>
		<td class="label">
			<label for="descrizioneAnnotazione"><bean:message key="protocollo.annotazioni.note"/>:</label>
		</td>
		<td>
			<html:text property="descrizioneAnnotazione" styleId="descrizioneAnnotazione" size="80%"></html:text>
		</td>
	</tr>					
</table>	
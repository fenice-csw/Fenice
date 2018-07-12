<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<br class="hidden" />
<jsp:include page="/WEB-INF/subpages/protocollo/common/datiProtocollo.jsp" />

<br class="hidden" />

<table>
<tr>
	<td class="label">
		<label for="provvedimentoAnnullamento">
		<bean:message key="protocollo.annullamento.estremiautorizzazione"/>
		<span class="obbligatorio"> * </span>:</label>
	</td>
	<td>
		<bean:define id="protocolloId" name="protocolloForm" property="protocolloId" />
		<html:hidden property="protocolloId"/>
		<html:textarea  styleClass="obbligatorio" property="provvedimentoAnnullamento" rows="4" cols="60%"></html:textarea>	
	</td>
</tr>
<!-- 
<tr>
	<td class="label">
		<label for="notaAnnullamento">
		<bean:message key="protocollo.annullamento.note"/>:
		</label>
	</td>
	<td>
		<html:textarea property="notaAnnullamento" rows="4" cols="60%"></html:textarea>	
	</td>
</tr>
 -->
<tr>
	<td>&nbsp;</td>
	<td colspan="2">
	<html:submit styleClass="submit" property="btnConfermaAnnullamento" value="Conferma annullamento" alt="Conferma annullamento"/>
	<html:submit styleClass="button" property="btnAnnulla" value="Annulla" alt="torna al dettaglio protocollo	"/>
	</td>
</tr>
</table>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<table summary="">
	<tr>
		<td class="label">
			<label for="oggettoProcedimento"><bean:message key="protocollo.procedimento.procedimentiassociato"/>:</label>
			<html:text property="oggettoProcedimento" styleId="oggettoProcedimento" size="50" maxlength="100"></html:text>&nbsp;
		</td>
		<td>
			<html:submit styleClass="submit" property="btnCercaProcedimenti" value="Cerca" title="Cerca Procedimenti"/>
		<logic:notEqual name="protocolloForm" property="protocolloId" value="0">
			<html:submit styleClass="submit" property="btnNuovoProcedimento" value="Nuovo" title="Nuovo Procedimento"/>
		</logic:notEqual>
		</td>
	</tr>
</table>

<logic:notEmpty name="protocolloForm" property="procedimentiProtocollo">
<ul>
	<logic:iterate id="proc" property="procedimentiProtocollo" name="protocolloForm">
		<li>
			<html:multibox property="procedimentoSelezionatoId"><bean:write name="proc" property="procedimentoId"/>
			</html:multibox>		
			<span>
				<bean:write name="proc" property="numeroProcedimento"/>
			</span> 
			<span>
			<logic:notEmpty name="proc" property="oggetto">
				 - <bean:write name="proc" property="oggetto"/>
			</logic:notEmpty>
			</span>
		</li>
	</logic:iterate>
</ul>
</logic:notEmpty>
<logic:notEmpty name="protocolloForm" property="procedimentiProtocollo">
  <html:submit styleClass="button" property="rimuoviProcedimenti" value="Rimuovi" title="Rimuovi i procedimenti selezionati dall'elenco"/>
</logic:notEmpty>


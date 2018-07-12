<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />


<html:submit styleClass="button"
	property="impostaTipoProcedimentoAction" value="Vai"
	styleId="selezionaTipoProcedimentoButton" style="display: none;" />

<tr>
	<td class="label"><span><bean:message
		key="procedimento.ufficio" /> <span class="obbligatorio"> * </span> :
	<html:hidden property="ufficioCorrenteId" /></span></td>
	<td><logic:notEmpty name="procedimentoForm"
		property="ufficioCorrentePath">
		<bean:define id="ufficioCorrentePath" name="procedimentoForm"
			property="ufficioCorrentePath" />
		<span
			title='<bean:write name="procedimentoForm" property="ufficioCorrentePath" />'>
		<strong> <bean:write name="procedimentoForm"
			property="ufficioCorrente.description" /> </strong></span>
	</logic:notEmpty></td>
</tr>
<tr>
	<td class="label"><label title="Data Avvio" for="dataAvvio"><bean:message
		key="procedimento.data.avvio" /></label>:</td>
	<td colspan="2">
	<span>
		<strong>
		<bean:write name="procedimentoForm" property="dataAvvio" />
	</strong></span>
	</td>
</tr>

<tr>
	<td class="label">Dirigente:</td>
	<td>
		<span><strong> <bean:write name="procedimentoForm" property="responsabile" /> </strong></span>
	</td>
</tr>
<tr>
	<td class="label">Funzionario:</td>

	<td colspan="2">
		
		<html:select name="procedimentoForm" property="referenteId"
			disabled="true">
			<html:option value=""></html:option>
			<html:optionsCollection name="procedimentoForm"
				property="referentiCollection" value="id" label="caricaFullName" />
		</html:select>
	</td>

</tr>
<tr>
	<td class="label">Istruttore:</td>
	<td>
		<logic:notEmpty name="procedimentoForm" property="istruttori">
			<logic:iterate id="istr" name="procedimentoForm" property="istruttori">
	    		<span><STRONG><bean:write name="istr" property="nomeUtente"/></STRONG> </span><br />
			</logic:iterate>
		</logic:notEmpty>
	</td>
</tr>
<tr>
	<td class="labelEvidenziata"><label for="interessato">Ricorrente</label>:
	</td>
	<td>
		<logic:notEmpty name="procedimentoForm" property="interessato">
			<span><strong><bean:write name="procedimentoForm" property="interessato" /></strong></span>
		</logic:notEmpty>
	</td>
</tr>
<tr>
	<td class="labelEvidenziata"><label for="delegato">Legale del Ricorrente</label>:</td>
	<td>
		<logic:notEmpty name="procedimentoForm" property="delegato">
			<span><strong><bean:write name="procedimentoForm" property="delegato" /></strong></span>
		</logic:notEmpty>
	</td>
</tr>

<tr>
	<td class="labelEvidenziata"><label for="autoritaEmanante"><bean:message
		key="procedimento.autorita_emanante" /></label>:</td>
	<td>
		<logic:notEmpty name="procedimentoForm" property="autoritaEmanante">
			<span><strong><bean:write name="procedimentoForm" property="autoritaEmanante" /> </strong></span>
		</logic:notEmpty>
	</td>
</tr>


<tr>
	<td class="label"><label for="oggettoProcedimento"><bean:message
		key="procedimento.oggetto" /></label>:
	<br />
	</td>
	<td colspan="2"><span>
		<strong><bean:write name="procedimentoForm" property="oggettoProcedimento" /></strong></span> </td>
</tr>


<tr>
	<td class="label"><label for="note"> <bean:message
		key="procedimento.note" />&nbsp;: </label> <br />
	</td>
	<td colspan="2"><html:textarea name="procedimentoForm"
		styleId="note" property="note" rows="3" cols="50" readonly="true"/></td>
</tr>

<tr>
	<td class="label"><label for="indicazioni"> <bean:message key="procedimento.indicazioni" />&nbsp;: </label> <br />
	</td>
		<td colspan="2"><html:textarea name="procedimentoForm"
			styleId="indicazioni" property="indicazioni" rows="3" cols="50"
			readonly="true" /></td>
	</tr>
<tr>
	<td class="label"><label for="numeroProtocollo"> <bean:message
		key="procedimento.numeroProtocollo" />&nbsp;: </label></td>
	<td colspan="2"><span><strong><bean:write
		name="procedimentoForm" property="numeroProtocollo" /></strong></span></td>
</tr>
<tr>
	<td class="label"><label for="numeroProcedimento"> <bean:message
		key="procedimento.numeroProcedimento" />&nbsp;: </label></td>
	<td colspan="2">
		<span><strong><bean:write name="procedimentoForm"
			property="numeroProcedimento" /></strong></span>
	</td>
</tr>
	
	<tr>
		<td class="label"><label for="versione"> <bean:message
			key="protocollo.versione" />&nbsp;: </label></td>
		<td colspan="2"><span><strong><bean:write
			name="procedimentoForm" property="versione" /></strong></span></td>
	</tr>
	



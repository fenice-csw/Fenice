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
		key="procedimento.data.avvio" /></label>&nbsp;<span class="obbligatorio">*</span>&nbsp;:</td>
	<td colspan="2">
	<bean:write name="procedimentoForm" property="dataAvvio" />
	</td>
</tr>
<tr>
	<td class="label">Dirigente</label> <span class="obbligatorio">*</span>:</td>
	<td><span><strong> <bean:write
		name="procedimentoForm" property="responsabile" /> </strong></span></td>
</tr>
<tr>
	<td class="label">Funzionario</label><span class="obbligatorio"> *
	</span>:</td>

	<td colspan="2">
		<html:select name="procedimentoForm" property="referenteId">
			<html:option value=""></html:option>
			<html:optionsCollection name="procedimentoForm"
				property="referentiCollection" value="id" label="caricaFullName" />
		</html:select>

	</td>

</tr>
<tr>
	<td class="label"><label for="istruttori">Istruttore:</td>
	<td>
	<div class="sezione"><jsp:include
		page="/WEB-INF/subpages/procedimento/istruttori.jsp" /></div>
	</td>
</tr>
<tr>
	<td class="labelEvidenziata"><label for="interessato">Ricorrente</label>:
	</td>
	<td><jsp:include
		page="/WEB-INF/subpages/procedimento/interessato.jsp" /></td>
</tr>
<tr>
	<td class="labelEvidenziata"><label for="delegato">Legale
	del Ricorrente</label>:</td>
	<td><jsp:include
		page="/WEB-INF/subpages/procedimento/delegato.jsp" /></td>
</tr>

<tr>
	<td class="labelEvidenziata"><label for="autoritaEmanante"><bean:message
		key="procedimento.autorita_emanante" /></label>:</td>
	<td><jsp:include
		page="/WEB-INF/subpages/procedimento/autorita_emanante.jsp" /></td>
</tr>

<tr>
	<td class="label">
	<td colspan="2"><html:select name="procedimentoForm"
		property="statoId">
		<html:optionsCollection name="procedimentoForm"
			property="statiProcedimentoCollection" value="id" label="description" />
	</html:select></td>
</tr>

	<logic:notEmpty name="procedimentoForm" property="posizioniProcedimentoCollection">
		<tr>
			<td class="label"><label for="posizione"> <bean:message
				key="procedimento.posizione" />&nbsp;: </label>
			</td>
		
			<td colspan="2">
				<html:select name="procedimentoForm" property="posizione">
					<html:optionsCollection name="procedimentoForm" property="posizioniProcedimentoCollection" value="codice"label="description" />
				</html:select> 
				<html:submit styleClass="button" property="impostaPosizioneAction" value="conferma" title="Conferma l'invio del procedimento" />
			</td>
		</tr>
	</logic:notEmpty>

<tr>
	<td class="label"><label for="oggettoProcedimento"><bean:message
		key="procedimento.oggetto" /></label>&nbsp;<span class="obbligatorio">*</span>&nbsp;:
	<br />
	<span><html:link
		action="/page/unicode.do?campo=oggettoProcedimento" target="_blank">segni diacritici</html:link></span>
	</td>
	<td colspan="2"><html:text styleClass="obbligatorio"
		styleId="oggettoProcedimento" name="procedimentoForm"
		property="oggettoProcedimento" size="50" maxlength="250" /></td>
</tr>

<tr>
	<td class="label"><label for="note"> <bean:message
		key="procedimento.note" />&nbsp;: </label> <br />
	<span><html:link action="/page/unicode.do?campo=note"
		target="_blank">segni diacritici</html:link></span></td>
	<td colspan="2"><html:textarea name="procedimentoForm"
		styleId="note" property="note" rows="3" cols="50"/></td>
</tr>

	<tr>
		<td class="label"><label for="indicazioni"> <bean:message
			key="procedimento.indicazioni" />&nbsp;: </label> <br />
		</td>
		<td colspan="2"><html:textarea name="procedimentoForm"
			styleId="indicazioni" property="indicazioni" rows="3" cols="50" /></td>
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
	<td colspan="2"><logic:greaterThan name="procedimentoForm"
		property="procedimentoId" value="0">
		<span><strong><bean:write name="procedimentoForm"
			property="numeroProcedimento" /></strong></span>
	</logic:greaterThan></td>
</tr>




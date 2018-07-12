<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />


	<tr>
		<td class="label"><span><bean:message
			key="procedimento.ufficio" />: <html:hidden property="ufficioCorrenteId" /></span></td>
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
		<strong>
			<bean:write name="procedimentoForm" property="dataAvvio" />
		</strong>
		</td>
	</tr>
	<tr>
		<td class="label"><label for="responsabile"><bean:message
			key="procedimento.responsabile" /></label>:
		</td>
		<td><span><strong> <bean:write
			name="procedimentoForm" property="responsabile" /> </strong></span></td>
	</tr>
	<tr>
		<td class="label"><label for="referenteId"><bean:message
			key="procedimento.referente" /></label>:</td>
		<td colspan="2"><html:select name="procedimentoForm"
			property="referenteId" disabled="true">
			<html:option value=""></html:option>
			<html:optionsCollection name="procedimentoForm"
				property="referentiCollection" value="id" label="caricaFullName" />
		</html:select></td>
	</tr>
	<tr>
		<td class="label"><label for="istruttori"><bean:message
			key="procedimento.istruttori" /></label>:</td>
		<td>
		<logic:notEmpty name="procedimentoForm" property="istruttori">
			<logic:iterate id="istr" name="procedimentoForm" property="istruttori">
	    		<span><strong><bean:write name="istr" property="nomeUtente"/></strong></span><br />
			</logic:iterate>
		</logic:notEmpty>
		</td>
	</tr>
	<tr>
    	<td class="labelEvidenziata">
      		<label for="interessato"><bean:message key="procedimento.interessato" /></label>:
    	</td>
	    <td>
	      <logic:notEmpty name="procedimentoForm" property="interessato">
				<span><strong><bean:write name="procedimentoForm" property="interessato" /></strong></span>
		</logic:notEmpty>
	    </td>
	</tr>
	<tr>
    	<td class="labelEvidenziata">
      		<label for="delegato"><bean:message key="procedimento.delegato" /></label>:
    	</td>
	    <td>
	      <logic:notEmpty name="procedimentoForm" property="delegato">
				<span><strong><bean:write name="procedimentoForm" property="delegato" /></strong></span>
		</logic:notEmpty>
	    </td>
	</tr>
	
	<tr>
		<td class="label"><label for="oggettoProcedimento"><bean:message
			key="procedimento.oggetto" /></label>:
			<br />
		</td>
		<td colspan="2">
		<span>
		<strong>
			<bean:write name="procedimentoForm" property="oggettoProcedimento" />
		</strong>
		</span>
		</td>
	</tr>
	<tr>
		<td class="label"><label for="versione"><bean:message
			key="procedimento.versione" /></label>:
			<br />
		</td>
		<td colspan="2">
		<span>
		<strong>
			<bean:write name="procedimentoForm" property="versione" />
		</strong>
		</span>
		</td>
	</tr>
	<tr>
		<td class="label"><label for="note"> <bean:message
			key="procedimento.note" />:</label> <br />
		</td>
		<td colspan="2"><html:textarea name="procedimentoForm"
			styleId="note" property="note" rows="3" cols="50" readonly="true"/></td>
	</tr>
	
	<tr>
	
		<td class="label"><label for="numeroProtocollo"> <bean:message
			key="procedimento.numeroProtocollo" />&nbsp;: </label></td>
		<td colspan="2">
			<span><strong>
				<html:link action="/page/procedimentoview.do" paramName="procedimentoForm" paramId="protocolloSelezionato" paramProperty="protocolloId">
					<bean:write name="procedimentoForm" property="numeroProtocollo" />
				</html:link>
			</strong></span>
		</td>
	</tr>
	
	<tr>
		<td class="label"><label for="numeroProcedimento"> <bean:message
			key="procedimento.numeroProcedimento" />: </label></td>
		<td colspan="2"><logic:greaterThan name="procedimentoForm"
			property="procedimentoId" value="0">
			<span><strong><bean:write name="procedimentoForm"
				property="numeroProcedimento" /></strong></span>
		</logic:greaterThan></td>
	</tr>
	<!-- NON VA, MA PER ADESSO È PERFETTO... -->
	<logic:equal name="procedimentoForm" property="statoId" value="1">
	<tr>
		<td class="label">Stato:</td>
		<td colspan="2">
			<strong>
				<span class="obbligatorio">Procedimento Chiuso</span>
			</strong>
		</td>
	</tr>
	</logic:equal>
	<logic:equal name="procedimentoForm" property="sospeso" value="true">
	<tr>
		<td class="label">Stato:</td>
		<td colspan="2">
			<strong>
				<span class="obbligatorio">Sospeso dal <bean:write name="procedimentoForm" property="dataSospensione" />.
				<logic:present name="procedimentoForm" property="protocolloSospensione">
					Protocollo della sospensione:<bean:write name="procedimentoForm" property="protocolloSospensioneDesc" />
				</logic:present>
				</span>
			</strong>
		</td>
	</tr>
	</logic:equal>




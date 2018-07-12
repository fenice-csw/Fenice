<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>


<html:xhtml />
  
  <script>
	function vai(id) {
		document.getElementById("treeSelection").value = id;
		document.getElementById("vaiButton").click();
	}

	function treeAction(idOrg, idBottone, nameHidden) {
		document.getElementById("treeSelection").value = idOrg;
		document.getElementById("treeSelection").name = nameHidden;
		document.getElementById(idBottone).click();
	}
</script>
  <input type="hidden" id="treeSelection"/ value="-1"/>

<html:submit styleClass="button" property="impostaUfficioAction"
				value="Vai" title="Imposta l'ufficio come corrente" styleId="vaiButton" style="display: none;"/>

  <tr>
    <td>
     <span ><bean:message key="protocollo.ricerca.assegnatario.ufficioCorrente" /> :  <html:hidden property="ufficioCorrenteId" /></span>  
    </td>
    <td colspan="3">
    <div class="sezione">
    	
		<div style="display:none;">
			<html:select property="ufficioCorrenteId">
				<option value='<bean:write name="ricercaProcedimentoForm" property="ufficioCorrente.id"/>'>
		        	<bean:write name="ricercaProcedimentoForm" property="ufficioCorrente.description" />
		        </option>
	 	    </html:select>
		</div>
<ul>
	<li class="ufficioCorrente"><a href="javascript:document.getElementById('levelUpButton').click();">
	<bean:write name="ricercaProcedimentoForm" property="ufficioCorrente.description" /></a>
		<html:submit
		styleClass="button" style="display: none;" styleId="levelUpButton" property="ufficioPrecedenteAction" value=".."
		title="Vai al livello superiore" />
		<ul>
			<logic:notEmpty name="ricercaProcedimentoForm" property="ufficiDipendenti">
				<logic:iterate id="ufficio" name="ricercaProcedimentoForm"
					property="ufficiDipendenti">
					<logic:equal name="ufficio" property="attivo" value="true">
					<li class="ufficio"><bean:write name="ufficio"
						property="description" />
					    [<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'vaiButton','ufficioSelezionatoId');">Vai</a>]
					</li>
					</logic:equal>  
				</logic:iterate>
			</logic:notEmpty>
		</ul>
	</li>
</ul>
	</div>   
	</td>
</tr>
<tr>
<td>
Responsabile:
</td>
<td>
<logic:notEmpty name="ricercaProcedimentoForm" property="utenti">
	<html:select property="utenteSelezionatoId" styleClass="evidenziato" >
		<html:option value=""></html:option>
		<html:optionsCollection property="utenti" value="id" label="fullName" />
	</html:select>
</logic:notEmpty>  
<logic:empty name="ricercaProcedimentoForm" property="utenti">
	<html:hidden property="utenteSelezionatoId" value="0"/>
</logic:empty>
</td>
</tr>
<tr>
<td>
<bean:message key="procedimento.tipo_procedimento" />:
</td>
<td>
<logic:notEmpty name="ricercaProcedimentoForm" property="tipiProcedimento">
	<html:select property="tipoProcedimentoSelezionatoId" styleClass="evidenziato" >
		<html:option value=""></html:option>
		<html:optionsCollection property="tipiProcedimento" value="idTipo" label="descrizione" />
	</html:select>
</logic:notEmpty>  
<logic:empty name="ricercaProcedimentoForm" property="tipiProcedimento">
	<html:hidden property="tipoProcedimentoSelezionatoId" value="0"/>
</logic:empty>
</td>
</tr>


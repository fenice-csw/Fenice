<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:xhtml />
<script>
function vai (id){
document.getElementById("treeSelection").value=id;
document.getElementById("vaiButton").click();
}

function treeAction (idOrg, idBottone, nameHidden){
	document.getElementById("treeSelection").value=idOrg;
	document.getElementById("treeSelection").name=nameHidden;
	document.getElementById(idBottone).click();
	}

</script>
<input type="hidden" id="treeSelection"/ value="-1"/>

<html:submit styleClass="button" property="impostaUfficioAction"
				value="Vai" title="Imposta l'ufficio come corrente" styleId="vaiButton" style="display: none;"/>
<html:submit styleClass="button"
					property="assegnaUfficioCorrenteAction" value="Assegna"
					title="Assegna l'ufficio corrente" style="display: none;" styleId="assegnaUfficioButton" />
<html:submit styleClass="button" property="assegnaUtenteAction"
					value="Assegna" title="Assegna l'utente" style="display: none;" styleId="assegnaUtenteButton"/>
<div>
<ul>
	<li class="ufficioCorrente"><a href="javascript:document.getElementById('levelUpButton').click();"><bean:write name="protocolloForm"
		property="ufficioCorrente.nomeUfficio" /></a>
		<html:submit
		styleClass="button" style="display: none;" styleId="levelUpButton" property="ufficioPrecedenteAction" value=".."
		title="Vai al livello superiore" />
	<ul>
		<logic:notEmpty name="protocolloForm" property="ufficiDipendenti">
			<logic:iterate id="ufficio" name="protocolloForm"
				property="ufficiDipendenti">
				<logic:equal name="ufficio" property="attivo" value="true">
					<li class="ufficio"><bean:write name="ufficio"
						property="nomeUfficio" />
						[<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'assegnaUfficioButton','ufficioSelezionatoId')">Assegna</a>] 
						[<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'vaiButton','ufficioSelezionatoId');">Vai</a>]</li>
				</logic:equal>
			</logic:iterate>
		</logic:notEmpty>
		
 		 
		<logic:notEmpty name="protocolloForm" property="utenti">

			<logic:iterate id="utente" name="protocolloForm" property="utenti">

				<li class="utente"><bean:write name="utente"
					property="fullName" />
						[<a class="treeAction" href="javascript:treeAction(<bean:write name="utente" property="id"/>,'assegnaUtenteButton','utenteSelezionatoId');">Assegna</a>]
					</li>
				

			</logic:iterate>

		</logic:notEmpty>

	</ul>
	</li>
</ul>

</div><!--

<table summary="">

	<tr>
		<td class="label"><span><bean:message
			key="protocollo.ricerca.assegnatario.ufficioCorrente" /> : <html:hidden
			property="ufficioCorrenteId" /></span></td>
		<td><logic:notEmpty name="protocolloForm"
			property="ufficioCorrentePath">
			<bean:define id="ufficioCorrentePath" name="protocolloForm"
				property="ufficioCorrentePath" />
			<span
				title='<bean:write name="protocolloForm" property="ufficioCorrentePath" />'>
			<strong> <bean:write name="protocolloForm"
				property="ufficioCorrente.description" /> </strong></span>
		</logic:notEmpty> <html:submit styleClass="button" property="ufficioPrecedenteAction"
			value=".." title="Vai al livello superiore" /></td>
		<td><logic:greaterThan name="protocolloForm"
			property="ufficioCorrente.parentId" value="0">
			<logic:equal name="protocolloForm"
				property="utenteAbilitatoSuUfficio" value="true">
				<html:submit styleClass="button"
					property="assegnaUfficioCorrenteAction" value="Assegna"
					title="Assegna l'ufficio corrente" />
			</logic:equal>
		</logic:greaterThan></td>
	</tr>

	<logic:notEmpty name="protocolloForm" property="ufficiDipendenti">
		<tr>
			<td class="label"><label for="ufficioSelezionatoId"><bean:message
				key="protocollo.ricerca.assegnatario.ufficio" /> :</label></td>
			<td colspan="2"><html:select property="ufficioSelezionatoId">
				<logic:iterate id="ufficio" name="protocolloForm"
					property="ufficiDipendenti">
					<option value='<bean:write name="ufficio" property="id"/>'>
					<bean:write name="ufficio" property="description" /></option>
				</logic:iterate>
			</html:select> <html:submit styleClass="button" property="impostaUfficioAction"
				value="Vai" title="Imposta l'ufficio come corrente" /></td>
		</tr>
	</logic:notEmpty>


	<logic:notEmpty name="protocolloForm" property="utenti">
		<tr>
			<td class="label"><label for="utenteSelezionatoId"><bean:message
				key="protocollo.ricerca.assegnatario.utente" /> :</label></td>
			<td><html:select property="utenteSelezionatoId">
				<logic:iterate id="utente" name="protocolloForm" property="utenti">
					<option value='<bean:write name="utente" property="id"/>'><bean:write
						name="utente" property="fullName" /></option>
				</logic:iterate>
			</html:select></td>
			<td><logic:equal name="protocolloForm"
				property="utenteAbilitatoSuUfficio" value="true">
				<html:submit styleClass="button" property="assegnaUtenteAction"
					value="Assegna" title="Assegna l'utente" />
			</logic:equal></td>
		</tr>
	</logic:notEmpty>

</table>
-->
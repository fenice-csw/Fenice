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
				<li class="ufficio"><bean:write name="ufficio"
					property="nomeUfficio" /><logic:equal name="protocolloForm" property="utenteAbilitatoSuUfficio" value="true">
					<logic:equal name="ufficio" property="flagInOggetto" value="true">
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'assegnaUfficioButton','ufficioSelezionatoId')">Assegna</a>] 
					</logic:equal>
					</logic:equal> [<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'vaiButton','ufficioSelezionatoId');">Vai</a>]</li>

			</logic:iterate>
		</logic:notEmpty>
		<logic:notEmpty name="protocolloForm" property="utenti">

			<logic:iterate id="utente" name="protocolloForm" property="utenti">

				<li class="utente"><bean:write name="utente" property="caricaFullName" /><logic:equal name="protocolloForm" property="utenteAbilitatoSuUfficio" value="true">
					<logic:equal name="utente"
					property="flagInOggetto" value="true">
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="utente" property="id"/>,'assegnaUtenteButton','utenteSelezionatoId');">Assegna</a>]
					</logic:equal>
					</logic:equal></li>
				

			</logic:iterate>

		</logic:notEmpty>


	</ul>
	</li>
</ul>

</div>


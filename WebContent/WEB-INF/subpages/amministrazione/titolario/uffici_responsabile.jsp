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
<html:submit styleClass="button" property="assegnaUtenteAction"
					value="Assegna" title="Assegna l'utente" style="display: none;" styleId="assegnaUtenteButton"/>
<html:submit styleClass="button"
					property="assegnaUfficioCorrenteAction" value="Assegna"
					title="Assegna l'ufficio corrente" style="display: none;" styleId="assegnaUfficioButton" />
<div>

<ul>
	<li class="ufficioCorrente"><a href="javascript:document.getElementById('levelUpButton').click();"><bean:write name="titolarioForm"
		property="ufficioCorrenteResponsabile.description" /></a>
		<html:submit
		styleClass="button" style="display: none;" styleId="levelUpButton" property="ufficioPrecedenteAction" value=".."
		title="Vai al livello superiore" />
	<ul>
		<logic:notEmpty name="titolarioForm" property="ufficiDipendentiResponsabile">
			<logic:iterate id="ufficio" name="titolarioForm"
				property="ufficiDipendentiResponsabile">
				<li class="ufficio"><bean:write name="ufficio"
					property="nomeUfficio" />
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'assegnaUfficioButton','ufficioResponsabileCorrenteId')">Assegna</a>] 
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'vaiButton','ufficioResponsabileSelezionatoId');">Vai</a>]</li>

			</logic:iterate>
		</logic:notEmpty>
		<logic:notEmpty name="titolarioForm" property="utenti">
			<logic:iterate id="utente" name="titolarioForm" property="utenti">

				<li class="utente"><bean:write name="utente"
					property="fullName" />
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="utente" property="id"/>,'assegnaUtenteButton','utenteResponsabileSelezionatoId');">Assegna</a>]
				</li>
			</logic:iterate>

		</logic:notEmpty>


	</ul>
	</li>
</ul>

</div>
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


<html:submit styleClass="button" property="assegnaUtenteAction"
					value="Assegna" title="Assegna l'utente" style="display: none;" styleId="assegnaUtenteButton"/>
<div>
<ul>
	<li class="ufficioCorrente"><a href="javascript:document.getElementById('levelUpButton').click();"><bean:write name="caricaForm"
		property="ufficioCorrente.description" /></a>
	<ul>
		<logic:notEmpty name="caricaForm" property="utenti">

			<logic:iterate id="utente" name="caricaForm" property="utenti">

				<li class="utente"><bean:write name="utente"property="fullName" />
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="utente" property="id"/>,'assegnaUtenteButton','utenteSelezionatoId');">Assegna</a>]
				</li>
			</logic:iterate>
		</logic:notEmpty>


	</ul>
	</li>
</ul>

</div>
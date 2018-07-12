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
<div>
<ul>
	
	<li class="ufficioCorrente"><a href="javascript:document.getElementById('levelUpButton').click();"><bean:write name="cartelleForm"
		property="ufficioCorrente.nomeUfficio" /></a>
		<html:submit
		styleClass="button" style="display: none;" styleId="levelUpButton" property="ufficioPrecedenteAction" value=".."
		title="Vai al livello superiore" />
	<ul>
		<logic:notEmpty name="cartelleForm" property="ufficiDipendenti">
			<logic:iterate id="ufficio" name="cartelleForm"
				property="ufficiDipendenti">
				<logic:equal name="ufficio" property="attivo" value="true">
				<li class="ufficio"><bean:write name="ufficio" property="nomeUfficio" />
						[<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'vaiButton','ufficioSelezionatoId');">Vai</a>]</li>
				</logic:equal>
			</logic:iterate>
		</logic:notEmpty>
		<logic:notEmpty name="cartelleForm" property="utenti">
			<logic:iterate id="utente" name="cartelleForm" property="utenti">

				<li class="utente"><bean:write name="utente"property="caricaFullName" />
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="utente" property="id"/>,'assegnaUtenteButton','utenteSelezionatoId');">Assegna</a>]
					</li>
			</logic:iterate>
		</logic:notEmpty>
		<p>
		<logic:notEmpty name="cartelleForm" property="utenteDestinatario"> 
		Selezionato: <span><bean:write name="cartelleForm" property="utenteDestinatarioName"/></span>
		</logic:notEmpty>
		</p>
	</ul>
	</li>
</ul>

</div>
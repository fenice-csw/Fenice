<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>


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

<input type="hidden" id="treeSelection" / value="-1" />

<html:submit styleClass="submit" property="btnModifica"
	styleId="modifyButton" value="Modifica"
	alt="Modifica l'ufficio selezionato" style="display: none;" />
<html:submit styleClass="submit" property="btnCancella"
	styleId="deleteButton" value="Cancella" alt="Cancella l'ufficio"
	style="display: none;" />
<html:submit styleClass="button" property="impostaUfficioAction"
	value="Vai" title="Imposta l'ufficio come corrente" styleId="vaiButton"
	style="display: none;" />
<html:submit styleClass="button" style="display: none;"
	styleId="levelUpButton" property="ufficioPrecedenteAction" value=".."
	title="Vai al livello superiore" />
<html:submit styleClass="submit" property="btnNuovo" styleId="newButton"
	value="Nuovo"
	alt="Inserisce un nuovo Ufficio dipendente da quello selezionato"
	style="display: none;" />
<html:submit styleClass="submit" style="display: none;"
	styleId="moveButton" property="btnSposta" value="Sposta"
	title="Sposta l'ufficio" />


<ul>
	<li class="ufficioCorrente"><a href="javascript:document.getElementById('levelUpButton').click();">
	<bean:write name="ufficioForm" property="ufficioCorrentePath" /> 
	<logic:equal name="ufficioForm" property="ufficioCorrente.attivo" value="false">
				<span class="inactive">
					&nbsp;(Non Attivo)&nbsp;
				</span>
	</logic:equal>
	</a> [<a
		class="treeAction"
		href="javascript:document.getElementById('newButton').click();">Nuovo</a>]
	[<a class="treeAction"
		href="javascript:document.getElementById('modifyButton').click();">Modifica</a>]
	[<a class="treeAction"
		href="javascript:document.getElementById('deleteButton').click();">Cancella</a>]
	<logic:greaterThan name="ufficioForm" property="parentId" value="0">	
		[<a class="treeAction" href="javascript:document.getElementById('moveButton').click();">Sposta</a>]			
	</logic:greaterThan>

	<ul>
		<logic:notEmpty name="ufficioForm" property="ufficiDipendenti">
			<logic:iterate id="ufficio" name="ufficioForm"
				property="ufficiDipendenti">
				<li class="ufficio">
				<bean:write name="ufficio" property="nomeUfficio" /> 
				<logic:equal name="ufficio" property="attivo" value="false">
				<span class="inactive">
				&nbsp;(Non Attivo)&nbsp;
				</span>
				</logic:equal>
				[<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'vaiButton','ufficioSelezionatoId');">Vai</a>]</li>
			</logic:iterate>
		</logic:notEmpty>
		<logic:notEmpty name="ufficioForm" property="utenti">
			<logic:iterate id="utente" name="ufficioForm" property="utenti">
				
				<li class="utente">
					<bean:write name="utente"property="caricaFullName" />
				</li>
			</logic:iterate>

		</logic:notEmpty>
	</ul>
	</li>
</ul>


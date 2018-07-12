<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

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
	title="Modifica/Cancella l'argomento corrente" style="display: none;" />
<html:submit styleClass="button" property="impostaTitolarioAction"
	styleId="vaiButton" value="Imposta"
	title="Imposta il titolario selezionato come corrente"
	style="display: none;" />
<html:submit styleClass="submit" property="btnAssocia"
	styleId="joinButton" value="Associa uffici"
	title="Associa l'argomento corrente agli uffici" style="display: none;" />
<html:submit styleClass="submit" property="btnAssociaTuttiUffici"
	styleId="joinAllButton" value="Associa tutti gli uffici"
	title="Associa l'argomento a tutti gli uffici" style="display: none;" />
<html:submit styleClass="submit" property="btnNuovo" value="Nuovo"
	styleId="newButton"
	title="Inserisce un argomento sotto il livello di quello selezionato"
	style="display: none;" />
<html:submit styleClass="button" style="display: none;"
	styleId="levelUpButton" property="titolarioPrecedenteAction" value=".."
	title="Vai al livello superiore" />
<html:submit styleClass="submit" style="display: none;"
	styleId="moveButton" property="btnSposta" value="Sposta"
	title="Sposta la voce di titolario" />
<html:submit styleClass="submit" style="display: none;"
	styleId="storyButton" property="btnStoria" value="Storia"
	title="Storia titolario" />


<ul>
	<li class="ufficioCorrente">
	<a href="javascript:document.getElementById('levelUpButton').click();">
		<logic:notEmpty name="titolarioForm" property="titolario">
			<bean:write name="titolarioForm" property="pathDescrizioneTitolario" />
		</logic:notEmpty> 
		<logic:empty name="titolarioForm" property="titolario">
			<bean:message key="protocollo.argomento.root" />
		</logic:empty> 
	</a> 
	[<a class="treeAction"href="javascript:document.getElementById('newButton').click();">Nuovo</a>]
	<logic:notEmpty name="titolarioForm" property="titolario">
		[<a class="treeAction" href="javascript:document.getElementById('modifyButton').click();">Modifica</a>]
		[<a class="treeAction" href="javascript:document.getElementById('joinButton').click();">Associa uffici</a>]
		[<a class="treeAction" href="javascript:document.getElementById('joinAllButton').click();">Associa tutti gli uffici</a>]
		<logic:greaterThan name="titolarioForm" property="titolario.parentId"value="0">	
			[<a class="treeAction" href="javascript:document.getElementById('moveButton').click();">Sposta</a>]			
		</logic:greaterThan>
		<logic:greaterThan name="titolarioForm" property="titolario.versione" value="0">	
			[<a class="treeAction" href="javascript:document.getElementById('storyButton').click();">Storia</a>]			
			</logic:greaterThan>
	</logic:notEmpty>

	<ul>
		<logic:notEmpty name="titolarioForm" property="titolariFigli">
			<logic:iterate id="titolario" name="titolarioForm"
				property="titolariFigli">
				<li class="ufficio">
				    <bean:write name="titolario"property="codice" /> - 
					<bean:write name="titolario"property="descrizione" /> 
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="titolario" property="id"/>,'vaiButton','titolarioSelezionatoId');">Vai</a>]
				</li>
			</logic:iterate>
		</logic:notEmpty>
	</ul>
	</li>
</ul>

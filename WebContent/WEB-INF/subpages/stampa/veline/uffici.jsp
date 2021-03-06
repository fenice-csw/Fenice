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

<html:submit styleClass="button" property="impostaUfficioAction"
	value="Vai" title="Imposta l'ufficio come corrente" styleId="vaiButton"
	style="display: none;" />

<html:submit styleClass="button" property="assegnaUfficioAction"
	value="Assegna" title="Assegna l'ufficio corrente"
	style="display: none;" styleId="assegnaUfficioButton" />
<html:submit styleClass="button" property="assegnaUtenteAction"
	value="Assegna" title="Assegna l'utente" style="display: none;"
	styleId="assegnaUtenteButton" />
<div class="sezione"><span class="title"> <strong>Assegnatario</strong> </span>
<ul>
	<li class="ufficioCorrente"><a
		href="javascript:document.getElementById('levelUpButton').click();"><bean:write
		name="reportRegistroForm" property="ufficioCorrente.nomeUfficio" /></a> <html:submit
		styleClass="button" style="display: none;" styleId="levelUpButton"
		property="ufficioPrecedenteAction" value=".."
		title="Vai al livello superiore" />
	<ul>
		<logic:notEmpty name="reportRegistroForm" property="ufficiDipendenti">
			<logic:iterate id="ufficio" name="reportRegistroForm"
				property="ufficiDipendenti">
				<logic:equal name="ufficio" property="attivo" value="true">
				<li class="ufficio"><bean:write name="ufficio" property="nomeUfficio" /> 
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'assegnaUfficioButton','ufficioCorrenteId')">Assegna</a>]
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'vaiButton','ufficioSelezionatoId');">Vai</a>]
				</li>
				</logic:equal>
			</logic:iterate>
		</logic:notEmpty>

		<logic:notEmpty name="reportRegistroForm" property="utenti">
			<logic:iterate id="utente" name="reportRegistroForm"
				property="utenti">
				<li class="utente"><bean:write name="utente"
					property="fullName" /> [<a class="treeAction"
					href="javascript:treeAction(<bean:write name="utente" property="id"/>,'assegnaUtenteButton','utenteSelezionatoId');">Seleziona</a>]
				</li>
			</logic:iterate>
		</logic:notEmpty>

	</ul>
	</li>
</ul>
<br>
<%-- 
<logic:notEmpty name="reportRegistroForm" property="selezionato">
<strong>Selezionato:</strong> 
<bean:write name="reportRegistroForm" property="selezionato.descrizioneUfficio" /> 
<logic:notEqual name="reportRegistroForm" property="selezionato.utenteId" value="0">
	<bean:write name="reportRegistroForm" property="selezionato.nomeUtente" />
</logic:notEqual>
</logic:notEmpty>
--%>
<logic:notEmpty name="reportRegistroForm" property="selezionato">
<strong>Selezionato:</strong> 
<bean:write name="reportRegistroForm" property="selezionatoDescription" /> 
</logic:notEmpty>
</div>
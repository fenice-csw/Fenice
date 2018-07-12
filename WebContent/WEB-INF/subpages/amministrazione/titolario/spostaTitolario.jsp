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


<html:submit styleClass="button" property="impostaTitolarioAction"
	styleId="vaiButton" value="Imposta"
	title="Imposta il titolario selezionato come corrente"
	style="display: none;" />
<html:submit styleClass="button" style="display: none;"
	styleId="levelUpButton" property="titolarioPrecedenteAction" value=".."
	title="Vai al livello superiore" />

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
	
	<ul>
		<logic:notEmpty name="titolarioForm" property="titolariFigli">
			<logic:equal name="titolarioForm" property="spostabile" value="true">
			<logic:iterate id="titolario" name="titolarioForm"
				property="titolariFigli">
				<li class="ufficio">
				    <bean:write name="titolario"property="codice" /> - 
					<bean:write name="titolario"property="descrizione" /> 
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="titolario" property="id"/>,'vaiButton','titolarioSelezionatoId');">Vai</a>]
				</li>
			</logic:iterate>
			</logic:equal>
			<logic:equal name="titolarioForm" property="spostabile" value="false">
			<logic:iterate id="titolario" name="titolarioForm"
				property="titolariFigli">
				<li class="ufficio">
				    <bean:write name="titolario"property="codice" /> - 
					<bean:write name="titolario"property="descrizione" /> 
				</li>
			</logic:iterate>
			</logic:equal>
		</logic:notEmpty>
	</ul>
	</li>
</ul>

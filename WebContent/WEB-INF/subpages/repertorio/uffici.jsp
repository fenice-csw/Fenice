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
<div>

<ul>
	<li class="ufficioCorrente"><a href="javascript:document.getElementById('levelUpButton').click();">
	<bean:write name="documentoRepertorioForm" property="ufficioCorrente.nomeUfficio" /></a>
		<html:submit styleClass="button" style="display: none;" styleId="levelUpButton" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
	<ul>
		<logic:notEmpty name="documentoRepertorioForm" property="ufficiDipendenti">
			<logic:iterate id="ufficio" name="documentoRepertorioForm"
				property="ufficiDipendenti">
				<li class="ufficio"><bean:write name="ufficio"
					property="nomeUfficio" />
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'assegnaUfficioButton','ufficioCorrenteId')">Assegna</a>] 
					[<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'vaiButton','ufficioSelezionatoId');">Vai</a>]</li>

			</logic:iterate>
		</logic:notEmpty>

	</ul>
	</li>
</ul>
<logic:notEmpty name="documentoRepertorioForm" property="ufficioResponsabile">
	<table>
		<tr>
			<td>
				<span><strong>
    				<bean:write name="documentoRepertorioForm" property="ufficioResponsabile.nomeUfficio"/>
    			</strong></span>
    		</td>
    		<td>
    			<html:submit styleClass="button" property="annullaResponsabileAction"value="annulla" title="annulla" />
    		</td>
    	</tr>
    </table>
<br />
</logic:notEmpty>

</div>
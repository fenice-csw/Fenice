<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:xhtml />
<script>
function vaiSettore (id){
document.getElementById("treeSettoreSelection").value=id;
document.getElementById("vaiSettoreButton").click();
}

function treeSettoreAction (idOrg, idBottone, nameHidden){
	document.getElementById("treeSettoreSelection").value=idOrg;
	document.getElementById("treeSettoreSelection").name=nameHidden;
	document.getElementById(idBottone).click();
	}

</script>
<input type="hidden" id="treeSettoreSelection" value="-1"/>

<html:submit styleClass="button" property="impostaSettoreAction"
				value="Vai" title="Imposta il settore come corrente" styleId="vaiSettoreButton" style="display: none;"/>
<html:submit styleClass="button"
					property="assegnaSettoreCorrenteAction" value="Assegna"
					title="Assegna il settore corrente" style="display: none;" styleId="assegnaSettoreButton" />
<div>

<ul>
	<li class="ufficioCorrente"><a href="javascript:document.getElementById('levelUpSettoreButton').click();">
	<bean:write name="documentoRepertorioForm" property="settoreCorrente.nomeUfficio" /></a>
		<html:submit styleClass="button" style="display: none;" styleId="levelUpSettoreButton" property="settorePrecedenteAction" value=".." title="Vai al livello superiore" />
	<ul>
		<logic:notEmpty name="documentoRepertorioForm" property="ufficiSettoriDipendenti">
			<logic:iterate id="ufficio" name="documentoRepertorioForm"
				property="ufficiSettoriDipendenti">
				<li class="ufficio"><bean:write name="ufficio"
					property="nomeUfficio" />
					[<a class="treeSettoreAction" href="javascript:treeSettoreAction(<bean:write name="ufficio" property="id"/>,'assegnaSettoreButton','settoreCorrenteId')">Assegna</a>] 
					[<a class="treeSettoreAction" href="javascript:treeSettoreAction(<bean:write name="ufficio" property="id"/>,'vaiSettoreButton','settoreSelezionatoId');">Vai</a>]</li>

			</logic:iterate>
		</logic:notEmpty>

	</ul>
	</li>
</ul>
<logic:notEmpty name="documentoRepertorioForm" property="settoreProponente">
	<table>
		<tr>
			<td>
				<span><strong>
    				<bean:write name="documentoRepertorioForm" property="settoreProponente"/>
    			</strong></span>
    		</td>
    		<td>
    			<html:submit styleClass="button" property="annullaSettoreAction"value="annulla" title="annulla" />
    		</td>
    	</tr>
    </table>
</logic:notEmpty>

</div>
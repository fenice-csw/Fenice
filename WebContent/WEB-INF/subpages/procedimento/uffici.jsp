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
<input type="hidden" id="treeSelection" value="-1"/>

<html:submit styleClass="button" property="assegnaUtenteAction"
					value="Assegna" title="Assegna l'utente" style="display: none;" styleId="assegnaUtenteButton"/>
<div>
<ul>
	
	<li class="ufficioCorrente"><a href="javascript:document.getElementById('levelUpButton').click();"><bean:write name="procedimentoForm"
		property="ufficioCorrente.nomeUfficio" /></a>
	<ul>
		
		<logic:notEmpty name="procedimentoForm" property="utenti">
			<logic:iterate id="utente" name="procedimentoForm" property="utenti">

				<li class="utente"><bean:write name="utente"property="caricaFullName" />
					<logic:equal name="procedimentoForm" property="tipoProcedimento.ull" value="true">
						<logic:empty name="procedimentoForm" property="istruttori">
							[<a class="treeAction" href="javascript:treeAction(<bean:write name="utente" property="id"/>,'assegnaUtenteButton','utenteSelezionatoId');">Assegna</a>]
						</logic:empty>
					</logic:equal>
					
					<logic:notEqual name="procedimentoForm" property="tipoProcedimento.ull" value="true">
						[<a class="treeAction" href="javascript:treeAction(<bean:write name="utente" property="id"/>,'assegnaUtenteButton','utenteSelezionatoId');">Assegna</a>]
					</logic:notEqual>
				</li>
			
			</logic:iterate>

		</logic:notEmpty>
	</ul>
	</li>
</ul>

</div>
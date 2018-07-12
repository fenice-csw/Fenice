<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

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
  <input type="hidden" id="treeSelection"/ value="-1"/>

<html:submit styleClass="button" property="impostaUfficioAction"
				value="Vai" title="Imposta l'ufficio come corrente" styleId="vaiButton" style="display: none;"/>
  <tr>
    <td class="label">
    <logic:equal name="fascicoloForm" property="dipendenzaTitolarioUfficio" value="1">
      <span ><bean:message key="fascicolo.ufficio" /> :<span class="obbligatorio"> * </span> <html:hidden property="ufficioCorrenteId" /></span>
     </logic:equal>
     <logic:equal name="fascicoloForm" property="dipendenzaTitolarioUfficio" value="0">
     <span ><bean:message key="fascicolo.ufficio" /> :  <html:hidden property="ufficioCorrenteId" /></span>  
     </logic:equal>
    </td>
    <td>
    <div class="sezione">
		<div style="display:none;">
			<html:select property="ufficioCorrenteId">
				<option value='<bean:write name="fascicoloForm" property="ufficioCorrente.id"/>'>
		        	<bean:write name="fascicoloForm" property="ufficioCorrente.nomeUfficio" />
		        </option>
	 	    </html:select>
		</div>
		
<ul>
	<li class="ufficioCorrente"><a href="javascript:document.getElementById('levelUpButton').click();">
	<bean:write name="fascicoloForm" property="ufficioCorrente.nomeUfficio" /></a>
		<html:submit
		styleClass="button" style="display: none;" styleId="levelUpButton" property="ufficioPrecedenteAction" value=".."
		title="Vai al livello superiore" />
		<ul>
			<logic:notEmpty name="fascicoloForm" property="ufficiDipendenti">
				<logic:iterate id="ufficio" name="fascicoloForm"
					property="ufficiDipendenti">
					<logic:equal name="ufficio" property="attivo" value="true">
						<li class="ufficio">
						<bean:write name="ufficio"property="nomeUfficio" />
						    [<a class="treeAction" href="javascript:treeAction(<bean:write name="ufficio" property="id"/>,'vaiButton','ufficioSelezionatoId');">Vai</a>]
						</li>  
					</logic:equal>
				</logic:iterate>
			</logic:notEmpty>
		</ul>
	</li>
</ul>
		
	</div>   
	</td>

</tr>




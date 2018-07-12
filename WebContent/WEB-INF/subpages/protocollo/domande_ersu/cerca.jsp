<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<script>
function selectStato(){
	document.getElementById("selezionaStatoButton").click();
}

</script>

<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>
<br />

<table>

    <tr>
		<td align="left">
			<span></span>
			<label for="numeroDomanda">
			Cerca numero domanda:
		</label>
			<html:text property="numeroDomanda" styleId="numeroDomanda" size="10" maxlength="10" />
	    <td align="right">
		    <html:submit styleClass="submit" property="cercaDomandaAction" value="Cerca" alt="Cerca domanda"/>&nbsp;&nbsp;
	    </td>
	    
	  
			<td colspan="3">
			
			    <html:select  disabled="false" property="stato" onchange="selectStato();">
					<html:option value="Tutti" ></html:option>
					<html:optionsCollection name="listaDomandeForm" property="statiDomanda" value="description" label="description" />	
				</html:select>
			<html:submit styleClass="button" property="impostaStatoAction"
				value="Vai" title="Imposta lo Stato" styleId="selezionaStatoButton" style="display: none;"/>			
			
			</td>
	 
   </tr>
</table>

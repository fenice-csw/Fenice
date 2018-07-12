<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<eprot:page title="Gestione mezzi di spedizione">

<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>

<html:form action="/page/amministrazione/mezzispedizione/spedizione.do">
<table summary="">
	<tr>  
		<td class="label">
			<label for="codiceSpedizione"><bean:message key="spedizione.codice"/></label>
			<span class="obbligatorio"> * </span>:
		</td>  
		<td>
			<html:hidden property="id"/>
			<html:text styleClass="obbligatorio" property="codiceSpedizione" styleId="codiceSpedizione" size="5" maxlength="5"></html:text>
		</td>  
	</tr>
	<tr>  
    	<td class="label">
      		<label for="descrizioneSpedizione"><bean:message key="spedizione.descrizione" /></label>
      		<span class="obbligatorio"> * </span>:
    	</td>  
    	<td>
      		<html:text property="descrizioneSpedizione" styleClass="obbligatorio" styleId="descrizioneSpedizione" size="50" maxlength="50"></html:text>
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
      		<label for="prezzo"><bean:message key="spedizione.prezzo" /></label>:
    	</td>  
    	<td>
      		<html:text property="prezzo" styleClass="obbligatorio" styleId="prezzo" size="10" maxlength="10"></html:text>
    	</td>  
	</tr>
	<tr>
    	<td class="label">
      		<label><bean:message key="spedizione.abilitato"/></label>: 
    	</td>  
		<td>
			<html:radio property="flagAbilitato"  value="true" styleId="abilitatoSI">
				<label for="abilitatoSI"><bean:message key="spedizione.si"/></label>
			</html:radio>
			<html:radio property="flagAbilitato" value="false" styleId="abilitatoNo">
				<label for="abilitatoNo"><bean:message key="spedizione.no"/></label>
			</html:radio>
		</td>
	</tr>	
	<logic:equal value="true" name="spedizioneForm" property="flagCancellabile">
	<tr>
    	<td class="label">
      		<label><bean:message key="spedizione.cancellabile"/></label>: 
    	</td>  
		<td>
			<html:radio property="flagCancellabile" styleId="cancellabileSI" value="true">
				<label for="cancellabileSI"><bean:message key="spedizione.si"/></label>
			</html:radio>
			<html:radio property="flagCancellabile" value="false" styleId="cancellabileNo">
				<label for="cancellabileNo"><bean:message key="spedizione.no"/></label>
			</html:radio>
		</td>
	</tr>	
	</logic:equal>	
	<tr>
		<td></td>
		<td>		
			<logic:greaterThan value="0" name="spedizioneForm" property="id">
				<html:submit styleClass="submit" property="btnConferma" value="Modifica" title="Modifica i dati del mezzo di spedizione" />
				<logic:equal value="true" name="spedizioneForm" property="flagCancellabile">
					<html:submit styleClass="submit" property="btnCancella" value="Cancella" title="Cancella il mezzo di spedizione"/>
				</logic:equal>
			</logic:greaterThan>
			<logic:equal value="0" name="spedizioneForm" property="id">
				<html:submit styleClass="submit" property="btnConferma" value="Salva" title="Inserisce il nuovo mezzo di spedizione" />
			</logic:equal>
			<html:submit styleClass="submit" property="btnAnnulla" value="Annulla" title="Annulla l'operazione" />
		</td>
	</tr>
</table>
</html:form>
</eprot:page>

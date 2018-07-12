<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<table summary="">
	<tr>
		<td class="label">
			<label for="codice"><bean:message key="amministrazione.registro.codiceregistro"/>:<span class="obbligatorio"> * </span></label>
		</td>
		<td>				
			<html:hidden property="id" />
			<html:hidden property="versione" />
			<html:hidden property="apertoIngresso" />
			<html:hidden property="apertoUscita" />
			<html:hidden property="dataApertura" />

			<html:text styleClass="obbligatorio" property="codice" styleId="codice" size="30" maxlength="30"></html:text>
		</td>
	</tr>
	<tr>
		<td class="label">
			<label for="descrizione"><bean:message key="amministrazione.registro.descrizione"/>:<span class="obbligatorio"> * </span></label>
		</td>
		<td>				
			<html:text styleClass="obbligatorio" property="descrizione" styleId="descrizione" size="50" maxlength="100"></html:text>
		</td>
	</tr>
	<tr>
	    <td class="label"><span><bean:message key="amministrazione.registro.cambiodata"/></span>&nbsp;:</td>
	    <td>
	      <html:radio property="dataBloccata" styleId="dataAutomatica" value="true">
	        <label for="dataAutomatica"><bean:message key="amministrazione.registro.automatico"/></label>&nbsp;&nbsp;
	      </html:radio>
	      <html:radio property="dataBloccata" styleId="dataManuale" value="false">
	        <label for="dataManuale"><bean:message key="amministrazione.registro.manuale"/></label>
	      </html:radio>
	    </td>
	</tr>
	<tr>
	    <td class="label"><span><bean:message key="amministrazione.registro.registroufficiale"/></span>&nbsp;:</td>
	    <td>
	      <html:radio property="ufficiale" styleId="ufficialeSi" value="true">
	        <label for="ufficialeSi"><bean:message key="amministrazione.registro.registroufficiale.si"/></label>&nbsp;&nbsp;
	      </html:radio>
	      <html:radio property="ufficiale" styleId="ufficialeNo" value="false">
	        <label for="ufficialeNo"><bean:message key="amministrazione.registro.registroufficiale.no"/></label>
	      </html:radio>
			
	    </td>
	</tr>
	
	<tr>
		<td></td>
		<td>
			<html:submit styleClass="submit" property="btnSalva" value="Salva" title="Salva il registro"/>
			
			<logic:greaterThan name="registroForm" property="id" value="0">
				<html:submit styleClass="submit" property="btnCancella" value="Cancella" alt="Cancella il registro selezionato" />
			</logic:greaterThan>	
			
			<html:submit styleClass="button" property="annulla" value="Annulla" title="Annulla l'operazione selezionata e torna alla lista registri"/>
		</td>
	</tr>
	
</table>	

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />
<table summary="">
	<tr>
		<td>
		<table summary="">
			<jsp:include page="/WEB-INF/subpages/fascicolo/cerca/titolario.jsp" />
		</table>
		</td>
	</tr>

	<tr>
		<td><label for="nome"><bean:message key="fascicolo.anno" />:</label>
		<html:text property="anno" styleId="anno" size="4" maxlength="4"></html:text>
		&nbsp; <label for="nome"><bean:message
			key="fascicolo.progressivo" />:</label> <html:text property="progressivo"
			styleId="progressivo" size="10" maxlength="10"></html:text>&nbsp;&nbsp;
			
				<label for="stato"><bean:message key="fascicolo.stato" />:</label>
				<html:select property="stato">
					<html:option value="-1">Tutti</html:option>
					<html:optionsCollection property="statiFascicolo" value="id" label="description" />
				</html:select>
		</td>
	</tr>
	<tr>
		<td><label for="oggettoFascicolo"><bean:message
			key="fascicolo.oggetto" />:</label> <html:text property="oggettoFascicolo"
			styleId="oggettoFascicolo" size="40" maxlength="100"></html:text></td>
	</tr>
	<tr>
		<td><label for="noteFascicolo"><bean:message
			key="fascicolo.note" />:</label> <html:text property="noteFascicolo"
			styleId="noteFascicolo" size="44" maxlength="100"></html:text></td>
	</tr>
	<tr>
		<td><label for="comune"><bean:message
			key="fascicolo.comune" />:</label> <html:text property="comune"
			styleId="comune" size="44" maxlength="100"></html:text></td>
	</tr>
	<tr>
		<td><label for="capitolo"><bean:message
			key="fascicolo.capitolo" />:</label> <html:text property="capitolo"
			styleId="capitolo" size="44" maxlength="100"></html:text></td>
	</tr>
	  
	<tr>
		<td><label for="foglio"><bean:message
			key="fascicolo.foglio" />:</label> <html:text property="collocazioneValore1"
			maxlength="50"></html:text>
			<label for="particelle"><bean:message
			key="fascicolo.particelle" />:</label> <html:text property="collocazioneValore2"
			maxlength="100"></html:text
		></td>
	</tr>
	
	<tr>
		<td><label for="indirizzo"><bean:message
			key="fascicolo.indirizzo" />:</label> <html:text property="collocazioneValore3"
			maxlength="100"></html:text>
			<label for="collocazione"><bean:message
			key="fascicolo.collocazione" />:</label> <html:text property="collocazioneValore4"
			maxlength="100"></html:text>
		</td>
	</tr>
	<tr>
		<td><span><bean:message key="fascicolo.data.apertura" /></span>
		<label title="Data apertura minima" for="dataAperturaDa"><bean:message
			key="protocollo.da" /></label>: <html:text property="dataAperturaDa"
			styleId="dataAperturaDa" size="10" maxlength="10" /> <eprot:calendar
			textField="dataAperturaDa" /> &nbsp; <label
			title="Data apertura massima" for="dataAperturaA"><bean:message
			key="protocollo.a" /></label>: <html:text property="dataAperturaA"
			styleId="dataAperturaA" size="10" maxlength="10" /> <eprot:calendar
			textField="dataAperturaA" /></td>
	</tr>


	<tr>

		<td>
		<div style="display: none;"><span><bean:message
			key="fascicolo.data.evidenza" /></span> <label title="Data apertura minima"
			for="dataAperturaDa"><bean:message key="protocollo.da" /></label>: <html:text
			property="dataEvidenzaDa" styleId="dataEvidenzaDa" size="10"
			maxlength="10" /> <eprot:calendar textField="dataEvidenzaDa" />
		&nbsp; <label title="Data apertura massima" for="dataEvidenzaA"><bean:message
			key="protocollo.a" /></label>: <html:text property="dataEvidenzaA"
			styleId="dataEvidenzaA" size="10" maxlength="10" /> <eprot:calendar
			textField="dataEvidenzaA" /></div>
		</td>

	</tr>


	<tr>
		<td><jsp:include
			page="/WEB-INF/subpages/fascicolo/cerca/referenteIstruttore.jsp" /></td>
	</tr>
	<tr>
		<td>Interessato/Delegato: <html:text
			property="descrizioneInteressatoDelegato" size="30" maxlength="100" />
		</td>
	</tr>
	<tr>
		<td>
		<br><html:submit styleClass="submit" property="btnCercaFascicoli"
			value="Cerca" title="Cerca Fascicoli" /> <html:reset
			styleClass="submit" property="ResetAction" value="Annulla"
			alt="Azzera i campi della ricerca" /> 
			
			<logic:equal name="tornaProtocollo" value="true">
				<html:submit styleClass="button" property="btnAnnulla" 
				value="Indietro" title="Torna alla pagina precedente" />
			</logic:equal> 
			
			<logic:equal name="tornaEditor" value="true">
				<html:submit styleClass="button" property="btnAnnulla" 
				value="Indietro" title="Torna alla pagina precedente" />
			</logic:equal>
			
			<logic:equal name="tornaTemplate" value="true">
				<html:submit styleClass="button" property="btnAnnulla" 
				value="Indietro" title="Torna alla pagina precedente" />
			</logic:equal>  
			
			<logic:equal name="tornaDocumento" value="true">
				<html:submit styleClass="button" property="btnAnnulla"
				value="Indietro" title="Torna alla pagina precedente" />
			</logic:equal> 
			<logic:equal name="tornaFaldone" value="true">
				<html:submit styleClass="button" property="btnAnnulla"
				value="Indietro" title="Torna alla pagina precedente" />
			</logic:equal> 
			<logic:equal name="tornaProcedimento" value="true">
				<html:submit styleClass="button" property="btnAnnulla"
				value="Indietro" title="Torna alla pagina precedente" />
			</logic:equal>
			<logic:equal name="tornaFascicoloCollegato" value="true">
				<html:submit styleClass="button" property="btnAnnulla"
				value="Indietro" title="Torna alla pagina precedente" />
			</logic:equal>
			</td>
	</tr>
</table>

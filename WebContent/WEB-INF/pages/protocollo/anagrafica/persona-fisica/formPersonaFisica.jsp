<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Gestione persona fisica">

<jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />

<br />

<div>
	<bean:message key="campo.obbligatorio.msg" />
</div>

<html:form action="/page/protocollo/anagrafica/persona-fisica/nuovo.do">
<div class="sezione">
  <span class="title"><strong><bean:message key="soggetto.fisica.datigenerali"/></strong></span>
<table>
	<tr >
	    <td class="label">
	    	<html:hidden property="soggettoId" />
			<label for="cognome"><bean:message key="soggetto.fisica.cognome"/></label><span
          class="obbligatorio"> * </span>:
		</td>
		<td>
		<html:text property="cognome" styleClass="obbligatorio" styleId="cognome" size="30" maxlength="100" /> 
		<label for="nome"> <bean:message key="soggetto.fisica.nome"/></label></label><span
          class="obbligatorio"> * </span>:
		<html:text property="nome" styleId="nome" size="20" maxlength="40" />
		</td>
	</tr>		
	<tr >
	    <td class="label">
		    <label for="codiceFiscale"><bean:message key="soggetto.fisica.codicefiscale"/>:</label>
		</td>
		<td>
		<html:text property="codiceFiscale" styleId="codiceFiscale" size="16" maxlength="16" />
		<label for="sesso"><bean:message key="soggetto.fisica.sesso"/>:</label>
		<html:select property="sesso">
		<option value="M"><bean:message key="soggetto.fisica.sessom"/></option>
		<option value="F"><bean:message key="soggetto.fisica.sessof"/></option>
		</html:select>
		<label for="statoCivile"><bean:message key="soggetto.fisica.statocivile" />:</label>
		<html:select property="statoCivile">
		<html:optionsCollection property="statiCivili" value="codice" label="descrizione" />
		</html:select>
		</td>
	</tr>		
	<tr >
	    <td class="label">
	    	<label for="dataNascita"><bean:message key="soggetto.fisica.datanascita" />:</label>
		</td>
		<td>
			<jsp:include page="/WEB-INF/subpages/protocollo/anagrafica/persona-fisica/dataNascita.jsp" />
		</td>
	</tr>		
	<tr >
	    <td class="label">
	    	<label for="comuneNascita"><bean:message key="soggetto.fisica.comunenascita" />:</label>
		</td>
		<td>
	  	<html:text property="comuneNascita" styleId="comuneNascita" size="20" maxlength="50" />
		<label for="provinciaNascitaId"><bean:message key="soggetto.provincia" />:</label>
		<html:select property="provinciaNascitaId">
		<html:optionsCollection property="province" value="provinciaId" label="descrizioneProvincia" />
		</html:select>
		</td>
	</tr>		
	<tr >
	    <td class="label">
	    	<label for="qualifica"><bean:message key="soggetto.fisica.qualifica" />:</label>
		</td>
		<td>
	  		<html:text property="qualifica" styleId="qualifica" size="30" maxlength="30" />
		</td>
	</tr>
	<tr >
		<td class="label">
	    	<label for="indirizzoEMail"><bean:message key="soggetto.email" />:</label>
		</td>
	    <td align="left">
			<html:text property="soggetto.indirizzoEMail" styleId="indirizzoEMail" size="30" maxlength="100" />
		</td>
	</tr>		
			
</table>
</div>  
<div class="sezione">
  <span class="title"><strong><bean:message key="soggetto.fisica.altridati"/></strong></span>
	<jsp:include page="/WEB-INF/subpages/protocollo/anagrafica/datiComuni.jsp" />
</div>

<div id="bottoni_salva">
<html:submit styleClass="submit" property="salvaAction" value="Salva" alt="Salva" />
<logic:equal name="personaFisicaForm" property="indietroVisibile" value="true" >
	<html:submit styleClass ="submit" property="indietroPF" value="Indietro" alt="Indietro" />
</logic:equal>
<logic:notEqual name="personaFisicaForm" property="indietroVisibile" value="true" >
	<logic:greaterThan name="personaFisicaForm" property="soggettoId" value="0"> 
		<html:submit styleClass="submit" property="deleteAction" value="Cancella" alt="Cancella" />
	</logic:greaterThan> 
</logic:notEqual>   
<html:reset styleClass="submit" property="ResetAction" value="Pulisci" alt="Pulisci" />		
</div>
	
</html:form>

</eprot:page>

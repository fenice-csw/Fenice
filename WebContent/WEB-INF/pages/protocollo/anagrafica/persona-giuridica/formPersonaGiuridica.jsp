<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Gestione Persona Giuridica">



<jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />

<br />

<div>
	<bean:message key="campo.obbligatorio.msg" />
</div>

<html:form action="/page/protocollo/anagrafica/persona-giuridica/nuovo.do">
<div class="sezione">
<span class="title"><strong><bean:message key="soggetto.giuridica.datigenerali"/></strong></span>
<table summary="">
	<tr>
	    <td class="label"><label for="descrizioneDitta">
		<html:hidden property="soggettoId" />
		<bean:message key="soggetto.giuridica.denominazione"/></label><span
          class="obbligatorio"> * </span>:
		</td>
	    <td>
		<html:text property="descrizioneDitta" styleId="descrizioneDitta" styleClass="obbligatorio" size="50" maxlength="100" />
		</td>
	</tr>	
	<tr >
	    <td class="label"><label for="partitaIva">
		<bean:message key="soggetto.giuridica.piva"/>:</label>
		</td>
	    <td>
		<html:text property="partitaIva" styleId="partitaIva" size="11" maxlength="11" />
		</td>
	</tr>	
	<tr >
	    <td class="label"><label for="dug">
		<bean:message key="soggetto.giuridica.dug"/>:</label>
		</td>
	    <td>
		<html:text property="dug" styleId="dug" size="10" maxlength="10" />
		</td>
	</tr>	
	<tr >
	    <td class="label"><label for="flagSettoreAppartenenza">
		<bean:message key="soggetto.giuridica.settore"/>:</label>
		</td>
	    <td>
		<html:select property="flagSettoreAppartenenza">
		<option value="Privato"><bean:message key="soggetto.giuridica.settore.privato"/></option>
		<option value="Pubblico"><bean:message key="soggetto.giuridica.settore.pubblico"/></option>
		</html:select>
		</td>
	</tr>	
	<tr >
	    <td class="label"><label for="referente">
	  	<bean:message key="soggetto.giuridica.referente" />:</label>
		</td>
	    <td>
	  	<html:text property="referente" styleId="referente" size="50" maxlength="90" />
		</td>
	</tr>	
	<tr >
	    <td class="label"><label for="telefonoReferente">
		<bean:message key="soggetto.giuridica.telefonoreferente"/>:</label>
		</td>
	    <td>
	  	<html:text property="telefonoReferente" styleId="telefonoReferente" size="16" maxlength="16" />
		</td>
	</tr>	
	<tr >
	    <td class="label"><label for="emailReferente">
			<bean:message key="soggetto.giuridica.emailreferente" />:</label>
		</td>
	    <td>
			<html:text property="emailReferente" styleId="emailReferente" size="30" maxlength="100" />
		</td>
	</tr>	
</table>
</div>
<div class="sezione">
  <span class="title"><strong><bean:message key="soggetto.giuridica.altridati"/></strong></span>
	<jsp:include page="/WEB-INF/subpages/protocollo/anagrafica/datiComuni.jsp" />
</div>

<div id="bottoni_salva">
<html:submit styleClass="submit"  property="salvaAction" value="Salva" alt="Salva" />
	<logic:equal name="personaGiuridicaForm" property="indietroVisibile" value="true" >
		<html:submit styleClass ="submit" property="indietroPG" value="Indietro" alt="Indietro" />
	</logic:equal>
<logic:notEqual name="personaGiuridicaForm" property="indietroVisibile" value="true" >
	<logic:greaterThan name="personaGiuridicaForm" property="soggettoId" value="0"> 
		<html:submit styleClass="submit"  property="deleteAction" value="Cancella" alt="Cancella" />
	</logic:greaterThan>  
</logic:notEqual>  
<html:reset styleClass="submit" property="ResetAction" value="Pulisci" alt="Pulisci" />		
</div>

</html:form>

</eprot:page>

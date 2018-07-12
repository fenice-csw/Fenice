<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />

<eprot:page title="Faldone">
	<div id="protocollo-errori"><html:errors bundle="bundleErroriFaldone" />
	</div>

	<html:form action="/page/faldone.do">


		<div class="sezione"><span class="title"><strong> <logic:equal
			name="faldoneForm" property="faldoneId" value="0">
			<bean:message key="protocollo.faldone.nuovofaldone" />
		</logic:equal> <logic:notEqual name="faldoneForm" property="faldoneId"
			value="0">
			<bean:message key="protocollo.faldone.modificafaldone" />
		</logic:notEqual> </strong></span>

		<div><jsp:include
			page="/WEB-INF/subpages/protocollo/faldone/uffici.jsp" /></div>
		<div><jsp:include
			page="/WEB-INF/subpages/protocollo/faldone/titolario.jsp" /></div>

		<table>

			<tr>
				<td class="label"><html:hidden name="faldoneForm" property="aooId" />
				<label for="sottoCategoria"><bean:message
					key="protocollo.faldone.sotto_categoria" />: </label></td>
				<td><html:text name="faldoneForm" property="sottoCategoria"
					styleId="sottoCategoria" size="20" maxlength="250" /></td>
			</tr>
			<tr>
				<td class="label"><label for="oggetto"><bean:message
					key="protocollo.faldone.oggetto" /><span class="obbligatorio"> * </span>:
				</label></td>
				<td><html:text name="faldoneForm" property="oggetto"
					styleId="oggetto" size="55" maxlength="100"
					styleClass="obbligatorio" /></td>
					
				
					
			</tr>
			<tr>
			
				<td class="label" ><label for="codiceLocale"><bean:message
					key="protocollo.faldone.codice_locale" /><span
					class="obbligatorio"> * </span> : </label>
					</td>
					
				<td colspan="2"><html:text name="faldoneForm" property="codiceLocale"
					styleId="codiceLocale" size="13" maxlength="20"
					styleClass="obbligatorio" />
					<html:submit styleClass="button"
					property="caricaMatricolaAction" value="Matricola"
					title="Carica matricola" />
					<logic:greaterThan name="faldoneForm" property="numeroMatricole" value="1">	
				<logic:notEmpty name="faldoneForm" property="matricole">
					<html:select style="font-size: 70%"  property="matricola" styleId="matricola">
						<logic:iterate id="matricola" name="faldoneForm"
							property="matricole">
							<option
								value='<bean:write name="matricola" property="matricola"/>'><bean:write
								name="matricola" property="nomeCompleto" /></option>
						</logic:iterate>
					</html:select> 
					
					<html:submit styleClass="button"
						property="impostaMatricolaAction" value="Vai"
						title="Imposta l'oggetto e il codice locale" />
				   		
						
				</logic:notEmpty>
				</logic:greaterThan>	
				</td>
			
			
			</tr>
			<tr>
				<td class="label"><label for="notaFaldone"><bean:message
					key="protocollo.faldone.nota" />:</label> <br />
				<span><html:link action="/page/unicode.do?campo=nota"
					target="_blank">segni diacritici</html:link></span></td>
				<td colspan="3"><html:textarea name="faldoneForm" property="nota"
					styleId="noteFascicolo" cols="52" rows="2"></html:textarea></td>
			</tr>
			<tr>
				<td class="label"><label for="stato"><bean:message
					key="faldone.stato" /></label>:</td>
				<td><html:select name="faldoneForm" property="posizioneSelezionata">
					<html:optionsCollection property="statiFaldone" value="id"
						label="descrizione" />
				</html:select></td>
			</tr>
			<tr>
				<td class="label"><label title="Data creazione faldone"
					for="dataCreazione"> <bean:message key="fascicolo.data.apertura" /></label>:
				</td>
				<%--			<logic:greaterThan name="faldoneForm" property="faldoneId" value="0" >-->
<!--				<html:text styleId="dataApertura"  disabled="true"  styleClass="text" property="dataApertura" size="10" maxlength="10" />-->
<!--			</logic:greaterThan>--%>
				<logic:greaterThan name="faldoneForm" property="faldoneId" value="0">
					<td><span><bean:write name="faldoneForm" property="dataCreazione" /></span>
					</td>
				</logic:greaterThan>
				<logic:equal name="faldoneForm" property="faldoneId" value="0">
					<td><html:text styleClass="text" property="dataCreazione"
						styleId="dataCreazione" size="10" maxlength="10" /> <eprot:calendar
						textField="dataCreazione" /> &nbsp;</td>
				</logic:equal>
			</tr>

			<tr>
				<td class="label"><label title="Data scarico faldone"
					for="dataScarico"> <bean:message key="fascicolo.data.scarico" /> </label>:
				</td>
				<td><html:text styleClass="text" property="dataScarico"
					styleId="dataScarico" size="10" maxlength="10" /> <eprot:calendar
					textField="dataScarico" /></td>
			</tr>
			<tr>
				<td class="label"><label title="Data carico faldone"
					for="dataCarico"> <bean:message key="fascicolo.data.carico" /> </label>:
				</td>
				<td><html:text styleClass="text" property="dataCarico"
					styleId="dataCarico" size="10" maxlength="10" /> <eprot:calendar
					textField="dataCarico" /></td>
			</tr>

			<tr>
				<td class="label"><label title="Data evidenza" for="dataEvidenza"><bean:message
					key="fascicolo.data.evidenza" /></label>:</td>
				<td><html:text styleClass="text" property="dataEvidenza"
					styleId="dataEvidenza" size="10" maxlength="10" /> <eprot:calendar
					textField="dataEvidenza" /></td>
			</tr>
			<tr>
				<td class="label"><label title="Data movimento" for="dataMovimento"><bean:message
					key="fascicolo.data.movimento" /></label>:</td>
				<td><html:text styleClass="text" property="dataMovimento"
					styleId="dataMovimento" disabled="true" size="10" maxlength="10" />
				<%--		     <eprot:calendar textField="dataMovimento" />--%></td>
			</tr>
			<%--	<tr>  -->
<!--	    <td class="label">-->
<!--	      <span>Locazione Fisica: </span>-->
<!--	    </td>-->
<!--	    <td class="label">-->
<!--	      <span></span>-->
<!--	    </td>-->
<!--	</tr>--%>

			<logic:notEmpty name="faldoneForm" property="collocazioneLabel1">
				<tr>
					<td class="label"><label title="Collocazione"
						for="collocazioneValore1"> <bean:write name="faldoneForm"
						property="collocazioneLabel1" /> </label>:</td>
					<td><html:text styleId="collocazioneValore1" disabled="false"
						styleClass="text" property="collocazioneValore1" size="20"
						maxlength="40" /></td>
				</tr>
			</logic:notEmpty>
			<logic:notEmpty name="faldoneForm" property="collocazioneLabel2">
				<tr>
					<td class="label"><label title="Collocazione"
						for="collocazioneValore2"> <bean:write name="faldoneForm"
						property="collocazioneLabel2" /> </label>:</td>
					<td><html:text styleId="collocazioneValore2" disabled="false"
						styleClass="text" property="collocazioneValore2" size="20"
						maxlength="40" /></td>
				</tr>
			</logic:notEmpty>
			<logic:notEmpty name="faldoneForm" property="collocazioneLabel3">
				<tr>
					<td class="label"><label title="Collocazione"
						for="collocazioneValore3"> <bean:write name="faldoneForm"
						property="collocazioneLabel3" /> </label>:</td>
					<td><html:text styleId="collocazioneValore3" disabled="false"
						styleClass="text" property="collocazioneValore3" size="20"
						maxlength="40" /></td>
				</tr>
			</logic:notEmpty>
			<logic:notEmpty name="faldoneForm" property="collocazioneLabel4">
				<tr>
					<td class="label"><label title="Collocazione"
						for="collocazioneValore4"> <bean:write name="faldoneForm"
						property="collocazioneLabel4" /> </label>:</td>
					<td><html:text styleId="collocazioneValore4" disabled="false"
						styleClass="text" property="collocazioneValore4" size="20"
						maxlength="40" /></td>
				</tr>
			</logic:notEmpty>

		</table>
		</div>
		<br class="hidden" />
		<div class="sezione"><span class="title"><strong><bean:message
			key="protocollo.faldone.fascicoli" /></strong></span> <jsp:include
			page="/WEB-INF/subpages/protocollo/faldone/listaFascicoli.jsp" /> <jsp:include
			page="/WEB-INF/subpages/protocollo/faldone/cercaFascicoli.jsp" /></div>
		<div class="sezione"><span class="title"><strong><bean:message
			key="protocollo.faldone.procedimenti" /></strong></span> <jsp:include
			page="/WEB-INF/subpages/protocollo/faldone/listaProcedimenti.jsp" />
		<jsp:include
			page="/WEB-INF/subpages/protocollo/faldone/cercaProcedimenti.jsp" />
		</div>
		<div>
			<html:submit styleClass="submit" property="salvaAction" value="Salva" alt="Salva" /> 
			<html:reset styleClass="submit" property="ResetAction" value="Pulisci" alt="Annulla" />
		</div>

	</html:form>

</eprot:page>

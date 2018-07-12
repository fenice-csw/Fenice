<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Gestione utenti">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" /></div>
	<div id="messaggi"><logic:messagesPresent message="true">
		<ul>
			<html:messages id="actionMessage" message="true"
				bundle="bundleMessaggiAmministrazione">
				<li><bean:write name="actionMessage" /></li>
			</html:messages>
		</ul>
	</logic:messagesPresent></div>
	<html:form action="/page/amministrazione/carica.do">
		<html:hidden property="caricaId" />

		<table summary="">
			<tr>
				<td class="label"><label for="nome">Denominazione <span
					class="obbligatorio"> * </span></label>:</td>
				<td><html:text property="nome" styleClass="obbligatorio"></html:text>
				</td>
			</tr>
			<logic:greaterThan name="caricaForm" property="caricaId" value="0">
				<tr>  
    				<td class="label">
      					<label for="attivo">Responsabile Ente</label>: 
    				</td>  
    				<td>
    					<logic:equal name="caricaForm" property="responsabileEnte" value="true">
			  				<strong>SI</strong>&emsp;<html:submit styleClass="submit" property="btnRemoveResponsabileEnte" value="Rimuovi" title="Disattiva responsabile dell'ufficio di Protocollo"/>
			 			</logic:equal>
			 			<logic:equal name="caricaForm" property="responsabileEnte" value="false">
			 				<strong>NO</strong>&emsp;<html:submit styleClass="submit" property="btnSetResponsabileEnte" value="Assegna" title="Assegna responsabile dell'ufficio di Protocollo"/>
			 			</logic:equal>
					</td>  
				</tr>
			</logic:greaterThan>
			<tr>
				<td colspan="2">
				<div class="sezione"><span class="title"> <strong>Profili<span
					class="obbligatorio"> * </span></strong> </span>
				<p /><html:select property="profiloId">
					<html:optionsCollection property="profiliCollection" value="id"
						label="descrizioneProfilo" />
				</html:select> <html:submit styleClass="submit" property="selectProfiloAction"
					value="Seleziona" title="Seleziona il profilo" />
				<p /><logic:notEmpty name="caricaForm" property="profilo">
					<span>Profilo Selezionato: <strong> <bean:write
						name="caricaForm" property="profilo.descrizioneProfilo" /> </strong> </span>
				</logic:notEmpty>
				</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
				<div class="sezione"><span class="title"> <strong>Utenti</strong>
				</span>
				<p /><html:select property="utenteId">
					<html:optionsCollection property="utentiCollection" value="id" label="fullName" />
				</html:select> <html:submit styleClass="submit" property="selectUtenteAction"
					value="Seleziona" title="Seleziona l'utente" />
				<p />
				<logic:notEmpty name="caricaForm" property="utente">
					<span>Utente Selezionato: <strong> <bean:write
						name="caricaForm" property="utente.fullName" /></strong></span>
					<html:submit styleClass="submit" property="removeUtenteAction"
						value="Rimuovi" title="Rimuovi l'utente" />
				</logic:notEmpty>
				<logic:empty name="caricaForm" property="utente">
					<strong><span>Nessun Utente Selezionato </span></strong>
				</logic:empty>
				</div>
				</td>
			</tr>
			
			<tr>
				<td colspan="3"><html:submit styleClass="submit"
					property="btnSalva" value="Salva" title="Inserisce la nuova carica"></html:submit>
				<html:submit styleClass="button" property="btnIndietro"
					value="Annulla" title="Annulla l'operazione"></html:submit>
				<logic:greaterThan name="caricaForm" property="versione" value="0">
				<html:submit styleClass="submit"
					property="btnStoria" value="Storia" title="Storico della carica"></html:submit>
					</logic:greaterThan>
				</td>
			</tr>
		</table>
	</html:form>
</eprot:page>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Permessi utenti">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriAmministrazione" />
</div>
<div id="protocollo-messaggi">
  <logic:messagesPresent message="true">
   <ul>
   <html:messages id="actionMessage" message="true" bundle="bundleMessaggiAmministrazione">
      <li>
      <bean:write name="actionMessage"/>
      </li>
   </html:messages> 
   </ul>
  </logic:messagesPresent>
</div>
<html:form action="/page/amministrazione/organizzazione/utenti/profilo.do">
<table summary="">
	<tr>  
    	<td class="label">
    		<label for="username"><bean:message bundle="bundleMessaggiAmministrazione" key="username"/></label>:
    	</td>  
    	<td><span><strong><html:hidden property="id" />
    	<bean:write name="profiloUtenteForm" property="userName" />
    	</strong></span></td>
	</tr>
	<tr>  
    	<td class="label">
			<label for="cognome"><bean:message bundle="bundleMessaggiAmministrazione" key="cognome"/></label>:
    	</td>  
    	<td><span><strong>
    	<bean:write name="profiloUtenteForm" property="cognome" />
    	</strong></span></td>
	</tr>
	<tr>  
    	<td class="label">
			<label for="cognome"><bean:message bundle="bundleMessaggiAmministrazione" key="nome"/></label>:
    	</td>  
    	<td><span><strong>
    	<bean:write name="profiloUtenteForm" property="nome" />
    	</strong></span></td>
	</tr>

	<tr>
    	<td class="label">
      	<label for="ufficioSelezionatoId"><bean:message key="amministrazione.organizzazione.utenti.ufficioselezionato"/>:</label>
    	</td>
    	<td><span><strong><html:hidden property="ufficioSelezionatoId"/>
    	<bean:write name="profiloUtenteForm" property="ufficioCorrentePath" />
    	</strong></span></td>
	</tr>
	<tr>
    	<td colspan="2"><hr></hr></td>
	</tr>	    
	<tr>
				    <td>
						<span>
						<logic:notEmpty name="profiloUtenteForm" property="funzioniMenu">
						<fieldset>
						<legend><strong>Funzioni di protocollazione</strong></legend>
							<logic:iterate id="currentRecord" property="protocolloMenu" name="profiloUtenteForm">
								<html:multibox property="funzioniMenuSelezionate"><bean:write name="currentRecord" property="id" /></html:multibox>
								<bean:write name="currentRecord" property="description"/><br/>
							</logic:iterate> <br/>
							<html:submit styleClass="submit"  property="assegnaProtocollazione" value="Assegna tutti i permessi" title="Abilita l'utente a tutte le funzioni elencate"></html:submit>
					
						</fieldset>   
						</logic:notEmpty>    
						</span>				    
				    </td>
					<td>
						<span>
						<logic:notEmpty name="profiloUtenteForm" property="funzioniMenu">
						<fieldset>
						<legend><strong>Funzioni Area Documentale</strong></legend>
							<logic:iterate id="currentRecord" property="documentaleMenu" name="profiloUtenteForm">
								<html:multibox property="funzioniMenuSelezionate"><bean:write name="currentRecord" property="id" /></html:multibox>
								<bean:write name="currentRecord" property="description"/><br/>
							</logic:iterate>  <br/>
							<html:submit styleClass="submit" property="assegnaDocumentale" value="Assegna tutti i permessi" title="Abilita l'utente a tutte le funzioni elencate"></html:submit>
			
						</fieldset>   
						</logic:notEmpty>    
						</span>
							<span>
							<logic:notEmpty name="profiloUtenteForm" property="funzioniMenu">
							<fieldset>
							<legend><strong>Funzioni Reportistica</strong></legend>
								<logic:iterate id="currentRecord" property="reportMenu" name="profiloUtenteForm">
									<html:multibox property="funzioniMenuSelezionate"><bean:write name="currentRecord" property="id" /></html:multibox>
									<bean:write name="currentRecord" property="description"/><br/>
								</logic:iterate>  <br/>
								<html:submit styleClass="submit" property="assegnaReport" value="Assegna tutti i permessi" title="Abilita l'utente a tutte le funzioni elencate"></html:submit>
			
							</fieldset>   
							</logic:notEmpty>    
						</span>		    
				    </td>
					<td>
						
						<span>
						<logic:notEmpty name="profiloUtenteForm" property="funzioniMenu">
						<fieldset>
						<legend><strong>Funzioni Area Amministrazione</strong></legend>
							<logic:iterate id="currentRecord" property="amministrazioneMenu" name="profiloUtenteForm">
								<html:multibox property="funzioniMenuSelezionate"><bean:write name="currentRecord" property="id" /></html:multibox>
								<bean:write  name="currentRecord" property="description"/><br/>
							</logic:iterate>  <br/>
							<html:submit styleClass="submit"  property="assegnaAmministrazione" value="Assegna tutti i permessi" title="Abilita l'utente a tutte le funzioni elencate"></html:submit>
			
						</fieldset>   
						</logic:notEmpty>    
						</span>	
					</td>
						
						<logic:iterate id="currentRecord" property="helpMenu" name="profiloUtenteForm">
							<input type="hidden" name="funzioniMenuSelezionate" value="<bean:write  name="currentRecord" property="id"/>"/>
						</logic:iterate> 	
				</tr>		    
	<tr>
		<td colspan="2"><hr></hr></td>
	</tr>	

	<tr>
		<td></td>
		<td>		
			<html:submit styleClass="submit" property="btnConfermaPermessi" value="Conferma" title="Abilita l'utente alle funzioni selezionate"></html:submit>
			<html:submit styleClass="submit" property="btnAbilitaTutto" value="Assegna tutti i permessi" title="Abilita l'utente a tutte le funzioni elencate"></html:submit>
			<html:submit styleClass="submit" property="btnCancellaTutto" value="Cancella i permessi" title="Cancella i permessi per l'ufficio selezionato"></html:submit>
			<html:submit styleClass="button" property="btnIndietro" value="Annulla" title="Annulla l'operazione"></html:submit>
		</td>
	</tr>	
</table>
</html:form>
</eprot:page>

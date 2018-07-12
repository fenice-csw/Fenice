<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<html:xhtml />
<table summary="">
	<tr>  
		<td class="label">
			<label for="codiceProfilo"><bean:message key="profilo.codice"/></label>
			<span class="obbligatorio"> * </span>:
		</td>  
		<td>
			<html:hidden property="id"/>
			<html:text styleClass="obbligatorio" property="codiceProfilo" size="20" maxlength="50"></html:text>
		</td>  
	</tr>
	<tr>  
    	<td class="label">
      		<label for="descrizioneProfilo"><bean:message key="profilo.descrizione"/></label>
      		<span class="obbligatorio"> * </span>:
    	</td>  
    	<td>
      		<html:text property="descrizioneProfilo" styleClass="obbligatorio" size="50" maxlength="200"></html:text>
    	</td>  
	</tr>
	

	<tr>  
    	<td colspan="2" class="label">
      		<hr></hr>
    	</td>  
	</tr>

	<tr>  
	    <td class="subtable" colspan="2">
    		<table summary="">
			    <tr>
					<td class="label">
						<span style="color: #00f"><strong><bean:message key="profilo.messaggio"/></strong></span>:<br/><br/>
					</td>  
				</tr>
				<tr>
				    <td>
						<span>
						<logic:notEmpty name="profiloForm" property="funzioniMenu">
						<fieldset>
						<legend><strong>Funzioni di protocollazione</strong></legend>
							<logic:iterate id="currentRecord" property="protocolloMenu" name="profiloForm">
								<html:multibox property="profiliMenu"><bean:write name="currentRecord" property="id" /></html:multibox>
								<bean:write name="currentRecord" property="description"/><br/>
							</logic:iterate> 
							<html:submit styleClass="submit" property="assegnaProtocollazione" value="Assegna tutti i permessi" title="Abilita l'utente a tutte le funzioni elencate"></html:submit>
			
						</fieldset>   
						</logic:notEmpty>    
						</span>				    
				    </td>
					<td>
						<span>
						<logic:notEmpty name="profiloForm" property="funzioniMenu">
						<fieldset>
						<legend><strong>Funzioni Area Documentale</strong></legend>
							<logic:iterate id="currentRecord" property="documentaleMenu" name="profiloForm">
								<html:multibox property="profiliMenu"><bean:write name="currentRecord" property="id" /></html:multibox>
								<bean:write name="currentRecord" property="description"/><br/>
							</logic:iterate> 
						<html:submit styleClass="submit" property="assegnaDocumentale" value="Assegna tutti i permessi" title="Abilita l'utente a tutte le funzioni elencate"></html:submit>
						</fieldset>   
						</logic:notEmpty>    
						</span>
							<span>
							<logic:notEmpty name="profiloForm" property="funzioniMenu">
							<fieldset>
							<legend><strong>Funzioni Reportistica</strong></legend>
								<logic:iterate id="currentRecord" property="reportMenu" name="profiloForm">
									<html:multibox property="profiliMenu"><bean:write name="currentRecord" property="id" /></html:multibox>
									<bean:write name="currentRecord" property="description"/><br/>
								</logic:iterate> 
						<html:submit styleClass="submit" property="assegnaReport" value="Assegna tutti i permessi" title="Abilita l'utente a tutte le funzioni elencate"></html:submit>
			
							</fieldset>   
							</logic:notEmpty>    
						</span>		    
				    </td>
					<td>
						
						<span>
						<logic:notEmpty name="profiloForm" property="funzioniMenu">
						<fieldset>
						<legend><strong>Funzioni Area Amministrazione</strong></legend>
							<logic:iterate id="currentRecord" property="amministrazioneMenu" name="profiloForm">
								<html:multibox property="profiliMenu"><bean:write name="currentRecord" property="id" /></html:multibox>
								<bean:write  name="currentRecord" property="description"/><br/>
							</logic:iterate> 
						<html:submit styleClass="submit" property="assegnaAmministrazione" value="Assegna tutti i permessi" title="Abilita l'utente a tutte le funzioni elencate"></html:submit>
			
						</fieldset>   
						</logic:notEmpty>    
						</span>			
					</td>
						<logic:iterate id="currentRecord" property="helpMenu" name="profiloForm">
							<input type="hidden" name="profiliMenu" value="<bean:write  name="currentRecord" property="id"/>"/>
						</logic:iterate>
					
				</tr>
			</table>
		</td>							
	</tr>
	<tr>
		<td></td>
		<td>		
			<logic:greaterThan value="0" name="profiloForm" property="id">
				<html:submit styleClass="submit" property="btnConferma" value="Modifica" title="Modifica i dati del profilo" />
				<html:submit styleClass="submit" property="btnCancella" value="Cancella" title="Cancella il profilo"/>
			</logic:greaterThan>
			<logic:equal value="0" name="profiloForm" property="id">
				<html:submit styleClass="submit" property="btnConferma" value="Salva" title="Inserisce il nuovo profilo" />
			</logic:equal>
			<html:submit styleClass="submit" property="btnAnnulla" value="Vai alla lista profili" title="Annulla l'operazione e torna alla lista dei profili" />
		</td>
	</tr>
</table>
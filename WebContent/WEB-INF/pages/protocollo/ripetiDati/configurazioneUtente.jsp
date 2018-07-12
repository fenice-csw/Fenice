<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Configurazione Utente">


<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>


<html:form action="/page/protocollo/ripetiDati.do">

	<div id="sezioni-protocolli">  
	<br />
	<table summary="">
			<tr>
				<td>
					<label for="checkOggetto"><bean:message key="protocollo.ripetidati.configurazioneutente.oggetto"/>:</label>
				</td>
				<td>					
					<html:checkbox property="checkOggetto" styleId="checkOggetto" />
				</td>
				<td>&nbsp;</td>
				<td class="label">
				    <html:text property="oggetto" styleId="oggetto" size="50" maxlength="2000"></html:text>
				</td>   
			</tr>
			<tr>
				<td>
					<label for="checkTipoDocumento"><bean:message key="protocollo.ripetidati.configurazioneutente.tipodocumento"/>:</label>
				</td>
					<td>
						<html:checkbox property="checkTipoDocumento" styleId="checkTipoDocumento"></html:checkbox>
					</td>
					<td> &nbsp; </td>
					<td>
			              <html:select property="tipoDocumentoId" styleClass="tipiDocumento" disabled="false">
				            <html:optionsCollection property="tipiDocumento" value="id" label="descrizione" />
			              </html:select>
		    	    </td>    
			</tr>
			<tr>
				<td>
					<label for="checkDataDocumento"><bean:message key="protocollo.ripetidati.configurazioneutente.datadocumento"/>:</label>
				</td>
				<td>
					<html:checkbox property="checkDataDocumento" styleId="checkDataDocumento"></html:checkbox>
				</td>
				<td>&nbsp;</td>				
				<td>
				
			        <html:radio property="dataDocumento" styleId="dataDocumento" value="1">
						<label for="dataDocumento"><bean:message key="protocollo.ripetidati.configurazioneutente.datacorrente"/></label>
			        </html:radio>
			        &nbsp;&nbsp;
			        <html:radio property="dataDocumento" styleId="dataDocumento1" value="0">
			          <label for="dataDocumento1"><bean:message key="protocollo.ripetidati.configurazioneutente.nessuna"/></label>
			        </html:radio>
			        &nbsp;&nbsp;
			    </td>
			</tr>
			<tr>
				<td>
					<label for="checkRicevuto"><bean:message key="protocollo.ripetidati.configurazioneutente.checkricevuto"/>:</label>
				</td>
				<td>
					<html:checkbox property="checkRicevuto" styleId="checkRicevuto"></html:checkbox>
				</td>
				<td>&nbsp;</td>
				<td>
			        <html:radio property="dataRicezione" styleId="dataRicezione" value="1">
			          <label for="dataRicezione"><bean:message key="protocollo.ripetidati.configurazioneutente.datacorrente"/></label>
			        </html:radio>
			        &nbsp;&nbsp;
			        <html:radio property="dataRicezione" styleId="dataRicezione1" value="0" >
			          <label for="dataRicezione1"><bean:message key="protocollo.ripetidati.configurazioneutente.nessuna"/></label>
			        </html:radio>
			        &nbsp;&nbsp;
			    </td>
			</tr>
			<tr>
				<td>
					<label for="checkTipoMittente"><bean:message key="protocollo.ripetidati.configurazioneutente.tipomittente"/>:</label>
				</td>
				<td>
					<html:checkbox property="checkTipoMittente" styleId="checkTipoMittente"></html:checkbox>
				</td>
				<td>&nbsp;</td>
			    <td>
			        <html:radio property="tipoMittente" styleId="personaFisica" value="F" >
			          <label for="personaFisica"><bean:message key="protocollo.mittente.personafisica"/></label>
			        </html:radio>
			        &nbsp;&nbsp;
			        <html:radio property="tipoMittente" styleId="personaGiuridica" value="G">
			          <label for="personaGiuridica"><bean:message key="protocollo.mittente.personagiuridica"/></label>
			        </html:radio>
			        &nbsp;&nbsp;
			    </td>
			</tr>
			<tr>
				<td>
					<label for="checkMittente"><bean:message key="protocollo.ripetidati.configurazioneutente.mittente"/>:</label>
				</td>
				<td>
					<html:checkbox property="checkMittente" styleId="checkMittente"></html:checkbox>
				</td>
				<td>&nbsp;</td>
				<td class="label" align="left">
			      <html:text property="mittente" styleId="mittente" size="50" maxlength="150"></html:text>
			    </td>
			</tr>
			<tr>
				<td>
					<label for="checkDestinatari"><bean:message key="protocollo.ripetidati.configurazioneutente.destinatario"/>:</label>
				</td>
				<td>
					<html:checkbox property="checkDestinatari" styleId="checkDestinatari"></html:checkbox>
				</td>
				<td>&nbsp;</td>
				<td class="label">
				    <html:text property="destinatario" styleId="destinatario" size="50" maxlength="150"></html:text>
				</td>
			</tr>
		</table>
		<hr/>
		<div>
			<table>	
				<tr>
					<td>
						<label for="checkAssegnatari">Assegnatari:</label>
					</td>
					<td>
						<html:checkbox property="checkAssegnatari" styleId="checkAssegnatari"></html:checkbox>
					</td>
					<td>&nbsp;</td>
					<td>
				      <jsp:include page="/WEB-INF/pages/protocollo/ripetiDati/assegnatari.jsp" />
				    </td>
				</tr>
				
			</table>
		</div>
		<hr/>
		<div>
		<table>	
			<tr>
				<td>
					<label for="checkTitolario">Titolario:</label>
				</td>
				<td>
					<html:checkbox property="checkTitolario" styleId="checkTitolario"></html:checkbox>
				</td>
				<td>&nbsp;</td>
				<td>
					<jsp:include page="/WEB-INF/pages/protocollo/ripetiDati/titolario.jsp" />
				</td>	
			</tr>	
		</table>
		</div>
	<p id="bottoni_salva">	
		<html:submit styleClass="submit" property="btnSalva" value="Salva" alt="Salva configurazione"/>
		<logic:greaterThan name="configurazioneForm" property="utenteId" value="0">
			<html:submit styleClass="submit" property="btnCancella" value="Cancella" alt="Salva configurazione"/>
		</logic:greaterThan>
		<html:submit styleClass="submit" property="btnAnnulla" value="Annulla" alt="Annulla"/>
	</p>
	
	</div>
</html:form>
</eprot:page>
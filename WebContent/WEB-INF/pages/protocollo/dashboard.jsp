<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Dashboard">
	
	<eprot:ifAuthorized permission="mail_log">
		<logic:equal name="log_pec" property="tipoLog" value="1">
  				<div id="protocollo-errori" align="center">
  					<ul>
  						<li>
  							<bean:write name="log_pec" property="dataString"/> - 
   							<bean:write name="log_pec" property="message"/>
   						</li>
   					</ul>
   				</div>
  			</logic:equal>
  	</eprot:ifAuthorized>

	<html:form action="/cambioPwd.do"></html:form>
	<eprot:ifAuthorized permission="1">
		<div align="center">
		<br>
		<br>
		<br>
	<div id="zona_lavoro_dash">
		<div id="titolo_zonalavoro" align="left"><strong>Attività</strong></div>
		<div id="corpo_zonalavoro" align="left">
			<table border="0" cellpadding="5" cellspacing="5" width="100%">
				
				<tr>
					<td><span>Protocolli Assegnati (Ufficio): </span></td>
					<td><bean:write name="assegnati_ufficio" /></td>
					<logic:notEqual name="assegnati_ufficio" value="0">
						<td><html:link href="page/protocollo/dashboard/assegnati.do?type=ufficio">Visualizza</html:link></td>
					</logic:notEqual>
					<logic:equal name="assegnati_ufficio" value="0">
						<td>Visualizza</td>
					</logic:equal>
				</tr>
			
				<tr>
					<td><span>Protocolli Assegnati (Utente):</span></td>
					<td><bean:write name="assegnati_utente" /></td> 
					<logic:notEqual name="assegnati_utente" value="0">
						<td><html:link href="page/protocollo/dashboard/scarico.do?type=carico">Visualizza</html:link></td>
					</logic:notEqual>
					<logic:equal name="assegnati_utente" value="0">
						<td>Visualizza</td>
					</logic:equal>
				</tr>
			
				<tr>
					<td><span>Posta Interna (Utente): </span></td>	
					<td><bean:write name="posta_interna_utente" /></td>
					<logic:notEqual name="posta_interna_utente" value="0">
						<td><html:link href="page/protocollo/dashboard/posta_interna.do?type=utente">Visualizza</html:link></td>
					</logic:notEqual>
					<logic:equal name="posta_interna_utente" value="0">
						<td>Visualizza</td>
					</logic:equal>
				</tr>
				
				<tr>
					<td><span>Posta Interna (Ufficio): </span></td>	
					<td><bean:write name="posta_interna_ufficio" /></td>					
					<logic:notEqual name="posta_interna_ufficio" value="0">
						<td><html:link href="page/protocollo/dashboard/posta_interna.do?type=ufficio">Visualizza</html:link></td>
					</logic:notEqual>
					<logic:equal name="posta_interna_ufficio" value="0">
						<td>Visualizza</td>
					</logic:equal>
				</tr>
				
				<logic:notEqual name="da_repertoriare" value="0">
				<tr>
					<td><span>Da Repertoriare: </span></td>	
					<td><bean:write name="da_repertoriare" /></td>					
					<td><html:link href="page/protocollo/dashboard/posta_interna_repertorio.do">Visualizza</html:link></td>
				</tr>
				</logic:notEqual>
				
				<eprot:ifAuthorized permission="60">
					<tr>
						<td><span>Protocolli Respinti: </span></td>
						<td><bean:write name="respinti_utente" /></td>
						<logic:notEqual name="respinti_utente" value="0">
							<td><html:link href="page/protocollo/dashboard/respinti.do">Visualizza</html:link></td>
						</logic:notEqual>
						<logic:equal name="respinti_utente" value="0">
							<td>Visualizza</td>
						</logic:equal>
					</tr>
				</eprot:ifAuthorized>
			
				<tr>
					<td><span>Per Conoscenza (Ufficio): </span></td>
					<td><bean:write name="conoscenza_ufficio" /></td>
					<logic:notEqual name="conoscenza_ufficio" value="0">
						<td><html:link href="page/protocollo/dashboard/conoscenza.do?type=ufficio">Visualizza</html:link></td>
					</logic:notEqual>
					<logic:equal name="conoscenza_ufficio" value="0">
						<td>Visualizza</td>
					</logic:equal>
				</tr>
			
				<tr>
					<td><span>Per Conoscenza (Utente): </span></td>
					<td><bean:write name="conoscenza_utente" /></td>
					<logic:notEqual name="conoscenza_utente" value="0">
						<td><html:link href="page/protocollo/dashboard/conoscenza.do?type=utente">Visualizza</html:link></td>
					</logic:notEqual>
					<logic:equal name="conoscenza_utente" value="0">
						<td>Visualizza</td>
					</logic:equal>
				</tr>
			
				<logic:notEqual name="check_pi" value="0">
					<tr>
						<td><span>Notifiche Posta Interna: </span></td>
						<td><bean:write name="check_pi" /></td>
						<td><html:link href="page/protocollo/dashboard/showCheckPostaInterna.do">Visualizza</html:link></td>
					</tr>
				</logic:notEqual>

				<tr>
					<td><span>Procedimenti in Alert: </span></td>
					<td><bean:write name="procedimenti_alert" /></td>
					<logic:notEqual name="procedimenti_alert" value="0">
						<td><html:link href="page/protocollo/dashboard/alertProcedimento.do">Visualizza</html:link></td>
					</logic:notEqual>
					<logic:equal name="procedimenti_alert" value="0">
						<td>Visualizza</td>
					</logic:equal>
				</tr>
				
				<tr>
					<td><span>Protocolli in alert: </span></td>
					<td><bean:write name="protocolli_alert" /></td>
					<logic:notEqual name="protocolli_alert" value="0">
						<td><html:link href="page/protocollo/dashboard/alertProtocollo.do">Visualizza</html:link></td>
					</logic:notEqual>
					<logic:equal name="protocolli_alert" value="0">
						<td>Visualizza</td>
					</logic:equal>
				</tr>
				
				<logic:notEqual name="ricorsi" value="0">
					<tr>
						<td><span>Ricorsi Straordinari: </span></td>
						<td><bean:write name="ricorsi" /></td>
						<td><html:link href="page/protocollo/dashboard/showRicorsi.do">Visualizza</html:link></td>
					</tr>
				</logic:notEqual>
				
				<logic:notEqual name="fatture_ufficio" value="0">
					<tr>
						<td><span>Fatture (Ufficio): </span></td>
						<td><bean:write name="fatture_ufficio" /></td>
						<td><html:link href="page/protocollo/dashboard/showFatture.do?type=ufficio">Visualizza</html:link></td>
					</tr>
				</logic:notEqual>
				
				<logic:notEqual name="fatture_utente" value="0">
					<tr>
						<td><span>Fatture (Utente): </span></td>
						<td><bean:write name="fatture_utente" /></td>
						<td><html:link href="page/protocollo/dashboard/showFatture.do?type=utente">Visualizza</html:link></td>
					</tr>
				</logic:notEqual>
				
				<logic:notEqual name="fatture_respinte" value="0">
					<tr>
						<td><span>Fatture (Respinte): </span></td>
						<td><bean:write name="fatture_respinte" /></td>
						<td><html:link href="page/protocollo/dashboard/showFatture.do?type=respinte">Visualizza</html:link></td>
					</tr>
				</logic:notEqual>
				
				<logic:notEqual name="da_firmare" value="0">
					<tr>
						<td><span>Bozze Documenti: </span></td>
						<td><bean:write name="da_firmare" /></td>
						<td><html:link href="page/protocollo/dashboard/showDocumentiFirma.do">Visualizza</html:link></td>
					</tr>
				</logic:notEqual>
				
				<logic:notEqual name="relazioni_decreti" value="0">
					<tr>
						<td><span>Relazioni e Decreti: </span></td>
						<td><bean:write name="relazioni_decreti" /></td>
						<td><html:link href="page/protocollo/dashboard/showAvvocatoGeneraleDashboard.do">Visualizza</html:link></td>
					</tr>
				</logic:notEqual>
								
				<eprot:ifAuthorized permission="email_ufficio">
					<tr>
						<td><span>Email (Ufficio): </span></td>
						<td><bean:write name="email_ufficio" /></td>
						<logic:notEqual name="email_ufficio" value="0">
							<td><html:link href="page/protocollo/ingresso/email_ufficio.do">Visualizza</html:link></td>
						</logic:notEqual>
						<logic:equal name="email_ufficio" value="0">
							<td>Visualizza</td>
						</logic:equal>	
					</tr>
				</eprot:ifAuthorized>
				
				<logic:notEqual name="procedimenti_istruttore" value="0">
					<tr>
						<td><span>Istruttore Procedimenti: </span></td>
						<td><bean:write name="procedimenti_istruttore" /></td>
						<td><html:link href="page/protocollo/dashboard/showProcedimentiIstruttore.do">Visualizza</html:link></td>
					</tr>
				</logic:notEqual>
				
				<logic:notEqual name="procedimenti_ufficio_partecipante" value="0">
					<tr>
						<td><span>Procedimenti Assegnati: </span></td>
						<td><bean:write name="procedimenti_ufficio_partecipante" /></td>
						<td><html:link href="page/protocollo/dashboard/showProcedimentiUfficioPartecipante.do">Visualizza</html:link></td>
					</tr>
				</logic:notEqual>
				
				<logic:notEqual name="repertori" value="0">
					<tr>
						<td colspan="2"><span>Bacheca</span></td>
						<td><html:link href="page/protocollo/dashboard/showRepertori.do">Visualizza</html:link></td>
					</tr>
				</logic:notEqual>
				
				<eprot:ifAuthorized permission="conservation_daily_registry">
					<logic:notEqual name="log_job" value="0">
						<tr>
							<td><span>Logs Conservazione</span></td>
							<td><bean:write name="log_job" /></td>
							<td><html:link href="page/protocollo/dashboard/showJobScheduledLogs.do">Visualizza</html:link></td>
						</tr>
					</logic:notEqual>
				</eprot:ifAuthorized>
			</table>
		</div>
	</div>

		</div>
	</eprot:ifAuthorized>

</eprot:page>

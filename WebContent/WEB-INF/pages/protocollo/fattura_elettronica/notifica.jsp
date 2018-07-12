<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Notifica Esito Committente">
	<html:form action="/page/fatturaelettronica/notifica.do" enctype="multipart/form-data">
		<table summary="">
			<logic:notEmpty name="notificaEsitoCommittenteForm" property="documentoPrincipale.fileName">
			<tr>
			    <td class="label">	
			  		<bean:message key="protocollo.documento.file"/>:
			  	</td>
				<td> 
					<html:link action="/page/fatturaelettronica/notifica.do" 
						paramId="visualizzaFatturaBrowser" 
					    paramName="notificaEsitoCommittenteForm" 
					    paramProperty="documentoPrincipale.fileName"
					    title="Visualizza nel browser"
					    target="_blank">
			        	<span><strong>
							<bean:write name="notificaEsitoCommittenteForm" property="documentoPrincipale.fileName" />
			        	</strong></span>
			      </html:link>
			   	</td>
			</tr> 
			</logic:notEmpty>
			<logic:notEmpty name="notificaEsitoCommittenteForm" property="notifica.fileName">
			<tr>
			    <td class="label">	
			  		Notifica File Committente:
			  	</td>
				<td> 
					<html:link action="/page/fatturaelettronica/notifica.do" 
						paramId="visualizzaNotificaBrowser" 
					    paramName="notificaEsitoCommittenteForm" 
					    paramProperty="notifica.fileName"
					    title="Visualizza nel browser"
					    target="_blank">
			        	<span><strong>
							<bean:write name="notificaEsitoCommittenteForm" property="notifica.fileName" />
			        	</strong></span>
			      </html:link>
			   	</td>
			</tr> 
			</logic:notEmpty>
			<tr>
			    <td class="label">Identificativo SDI:
			    </td>
			    <td> 
					<html:text name="notificaEsitoCommittenteForm" property="identificativoSDI"></html:text>
				</td>
			</tr>  
			<tr>
			    <td class="label">	
			    	Esito:
			    </td>
			    <td colspan="2"> 
					<html:select property="esito" styleClass="obbligatorio" disabled="false">
						<html:optionsCollection property="esiti" value="codice" label="descrizione" />
					</html:select>
				</td>
			</tr>
			<tr>
			    <td class="label">Descrizione:
			    </td>
			    <td> 
					<html:textarea name="notificaEsitoCommittenteForm" property="descrizione"cols="50" rows="5"></html:textarea>
				</td>
			</tr>
		</table>
		<logic:notEmpty name="notificaEsitoCommittenteForm" property="allaccio">	
		<div class="sezione">
			<span class="title">
				<strong>Protocollo</strong>
			</span>
			<p>
  				<span><bean:write name="notificaEsitoCommittenteForm" property="allaccio.allaccioDescrizione" /></span>&nbsp;&nbsp;
			</p>
		</div>
		</logic:notEmpty>
		<html:submit styleClass="submit" property="btnIndietro" value="Indietro" alt="Torna al protocollo in ingresso" />
		<html:submit styleClass="submit" property="btnCreaNotifica" value="Notifica" alt="Crea File Notifica" />
		<logic:notEmpty name="notificaEsitoCommittenteForm" property="notifica.fileName">
			<html:submit styleClass="submit" property="btnProtocolla" value="Protocolla" alt="Protocolla" />
		</logic:notEmpty>
	</html:form>
</eprot:page>

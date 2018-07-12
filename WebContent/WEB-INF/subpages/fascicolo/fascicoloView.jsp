<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<html:xhtml />
<div class="sezione">
	<span class="title">
	<strong><bean:message key="fascicolo.fascicolo"/></strong>
	</span>	 
	
	<table summary="">
		<logic:equal name="fascicoloForm" property="versioneDefault" value="false">
			<tr>  
		    	<td class="label">
		    		<label for="codice"><bean:message key="fascicolo.versione"/>:</label>
		    	</td>  
		    	<td>
			    	<span><strong><bean:write name="fascicoloForm" property="versione"/></strong></span>
		    	</td>  
			</tr>
		</logic:equal>

		<tr>  
	    	<td class="label">
	    		<label for="codice"><bean:message key="fascicolo.titolo"/>:</label>
	    	</td>  
	    	<td>
		    	<span><strong><bean:write name="fascicoloForm" property="annoProgressivo"/></strong></span>
	    	</td>  
		</tr>
		<tr>  
	    	<td class="label">
	    		<label for="oggettoFascicolo"><bean:message key="fascicolo.oggetto"/>:</label>
	    	</td>  
	    	<td>
		    	<span><strong><bean:write name="fascicoloForm" property="oggettoFascicolo" filter="false"/></strong></span>
	    	</td>  
		</tr>
	   
			<tr>
		    	<td class="label">
		      		<label for="mittente"><bean:message key="fascicolo.ufficio"/></label>:
		    	</td>
		    	 <logic:notEmpty name="fascicoloForm" property="mittente">
			    <td>
					<span><strong><bean:write name="fascicoloForm" property="mittente.descrizioneUfficio" /></strong></span>
			    </td>
				</logic:notEmpty>
			</tr>
		  
		
			<tr>
		    	<td class="labelEvidenziata">
		      		<label for="mittente"><bean:message key="fascicolo.referente"/></label>:
		    	</td>
		    	<logic:notEmpty name="fascicoloForm" property="mittente">
			    <td>
					<span class="evidenziato"><strong><bean:write name="fascicoloForm" property="mittente.nomeUtente" /></strong></span>
			    </td>
			</logic:notEmpty>  
			</tr>
		
			<tr>
		    	<td class="labelEvidenziata">
		      		<label for="mittente"><bean:message key="fascicolo.istruttore"/></label>:
		    	</td>
			    <td>
			    	<logic:notEmpty name="fascicoloForm" property="istruttore">
						<span><strong><bean:write name="fascicoloForm" property="istruttore.nomeUtente" /></strong></span>
					</logic:notEmpty>
					<logic:empty name="fascicoloForm" property="istruttore">
						<span><strong> </strong></span>
					</logic:empty>
			    </td>
			</tr>
			<tr>
		    	<td class="labelEvidenziata">
		      		<label for="mittente"><bean:message key="fascicolo.interessato"/></label>:
		    	</td>
			    <td>
			    	<logic:notEmpty name="fascicoloForm" property="interessato">
						<span><strong><bean:write name="fascicoloForm" property="interessato" /></strong></span>
			    	</logic:notEmpty>
			    	<logic:empty name="fascicoloForm" property="interessato">
						<span><strong> </strong></span>
					</logic:empty>
			    </td>
			</tr>
			<tr>
		    	<td class="labelEvidenziata">
		      		<label for="delegato"><bean:message key="fascicolo.delegato"/></label>:
		    	</td>
			    <td>
					<logic:notEmpty name="fascicoloForm" property="delegato">
						<span><strong><bean:write name="fascicoloForm" property="delegato" /></strong></span>
			    	</logic:notEmpty>
			    	<logic:empty name="fascicoloForm" property="delegato">
						<span><strong> </strong></span>
					</logic:empty>
			    </td>
			</tr>
			
			<tr>
		    	<td class="labelEvidenziata">
		      		<label for="padre">Fascicolo Padre</label>:
		    	</td>
			    <td>
					<logic:notEmpty name="fascicoloForm" property="padre">
						<span><strong><bean:write name="fascicoloForm" property="padre" /></strong></span>
			    	</logic:notEmpty>
			    	<logic:empty name="fascicoloForm" property="padre">
						<span><strong> </strong></span>
					</logic:empty>
			    </td>
			</tr>
       
		<tr>
	    	<td class="label">
	      		<label for="titolario"><bean:message key="fascicolo.livelli.titolario"/></label>:
	    	</td>
		    <td>
	   		    <logic:notEmpty name="fascicoloForm" property="titolario">
					<span><strong><bean:write name="fascicoloForm" property="titolario.descrizione"/></strong></span>
	   		    </logic:notEmpty>
		    </td>
		</tr>
		<tr>
	    	<td class="label">
	      		<label for="note"><bean:message key="fascicolo.note"/></label>:
	    	</td>
		    <td>
				<span><strong><bean:write name="fascicoloForm" property="note" filter="false"/></strong></span>
		    </td>
		</tr>
		<tr>
	    	<td class="label">
	      		<label for="comune"><bean:message key="fascicolo.comune"/></label>:
	    	</td>
		    <td>
				<span><strong><bean:write name="fascicoloForm" property="comune" filter="false"/></strong></span>
		    </td>
		</tr>
		<tr>
	    	<td class="label">
	      		<label for="capitolo"><bean:message key="fascicolo.capitolo"/></label>:
	    	</td>
		    <td>
				<span><strong><bean:write name="fascicoloForm" property="capitolo" filter="false"/></strong></span>
		    </td>
		</tr>
		<tr>
	    	<td class="label">
	      		<label for="a"><bean:message key="fascicolo.anno"/></label>:
	    	</td>
		    <td>
				<span><strong><bean:write name="fascicoloForm" property="annoRiferimento" /></strong></span>
		    </td>
		</tr>
		
		<tr>  
	    	<td class="label">
	    		<span><bean:message key="fascicolo.responsabile"/>:</span>
	    	</td>
	    	<td>
		    	<span><strong><bean:write name="fascicoloForm" property="responsabile" /></strong></span>
	    	</td>  
		</tr>

		<tr>  
	    	<td class="label">
	    		<span><bean:message key="fascicolo.tipo"/>:</span>
	    	</td>
	    	<td>
		    	<span><strong><bean:write name="fascicoloForm" property="descrizioneTipoFascicolo" /></strong></span>
	    	</td>  
		</tr>
		<tr>  
	    	<td class="labelEvidenziata">
				<label for="stato"><bean:message key="fascicolo.stato"/>:</label>
	    	</td>  
	    	<td>
	    		<span class="evidenziato">
	    			<strong><bean:write name="fascicoloForm" property="descrizioneStato"/></strong>
	    		</span>
		    </td>
		</tr>
		<tr>  
	    	<td class="label">
				<label title="Data apertura fascicolo" for="dataApertura"><bean:message key="fascicolo.data.apertura"/></label>:
	    	</td>  
	    	<td>
				<span><strong><bean:write name="fascicoloForm" property="dataApertura"/></strong></span>
		    </td>
		</tr>
		<%-- 
		<tr>  
	    	<td class="label">
				<label title="Data carico fascicolo" for="dataCarico"><bean:message key="fascicolo.data.carico"/></label>:
	    	</td>  
	    	<td>
				<span><strong><bean:write name="fascicoloForm" property="dataCarico"/></strong></span>
		    </td>
		</tr>
		<tr>  
	    	<td class="labelEvidenziata">
				<label title="Data evidenza fascicolo" for="dataEvidenza">
					<bean:message key="fascicolo.data.evidenza"/></label>:
	    	</td>  
	    	<td>
				<span class="evidenziato"><strong><bean:write name="fascicoloForm" property="dataEvidenza"/></strong></span>
		    </td>
		</tr>
		--%>
		<tr>  
	    	<td class="label">
				<label title="Data ultimo movimento" for="dataUltimoMovimento"><bean:message key="fascicolo.data.movimento"/></label>:
	    	</td>  
	    	<td>
				<span><strong><bean:write name="fascicoloForm" property="dataUltimoMovimento"/></strong></span>
		    </td>
		</tr>

	<logic:greaterThan name="fascicoloForm" property="statoFascicolo" value="0">
		<tr>  
	    	<td class="label">
				<label title="Data chiusura fascicolo" for="dataChiusura"><bean:message key="fascicolo.data.chiusura"/></label>:
	    	</td>  
	    	<td>
				<span><strong><bean:write name="fascicoloForm" property="dataChiusura" /></strong></span>
		    </td>
		</tr>
	</logic:greaterThan>

	<logic:equal name="fascicoloForm" property="statoFascicolo" value="3">
		<tr>  
	    	<td class="label">
				<label title="Data scarto fascicolo" for="dataScarto"><bean:message key="fascicolo.data.scarto"/></label>:
	    	</td>  
	    	<td>
				<span><strong><bean:write name="fascicoloForm" property="dataScarto" /></strong></span>
		    </td>
		</tr>
		<tr>  
	    	<td class="label">
				<label title="Destinazione scarto" for="destinazioneScarto"><bean:message key="fascicolo.destinazione.scarto"/></label>:
	    	</td>  
	    	<td>
				<span><strong><bean:write name="fascicoloForm" property="propostaScarto" /></strong></span>
		    </td>
		</tr>
	</logic:equal>
	
	
		<tr>	 
			<td class="label">
   			<label title="Foglio" for="collocazioneValore1">
   				<bean:write name="fascicoloForm" property="collocazioneLabel1" />
   			</label>:
			</td>  
			<td>
				<span><strong><bean:write name="fascicoloForm" property="collocazioneValore1" /></strong></span>
			</td>
		</tr>			
  		<tr>	 
			<td class="label">
   			<label title="Particelle" for="collocazioneValore2">
   				<bean:write name="fascicoloForm" property="collocazioneLabel2" />
   			</label>:
			</td>  
			<td>
				<span><strong><bean:write name="fascicoloForm" property="collocazioneValore2" /></strong></span>
			</td>
		</tr>			
  		<tr>	 
			<td class="label">
   			<label title="Indirizzo" for="collocazioneValore3">
   				<bean:write name="fascicoloForm" property="collocazioneLabel3" />
   			</label>:
			</td>  
			<td>
				<span><strong><bean:write name="fascicoloForm" property="collocazioneValore3" /></strong></span>
			</td>
		</tr>			
  		<tr>	 
			<td class="label">
   			<label title="Collocazione" for="collocazioneValore4">
   				<bean:write name="fascicoloForm" property="collocazioneLabel4" />
   			</label>:
			</td>  
			<td>
				<span><strong><bean:write name="fascicoloForm" property="collocazioneValore4" /></strong></span>
			</td>
		</tr>			
		</table>
</div>

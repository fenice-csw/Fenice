<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione Fascicolo">

<jsp:include page="/WEB-INF/subpages/protocollo/common/errori.jsp" />

<html:form action="/page/fascicolo" enctype="multipart/form-data">
<html:hidden property="id"/>
  <div class="sezione" styleClass="obbligatorio">
	<span class="title" >	 
	<strong><bean:message key="fascicolo.fascicolo"/></strong>
	</span>	    
	<table summary="">
		<jsp:include page="/WEB-INF/subpages/fascicolo/cercaPadre.jsp" />
		
		<logic:notEmpty name="fascicoloForm" property="faldoneCodiceLocale">
			<tr>  
		    	<td class="label">
		    		<label for="faldoneCodiceLocale"><bean:message key="fascicolo.faldone.codice.locale"/>:</label>
		    	</td>  
		    	<td>
		      		<span>
		      			<strong><bean:write name="fascicoloForm" property="faldoneCodiceLocale" /></strong>
		      		</span>
		    	</td>  
			</tr>		
		</logic:notEmpty>
		<logic:notEmpty name="fascicoloForm" property="faldoneOggetto">
			<tr>  
		    	<td class="label">
		    		<label for="faldoneOggetto"><bean:message key="fascicolo.faldone.oggetto"/>:</label>
		    	</td>  
		    	<td>
		      		<span>
		      			<strong><bean:write name="fascicoloForm" property="faldoneOggetto" /></strong>
		      		</span>
		    	</td>  
			</tr>		
		</logic:notEmpty>
		
		<logic:greaterThan name="fascicoloForm" property="id" value="0" >
		<tr>  
	    	<td class="label">
	    		<label for="progressivo"><bean:message key="fascicolo.progressivo"/></label>
	    	</td>  
	    	<td>
	      		<span><strong>
	      		<html:hidden name="fascicoloForm" property="progressivo" />
	      		<bean:write name="fascicoloForm" property="annoProgressivo" />
	      		</strong></span>
	    	</td>  
		</tr>
		</logic:greaterThan>
		<tr>
	    	<td class="label">
	      		<label for="oggettoFascicolo"><bean:message key="fascicolo.oggetto"/><span class="obbligatorio"> * </span>:
				<br/>
				<html:link action="/page/unicode.do?campo=oggettoFascicolo" target="_blank" >segni diacritici</html:link>
				</label>
	    	</td>
		    <td>
		      <html:textarea property="oggettoFascicolo" styleId="oggettoFascicolo" cols="52" rows="2" styleClass="obbligatorio"></html:textarea>
		    </td>
		</tr>
		<jsp:include page="/WEB-INF/subpages/fascicolo/ufficio.jsp" />
		<jsp:include page="/WEB-INF/subpages/fascicolo/titolario.jsp" />
		<tr>
	    	<td class="labelEvidenziata">
	      		<label for="note"><bean:message key="fascicolo.referente" /><span class="obbligatorio"> * </span></label>:
	    	</td>
		    <td>
		      <jsp:include page="/WEB-INF/subpages/fascicolo/referenti.jsp" />
		    </td>
		</tr>
		<tr>
	    	<td class="labelEvidenziata">
	      		<label for="note"><bean:message key="fascicolo.istruttore" /></label>:
	    	</td>
		    <td>
		      <jsp:include page="/WEB-INF/subpages/fascicolo/istruttore.jsp" />
		    </td>
		</tr>
		<tr>
	    	<td class="labelEvidenziata">
	      		<label for="note"><bean:message key="fascicolo.interessato" /></label>:
	    	</td>
		    <td>
		      <jsp:include page="/WEB-INF/subpages/fascicolo/interessato.jsp" />
		    </td>
		</tr>
		<tr>
	    	<td class="labelEvidenziata">
	      		<label for="note"><bean:message key="fascicolo.delegato" /></label>:
	    	</td>
		    <td>
		      <jsp:include page="/WEB-INF/subpages/fascicolo/delegato.jsp" />
		    </td>
		</tr>
	  	<tr>
	    	<td class="label">
	      		<label for="note"><bean:message key="fascicolo.note" />:
	      		<br/>
				<html:link action="/page/unicode.do?campo=note" target="_blank" >segni diacritici</html:link>
				</label>
	    	</td>
		    <td>
		      <html:textarea property="note" styleId="note" cols="52" rows="4"></html:textarea>
		    </td>
		</tr>
			<tr>
	    	<td class="label">
	      		<label for="comune"><bean:message key="fascicolo.comune" />:</label>
	    	</td>
		    <td>
		      <html:text property="comune" maxlength="50" ></html:text>
		    </td>
		</tr>
		<tr>
	    	<td class="label">
	      		<label for="capitolo"><bean:message key="fascicolo.capitolo" />:</label>
	    	</td>
		    <td>
		      <html:text property="capitolo" maxlength="100" ></html:text>
		    </td>
		</tr>
		
		<tr>  
	    	<td class="label">
	    		<label for="annoRiferimento"><bean:message key="fascicolo.anno" /></label>:
	    	</td>  
	    	<td>
	    		<html:text property="annoRiferimento" styleId="annoRiferimento" size="4" maxlength="4"></html:text>
	    	</td>  
		</tr>
		
		<tr>  
	    	<td class="label">
	    		<label for="responsabile"><bean:message key="fascicolo.responsabile" /></label>:
	    	</td>
	    	<td>
		    	<span><strong>
		    	<bean:write name="fascicoloForm" property="responsabile" />
		    	</strong></span>
	    	</td>  
		</tr>
		<tr>  
	    	<td class="label">
				<label for="tipoFascicolo"><bean:message key="fascicolo.tipo" /></label>:
	    	</td>  
	    	<td>	
				<html:select property="tipoFascicolo">
					<html:optionsCollection property="tipiFascicolo" value="codice" label="description" />
				</html:select>&nbsp;
	    	</td>  
		</tr>
		
		<tr>
		    <td class="label">
		      <label title="Data apertura fascicolo" for="dataApertura">
		      <bean:message key="fascicolo.data.apertura"/></label>:
		    </td>
		    <td>
			<logic:greaterThan name="fascicoloForm" property="id" value="0" >
				<span><strong><bean:write name="fascicoloForm" property="dataApertura" /></strong></span>
			</logic:greaterThan>
			<logic:equal name="fascicoloForm" property="id" value="0" >
			      <html:text styleClass="text" property="dataApertura" styleId="dataApertura" size="10" maxlength="10" />
			       <eprot:calendar textField="dataApertura" />
			      &nbsp;
			</logic:equal>
		    </td>
		</tr>
		
		<logic:greaterThan name="fascicoloForm" property="id" value="0" >
			<logic:notEmpty name="fascicoloForm" property="dataUltimoMovimento" >
			<tr>
			    <td class="label">
			      <label title="Data ultimo movimento" for="dataUltimoMovimento"><bean:message key="fascicolo.data.movimento"/></label>:
			    </td>
			    <td>
				    <span><strong><bean:write name="fascicoloForm" property="dataUltimoMovimento" /></strong></span>
				</td>
			</tr>
			</logic:notEmpty>
		</logic:greaterThan>
		

		<logic:notEmpty name="fascicoloForm" property="collocazioneLabel1">	
			<tr>	 
				<td class="label">
	   			<label title="Collocazione" for="collocazioneValore1">
	   				<bean:write name="fascicoloForm" property="collocazioneLabel1" />
	   			</label>:
				</td>  
				<td>
				    <html:text styleId="collocazioneValore1" disabled="false"  styleClass="text" property="collocazioneValore1" size="20" maxlength="40" />
				</td>
			</tr>			
   		</logic:notEmpty>
		<logic:notEmpty name="fascicoloForm" property="collocazioneLabel2">	
			<tr>	 
				<td class="label">
	   			<label title="Collocazione" for="collocazioneValore2">
	   				<bean:write name="fascicoloForm" property="collocazioneLabel2" />
	   			</label>:
				</td>  
				<td>
				    <html:text styleId="collocazioneValore2" disabled="false"  styleClass="text" property="collocazioneValore2" size="20" maxlength="40" />
				</td>
			</tr>			
   		</logic:notEmpty>
		<logic:notEmpty name="fascicoloForm" property="collocazioneLabel3">	
			<tr>	 
				<td class="label">
	   			<label title="Collocazione" for="collocazioneValore3">
	   				<bean:write name="fascicoloForm" property="collocazioneLabel3" />
	   			</label>:
				</td>  
				<td>
				    <html:text styleId="collocazioneValore3" disabled="false"  styleClass="text" property="collocazioneValore3" size="20" maxlength="40" />
				</td>
			</tr>			
   		</logic:notEmpty>
		<logic:notEmpty name="fascicoloForm" property="collocazioneLabel4">	
			<tr>	 
				<td class="label">
	   			<label title="Collocazione" for="collocazioneValore4">
	   				<bean:write name="fascicoloForm" property="collocazioneLabel4" />
	   			</label>:
				</td>  
				<td>
				    <html:text styleId="collocazioneValore4" disabled="false"  styleClass="text" property="collocazioneValore4" size="20" maxlength="40" />
				</td>
			</tr>			
   		</logic:notEmpty>
   	<!-- data evidenza -> display:none -->
		<tr>
		    <td class="labelEvidenziata">
		    <div style="display: none;">
		      <label title="Data evidenza" for="dataEvidenza"><bean:message key="fascicolo.data.evidenza"/></label>:
		    </div>
		    </td>
		    <td >
		    <div style="display: none;">
				<html:text styleClass="evidenziato" property="dataEvidenza" styleId="dataEvidenza" size="10" maxlength="10" />
				<eprot:calendar textField="dataEvidenza" />
			</div>
		    </td>
		</tr>
		
	</table>
</div>

<div id="bottoni_salva">
		<logic:match header="Referer"
			value="/page/protocollo/ingresso/fascicolaEdit.do">
			<html:submit styleClass="submit" property="btnConfermaNew"
				value="Conferma" title="Crea un nuovo fascicolo" />
			<html:reset styleClass="submit" property="ResetAction"
				value="Annulla" alt="Annulla l'operazione" />
			<logic:equal name="fascicoloForm" property="indietroVisibile"
				value="true">
				<html:submit styleClass="submit" property="btnAnnullaNew"
					value="Indietro" alt="Indietro" />
			</logic:equal>
		</logic:match>

		<logic:notMatch header="Referer"
			value="/page/protocollo/ingresso/fascicolaEdit.do">
			<html:submit styleClass="submit" property="btnConferma"
				value="Conferma" title="Crea un nuovo fascicolo" />
			<html:reset styleClass="submit" property="ResetAction"
				value="Annulla" alt="Annulla l'operazione" />
			<logic:equal name="fascicoloForm" property="indietroVisibile"
				value="true">
				<html:submit styleClass="submit" property="btnAnnulla"
					value="Indietro" alt="Indietro" />
			</logic:equal>
		</logic:notMatch>
</div>

</html:form>
</eprot:page>

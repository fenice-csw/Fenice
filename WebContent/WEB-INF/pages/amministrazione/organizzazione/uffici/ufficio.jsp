<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione uffici">

<div id="protocollo-errori">
  <html:errors bundle="bundleErroriAmministrazione" />
</div>

<html:form action="/page/amministrazione/organizzazione/ufficio.do">
<html:hidden property="id"/>
<table summary="">
	<tr>
    	<td class="label">
      		<span><bean:message key="amministrazione.organizzazione.uffici.ufficiopadre"/>: <html:hidden property="parentId" /></span>
    	</td>
    	<td>
      		<logic:notEqual name="ufficioForm" property="parentId" value="0">
	      		<bean:define id="ufficioPadre" name="ufficioForm" property="ufficioPadre" />
	      		<bean:define id="path" name="ufficioForm" property="ufficioPadre.path" />
	      		<span title="<bean:write name='ufficioForm' property='ufficioPadre.path' />">
	      		<strong>
					<bean:write name="ufficioPadre" property="valueObject.description"/>
	      		</strong></span>
	      	</logic:notEqual>	
	    </td>
	</tr>
	<tr>  
    	<td class="label">
    		<label for="description"><bean:message bundle="bundleMessaggiAmministrazione" key="descrizione"/>
      		<span class="obbligatorio"> * </span></label>:
    	</td>  
    	<td>
      		<html:text property="description" styleClass="obbligatorio" styleId="description" size="50" maxlength="254"></html:text>
    	</td>  
	</tr>
	<%-- AGGIUNTE --%>
	<tr>  
    	<td class="label">
    		<label for="telefono">Telefono</label>:
    	</td>  
    	<td>
      		<html:text property="telefono"styleId="telefono" size="20" maxlength="20"></html:text>
    	</td>  
	</tr>
	
	<tr>  
    	<td class="label">
    		<label for="fax">Fax</label>:
    	</td>  
    	<td>
      		<html:text property="fax"styleId="fax" size="20" maxlength="20"></html:text>
    	</td>  
	</tr>
	
	<tr>  
    	<td class="label">
    		<label for="email">Email</label>:
    	</td>  
    	<td>
      		<html:text property="email"styleId="email" size="50" maxlength="50"></html:text>
      	</td>
      	<td>
      		<a id="various1" href="#inline1" title="Parametri Email Ufficio">Imposta Parametri</a>
      	</td>  
	</tr>
	 <tr>
	 	<td>
	 		<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/uffici/fancybox_import.jsp" />
	 	</td>
	 </tr>
	<tr>  
    	<td class="label">
    		<label for="piano">Piano</label>:
    	</td>  
    	<td>
      		<html:text property="piano"styleId="piano" size="2" maxlength="2"></html:text>
    	</td>  
	</tr>
	
	<tr>  
    	<td class="label">
    		<label for="stanza">Stanza</label>:
    	</td>  
    	<td>
      		<html:text property="stanza"styleId="stanza" size="5" maxlength="5"></html:text>
    	</td>  
	</tr>
	
	<tr>  
    	<td class="label">
      		<label for="ufficioCentrale"><bean:message bundle="bundleMessaggiAmministrazione" key="tipoUfficio"/></label>: 
    	</td>      	
    	<td>
    		<logic:equal name="ufficioForm" property="parentId" value="0" >
				<html:hidden property="tipo" /><span><bean:message key="amministrazione.organizzazione.uffici.centrale"/></span>
			</logic:equal>	
    		<logic:greaterThan name="ufficioForm" property="parentId" value="0" >
				<html:select styleClass="obbligatorio" property="tipo">
					<html:optionsCollection name="tipiUfficio" value="codice" label="descrizione" />
				</html:select>
			</logic:greaterThan>
			<logic:equal name="ufficioForm" property="parentId" value="0" >
				<logic:equal name="ufficioForm" property="id" value="0" >
					<html:select styleClass="obbligatorio" property="tipo">
						<html:optionsCollection name="tipiUfficio" value="codice" label="descrizione" />
					</html:select>
				</logic:equal>
			</logic:equal>	
    		
    	</td>  
	</tr>	
	<logic:greaterThan name="ufficioForm" property="id" value="0">
	<tr>  
    	<td class="label">
      		<label for="attivo"><bean:message bundle="bundleMessaggiAmministrazione" key="attivo"/></label>: 
    	</td>  
    	<td>
    		
    		<logic:equal name="ufficioForm" property="parentId" value="0">
			  SI
			</logic:equal>
			<logic:notEqual name="ufficioForm" property="parentId" value="0">
			  
			 <logic:equal name="ufficioForm" property="attivo" value="true">
			  	SI&emsp;<html:submit styleClass="submit" property="btnDisattiva" value="Disattiva" title="Disattiva l'ufficio corrente"/>
			 </logic:equal>
			 <logic:equal name="ufficioForm" property="attivo" value="false">
			 	NO&emsp;<html:submit styleClass="submit" property="btnAttiva" value="Attiva" title="Attiva l'ufficio corrente"/>
			 </logic:equal>
			</logic:notEqual>
    	</td>  
	</tr>
	</logic:greaterThan>
	
	<logic:greaterThan name="ufficioForm" property="id" value="0">
	<tr>  
    	<td class="label">
      		<label for="attivo">Ufficio Protocollo</label>: 
    	</td>  
    	<td>
	 		<logic:equal name="ufficioForm" property="ufficioProtocollo" value="true">
	  			SI&emsp;<html:submit styleClass="submit" property="btnDisattivaUfficioProtocollo" value="Rimuovi" title="Disattiva come ufficio di protocollo"/>
	 		</logic:equal>
	 		<logic:equal name="ufficioForm" property="ufficioProtocollo" value="false">
	 			NO&emsp;<html:submit styleClass="submit" property="btnAttivaUfficioProtocollo" value="Attiva" title="Attiva come ufficio di protocollo"/>
	 		</logic:equal>
    	</td>  
	</tr>
	</logic:greaterThan>
	
</table>

<logic:greaterThan name="ufficioForm" property="id" value="0" >

<div class="sezione">
<span class="title">
		<strong>Cariche associate</strong>
	</span>
<logic:notEmpty name="ufficioForm" property="dipendentiUfficio" >

<table summary="" border="1" cellpadding="2" cellspacing="2" width="100%">
	<tr>
		<th><span>Carica</span></th>
    	<th><span>Utente</span></th>
    	<th><span>Referente</span></th>
    	<th><span>Dirigente</span></th>
    	<logic:equal name="ufficioForm" property="ufficioProtocollo" value="true">
    		<th><span>Resp. Ufficio Protocollo</span></th>
    	</logic:equal>
    	<th><span>Azioni</span></th>
	</tr>

	<logic:notEmpty name="ufficioForm" property="dipendentiUfficio" >
	<logic:iterate id="utente" name="ufficioForm" property="dipendentiUfficio" >
	<tr>
		<logic:equal name="utente" property="attivo" value="false">
		<td>
			<span>
 				<bean:write name="utente" property="carica" /> <strong> <span class="obbligatorio"> non attivo</span></strong>		
			</span>
    	</td>
    	</logic:equal>
		<logic:equal name="utente" property="attivo" value="true">
		<td>
			<span>
				<html:link action="/page/amministrazione/carica.do" paramId="parId" paramName="utente" paramProperty="caricaId">
 					<bean:write name="utente" property="carica" />
				</html:link >		
			</span>
    	</td>
    	</logic:equal>
    	<!--  -->
    	<td>
			<span>
				<logic:notEmpty name="utente" property="cognome">
					<bean:write name="utente" property="cognome" /> 
					<bean:write name="utente" property="nome" />
					<logic:equal name="utente" property="abilitato" value="false">
				 		<strong><span class="obbligatorio"> non abilitato</span></strong>
					</logic:equal> 
				</logic:notEmpty>
			</span>
    	</td>
  
    	<td>		
			<span>
		     	<html:multibox name="ufficioForm" property="referentiId" >
    				<bean:write name="utente" property="caricaId"/>
		    	</html:multibox> 
			</span> 
    	</td>
    	<td>		
			<html:radio property="caricaDirigenteId" idName="utente" value="caricaId"></html:radio>
    	</td>
    	<logic:equal name="ufficioForm" property="ufficioProtocollo" value="true">
    		<td>
    			<html:radio property="caricaResponsabileUfficioProtocolloId" idName="utente" value="caricaId"></html:radio>
    		</td>
    	</logic:equal>
    	<td>		
			<span>
			<logic:equal name="utente" property="attivo" value="true">
		     	<html:link action="/page/amministrazione/carica.do?btnDisattiva=true" paramId="parId" paramName="utente" paramProperty="caricaId">
 					[Disattiva]
				</html:link>
			</logic:equal>
			<logic:equal name="utente" property="attivo" value="false">
		     	<html:link action="/page/amministrazione/carica.do?btnAttiva=true" paramId="parId" paramName="utente" paramProperty="caricaId">
 					[Attiva]
				</html:link>
			</logic:equal>
			</span> 
    	</td>
	</tr>

	</logic:iterate>
	</logic:notEmpty>

</table>

</logic:notEmpty>

<logic:empty name="ufficioForm" property="dipendentiUfficio" >
	<p><span><strong><bean:message key="amministrazione.organizzazione.uffici.messaggio"/>.</strong></span></p>
</logic:empty>
<p>
	<html:submit styleClass="submit" property="btnCaricaAction" value="Nuova carica" title="Inserisce la nuova carica"></html:submit>
</p>
</div>
  		<logic:notEmpty name="ufficioForm" property="vociTitolario" >
			<div class="sezione">
				<span class="title">
					<strong>Voci di titolario</strong>
				</span>
				<br />
			<logic:iterate id="titolario" name="ufficioForm" property="vociTitolario" >
					<bean:write name="titolario" property="path" />
				<br />
			</logic:iterate>
			</div>
			</logic:notEmpty>	
</logic:greaterThan>
<table>
<tr>
		<td></td>
		<td>		
			<html:submit styleClass="submit" property="btnSalva" value="Salva" title="Inserisce il nuovo ufficio"></html:submit>
			<html:submit styleClass="button" property="btnAnnulla" value="Annulla" title="Annulla l'operazione"></html:submit>
		</td>
</tr>	
</table>

	<div style="display:none">
      	<div id="inline1">
      		<table>
      			<tr>
					<td class="label">
    					<label for="emailUsername">Username</label>
    				</td>  
    				<td>
      					<html:text property="emailUsername"styleId="emailUsername" size="20" maxlength="50"></html:text>
      				</td>
      			</tr>
      			<tr>
      				<td class="label">
    					<label for="emailPassword">Password</label>
    				</td>  
    				<td>
    					<html:text property="emailPassword"styleId="emailPassword" size="20" maxlength="50"></html:text>
      				</td>
      			</tr>
      		</table>
      	</div>
   </div>

</html:form>


</eprot:page>

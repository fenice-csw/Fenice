<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione utenti">
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
<html:hidden property="id"/>
<table summary="">
	<tr>  
    	<td class="label">
    		<label for="userName"><bean:message bundle="bundleMessaggiAmministrazione" key="username"/>
      		<span class="obbligatorio"> * </span></label>:
    	</td>  
    	<td>
			<html:text property="userName" styleClass="obbligatorio" styleId="userName" size="32" maxlength="32"></html:text>
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
	    	<label for="passWord"><bean:message bundle="bundleMessaggiAmministrazione" key="password"/>
      		<span class="obbligatorio"> * </span></label>:
    	</td>  
    	<td>
      		<html:password property="passWord" styleClass="obbligatorio" styleId="passWord" size="20" maxlength="20"></html:password>	
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
		    <label for="confermaPassword"><bean:message bundle="bundleMessaggiAmministrazione" key="confermaPassword"/>
      		<span class="obbligatorio"> * </span></label>:
    	</td>  
    	<td>
      		<html:password property="confermaPassword" styleClass="obbligatorio" styleId="confermaPassword" size="20" maxlength="20"></html:password>	
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
			<label for="cognome"><bean:message bundle="bundleMessaggiAmministrazione" key="cognome"/>
      		<span class="obbligatorio"> * </span></label>:
    	</td>  
    	<td>
      		<html:text property="cognome" styleClass="obbligatorio" styleId="cognome" size="60" maxlength="100"></html:text>	
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
			<label for="nome"><bean:message bundle="bundleMessaggiAmministrazione" key="nome"/>
      		<span class="obbligatorio"> * </span></label>:
    	</td>  
    	<td>
      		<html:text property="nome" styleClass="obbligatorio" styleId="nome" size="40" maxlength="40"></html:text>	
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
			<label for="email"><bean:message bundle="bundleMessaggiAmministrazione" key="email"/></label>:
    	</td>  
    	<td>
      		<html:text property="emailAddress" styleId="email" size="60" maxlength="256"></html:text>	
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
			<label for="codiceFiscale"><bean:message bundle="bundleMessaggiAmministrazione" key="codiceFiscale"/>
      		<span class="obbligatorio"> * </span></label>:
    	</td>  
    	<td>
      		<html:text property="codiceFiscale" styleClass="obbligatorio" styleId="codiceFiscale" size="16" maxlength="16"></html:text>	
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
			<label for="matricola"><bean:message bundle="bundleMessaggiAmministrazione" key="matricola"/></label>:
    	</td>  
    	<td>
      		<html:text property="matricola" styleClass="normale" styleId="matricola" size="10" maxlength="10"></html:text>	
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
			<label for="dataFineAttivita"><bean:message bundle="bundleMessaggiAmministrazione" key="dataFineAttivita"/></label>:
    	</td>  
    	<td>
      		<html:text styleClass="normale" property="dataFineAttivita" styleId="dataFineAttivita" size="10" maxlength="10"></html:text>
    	</td>  
	</tr>
	 
	<tr>
    	<td class="label">
      		<label for="abilitato"><bean:message bundle="bundleMessaggiAmministrazione" key="abilitato"/></label>: 
    	</td>  
		<td>
			<html:radio property="abilitato" value="true" styleId="abilitatoSI">
				<label for="abilitatoSI">Si</label>
			</html:radio>
			<html:radio property="abilitato" value="false" styleId="abilitatoNo">
				<label for="abilitatoNo">No</label>
			</html:radio>
		</td>
  </tr>
   
<logic:notEmpty name="profiloUtenteForm" property="registri" >
	<tr>
    	<td class="label">
			<label for="registriSelezionatiId"><bean:message bundle="bundleMessaggiAmministrazione" key="registro"/></label>
			<span class="obbligatorio"> * </span>:
    	</td>  
		<td>
			<span>
			
			<logic:iterate id="ass" name="profiloUtenteForm" property="registri" >
			
			    <html:multibox property="registriSelezionatiId">
			    	<bean:write name="ass" property="id" />
			    </html:multibox>
				<bean:write name="ass" property="descrizioneRegistro" />
				<br />
			</logic:iterate>
			
			</span>
		</td>
	</tr>
</logic:notEmpty>	

<!-- 
<logic:equal name="profiloUtenteForm" property="id" value="0" >
  <tr>
    <td class="label">
      <label for="ufficioSelezionatoId">Profilo:</label>
    </td>
    <td>
 	    <html:select property="profiloId">
			<html:optionsCollection property="profiliMenu" value="id" label="descrizioneProfilo" />
 	    </html:select>
    </td>
  </tr>
</logic:equal>  
 -->

<logic:greaterThan name="profiloUtenteForm" property="id" value="0">
	<tr>
	    <td class="label">
	      <label for="ufficioSelezionatoId">Uffici di competenza:</label>
	    </td>
		
		<td>
			<span>		
			<logic:notEmpty name="profiloUtenteForm" property="ufficiAssegnati" >
			<logic:iterate id="ufficio" name="profiloUtenteForm" property="ufficiAssegnati" > 
				<html:link action="/page/amministrazione/organizzazione/ufficio.do?indietroCaricaAction=true" paramId="ufficioId" paramName="ufficio" paramProperty="valueObject.id">
 					<bean:write name="ufficio" property="path" />
				</html:link >
				<br />
			</logic:iterate>
			</logic:notEmpty>		
			</span>
		</td>
		
	</tr>
</logic:greaterThan>
<!-- 	
	<tr>
		<td colspan="2">		
			<div class="sezione">
				<jsp:include page="/WEB-INF/subpages/amministrazione/organizzazione/utenti/ufficiassegnati.jsp" />
			</div>
		</td>
	</tr>
 -->
	<tr>
		<td></td>
	</tr>
	<tr>
		<td colspan="2">		
			<html:submit styleClass="submit" property="btnSalva" value="Salva" title="Inserisce il nuovo utente"></html:submit>
				<logic:notEmpty name="profiloUtenteForm" property="ufficiAssegnati" >
					<html:submit styleClass="submit" property="btnStoriaCariche" value="Storico cariche" title="Storiche della cariche ricoperte"></html:submit>
				</logic:notEmpty>
			<%-- 
			<logic:greaterThan name="profiloUtenteForm" property="id" value="0">
				<html:submit styleClass="submit" property="btnCancella" value="Cancella" title="Cancella l'utente"></html:submit>
			</logic:greaterThan>
			--%>
			<html:submit styleClass="button" property="btnIndietro" value="Annulla" title="Annulla l'operazione"></html:submit>
		</td>
	</tr>	
</table>
</html:form>
</eprot:page>
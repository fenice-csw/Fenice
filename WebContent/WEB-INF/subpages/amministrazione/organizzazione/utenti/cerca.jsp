<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>


<html:xhtml />
<table>
  <tr>  
    <tr>  
    	<td class="label">
	    	<label for="username">
      		<bean:message bundle="bundleMessaggiAmministrazione" key="username"/>:
      		</label>
    	</td>  
    	<td>
      		<html:text property="username" styleId="username" size="32" maxlength="32"></html:text>
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
    		<label for="cognome">
      		<bean:message bundle="bundleMessaggiAmministrazione" key="cognome"/>:
			</label>      		
    	</td>  
    	<td>
      		<html:text property="cognome" styleId="cognome" size="60" maxlength="100"></html:text>	
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
	    	<label for="nome">
      		<bean:message bundle="bundleMessaggiAmministrazione" key="nome"/>:
			</label>      		
    	</td>  
    	<td>
      		<html:text property="nome" styleId="nome" size="40" maxlength="40"></html:text>	
    	</td>  
	</tr>
   <tr>
		<td></td>   
	    <td align="right">
		    <html:submit styleClass="submit" property="btnCerca" value="Cerca" alt="Cerca utenti"/>
			<html:submit styleClass="submit" property="btnNuovo" value="Nuovo" alt="Inserisce un nuovo Utente"/>
		    <html:submit styleClass="button" property="btnAnnulla" value="Annulla" alt="Annulla l'operazione"/>
	    </td>  
   </tr>
</table>

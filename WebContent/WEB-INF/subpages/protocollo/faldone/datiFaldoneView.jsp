<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<div class="sezione">
<span class="title"><strong><bean:message key="protocollo.faldone"/></strong></span>
<table summary="" width="100%">
  	<logic:greaterThan name="faldoneForm" property="faldoneId" value="0">
	  	<tr>
	    	<td class="label" width="25%">
	      		<span><bean:message key="protocollo.faldone.numero_faldone"/></span>&nbsp;:
	    	</td>
	    	<td>
		    	<span><strong>
		    	<bean:write name="faldoneForm" property="numeroFaldone" />
		    	</strong></span>
	    	</td>
	  	</tr>
  	</logic:greaterThan>
  	<tr>
    	<td class="label">
      		<span><bean:message key="protocollo.faldone.ufficio"/></span>&nbsp;:
    	</td>
    	<td>
	    	<span><strong>
	    	<bean:write name="faldoneForm" property="ufficioCorrentePath" />
	    	</strong></span>
    	</td>
  	</tr>
  	<tr>
    	<td class="label">
      		<span><bean:message key="protocollo.faldone.creato_da"/></span>&nbsp;:
    	</td>
    	<td>
	    	<span><strong>
	    	<bean:write name="faldoneForm" property="rowCreatedUser" />
	    	</strong></span>
    	</td>
  	</tr>
  	
	<logic:notEmpty name="faldoneForm" property="titolario" > 
        <tr> 
            <td class="label"> 
              <span><bean:message key="protocollo.titolario.argomento"/></span>&nbsp;: 
            </td> 
            <td> 
				<span><strong><bean:write name="faldoneForm" property="titolario.descrizione" /></strong></span> 
            </td> 
        </tr> 
	</logic:notEmpty>
  	<tr>   
		<td class="label">
	  		<span><bean:message key="protocollo.faldone.sotto_categoria"/>: </span>
		</td>
		<td>
			<span><strong>
				<bean:write name="faldoneForm" property="sottoCategoria" />
			</strong></span>
		</td> 
  	</tr>
  	<tr>
		<td class="label">
	      <span><bean:message key="protocollo.faldone.procedimento.oggetto"/>: </span>
		</td>
		<td>
	    	<span><strong>
	    		<bean:write name="faldoneForm" property="oggetto" />
	    	</strong></span>
		</td> 
  	</tr>      
  	<tr>  
	    <td class="label">
	      <span><bean:message key="protocollo.faldone.codice_locale"/>: </span>
	    </td>
	    <td>
	    	<span><strong>
		    	<bean:write name="faldoneForm" property="codiceLocale" />
		    </strong></span>	
	    </td> 
	</tr>
		<logic:notEmpty name="faldoneForm" property="collocazioneValore1">	
			<tr>	 
				<td class="label">
	   			<label title="Collocazione" for="collocazioneValore1">
	   				<bean:write name="faldoneForm" property="collocazioneLabel1" />
	   			</label>:
				</td>  
				<td>
					<span><strong><bean:write name="faldoneForm" property="collocazioneValore1" /></strong></span>
				</td>
			</tr>			
   		</logic:notEmpty>
		<logic:notEmpty name="faldoneForm" property="collocazioneValore2">	
			<tr>	 
				<td class="label">
	   			<label title="Collocazione" for="collocazioneValore2">
	   				<bean:write name="faldoneForm" property="collocazioneLabel2" />
	   			</label>:
				</td>  
				<td>
					<span><strong><bean:write name="faldoneForm" property="collocazioneValore2" /></strong></span>
				</td>
			</tr>			
   		</logic:notEmpty>
		<logic:notEmpty name="faldoneForm" property="collocazioneValore3">	
			<tr>	 
				<td class="label">
	   			<label title="Collocazione" for="collocazioneValore3">
	   				<bean:write name="faldoneForm" property="collocazioneLabel3" />
	   			</label>:
				</td>  
				<td>
					<span><strong><bean:write name="faldoneForm" property="collocazioneValore3" /></strong></span>
				</td>
			</tr>			
   		</logic:notEmpty>
		<logic:notEmpty name="faldoneForm" property="collocazioneValore4">	
			<tr>	 
				<td class="label">
	   			<label title="Collocazione" for="collocazioneValore4">
	   				<bean:write name="faldoneForm" property="collocazioneLabel4" />
	   			</label>:
				</td>  
				<td>
					<span><strong><bean:write name="faldoneForm" property="collocazioneValore4" /></strong></span>
				</td>
			</tr>			
   		</logic:notEmpty>

	<tr>
	    <td class="label">
	      <span><bean:message key="protocollo.faldone.nota"/>: </span>
	    </td>
	    <td>
	    	<span><strong>
		    	<bean:write name="faldoneForm" property="nota" filter="false" />
		    </strong></span>	
	    </td> 
	</tr>
	<tr>
		<td class="label">
			<label for="stato"><bean:message key="protocollo.faldone.stato"/></label>:
		</td>
	    <td>
			<span>
				<strong><bean:write name="faldoneForm" property="statoFaldone"  /></strong>
			</span>
	    </td>
	</tr> 
    <logic:greaterThan name="faldoneForm" property="faldoneId" value="0">
		<tr>
		    <td class="label">
		      <span><bean:message key="faldone.datacreazione"/>: </span>
		    </td>
		    <td>
		    	<span><strong><bean:write name="faldoneForm" property="dataCreazione" /></strong></span>	
		    </td> 
		</tr> 
		<tr>
			<td class="label">
				<span><bean:message key="protocollo.faldone.datacarico"/>: </span>
		    </td>
		    <td>
				<span><strong><bean:write name="faldoneForm" property="dataCarico" /></strong></span>	
		      	<span><bean:message key="protocollo.faldone.datascarico"/>: </span>
		    	<span><strong><bean:write name="faldoneForm" property="dataScarico" /></strong></span>	
		    </td> 
		</tr> 
		<tr>
			<td class="label">
				<span><bean:message key="protocollo.faldone.dataevidenza"/>: </span>
			</td>
		    <td>
				<span><strong><bean:write name="faldoneForm" property="dataEvidenza" /></strong></span>	
		      	<span><bean:message key="protocollo.faldone.dataultimomovimento"/>: </span>
		    	<span><strong><bean:write name="faldoneForm" property="dataMovimento" /></strong></span>	
		    </td> 
		</tr> 
		<logic:greaterThan name="faldoneForm" property="versione" value="0">
			<tr>
			    <td class="label">
			      <span><bean:message key="protocollo.faldone.modificatoDa"/>: </span>
			    </td>
			    <td>
					<span><strong><bean:write name="faldoneForm" property="rowUpdatedUser" /></strong></span>	
			    </td> 
			</tr> 		
		</logic:greaterThan>
	</logic:greaterThan>
	<logic:equal name="faldoneForm" property="versioneDefault" value="true">
		<tr>
		    <td colspan="2">
		    <div class="sezione">
			  <span class="title"><strong><bean:message key="protocollo.faldone.fascicoli"/></strong></span>
		      <jsp:include page="/WEB-INF/subpages/protocollo/faldone/listaFascicoliView.jsp" />
		    </div>
		    </td> 
		</tr> 
		<tr>
		    <td colspan="2">
		      <div class="sezione">
				<span class="title"><strong><bean:message key="protocollo.faldone.procedimenti"/></strong></span>
		      	<jsp:include page="/WEB-INF/subpages/protocollo/faldone/listaProcedimentiView.jsp" />
		      </div>
		    </td> 
		</tr> 
	</logic:equal>
</table>
</div>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<table summary="">
<tr>
   <td>
   		<html:radio property="fascicoliProcedimenti" styleId="fascicoli" value="F">
			<label for="fascicoli"><bean:message key="fascicolo.fascicoli"/></label>
		</html:radio>
   		<html:radio property="fascicoliProcedimenti" styleId="procedimenti" value="P">
			<label for="procedimenti"><bean:message key="protocollo.faldone.procedimenti"/></label>
   		</html:radio>
    </td>
  </tr>

<tr>
   <td>
   		<span><bean:message key="protocollo.ricerca.assegnatario.ufficioCorrente"/>:</span>
   
		<html:select property="ufficioRicercaId">
			<logic:equal name="ricercaEvidenzaForm" property="tuttiUffici" value="true">
	    	    <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
	        </logic:equal>
	        <option value='<bean:write name="ricercaEvidenzaForm" property="ufficioCorrenteId" />'>
				<bean:write name="ricercaEvidenzaForm" property="ufficioCorrente.description" />
			</option>
	 	</html:select>
      	<html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
    </td>
  </tr>
 
<logic:notEmpty name="ricercaEvidenzaForm" property="ufficiDipendenti">
	<tr>  
	    <td>
	       <label for="ufficioSelezionatoId"><bean:message key="protocollo.ricerca.assegnatario.ufficio"/> :</label>
	   
	 	    <html:select property="ufficioSelezionatoId">
		        <option value="0"><bean:message key="protocollo.ricerca.assegnatario.tutti"/></option>
		        <bean:define id="ufficiDipendenti" name="ricercaEvidenzaForm" property="ufficiDipendenti"/>
				<html:optionsCollection name="ufficiDipendenti" value="id" label="description" />
	 	    </html:select>
	      <html:submit styleClass="button" property="impostaUfficioAction" value="Vai" title="Imposta l'ufficio come corrente" />
	    </td>
	 </tr> 
</logic:notEmpty> 
<tr>
   <td> 
  <table summary="">
			<jsp:include page="/WEB-INF/subpages/evidenza/titolario.jsp" />
</table>
 </td>
  </tr>
	<tr>
    <td class="subtable" colspan="2">
      <table summary="">
        <tr>
          <td>
			<span><bean:message key="evidenza.data"/></span>
            <label title="Data apertura minima" for="dataEvidenzaDa"><bean:message key="protocollo.da"/></label>:
          </td>
          <td>
			<html:text property="dataEvidenzaDa" styleId="dataEvidenzaDa" size="10" maxlength="10" />
              <eprot:calendar textField="dataEvidenzaDa" />
          </td>
          <td>&nbsp;</td>
          <td>
            <label title="Data apertura massima" for="dataEvidenzaA"><bean:message key="protocollo.a"/></label>:
          </td>
          <td>
            <html:text property="dataEvidenzaA" styleId="dataEvidenzaA" size="10" maxlength="10" />
          </td>
          <td>
          <eprot:calendar textField="dataEvidenzaA" />
		</td>
		 <td>
			<html:submit styleClass="submit" property="btnCercaEvidenze" value="Cerca" title="Cerca Fascicoli"/>     
			<html:submit styleClass="submit" property="annullaAction" value="Annulla" alt="Azzera i campi della ricerca" />
		
          </td>
        </tr>
      	</table>

          	</td>
	</tr>

</table>


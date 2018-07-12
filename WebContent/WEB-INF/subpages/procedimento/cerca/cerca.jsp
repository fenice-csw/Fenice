<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>


<html:xhtml />
<table >
	<tr> <td colspan="4">
	</td>
		</tr>   
	<tr>  
	 <td colspan="4">
	 <table >
	 <tr>
	    <td>
	  
			<label for="anno"><bean:message key="procedimento.annoprocedimento"/> &nbsp;:</label>
		</td>
		<td colspan="1">
			<html:text name="ricercaProcedimentoForm" property="anno" styleId="anno" size="10" maxlength="4"/>
	    </td>
	 
	    <td>&nbsp;
			<label for="numero"><bean:message key="procedimento.numeroProcedimento"/> &nbsp;:</label>
		</td>
		<td colspan="1">
		<html:text name="ricercaProcedimentoForm" property="numero" styleId="numero" size="10" maxlength="6"/>
	    </td>
	    </tr>
	    <tr>
  		<td>
	    	<label for="dataAvvioInizio">
				<bean:message key="procedimento.data.avvioda"/> &nbsp;:
			</label>
		</td>
		<td colspan="1">
			<html:text name="ricercaProcedimentoForm" property="dataAvvioInizio" styleId="dataAvvioInizio" size="10" maxlength="10" />
			<eprot:calendar textField="dataAvvioInizio" />
		</td>
	  		<td>
	    	<label for="dataAvvioFine">&nbsp;
				<bean:message key="procedimento.data.avvioa"/> &nbsp;:
			</label>
		</td>
		<td colspan="1">
			<html:text name="ricercaProcedimentoForm" property="dataAvvioFine" styleId="dataAvvioFine" size="10" maxlength="10" />
			<eprot:calendar textField="dataAvvioFine" />
    	</td> 
	</tr>
	<%-- DATA SCADENZA --%>  
	<tr>
  		<td>
	    	<label for="dataScadenzaInizio">
				<bean:message key="procedimento.data.scadenzada"/> &nbsp;:
			</label>
		</td>
		<td colspan="1">
			<html:text name="ricercaProcedimentoForm" property="dataScadenzaInizio" styleId="dataScadenzaInizio" size="10" maxlength="10" />
			<eprot:calendar textField="dataScadenzaInizio" />
		</td>
	  		<td>
	    	<label for="dataScadenzaFine">&nbsp;
				<bean:message key="procedimento.data.scadenzaa"/> &nbsp;:
			</label>
		</td>
		<td colspan="1">
			<html:text name="ricercaProcedimentoForm" property="dataScadenzaFine" styleId="dataScadenzaFine" size="10" maxlength="10" />
			<eprot:calendar textField="dataScadenzaFine" />
    	</td> 
	</tr>
	    </table>
	     </td>
	</tr> 
	<%-- 
		<jsp:include page="/WEB-INF/subpages/procedimento/cerca/uffici.jsp"/>
	--%>
	<tr>
		<td>
			Responsabile:
		</td>
		<td>
			<logic:notEmpty name="ricercaProcedimentoForm" property="utenti">
				<html:select property="utenteSelezionatoId" styleClass="evidenziato" >
					<html:option value=""></html:option>
					<html:optionsCollection property="utenti" value="id" label="fullName" />
				</html:select>
			</logic:notEmpty>  
			<logic:empty name="ricercaProcedimentoForm" property="utenti">
				<html:hidden property="utenteSelezionatoId" value="0"/>
			</logic:empty>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="procedimento.tipo_procedimento" />:
		</td>
		<td>
			<logic:notEmpty name="ricercaProcedimentoForm" property="tipiProcedimento">
				<html:select property="tipoProcedimentoSelezionatoId" styleClass="evidenziato" >
					<html:option value=""></html:option>
					<html:optionsCollection property="tipiProcedimento" value="idTipo" label="descrizione" />
				</html:select>
			</logic:notEmpty>  
			<logic:empty name="ricercaProcedimentoForm" property="tipiProcedimento">
				<html:hidden property="tipoProcedimentoSelezionatoId" value="0"/>
			</logic:empty>
		</td>
	</tr>
	<tr>
		<td>Interessato/Delegato: </td>
		<td colspan="2">
		<html:text
			property="descrizioneInteressatoDelegato" size="30" maxlength="100" disabled="true"/>
		</td>
	</tr>
	
	
 	<tr>
	    <td >
	      <label for="oggettoProcedimento"><bean:message key="procedimento.oggetto"/></label>&nbsp;:
	      <br/>
				<span><html:link action="/page/unicode.do?campo=oggettoProcedimento" target="_blank" ><bean:message key="procedimento.segni"/></html:link></span>
	    </td>
		<td colspan="2">
	      <html:text name="ricercaProcedimentoForm" property="oggettoProcedimento" styleId="oggettoProcedimento" size="50" maxlength="255" />
	    </td>
	</tr>
	<tr>
		<td><label for="note"> <bean:message
			key="procedimento.note" />&nbsp;: </label>
			<br/>
				<span><html:link action="/page/unicode.do?campo=note" target="_blank" ><bean:message key="procedimento.segni"/></html:link></span></td>
		<td colspan="2"><html:textarea name="ricercaProcedimentoForm" property="note" styleId="note"
			rows="1" cols="50" /></td>
	</tr>
	
	<tr>
 		<td colspan="3">
		<br>
     		<html:submit styleClass="submit" property="btnCerca" value="Cerca" alt="Cerca"/>
				<html:reset styleClass="submit"	property="ResetAction" value="Annulla" alt="Annulla i parametri di ricerca" />
		 <logic:equal name="ricercaProcedimentoForm" property="indietroVisibile" value="true" >
		<html:submit styleClass ="submit" property="btnAnnulla" value="Indietro" alt="Indietro" />
	</logic:equal>	    
		</td>  
 	</tr> 
 
</table>	

  


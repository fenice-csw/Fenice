<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Report - Protocolli da scartare">


<br />

<%--<div>-->
<!--	<bean:message key="campo.obbligatorio.msg" />-->
<!--</div>--%>

<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>

<html:form action="/page/amministrazione/report/protocolliDaScartare.do">
<div class="sezione">
  <span class="title"><strong><bean:message key="amministrazione.report.protocollidascartare"/></strong></span>
<table>
	<html:hidden property="aooId" />
	<tr>
    <td class="label">
      <span><bean:message key="report.prnpro.ufficiocorrente"/>: <html:hidden property="ufficioCorrenteId" /></span>
    </td>
    <td>
	  <bean:define id="ufficioCorrentePath" name="protocolloDaScartareForm" property="ufficioCorrentePath" />
		<span title="<bean:write name="protocolloDaScartareForm" property="ufficioCorrentePath"/>">
      	<strong>
			<bean:write name="protocolloDaScartareForm" property="ufficioCorrente.description" />
      	</strong>
      	</span>
      <html:submit styleClass="button" property="ufficioPrecedenteAction" value=".." title="Vai al livello superiore" />
    </td>
  </tr>
<logic:equal name="protocolloDaScartareForm" property="ufficioSelezionatoId" value="0">
		<tr>
		    <td class="label">
		    	<label for="ufficioSelezionatoId"><bean:message key="amministrazione.report.ufficio"/> <span class="obbligatorio"> * </span>:</label>
		    </td>
		    <td>
				<html:select name="protocolloDaScartareForm" property="ufficioSelezionatoId" styleClass="obbligatorio">
					<logic:iterate id="ufficio" name="protocolloDaScartareForm" property="uffici">
				        <option value='<bean:write name="ufficio" property="id"/>'>
				        	<bean:write name="ufficio" property="description"/>
				        </option>
					</logic:iterate>
	 	    	</html:select>
 	    	</td>
		    <td>
		    	<html:submit styleClass="button" property="impostaUfficioAction" value="Seleziona" title="Imposta l'ufficio come corrente" />
		    </td>
		</tr>
	</logic:equal>
	<logic:greaterThan name="protocolloDaScartareForm" property="ufficioSelezionatoId" value="0">
		<html:hidden property="ufficioSelezionatoId" />
		<tr>
		    <td class="label">
		      <span>Ufficio <span class="obbligatorio"> * </span>: </span>
		    </td>
		    <td>
				<span><bean:write name="protocolloDaScartareForm" property="ufficioSelezionato" /></span>
			</td>
		    <td>
				<html:submit styleClass="button" property="cambiaUfficioAction" value="Cambia" title="Cambia l'ufficio" />
		    </td>
		</tr>
		<logic:equal name="protocolloDaScartareForm" property="servizioSelezionatoId" value="0">
			<logic:notEmpty name="protocolloDaScartareForm" property="servizi">
				<tr>  
				    <td class="label">
				      <label for="servizioSelezionatoId"><bean:message key="amministrazione.report.servizio"/>Servizio <span class="obbligatorio"> * </span> :</label>
				    </td>
				    <td>
				    <html:select name="protocolloDaScartareForm" property="servizioSelezionatoId" styleClass="obbligatorio">
						<logic:iterate id="servizio" name="protocolloDaScartareForm" property="servizi">
				        <option value='<bean:write name="servizio" property="id"/>'>
				        <bean:write name="servizio" property="codice"/>
				        </option>
						</logic:iterate>
 	    			</html:select>
 	    			</td>
				</tr>
			</logic:notEmpty>
		</logic:equal>
		<logic:greaterThan name="protocolloDaScartareForm" property="servizioSelezionatoId" value="0">
			<html:hidden property="servizioSelezionatoId" />
			<tr>
			    <td class="label">
			      <span>Servizio <span class="obbligatorio"> * </span> : </span>
			    </td>
			    <td>
					<span><bean:write name="protocolloDaScartareForm" property="servizioSelezionato" /></span>
				</td>
		    	<td>
					<html:submit styleClass="button" property="cambiaServizioAction" value="Cambia" title="Cambia servizio" />
			    </td>
			</tr>
		</logic:greaterThan>  <!--chiude servizio selezionato-->
	</logic:greaterThan>	 <!--chiude ufficio selezionato-->
	<tr>
    
    <td class="subtable">
      
        <tr>
          <td class="label"><span><bean:message key="protocollo.dataregistrazione"/>&nbsp;</span>
            <label title="Data registrazione minima" for="dataRegistrazioneDa"><bean:message key="protocollo.da"/></label>&nbsp;:
          </td>
          <td>
            <html:text property="dataRegistrazioneDa" styleId="dataRegistrazioneDa" size="10" maxlength="10" />
              <eprot:calendar textField="dataRegistrazioneDa" />
          </td>
          <td>
            <label title="Data registrazione massima" for="dataRegistrazioneA"><bean:message key="protocollo.a"/></label>&nbsp;:
            <html:text property="dataRegistrazioneA" styleId="dataRegistrazioneA" size="10" maxlength="10" />
          	  <eprot:calendar textField="dataRegistrazioneA" />
    	</td>
  	</tr>
	</table>
</div>

<div>
	<html:submit styleClass="submit" property="cercaAction" value="Cerca" alt="Cerca" />
	<html:reset styleClass="submit" property="resetAction" value="Pulisci" alt="Pulisci" />		
</div>
<br/>
<br class="hidden" />

   <jsp:include page="/WEB-INF/subpages/amministrazione/report/listaProtocolliDaScartare.jsp" />

	
</html:form>

</eprot:page>
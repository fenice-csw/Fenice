<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
  
  <tr>
    <td class="label">
     <logic:equal name="fascicoloForm" property="dipendenzaTitolarioUfficio" value="1">
      <span><bean:message key="fascicolo.livelli.titolario" /></span><span class="obbligatorio">*</span>:
    </logic:equal>
      <logic:equal name="fascicoloForm" property="dipendenzaTitolarioUfficio" value="0">
      <span><bean:message key="fascicolo.livelli.titolario" /></span><span class="obbligatorio"> * </span>:
     </logic:equal>   
    </td>
    
    <td colspan="2">
    
      <span><strong>
      	<logic:empty name="fascicoloForm" property="titolario">
      		<logic:empty name="fascicoloForm" property="titolariFigli">
      			Nessuna Classificazione associata all'ufficio in uso
      		</logic:empty>
      	</logic:empty>
		<logic:notEmpty name="fascicoloForm" property="titolario">
		        <bean:write name="fascicoloForm" property="titolario.descrizione"/>
		</logic:notEmpty>        
      </strong></span>
       <html:hidden property="titolarioPrecedenteId" />
      	<logic:notEmpty name="fascicoloForm" property="titolario">
	      	<logic:empty name="fascicoloForm" property="padre">
			      <html:submit styleClass="button" property="titolarioPrecedenteAction" value=".." title="Imposta il titolario precedente come corrente" />
			</logic:empty>
		</logic:notEmpty> 
    </td>
  </tr>
  <logic:notEmpty name="fascicoloForm" property="titolariFigli">
  	<logic:empty name="fascicoloForm" property="padre">
  <tr>
    <td class="label">
&nbsp;
    </td>
    <logic:equal name="fascicoloForm" property="dipendenzaTitolarioUfficio" value="1">
    <td>
      <html:select property="titolarioSelezionatoId" styleClass="obbligatorio">
		<logic:iterate id="tit" name="fascicoloForm" property="titolariFigli">
			<bean:define id="id" name="tit" property="id"/>
        	<option value="<bean:write name="tit" property="id"/>">
        		<bean:write name="tit" property="path" /> - <bean:write name="tit" property="descrizione"/>
        	</option>
		</logic:iterate>	
      </html:select>
&nbsp;
      		<html:submit styleClass="button" property="impostaTitolarioAction" value="Imposta" title="Imposta il titolario selezionato come corrente" />
    </td>
    </logic:equal>  
      <logic:equal name="fascicoloForm" property="dipendenzaTitolarioUfficio" value="0">
    <td>
     <html:select property="titolarioSelezionatoId" >
    <logic:iterate id="tit" name="fascicoloForm" property="titolariFigli">
			<bean:define id="id1" name="tit" property="id"/>
        	<option value="<bean:write name="tit" property="id"/>">
        		<bean:write name="tit" property="path" /> - <bean:write name="tit" property="descrizione"/>
        	</option>
		</logic:iterate>	
      </html:select>
&nbsp;
      		<html:submit styleClass="button" property="impostaTitolarioAction" value="Imposta" title="Imposta il titolario selezionato come corrente" />
    </td>
     </logic:equal>  
  </tr>
	</logic:empty>
</logic:notEmpty>  

<%-- 
<logic:notEmpty name="fascicoloForm" property="titolario">
	<jsp:include page="/WEB-INF/subpages/fascicolo/proprietaTitolario.jsp" />
</logic:notEmpty>
--%>

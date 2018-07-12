<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
  
	<p>
      <span>
      	<logic:empty name="tipoProcedimentoForm" property="titolario">
      		<logic:empty name="tipoProcedimentoForm" property="titolariFigli">
      		<strong>
      			Nessuna Classificazione associata all'ufficio in uso
      		</strong>
      		</logic:empty>
      	</logic:empty>
		<logic:notEmpty name="tipoProcedimentoForm" property="titolario">
		        Selezionato: 
		        <strong>
		        	<bean:write name="tipoProcedimentoForm" property="titolario.descrizione"/>
				</strong>
		</logic:notEmpty>        
      </span>
       <html:hidden property="titolarioPrecedenteId" />
      	<logic:notEmpty name="tipoProcedimentoForm" property="titolario">
			<html:submit styleClass="button" property="titolarioPrecedenteAction" value=".." title="Imposta il titolario precedente come corrente" />
		</logic:notEmpty>
		<p>
		<logic:notEmpty name="tipoProcedimentoForm" property="titolariFigli">
		      <html:select property="titolarioSelezionatoId" styleClass="obbligatorio">
				<logic:iterate id="tit" name="tipoProcedimentoForm" property="titolariFigli">
					<bean:define id="id" name="tit" property="id"/>
		        	<option value="<bean:write name="tit" property="id"/>">
		        		<bean:write name="tit" property="codice" /> - <bean:write name="tit" property="descrizione"/>
		        	</option>
				</logic:iterate>	
		      </html:select>
				&nbsp;
		      	<html:submit styleClass="button" property="impostaTitolarioAction" value="Imposta" title="Imposta il titolario selezionato come corrente" />
		</logic:notEmpty>
		</p>
</p>
    



<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml />
<tr>
	<td class="label">
		<span><bean:message key="fascicolo.livelli.titolario" />:</span>
	</td>
	
		<td><span><strong>
		<logic:empty name="ricercaFascicoliForm" property="titolario">
      		<logic:empty name="ricercaFascicoliForm" property="titolariFigli">
      			Nessuna Classificazione associata all'ufficio in uso
      		</logic:empty>
      	</logic:empty>
		 
		<logic:notEmpty name="ricercaFascicoliForm" property="titolario">
			<bean:write name="ricercaFascicoliForm"
				property="titolario.descrizione" />
		</logic:notEmpty> </strong> <html:hidden
			property="titolarioPrecedenteId" /> <logic:notEmpty
			name="ricercaFascicoliForm" property="titolario">
			<html:submit styleClass="button" property="titolarioPrecedenteAction"
				value=".." title="Imposta il titolario precedente come corrente" />
		</logic:notEmpty> </span></td>
	

</tr>
<logic:notEmpty name="ricercaFascicoliForm" property="titolariFigli">
	<tr>
		<td class="label">&nbsp;</td>
		<td><html:select property="titolarioSelezionatoId" >
			<logic:iterate id="tit" name="ricercaFascicoliForm"
				property="titolariFigli">
				<%-- 
				<bean:define id="id" name="tit" property="id" />
				<option value="<bean:write name="tit" property="id"/>"><bean:write
					name="tit" property="codice" /> - <bean:write name="tit"
					property="descrizione" /></option>
					--%>
					<bean:define id="id" name="tit" property="id" />
					<option value="<bean:write name="tit" property="id"/>"><bean:write
					name="tit" property="path" /> - <bean:write name="tit"
					property="descrizione" /></option>
					
			</logic:iterate>
		</html:select> &nbsp; <html:submit styleClass="button"
			property="impostaTitolarioAction" value="Imposta"
			title="Imposta il titolario selezionato come corrente" /></td>
	</tr>
</logic:notEmpty>

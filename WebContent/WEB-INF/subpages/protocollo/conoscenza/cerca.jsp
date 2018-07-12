<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>
<br />
<table style="display:none;">
  <tr>  
    <td align="left" colspan="2">
    	<span><bean:message key="protocollo.data"/></span>
    	<label for="dataRegistrazioneDa">
			<bean:message key="protocollo.da"/>
		</label>
		<html:text property="dataRegistrazioneDa" styleId="dataRegistrazioneDa" size="10" maxlength="10" />
		<eprot:calendar textField="dataRegistrazioneDa" />
		&nbsp;&nbsp;

    	<label for="dataRegistrazioneA">
			<bean:message key="protocollo.a"/>
		</label>
		<html:text property="dataRegistrazioneA" styleId="dataRegistrazioneA" size="10" maxlength="10" />
		<eprot:calendar textField="dataRegistrazioneA" />
		&nbsp;&nbsp;
    	<label for="statoProtocollo" title="Stato in cui si trova il protocollo">
			<bean:message key="protocollo.stato"/>		
		</label>
		<html:select property="statoProtocollo">
			<html:optionsCollection name="statiAssegnazioneProtocollo" value="codice" label="descrizione" />
		</html:select>

    	</td>   
	</tr>
  	<tr>
		<td align="left">
			<span><bean:message key="protocollo.numero_anno"/></span>
			<label for="numeroProtocolloDa">
				<bean:message key="protocollo.da"/>
			</label>
			<html:text property="numeroProtocolloDa" styleId="numeroProtocolloDa" size="10" maxlength="10" />&nbsp;/&nbsp;<html:text property="annoProtocolloDa" size="4" maxlength="4" />
			&nbsp;&nbsp;
			<label for="numeroProtocolloA">			
				<bean:message key="protocollo.a"/>
			</label>
			<html:text property="numeroProtocolloA" styleId="numeroProtocolloA" size="10" maxlength="10" />&nbsp;/&nbsp;<html:text property="annoProtocolloA" size="4" maxlength="4" />
		</td>   
	    <td align="right">
		    <html:submit styleClass="submit" property="btnCerca" value="Cerca" alt="Cerca protocolli da scaricare"/>&nbsp;&nbsp;
	    </td>  
   </tr>
</table>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Tipi documento">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>

<html:form action="page/amministrazione/tipidocumento.do">
<table summary="">
	<tr>  
		<td class="label">
			<label for="descrizione"><bean:message bundle="bundleMessaggiAmministrazione" key="descrizione"/></label>
			<span class="obbligatorio"> * </span>:
		</td>  
		
		<td>
			<html:hidden property="id"/>
			<html:text styleClass="obbligatorio" property="descrizione" styleId="descrizione" size="60" maxlength="254"></html:text>
		</td>  
	</tr>
	<tr>  

    	<td class="label">
      		<label for="descrizioneDoc"><bean:message key="amministrazione.tipodocumento.tipodocumento"/></label>:
	   	</td>  
	   	<td>
		<html:select property="flagDefault">
				<html:optionsCollection property="tipiDefault" value="codice" label="description" />
		</html:select>	
    	</td>  
	</tr>

	<tr>
		<td></td>
		<td>		
			<logic:greaterThan value="0" name="tipoDocumentoForm" property="id">
				<html:submit styleClass="submit" property="btnConferma" value="Modifica" title="Modifica i dati del tipo documento" />
				<html:submit styleClass="submit" property="btnCancella" value="Cancella" title="Cancella il tipo documento"/>
			</logic:greaterThan>
			<logic:equal value="0" name="tipoDocumentoForm" property="id">
				<html:submit styleClass="submit" property="btnConferma" value="Salva" title="Inserisce il nuovo tipo documento" />
			</logic:equal>
			<html:submit styleClass="submit" property="btnAnnulla" value="Annulla" title="Annulla l'operazione" />
		</td>
	</tr>
	
</table>
</html:form>
</eprot:page>

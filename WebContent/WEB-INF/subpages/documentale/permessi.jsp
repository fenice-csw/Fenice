<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml />
<div class="sezione">
<span class="title"><strong><bean:message key="documentale.permessi"/></strong></span>
<jsp:include page="/WEB-INF/subpages/documentale/uffici.jsp" />
<div>
<table summary="">
	<tr>
	    <td>
			<label for="tipoPermessoSelezionato"><bean:message key="documentale.tipopermessi"/></label><span class="obbligatorio">*</span>&nbsp;:
	    </td>
	    <td>
			<html:select styleClass="obbligatorio" property="tipoPermessoSelezionato">
				<html:optionsCollection name="tipiPermessiDocumenti" value="codice" label="description" />
			</html:select>	    
		</td>
	</tr>
	<tr>
	    <td>
			<label for="tipoPermessoSelezionato"><bean:message key="documentale.messaggio"/>:</label>
	    </td>
	    <td>
			<html:text property="msgPermesso" size="60" maxlength="255"></html:text>	    
		</td>
		
	</tr>
</table>

</div>
<br/>
<logic:notEmpty name="documentoForm" property="permessi">
<table id="assegnatari" width="100%">
  <tr>
    <th />
    <th><span><bean:message key="documentale.tipopermessi"/></span></th>
    <th><span><bean:message key="documentale.ufficio"/></span></th>
    <th><span><bean:message key="documentale.utente"/></span></th>
    <th><span><bean:message key="documentale.messaggio"/></span></th>    
  </tr>

<logic:iterate id="per" name="documentoForm" property="permessi">
  <tr>
    <td><html:multibox property="permessiSelezionatiId" >
		<bean:write name="per" property="key"/>
    	</html:multibox></td>
    <td><span>
		<bean:write name="per" property="descrizioneTipoPermesso" />
    	</span></td>
    <td>
    	<logic:notEmpty name="per" property="nomeUfficio">
			<span><bean:write name="per" property="nomeUfficio" /></span>
		</logic:notEmpty>	
    </td>
    <td><span>
		<bean:write name="per" property="nomeUtente" />
    	</span></td>
    <td>
    	<span><bean:write name="per" property="msgPermesso" /></span>
    </td>
  </tr>
</logic:iterate>  
</table>
<br />
<html:submit styleClass="button" property="rimuoviPermessiAction" value="Rimuovi" title="Rimuove i permessi selezionati dall'elenco" />
</logic:notEmpty>
</div>

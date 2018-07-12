<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<eprot:page title="Titolario">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>
<br />
<html:form action="/page/amministrazione/titolario.do" enctype="multipart/form-data">
<table summary="">
<logic:notEmpty name="titolarioForm" property="titolario" >
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.argomento.argomentopadre"/>:</span>
    </td>
    <td colspan="2">
      <span><strong>
        <bean:write name="titolarioForm" property="parentPath" /> - <bean:write name="titolarioForm" property="parentDescrizione" />
		<html:hidden property="parentPath"/>
		<html:hidden property="parentId"/>
      </strong></span>
    </td>
  </tr>
</logic:notEmpty>  

	<tr>  
		<td class="label">
			<label for="codice"><bean:message key="protocollo.argomento.codice"/></label>
			<span class="obbligatorio"> * </span>:
		</td>  
		<td>
			<html:hidden property="id"/>
			
			<logic:greaterThan name="titolarioForm" property="id" value="0">	
				<html:text property="codice" styleClass="obbligatorio" 
					disabled="true"	size="20" maxlength="20">
				</html:text>
			</logic:greaterThan>
			<logic:equal name='titolarioForm' property='id' value='0'>	
				<html:text property="codice"  styleClass="obbligatorio" 
					disabled="false" size="20" maxlength="20">
				</html:text>
			</logic:equal>
		</td>  
	</tr>
	<tr>  
    	<td class="label">
      		<label for="descrizione"><bean:message key="protocollo.argomento.descrizione"/></label>
      		<span class="obbligatorio"> * </span>:
    	</td>  
    	<td>
      		<html:text property="descrizione" styleClass="obbligatorio" size="60" maxlength="150"></html:text>
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
      		<label for="massimario"><bean:message key="protocollo.argomento.massimario"/></label>
      		:
    	</td>  
    	<td>
      		<html:text property="massimario" size="6" maxlength="6"></html:text>
    	</td>  
	</tr>
	<%--
	<tr>  
    	<td class="label">
      		<label for="giornimax">Numero di giorni (massimo)</label>
      		:
    	</td>  
    	<td>
      		<html:text property="giorniMax" size="6" maxlength="6"></html:text>
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
      		<label for="giorniAlert">Numero di giorni (alert)</label>
      		:
    	</td>  
    	<td>
      		<html:text property="giorniAlert" size="6" maxlength="6"></html:text>
    	</td>  
	</tr>
	<!--
	<tr>  
    	<td>
    	</td>
    	<td>
      		<div class="sezione">
				<span class="title">
					<strong>Responsabile del Procedimento</strong>
				</span>
		      	<jsp:include page="/WEB-INF/subpages/amministrazione/titolario/responsabile.jsp" />
      		</div>
    	</td>
	</tr>
	 -->
	<tr>  
    	<td>
    	</td>
    	<td>
      		<div class="sezione">
				<span class="title">
					<strong>Amministrazioni Partecipanti</strong>
				</span>
		      	<jsp:include page="/WEB-INF/subpages/amministrazione/titolario/amministrazioni.jsp" />
      		</div>
    	</td>  
	</tr>
	<tr>  
    	<td>
    	</td>
    	<td>
      		<div class="sezione">
				<span class="title">
					<strong>Riferimenti legislativi</strong>
				</span>
		      	<jsp:include page="/WEB-INF/subpages/amministrazione/titolario/riferimenti.jsp" />
      		</div>
    	</td>  
	</tr>
	 --%>
	<tr>
		<td></td>
		<td>		
			<logic:greaterThan value="0" name="titolarioForm" property="id">
				<html:submit styleClass="submit" property="btnConferma" value="Modifica" title="Modifica i dati del mezzo di spedizione" />
				<html:submit styleClass="submit" property="btnCancella" value="Cancella" title="Cancella il mezzo di spedizione"/>
				<html:submit styleClass="submit" property="btnAssocia" value="Associa uffici" title="Associa l'argomento agli uffici"/>
				<html:submit styleClass="submit" property="btnAssociaTuttiUffici" value="Associa tutti gli uffici" title="Associa l'argomento a tutti gli uffici"/>
			</logic:greaterThan>
			<logic:equal value="0" name="titolarioForm" property="id">
				<html:submit styleClass="submit" property="btnConferma" value="Salva" title="Inserisce il nuovo mezzo di spedizione" />
			</logic:equal>
			<html:submit styleClass="submit" property="btnAnnulla" value="Annulla" title="Annulla l'operazione" />
		</td>
	</tr>
</table>
</html:form>
</eprot:page>

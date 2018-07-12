<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Tipi procedimento">

<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>

<html:form action="page/amministrazione/tipiProcedimento.do" enctype="multipart/form-data">

<table summary="" style="width:80%">
	<tr>  
		<td class="label">
			<label for="descrizione"><bean:message bundle="bundleMessaggiAmministrazione" key="descrizione"/></label>
			<span class="obbligatorio"> * </span>:
		</td>  
		
		<td>
			<html:textarea property="descrizione" rows="3" cols="90%"/>
		</td>  
	</tr>
	<!--  -->
	<tr>  
    	<td class="label">
      		<label for="giornimax">Numero di giorni (massimo)</label>
      		<span class="obbligatorio"> * </span>:
    	</td>  
    	<td>
      		<html:text property="giorniMax" size="6" maxlength="6"></html:text>
    	</td>  
	</tr>
	<tr>  
    	<td class="label">
      		<label for="giorniAlert">Numero di giorni (alert)</label>
      		<span class="obbligatorio"> * </span>:
    	</td>  
    	<td>
      		<html:text property="giorniAlert" size="6" maxlength="6"></html:text>
    	</td>  
	</tr>
	<tr>  
    	<td>
    	</td>
    	<td>
      		<div class="sezione">
				<span class="title">
					<strong>Amministrazioni Partecipanti</strong>
				</span>
		      	<jsp:include page="/WEB-INF/subpages/amministrazione/tipiProcedimento/amministrazioni.jsp" />
      		</div>
    	</td>
    	
    </tr>
    <tr>
    	<td>
    	</td>
    	<td>
      		<div class="sezione">
				<span class="title">
					<strong>Uffici Partecipanti</strong>
				</span>
		      	<jsp:include page="/WEB-INF/subpages/amministrazione/tipiProcedimento/uffici_partecipanti.jsp" />
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
		      	<jsp:include page="/WEB-INF/subpages/amministrazione/tipiProcedimento/riferimenti.jsp" />
      		</div>
    	</td>  
	</tr>
	<tr>  
    	<td>
    	</td>
    	<td>
      		<div class="sezione">
				<span class="title">
					<strong><bean:message key="amministrazione.tipoProcedimento.titolario"/></strong>
				</span>
		      	<jsp:include page="/WEB-INF/subpages/amministrazione/tipiProcedimento/titolario.jsp" />
      		</div>
    	</td>  
	</tr>
	
	<tr>
		<td></td>
		<td>		
			<logic:greaterThan value="0" name="tipoProcedimentoForm" property="idTipo">
				<html:submit styleClass="submit" property="btnModifica" value="Modifica" title="Modifica i dati del tipo documento" />
				<html:submit styleClass="submit" property="btnCancella" value="Cancella" title="Cancella il tipo documento"/>
			</logic:greaterThan>
			<logic:equal value="0" name="tipoProcedimentoForm" property="idTipo">
				<html:submit styleClass="submit" property="btnConferma" value="Salva" title="Inserisce il nuovo tipo documento" />
			</logic:equal>
			<html:submit styleClass="submit" property="btnAnnulla" value="Indietro" title="Annulla l'operazione" />
		</td>
	</tr>
</table>

</html:form>

</eprot:page>

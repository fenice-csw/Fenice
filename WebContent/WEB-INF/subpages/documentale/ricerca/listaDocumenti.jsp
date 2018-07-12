<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<html:xhtml />
<bean:define value="/page/documentale/cerca.do" scope="request" id="url" />
<div>
	<logic:notEmpty name="documentoForm" property="listaDocumenti">
		<display:table class="simple" width="100%"
			requestURI="/page/documentale/cerca.do"
			name="sessionScope.documentoForm.listaDocumenti" export="false"
			sort="list" pagesize="10" id="row">


			<logic:notEmpty name="tornaFascicolo" >
					<display:column title="">
						<html:radio property="documentoSelezionato" idName="row" value="documentoId"></html:radio>
					</display:column>
			</logic:notEmpty>		
		
		
<%--			<display:column title="Nome File" property="nome"-->
<!--					href="<%=url%>" paramId="documentoSelezionato"-->
<!--					paramProperty="documentoId" />--%>
					
			<display:column title="Nome file">
				<html:link paramId="documentoSelezionato" paramName="row"
						paramProperty="documentoId" page="/page/documentale/cerca.do">
   					<span><bean:write name="row" property="nome" /></span>
  				</html:link>
			</display:column>		
			<display:column property="dataDocumento" title="Data" />
			<display:column property="oggetto" title="Oggetto" />
			<display:column property="argomento" title="Argomento" />
			<display:column property="owner" title="Owner" />
			<display:column title="Stato">
				 <logic:equal name="row" property="stato" value="L">
				<bean:message key="documentale.lavorazione"/>
				 </logic:equal>
				 <logic:equal name="row" property="stato" value="C">
				  <bean:message key="documentale.classificato"/>
				 </logic:equal>
				 <logic:equal name="row" property="stato" value="I">
				 	<bean:message key="documentale.inviato"/>
				 </logic:equal>
				 <logic:notEqual name="row" property="statoLav" value="0">
				 	<bean:message key="documentale.checkedout"/>: <bean:write name="row" property="usernameLav"/>
				 </logic:notEqual>	
			</display:column>
		</display:table>
	</logic:notEmpty>	
	<p>		
	<logic:notEmpty name="tornaFascicolo">
		<html:submit styleClass="submit" property="btnAggiungi" value="Aggiungi al fascicolo" alt="ggiungi al fascicolo" />
		<html:submit styleClass="button" property="btnAnnullaRicerca" value="Annulla" alt="Annulla"/>
	</logic:notEmpty>
	</p>
</div>

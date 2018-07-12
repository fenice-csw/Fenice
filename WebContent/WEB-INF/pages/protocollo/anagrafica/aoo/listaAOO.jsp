<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Cerca aree organizzative omogenee">



<html:form action="/page/protocollo/anagrafica/aoo/cerca.do">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>

<div id="cerca">

<%--	<jsp:include page="WEB-INF/subpages/protocollo/anagrafica/aoo/cerca.jsp" />--%>
		<table>
		  <tr>
		   <td>
		  		<span><bean:message key="soggetto.amministrazione.categoria"/></span>
		   </td>
		   <td>
			   <html:select property="categoriaId">
					<html:optionsCollection name ="LOOKUP_DELEGATE" property="categoriePA" value="codice" label="description" />
			   </html:select>
		  </td>
		  </tr>
		  <tr>
		    <td><span><bean:message key="soggetto.amministrazione.nome"/></span></td>
		    <td>
		    	<html:text property="nome" size="30" maxlength="50" />
		    	<html:submit styleClass ="submit" property="cerca" value="Cerca" alt="Cerca" />
		    </td>
		  </tr>
		</table>
</div>
<hr />
<div id="elenco">

<%--	<jsp:include page="WEB-INF/subpages/protocollo/anagrafica/aoo/elencoAoo.jsp" />--%>
	<display:table class="simple" width="100%"
		requestURI="/page/protocollo/anagrafica/aoo/cerca.do"
		name="requestScope.cercaAooForm.listaAoo" export="false"
		sort="list" pagesize="15" id="row">
		<display:column title="Area Organizzativa (AOO)">
			<html:link action="/page/protocollo/anagrafica/aoo/cerca.do" 
					paramName="row" paramId="codiceAoo" 
					paramProperty="codice">
					<bean:write name="row" property="intestazione"/>
			</html:link>	
		</display:column>	
		<display:column property="destinatario" title="Responsabile" />
	</display:table>
</div>

</html:form>

</eprot:page>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione Firma Digitale">
	<div id="protocollo-errori">
    	<html:errors bundle="bundleErroriAmministrazione"/>
	</div>
	
		 <logic:notEmpty name="listaCAForm" property="listaCa">
			<display:table 	class="simple" 	width="100%" name="requestScope.listaCAForm.listaCa" 
				export="false" sort="list" 	pagesize="10" id="row"
				requestURI="/page/amministrazione/firmadigitale/lista_ca.do">
				<display:column property="issuerCN" title="Issuer CN" 
					url="/page/amministrazione/firmadigitale/edit_ca.do"
					paramId="caSelezionata" paramProperty="id" />
				<display:column property="validoDal" title="Valido Dal" />
				<display:column property="validoAl" title="Valido fino al" />
				<display:column property="crlUrls.size" title="Punti di Distribuzione CRL" />
			</display:table>   
		</logic:notEmpty> 
	<html:form action="/page/amministrazione/firmadigitale/edit_ca.do" enctype="multipart/form-data">
		<html:submit styleClass="button" property="nuovo" value="Nuova CA" alt="Nuova CA" />
	</html:form>
</eprot:page>

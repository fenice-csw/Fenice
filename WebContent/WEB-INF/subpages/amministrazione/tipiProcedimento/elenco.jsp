<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />



<logic:notEmpty name="tipoProcedimentoForm" property="tipiProcedimento">

<display:table class="simple" width="100%"
	requestURI="/page/amministrazione/tipiProcedimento.do"
	name="sessionScope.tipoProcedimentoForm.tipiProcedimento" export="false"
	sort="list" pagesize="15" id="row">
	<display:column property="titolario" title="Classificazione" />
	<display:column title="Descrizione" property="descrizione"	
		url="/page/amministrazione/tipiProcedimento.do" 
		paramId="tipoProcId" paramProperty="idTipo" />
</display:table>
</logic:notEmpty>




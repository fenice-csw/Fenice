<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<html:xhtml />
<div>
	<logic:notEmpty name="scaricoForm" property="checkPICollection">
		<display:table class="simple" width="100%" requestURI="/page/protocollo/dashboard/showCheckPostaInterna.do"
			name="sessionScope.scaricoForm.checkPICollection"
			export="false" sort="list" pagesize="30" id="row" >
			<display:column property="dataOperazione" title="Letto il" />
			<display:column property="assegnatario" title="Da" />
			<display:column title="N° Posta Interna">
				<a id="variousPI" href="<%= request.getContextPath()%>/page/protocollo/posta_interna/summaryview.do?type=P&protocolloId=<bean:write name='row' property='protocolloId' />" >
					<bean:write name="row" property="protocolloDescrizione"/>
				</a>
			</display:column>
			<display:column title="Competente" property="competenteDescr"/>		
				<display:column title="Azioni">
					[<html:link  action="/page/protocollo/scarico.do"
						paramId="checkPostaInterna" paramProperty="checkId" paramName="row">Visto</html:link>]
				</display:column>
		</display:table>
		</logic:notEmpty>
		
</div>


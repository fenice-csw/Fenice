<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Lista Repertori">

	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" /></div>

	<div class="sezione"><span class="title"> <strong>Lista
	Repertori</strong> </span> <logic:notEmpty name="repertorioForm" property="repertori">
		<display:table class="simple" width="100%"
			name="sessionScope.repertorioForm.repertori" export="false"
			sort="page" pagesize="15" id="repertorio"
			requestURI="/page/protocollo/dashboard/showRepertori.do">
			<display:column title="ID">
				<html:link action="/page/repertorioView.do" paramName="repertorio"
					paramId="repertorioId" paramProperty="repertorioId">
					<bean:write name="repertorio" property="repertorioId" />
				</html:link>
			</display:column>
			<display:column property="descrizione" title="Descrizione" />
			<display:column property="nomeResponsabile" title="Responsabile" />
		</display:table>
	</logic:notEmpty> 
		<logic:empty name="repertorioForm" property="repertori">
			Nessun repertorio presente
		</logic:empty></div>
</eprot:page>
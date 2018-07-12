<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Lista Documenti Editor">

	<html:form action="/page/documentale/daTemplate">

	
		<div class="sezione"> 
		<logic:notEmpty name="editorForm" property="documenti">
			<display:table class="simple" width="100%"
				name="sessionScope.editorForm.documenti"
				export="false" sort="page" pagesize="15" id="doc"
				requestURI="/page/documentale/daTemplate.do">
				<!--  mettere un isULL -->
				<display:column title="Id">
					<html:link action="/page/documentale/daTemplate.do" paramName="doc" paramId="docId"paramProperty="documentoId">
						<bean:write name="doc" property="documentoId" />
					</html:link>
				</display:column>
				
				<display:column property="oggetto" title="Oggetto" />
				<display:column property="mittente" title="Autore" />
				<display:column property="fascicolo" title="Fascicolo/Procedimento" />
				<display:column property="msgCarica" title="Messaggio" />
				<%--
				<display:column title="Azioni">
				 	
					<html:link action="/page/documentale/daTemplate.do"
						paramName="doc" paramId="eliminaDoc"
						paramProperty="documentoId">
						[Elimina]
					</html:link>
				
				</display:column>
				--%>
			</display:table>
		</logic:notEmpty> 
		</div>
	</html:form>
</eprot:page>
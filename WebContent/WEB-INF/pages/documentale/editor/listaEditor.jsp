<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Lista Documenti Editor">

	


	<html:form action="/page/documentale/editor">

	
		<div class="sezione"><span class="title"> <strong>Documenti</strong>
		</span> <logic:notEmpty name="editorForm" property="documenti">
			<display:table class="simple" width="100%"
				name="sessionScope.editorForm.documenti"
				export="false" sort="page" pagesize="15" id="doc"
				requestURI="/page/documentale/editor.do">
				<display:column title="Id">
					<html:link action="/page/documentale/editor.do" paramName="doc" paramId="docId"paramProperty="documentoId">
						<bean:write name="doc" property="documentoId" />
					</html:link>
				</display:column>
				<display:column property="nome" title="Nome" />
				<display:column property="stato" title="Stato" />
				<display:column title="Azioni">
					<html:link action="/page/documentale/editor.do"
						paramName="doc" paramId="eliminaDoc"
						paramProperty="documentoId">
						[Elimina]
					</html:link>
				</display:column>

			</display:table>
		</logic:notEmpty> 
		<logic:empty name="editorForm" property="documenti">
			Nessun documento presente 
		</logic:empty>

		<div id="bottoni_salva">
			<html:submit styleClass="submit"property="btnNuovoDocumentoAction" value="Nuovo" alt="Inserisce un nuovo Documento" />
			<%-- 
			<html:submit styleClass="submit"property="btnDaTemplateAction" value="Da Template" alt="Inserisce un nuovo Documento da Template" />
			--%>
		</div>
		</div>
	</html:form>
</eprot:page>
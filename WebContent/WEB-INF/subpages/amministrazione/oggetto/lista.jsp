<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag-11.tld" prefix="display"%>

<html:xhtml />

<logic:notEmpty name="oggettiForm" property="oggettiCollection">

<display:table class="simple" width="100%" requestURI="/page/amministrazione/oggettario.do"
			name="sessionScope.oggettiForm.oggettiCollection"
			export="false" sort="list" pagesize="50" id="row">
			
			<display:column title="Descrizione">
				<html:link  action="/page/amministrazione/oggettario.do" 
						paramId="oggettoSelezionato" paramProperty="oggettoId" paramName="row" >
				<bean:write name="row" property="descrizione" />
				</html:link>
			</display:column>
			
			<display:column property="giorniAlert" title="Giorni Alert" />
			<display:column title="Azioni">
					[<html:link  action="/page/amministrazione/oggettarioDelete.do" 
						paramId="elimina" paramProperty="oggettoId" paramName="row" >Elimina</html:link>]
			</display:column>	
		</display:table>
	</logic:notEmpty>
	<logic:empty  name="oggettiForm" property="oggettiCollection">
	<span> <strong>Nessun oggetto presente</strong> </span>
	</logic:empty>


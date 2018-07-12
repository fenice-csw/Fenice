<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

		<p>
			<html:radio property="tipo" value="IN" onclick="document.forms[0].submit()">
				Interni
			</html:radio>
			&nbsp;&nbsp;
			<html:radio property="tipo" value="OUT" onclick="document.forms[0].submit()" >
				Esterni
			</html:radio>	
		</p>
			
		<logic:equal name="editorForm" property="tipo" value="IN">
			<jsp:include page="/WEB-INF/subpages/documentale/editor/assegnatari.jsp" />
		</logic:equal>
		<logic:equal name="editorForm" property="tipo" value="OUT">
			<jsp:include page="/WEB-INF/subpages/documentale/editor/destinatariEditor.jsp" />
		</logic:equal>
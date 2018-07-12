<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<html:html>
<head>
<title>Fenice - <bean:message key="home.title" /></title>
 
<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/style/login.css" paramScope="request" paramName="url" />' />
<%--
<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/style/compit.css" paramScope="request" paramName="url" />' />
--%>
</head>
<body>
<div id="header"><span class="title"><STRONG><bean:message
	key="registro.title" />  
 </STRONG></span></div>
<hr />
<html:errors />
<html:form action="/selezionaRegistroUfficio.do">
	<logic:equal name="SelezionaRegistroUfficioForm"
		property="tabellaVisibile" value="true">
		<div class="sezione">
		<table>
			<logic:equal name="SelezionaRegistroUfficioForm"
				property="multiCarica" value="true">
				<tr>
					<th align="right"><label for="ufficioId"> Seleziona la
					carica di competenza </label>:</th>
					<td><html:select name="SelezionaRegistroUfficioForm"
						property="caricaId">
						<html:optionsCollection name="SelezionaRegistroUfficioForm"
							property="cariche" value="caricaId"
							label="descrizioneCaricaUfficio" />
					</html:select></td>
				</tr>
			</logic:equal>
			<logic:equal name="SelezionaRegistroUfficioForm"
				property="numPrtZero" value="true">
				<tr>
					<th align="right"><label for="numPrt"> Scegli il
					numero di protocollo di partenza <!-- 
      	<bean:message key="registro.ufficio"/>
      	 --> </label>:</th>
					<td><html:text property="numPrt"
						name="SelezionaRegistroUfficioForm" size="4"></html:text></td>
				</tr>
			</logic:equal>
			<tr>
				<td></td>
				<td><br />
				<html:submit styleClass="submit" property="buttonSubmit"
					value="Seleziona"></html:submit></td>
			</tr>
		</table>
		<br />
		</div>
	</logic:equal>
	<br />
	<br />
<%--  	
	<logic:equal name="SelezionaRegistroUfficioForm"
		property="utenteResponsabile" value="true">
		<div> 
		<html:submit styleClass="submit"
			property="selectUtenteResponsabileAction" value="Accedi come Responsabile dell'Area Organizzativa"
			alt="Accedi come Utente Responsabile dell'area organizzativa"  />
			<br />		
		</div>
	</logic:equal>
--%>
</html:form>
</body>
</html:html>

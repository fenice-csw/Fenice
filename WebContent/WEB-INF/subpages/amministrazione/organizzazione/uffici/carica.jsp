<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />



	
	<div>
		<table summary="">
			<tr>
				
				<td>
				<div class="sezione"><span class="title"> <strong>Seleziona l'Utente Responsabile dell'AOO</strong>
				</span>
				<p /><html:select property="utenteResponsabileId">
					<html:optionsCollection property="utentiResponsabileCollection" value="id"
						label="fullName" />
				</html:select> <html:submit styleClass="submit" property="selectResponsabileAction"
					value="Seleziona" title="Seleziona l'utente" />
				<p />
				<logic:notEmpty name="ufficioForm" property="utenteResponsabile">
					<span>Responsabile: <strong> 
					<bean:write name="ufficioForm" property="utenteResponsabile.fullName" /></strong></span>
				</logic:notEmpty>
				</div>
				</td>
			</tr>
		</table>
	</div>

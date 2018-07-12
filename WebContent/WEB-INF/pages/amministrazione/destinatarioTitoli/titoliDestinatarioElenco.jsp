<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Gestione amministrazione">

	<div id="protocollo-errori">
		<jsp:include page="/WEB-INF/subpages/amministrazione/common/errori.jsp" />
	</div>
	<html:form action="/page/amministrazione/titoliDestinatario.do">
		<logic:empty name="titoloDestinatarioForm" property="titoli" >
			
				<span class="title"><strong><strong><bean:message key="amministrazione.elencotitolivuoto"/></strong></span><br />
			
		</logic:empty>	
		<logic:notEmpty name="titoloDestinatarioForm" property="titoli" >
			<div class="sezione">
				<span class="title"><strong><bean:message key="amministrazione.elencotitoli"/></strong></span><br />
				<table summary="">
					
					<logic:iterate id="titolo" name="titoloDestinatarioForm" property="titoli">
					<tr>
						<td>
							<span>
								<bean:define id="id" name="titolo" property="id"/>
								<html:radio property="id" value="id" idName="titolo" />
							</span>
						</td>
						<td>
							<span>
								<bean:write name="titolo" property="description" />
							</span>
						</td>
						
					</tr>
					</logic:iterate>
				</table>	
			</div>
		</logic:notEmpty>
		
		
		<p>
	    <html:submit styleClass="submit" property="btnNuovo" value="Nuovo" alt="Nuovo" /> 
		<html:submit styleClass="button" property="btnModifica" value="Modifica" alt="Modifica" />
		<html:submit styleClass="button" property="btnCancella" value="Cancella" alt="Modifica" />
		</p>
	</html:form>
</eprot:page>

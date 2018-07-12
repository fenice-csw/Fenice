<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<div>

<table summary="">
	<tr>
	    <td class="label">
	    	<label for="descrizione">
	    <html:hidden property="codice" />
				<bean:message key="soggetto.lista.descrizione"/>
			</label><span class="obbligatorio"> * </span>:
		</td>
	    <td>
		<html:text property="descrizione" styleClass="obbligatorio" styleId="descrizione" size="50" maxlength="100" />
		</td>
	</tr>	
</table>

</div>



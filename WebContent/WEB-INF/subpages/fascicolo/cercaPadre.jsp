<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
	<tr>
		<td class="label">
    		<label for="oggettoFascicolo">fascicolo Padre:</label>
	    </td>
		<td>		
		<logic:empty name="fascicoloForm" property="padre">
				<logic:equal name="fascicoloForm" property="id" value="0" >
				<div class="sezione">
				<p>	
					<label for="oggettoFascicoloPadre"><bean:message key="fascicolo.oggetto"/>:</label>
					<html:text property="oggettoFascicoloPadre" styleId="oggettoFascicoloPadre" size="40" maxlength="100"></html:text>
					<html:submit styleClass="submit" property="cercaFascicoloPadreAction" value="Cerca" title="Cerca Fascicolo padre"/>
				</p>
				</div>
				</logic:equal>
			</logic:empty>
			<logic:notEmpty name="fascicoloForm" property="padre">
			 	<span><strong>
					<bean:write name="fascicoloForm" property="padre"/>
				</strong></span>
				<logic:equal name="fascicoloForm" property="id" value="0" >
					<html:submit styleClass="submit" property="annullaPadreAction" value="Annulla" title="Cerca Fascicolo padre"/>
				</logic:equal>
				
			</logic:notEmpty>
		</td>
	</tr>

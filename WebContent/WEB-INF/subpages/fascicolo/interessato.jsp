<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />

<div class="sezione">
	<p>
	<table summary="">
				<tr>
					<td>
						<logic:notEmpty name="fascicoloForm" property="interessato">
							<span><strong><bean:write name="fascicoloForm" property="interessato" /></strong></span>
			    		</logic:notEmpty>
					</td>			
					<td>
						<html:submit styleClass="button" property="cercaSoggettoAction"value="seleziona l'interessato" title="Seleziona l'interessato dalla rubrica" />
						<logic:notEmpty name="fascicoloForm" property="interessato">
							<html:submit styleClass="button" property="annullaInteressatoAction"value="annulla" title="annulla" />
				    	</logic:notEmpty>
					</td>
				</tr>
	</table>
	</p>
</div>
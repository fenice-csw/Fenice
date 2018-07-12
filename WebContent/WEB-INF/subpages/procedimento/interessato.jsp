<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>
<html:xhtml />

<div class="sezione">
	<p>
	<table summary="">
		<tr>  
			<logic:notEmpty name="procedimentoForm" property="interessato">
	
				<td>
						<span><strong><bean:write name="procedimentoForm" property="interessato" /></strong></span>
				</td>    
			</logic:notEmpty>
				<td>
					<html:submit styleClass="button" property="cercaInteressatoAction"value="seleziona" title="Seleziona l'interessato dalla rubrica" />
				</td>
			<logic:notEmpty name="procedimentoForm" property="interessato">
					<td>
						<html:submit styleClass="button" property="annullaInteressatoAction"value="Annulla" title="Annulla" />
					</td>
			</logic:notEmpty>
		</tr>
	</table>
	</p>
</div>
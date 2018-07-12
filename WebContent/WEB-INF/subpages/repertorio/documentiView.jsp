<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<html:xhtml />

<p><logic:notEmpty property="documentiAllegatiCollection"
	name="documentoRepertorioForm">
	<fieldset><legend>Documenti inseriti</legend>
	<table summary=""  border="1" id="tabella_mittenti">
		<tr>
			
			<th>Nome file</th>
			<th>Dimensione</th>
			<th>Riservato</th>
			<th>Principale</th>
			<th>Pubblicabile</th>
			<logic:equal name="documentoRepertorioForm" property="unitaAmministrativa.denominazione" value="POLICLINICO_CT">
				<th>Ex. Art. 12</th>
			</logic:equal>
		</tr>
		<logic:iterate id="recordDocumento"
			property="documentiAllegatiCollection" name="documentoRepertorioForm">
			<bean:define id="idx" name="recordDocumento" property="idx" />
			<bean:define id="descrizione" name="recordDocumento"
				property="descrizione" />
			<bean:define id="size" name="recordDocumento" property="size" />
			<bean:define id="riservato" name="recordDocumento"
				property="riservato" />
			<tr>
				<td><html:link
					action="/page/repertorio/documento_repertorio.do"
					paramId="downloadAllegatoId" paramName="recordDocumento"
					paramProperty="idx" target="_blank" title="Download File">
					<bean:write name="recordDocumento" property="descrizione" />
				</html:link></td>
				<td><bean:write name="recordDocumento" property="size" />bytes</td>
				
				<td><logic:equal property="riservato" name="recordDocumento" value="true">
		  				Si
		  			</logic:equal> 
		  			<logic:equal property="riservato" name="recordDocumento" value="false">
		  				No
		  			</logic:equal>
		  		</td>
		  		
		  		<td><logic:equal property="principale" name="recordDocumento" value="true">
		  				Si
		  			</logic:equal> 
		  			<logic:equal property="principale" name="recordDocumento" value="false">
		  				No
		  			</logic:equal>
		  		</td>
		  		<td><logic:equal property="pubblicabile" name="recordDocumento" value="true">
		  				Si
		  			</logic:equal> 
		  			<logic:equal property="pubblicabile" name="recordDocumento" value="false">
		  				No
		  			</logic:equal>
		  		</td>
		  		<logic:equal name="documentoRepertorioForm" property="unitaAmministrativa.denominazione" value="POLICLINICO_CT">
				<td><logic:equal property="type" name="recordDocumento"
						value="0">
		  				No
		  			</logic:equal> 
		  			<logic:equal property="type" name="recordDocumento"
						value="1">
		  				Si
		  			</logic:equal>
		  		</td>
		  		</logic:equal>
		  	</tr>
		</logic:iterate>
	</table>
	<br />
	</fieldset>
</logic:notEmpty></p>



<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<html:xhtml />

<span class="hidden">Sezione corrente:<strong><bean:write
	name="protocolloForm" property="sezioneVisualizzata" /></strong></span>
<br class="hidden" />
<span class="hidden">Sezioni: </span>
<div id="link-sezioni">
<logic:notEmpty name="protocolloForm"
	property="elencoSezioni">
	<logic:iterate id="sez" name="protocolloForm" property="elencoSezioni">
	
		<bean:define id="OR_CONDITION" value="false"/>
		<logic:match name="sez" property="nome" value="Titolario"><bean:define id="OR_CONDITION" value="true"/></logic:match>
		<logic:match name="sez" property="nome" value="Fascicoli"><bean:define id="OR_CONDITION" value="true"/></logic:match>
		<%--  
		<logic:match name="sez" property="nome" value="Procedimenti"><bean:define id="OR_CONDITION" value="true"/></logic:match>
		--%>
	<logic:equal name="OR_CONDITION" value="false">
		<logic:equal name="sez" property="corrente" value="true">
			<logic:equal name="sez" property="obbligatorio" value="true">
			<html:submit property="sezioneVisualizzata"
				styleClass="correnteobbligatorio">
				<bean:write name="sez" property="nome" />
			</html:submit>
			</logic:equal>
			<logic:notEqual name="sez" property="obbligatorio" value="true">
			<html:submit property="sezioneVisualizzata"
				styleClass="corrente">
				<bean:write name="sez" property="nome" />
			</html:submit>
			</logic:notEqual>
		</logic:equal>
		<logic:notEqual name="sez" property="corrente" value="true">
			<logic:equal name="sez" property="obbligatorio" value="true">
			<html:submit property="sezioneVisualizzata"
				styleClass="obbligatorio">
				<bean:write name="sez" property="nome" />
			</html:submit>
			</logic:equal>
			<logic:notEqual name="sez" property="obbligatorio" value="true">
			<html:submit property="sezioneVisualizzata">
				<bean:write name="sez" property="nome" />
			</html:submit>
			</logic:notEqual>
		</logic:notEqual>
		</logic:equal>
	</logic:iterate>
</logic:notEmpty>
</div>

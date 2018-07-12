<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />


<bean:define id="sezioneVisualizzata" name="fascicoloForm" property="sezioneVisualizzata" />
  <span class="hidden">Sezione corrente: 
		<strong><bean:write name="fascicoloForm" property="sezioneVisualizzata" /></strong>
  </span>
  <br class="hidden" />
  <span class="hidden">Sezioni: </span>
<div id="link-sezioni">

<logic:notEmpty name="fascicoloForm" property="elencoSezioni">
	<logic:iterate id="sez" name="fascicoloForm" property="elencoSezioni">
		<logic:equal name="sez" property="corrente" value="true">
			<html:submit property="sezioneVisualizzata" styleClass="corrente">
				<bean:write name="sez" property="nome" />
			</html:submit>
		</logic:equal>
		<logic:notEqual name="sez" property="corrente" value="true">
			<html:submit property="sezioneVisualizzata" styleClass="">
				<bean:write name="sez" property="nome" />
			</html:submit>
		</logic:notEqual>
	</logic:iterate>
</logic:notEmpty>
</div>


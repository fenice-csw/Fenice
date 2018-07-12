<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<%! String[] sezioni = {"Permessi", "Titolario", "Fascicoli"};
%>
<%  request.setAttribute("sezioni", sezioni); %>

  <span class="hidden">Sezione corrente: 
		<strong><bean:write name="section" /></strong>
  </span>
  <br class="hidden" />
  <span class="hidden">Sezioni: </span>
<div id="link-sezioni">
<logic:notEmpty name="sezioni">
<logic:iterate id="sez" name="sezioni">
	<bean:define id="sezioneVisualizzata" name="documentoForm" property="sezioneVisualizzata" />
	<logic:equal name="sez" value="<bean:write name='documentoForm' property='sezioneVisualizzata' />" >
		<html:submit property="sezioneVisualizzata" value="<bean:write name='documentoForm' property='sezioneVisualizzata' />" styleClass="corrente"/>
	</logic:equal>
	<logic:equal name="sez" value="Permessi">
		<html:submit property="sezioneVisualizzata" value="Permessi" styleClass="obbligatorio"/>
	</logic:equal>
</logic:iterate>
</logic:notEmpty>


</div>

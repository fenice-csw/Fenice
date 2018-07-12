<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />

<eprot:page title="Ricarica organizzazione">
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriAmministrazione" />
</div>

<html:form action="/page/amministrazione/organizzazione.do" >
	<p align="center">
	<span>
	<logic:equal name="ORGANIZZAZIONE_ROOT" property="numeroUtentiConnessi" value="1">
		Non ci sono altri utenti connessi.
	</logic:equal>	
	<logic:greaterThan name="ORGANIZZAZIONE_ROOT" property="numeroUtentiConnessi" value="1">
		Ci sono 
			<strong> 
				<bean:write name="ORGANIZZAZIONE_ROOT" property="numeroUtentiConnessi" />
			</strong>
			utenti connessi:
	</logic:greaterThan>
	</span>		
	</p>
	<br />
	
	<logic:greaterThan name="ORGANIZZAZIONE_ROOT" property="numeroUtentiConnessi" value="1">
		<span>
			<div>
			<ul>		
			<logic:notEmpty name="ORGANIZZAZIONE_ROOT" property="utentiConnessi">
			<logic:iterate id="currentRecord" name="ORGANIZZAZIONE_ROOT" property="utentiConnessi">
					<logic:notEqual name="currentRecord" property="valueObject.id" value="UTENTE_KEY.valueObject.id" >
						<li>
							<strong><bean:write name="currentRecord" property="valueObject.fullName" /></strong> 
							<logic:notEmpty name="currentRecord" property="ufficioVOInUso">
								<bean:write name="currentRecord" property="ufficioVOInUso.description" /> - 
							</logic:notEmpty>
							<logic:notEmpty name="currentRecord" property="registroVOInUso">
								<bean:write name="currentRecord" property="registroVOInUso.descrizioneRegistro" />
							</logic:notEmpty>
						</li>
					</logic:notEqual>
			</logic:iterate>
			</logic:notEmpty>			
			</ul>
			</div>
		</span>
	</logic:greaterThan>
	
	<p align="center">
	<br/><span>La conferma di tale operazione comporta la disconnessione di tutti gli utenti dal sistema.</span>
	<br /><br />
		<html:submit styleClass="submit" property="btnRicaricaOrganizzazione" value="Conferma" alt="Conferma"/>
		<html:submit styleClass="button" property="btnAnnulla" value="Annulla" alt="Annulla l'operazione"/>
	</p>

</html:form>

</eprot:page>
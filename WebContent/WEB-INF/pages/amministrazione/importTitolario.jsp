<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Import Titolario">
<div id="protocollo-errori">
<html:errors bundle="bundleErroriAmministrazione" />
</div>


<html:form action="/cambioPwd.do"></html:form>
<logic:empty name="imported">
<html:form action="/page/amministrazione/executeImport.do" method="post" enctype="multipart/form-data">
		<input type="file" name="file">
		<input type="reset" class="button" value="Cancella" /> 
		<input type="submit" class="button" value="Salva" />
</html:form>
<br><br>

</logic:empty>
<logic:notEmpty name="imported">
Siamo spiacenti!
<br>Impossibile importare il file. Il titolario legato alla Area Organizzativa Omogenea a cui appartiene l'utente deve essere vuota.
	Volete sovrascrivere la tabella?
<html:form action="/page/amministrazione/forcedImport.do" method="post" enctype="multipart/form-data">	
		<input type="submit" class="button" value="Si" />
</html:form>
</logic:notEmpty>


<logic:notEmpty name="dependencies">
Impossibile sovrascrivere il titolario in quanto referenziato da altri elementi.
</logic:notEmpty>
</eprot:page>

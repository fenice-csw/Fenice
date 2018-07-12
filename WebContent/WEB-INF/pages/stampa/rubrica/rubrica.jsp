<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>
<html:xhtml />
<eprot:page title="Stampa rubrica anagrafica">


<html:form action="/page/stampa/rubrica" >
<div id="protocollo-errori">
  <html:errors bundle="bundleErroriProtocollo" />
</div>
<div>
<table summary="">
  <tr>
    <td class="label">
      <span><bean:message key="protocollo.mittente.tipo"/>:</span>
    </td>
    <td class="subtable">
      <table summary="">
        <tr>
          <td>
            <html:radio property="flagTipo" styleId="flagTipoF" value="F" >
              <label for="flagTipoF"><bean:message key="protocollo.mittente.personafisica"/></label>
            </html:radio>
          </td>
          <td>
            <html:radio property="flagTipo" styleId="flagTipoG" value="G">
              <label for="flagTipoG"><bean:message key="protocollo.mittente.personagiuridica"/></label>
            </html:radio>
          </td>
          <td>
			<html:submit styleClass="submit" property="btnStampa" value="Stampa" title="Stampa"/>

			<html:submit styleClass="button" property="btnAnnulla" value="Annulla" title="Annulla"/>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<hr></hr>
</div>
<div>
	<jsp:include page="/WEB-INF/subpages/stampa/rubrica/lista.jsp" />
</div>

</html:form>

</eprot:page>


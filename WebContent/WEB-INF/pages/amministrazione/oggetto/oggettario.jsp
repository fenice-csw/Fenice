<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Oggettario">
	<div id="protocollo-errori">
		<html:errors bundle="bundleErroriAmministrazione" />
	</div>
	

	<html:form action="/page/amministrazione/oggettarioAdd.do">

		<div class="sezione"><span class="title"> <strong>Oggetto</strong> </span>
		<table summary="">
			<tr>
				<td class="label"><label title="Descrizione" for="descrizione">Descrizione</label>&nbsp;:</td>
				<td align="left"><html:text styleClass="text"
					styleId="descrizione" property="descrizione" /></td>
			</tr>
			<tr>
				<td class="label"><label title="Descrizione" for="descrizione">Numero di giorni (alert)</label>&nbsp;:</td>
				<td align="left"><html:text styleClass="text"
					styleId="giorniAlert" property="giorniAlert" /></td>
			</tr>
			<tr>
				<td class="label">Assegnatari:</td>
				<td align="left">
				<div class="sezione"><jsp:include
					page="/WEB-INF/subpages/amministrazione/oggetto/uffici.jsp" /> <br>
				<jsp:include
					page="/WEB-INF/subpages/amministrazione/oggetto/assegnatari.jsp" />
				</div>
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td align="left"><html:submit value="Salva" alt="Salva Oggetto" styleClass="submit" property="salvaAction"/>
				&nbsp;
				<html:submit value="Indietro" alt="Pag. precedente" styleClass="submit" property="indietroAction"/></td>
			</tr>
		</table>
		</div>
	</html:form>
</eprot:page>

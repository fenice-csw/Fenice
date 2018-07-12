<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<logic:notEmpty name="protocolloForm" property="fascicoliProtocolloOld">
	
	<br/>
		<label>Già fascicolato in:</label>
		<ul>	
		
		<logic:iterate id="currentRecord" property="fascicoliProtocolloOld" name="protocolloForm">
		<li>
		<logic:equal  name="currentRecord" property="owner" value="true">
			<html:multibox property="fascicoloSelezionatoOldId"><bean:write name="currentRecord" property="id"/></html:multibox>
		</logic:equal>
			
			<logic:equal  name="currentRecord" property="owner" value="true">
				<logic:equal name="protocolloForm" property="flagTipo" value="I">
					<html:link action="/page/protocollo/ingresso/documento.do" paramName="currentRecord" paramId="fascicoloId" paramProperty="id">
						<bean:write name="currentRecord" property="annoProgressivo"/>
					</html:link>
				</logic:equal>
				<logic:equal name="protocolloForm" property="flagTipo" value="U">
					<html:link action="/page/protocollo/uscita/documento.do" paramName="currentRecord" paramId="fascicoloId" paramProperty="id">
						<bean:write name="currentRecord" property="annoProgressivo"/>
					</html:link>
				</logic:equal>
				<logic:equal name="protocolloForm" property="flagTipo" value="P">
					<html:link action="/page/protocollo/posta_interna/documento.do" paramName="currentRecord" paramId="fascicoloId" paramProperty="id">
						<bean:write name="currentRecord" property="annoProgressivo"/>
					</html:link>
				</logic:equal>
			</logic:equal>
			
			<logic:notEqual  name="currentRecord" property="owner" value="true">
				<bean:write name="currentRecord" property="annoProgressivo"/>
			</logic:notEqual>
		 - <bean:write name="currentRecord" property="oggetto"/>
		</li>
		</logic:iterate>
	</ul>	
	<br/>
	<br/>
	
	</logic:notEmpty>
<table summary="">
	<tr>
		<td>
			<label for="cercaFascicoloNome"><bean:message key="protocollo.fascicolo.oggettofascicolo"/>:</label>
			<html:text property="cercaFascicoloNome" styleId="cercaFascicoloNome" size="50" maxlength="100"></html:text>&nbsp;
			<html:submit styleClass="submit" property="btnCercaFascicoli" value="Cerca" title="Cerca Fascicoli"/>
			<html:submit styleClass="submit" property="btnNuovoFascicolo" value="Nuovo" title="Nuovo Fascicolo"/>
		</td>
	</tr>
	<logic:notEmpty name="protocolloForm" property="fascicoliProtocollo">
	<tr>
		<td>
			<label for="nomeFascicolo"><bean:message key="protocollo.fascicolo.fascicoloassociato"/>:</label>
		</td>
	</tr>
	<tr>		
		<td>
		<logic:iterate id="currentRecord" property="fascicoliProtocollo" name="protocolloForm">
		<logic:equal  name="currentRecord" property="owner" value="true">
			<html:multibox property="fascicoloSelezionatoId"><bean:write name="currentRecord" property="id"/></html:multibox>
		</logic:equal>
		<span>
		
		<logic:equal  name="currentRecord" property="progressivo" value="0">
			<!--
			<bean:write name="currentRecord" property="annoRiferimento"/>&lt;Nuovo&gt;
			-->
			&lt;Nuovo&gt;
		</logic:equal>
		<logic:greaterThan name="currentRecord" property="progressivo" value="0">
				<logic:equal name="protocolloForm" property="flagTipo" value="I">
					<html:link action="/page/protocollo/ingresso/documento.do" paramName="currentRecord" paramId="fascicoloId" paramProperty="id">
						<bean:write name="currentRecord" property="annoProgressivo"/> 
					</html:link>
				</logic:equal>
				<logic:equal name="protocolloForm" property="flagTipo" value="U">
					<html:link action="/page/protocollo/uscita/documento.do" paramName="currentRecord" paramId="fascicoloId" paramProperty="id">
						<bean:write name="currentRecord" property="annoProgressivo"/> 
					</html:link>
				</logic:equal>
				<logic:equal name="protocolloForm" property="flagTipo" value="P">
					<html:link action="/page/protocollo/posta_interna/documento.do" paramName="currentRecord" paramId="fascicoloId" paramProperty="id">
						<bean:write name="currentRecord" property="annoProgressivo"/>
					</html:link>
				</logic:equal>
		</logic:greaterThan>
		<logic:notEmpty name="currentRecord" property="oggetto">
		 - <bean:write name="currentRecord" property="oggetto"/>
		</logic:notEmpty>
		</span>
		<br/>
		</logic:iterate>
		
		</td>
	</tr>
	</logic:notEmpty>
	<tr>
		<td>
		<bean:define id="OR_CONDITION" value="false"/>
		<logic:notEmpty name="protocolloForm" property="fascicoliProtocollo"><bean:define id="OR_CONDITION" value="true"/></logic:notEmpty>
		<logic:notEmpty name="protocolloForm" property="fascicoliProtocolloOld"><bean:define id="OR_CONDITION" value="true"/></logic:notEmpty>
		<logic:equal name="OR_CONDITION" value="true">
			<html:submit styleClass="submit" property="rimuoviFascicoli" value="Rimuovi" title="Rimuovi i fascicoli selezionati"/>
		</logic:equal>
		</td>
	</tr>
	
</table>


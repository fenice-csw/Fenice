<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />
<logic:notEmpty name="protocolloForm" property="fascicoliProtocolloOld">
<div>
<ul>
	<logic:iterate id="currentRecord" property="fascicoliProtocolloOld" name="protocolloForm">
		<li>
		<span>
		<logic:equal name="protocolloForm" property="flagTipo" value="I">
			<logic:equal  name="currentRecord" property="owner" value="true">
				<html:link action="/page/protocollo/ingresso/documentoview.do" paramName="currentRecord" paramId="fascicoloId" paramProperty="id">				
					<bean:write name="currentRecord" property="annoProgressivo"/>
					- 
					<bean:write name="currentRecord" property="oggetto"/>
					<logic:equal  name="currentRecord" property="stato" value="1">
					<span class="obbligatorio">
					[chiuso]
					</span>
					</logic:equal>
				</html:link>
			</logic:equal>
			<logic:equal  name="currentRecord" property="owner" value="false">				
				<bean:write name="currentRecord" property="annoProgressivo"/>
				- 
				<bean:write name="currentRecord" property="oggetto"/>
				<logic:equal  name="currentRecord" property="stato" value="1">
					<span class="obbligatorio">
					[chiuso]
					</span>
					</logic:equal>
			</logic:equal>
		</logic:equal>	
			
		<logic:equal name="protocolloForm" property="flagTipo" value="U">
		   	<logic:equal  name="currentRecord" property="owner" value="true">
				<html:link action="/page/protocollo/uscita/documentoview.do" paramName="currentRecord" paramId="fascicoloId" paramProperty="id">			
					<bean:write name="currentRecord" property="annoProgressivo"/>
					- 
					<bean:write name="currentRecord" property="oggetto"/>
					<logic:equal  name="currentRecord" property="stato" value="1">
					<span class="obbligatorio">
					[chiuso]
					</span>
					</logic:equal>
				</html:link>
			</logic:equal>
			<logic:equal  name="currentRecord" property="owner" value="false">			
				<bean:write name="currentRecord" property="annoProgressivo"/>
				- 
				<bean:write name="currentRecord" property="oggetto"/>
				<logic:equal  name="currentRecord" property="stato" value="1">
					<span class="obbligatorio">
					[chiuso]
					</span>
				</logic:equal>
			</logic:equal>
		</logic:equal>		

		<logic:equal name="protocolloForm" property="flagTipo" value="P">
		   	<logic:equal  name="currentRecord" property="owner" value="true">
				<html:link action="/page/protocollo/posta_interna/documentoview.do" paramName="currentRecord" paramId="fascicoloId" paramProperty="id">			
					<bean:write name="currentRecord" property="annoProgressivo"/>
					- 
					<bean:write name="currentRecord" property="oggetto"/>
					<logic:equal  name="currentRecord" property="stato" value="1">
					<span class="obbligatorio">
					[chiuso]
					</span>
					</logic:equal>
				</html:link>
			</logic:equal>
			<logic:equal  name="currentRecord" property="owner" value="false">			
				<bean:write name="currentRecord" property="annoProgressivo"/>
				- 
				<bean:write name="currentRecord" property="oggetto"/>
				<logic:equal  name="currentRecord" property="stato" value="1">
					<span class="obbligatorio">
					[chiuso]
					</span>
				</logic:equal>
			</logic:equal>
		</logic:equal>

		</span>
		
		</li><br/>
	</logic:iterate>
</ul>
</div>
</logic:notEmpty>

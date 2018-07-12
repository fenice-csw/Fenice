<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot"%>

<html:xhtml />

<eprot:page title="Log Page">
	<div id="protocollo-errori"><html:errors
		bundle="bundleErroriAmministrazione" />
	</div>


		<div align="center">
		<br>
		<br>
		<br>
	<div id="zona_lavoro_dash">
		<div id="titolo_zonalavoro" align="left"><strong>LOG</strong></div>
		<div id="corpo_zonalavoro" align="left">
			<table border="0" cellpadding="5" cellspacing="5" width="100%">
				<tr>
					<td>
						<html:link href="log.do?type=debug">DEBUG</html:link>
					</td>
				</tr>				
				<tr>
					<td>
						<html:link href="log.do?type=error">ERROR</html:link>
					</td>
				</tr>
				<tr>
					<td>
						<html:link href="log.do?type=console">CONSOLE</html:link>
					</td>
				</tr>
			</table>
		</div>
	</div>

		</div>

</eprot:page>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml />

<table>
  <tr>
   <td>
  	<span><bean:message key="soggetto.amministrazione.categoria"/></span>
   </td>
   <td>
   <html:select property="categoriaId">
		<html:optionsCollection name ="LOOKUP_DELEGATE" property="categoriePA" value="codice" label="description" />
   </html:select>
  </td>
  </tr>
  <tr>
    <td><span><bean:message key="soggetto.amministrazione.nome"/></span></td>
    <td><html:text property="nome" size="30" maxlength="50" />
    <html:submit styleClass ="submit" property="cerca" value="Cerca" alt="Cerca" />
    <html:submit styleClass ="button" property="annulla" value="Indietro" alt="Indietro" />
    </td>
  </tr>
</table>


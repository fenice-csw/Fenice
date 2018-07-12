<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>


<html:xhtml />
<html:html>
<head>
  <title>Fenice - <bean:message key="home.title"/></title>
  
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/style/login.css" paramScope="request" paramName="url" />' />
</head>
<br>
<div id="header">
        <img title="Fenice" align="center" border="0" src="<html:rewrite page='/images/logo/logo_fenice.png'/>"/> 
  </div><br>


<h2>[ <bean:write name="ORGANIZZAZIONE_ROOT" property="valueObject.description" /> ]</h2>
<table id="layout" summary="">
<tr>
<td id="loginbase"> 
  
  <logic:equal name="ORGANIZZAZIONE_ROOT" property="valueObject.flagLdap" value="1">
  <tr>
  	<td colspan="2" align="left"><span>Autenticazione su server LDAP</span></td>
  </tr>  
  </logic:equal>

<html:errors/>



<div id="login"> 
	 <html:form action="/logon.do" focus="username">


<p>
<label>
Nome utente
<br/>
<html:text styleId="username" property="username" size="18" maxlength="32"/>
</label>
</p>
<p>
<label>
Password
<br/>
<html:password styleId="password" property="password" size="18" maxlength="20" redisplay="false" />
</label>
</p>
<p class="forgetmenot">
<logic:notEmpty scope="request" name="mostra_forzatura">
     
      <label for="forzatura"><bean:message key="prompt.forzatura"/></label>:
      <html:checkbox styleId="forzatura" property="forzatura" />
  </logic:notEmpty>
</p>
<br>
<p class="submit">
<html:submit styleClass="submit" property="login" value="Accedi"/>
      <html:reset styleClass="submit" value="Ricomincia" />
</p>


</html:form>
</div>
<p id="nav">Powered by <a href="http://http://www.cswservizi.com">CSW</a></p>

</html:html>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/eprot.tld" prefix="eprot" %>

<html:xhtml />
<label for="data_soppressione">
<html:text property="data_soppressione" styleId="data_soppressione" size="10" maxlength="10" />
<eprot:calendar textField="data_soppressione" />
</label>

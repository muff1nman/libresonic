<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="iso-8859-1" %>
<%--@elvariable id="command" type="org.libresonic.player.command.DatabaseSettingsCommand"--%>

<html>
<head>
    <%@ include file="head.jsp" %>
    <%@ include file="jquery.jsp" %>
</head>
<body class="mainframe bgcolor1">
<script type="text/javascript" src="<c:url value="/script/wz_tooltip.js"/>"></script>
<script type="text/javascript" src="<c:url value="/script/tip_balloon.js"/>"></script>

<c:import url="settingsHeader.jsp">
    <c:param name="cat" value="database"/>
    <c:param name="toast" value="${settings_toast}"/>
</c:import>

<form:form commandName="command" action="databaseSettings.view" method="post">


    <table style="white-space:nowrap" class="indent">
        <tr>
            <td><fmt:message key="databasesettings.configtype"/></td>
            <td>
                <form:select path="configType" cssStyle="width:8em">
                    <form:option value="LEGACY" label="Legacy" />
                    <form:option value="EMBED" label="Embedded JDBC" />
                    <form:option value="JNDI" label="JNDI" />
                </form:select>
                <c:import url="helpToolTip.jsp"><c:param name="topic" value="databaseConfigType"/></c:import>
            </td>
        </tr>
    </table>

    <p>
        <input type="submit" value="<fmt:message key="common.save"/>" style="margin-right:0.3em">
        <input type="button" value="<fmt:message key="common.cancel"/>" onclick="location.href='nowPlaying.view'">
    </p>

</form:form>

</body>
</html>
<table id="generalDatabaseOptions" style="white-space:nowrap" class="indent">
    <tr>
        <td><fmt:message key="databasesettings.mysqlvarcharmaxlength"/></td>
        <td>
            <form:input path="mysqlVarcharMaxlength" size="8"/>
            <c:import url="helpToolTip.jsp"><c:param name="topic" value="mysqlvarcharmaxlength"/></c:import>
        </td>
    </tr>
    <tr>
        <td><fmt:message key="databasesettings.usertablequote"/></td>
        <td>
            <form:input path="usertableQuote" size="1" htmlEscape="true"/>
            <c:import url="helpToolTip.jsp"><c:param name="topic" value="usertablequote"/></c:import>
        </td>
    </tr>
</table>

<p class="warning"><fmt:message key="databasesettings.jdbclibrary"/></p>

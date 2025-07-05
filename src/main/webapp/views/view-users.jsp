<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>
    <head>
        <title>All Users</title>
        <%@include file="./header.jsp" %>
    </head>
    <script>
        $(document).ready(function() {
            $('#view-users-link').addClass("active");
            $('#reg-link').removeClass("active");
        });
    </script>
    <body>
        <%@include file="./navbar.jsp" %>
        <s:if test="#session.downloadError == true">
            <script>
                $(document).ready(function() {
                    alert('<s:property value="#session.message" />');
                });
            </script>
            <%-- Clear session after displaying message --%>
            <%
                session.removeAttribute("downloadError");
                session.removeAttribute("message");
            %>
        </s:if>

        <div class="container">
            <div class="table-container">
                <h1>View Users</h1>
                <div class="search-box">
                    <span id="download">
                        <img src="${pageContext.request.contextPath}/images/download.svg" alt="download" />
                        Download All
                    </span>
                    <input type="text" id="searchText" placeholder="Search here" style="width:250px" />
                </div>

                <table border="1">
                    <thead>
                        <tr>
                            <th>User ID</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Email Id</th>
                            <th>DOB</th>
                            <th>GENDER</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="userTable">
                        <s:if test="users == null || users.size() == 0">
                            <tr id="no-data-row">
                                <td colspan="7"><span class="no-data">No Data Available</span></td>
                            </tr>
                        </s:if>

                        <s:iterator value="users" var="user">
                            <tr>
                                <td><s:property value="#user.userId" /></td>
                                <td><s:property value="#user.firstName" /></td>
                                <td><s:property value="#user.lastName" /></td>
                                <td><s:property value="#user.email" /></td>
                                <td>
                                    <s:date name="#user.dob" format="dd-MM-yyyy"/>
                                </td>
                                <td>
                                    <s:if test="#user.gender == 77">Male</s:if>
                                    <s:elseif test="#user.gender == 70">Female</s:elseif>
                                    <s:elseif test="#user.gender == 79">Other</s:elseif>
                                    <s:else>Not Specified</s:else>
                                </td>
                                <td>
                                    <a class="action-btn" href="edit-user?userid=<s:property value="#user.userId" />">Update</a>

                                    <a class="action-btn rm-btn" href="#"
                                        onclick="removeUser('<s:property value="#user.userId"/>',
                                        '<s:property value="#user.firstName"/> <s:property value="#user.lastName"/>')">
                                        Remove
                                    </a>
                                </td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>

                <s:if test="users != null && users.size() > 0">
                    <div class="pagination">
                        <s:url var="firstPage" action="view-users">
                            <s:param name="page" value="1"/>
                        </s:url>
                        <a href="${firstPage}">&laquo; First</a>

                        <s:iterator begin="0" end="%{totalPages - 1}" var="i">
                            <s:url var="pageUrl" action="view-users">
                                <s:param name="page" value="%{#i + 1}" />
                            </s:url>
                            <a href="${pageUrl}" class="<s:if test="#i == currentPage - 1">active</s:if>">
                                <s:property value="%{#i + 1}" />
                            </a>
                        </s:iterator>

                        <s:url var="lastPage" action="view-users">
                            <s:param name="page" value="%{totalPages}" />
                        </s:url>
                        <a href="${lastPage}">Last &raquo;</a>
                    </div>
                </s:if>

            </div>
        </div>

        <%@include file="./footer.jsp" %>
    </body>
</html>

<%@page import="java.time.LocalDate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="com.register.models.User"%>
<%@ page isELIgnored="false" %>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>User Details</title>
		<meta name='viewport' content='width=device-width, initial-scale=1'>
		<link rel='stylesheet' type='text/css' media='screen' href='./stylesheet/main.css'>
		<link rel='stylesheet' type='text/css' media='screen' href='./stylesheet/table.css'>
		<!-- jQuery Library -->
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
		<!-- jQuery UI CSS -->
		<link rel="stylesheet" href="https://code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
		<!-- jQuery UI Library -->
		<script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>		<script src='./script/main.js'></script>
		<script src='./script/table.js'></script>
	</head>
	<body>
	
		<%@include file="./navbar.jsp" %>
	
		<%
			User user = (User)request.getAttribute("user");
			/* User user = new User();
			user.setUserId(1);
			user.setFirstName("John");
			user.setLastName("Doe");
			user.setEmail("johndoe@gmail.com");
			user.setDob(LocalDate.now()); */
			System.out.println("User in jsp >> "+user);
		%>

		<main class="container">
			<h1>Registration Successful</h1>
			<h2>Details of ${user.firstName}</h2>
			
			<table id="user-det-tbl">
			  <tbody>
			    <tr>
				  <td class="det-td" scope="col">Id</td>
				  <td class="det-td" ><c:out value="${user.userId}" /></td>
			    </tr>
			    <tr>
			      <td class="det-td"  scope="col">First Name</td>
			      <td class="det-td" ><c:out value="${user.firstName}" /></td>
				</tr>
				<tr>
					<td class="det-td"  scope="col">Last Name</td>
					<td class="det-td" ><c:out value="${user.lastName}" /></td>
				</tr>
				<tr>
					<td class="det-td"  scope="col">Email Id</td>
					<td class="det-td" > <c:out value="${user.email}" /></td>
				</tr>
				<tr>
					<td class="det-td"  scope="col">DOB</td>
					<td class="det-td" >
						<fmt:parseDate  value="${user.dob}"  type="date" pattern="yyyy-MM-dd" var="parsedDate" />
						<fmt:formatDate value="${parsedDate}" type="date" pattern="dd-MM-yyyy" />
					</td>
				</tr>
				<tr>
					<td class="det-td"  scope="col">Gender</td>
					<td class="det-td">
					    <c:choose>
					        <c:when test="${user.gender == 77}">Male</c:when>
					        <c:when test="${user.gender == 70}">Female</c:when>
					        <c:when test="${user.gender == 79}">Other</c:when>
					        <c:otherwise>Not Specified</c:otherwise>
					    </c:choose>
					</td>
				</tr>
			  </tbody>
			</table>
			
			<%-- <a id="edit-btn" href="EditUserDetailsServlet?username=<%=user.getUserId()%>">Edit Details</a> --%>
		</main>
	</body>
</html>


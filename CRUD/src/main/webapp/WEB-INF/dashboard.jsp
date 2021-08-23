<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
   <!-- c:out ; c:forEach ; c:if -->
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
   <!-- Formatting (like dates) -->
 <%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
   <!-- form:form -->
 <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
   <!-- for rendering errors on PUT routes -->
 <%@ page isErrorPage="true" %>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Title Here</title>
  <!-- Bootstrap -->
  <link rel="stylesheet"
    href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
    integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
    crossorigin="anonymous">
</head>
<body>
    <div class="container"> <!-- Beginning of Container -->
        <h1>Welcome ${user.firstName} ${user.lastName}</h1>
        <form action="/logout">
        	<button class="btn-danger">LogOut</button>       
        </form>
        
                <table class="table">
        	<thead>
        		<tr>
        			<th scope="col">Idea:</th>
        			<th scope="col">Created By:</th>
        			<th scope="col">Actions</th>
        		</tr>
        	</thead>
        	<tbody>
        		<c:forEach items="${allIdeas}" var="i">
        		<tr>
        				<td><a href="/oneIdea/${i.id}">${i.content}</a></td>
        				
        				<td> <c:out value="${i.user.firstName}"/> </td>
        				        
        			<c:choose>
        				<c:when test="${i.user.id == user.id}">
        				<td>
        					<a href="/editIdea/${i.id}">Edit</a>
        					<a href="/idea/${i.id}/delete">Delete</a>
        				</td>
        				</c:when>
        				<c:otherwise>
        				<td> Sorry - This isn't your idea</td>
        				</c:otherwise>
        			</c:choose>
        		</tr>
        	</c:forEach>
        	</tbody>        
        </table> 
        <a href="/newIdea">New Idea</a>
    </div> <!-- End of Container -->
</body>
</html>
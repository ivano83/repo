<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:url var="url" value="/search-mime-res" />
<form:form action="${url}" method="post" modelAttribute="mimeForm">
	<table>
		<tr>
			<td><form:label path="text">Testo</form:label></td>
			<td><form:input path="text" /></td>
			<td><form:errors path="text" cssClass="error" /></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="Cerca" /></td>
		</tr>
	</table>
</form:form>
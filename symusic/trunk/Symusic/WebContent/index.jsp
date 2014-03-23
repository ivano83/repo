<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<jsp:include page="jsp/head_common.jsp"/>
	
	<title>SYMUSIC - Facilita la ricerca delle release musicali</title>
	
	<script type="text/javascript">
		function checkDataRange() {
			var da = document.getElementById("annoDa");
			var a = document.getElementById("annoAl");
			var daValue = da.options[da.selectedIndex].value;
			var aValue = a.options[a.selectedIndex].value;
			if(aValue<daValue) {
				da.selectedIndex = a.selectedIndex;
			}

		}
	</script>
</head>
<body>
<h2>S Y M U S I C</h2>

<form action="ZeroDayMusicServlet"> 

	<table>
		<tr>
			<td>
				<div>Fonte Release</div>
			</td>
			<td>
				<select name="site">
					<option value="1">0DayMusic</option>
					<option value="2" selected="selected">0DayMp3</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<div>Genere Release</div>
			</td>
			<td>
				<select name="genre">
					<option value="dance" selected="selected">Dance</option>
					<option value="trance">Trance</option>
					<option value="house">House</option>
					<option value="0day-mp3">0day-mp3</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<div>Data inizio </div>
			</td>
			<td>
				<input type="text" name="initDate" class="tcal">
			</td>
		</tr>
		<tr>
			<td>
				<div>Data Fine </div>
			</td>
			<td>
				<input type="text" name="endDate" class="tcal">
			</td>
		</tr>
		<tr>
			<td>
				<div>Estrai dati da Beatport</div>

			</td>
			<td>
				<input type="checkbox" name="enableBeatport" value="true"/>
			</td>
		</tr>
		<tr>
			<td>
				<div>Ignora Release Radio/Sat RIP</div>
			</td>
			<td>
				<input type="checkbox" name="excludeRelaseRip" value="true" checked="checked" />
			</td>
		</tr>
		<tr>
			<td>
				<div>Ignora Release Various Artist (VA)</div>
			</td>
			<td>
				<input type="checkbox" name="excludeVA" value="true" checked="checked" />
			</td>
		</tr>
		<tr>
			<td>
				<div>Range Data</div>
			</td>
			<td>
				<jsp:useBean id="date" class="java.util.Date" />
				<fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />
				Dal <select id="annoDa" name="annoDa" onchange="javascript:checkDataRange();">
				<c:forEach items="${listaAnni}" var="anno">
					<c:choose>
						<c:when test="${anno==currentYear}">
							<option value="${anno}" selected="selected">${anno}</option>
						</c:when>
						<c:otherwise>
							<option value="${anno}">${anno}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</select>
				Al <select id="annoAl" name="annoAl" onchange="javascript:checkDataRange();">
				<c:forEach items="${listaAnni}" var="anno">
					<c:choose>
						<c:when test="${anno==currentYear}">
							<option value="${anno}" selected="selected">${anno}</option>
						</c:when>
						<c:otherwise>
							<option value="${anno}">${anno}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</select>
			</td>
		</tr>
	</table>
	<input type="submit" value="Ricerca">
	
</form>

<div style="padding-bottom: 60px"></div>


<form action="ScenelogServlet"> 
	<h3>Ricerca Scenelog</h3>
	<table>

		<tr>
			<td>
				<div>Data inizio </div>
			</td>
			<td>
				<input type="text" name="initDate" class="tcal">
			</td>
		</tr>
		<tr>
			<td>
				<div>Data Fine </div>
			</td>
			<td>
				<input type="text" name="endDate" class="tcal">
			</td>
		</tr>
		<tr>
			<td>
				<div>Ignora Release Radio/Sat RIP</div>
			</td>
			<td>
				<input type="checkbox" name="excludeRelaseRip" value="true" checked="checked" />
			</td>
		</tr>
		<tr>
			<td>
				<div>Generi ricercati</div>
			</td>
			<td>
				<select name="genre" multiple="multiple" size="5" >
					<option value="Hard Dance" selected="selected">Hard Dance</option>
					<option value="Trance">Trance</option>
					<option value="Progressive House">Progressive House</option>
					<option value="House">House</option>
					<option value="Electro House">Electro House</option>
				</select>
			</td>
		</tr>
	</table>
	<input type="submit" value="Ricerca">
	
</form>

<div style="padding-bottom: 60px"></div>

<form action="BeatportServlet"> 
	<h3>Ricerca Beatport</h3>
	<table>
		<tr>
			<td>
				<div>Genere Release</div>
			</td>
			<td>
				<select name="genre">
				  <c:forEach items="${genreList}" var="gen">
        			<option value="${gen}" ${gen == selectedGenre ? 'selected' : ''}>${gen}</option>
    			  </c:forEach>
				</select>
			</td>
		</tr>
	</table>
	<input type="submit" value="Ricerca">
	
</form>



<div style="padding-bottom: 100px"></div>
</body>
</html>

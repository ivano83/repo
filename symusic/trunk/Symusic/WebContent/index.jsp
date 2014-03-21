<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<jsp:include page="jsp/head_common.jsp"/>
	
	<title>SYMUSIC - Facilita la ricerca delle release musicali</title>
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
				Dal <input type="text" name="annoDa" size="5" value="${currentYear}"/> Al <input type="text" name="annoAl" size="5" value="${currentYear}"/>
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

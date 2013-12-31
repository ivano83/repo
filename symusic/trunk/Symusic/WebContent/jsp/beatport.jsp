<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<jsp:include page="head_common.jsp"/>
	
	<title>SYMUSIC - Facilita la ricerca delle release musicali</title>
</head>
<body>
<h2>BEATPORT</h2>

<form action="BeatportServlet"> 

	<table>
		<tr>
			<td>
				<div>Genere Release</div>
			</td>
			<td>
				<select name="genre">
					<option value="dance" selected="selected">Dance</option>
					<option value="trance">Trance</option>
					<option value="house">House</option>
				</select>
			</td>
		</tr>
	</table>
	<input type="submit" value="Ricerca">
	
</form>

<div style="padding-bottom: 60px"></div>

<h3>Ricerca Scenelog</h3>
<form action="ScenelogServlet"> 

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
				<select name="genre" multiple="multiple">
					<option value="dance" selected="selected">Dance</option>
					<option value="trance">Trance</option>
					<option value="house">House</option>
					<option value="electro house">Electro House</option>
				</select>
			</td>
		</tr>
	</table>
	<input type="submit" value="Ricerca">
	
</form>



</body>
</html>

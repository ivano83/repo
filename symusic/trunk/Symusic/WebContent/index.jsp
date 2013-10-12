<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
	</table>
	<input type="submit" value="Ricerca">
	
</form>



</body>
</html>

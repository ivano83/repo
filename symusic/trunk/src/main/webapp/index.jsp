<html>
<head>
	<script type="text/javascript" src="js/tcal.js"></script>
	
	<link rel="stylesheet" href="./styles/symusic.css" type="text/css" media="screen" />
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
	
	<title>SYMUSIC - Facilita la ricerca delle release musicali</title>
</head>
<body>
<h2>Hello World!</h2>

<form action="ZeroDayMusicServlet"> 

	<table>
		<tr>
			<td>
				<div>Fonte Release</div>
			</td>
			<td>
				<select name="site">
					<option value="1" selected="selected">0DayMusic</option>
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
	</table>
	<input type="submit" value="Ricerca">
	
</form>



</body>
</html>

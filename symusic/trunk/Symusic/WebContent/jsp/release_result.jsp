<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<c:import url="head_common.jsp"/>
	<script type="text/javascript">
	  $(document).ready(function(){
	    $(".rating").jRating({
		  step:true,
		  length:5,
		  decimalLength:0 // number of decimal in the rate
		});
	  });
	</script>
	<title>SYMUSIC - Risultati ricerca</title>
</head>
<body>

<h2>Risultati release</h2>

<table class="rel_table">	
	<c:forEach items="${listRelease}" var="item">
		<tr>
			<td width="30%">
				<div>Data: ${item.releaseDate}</div>
				<div class="rel_name">${item.nameWithUnderscore}</div>
				<div>Artista: ${item.artist}</div>
				<div>Titolo release: ${item.song}</div>
				<div class="rating" data-average="${item.voteAverage}" data-id="${item.id}" data-value="${item.voteValue}" disable="${item.voted}"></div>
				<span class="voted voted_${item.id}">${item.voteAverage}/5
				<c:choose>
					<c:when test="${item.voted}">
						<img class="voted_img" src="./img/rating/rated_ok_green.png" alt="THANKS" title="Il tuo voto: ${item.voteValue}/5" />
					</c:when>
				</c:choose>
				</span>
				<div class="rel_link"><a href="${item.id}" target="_blank">Resetta dati</a></div>
			</td>
			<td>
				<ol>
					<c:forEach items="${item.tracks}" var="track">
						<li class="rel_tracks"><span>${track.trackName}</span>
						<span>${track.time}</span>
						<span>${track.bpm}</span>
						<span>${track.genere}</span></li>
					</c:forEach>
				</ol>
			</td>
			<td>
				<div>VIDEO</div>
				<c:forEach items="${item.videos}" var="video">
					<div class="rel_video"><a href="${video.link}" target="_blank">${video.name}</a></div>
				</c:forEach>
				<div>DOWNLOAD</div>
				<c:forEach items="${item.links}" var="link">
					<div class="rel_link"><a href="${link.link}" target="_blank">${link.name}</a></div>
				</c:forEach>
			</td>
		</tr>
	</c:forEach>
	
</table>

<p>
<span class="rel_successive"><a href="${urlSuccessivo}">SUCCESSIVE RELEASE</a></span>
<span class="rel_home"><a href="./">HOME</a></span>
<span class="rel_precedenti"><a href="${urlPrecedente}">PRECEDENTI RELEASE</a></span>
</p>

</body>
<head>
	<meta http-equiv="Pragma" content="no-cache">
</head>
</html>
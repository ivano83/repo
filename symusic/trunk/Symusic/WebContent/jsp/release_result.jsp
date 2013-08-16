<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<jsp:include page="head_common.jsp" />
	<title>SYMUSIC - Risultati ricerca</title>
</head>
<body>

<h2>Risultati release</h2>

<table class="rel_table">	
	<c:forEach items="${listRelease}" var="item">
		<tr>
			<td>
				<div class="rel_name">${item.nameWithUnderscore}</div>
				<div>Artista: ${item.artist}</div>
				<div>Titolo release: ${item.song}</div>
			</td>
			<td>
				<div>${item.releaseDate}</div>
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
					<div><a href="${video.link}" target="_blank">${video.name}</a></div>
				</c:forEach>
				<div>DOWNLOAD</div>
				<c:forEach items="${item.links}" var="link">
					<div><a href="${link.link}" target="_blank">${link.link}</a></div>
				</c:forEach>
			</td>
		</tr>
	</c:forEach>
	
</table>


</body>
</html>
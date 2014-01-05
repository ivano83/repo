<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
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

<script type="text/javascript">
	function delrel(idBox) {
		 $.get('ReleaseDeleteServlet',{id:idBox},function(responseText) { 
             $('.del_'+idBox).html('<div>I dati della release sono stati eliminati!</div>');
             $('.rel_link_'+idBox).html('');
             $('.rel_track_all_'+idBox).html('');
             $('.rel_video_'+idBox).html('');
             $('.artist_'+idBox).html('');
             $('.title_'+idBox).html('');
     });
	 
	}
	function releaseOption(idRel,option,optionName,color) {
			 $.get('FlagReleaseServlet',{idRelease:idRel,optionType:option,optionName:optionName,color:color},function(responseText) { 
	             $('.status_'+idRel).html('<span>'+optionName+'</span>');
	             $('.row_'+idRel).style.backgroundColor="#EEE8AA";
	     });
	}

	</script>

	<title>SYMUSIC - Risultati ricerca</title>
</head>
<body>

<h2>Risultati: ${fn:length(listRelease)} release trovate</h2>

<table class="rel_table">	
	<c:forEach items="${listRelease}" var="item">
		<tr class="row_${item.id}">
			<td width="30%">
				<div>Data: ${item.releaseDate}</div>
				<c:choose>
				  <c:when test="${empty item.links}"><div class="rel_name" style="background-color:#ffff77">${item.nameWithUnderscore}</div></c:when>
				  <c:otherwise><div class="rel_name">${item.nameWithUnderscore}</div></c:otherwise>
				</c:choose>
				<div>Artista: <span class="artist_${item.id}">${item.artist}</span></div>
				<div>Titolo release: <span class="title_${item.id}">${item.song}</span></div>
				<div class="rating" data-average="${item.voteAverage}" data-id="${item.id}" data-value="${item.voteValue}" disable="${item.voted}"></div>
				<span class="voted voted_${item.id}">${item.voteAverage}/5
				<c:choose>
					<c:when test="${item.voted}">
						<img class="voted_img" src="./img/rating/rated_ok_green.png" alt="THANKS" title="Il tuo voto: ${item.voteValue}/5" />
					</c:when>
				</c:choose>
				</span>
				<div class="delrel" data-id="${item.id}">
					<span class="rel_link del_${item.id}"><a href="#" onclick="javascript:delrel(${item.id})">Resetta dati</a></span>
				</div>
				<div class="rel_status status_${item.id}">
				<c:choose>
					<c:when test="${item.releaseFlag.downloaded}">
						<span>SCARICATO</span>
					</c:when>
					<c:when test="${item.releaseFlag.preview}">
						<span>VISIONATO</span>
					</c:when>
					<c:otherwise>
						<span>NON VISIONATO</span>
					</c:otherwise>
				</c:choose>
				</div>
			</td>
			<td>
			
				<table class="table_track rel_track_all_${item.id}">
					<tr class="table_track_head">
						<td><strong>Track Name</strong></td>
						<td><strong>Time</strong></td>
						<td><strong>BPM</strong></td>
						<td><strong>Genere</strong></td>
					</tr>
					<c:forEach items="${item.tracks}" var="track">
					<tr class="rel_tracks rel_track_${item.id}">
						<td><span>${track.trackNumber}.&nbsp;${track.trackName}</span></td>
						<td><span>${track.time}</span></td>
						<td><span>${track.bpm}</span></td>
						<td><span>${track.genere}</span></td>
					</tr>
					</c:forEach>
				</table>
			</td>
			<td>
				<div>VIDEO</div>
				<c:forEach items="${item.videos}" var="video">
					<div class="rel_video rel_video_${item.id}"><a href="${video.link}" target="_blank" onclick="javascript:releaseOption(${item.id},1,'VISIONATO','#EEE8AA')">${video.name}</a></div>
				</c:forEach>
				<div>DOWNLOAD</div>
				<c:forEach items="${item.links}" var="link" varStatus="status">
					<div class="rel_link rel_link_${item.id}"><a name="link_copy_class" rel-id="${item.id}" id="link_copy_${item.id}_${status.count}" href="${link.link}" target="_blank" onclick="javascript:releaseOption(${item.id},2,'SCARICATO','#EEE8AA'); return false;" data-clipboard-text="${link.link}">${link.name}</a><span id="link_copy_${item.id}" style="display: none">${link.link}</span></div>
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

    <script type="text/javascript">
      var clip = new ZeroClipboard( );
      var listLinks = document.getElementsByName('link_copy_class');
      var i;
      for(i = 0; i < listLinks.length; ++i) {
    	  clip.glue(document.getElementById(listLinks[i].id));
      }
      clip.on( 'complete', function(client, args) {
         
          var i, el;
          var listLinks = document.getElementsByName('link_copy_class');
          alert('listLinks '+listLinks.length);
          for(i = 0; i < listLinks.length; ++i) {
        	  el = document.getElementById(listLinks[i].id);
        	  
        	  if(args.text == el.getAttribute('href')) {
        		  alert(args.text+' == '+el.getAttribute('href'));
        		  releaseOption(el.getAttribute('rel-id'),2,'SCARICATO','#EEE8AA');
              };
          };
          
        } );
    </script>
<head>
	<meta http-equiv="Pragma" content="no-cache">
</head>
</html>
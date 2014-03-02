<!doctype html>
<html>
<head> 
<?php  include('common/head.php'); ?>
</head>
<body onload="javascript:evidenziaMenu('m_home');">
<div class="wrapper">
	<header>
		<?php  include('common/header.php'); ?>
		<div class="clearfloat"></div>
		<div class="pageTitle">
        	<h3>Ivano & Cristina - 11 settembre 2014</h3>
        </div>
    </header>
    
    <div class="welcome">
        <h1>Benvenuti</h1>
        <div class="picBorder leftPic"><img class="img_custom" src="images/ivano_cristina_home.jpg" alt="welcome" width="80"  ></div>
        <p>
            11 Settembre 2014... a quanto pare è il giorno in cui <strong>ci sposiamo</strong>.<br>
        </p>
        <p>
        	Finalmente abbiamo deciso di fare il grande passo, e vogliamo condividere la nostra gioia con voi.
        </p>
        <p>
        	Abbiamo creato questo sito per farvi avere alcune informazioni utili sull'evento.<br>
        	Scorrendo tra le varie sezioni potete trovare tutte le informazioni riguardanti la celebrazione del matrimonio, la location e tutto il programma della giornata, nonchè curiosità sui preparativi e sul viaggio di nozze.
        </p>
        <div class="clearfloat"></div>
    </div>
    <div class="latestNews">
    	<h2>Ultime News</h2>
    	<div class="latestNews_inner">
        <ul>
       		<?php 
       		include("Conn.php");
       		
       		$html = "";
       		$sql="SELECT news,date FROM news order by date desc";
       		$result=mysql_query($sql);
       		while($r=mysql_fetch_array($result)){
				$html .= '<li><span>' . $r['date'] . '</span>';
				$html .= '<p>' . $r['news'] . '</p></li>';
			}
			echo $html;
        	?>
            
        </ul>
        </div>
    </div>
    <div class="clearfloat"></div>
</div>
<footer>
<?php  include('common/footer.php'); ?>
</footer>
<script src="js/script.js"></script>
</body>
</html>

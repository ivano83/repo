<!doctype html>
<html>
<head> 
<?php  include('common/head.php'); ?>
</head>
<body onload="javascript:evidenziaMenu('m_home');">
<div class="wrapper">
	<header>
		<?php
			include('lock.php');
		?>
		<div class="logoContainer">
            <span><a href=""><img class="image_title" alt="Ivano &amp; Cristina" src="images/header_title.png" height="60px" /></a></span>
            <span class="logoutContainer">
	        	<span>Benvenuto <b><?php echo $_SESSION['login_user'] ?> </b></span>
	        	<form id="loginform" name="loginform" method="post" action="logout.php">
	    		<span><input type="submit" name="loginbtn" class="flatbtn-logout hidemodal" value="Logout" ></span>
				</form>
        	</span>
        </div>
        <div class="clearfloat"></div>
        <div id="countdown"></div>
		<div class="clearfloat"></div>
		<div class="pageTitle">
        	<h3>Ivano & Cristina - 11 settembre 2014</h3>
        </div>
    </header>
    
    <div class="welcome">
        <h1>ACCESS LOG</h1>
        <table border="1" cellspacing="0" >
        	<tr>
        		<th>NOME</th>
	        	<th>DATA LOGIN</th>
	        	<th>ACCESSO</th>
	        	<th>PASSWORD DIGITATA</th>
        	</tr>
       		<?php 
       		include("Conn.php");
       		
       		$html = "";
       		$sql="SELECT * FROM user_log order by login_date desc";
       		$result=mysql_query($sql);
       		while($r=mysql_fetch_array($result)){
				$html .= '<tr><td style="padding:3px">' . $r['name'] .'</td>';
				$html .= '<td style="padding:3px">' . $r['login_date'] . '</td>';
				$html .= '<td style="padding:3px">' . $r['login_success'] . '</td>';
				$html .= '<td style="padding:3px">' . $r['digit_password'] . '</td></tr>';
			}
			echo $html;
        	?>
            
        </table>
        <div class="clearfloat"></div>
    </div>
    <div class="latestNews">
    	<h2>MESSAGGI RICEVUTI</h2>
    	<div class="latestNews_inner">
        <ul>
       		<?php 
       		include("Conn.php");
       		
       		$html = "";
       		$sql="SELECT nome, messaggio,email, data FROM message order by data desc";
       		$result=mysql_query($sql);
       		while($r=mysql_fetch_array($result)){
				$html .= '<li><span>' . $r['nome'] .' [' . $r['data'] . ']</span><br>';
				$html .= '<span>' . $r['email'] . '</span>';
				$html .= '<p>' . $r['messaggio'] . '</p></li>';
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

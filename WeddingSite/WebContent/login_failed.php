<!doctype html>

<html>
<head>
<?php  include('common/head.php'); ?>
</head>
<body>
<div class="wrapper">
	<div class="logoContainer">
        <div class="logo"><a href="">Ivano & Cristina</a></div>
        <div class="slogan">Il matrimonio</div>
    </div>
    <div class="clearfloat"></div>
    
     <div id="loginfailed" style="display:block">
    	<div class="sliderSlogan"><h2>Attenzione! Accesso non autorizzato.</h2></div>
    	<form id="loginform" name="loginform" method="post" action="index.php">
    	<center><input type="submit" name="loginbtn" id="modaltrigger" class="flatbtn-blu hidemodal" value="Login" ></center>
		</form>
	</div>
</div>
<footer id="includedFooter" style="bottom:0;">
<?php  include('common/footer.php'); ?>
</footer>
</body>
</html>

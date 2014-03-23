<!doctype html>

<html>
<head>
<?php  include('common/head.php'); ?>
</head>
<body>
<div class="wrapper" style="width:460px;">
	<div class="logoContainer" style="text-align: center;">
        <span class="logo"><a href=""><img class="image_title" alt="Ivano &amp; Cristina" src="images/header_title.png" height="60px" /></a></span>
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

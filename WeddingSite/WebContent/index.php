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
    
	<div id="loginmodal" >
		<h1 style="text-align: center;">Accedi</h1>
		<form id="loginform" name="loginform" method="post" action="Login.php">
			<label for="name">Chi sei &nbsp;&nbsp;&nbsp;<a href="#" onclick="javascript:alert('Se ti va puoi scrivere il tuo nome (e cognome), in questo modo ho il piacere di sapere chi ha visitato il sito.');">?</a></label> <input type="text" name="name" id="name" class="txtfield" tabindex="1"  maxlength="65">
			<label for="password">Password:</label> <input type="password" name="password" id="password" class="txtfield" tabindex="2" maxlength="65">
			<input type="hidden" name="username" value="Visitatore"/>
			<div class="center">
			<input type="submit" name="loginbtn" id="loginbtn" class="flatbtn-blu hidemodal" value="Login" tabindex="3">
			</div>
		</form>
	</div>
</div>

<footer id="footer" style="bottom:0;">
<?php  include('common/footer.php'); ?>
</footer>
</body>
</html>

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
    
	<div id="loginmodal" style="display: block;">
		<h1 style="text-align: center;">Accedi</h1>
		<form id="loginform" name="loginform" method="post" action="Login.php">
			<label for="name">Chi sei &nbsp;&nbsp;&nbsp;<a href="#" onclick="javascript:alert('Se ti va puoi scrivere il tuo nome (e cognome), in questo modo ho il piacere di sapere chi ha visitato il sito.');">?</a></label> <input type="text" name="name" id="username" class="txtfield" tabindex="1">
			<label for="password">Password:</label> <input type="password" name="password" id="password" class="txtfield" tabindex="2">
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

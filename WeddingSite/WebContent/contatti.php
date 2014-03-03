<!doctype html>
<html>
<head>
<?php  include('common/head.php'); ?>

<script type="text/javascript">
function validateMessageInsert() {
	var form = document.sendMess;
	var name = form.name.value;
	var mail = form.email.value;
	var message = form.message.value;
	var errori = document.getElementById("errori");
	var res = true;
	if(name == "Nome:" || name.trim() == "") {
		var elem = document.createElement("li");
		elem.innerHTML = "Nome obbligatorio";
		errori.appendChild(elem);
		res = false;
	}
	if(mail == "E-mail:" || mail.trim() == "") {
		var elem = document.createElement("li");
		elem.innerHTML = "E-mail obbligatoria";
		errori.appendChild(elem);
		res = false;
	}
	if(message == "Messaggio:" || message.trim() == "") {
		var elem = document.createElement("li");
		elem.innerHTML = "Messaggio obbligatorio";
		errori.appendChild(elem);
		res = false;
	}
	return res;
}
</script>
</head>
<body onload="javascript:evidenziaMenu('m_contatti');">
<div class="wrapper">
	<header>
    	<?php  include('common/header.php'); ?>
        <div class="clearfloat"></div>
        <div class="pageTitle">
        	<h3>Contact</h3>
        </div>
    </header>
    <div class="welcome contact">
 <?php 
	if(!empty($_GET['inv'])) {
		echo "<script>alert('Grazie per averci scritto. Buon proseguimento!'); </script>";
	}

?>
        <h2>Contact Info</h2>
        <iframe class="map" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="https://maps.google.com/maps?f=q&amp;source=s_q&amp;hl=en&amp;geocode=&amp;q=1401+Southwest+15th+Street+Pompano+Beach,+FL+33069&amp;aq=&amp;sll=26.21114,-80.142404&amp;sspn=0.002041,0.004128&amp;t=h&amp;ie=UTF8&amp;hq=&amp;hnear=1401+SW+15th+St,+Pompano+Beach,+Florida+33069&amp;ll=26.211111,-80.142383&amp;spn=0.008163,0.016512&amp;z=14&amp;output=embed"></iframe><br /><small><a href="https://maps.google.com/maps?f=q&amp;source=embed&amp;hl=en&amp;geocode=&amp;q=1401+Southwest+15th+Street+Pompano+Beach,+FL+33069&amp;aq=&amp;sll=26.21114,-80.142404&amp;sspn=0.002041,0.004128&amp;t=h&amp;ie=UTF8&amp;hq=&amp;hnear=1401+SW+15th+St,+Pompano+Beach,+Florida+33069&amp;ll=26.211111,-80.142383&amp;spn=0.008163,0.016512&amp;z=14" style="color:#0000FF;text-align:left">View Larger Map</a></small>
        <p>
        	1401 SW 15th St<br>Pompano Beach, FL 33069
        </p>
        <p>
        	Telefone:             +1 500 923 3321<br>
            FAX:                      +1 500 239 2211<br>
            E-mail: mail@yourwebsite.com 
        </p>
    </div>
    <div class="contactForm">
    	<h2>Contattaci</h2>
        <form method="post" name="sendMess" class="" action="Save_message.php">
	        <div>
	        	<ul id="errori" ></ul>
	        </div>
            <input type="text" id="name" name="name" value="Nome:" class="inputContact" onFocus="if(this.value==this.defaultValue)this.value='';" onBlur="if(this.value=='')this.value=this.defaultValue;">
            <input type="text" id="email" name="email" value="E-mail:" class="inputContact" onFocus="if(this.value==this.defaultValue)this.value='';" onBlur="if(this.value=='')this.value=this.defaultValue;">
            <textarea name="message" cols="" rows="" class="textareaContact" onFocus="if(this.value==this.defaultValue)this.value='';" onBlur="if(this.value=='')this.value=this.defaultValue;">Messaggio:</textarea>
            <input type="reset" name="loginbtn" id="loginbtn" class="flatbtn-blu hidemodal" value="Pulisci" tabindex="2">
            <input type="submit" name="loginbtn" id="loginbtn" class="flatbtn-blu hidemodal" value="Invia" tabindex="3" onclick="javascript: return validateMessageInsert();">
        </form>
    </div>
    <div class="clearfloat"></div>
</div>
<footer>
<?php  include('common/footer.php'); ?>
</footer>
</body>
</html>

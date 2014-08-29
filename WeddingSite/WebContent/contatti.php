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
	errori.innerHTML = "";
	var res = true;
	if(name == "Nome:" || name.trim() == "") {
		var elem = document.createElement("li");
		elem.innerHTML = "Nome obbligatorio";
		errori.appendChild(elem);
		res = false;
	}
// 	if(mail == "E-mail:" || mail.trim() == "") {
// 		var elem = document.createElement("li");
// 		elem.innerHTML = "E-mail obbligatoria";
// 		errori.appendChild(elem);
// 		res = false;
// 	}
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
        	<h3>Contatti</h3>
        </div>
    </header>
    <div class="welcome contact">
 <?php 
	if(!empty($_GET['inv'])) {
		echo "<script>alert('Grazie per averci scritto. Buon proseguimento!'); </script>";
	}
 ?>
        <h2>Informazioni utili</h2>
        <p>
        	<span class="strong">Partenza dello sposo</span><br>
        	<span>Ore 16:15 circa</span><br>
        	<span>Via Corrado del Greco, 90 - Roma</span>
        </p>
        <p>
        	<span class="strong">Partenza della sposa</span><br>
        	<span>Ore 16:30 circa</span><br>
        	<span>Via Corrado del Greco, 90 - Roma</span>
        </p>
        <p>
        	<span class="strong">Destinazione: Il Castello Borghese</span><br>
        	<span>Dalle ore 18:00</span><br>
        	<span>Via dell'Olmata, 99 - Nettuno (Rm)</span>
        </p>
        <p>
        	<span class="strong">Celebrazione del Rito</span><br>
        	<span>Ore 18:30</span><br>
        	<span>Via dell'Olmata, 99 - Nettuno (Rm)</span>
        </p>
        <p>
        	<span class="strong">Rinfresco, Cena e Proseguimento di Serata</span><br>
        	<span>Dalle ore 19:00 in poi</span><br>
        	<span>Via dell'Olmata, 99 - Nettuno (Rm)</span>
        </p>
    </div>
    <div class="contactForm">
    	<h2>Contattaci</h2>
        <form method="post" name="sendMess" class="" action="Save_message.php">
        	<div>Se volete chiederci qualcosa, confermare la vostra presenza, lasciare un commento o un semplice saluto... questa è la sezione giusta! </div>
	        <div>
	        	<ul id="errori" ></ul>
	        </div>
            <div><input type="text" id="name" name="name" value="Nome:" class="inputContact" onFocus="if(this.value==this.defaultValue)this.value='';" onBlur="if(this.value=='')this.value=this.defaultValue;"></div>
            <div><input type="text" id="email" name="email" value="E-mail:" class="inputContact" onFocus="if(this.value==this.defaultValue)this.value='';" onBlur="if(this.value=='')this.value=this.defaultValue;"></div>
            <div><textarea name="message" cols="" rows="" class="textareaContact" onFocus="if(this.value==this.defaultValue)this.value='';" onBlur="if(this.value=='')this.value=this.defaultValue;">Messaggio:</textarea></div>
            <div>
            	<input type="reset" name="loginbtn" id="loginbtn" class="flatbtn-blu hidemodal" value="Cancella" tabindex="2">
            	<input type="submit" name="loginbtn" id="loginbtn" class="flatbtn-blu hidemodal" value="Invia" tabindex="3" onclick="javascript: return validateMessageInsert();">
            </div>
        </form>
    </div>
    <div class="clearfloat"></div>
</div>
<footer>
<?php  include('common/footer.php'); ?>
</footer>
</body>
</html>

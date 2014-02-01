<!doctype html>
<!--
Designed by: http://www.cssing.org
Released for free under a Creative Commons Attribution 3.0 License: http://creativecommons.org/licenses/by/3.0/
Name: Portfolio
Description:  A two-columns, responsive design template.
Template number: 16
Version: 1.0
Released: 4.3.13
-->
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Portfolio</title>
<link href='http://fonts.googleapis.com/css?family=Yesteryear' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Salsa' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Karla' rel='stylesheet' type='text/css'>
<link href="css/styles.css" rel="stylesheet" type="text/css">
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
    	<h2>Contact Form</h2>
        <form method="get" class="" action="http://www.free-responsive-templates.com">
            <input type="text" id="name" name="name" value="Name:" class="inputContact" onFocus="if(this.value==this.defaultValue)this.value='';" onBlur="if(this.value=='')this.value=this.defaultValue;">
            <input type="text" id="email" name="email" value="E-mail:" class="inputContact" onFocus="if(this.value==this.defaultValue)this.value='';" onBlur="if(this.value=='')this.value=this.defaultValue;">
            <input type="text" id="phone" name="phone" value="Phone:" class="inputContact" onFocus="if(this.value==this.defaultValue)this.value='';" onBlur="if(this.value=='')this.value=this.defaultValue;">
            <textarea name="" cols="" rows="" class="textareaContact" onFocus="if(this.value==this.defaultValue)this.value='';" onBlur="if(this.value=='')this.value=this.defaultValue;">Message:</textarea>
            <p class="button"><a href="">Clear</a> <a href="">Send</a></p>
        </form>
    </div>
    <div class="clearfloat"></div>
</div>
<footer>
<?php  include('common/footer.php'); ?>
</footer>
</body>
</html>

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
        <nav>
            <ul id="navlist">
                <li class="m_home"><a href="home.php">Home</a></li>
                <li class="m_cerimonia"><a href="cerimonia.php">Cerimonia</a></li>
                <li class="m_location"><a href="location.php">Location</a></li>
                <li class="m_lista_nozze"><a href="lista_nozze.php">Lista Nozze</a></li>
                <li class="m_viaggio"><a href="viaggio.php">Viaggio</a></li>
                <li class="m_contatti"><a href="contatti.php">Contatti</a></li>
            </ul>
        </nav>

        <script type="text/javascript">
			function evidenziaMenu(itemMenu) {
				var menuList = document.getElementById('navlist').getElementsByTagName('li');
				for(var i=0;i<menuList.length;i++) {
					if(menuList[i].className == itemMenu) {
						menuList[i].className = menuList[i].className + ' active';
					}
				}
			}
        </script>
        <script type="application/javascript">
			var myCountdownTest = new Countdown({
								 	year	: 2014,
									month	: 09, 
									day		: 11,
									hour	: 18,
									minute	: 30,
									width	: 250, 
									height	: 40,
									target   : "countdown",    // A reference to an html DIV id (e.g. DIV id="foo")
									rangeHi  : "month", // The highest unit of time to display
									style    : "flip", // flip boring whatever (only "flip" uses image/animation)
									numbers		: 	{
										font 	: "Arial",
										color	: "#FFFFFF",
										bkgd	: "#365D8B",
										rounded	: 0.15,				// percentage of size 
										shadow	: {
													x : 0,			// x offset (in pixels)
													y : 3,			// y offset (in pixels)
													s : 4,			// spread
													c : "#000000",	// color
													a : 0.4			// alpha	// <- no comma on last item!
													}
										} // <- no comma on last item!
									});

			</script>

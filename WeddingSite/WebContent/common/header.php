<?php
include('lock.php');
?>
    	<div class="logoContainer">
            <div class="logo"><a href="">Ivano & Cristina</a></div>
            <div class="slogan">Il matrimonio</div>
        </div>
        <div class="logoutContainer">
        	<form id="loginform" name="loginform" method="post" action="logout.php">
    		<center><input type="submit" name="loginbtn" class="flatbtn-logout hidemodal" value="Logout" ></center>
			</form>
        </div>
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
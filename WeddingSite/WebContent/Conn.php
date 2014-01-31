<?php
//connect to database 
$mysql_hostname = "localhost";
$mysql_user = "root";
$mysql_password = "passw0rd.1";
$mysql_database = "test";
$bd = mysql_connect($mysql_hostname, $mysql_user, $mysql_password)
or die("Connection failed");
mysql_select_db($mysql_database, $bd) or die("MySql operation failed");
?> 

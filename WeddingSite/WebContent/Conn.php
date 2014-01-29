<?php
//connect to database 
$connection = new mysqli("localhost","root","passw0rd.1","test"); 
if(mysqli_connect_errno()){
    printf("Connection failed: %s\n",mysqli_connect_error());   
    exit(); 
}
?> 

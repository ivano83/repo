<?php 
include("Conn.php");

session_start();
if($_SERVER["REQUEST_METHOD"] == "POST")
{
	// username and password sent from Form
	$name=addslashes(trim($_POST['name']));
	$email=addslashes(trim($_POST['email']));
	$message=addslashes(trim($_POST['message']));
	$ip=$_SERVER['REMOTE_ADDR'];
	
	if(empty($name) || empty($email) || empty($message)) {
		header("location: contatti.php");
	}
	
	$sql="INSERT INTO message (nome, email, messaggio, data)  VALUES ('$name', '$email','$message', NOW())";
	$result=mysql_query($sql);
	if(! $result ) {
		die('Could not enter data: ' . mysql_error());
	}
	header("location: contatti.php?inv=1");
}
?>



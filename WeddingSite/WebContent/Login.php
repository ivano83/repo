<?php 
include("Conn.php");

session_start();
if($_SERVER["REQUEST_METHOD"] == "POST")
{
	// username and password sent from Form
	$name=addslashes($_POST['name']);
	$myusername=addslashes($_POST['username']);
	$mypassword=addslashes($_POST['password']);
	$mypassword=sha1($_POST['password']);
	$ip=$_SERVER['REMOTE_ADDR'];

	$sql="SELECT iduser,user FROM user WHERE user='$myusername' and password='$mypassword'";
	$result=mysql_query($sql);
	$row=mysql_fetch_array($result);
	$iduser=$row['iduser'];
	$count=mysql_num_rows($result);

	// If result matched $myusername and $mypassword, table row must be 1 row
	if($count==1)
	{
		$_SESSION['login_user']=$myusername;
		
		$sql="INSERT INTO user_log (name, login_success, login_date, ip_address)  VALUES ('$name', 1, NOW(), '$ip')";
		$result=mysql_query($sql);
		if(! $result )
		{
			die('Could not enter data: ' . mysql_error());
		}
		header("location: home.php");
	}
	else
	{
		$sql="INSERT INTO user_log (name, login_success, login_date, ip_address)  VALUES ('$name', 0, NOW(), '$ip')";
		$result=mysql_query($sql);
		if(! $result )
		{
			die('Could not enter data: ' . mysql_error());
		}
		header("Location:login_failed.html");
	}
}
?>


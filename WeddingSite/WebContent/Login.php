<?php 
include("conn.php");

session_start();
if($_SERVER["REQUEST_METHOD"] == "POST")
{
	// username and password sent from Form
	$myusername=addslashes($_POST['username']);
	$mypassword=addslashes($_POST['password']);

	$sql="SELECT iduser,user FROM test.user WHERE user='$myusername' and password='$mypassword'";
	$result=mysql_query($sql);
	$row=mysql_fetch_array($result);
	$iduser=$row['iduser'];
	$count=mysql_num_rows($result);

	// If result matched $myusername and $mypassword, table row must be 1 row
	if($count==1)
	{
		$_SESSION['login_user']=$myusername;

		header("location: home.php");
	}
	else
	{
		header("Location:login_failed.html");
	}
}
?>






$username=$_POST['username'];
$password=$_POST['password'];
//$password=sha1($_POST['password']); my passwords are hashed in the database using the sha1
$checklogin="SELECT iduser,user FROM test.user WHERE user=? AND password=?";
$query = $connection->prepare($checklogin);
if( ! $eintrag = $connection->prepare( $checklogin ) ) {
  echo 'Error: ' . $connection->error;
  return false; // throw exception, die(), exit, whatever...
} 

$query->bind_param("ss",$username,$password);
$query->execute() or die($connection->error);
$result = $query->bind_result($username, $password);
$count = $result->num_rows;
echo 'count=' . $count;
if($count==1){
	while($row=$result->fetch_assoc)	{
		
		session_regenerate_id();
		$_SESSION['sess_user_id'] = $row['iduser'];
		$_SESSION['sess_username']=$row['user'];
		echo $row['user'];
		session_write_close();
	}
	/* close connection */
	$mysqli->close();
	
	//header("Location:home.php");
}
else {
	/* close connection */
	$mysqli->close();
	
	header("Location:login_failed.html");
}
 ?>

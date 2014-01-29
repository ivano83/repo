<?php 
include("conn.php");
$username=$_POST['username'];
$password=$_POST['password'];
//$password=sha1($_POST['password']); my passwords are hashed in the database using the sha1
echo 'User: ' . $username . 'pass: ' . $password . '\n';
$checklogin="SELECT * FROM test.user WHERE user=? AND password=?";
$query = $connection->prepare($checklogin);
if( ! $eintrag = $connection->prepare( $checklogin ) ) {
  echo 'Error: ' . $connection->error;
  return false; // throw exception, die(), exit, whatever...
} 
$query->bind_param("ss",$username,$password);
$query->execute() or die($connection->error);
$query->bind_result($username, $password);
$query->store_result();
$count = $query->num_rows;
if($count==1){
	while($row=$query->fetch_assoc)	{
		$_SESSION['username']=$row['Username'];
	}
	header("Location:home.html");
}
else {
	header("Location:login_failed.html");
}
 ?>

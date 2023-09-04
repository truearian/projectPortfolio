<?php
  session_start();

  // this info is passed in by the user
  $login = $_POST['login'];
	$password = $_POST['password'];
  $date = date('Y-m-d H:i:s');
 
	$conn = mysqli_connect("localhost", "AdminAccount", "wearetesting", "COP4331");
  
	if (!$conn)
	{
		die("Connection failed: " . mysqli_connect_error());
    //header('Location: ../signup.html');
	}
 
	else
	{
		$checkLogin = $conn->prepare("SELECT ID,FirstName,LastName FROM Users WHERE Login=? AND Password =?");
		$checkLogin->bind_param("ss", $login, $password);
    if($checkLogin->execute()) {
       $curr_user = mysqli_fetch_assoc(mysqli_stmt_get_result($checkLogin));
    }
    
		if($curr_user)
		{
			$update_date_login = $conn->prepare("UPDATE Users SET DateLastLoggedIn = ? where ID = ?");
			$update_date_login->bind_param("ss", $date, $curr_user['ID']);
			$update_date_login->execute();

      header('Location: ../contact.html');
			//returnWithInfo( $row['FirstName'], $row['LastName'], $row['ID'] );
		}
		else
		{
			returnWithError("No Records Found");
		}

		$stmt->close();
		$conn->close();
	}
	
	function getRequestInfo()
	{
		return json_decode(file_get_contents('php://input'), true);
	}

	function sendResultInfoAsJson( $obj )
	{
		header('Content-type: application/json');
		echo $obj;
	}
	
	function returnWithError( $err )
	{
		$retValue = '{"ID":0,"FirstName":"","LastName":"","error":"' . $err . '"}';
		sendResultInfoAsJson( $retValue );
	}
	
	function returnWithInfo( $firstName, $lastName, $id )
	{
		$retValue = '{"ID":' . $id . ',"FirstName":"' . $firstName . '","LastName":"' . $lastName . '","error":""}';
		sendResultInfoAsJson( $retValue );
	}
	
?>
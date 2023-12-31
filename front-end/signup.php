<?php
  include("LAMPAPI\Register.php");
  $messages = array (
    0 => "",
    1 => "Login username already exists. Try again with a different login.",
    2 => "Registration successful!"
  );

  $msg_id = isset($_GET['msg']) ? (int)$_GET['msg'] : 0;
  if ($msg_id != 0 && array_key_exists($msg_id, $messages)) {
      echo $messages[$msg_id];
  }
?>


<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>MemoryMosaics Sign Up</title>
    <link rel="stylesheet" href="css/style.css" />
  </head>
  <body>
    <div class="container">
      <img
        src="images/polaroid.png"
        alt="Polaroid background"
        width="500"
        height="600"
        class="signup-img-background"
      />
      <div class="signup-box">
        <h1 class="signup-header">Memory Mosaics</h1>
        <h2 class="signup-label">Sign Up</h2>
        <form class="signup" action="LAMPAPI/Register.php" method="post">
          <div class="form-group">
            <!--first name-->
            <input
              type="text"
              placeholder="First Name"
              id="firstName"
              name="firstName"
              class="form-control"
              required
            />
          </div>
          <div class="form-group">
            <!--last name-->
            <input
              type="text"
              placeholder="Last Name"
              id="lastName"
              name="lastName"
              class="form-control"
              required
            />
          </div>
          <div class="form-group">
            <!--username-->
            <input
              type="text"
              placeholder="Username"
              id="login"
              name="login"
              class="form-control"
              required
            />
          </div>
          <div class="form-group">
            <!--password-->
            <input
              type="password"
              placeholder="Password"
              id="password"
              name="password"
              class="form-control"
              required
            />
          </div>
          <div class="form-group">
            <button class="register-btn" type="submit">Sign Up</button>
          </div>
          <div class="form-group">
            <p>Already Registered? <a href="index.html">Login</a></p>
          </div>
        </form>
      </div>
    </div>
  </body>
</html>
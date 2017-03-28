<?php

include 'connectCDB.php';

if(isset($_POST['username'])){
	$username = $_POST ['username'];
	$password = $_POST ['passWord'];
	$sql = "SELECT * FROM  customers WHERE customers= '".$username."' AND passWord = '".$password."' LIMIT 1";
	$res = mysql_query ($sql);

if(mysql_num_rows($res) ==1){
   echo "You logged in ";
   exit();
}
else {
      echo " Try again"; 
      exit();
  }
}

?>
<html>
 <head>
 <title>
 GAMEFliLoginScreen
</title>
<style type = "text/css">
body{background-image: url("backgroundimg.png")}
#tst1 {position: absolute; top:65%; left:45%;}
#tst2 {position: absolute; top: 35%;  left: 45%;}
#tst3 {position: absolute; top: 45%;  left: 45%;}
#tst4 {position: absolute; top: 55%;  left: 45%;}
#tst5 {position: absolute; top: 73%;  left: 45%;}
</style>
 </head>
 <body> 
 <p> 
 <h1>GAME Fli </h1>
 </p>
 
 <form  action="login.php" autocomplete="on"> 
     <h1>Log in</h1> 
     <p id ="tst2"> 
          <input id="username" name="username" required="required" type="text" placeholder="Username"/>
     </p>
     <p id= "tst3">  
          <input id="passWord" name="Password" required="required" type="password" placeholder="Password" /> 
     </p>
     <p class="keepLogin" id = "tst1"> 
          <input type="checkbox" name="loginkeeping" id="loginkeeping" value="loginkeeping" /> 
          <label for="loginkeeping">Keep me logged in</label>
     </p>
     <p class="login button" id = "tst4"> 
          <input type="submit" value="Login" /> 
     </p>
	 <a href = "createaccount.html"color = "black" id = "tst5"> CREATE ACCOUNT
	 </a>
	 
</form>
  
        
    
 </body>
 
 
 </html>
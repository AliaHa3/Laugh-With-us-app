<?php

$host="mysql.hostinger.in"; //replace with database hostname
$username="u946754690_admin"; //replace with database username
$password="adabdatabase333"; //replace with database password
$db_name="u946754690_laugh"; //replace with database name
 
$con=mysqli_connect("$host", "$username", "$password")or die("cannot connect");
mysqli_select_db($con,"$db_name")or die("cannot select DB");

if($_SERVER['REQUEST_METHOD'] == "GET"){
 // Get data
 $id = isset($_GET['id']) ? mysqli_real_escape_string($con,$_GET['id']) : "";
  
if (! empty($id)){
    
 // get data from data base
 $sql = "SELECT name,link
         FROM video
         WHERE type_id = $id";
mysqli_set_charset($con,"utf8");
 $result = mysqli_query($con,$sql) or die(mysqli_error($con));

// check for empty result
if (mysqli_num_rows($result) > 0) {
    // looping through all results
    $response["videos"] = array();
 
    while ($row = mysqli_fetch_array($result)) {
        // temp video array
        $video = array();
        $video["name"] = $row["name"];
        $video["link"] = $row["link"];
    
        // push single video into final response array
        array_push($response["videos"], $video);
    }
    // echoing JSON response
    echo json_encode($response);

   }
}
}
mysqli_close($con);
?> 
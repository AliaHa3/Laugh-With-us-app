<?php

$host="mysql.hostinger.in"; //replace with database hostname
$username="u946754690_admin"; //replace with database username
$password="adabdatabase333"; //replace with database password
$db_name="u946754690_laugh"; //replace with database name
 
$con=mysqli_connect("$host", "$username", "$password")or die("cannot connect");
mysqli_select_db($con,"$db_name")or die("cannot select DB");

$sql ="SELECT *FROM type";
mysqli_set_charset($con,"utf8");
$result = mysqli_query($con,$sql) or die(mysqli_error($con));
 
// check for empty result
if (mysqli_num_rows($result) > 0) {
    
	// looping through all results
    
	$response["types"] = array();
 
    while ($row = mysqli_fetch_array($result)) {
        // temp type array
        $type = array();
        $type["type_id"] = $row["type_id"];
        $type["name"] = $row["name"];
    
        // push single type into final response array
        array_push($response["types"], $type);
    }
    // echoing JSON response
    echo json_encode($response);
} 
mysqli_close($con);

?> 

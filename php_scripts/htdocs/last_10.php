<?php

	include_once("module.php");

	// Create connection
	$conn = connectToDatabase();
	
	//Selecting last 10 results
	$sql = "SELECT * FROM templog order by id desc limit 10";
	$result = $conn->query($sql);	
	$array = queryToAssoc($result);
	
	echo json_encode($array, JSON_NUMERIC_CHECK);
	
	$conn->close();
?>
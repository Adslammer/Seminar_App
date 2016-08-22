<?php

	include_once("module.php");

	// Create connection
	$conn = connectToDatabase();
	
	//Getting last result
	$sql = "SELECT * FROM templog order by id desc limit 1";
	$result = $conn->query($sql);
	$array = queryToAssoc($result);
	
	$output = array("data" => $array[0]);
	echo json_encode($output, JSON_FORCE_OBJECT);
	
	$conn->close();
?>
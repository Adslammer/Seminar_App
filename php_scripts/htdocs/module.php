<?php

	define('SERVER_NAME', '161.53.19.53');
	define('USERNAME', 'ajaksic');
	define('PASSWORD', 'G55NwGEmHHXJDjQj');
	define('DB_NAME', 'playground');
	
	function connectToDatabase() {
		// Create connection
		$conn = new mysqli(SERVER_NAME, USERNAME, PASSWORD, DB_NAME);
		// Check connection
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		return $conn;
	}
	
	function queryToAssoc($query) {
	
		$resultArray = array();
		$counter = 0;
	
		if ($query->num_rows > 0) {
		
			// output data of each row
			while($row = $query->fetch_assoc()) {
				$subArray = array();
				
				$subArray["counter"] = $counter++;
				$subArray["id"] = $row["id"];
				$subArray["time"] = $row["time"];
				$subArray["value"] = $row["value"];
				
				$resultArray[] = $subArray;
			}
			
		}
		
		return $resultArray;	
	}
	
	
?>
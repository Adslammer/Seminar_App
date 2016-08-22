<?php

	include_once("module.php");
	
	// Create connection
	$conn = connectToDatabase();
	
	$day = $_GET["day"];
	$month = $_GET["month"];
	$year = $_GET["year"];
	
	//Selecting data from begining of each hour.
	$sql = "SELECT * FROM templog WHERE (time>='$year-$month-$day 00:00:00' and time<'$year-$month-$day 00:00:10') 
									 OR (time>='$year-$month-$day 01:00:00' and time<'$year-$month-$day 01:00:10')
									 OR (time>='$year-$month-$day 02:00:00' and time<'$year-$month-$day 02:00:10')
									 OR (time>='$year-$month-$day 03:00:00' and time<'$year-$month-$day 03:00:10')
									 OR (time>='$year-$month-$day 04:00:00' and time<'$year-$month-$day 04:00:10')
									 OR (time>='$year-$month-$day 05:00:00' and time<'$year-$month-$day 05:00:10')
									 OR (time>='$year-$month-$day 06:00:00' and time<'$year-$month-$day 06:00:10')
									 OR (time>='$year-$month-$day 07:00:00' and time<'$year-$month-$day 07:00:10')
									 OR (time>='$year-$month-$day 08:00:00' and time<'$year-$month-$day 08:00:10')
									 OR (time>='$year-$month-$day 09:00:00' and time<'$year-$month-$day 09:00:10')
									 OR (time>='$year-$month-$day 10:00:00' and time<'$year-$month-$day 10:00:10')
									 OR (time>='$year-$month-$day 11:00:00' and time<'$year-$month-$day 11:00:10')
									 OR (time>='$year-$month-$day 12:00:00' and time<'$year-$month-$day 12:00:10')
									 OR (time>='$year-$month-$day 13:00:00' and time<'$year-$month-$day 13:00:10')
									 OR (time>='$year-$month-$day 14:00:00' and time<'$year-$month-$day 14:00:10')
									 OR (time>='$year-$month-$day 15:00:00' and time<'$year-$month-$day 15:00:10')
									 OR (time>='$year-$month-$day 16:00:00' and time<'$year-$month-$day 16:00:10')
									 OR (time>='$year-$month-$day 17:00:00' and time<'$year-$month-$day 17:00:10')
									 OR (time>='$year-$month-$day 18:00:00' and time<'$year-$month-$day 18:00:10')
									 OR (time>='$year-$month-$day 19:00:00' and time<'$year-$month-$day 19:00:10')
									 OR (time>='$year-$month-$day 20:00:00' and time<'$year-$month-$day 20:00:10')
									 OR (time>='$year-$month-$day 21:00:00' and time<'$year-$month-$day 21:00:10')
									 OR (time>='$year-$month-$day 22:00:00' and time<'$year-$month-$day 22:00:10')
									 OR (time>='$year-$month-$day 23:00:00' and time<'$year-$month-$day 23:00:10')";
	
	$result = $conn->query($sql);
	$array = queryToAssoc($result);
	
	//Json numeric check is for forcing ints to be int, not string
	$output = array("data" => $array);
	echo json_encode($array, JSON_NUMERIC_CHECK);
	
	$conn->close();
?>
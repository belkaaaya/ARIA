<?php
header('Content-Type: text/plain; charset=utf-8');

$host = "localhost";
$user = "root";
$pass = "";
$db   = "coffeeshop";

$conn = new mysqli($host, $user, $pass, $db);
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$result = $conn->query("DELETE FROM likes");

if ($result) {
    echo "cleared";
} else {
    echo "error: " . $conn->error;
}

$conn->close();
?>
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

$result = $conn->query("SELECT title, image FROM likes ORDER BY id DESC");

if ($result && $result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        echo htmlspecialchars($row["title"]) . "|" . htmlspecialchars($row["image"]) . "\n";
    }
} else {
    echo "empty\n";
}

$conn->close();
?>
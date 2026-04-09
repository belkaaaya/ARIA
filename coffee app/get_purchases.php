<?php
$host = "localhost";
$user = "root";
$pass = "";
$db   = "coffeeshop";

$conn = new mysqli($host, $user, $pass, $db);

$result = $conn->query("SELECT item_name, price, quantity FROM purchases");

while ($row = $result->fetch_assoc()) {
    echo $row["item_name"] . "|" . $row["price"] . "|" . $row["quantity"] . "\n";
}
?>

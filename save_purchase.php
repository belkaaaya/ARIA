<?php
$host = "localhost";
$user = "root";
$pass = "";
$db   = "coffeeshop";

$conn = new mysqli($host, $user, $pass, $db);

if ($conn->connect_error) {
    die("DB error");
}

$item_name = $_POST["item_name"];
$price     = $_POST["price"];
$quantity  = $_POST["quantity"];



$sql = "INSERT INTO purchases (item_name, price, quantity)
        VALUES ('$item_name', '$price', '$quantity')";

$conn->query($sql);

echo "OK";
?>

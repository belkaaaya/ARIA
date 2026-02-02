<?php
include "db.php";

$user_id = 1;

$result = $conn->query("SELECT product_name, quantity, price FROM cart_items WHERE user_id = $user_id");

$items = [];

while ($row = $result->fetch_assoc()) {
    $items[] = $row;
}

echo json_encode($items);
?>

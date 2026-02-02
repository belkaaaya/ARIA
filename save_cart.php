<?php
include "db.php";

$user_id = 1;
$conn->query("DELETE FROM cart_items WHERE user_id = $user_id");

$data = json_decode(file_get_contents("php://input"), true);

foreach ($data as $item) {
    $name = $item["name"];
    $quantity = $item["quantity"];
    $price = $item["price"];

    $sql = "INSERT INTO cart_items (product_name, quantity, price, user_id)
            VALUES ('$name', $quantity, $price, $user_id)";
    $conn->query($sql);
}

echo "Cart saved";
?>

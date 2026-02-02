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

$title = $_POST['title'] ?? '';
$image = $_POST['image'] ?? '';

// Nettoyer les données
$title = trim($title);
$image = trim($image);

// NORMALISATION : Extraire uniquement le nom du fichier sans chemin
if (!empty($image)) {
    // Si l'image contient un chemin, extraire seulement le nom du fichier
    if (str_contains($image, '/')) {
        $image = basename($image);
    }
    
    // Assurer que l'extension .png est présente
    if (!str_ends_with(strtolower($image), '.png')) {
        // Ajouter l'extension si manquante
        $image .= '.png';
    }
    
    // Nettoyer encore le nom (enlever tout chemin restant)
    $image = basename($image);
}

// Liste des noms d'images valides (basée sur votre liste)
$valid_images = [
    'americano.png', 'americanohh.png', 'bannanatoast.png', 'avocattoast.png',
    'berry.png', 'brownies.png', 'blueberry.png', 'capuccinoh.png',
    'caramellattee.png', 'chocolatcookie.png', 'chocolatcrepe.png', 'chocolatdonuts.png',
    'chocoswirl.png', 'cookie.png', 'croissant.png', 'eggstoast.png',
    'esspressoo.png', 'frenchtoast.png', 'granolabowl.png', 'hotcoffee.png',
    'icedtea.png', 'kiwi.png', 'latte.png', 'lattee.png',
    'latteee.png', 'latteh.png', 'lemon.png', 'mapp.png',
    'matcha.png', 'matchacookie.png', 'mixedplat.png', 'mochah.png',
    'mojito.png', 'orange.png', 'oreocookie.png', 'pancakes.png',
    'Pasteries.png', 'pinkcookie.png', 'savorybagels.png', 'savorycroissant.png',
    'strawberry.png', 'strawberrychcake.png', 'strawberrychocolatcake.png', 'vanilla.png',
    'vanillamuffins.png', 'watermelon.png', 'whitemocha.png'
];

// Vérifier si l'image est dans la liste valide, sinon utiliser une image par défaut
if (!empty($image) && !in_array(strtolower($image), array_map('strtolower', $valid_images))) {
    // Chercher une correspondance partielle
    $found = false;
    $image_lower = strtolower($image);
    foreach ($valid_images as $valid_image) {
        $valid_lower = strtolower($valid_image);
        // Vérifier si le nom sans extension correspond
        $image_base = str_replace('.png', '', $image_lower);
        $valid_base = str_replace('.png', '', $valid_lower);
        
        if (str_contains($image_base, $valid_base) || str_contains($valid_base, $image_base)) {
            $image = $valid_image; // Utiliser le nom correct avec la bonne casse
            $found = true;
            break;
        }
    }
    
    if (!$found) {
        // Utiliser une image par défaut si aucune correspondance
        $image = 'americano.png';
    }
}

if (!empty($title)) {
    // Éviter les doublons
    $check = $conn->prepare("SELECT id FROM likes WHERE title = ?");
    $check->bind_param("s", $title);
    $check->execute();
    $check->store_result();
    
    if ($check->num_rows == 0) {
        // Insérer si n'existe pas
        $stmt = $conn->prepare("INSERT INTO likes (title, image) VALUES (?, ?)");
        $stmt->bind_param("ss", $title, $image);
        
        if ($stmt->execute()) {
            echo "saved|" . $image;
        } else {
            echo "error|" . $conn->error;
        }
        $stmt->close();
    } else {
        echo "already_exists";
    }
    $check->close();
} else {
    echo "no_title";
}

$conn->close();
?>
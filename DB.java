import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** SQLite DISABLED – now using PHP + MySQL only */
public class DB {

    // On désactive totalement SQLite
    // private static final String URL = "jdbc:sqlite:coffee.db";

    // ----- INIT : SQLite supprimé -----
    public static void init() {
        System.out.println("SQLite disabled (using PHP + MySQL)");
    }

    // ----- LIKED ITEMS (désactivés car dépendaient de SQLite) -----
    public static void addLiked(String name, String imagePath) {
        System.out.println("addLiked() disabled – using MySQL/PHP instead");
    }

    public static void removeLiked(String name) {
        System.out.println("removeLiked() disabled – using MySQL/PHP instead");
    }

    public static List<LikedItem> getAllLiked() {
        System.out.println("getAllLiked() disabled – using MySQL/PHP instead");
        return new ArrayList<>();
    }

    public static class LikedItem {
        public final String name;
        public final String imagePath;
        public LikedItem(String name, String imagePath) {
            this.name = name; this.imagePath = imagePath;
        }
    }

    // ----- COMMENTS (désactivés car dépendaient de SQLite) -----
    public static void addComment(String author, String content) {
        System.out.println("addComment() disabled – using MySQL/PHP instead");
    }

    public static List<Comment> getComments() {
        System.out.println("getComments() disabled – using MySQL/PHP instead");
        return new ArrayList<>();
    }

    public static class Comment {
        public final String author, content, createdAt;
        public Comment(String author, String content, String createdAt) {
            this.author = author; this.content = content; this.createdAt = createdAt;
        }
    }
}

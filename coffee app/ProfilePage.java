import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;
import java.awt.image.BufferedImage;

public class ProfilePage extends JFrame {

    // ==== theme
    private static final Color PHONE_BG   = new Color(0x241B02);
    private static final Color TEXT_GOLD  = new Color(0x807043);
    private static final Color BROWN_DARK = new Color(0x241B02);
    private static final Color CARD_BG    = new Color(0xB9B29F);
    private static final Color TEXT_DARK  = new Color(0x3F2F03);
    private static final Color beige      = new Color(0xFBF1D6);

    private static final Font  TITLE_F    = new Font("Perpetua Titling MT", Font.PLAIN, 20);
    private static final Font  SUBTITLE_F = new Font("Perpetua Titling MT", Font.PLAIN, 16);
    private static final Font  BODY_F     = new Font("Serif", Font.PLAIN, 13);

    // ==== liked items model
    static class LikedItem {
        final String name;
        final ImageIcon icon;
        LikedItem(String n, ImageIcon i) { name = n; icon = i; }
    }
    private static final List<LikedItem> LIKED = new ArrayList<>();

    public static void addLiked(String itemName, ImageIcon thumb) {
        // Vérifier si l'item existe déjà
        for (LikedItem item : LIKED) {
            if (item.name.equals(itemName)) {
                System.out.println("Item déjà dans la liste: " + itemName);
                return; // Déjà dans la liste
            }
        }
        
        System.out.println("Ajout du like: " + itemName);
        
        // Ajouter dans la liste
        LIKED.add(new LikedItem(itemName, thumb));
        
        // Mettre à jour l'interface si une instance existe
        if (currentInstance != null) {
            SwingUtilities.invokeLater(() -> {
                currentInstance.refreshLikedStrip();
            });
        }
        
        // Préparer le chemin d'image pour la BD
        String imagePath = "images/default_coffee.jpg"; // Chemin par défaut
        
        if (thumb != null) {
            if (thumb.getDescription() != null && !thumb.getDescription().isEmpty()) {
                imagePath = thumb.getDescription();
                // Normaliser le chemin
                if (!imagePath.startsWith("images/")) {
                    // Extraire le nom du fichier
                    String[] parts = imagePath.split("/");
                    if (parts.length > 0) {
                        String filename = parts[parts.length - 1];
                        imagePath = "images/" + filename;
                    }
                }
            }
        }
        
        System.out.println("Chemin d'image pour BD: " + imagePath);
        
        // Sauvegarder dans la base de données
        saveLikeToDB(itemName, imagePath);
    }

    private static ProfilePage currentInstance;

    // ui refs
    private JPanel likedStrip;
    private JScrollPane likedScroll;
    private JScrollPane pageScroll;

    public ProfilePage() {
        super("ARIA COFFEE SHOP - PROFILE");
        setSize(420, 860);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        currentInstance = this;

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(0x1A1A1A));
        setContentPane(root);

        RoundedPanel phone = new RoundedPanel(28);
        phone.setBackground(PHONE_BG);
        phone.setLayout(new BorderLayout());
        root.add(phone, BorderLayout.CENTER);

        // ===== header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 14, 10, 14));
        header.setPreferredSize(new Dimension(420, 70));

        JLabel back = new JLabel("←");
        back.setFont(new Font("SansSerif", Font.BOLD, 22));
        back.setForeground(beige);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                new MenuCarousel().setVisible(true);
                dispose();
            }
        });
        header.add(back, BorderLayout.WEST);

        JLabel title = new JLabel("ARIA COFFEE SHOP", SwingConstants.CENTER);
        title.setFont(TITLE_F);
        title.setForeground(beige);
        header.add(title, BorderLayout.CENTER);

        JLabel cart = new JLabel("\uD83D\uDED2");
        cart.setFont(new Font("SansSerif", Font.PLAIN, 20));
        cart.setForeground(beige);
        cart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cart.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                new Panier().setVisible(true);
                dispose();
            }
        });
        header.add(cart, BorderLayout.EAST);

        phone.add(header, BorderLayout.NORTH);

        // ===== content (vertical)
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(12, 16, 90, 16));

        // ---- Upper card
        ShadowCard upper = new ShadowCard();
        upper.setBackground(CARD_BG);
        upper.setLayout(new BorderLayout());
        upper.setBorder(new EmptyBorder(16,16,16,16));

        JPanel up = new JPanel();
        up.setOpaque(false);
        up.setLayout(new BoxLayout(up, BoxLayout.Y_AXIS));

        JPanel avatar = new RoundedPanel(100);
        avatar.setBackground(new Color(0xE3DAC8));
        avatar.setPreferredSize(new Dimension(106,106));
        avatar.setMaximumSize(new Dimension(106,106));
        avatar.setLayout(new GridBagLayout());
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel letter = new JLabel("M");
        letter.setFont(new Font("Perpetua Titling MT", Font.BOLD, 40));
        letter.setForeground(BROWN_DARK);
        avatar.add(letter);

        up.add(avatar);
        up.add(Box.createVerticalStrut(8));

        JLabel name = new JLabel("imene bnr", SwingConstants.CENTER);
        name.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 18));
        name.setForeground(new Color(0x241B02));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        up.add(name);

        JLabel since = new JLabel("Loyal customer · since 2025", SwingConstants.CENTER);
        since.setFont(new Font("Serif", Font.PLAIN, 12));
        since.setForeground(new Color(0x241B02));
        since.setAlignmentX(Component.CENTER_ALIGNMENT);
        up.add(since);

        upper.add(up, BorderLayout.CENTER);
        upper.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(upper);
        content.add(Box.createVerticalStrut(12));

        // ---- Info card
        ShadowCard info = new ShadowCard();
        info.setBackground(CARD_BG);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(14,14,14,14));
        info.add(makeInfoRow("Email", "imene@example.com"));
        info.add(makeDivider());
        info.add(makeInfoRow("Phone", "+213 6 00 00 00 00"));
        info.add(makeDivider());
        info.add(makeInfoRow("Favorite branch", "ARIA COFFEE – alger"));
        info.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(info);
        content.add(Box.createVerticalStrut(12));

        // ---- Liked items card (horizontal scroll + clear)
        ShadowCard likedCard = new ShadowCard();
        likedCard.setBackground(CARD_BG);
        likedCard.setLayout(new BorderLayout());
        likedCard.setBorder(new EmptyBorder(14,14,14,14));
        likedCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        // header (title + Clear)
        JPanel likedHeader = new JPanel(new BorderLayout());
        likedHeader.setOpaque(false);

        JLabel likedTitle = new JLabel("LIKED ITEMS");
        likedTitle.setFont(SUBTITLE_F);
        likedTitle.setForeground(TEXT_DARK);
        likedHeader.add(likedTitle, BorderLayout.WEST);

        JButton clearBtn = pillButton("Clear", true);
        clearBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        clearBtn.setBorder(new EmptyBorder(6,12,6,12));
        clearBtn.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(
                    this, "Clear all liked items?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) clearLikedItems();
        });
        likedHeader.add(clearBtn, BorderLayout.EAST);

        likedCard.add(likedHeader, BorderLayout.NORTH);

        // inner strip (can be plus large que la vue)
        likedStrip = new HScrollPanel();
        likedStrip.setOpaque(false);
        likedStrip.setLayout(new BoxLayout(likedStrip, BoxLayout.X_AXIS));
        likedStrip.setBorder(new EmptyBorder(6, 0, 6, 0));

        // scrollpane FIXÉE en largeur (pour éviter que la page parte à droite)
        likedScroll = new JScrollPane(
                likedStrip,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        likedScroll.setBorder(new EmptyBorder(8,0,0,0));
        likedScroll.setOpaque(false);
        likedScroll.getViewport().setOpaque(false);
        likedScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        // fixe la hauteur, largeur auto: ne dépasse pas le parent
        likedScroll.setPreferredSize(new Dimension(0, 150));
        likedScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // barre horizontale invisible et fine
        JScrollBar hb = likedScroll.getHorizontalScrollBar();
        hb.setPreferredSize(new Dimension(0, 2));
        hb.setOpaque(false);
        hb.setUI(new BasicScrollBarUI() {
            @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {}
            @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {}
        });

        likedCard.add(likedScroll, BorderLayout.CENTER);
        content.add(likedCard);
        content.add(Box.createVerticalStrut(12));

        // ---- Comments card
        ShadowCard comments = new ShadowCard();
        comments.setBackground(CARD_BG);
        comments.setLayout(new BorderLayout());
        comments.setBorder(new EmptyBorder(14,14,14,14));
        comments.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel cTitle = new JLabel("SHARE YOUR THOUGHTS");
        cTitle.setFont(SUBTITLE_F);
        cTitle.setForeground(TEXT_DARK);
        comments.add(cTitle, BorderLayout.NORTH);

        JTextArea box = new JTextArea(3, 20);
        box.setLineWrap(true);
        box.setWrapStyleWord(true);
        box.setFont(BODY_F);
        box.setForeground(TEXT_DARK);
        box.setBackground(new Color(0xEFE9DC));
        box.setBorder(new EmptyBorder(8,8,8,8));

        JButton post = pillButton("Post", true);
        post.addActionListener(e -> {
            box.setText("");
            JOptionPane.showMessageDialog(this, "Thanks for your feedback!");
        });

        JPanel input = new JPanel(new BorderLayout(8,8));
        input.setOpaque(false);
        input.add(new JScrollPane(box), BorderLayout.CENTER);
        input.add(post, BorderLayout.EAST);
        comments.add(input, BorderLayout.CENTER);

        content.add(comments);

        // page scroll (vertical only), bien alignée à gauche
        pageScroll = new JScrollPane(
                content,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        pageScroll.setBorder(null);
        pageScroll.setOpaque(false);
        pageScroll.getViewport().setOpaque(false);
        pageScroll.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
        pageScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        phone.add(pageScroll, BorderLayout.CENTER);

        JPanel bottomHolder = new JPanel(new BorderLayout());
        bottomHolder.setOpaque(false);
        bottomHolder.setBorder(new EmptyBorder(0, 0, 20, 0));
        bottomHolder.add(makeBottomBar(), BorderLayout.CENTER);
        phone.add(bottomHolder, BorderLayout.SOUTH);

        // ===== comportements: scroll horizontal dans la carte seulement
        likedScroll.setWheelScrollingEnabled(false);

        MouseWheelListener horizontalWheel = e -> {
            JScrollBar h = likedScroll.getHorizontalScrollBar();
            int step = 40;
            h.setValue(h.getValue() + (int)(e.getPreciseWheelRotation() * step));
            e.consume();
        };
        likedScroll.addMouseWheelListener(horizontalWheel);
        likedScroll.getViewport().addMouseWheelListener(horizontalWheel);
        likedStrip.addMouseWheelListener(horizontalWheel);

        // désactiver la roulette de la page quand on survole la carte
        MouseAdapter lockPageWheel = new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { pageScroll.setWheelScrollingEnabled(false); }
            @Override public void mouseExited (MouseEvent e) { pageScroll.setWheelScrollingEnabled(true);  }
        };
        likedScroll.addMouseListener(lockPageWheel);
        likedScroll.getViewport().addMouseListener(lockPageWheel);
        likedStrip.addMouseListener(lockPageWheel);

        // drag pour déplacer horizontalement
        final JViewport vp = likedScroll.getViewport();
        final Point[] dragStart = { null };
        final Point[] viewStart = { null };

        MouseAdapter dragPan = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                dragStart[0] = e.getPoint();
                viewStart[0] = vp.getViewPosition();
                vp.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
            @Override public void mouseDragged(MouseEvent e) {
                if (dragStart[0] == null || viewStart[0] == null) return;
                int dx = e.getX() - dragStart[0].x;
                Point p = new Point(viewStart[0].x - dx, viewStart[0].y);
                p.x = Math.max(0, Math.min(p.x, likedStrip.getWidth() - vp.getWidth()));
                vp.setViewPosition(p);
            }
            @Override public void mouseReleased(MouseEvent e) {
                vp.setCursor(Cursor.getDefaultCursor());
                dragStart[0] = null;
                viewStart[0] = null;
            }
        };
        vp.addMouseListener(dragPan);
        vp.addMouseMotionListener(dragPan);
        likedStrip.addMouseListener(dragPan);
        likedStrip.addMouseMotionListener(dragPan);

        // Charger les likes après que l'interface est complètement créée
        SwingUtilities.invokeLater(() -> {
            System.out.println("=== INITIALISATION PROFIL ===");
            new Thread(() -> {
                try {
                    Thread.sleep(500); // Pause pour l'initialisation
                    System.out.println("Début du chargement des likes depuis la BD...");
                    LoadLikesFromDB();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    // ===== Scrollable panel (n'élargit pas la page)
    static class HScrollPanel extends JPanel implements Scrollable {
        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 32; }
        @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 128; }
        @Override public boolean getScrollableTracksViewportWidth() { return false; }  // autorise largeur > viewport
        @Override public boolean getScrollableTracksViewportHeight() { return true; }
    }

    // clear all liked items
    public void clearLikedItems() {
        int ok = JOptionPane.showConfirmDialog(
                this, "Clear all liked items from database too?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            LIKED.clear();
            refreshLikedStrip();
            // Effacer de la base de données
            clearLikesFromDB();
        }
    }

    // rebuild strip
    public void refreshLikedStrip() {
        if (likedStrip == null) {
            System.out.println("ERROR: likedStrip est null!");
            return;
        }
        
        System.out.println("Rafraîchissement de l'affichage. Nombre d'items: " + LIKED.size());
        
        likedStrip.removeAll();

        if (LIKED.isEmpty()) {
            // Afficher un message si vide
            JLabel emptyLabel = new JLabel("No liked items yet");
            emptyLabel.setFont(new Font("Serif", Font.ITALIC, 14));
            emptyLabel.setForeground(TEXT_DARK);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            likedStrip.add(emptyLabel);
        } else {
            final int tileW = 110;
            final int gap   = 8;

            for (LikedItem it : LIKED) {
                System.out.println("Affichage de: " + it.name);
                likedStrip.add(makeLikedTile(it));
                likedStrip.add(Box.createHorizontalStrut(gap));
            }
        }

        likedStrip.revalidate();
        likedStrip.repaint();
        System.out.println("Affichage rafraîchi avec succès");
    }

    // tile (image + nom)
    private JComponent makeLikedTile(LikedItem it) {
        JPanel tile = new RoundedPanel(16);
        tile.setBackground(new Color(0xEFE9DC));
        tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
        tile.setBorder(new EmptyBorder(8,8,8,8));
        tile.setPreferredSize(new Dimension(110, 120));
        tile.setMaximumSize(new Dimension(110, 120));

        JLabel pic = new JLabel();
        pic.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Afficher l'image si disponible
        if (it.icon != null) {
            System.out.println("  Icon pour " + it.name + ": " + 
                (it.icon.getIconWidth() > 0 ? "valide (" + it.icon.getIconWidth() + "x" + it.icon.getIconHeight() + ")" : "invalide"));
            
            if (it.icon.getIconWidth() > 0) {
                Image scaled = it.icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                pic.setIcon(new ImageIcon(scaled));
            } else {
                // Icône par défaut
                pic.setText("☕");
                pic.setFont(new Font("SansSerif", Font.PLAIN, 32));
            }
        } else {
            System.out.println("  Icon NULL pour " + it.name);
            // Icône par défaut
            pic.setText("❤️");
            pic.setFont(new Font("SansSerif", Font.PLAIN, 32));
        }

        JLabel name = new JLabel(it.name);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setFont(new Font("Serif", Font.PLAIN, 12));
        name.setForeground(TEXT_DARK);
        name.setHorizontalAlignment(SwingConstants.CENTER);

        tile.add(pic);
        tile.add(Box.createVerticalStrut(6));
        tile.add(name);

        return tile;
    }

    // bottom bar
    private JComponent makeBottomBar() {
        JPanel bottom = new JPanel(new GridLayout(1,3));
        bottom.setPreferredSize(new Dimension(420, 56));
        bottom.setBackground(new Color(0xEFE9DC));

        JButton home = navButton("🏠");
        JButton profile = navButton("👤");
        JButton cart = navButton("\uD83D\uDED2");

        home.addActionListener(e -> { new MenuCarousel().setVisible(true); dispose(); });
        profile.addActionListener(e -> { /* already here */ });
        cart.addActionListener(e -> { new Panier().setVisible(true); dispose(); });

        bottom.add(home); bottom.add(profile); bottom.add(cart);
        return bottom;
    }

    private static JButton navButton(String text) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? new Color(0xEADCC2) : new Color(0xF5EEDD));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFocusPainted(false);
        b.setBorder(null);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setFont(new Font("SansSerif", Font.PLAIN, 22));
        b.setForeground(TEXT_DARK);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel makeInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(4, 4, 4, 4));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 12));
        l.setForeground(TEXT_DARK);

        JLabel v = new JLabel(value);
        v.setFont(BODY_F);
        v.setForeground(TEXT_DARK);

        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.EAST);
        return row;
    }

    private JComponent makeDivider() {
        return new JComponent() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setColor(new Color(0xD9D0BF));
                g2.fillRoundRect(0, getHeight()/2-1, getWidth(), 2, 2, 2);
                g2.dispose();
            }
        };
    }

    private static JButton pillButton(String text, boolean primary) {
        JButton b = new JButton(text){
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),22,22);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setBackground(BROWN_DARK);
        b.setForeground(Color.WHITE);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // rounded boxes
    static class RoundedPanel extends JPanel {
        private final int r;
        RoundedPanel(int r){ this.r=r; setOpaque(false); }
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // soft shadow card
    static class ShadowCard extends JPanel {
        ShadowCard(){ setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0,0,0,28));
            g2.fillRoundRect(4, 6, getWidth()-8, getHeight()-6, 22, 22);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-8, getHeight()-8, 22, 22);
            g2.dispose();
            super.paintComponent(g);
        }
        @Override public Insets getInsets(){ return new Insets(6,6,10,12); }
    }

    // === Méthodes pour la base de données ===
    public static void saveLikeToDB(String title, String imagePath) {
        try {
            System.out.println("=== SAUVEGARDE BD ===");
            System.out.println("Titre: " + title);
            System.out.println("Image: " + imagePath);
            
            String url = "http://localhost/CoffeeApp/save_like.php";
            String params = "title=" + java.net.URLEncoder.encode(title, "UTF-8") + 
                           "&image=" + java.net.URLEncoder.encode(imagePath, "UTF-8");

            java.net.URL u = new java.net.URL(url);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) u.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.getOutputStream().write(params.getBytes("UTF-8"));
            
            int responseCode = conn.getResponseCode();
            System.out.println("Code réponse: " + responseCode);
            
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response = reader.readLine();
                reader.close();
                System.out.println("Réponse serveur: " + response);
            }
            
            System.out.println("=== FIN SAUVEGARDE ===");

        } catch (Exception ex) {
            System.out.println("ERREUR sauvegarde BD:");
            ex.printStackTrace();
        }
    }

    public static void LoadLikesFromDB() {
        try {
            System.out.println("=== DÉBUT CHARGEMENT BD ===");
            
            URL url = new URL("http://localhost/CoffeeApp/get_likes.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            System.out.println("Code HTTP: " + responseCode);
            
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                
                // Liste temporaire pour éviter les modifications concurrentes
                List<LikedItem> loadedItems = new ArrayList<>();
                
                int loadedCount = 0;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.equals("empty")) continue;
                    
                    System.out.println("Ligne BD: " + line);
                    String[] parts = line.split("\\|");
                    
                    if (parts.length >= 2) {
                        String title = parts[0].trim();
                        String imagePath = parts[1].trim();
                        
                        System.out.println("  Titre: " + title);
                        System.out.println("  Chemin: " + imagePath);
                        
                        // Charger l'image
                        ImageIcon icon = loadImageFromPath(imagePath);
                        
                        // Vérifier si déjà chargé
                        boolean alreadyLoaded = false;
                        for (LikedItem item : LIKED) {
                            if (item.name.equals(title)) {
                                alreadyLoaded = true;
                                break;
                            }
                        }
                        
                        if (!alreadyLoaded) {
                            loadedItems.add(new LikedItem(title, icon));
                            loadedCount++;
                            System.out.println("  ✓ Ajouté à la liste");
                        } else {
                            System.out.println("  ⚠ Déjà chargé");
                        }
                    }
                }
                reader.close();
                
                // Ajouter tous les nouveaux items
                if (!loadedItems.isEmpty()) {
                    LIKED.addAll(loadedItems);
                }
                
                System.out.println("=== CHARGEMENT TERMINÉ: " + loadedCount + " nouveaux items ===");
                System.out.println("Total en mémoire: " + LIKED.size() + " items");
                
                // Mettre à jour l'interface
                if (currentInstance != null) {
                    SwingUtilities.invokeLater(() -> {
                        System.out.println("Mise à jour de l'interface...");
                        currentInstance.refreshLikedStrip();
                        System.out.println("Interface mise à jour");
                    });
                }
                
            } else {
                System.out.println("ERREUR HTTP: " + responseCode);
            }
            
        } catch (Exception e) {
            System.out.println("ERREUR chargement BD:");
            e.printStackTrace();
        }
    }

    // Méthode pour charger une image depuis un chemin
    private static ImageIcon loadImageFromPath(String imagePath) {
        try {
            System.out.println("  Chargement image: " + imagePath);
            
            // Liste des chemins possibles à essayer
            String[] possiblePaths = {
                imagePath,
                "images/" + imagePath,
                "images/" + new java.io.File(imagePath).getName(),
                System.getProperty("user.dir") + "/" + imagePath,
                System.getProperty("user.dir") + "/images/" + new java.io.File(imagePath).getName(),
                "C:/wamp64/www/CoffeeApp/" + imagePath,
                "C:/wamp64/www/CoffeeApp/images/" + new java.io.File(imagePath).getName()
            };
            
            for (String path : possiblePaths) {
                java.io.File file = new java.io.File(path);
                System.out.println("    Test: " + file.getAbsolutePath());
                
                if (file.exists() && file.isFile()) {
                    System.out.println("    ✓ Fichier trouvé!");
                    ImageIcon icon = new ImageIcon(path);
                    
                    if (icon.getIconWidth() > 0) {
                        System.out.println("    ✓ Image valide: " + icon.getIconWidth() + "x" + icon.getIconHeight());
                        return icon;
                    } else {
                        System.out.println("    ✗ Image invalide");
                    }
                }
            }
            
            System.out.println("    ✗ Aucun fichier trouvé");
            
        } catch (Exception e) {
            System.out.println("    ✗ Erreur: " + e.getMessage());
        }
        
        // Fallback: créer une image par défaut
        System.out.println("    → Utilisation image par défaut");
        return createDefaultCoffeeIcon();
    }

    // Créer une icône de café par défaut
    private static ImageIcon createDefaultCoffeeIcon() {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Activer l'antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fond beige clair
        g2d.setColor(new Color(0xF5EEDD));
        g2d.fillRect(0, 0, 64, 64);
        
        // Tasse (marron)
        g2d.setColor(new Color(0x8B7355));
        g2d.fillRoundRect(15, 20, 34, 30, 10, 10);
        
        // Anse
        g2d.fillRoundRect(45, 25, 8, 20, 5, 5);
        
        // Café (marron foncé)
        g2d.setColor(new Color(0x4A3520));
        g2d.fillRoundRect(18, 23, 28, 24, 8, 8);
        
        // Vapeur
        g2d.setColor(new Color(0xDDDDDD));
        g2d.fillOval(25, 10, 14, 8);
        g2d.fillOval(20, 5, 10, 6);
        g2d.fillOval(30, 2, 12, 7);
        
        g2d.dispose();
        return new ImageIcon(image);
    }

    // Effacer tous les likes de la BD
    public static void clearLikesFromDB() {
        try {
            System.out.println("Effacement BD...");
            URL url = new URL("http://localhost/CoffeeApp/clear_likes.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            System.out.println("BD effacée: " + conn.getResponseCode());
        } catch (Exception ex) {
            System.out.println("Erreur effacement BD:");
            ex.printStackTrace();
        }
    }

    // === Méthode utilitaire pour ajouter un like avec une image depuis un fichier
    public static void addLikedWithImage(String itemName, String imageFilePath) {
        try {
            java.io.File imageFile = new java.io.File(imageFilePath);
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(imageFilePath);
                addLiked(itemName, icon);
            } else {
                System.out.println("Fichier image non trouvé: " + imageFilePath);
                addLiked(itemName, createDefaultCoffeeIcon());
            }
        } catch (Exception e) {
            System.out.println("Erreur ajout like avec image:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProfilePage().setVisible(true);
        });
    }
}
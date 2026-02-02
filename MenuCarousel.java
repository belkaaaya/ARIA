import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class MenuCarousel extends JFrame {
static { DB.init(); }   // bloc static de la classe
// ou dans le constructeur, tout au début : DB.init();

    // colors
    private final Color PHONE_BG   = new Color(0xFBF1D6);
    private final Color TEXT_GOLD  = new Color(0x807043);
    private final Color SEARCH_BG  = new Color(0xFBF1D6);
    private final Color CARD_BG    = new Color(0xB9B29F);
      private final Color menutitle   = new Color(0xFBF1D6);

    // fonts
    private final Font HEADER_FONT = new Font("Perpetua Titling MT", Font.PLAIN, 22);
    private final Font TITLE_FONT  = new Font("Perpetua Titling MT", Font.PLAIN, 40);

    public MenuCarousel() {
        super("ARIA COFFEE SHOP - MENU");

        setSize(420, 860);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // root = fond noir
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(0x1a1a1a));
        setContentPane(root);

        // téléphone beige
        RoundedPanel phone = new RoundedPanel(30);
        phone.setBackground(PHONE_BG);
        phone.setLayout(new BorderLayout());
        phone.setBorder(new EmptyBorder(0, 0, 0, 0));
        root.add(phone, BorderLayout.CENTER);

        // 1. header
        JPanel header = buildHeader();
        phone.add(header, BorderLayout.NORTH);

        // 2. centre (menu slides + popular + special)
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(10, 12, 10, 12));

        // slides (scrollable horizontal)
        center.add(buildMenuSlides());
        center.add(Box.createVerticalStrut(16));

        // popular (fixed)
        center.add(buildSmallItemCard(
                "brownies.png",
                "popular: BROWNIES",
                "taste our chocolat brownies with vanilla ice cream ,such a perfect combo",
                5
        ));
        center.add(Box.createVerticalStrut(10));

        // special (fixed)
  center.add(buildSmallItemCard(
                "strawberrychocolatcake.png",
                "popular: strawberry cake",
                "try our strawberry & chocolat cake , best choice for you birthday parties",
                20
        ));
        center.add(Box.createVerticalStrut(10));

        phone.add(center, BorderLayout.CENTER);

        // 3. bottom bar
        phone.add(buildBottomBar(), BorderLayout.SOUTH);
    }

    // =========================================================
    // HEADER avec image coffeemenub.png
    // =========================================================
    private JPanel buildHeader() {
        Image bg = loadHeaderImage();   // essaie de charger coffeemenub.png

        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) {
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                    // voile pour lisibilité
                    g.setColor(new Color(0, 0, 0, 60));
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    // fallback
                    g.setColor(new Color(0x3F2F03));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setPreferredSize(new Dimension(420, 220));
        header.setBorder(new EmptyBorder(12, 12, 12, 12));

        // ---- row 1: burger | title | cart
        JPanel row1 = new JPanel(new BorderLayout());
        row1.setOpaque(false);

        JLabel burger = new JLabel("≡");
        burger.setForeground(Color.WHITE);
        burger.setFont(new Font("SansSerif", Font.PLAIN, 20));
        row1.add(burger, BorderLayout.WEST);

        JLabel title = new JLabel("ARIA COFFEE SHOP", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(HEADER_FONT);
        title.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new WelcomePanelTest().setVisible(true);
                dispose();
            }
        });
        row1.add(title, BorderLayout.CENTER);

        JLabel cart = new JLabel("\uD83D\uDED2");
        cart.setForeground(Color.WHITE);
        cart.setFont(new Font("SansSerif", Font.PLAIN, 20));
        cart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Panier().setVisible(true);
            }
        });
        row1.add(cart, BorderLayout.EAST);

        header.add(row1);
        header.add(Box.createVerticalStrut(14));

        // ---- search centered
        JPanel searchWrapper = new JPanel();
        searchWrapper.setOpaque(false);
        searchWrapper.setLayout(new BoxLayout(searchWrapper, BoxLayout.X_AXIS));
        searchWrapper.add(Box.createHorizontalGlue());

        RoundedFillPanel search = new RoundedFillPanel(SEARCH_BG, 40, 1f);
        search.setPreferredSize(new Dimension(280, 50));
        search.setLayout(new BoxLayout(search, BoxLayout.X_AXIS));
        search.setBorder(new EmptyBorder(10, 16, 10, 16));
        search.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        search.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Recherche().setVisible(true);
                dispose();
            }
        });

        JLabel loupe = new JLabel("🔍");
        loupe.setForeground(Color.BLACK);
        loupe.setBorder(new EmptyBorder(0, 0, 0, 6));
        JLabel searchTxt = new JLabel("Search");
        searchTxt.setForeground(Color.BLACK);
        searchTxt.setFont(new Font("SansSerif", Font.BOLD, 14));
        search.add(loupe);
        search.add(searchTxt);

        searchWrapper.add(search);
        searchWrapper.add(Box.createHorizontalGlue());
        header.add(searchWrapper);
        header.add(Box.createVerticalStrut(18));

        // ---- title MENU
        JLabel menuTitle = new JLabel("MENU", SwingConstants.CENTER);
        menuTitle.setForeground(Color.white);
        menuTitle.setFont(TITLE_FONT);
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.add(menuTitle);

        return header;
    }

    // essaie de trouver coffeemenub.png
    private Image loadHeaderImage() {
        String name = "coffeemenub.png";

        // 1. même dossier
        File f = new File(name);
        if (f.exists()) {
            System.out.println("✅ header trouvé dans le dossier courant: " + f.getAbsolutePath());
            return new ImageIcon(f.getAbsolutePath()).getImage();
        }

        // 2. dossier images
        f = new File("images/" + name);
        if (f.exists()) {
            System.out.println("✅ header trouvé dans images/: " + f.getAbsolutePath());
            return new ImageIcon(f.getAbsolutePath()).getImage();
        }

        // 3. classpath
        java.net.URL url = getClass().getResource("/" + name);
        if (url != null) {
            System.out.println("✅ header trouvé dans le classpath: " + url);
            return new ImageIcon(url).getImage();
        }

        System.out.println("❌ image PAS trouvée : " + name);
        return null;
    }

    // =========================================================
    //  SLIDES (horizontal, manuel) + "Discover our menu"
    // =========================================================
    private JComponent buildMenuSlides() {

    // conteneur vertical (titre + scroll)
    JPanel container = new JPanel();
    container.setOpaque(false);
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
    container.setAlignmentX(Component.CENTER_ALIGNMENT);

    // petit espace
    container.add(Box.createVerticalStrut(10));

    // panneau horizontal qui va contenir les cartes
    JPanel row = new JPanel();
    row.setOpaque(false);
    row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

    // on centre un peu le row au début
    row.add(Box.createHorizontalStrut(10));

    // === on ajoute les cartes une par une ===
    row.add(makeMenuCard("lattee.png",  "HOT COFFEES"));
    row.add(Box.createHorizontalStrut(12));
    row.add(makeMenuCard("whitemocha.png", "COLD COFFEES"));
    row.add(Box.createHorizontalStrut(12));
    row.add(makeMenuCard("chocolatcookie.png",      "COOKIES"));
    row.add(Box.createHorizontalStrut(12));
    row.add(makeMenuCard("icedtea.png",       "DRINKS"));
    row.add(Box.createHorizontalStrut(12));
    row.add(makeMenuCard("mixedplat.png",       "SAVORY BRUNCH"));
    row.add(Box.createHorizontalStrut(12));
    row.add(makeMenuCard("pancakes.png",        "SWEET BRUNCH"));
    row.add(Box.createHorizontalStrut(12));
    row.add(makeMenuCard("vanilla.png",        "TARTS"));
    row.add(Box.createHorizontalStrut(12));
    row.add(makeMenuCard("chocoswirl.png",    "PASTERIES"));
    row.add(Box.createHorizontalStrut(10));

    // scroll horizontal (manuel)
    JScrollPane hscroll = new JScrollPane(
            row,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
    );
    hscroll.setBorder(null);
    hscroll.setOpaque(false);
    hscroll.getViewport().setOpaque(false);

    // barre fine
    hscroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 3));
    hscroll.getHorizontalScrollBar().setUnitIncrement(20);
    hscroll.getHorizontalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(0, 0, 0, 60);
            this.trackColor = new Color(0, 0, 0, 0);
        }
    });

    // largeur du bloc de slides
    hscroll.setPreferredSize(new Dimension(360, 180));
    hscroll.setAlignmentX(Component.CENTER_ALIGNMENT);

    container.add(hscroll);

    return container;
}


    // 1 slide card (image en haut + texte centré)
 // creates ONE menu slide (hot coffees, cold coffees, ...)
private JPanel makeMenuCard(String imgPath, String titleText) {
    Color cardColor = new Color(0xB9B29F);   // same beige as your figma

    // rounded card
    JPanel card = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cardColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
            g2.dispose();
            super.paintComponent(g);
        }
    };
    card.setOpaque(false);
    card.setPreferredSize(new Dimension(160, 210)); // adjust if you want bigger
    card.setMaximumSize(new Dimension(160, 210));
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(new EmptyBorder(12, 12, 12, 12));
    card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    // ===== image on top (original size) =====
    ImageIcon icon = new ImageIcon(imgPath);
    JLabel imgLabel;
    if (icon.getIconWidth() > 0) {
        imgLabel = new JLabel(icon);
    } else {
        imgLabel = new JLabel("☕");
        imgLabel.setFont(new Font("SansSerif", Font.PLAIN, 26));
    }
    imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    card.add(imgLabel);
    card.add(Box.createVerticalStrut(6));

    // ===== title: HOT COFFEES =====
    JLabel title = new JLabel(titleText, SwingConstants.CENTER);
    // try Perpetua, fallback Serif
    Font titleFont = new Font("Perpetua Titling MT", Font.PLAIN, 14);
    title.setFont(titleFont);
    title.setForeground(new Color(0x807043));  // gold like figma
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    card.add(title);
    card.add(Box.createVerticalStrut(4));

    // ===== subtitle: DISCOVER OUR ... MENU (3 lines) =====
    String sub = "DISCOVER OUR " + titleText.toUpperCase() + " MENU";
    JLabel subLabel = new JLabel(
            "<html><div style='text-align:center;'>" +
                    sub.replace(" ", " ") +   // just to be sure 🙂 
                    "</div></html>"
    );
    subLabel.setFont(new Font("Serif", Font.PLAIN, 11));
    subLabel.setForeground(Color.BLACK);
    subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    card.add(subLabel);

    card.add(Box.createVerticalGlue());

    // ===== click opens right page =====
    card.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            switch (titleText) {
                case "HOT COFFEES":
                    new HotCoffee().setVisible(true);
                    break;
                case "COLD COFFEES":
                    new ColdCoffees().setVisible(true);
                    break;
                case "COOKIES":
                    new Cookies().setVisible(true); // change if you have Cookies.java
                    break;
                case "DRINKS":
                    new Drinks().setVisible(true);
                    break;
                case "SAVORY BRUNCH":
                    new Savory().setVisible(true);
                    break;
                case "SWEET BRUNCH":
                    new sweat().setVisible(true);
                    break;
                case "TARTS":
                    new Tarts().setVisible(true);
                    break;
                case "PASTERIES":
                    new Pasteries().setVisible(true);
                    break;
            }
        }
    });

    return card;
}





    // small items (popular, special)
    private JPanel buildSmallItemCard(String imgPath, String title, String desc, double price) {
        RoundedFillPanel card = new RoundedFillPanel(CARD_BG, 35, 1f);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(340, 90));
        card.setMaximumSize(new Dimension(340, 90));
        card.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel imgWrap = new JPanel(new GridBagLayout());
        imgWrap.setOpaque(false);
        ImageIcon ic = loadIconKeepRatio(imgPath, 70, 70);
        JLabel img = (ic != null) ? new JLabel(ic) : new JLabel("🍰");
        imgWrap.add(img);
        card.add(imgWrap, BorderLayout.WEST);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 14));
        t.setForeground(new Color(0x3F2F03));
        text.add(t);

        JLabel d = new JLabel("<html><body style='width:180px;'>" + desc + "</body></html>");
        d.setFont(new Font("Serif", Font.PLAIN, 11));
        d.setForeground(new Color(0x1A1917));
        d.setBorder(new EmptyBorder(3, 0, 0, 0));
        text.add(d);

        card.add(text, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        right.setOpaque(false);
        JLabel priceLabel = new JLabel(String.format("%.1f $", price));
        priceLabel.setForeground(new Color(0x3F2F03));
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        right.add(priceLabel);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    // bottom bar
    private JPanel buildBottomBar() {
        JPanel bar = new JPanel();
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(420, 72));
        bar.setLayout(new FlowLayout(FlowLayout.CENTER, 35, 10));

        JLabel home = new JLabel("⌂");
        JLabel user = new JLabel("☺");
        JLabel cart = new JLabel("\uD83D\uDED2");

        for (JLabel l : new JLabel[]{home, user, cart}) {
            l.setFont(new Font("SansSerif", Font.PLAIN, 20));
            l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            l.setForeground(new Color(0x3F2F03));
        }

    home.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {

        // 🔥 Charger les articles depuis MySQL AVANT d'ouvrir le menu
   

        new MenuCarousel().setVisible(true);
        dispose();
    }
});


        user.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ProfilePage().setVisible(true);
                dispose();
            }
        });

        cart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              
                new Panier().setVisible(true);
            }
        });

        bar.add(home);
        bar.add(user);
        bar.add(cart);

        return bar;
    }

    // load image and keep ratio
    private ImageIcon loadIconKeepRatio(String path, int maxW, int maxH) {
        ImageIcon icon = new ImageIcon(path);
        if (icon.getIconWidth() <= 0) return null;

        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        double rw = (double) maxW / w;
        double rh = (double) maxH / h;
        double r = Math.min(rw, rh);

        int newW = (int) (w * r);
        int newH = (int) (h * r);

        Image img = icon.getImage().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    // rounded panel
    static class RoundedPanel extends JPanel {
        int r;
        RoundedPanel(int r) {
            this.r = r;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // rounded fill panel
    static class RoundedFillPanel extends JPanel {
        int r; Color fill;
        RoundedFillPanel(Color c, int r, float alpha) {
            this.r = r;
            this.fill = new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.round(alpha*255));
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuCarousel().setVisible(true));
    }
}

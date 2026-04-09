import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ColdCoffees extends JFrame {
// --- utils robustes ---
private static ImageIcon safeLoad(String path) {
    ImageIcon ic = new ImageIcon(path);
    if (ic.getIconWidth() <= 0) {
        System.out.println("[IMG] introuvable: " + path);
        ic = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
    }
    return ic;
}

private static ImageIcon scaleIcon(ImageIcon src, int w, int h) {
    if (src == null || src.getIconWidth() <= 0)
        return new ImageIcon(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB));
    Image scaled = src.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
    return new ImageIcon(scaled);
}


    private final Color PHONE_BG   = new Color(0xFBF1D6);
    private final Color TEXT_GOLD  = new Color(0x807043);
    private final Color SEARCH_BG  = new Color(0x3F2F03);
    private final Color CARD_BG    = new Color(0xB9B29F);
    private final Font  HEADER_FONT = new Font("Perpetua Titling MT", Font.PLAIN, 22);
    private final Font  TITLE_FONT  = new Font("Perpetua Titling MT", Font.PLAIN, 36);
    private final Font  ITEM_TITLE_FONT = new Font("Perpetua Titling MT", Font.PLAIN, 20);
    private final Font  DESC_FONT   = new Font("Serif", Font.PLAIN, 13);

    public ColdCoffees() {
        super("ColdCoffees");

        setSize(420, 860);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(0x1a1a1a));
        setContentPane(root);

        RoundedPanel phone = new RoundedPanel(30);
        phone.setBackground(PHONE_BG);
        phone.setLayout(new BorderLayout());
        phone.setBorder(new EmptyBorder(20, 12, 12, 12));
        root.add(phone, BorderLayout.CENTER);

        // ================= HEADER =================
        JPanel topSection = new JPanel();
        topSection.setOpaque(false);
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

         JLabel backArrow = new JLabel("←");
backArrow.setFont(new Font("SansSerif", Font.BOLD, 22));
backArrow.setForeground(TEXT_GOLD);
backArrow.setBorder(new EmptyBorder(0, 8, 0, 0));
backArrow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       // when clicked, it goes back to the previous page (for example, MenuPage)
backArrow.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        new MenuCarousel().setVisible(true);  // 👈 replace with the page you want to go back to
        dispose();
    }
});

        JLabel shopTitle = new JLabel("ARIA COFFEE SHOP", SwingConstants.CENTER);
        shopTitle.setFont(HEADER_FONT);
        shopTitle.setForeground(TEXT_GOLD);
        shopTitle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        shopTitle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new MenuCarousel().setVisible(true);
                dispose();
            }
        });
        headerRow.add(shopTitle, BorderLayout.CENTER);

        JLabel cartIcon = new JLabel("\uD83D\uDED2");
        cartIcon.setFont(new Font("SansSerif", Font.PLAIN, 20));
        cartIcon.setForeground(TEXT_GOLD);
        cartIcon.setBorder(new EmptyBorder(0, 0, 0, 10));
        cartIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cartIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Panier().setVisible(true);
            }
        });
        headerRow.add(cartIcon, BorderLayout.EAST);

        topSection.add(headerRow);
        topSection.add(Box.createVerticalStrut(16));

        // ================= SEARCH BAR =================
        JPanel searchWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        searchWrapper.setOpaque(false);

        RoundedFillPanel searchBar = new RoundedFillPanel(SEARCH_BG, 50, 1f);
        searchBar.setPreferredSize(new Dimension(320, 48));
        searchBar.setLayout(new BoxLayout(searchBar, BoxLayout.X_AXIS));
        searchBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Recherche().setVisible(true);
            }
        });

        searchBar.setBorder(new EmptyBorder(10, 16, 10, 16));

        JLabel iconSearch = new JLabel("🔍");
        iconSearch.setForeground(Color.WHITE);
        iconSearch.setBorder(new EmptyBorder(0, 0, 0, 8));
        JLabel searchTxt = new JLabel("Search");
        searchTxt.setForeground(Color.WHITE);
        searchTxt.setFont(new Font("SansSerif", Font.BOLD, 14));

        searchBar.add(iconSearch);
        searchBar.add(searchTxt);
        searchWrapper.add(searchBar);
        topSection.add(searchWrapper);
        topSection.add(Box.createVerticalStrut(18));

        // ================= TITLE =================
        JLabel pageTitle = new JLabel("cold coffees", SwingConstants.CENTER);
        pageTitle.setFont(TITLE_FONT);
        pageTitle.setForeground(Color.BLACK);
        pageTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        topSection.add(pageTitle);
        topSection.add(Box.createVerticalStrut(18));

        phone.add(topSection, BorderLayout.NORTH);

        // ================= CONTENT =================
        JPanel listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(0, 6, 12, 6));

        listPanel.add(buildSavoryCard("latte.png", "LATTE",
                "Doux et équilibré, l’union parfaite du café et du lait froid", 3.0));
        listPanel.add(Box.createVerticalStrut(12));
        listPanel.add(buildSavoryCard("caramellattee.png", "CARAMEL LATTE",
                "Café crémeux au caramel fondant et gourmand.", 6.5));
        listPanel.add(Box.createVerticalStrut(12));
        
        listPanel.add(buildSavoryCard("americano.png", "AMERICANO",
                "Léger et aromatique, pour ceux qui aiment la simplicité", 2.0));
        listPanel.add(Box.createVerticalStrut(12));
        
         listPanel.add(buildSavoryCard("matcha.png", "MATCHA",
                "Un lait velouté au thé matcha, doux et apaisant", 5.0));
        listPanel.add(Box.createVerticalStrut(20));


        listPanel.add(buildSavoryCard("whitemocha.png", "WHITE MOCHA",
                "Espresso et chocolat blanc pour une touche sucrée", 4.5));
        listPanel.add(Box.createVerticalStrut(12));

         

 

      


        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(4, 0));
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0, 0, 0, 50);
                this.trackColor = new Color(0, 0, 0, 0);
            }
        });

        phone.add(scroll, BorderLayout.CENTER);
    }

private JPanel buildSavoryCard(String imgPath, String title, String desc, double price) {
    // carte arrondie
    RoundedFillPanel card = new RoundedFillPanel(CARD_BG, 40, 1f);
    card.setLayout(new BorderLayout());
    card.setPreferredSize(new Dimension(340, 130));
    card.setMaximumSize(new Dimension(340, 130));
    card.setBorder(new EmptyBorder(10, 10, 10, 10));

 // ====== IMAGE À GAUCHE (garde la taille originale) ======
JPanel imgWrapper = new JPanel();
imgWrapper.setOpaque(false);

// charge l’image telle qu’elle est, sans redimensionner
ImageIcon icon = safeLoad(imgPath);   // ← charge l’image de façon sûre
JLabel imgLabel = new JLabel(icon);   // ← garde UNE SEULE instance pour l’affichage + le like


// garde l’image centrée verticalement
imgWrapper.setLayout(new GridBagLayout());
imgWrapper.add(imgLabel);

// si tu veux que le bloc s’adapte à la taille de l’image :
imgWrapper.setPreferredSize(new Dimension(icon.getIconWidth() + 10, icon.getIconHeight() + 10));

card.add(imgWrapper, BorderLayout.WEST);


    // ====== PARTIE CENTRALE (texte + bloc droite) ======
    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.setOpaque(false);

    // texte (titre + description)
    JPanel textPanel = new JPanel();
    textPanel.setOpaque(false);
    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(ITEM_TITLE_FONT);
    titleLabel.setForeground(new Color(0x807043));
    textPanel.add(titleLabel);

    JLabel descLabel = new JLabel("<html><body style='width:180px;'>" + desc + "</body></html>");
    descLabel.setFont(DESC_FONT);
    descLabel.setForeground(new Color(0x1A1917));
    descLabel.setBorder(new EmptyBorder(4, 0, 6, 0));
    textPanel.add(descLabel);

    centerPanel.add(textPanel, BorderLayout.CENTER);

    // ====== BLOC DROITE (prix + / - add cœur) ======
    JPanel rightBlock = new JPanel();
    rightBlock.setOpaque(false);
    rightBlock.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));

    // prix
    JLabel priceLabel = new JLabel(String.format("%.1f $", price));
    priceLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
    priceLabel.setForeground(new Color(0x3F2F03));
    rightBlock.add(priceLabel);

    // quantité -> on commence à 0
    final int[] q = {0};
    JLabel qtyLabel = new JLabel(String.valueOf(q[0]));
    qtyLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

    JButton minusBtn = new JButton("-");
    JButton plusBtn = new JButton("+");
    styleCircleBtn(minusBtn);
    styleCircleBtn(plusBtn);

    rightBlock.add(minusBtn);
    rightBlock.add(qtyLabel);
    rightBlock.add(plusBtn);

    // bouton add
    JButton addBtn = new JButton("add");
    addBtn.setBackground(new Color(0x3F2F03));
    addBtn.setForeground(Color.WHITE);
    addBtn.setFocusPainted(false);
    addBtn.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
    rightBlock.add(addBtn);

    // cœur
    JButton heartBtn = new JButton("♡");
    heartBtn.setFocusPainted(false);
    heartBtn.setContentAreaFilled(false);
    heartBtn.setBorder(null);
    heartBtn.setForeground(new Color(0x3F2F03));
    rightBlock.add(heartBtn);




    centerPanel.add(rightBlock, BorderLayout.SOUTH);
    card.add(centerPanel, BorderLayout.CENTER);

    // ====== ACTIONS ======

    // + augmente
    plusBtn.addActionListener(e -> {
        q[0]++;
        qtyLabel.setText(String.valueOf(q[0]));
    });

    // - diminue mais pas en dessous de 0
    minusBtn.addActionListener(e -> {
        if (q[0] > 0) {
            q[0]--;
            qtyLabel.setText(String.valueOf(q[0]));
        }
    });

    // cœur toggle
  // cœur toggle + send to profile when liked (safe + scaled)
heartBtn.addActionListener(e -> {
    boolean nowLike = heartBtn.getText().equals("♡");
    heartBtn.setText(nowLike ? "♥" : "♡");
    heartBtn.setForeground(nowLike ? Color.RED : new Color(0x3F2F03));

    if (nowLike) {
        ImageIcon thumb = scaleIcon(icon, 64, 64);       // <-- réutilise 'icon' ci-dessus
        System.out.println("[LIKE] " + title + " w=" + icon.getIconWidth());
        ProfilePage.addLiked(title, thumb);              // <-- envoie (nom + image) vers le profil
    }
});





   addBtn.addActionListener(e -> {
    // theme for JOptionPane
    UIManager.put("OptionPane.background", new Color(0xFBF1D6));
    UIManager.put("Panel.background",       new Color(0xFBF1D6));
    UIManager.put("OptionPane.messageForeground", new Color(0x3F2F03));
    UIManager.put("OptionPane.messageFont",  new Font("Perpetua Titling MT", Font.PLAIN, 16));
    UIManager.put("Button.background",       new Color(0x3F2F03));
    UIManager.put("Button.foreground",       Color.WHITE);
    UIManager.put("Button.font",             new Font("SansSerif", Font.BOLD, 13));

    if (q[0] > 0) {
        // add to cart
        Panier.addItem(title, q[0], price, imgPath);

        // pretty message (parent = current window)
        JLabel msg = new JLabel(
            "<html><div style='text-align:center;'>"
          + "<b style='font-size:15px;color:#3F2F03;'>"
          + title.toUpperCase() + " ADDED TO YOUR CART 🛒</b><br>"
          + "<span style='font-size:12px;color:#807043;'>Enjoy your choice!</span>"
          + "</div></html>"
        );
     

        Component parent = (Component) e.getSource();
        JOptionPane.showMessageDialog(
            (Component) SwingUtilities.getWindowAncestor(parent),
            msg,
            "ARIA COFFEE SHOP",
            JOptionPane.INFORMATION_MESSAGE,
            (icon != null && ((ImageIcon)icon).getIconWidth() > 0) ? icon : null
        );

    } else {
        JLabel warn = new JLabel(
            "<html><div style='text-align:center;'>"
          + "<b style='font-size:15px;color:#3F2F03;'>PLEASE CHOOSE A QUANTITY 🙂</b><br>"
          + "<span style='font-size:12px;color:#807043;'>Select how many before adding to cart.</span>"
          + "</div></html>"
        );
        Component parent = (Component) e.getSource();
        JOptionPane.showMessageDialog(
            (Component) SwingUtilities.getWindowAncestor(parent),
            warn,
            "ARIA COFFEE SHOP",
            JOptionPane.WARNING_MESSAGE
        );
    }
});

    return card;
}


    private ImageIcon getScaledIcon(String path, int w, int h) {
        ImageIcon ic = new ImageIcon(path);
        if (ic.getIconWidth() <= 0) return null;
        BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(ic.getImage(), 0, 0, w, h, null);
        g2.dispose();
        return new ImageIcon(resized);
    }

    static class RoundedPanel extends JPanel {
        private final int radius;
        RoundedPanel(int r) { this.radius = r; setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),radius,radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedFillPanel extends JPanel {
        private final int radius;
        private final Color fill;
        RoundedFillPanel(Color base, int radius, float alpha) {
            this.radius = radius;
            int a = Math.round(alpha * 255);
            this.fill = new Color(base.getRed(), base.getGreen(), base.getBlue(), a);
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),radius,radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }


    private void styleCircleBtn(JButton b) {
        b.setBackground(new Color(0x3F2F03));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ColdCoffees().setVisible(true));
    }
}
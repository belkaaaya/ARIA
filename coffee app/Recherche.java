import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Recherche extends JFrame {

    private final Color PHONE_BG  = new Color(0xFBF1D6);
    private final Color BROWN_BG  = new Color(0x241B02);
    private final Color CARD_BG   = new Color(0xB9B29F);
    private final Color TEXT_GOLD = new Color(0x807043);
    private final Color TEXT_WHITE = new Color(0xFBF1D6);

    private final Font HEADER_FONT = new Font("Perpetua Titling MT", Font.PLAIN, 22);
    private final Font ITEM_FONT   = new Font("Perpetua Titling MT", Font.PLAIN, 14);

    private JTextField searchField;
    private JPanel listPanel;
    private final List<MenuItem> allItems = new ArrayList<>();

    public Recherche() {
        super("ARIA COFFEE SHOP - RECHERCHE");

        setSize(420, 860);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        loadItems();

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(0x1a1a1a));
        setContentPane(root);

        RoundedPanel phone = new RoundedPanel(30);
        phone.setBackground(PHONE_BG);
        phone.setLayout(new BorderLayout());
        phone.setBorder(new EmptyBorder(20, 0, 0, 0));
        root.add(phone, BorderLayout.CENTER);

        // ===== HEADER =====
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 44));

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


        JLabel title = new JLabel("ARIA COFFEE SHOP", SwingConstants.CENTER);
        title.setForeground(TEXT_GOLD);
        title.setFont(HEADER_FONT);
        header.add(title, BorderLayout.CENTER);

        JLabel cart = new JLabel("\uD83D\uDED2");
        cart.setForeground(TEXT_GOLD);
        cart.setFont(new Font("SansSerif", Font.PLAIN, 18));
        cart.setBorder(new EmptyBorder(0, 0, 0, 6));
        cart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Panier().setVisible(true);
                dispose();
            }
        });
        header.add(cart, BorderLayout.EAST);

        phone.add(header, BorderLayout.NORTH);

        // ===== CENTRE =====
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        phone.add(center, BorderLayout.CENTER);

        RoundedFillPanel brownPanel = new RoundedFillPanel(BROWN_BG, 70, 1f);
        brownPanel.setLayout(new BorderLayout());
        center.add(brownPanel, BorderLayout.CENTER);

        // Bande verticale gauche
        JPanel leftStrip = new JPanel();
        leftStrip.setOpaque(false);
        leftStrip.setLayout(new BoxLayout(leftStrip, BoxLayout.Y_AXIS));
        leftStrip.setBorder(new EmptyBorder(20, 0, 20, 10));


        // Partie droite
        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.setBorder(new EmptyBorder(16, 0, 16, 8));
        brownPanel.add(right, BorderLayout.CENTER);

        // Barre de recherche
        RoundedFillPanel searchBar = new RoundedFillPanel(new Color(0xB9B29F), 40, 1f);
        searchBar.setLayout(new BorderLayout());
        searchBar.setPreferredSize(new Dimension(5, 38));

        JLabel loupe = new JLabel("  🔍 ");
        loupe.setForeground(new Color(0x3F2F03));
        searchBar.add(loupe, BorderLayout.WEST);

        searchField = new JTextField();
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setForeground(new Color(0x3F2F03));
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchBar.add(searchField, BorderLayout.CENTER);

     // create a wrapper panel for the search bar + spacing
JPanel searchWrapper = new JPanel();
searchWrapper.setOpaque(false);
searchWrapper.setLayout(new BoxLayout(searchWrapper, BoxLayout.Y_AXIS));

// add the search bar
searchWrapper.add(searchBar);

// add a little empty space (10px for example)
searchWrapper.add(Box.createVerticalStrut(10));

// now add the wrapper to the top
right.add(searchWrapper, BorderLayout.NORTH);


        // Liste verticale
        listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        populateList(allItems);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(3, 0));
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(0, 0, 0, 40);
                this.trackColor = new Color(0, 0, 0, 0);
            }
        });

        right.add(scroll, BorderLayout.CENTER);

        // Recherche dynamique
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String txt = searchField.getText().trim().toLowerCase();
                if (txt.isEmpty()) {
                    populateList(allItems);
                } else {
                    List<MenuItem> filtered = new ArrayList<>();
                    for (MenuItem it : allItems) {
                        if (it.name.toLowerCase().contains(txt)) {
                            filtered.add(it);
                        }
                    }
                    populateList(filtered);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String txt = searchField.getText().trim().toLowerCase();
                    for (MenuItem it : allItems) {
                        if (it.name.toLowerCase().contains(txt)) {
                            openPageFor(it);
                            break;
                        }
                    }
                }
            }
        });
    }

    // Liste des articles
    private void loadItems() {
        allItems.add(new MenuItem("oreo cookie", "oreocookie.png", "cookies"));
        allItems.add(new MenuItem("latte", "latteh.png", "hot"));
        allItems.add(new MenuItem("pancakes", "pancakes.png", "sweat"));
        allItems.add(new MenuItem("matcha", "matcha.png", "cold"));
        allItems.add(new MenuItem("lemon cream", "lemon.png", "tarts"));
        allItems.add(new MenuItem("french toast", "frenchtoast.png", "sweat"));
        allItems.add(new MenuItem("cookie", "cookie.png", "cookies"));
        allItems.add(new MenuItem("granola bowl", "granolabowl.png", "sweat"));
        allItems.add(new MenuItem("chocolat Cookies", "chocolatcookie.png", "cookies"));
         allItems.add(new MenuItem("strawberry cream", "strawberry.png", "tarts"));
         allItems.add(new MenuItem("pink cookie ", "pinkcookie.png", "cookies"));
           allItems.add(new MenuItem("choco swirl", "chocoswirl.png", "pasteries"));
        allItems.add(new MenuItem("matcha cookie", "matchacookie.png", "cookies"));
        allItems.add(new MenuItem("americano", "americanohh.png", "hot"));
         allItems.add(new MenuItem("savory bagels ", "savorybagels.png", "savory"));
        allItems.add(new MenuItem("cappuccino", "capuccinoh.png", "hot"));
        allItems.add(new MenuItem("esspresso", "esspressoo.png", "hot"));
           allItems.add(new MenuItem("avocado toast", "avocattoast.png", "savory"));
        allItems.add(new MenuItem("latte", "latteee.png", "cold"));
        allItems.add(new MenuItem("caramel latte", "caramellattee.png", "cold"));
              allItems.add(new MenuItem("americano", "americano.png", "cold"));
        allItems.add(new MenuItem("orange juice", "orange.png", "drinks"));
        allItems.add(new MenuItem("mojito", "mojito.png", "drinks"));
        allItems.add(new MenuItem("berry lemonade", "berry.png", "drinks"));
        allItems.add(new MenuItem("iced tea", "icedtea.png", "drinks"));
             allItems.add(new MenuItem("watermalon juicew", "watermelon.png", "drinks"));
        allItems.add(new MenuItem("savory croissant", "savorycroissant.png", "savory"));
        allItems.add(new MenuItem("mocha", "mochah.png", "hot"));
         allItems.add(new MenuItem("vanilla muffins ", "vanillamuffins.png", "pasteries"));
        allItems.add(new MenuItem("eggs toast", "eggstoast.png", "savory"));
     
           allItems.add(new MenuItem("mixed plat", "mixedplat.png", "savory"));

           allItems.add(new MenuItem("cocolat crepe", "chocolatcrepe.png", "sweat"));
        allItems.add(new MenuItem("bannana toast ", "bannanatoast.png", "sweat"));
        


       
           allItems.add(new MenuItem("blueberry thyme", "blueberry.png", "tarts"));
           allItems.add(new MenuItem("croissant", "croissant.png", "pasteries"));
        allItems.add(new MenuItem("kiwi vanilla ", "kiwi.png", "tarts"));
        allItems.add(new MenuItem("vanilla choux", "vanilla.png", "tarts"));
      
           allItems.add(new MenuItem("brownies", "brownies.png", "pasteries"));
      
        allItems.add(new MenuItem("chocolat donuts", "chocolatdonuts.png", "pasteries"));
    }
    

    // --- affichage vertical, 2 cartes par ligne
 private void populateList(List<MenuItem> items) {
    listPanel.removeAll();
    listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    listPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

    JPanel currentRow = null;
    int count = 0;

    for (MenuItem it : items) {
        if (count % 2 == 0) {
            currentRow = new JPanel();
            currentRow.setOpaque(false);
            currentRow.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0)); // centered + space between cards
            listPanel.add(currentRow);
            listPanel.add(Box.createVerticalStrut(20)); // space between rows
        }

        JComponent card = buildCard(it);
        card.setPreferredSize(new Dimension(155, 160)); // bigger card size
        currentRow.add(card);

        count++;
    }

    listPanel.revalidate();
    listPanel.repaint();
}


    private JComponent buildCard(MenuItem item) {
        RoundedFillPanel card = new RoundedFillPanel(CARD_BG, 70, 1f);
        card.setPreferredSize(new Dimension(120, 120));
        card.setLayout(new BorderLayout());
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setBorder(new EmptyBorder(15, 15, 15, 8));

        // image centrée
        ImageIcon icon = new ImageIcon(item.imagePath);
        JLabel img = new JLabel(icon);
        img.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(img, BorderLayout.CENTER);

        // titre dessous
        JLabel title = new JLabel(item.name.toUpperCase(), SwingConstants.CENTER);
        title.setFont(ITEM_FONT);
        title.setForeground(new Color(0x3F2F03));
        card.add(title, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openPageFor(item);
            }
        });

        return card;
    }

    private void openPageFor(MenuItem item) {
        String key = item.target.toLowerCase();
        try {
            switch (key) {
                case "hot": new HotCoffee().setVisible(true); break;
                case "cold": new ColdCoffees().setVisible(true); break;
                case "savory": new Savory().setVisible(true); break;
                case "sweat": new sweat().setVisible(true); break;
                case "tarts": new Tarts().setVisible(true); break;
                case "pasteries": new Pasteries().setVisible(true); break;
                case "drinks": new Drinks().setVisible(true); break;
                case "cookies": new Cookies().setVisible(true); break;
                default: new MenuCarousel().setVisible(true); break;
            }
            dispose();
        } catch (Exception ex) {
            new MenuCarousel().setVisible(true);
            dispose();
        }
    }

    // Classes utilitaires
    private static class MenuItem {
        String name, imagePath, target;
        MenuItem(String n, String i, String t) { name = n; imagePath = i; target = t; }
    }

    static class RoundedPanel extends JPanel {
        int r; RoundedPanel(int r){this.r=r;setOpaque(false);}
        protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground()); g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r);
            g2.dispose(); super.paintComponent(g);}
    }

    static class RoundedFillPanel extends JPanel {
        int r; Color fill;
        RoundedFillPanel(Color c,int r,float a){
            this.r=r;this.fill=new Color(c.getRed(),c.getGreen(),c.getBlue(),Math.round(a*255));
            setOpaque(false);
        }
        protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill); g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r);
            g2.dispose(); super.paintComponent(g);}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Recherche().setVisible(true));
    }
}

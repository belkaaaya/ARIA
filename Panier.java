import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;


/** ARIA COFFEE SHOP – CART (Panier) */
public class Panier extends JFrame {

    /* ===================== THEME ===================== */
    private static final Color PHONE_BG   = new Color(0x241B02);
    private static final Color HEADER_BG  = new Color(0x241B02);
    private static final Color CARD_BG    = new Color(0xB9B29F);
    private static final Color TEXT_GOLD  = new Color(0x807043);
    private static final Color TEXT_DARK  = new Color(0x1A1917);
    private static final Color ACCENT_BEIGE = new Color(0xFBF1D6);

    private static final Font  TITLE_FONT  = new Font("Perpetua Titling MT", Font.PLAIN, 26);
    private static final Font  HEADER_FONT = new Font("Perpetua Titling MT", Font.PLAIN, 18);
    private static final Font  ITEM_FONT   = new Font("Perpetua Titling MT", Font.PLAIN, 18);
    private static final Font  META_FONT   = new Font("SansSerif", Font.PLAIN, 13);

    /* ===================== DATA ===================== */
    public static class CartItem {
        private final String title;
        private int qty;
        private final double unitPrice;
        private final String imagePath;

        public CartItem(String title, int qty, double unitPrice, String imagePath) {
            this.title = title;
            this.qty = qty;
            this.unitPrice = unitPrice;
            this.imagePath = imagePath;
        }

        public String getTitle() { return title; }
        public int getQty() { return qty; }
        public void setQty(int q) { qty = q; }
        public double getUnitPrice() { return unitPrice; }
        public String getImagePath() { return imagePath; }
        public double lineTotal() { return qty * unitPrice; }
    }

    public static final List<CartItem> ITEMS = new ArrayList<>();

    /** Ajouter un item au panier (SANS BASE DE DONNÉE) */
    public static void addItem(String title, int quantity, double unitPrice, String imagePath) {
        if (quantity <= 0) return;

        for (CartItem it : ITEMS) {
            if (it.getTitle().equalsIgnoreCase(title)) {
                it.setQty(it.getQty() + quantity);
                return;
            }
        }

       String finalImg = (imagePath == null || imagePath.isEmpty())
        ? "default.png"       // ← mets ton image ici
        : imagePath;

CartItem newItem = new CartItem(title, quantity, unitPrice, finalImg);

        ITEMS.add(newItem);
    }

    /* ===================== UI FIELDS ===================== */
    private JPanel listContainer;
    private JLabel totalLabel;

    /* ===================== CONSTRUCTOR ===================== */
    public Panier() {
        super("ARIA COFFEE SHOP - CART");

        setSize(420, 860);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(0x1A1A1A));
        setContentPane(root);

        RoundedPanel phone = new RoundedPanel(28);
        phone.setBackground(PHONE_BG);
        phone.setLayout(new BorderLayout());
        phone.setBorder(new EmptyBorder(16, 12, 12, 12));
        root.add(phone, BorderLayout.CENTER);

        /* ---------- Header ---------- */
        RoundedFillPanel header = new RoundedFillPanel(HEADER_BG, 40, 1f);
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 56));
        header.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel back = new JLabel("  \u2190  ");
        back.setForeground(Color.WHITE);
        back.setFont(new Font("SansSerif", Font.PLAIN, 18));
        makeClickable(back, this::dispose);
        header.add(back, BorderLayout.WEST);

        JLabel title = new JLabel("CHECK YOUR ORDER", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(TITLE_FONT);
        header.add(title, BorderLayout.CENTER);

        JLabel cartIcon = new JLabel("\uD83D\uDED2  ");
        cartIcon.setForeground(Color.WHITE);
        header.add(cartIcon, BorderLayout.EAST);

        phone.add(header, BorderLayout.NORTH);

        /* ---------- List area ---------- */
        RoundedPanel listBackground = new RoundedPanel(22);
        listBackground.setBackground(CARD_BG);
        listBackground.setLayout(new BorderLayout());
        listBackground.setBorder(new EmptyBorder(12, 12, 12, 12));
        phone.add(listBackground, BorderLayout.CENTER);

        listContainer = new JPanel();
        listContainer.setOpaque(false);
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));

        JScrollPane sc = new JScrollPane(
                listContainer,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        sc.setBorder(null);
        sc.setOpaque(false);
        sc.getViewport().setOpaque(false);
        sc.getVerticalScrollBar().setUnitIncrement(18);
        sc.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));

        listBackground.add(sc, BorderLayout.CENTER);

        /* ---------- Footer ---------- */
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.setBorder(new EmptyBorder(12, 8, 48, 8));

        totalLabel = new JLabel("TOTAL: 0.00 $", SwingConstants.CENTER);
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        totalLabel.setFont(HEADER_FONT);
        totalLabel.setForeground(ACCENT_BEIGE);
        totalLabel.setBorder(new EmptyBorder(8, 0, 8, 0));
        footer.add(totalLabel);

        JButton confirm = new JButton("CONFIRM YOUR ORDER");
        confirm.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirm.setFocusPainted(false);
        confirm.setBackground(ACCENT_BEIGE);
        confirm.setForeground(PHONE_BG);
        confirm.setFont(new Font("SansSerif", Font.BOLD, 14));
        confirm.setBorder(new EmptyBorder(10, 18, 10, 18));

        confirm.addActionListener(e -> {
     for (CartItem item : ITEMS) {
        sendToDatabase(item);
    }     new Map().setVisible(true);
            dispose();
        });

        footer.add(confirm);
        phone.add(footer, BorderLayout.SOUTH);

        refreshList();
    }

    /* ===================== UI METHODS ===================== */
    private JComponent buildCartLine(CartItem it) {
        RoundedFillPanel card = new RoundedFillPanel(CARD_BG, 40, 1f);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(10, 12, 10, 12));
        card.setMaximumSize(new Dimension(340, 120));

        JPanel left = new JPanel(new GridBagLayout());
        left.setOpaque(false);
        JLabel img = new JLabel(thumb(it.getImagePath(), 90, 90));
        left.add(img);
        card.add(left, BorderLayout.WEST);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(0, 10, 0, 10));

        JLabel name = new JLabel(it.getTitle());
        name.setFont(ITEM_FONT);
        name.setForeground(TEXT_GOLD);

        JLabel qtyLblLine = new JLabel("Quantity: " + it.getQty());
        qtyLblLine.setFont(META_FONT);
        qtyLblLine.setForeground(TEXT_DARK);

        JLabel priceLblLine = new JLabel(String.format("Price: %.2f $", it.lineTotal()));
        priceLblLine.setFont(META_FONT);
        priceLblLine.setForeground(TEXT_DARK);

        center.add(name);
        center.add(Box.createVerticalStrut(6));
        center.add(qtyLblLine);
        center.add(priceLblLine);
        card.add(center, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);

        JButton minus = smallRoundBtn("−");
        JLabel qtyLbl  = new JLabel(String.valueOf(it.getQty()));
        qtyLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        JButton plus   = smallRoundBtn("+");
        JButton trash  = flatIconBtn("🗑");

        right.add(minus);
        right.add(qtyLbl);
        right.add(plus);
        right.add(trash);
        card.add(right, BorderLayout.EAST);

        minus.addActionListener(e -> {
            if (it.getQty() > 1) it.setQty(it.getQty() - 1);
            else ITEMS.remove(it);
            refreshList();
        });

        plus.addActionListener(e -> {
            it.setQty(it.getQty() + 1);
            refreshList();
        });

        trash.addActionListener(e -> {
            ITEMS.remove(it);
            refreshList();
        });

        return card;
    }

    private void refreshList() {
        listContainer.removeAll();
        double total = 0.0;

        if (ITEMS.isEmpty()) {
            RoundedFillPanel emptyBox = new RoundedFillPanel(new Color(0xEFE9DC), 24, 1f);
            emptyBox.setLayout(new BoxLayout(emptyBox, BoxLayout.Y_AXIS));
            emptyBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            emptyBox.setBorder(new EmptyBorder(16, 16, 16, 16));
            emptyBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

            JLabel title = new JLabel("YOUR CART IS EMPTY", SwingConstants.CENTER);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            title.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 18));
            title.setForeground(new Color(0x3F2F03));

            JLabel hint = new JLabel("Browse the menu and add something you like ☕️",
                    SwingConstants.CENTER);
            hint.setAlignmentX(Component.CENTER_ALIGNMENT);
            hint.setFont(new Font("SansSerif", Font.PLAIN, 13));
            hint.setForeground(new Color(0x6B5E3D));

            emptyBox.add(title);
            emptyBox.add(Box.createVerticalStrut(6));
            emptyBox.add(hint);

            listContainer.add(Box.createVerticalStrut(12));
            listContainer.add(emptyBox);
            listContainer.add(Box.createVerticalGlue());

            totalLabel.setText("TOTAL: 0.00 $");

        } else {
            for (CartItem it : ITEMS) {
                total += it.lineTotal();
                listContainer.add(buildCartLine(it));
                listContainer.add(Box.createVerticalStrut(8));
            }
            totalLabel.setText(String.format("TOTAL: %.2f $", total));
        }

        listContainer.revalidate();
        listContainer.repaint();
    }

    private static void makeClickable(JComponent c, Runnable action) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { action.run(); }
        });
    }

    private static ImageIcon thumb(String path, int w, int h) {
        ImageIcon ic = new ImageIcon(path);
        if (ic.getIconWidth() <= 0 || ic.getIconHeight() <= 0) return ic;

        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = out.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double sx = (double) w / ic.getIconWidth();
        double sy = (double) h / ic.getIconHeight();
        double s  = Math.min(sx, sy);
        int nw = (int) Math.round(ic.getIconWidth() * s);
        int nh = (int) Math.round(ic.getIconHeight() * s);
        int x = (w - nw) / 2;
        int y = (h - nh) / 2;

        g2.drawImage(ic.getImage(), x, y, nw, nh, null);
        g2.dispose();
        return new ImageIcon(out);
    }

    private JButton smallRoundBtn(String t){
        JButton b = new JButton(t);
        b.setFocusPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(HEADER_BG);
        b.setBorder(BorderFactory.createEmptyBorder(4,10,4,10));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton flatIconBtn(String t){
        JButton b = new JButton(t);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorder(BorderFactory.createEmptyBorder(4,6,4,6));
        b.setForeground(TEXT_GOLD);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    static class RoundedPanel extends JPanel {
        private final int r;
        RoundedPanel(int r) { this.r = r; setOpaque(false); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedFillPanel extends JPanel {
        private final int r;
        private final Color fill;
        RoundedFillPanel(Color c, int r, float alpha) {
            this.r = r;
            this.fill = new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.round(alpha * 255));
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), r, r);
            g2.dispose();
            super.paintComponent(g);
        }
    }
public void sendToDatabase(CartItem item) {
    try {
        String url = "http://localhost/CoffeeApp/save_purchase.php";
String params =
    "item_name=" + item.title +
    "&price=" + item.unitPrice +
    "&quantity=" + item.qty 
    ;


        java.net.URL u = new java.net.URL(url);
        java.net.HttpURLConnection conn =
            (java.net.HttpURLConnection) u.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        java.io.OutputStream os = conn.getOutputStream();
        os.write(params.getBytes());
        os.flush();
        os.close();

        System.out.println("Saved to DB: " + conn.getResponseMessage());

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Panier().setVisible(true));
    }
}

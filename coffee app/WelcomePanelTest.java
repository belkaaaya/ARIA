
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WelcomePanelTest extends JFrame {

    public WelcomePanelTest() {
        super("Login - ARIA COFFEE SHOP");

        // couleurs
        Color bgFull     = new Color(0xFBF1D6);   // beige sur toute la page
        Color fieldBg    = new Color(0xD8D3C6);   // fond des champs/boutons
        Color textColor  = new Color(0x1A1A1A);   // texte foncé
        Color loginColor = new Color(0x3F2F03);   // bouton Log in

        // fenêtre format mobile
        setSize(420, 860);           // iPhone style
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // panel principal plein écran
        JPanel screen = new JPanel();
        screen.setBackground(bgFull);
        screen.setLayout(new BoxLayout(screen, BoxLayout.Y_AXIS));
        screen.setBorder(new EmptyBorder(32, 24, 32, 24));
        setContentPane(screen);

        // ====== 1. TOP: logo + welcome text ======
        JPanel topBlock = new JPanel();
        topBlock.setOpaque(false);
        topBlock.setLayout(new BoxLayout(topBlock, BoxLayout.Y_AXIS));
        topBlock.setAlignmentX(Component.CENTER_ALIGNMENT);

        // logo
        ImageIcon icon = new ImageIcon("aria logo.png");
        Image scaled = icon.getImage().getScaledInstance(203, 305, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaled));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcome1 = new JLabel("Welcome to");
        welcome1.setForeground(textColor);
        welcome1.setFont(new Font("SansSerif", Font.PLAIN, 16));
        welcome1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcome2 = new JLabel("ARIA COFFEE SHOP");
        welcome2.setForeground(textColor);
        welcome2.setFont(new Font("SansSerif", Font.BOLD, 16));
        welcome2.setAlignmentX(Component.CENTER_ALIGNMENT);

        topBlock.add(Box.createVerticalStrut(8));
        topBlock.add(logo);
        topBlock.add(Box.createVerticalStrut(16));
        topBlock.add(welcome1);
        topBlock.add(welcome2);
        topBlock.add(Box.createVerticalStrut(24));

        // ====== 2. GOOGLE SIGN IN ======
        RoundedPanel googleBtn = new RoundedPanel(new BorderLayout(), 25);
        googleBtn.setBackground(fieldBg);
        googleBtn.setBorder(new EmptyBorder(12,16,12,16));
        googleBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        googleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel gIcon = new JLabel("G");
        gIcon.setFont(new Font("SansSerif", Font.BOLD, 16));
        gIcon.setForeground(textColor);
        gIcon.setBorder(new EmptyBorder(0,0,0,8));

        JLabel gText = new JLabel("Sign in with Google");
        gText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gText.setForeground(textColor);

        JPanel googleInner = new JPanel();
        googleInner.setOpaque(false);
        googleInner.setLayout(new BoxLayout(googleInner, BoxLayout.X_AXIS));
        googleInner.add(gIcon);
        googleInner.add(gText);

        googleBtn.add(googleInner, BorderLayout.CENTER);

        // ====== 3. SEPARATOR "Or" ======
        JPanel separator = new JPanel();
        separator.setOpaque(false);
        separator.setLayout(new BoxLayout(separator, BoxLayout.X_AXIS));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel leftLine  = new JLabel("────────");
        JLabel orLabel   = new JLabel("  Or  ");
        JLabel rightLine = new JLabel("────────");
        leftLine.setForeground(Color.GRAY);
        rightLine.setForeground(Color.GRAY);
        orLabel.setForeground(Color.DARK_GRAY);
        separator.add(leftLine);
        separator.add(orLabel);
        separator.add(rightLine);

        // ====== 4. USER FIELD ======
        JPanel userBlock = new JPanel();
        userBlock.setOpaque(false);
        userBlock.setLayout(new BoxLayout(userBlock, BoxLayout.Y_AXIS));
        userBlock.setAlignmentX(Component.CENTER_ALIGNMENT);
JLabel userLabel = new JLabel("Username or Email");
        userLabel.setFont(new Font("Arial Rounded MT", Font.PLAIN, 14));
        userLabel.setForeground(textColor);

        RoundedTextField userField = new RoundedTextField(fieldBg);
        userField.setPreferredSize(new Dimension(320, 44));
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        userField.setBorder(new EmptyBorder(10,16,10,16));
        userField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userField.setForeground(textColor);

        userBlock.add(userLabel);
        userBlock.add(Box.createVerticalStrut(6));
        userBlock.add(userField);
// ====== 5. PASSWORD FIELD ======
JPanel passBlock = new JPanel();
passBlock.setOpaque(false);
passBlock.setLayout(new BoxLayout(passBlock, BoxLayout.Y_AXIS));
passBlock.setAlignmentX(Component.CENTER_ALIGNMENT);

JLabel passLabel = new JLabel("Password");
passLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
passLabel.setForeground(textColor);

// Same rounded style as userField
RoundedPasswordField passField = new RoundedPasswordField(fieldBg);
passField.setPreferredSize(new Dimension(320, 44));
passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
passField.setBorder(new EmptyBorder(10,16,10,16)); // same padding as userField
passField.setFont(new Font("SansSerif", Font.PLAIN, 14));
passField.setForeground(textColor);

passBlock.add(passLabel);
passBlock.add(Box.createVerticalStrut(6));
passBlock.add(passField);


// ====== 6. LOGIN BUTTON ======
RoundedButton loginBtn = new RoundedButton("Log in", loginColor, Color.WHITE);
loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
loginBtn.setPreferredSize(new Dimension(320, 52));
loginBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

// Action quand on clique sur Log in
loginBtn.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // ouvrir la page menu
        MenuCarousel menu = new MenuCarousel();
        menu.setVisible(true);

        // fermer la page de login
        dispose();
    }
});

        // ====== 7. AJOUT DANS L'ÉCRAN ======
        screen.add(topBlock);
        screen.add(Box.createVerticalStrut(8));
        screen.add(googleBtn);
        screen.add(Box.createVerticalStrut(16));
        screen.add(separator);
        screen.add(Box.createVerticalStrut(16));
        screen.add(userBlock);
        screen.add(Box.createVerticalStrut(16));
        screen.add(passBlock);
        screen.add(Box.createVerticalStrut(24));
        screen.add(loginBtn);
        screen.add(Box.createVerticalGlue());
    }

    // ======= classes utilitaires =======

    // panel arrondi réutilisable (pour Google btn, password wrapper)
    static class RoundedPanel extends JPanel {
        private final int arc;
        RoundedPanel(LayoutManager layout, int arc) {
            super(layout);
            this.arc = arc;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
g2.fillRoundRect(0,0,getWidth(),getHeight(),arc,arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // champ texte arrondi
    static class RoundedTextField extends JTextField {
        private final Color bg;
        RoundedTextField(Color bg) {
            this.bg = bg;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),25,25);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // champ password arrondi
    static class RoundedPasswordField extends JPasswordField {
        private final Color bg;
        RoundedPasswordField(Color bg) {
            this.bg = bg;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),25,25);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // bouton arrondi "Log in"
    static class RoundedButton extends JButton {
        private final Color bg;
        private final Color fg;
        RoundedButton(String text, Color bg, Color fg) {
            super(text);
            this.bg = bg;
            this.fg = fg;
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0,0,getWidth(),getHeight(),28,28);
            g2.dispose();
            super.paintComponent(g);
        }
        @Override
        protected void paintBorder(Graphics g) {}
        @Override
        public void paintChildren(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            String text = getText();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();
            g2.setColor(fg);
            g2.drawString(
                text,
                (getWidth() - textWidth) / 2,
                (getHeight() + textHeight) / 2 - 4
            );
            g2.dispose();
        }
    }

    // main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WelcomePanelTest frame = new WelcomePanelTest();
            frame.setVisible(true);
        });
    }
    // =====================================================================
// IMAGE UTILITY CLASS – high-quality scaling & caching
// =====================================================================
static class Img {
    private static final java.util.Map<String, ImageIcon> CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    private static java.awt.image.BufferedImage load(String path) throws java.io.IOException {
        java.net.URL url = Img.class.getResource("/" + path);
        if (url != null) return javax.imageio.ImageIO.read(url);
        return javax.imageio.ImageIO.read(new java.io.File(path));
    }

    public static ImageIcon hiQualityScaledIcon(String path, int targetW, int targetH) {
        String key = path + "@" + targetW + "x" + targetH;
        ImageIcon cached = CACHE.get(key);
        if (cached != null) return cached;

        try {
            java.awt.image.BufferedImage src = load(path);
            java.awt.image.BufferedImage scaled = progressiveDownscale(src, targetW, targetH);
            ImageIcon icon = new ImageIcon(scaled);
            CACHE.put(key, icon);
            return icon;
        } catch (java.io.IOException e) {
            return new ImageIcon(new java.awt.image.BufferedImage(targetW, targetH, java.awt.image.BufferedImage.TYPE_INT_ARGB));
        }
    }

    private static java.awt.image.BufferedImage progressiveDownscale(java.awt.image.BufferedImage src, int targetW, int targetH) {
        int type = src.getTransparency() == java.awt.Transparency.OPAQUE
                ? java.awt.image.BufferedImage.TYPE_INT_RGB
                : java.awt.image.BufferedImage.TYPE_INT_ARGB;

        java.awt.image.BufferedImage img = src;
        int w = src.getWidth();
        int h = src.getHeight();

        do {
            int w2 = Math.max(targetW, w / 2);
            int h2 = Math.max(targetH, h / 2);
            java.awt.image.BufferedImage tmp = new java.awt.image.BufferedImage(w2, h2, type);
            java.awt.Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2.drawImage(img, 0, 0, w2, h2, null);
            g2.dispose();
            img = tmp;
            w = w2; h = h2;
        } while (w != targetW || h != targetH);

        java.awt.image.BufferedImage out = new java.awt.image.BufferedImage(targetW, targetH, type);
        java.awt.geom.AffineTransform at = java.awt.geom.AffineTransform.getScaleInstance(1.0, 1.0);
        java.awt.image.AffineTransformOp op = new java.awt.image.AffineTransformOp(at, java.awt.image.AffineTransformOp.TYPE_BICUBIC);
        op.filter(img, out);
        return out;
    }
}

}

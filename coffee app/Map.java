import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Map extends JFrame {

    // ===== THEME (same family as your app) =====
    private static final Color PHONE_BG   = new Color(0x241B02);   // brown
    private static final Color BEIGE      = new Color(0xFBF1D6);   // beige
    private static final Color TEXT_GOLD  = new Color(0x807043);   // gold-ish
    private static final Font  HEADER_F   = new Font("Perpetua Titling MT", Font.PLAIN, 22);
    private static final Font  TITLE_F    = new Font("Perpetua Titling MT", Font.PLAIN, 26);
    private static final Font  BODY_F     = new Font("SansSerif", Font.PLAIN, 13);

    // 0 = confirmed, 1 = on the way, 2 = delivered
    private int stage = 0;
    private Stepper stepper;

    public Map() {
        super("ARIA COFFEE SHOP - TRACKING");
        setSize(420, 860);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(0x1A1A1A));
        setContentPane(root);

        RoundedPanel phone = new RoundedPanel(28);
        phone.setBackground(PHONE_BG);
        phone.setLayout(new BorderLayout());
        phone.setBorder(new EmptyBorder(16, 12, 12, 12));
        root.add(phone, BorderLayout.CENTER);

        // ===== HEADER =====
        JPanel header = new RoundedFill(BEIGE, 40, 1f);
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(8, 12, 8, 12));
        JLabel back = new JLabel("  \u2190  ");
        back.setForeground(THEME_DARK());
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addMouseListener(new MouseAdapter(){ @Override public void mouseClicked(MouseEvent e){ dispose(); }});
        JLabel title = new JLabel("FOLLOW YOUR ORDER", SwingConstants.CENTER);
        title.setFont(TITLE_F);
        title.setForeground(THEME_DARK());
        JLabel cart = new JLabel("\uD83D\uDED2  ");
        cart.setForeground(THEME_DARK());

        header.add(back, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(cart, BorderLayout.EAST);
        phone.add(header, BorderLayout.NORTH);

        // ===== CENTER CONTENT =====
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(new EmptyBorder(12, 8, 12, 8));
        phone.add(center, BorderLayout.CENTER);

        // --- Map image (top) ---
        // Put your image file (e.g. "map.png") in the same folder as the .class/.java and set the path below.
        String MAP_PATH = "mapp.png";   // <<<<<<<<<<<<<< REPLACE with your file (e.g. "map.jpg")
        JLabel mapImg = new JLabel(scaleKeep(MAP_PATH, 450, 294));  // W=450, H=294 (as you asked)
        JPanel mapWrap = new RoundedFill(BEIGE, 28, 1f);
        mapWrap.setLayout(new GridBagLayout());
        mapWrap.add(mapImg);
        mapWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(mapWrap);
        center.add(Box.createVerticalStrut(14));

        // --- Stepper (vertical line with 3 phases) ---
        stepper = new Stepper();
        stepper.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(stepper);
        center.add(Box.createVerticalStrut(14));

        // --- Thank-you box (bottom of center) ---
        JPanel thanks = new RoundedFill(BEIGE, 26, 1f);
        thanks.setLayout(new BoxLayout(thanks, BoxLayout.Y_AXIS));
        thanks.setBorder(new EmptyBorder(16, 18, 16, 18));
        JLabel t1 = new JLabel("<html><div style='text-align:center;'>THANK YOU FOR TRUSTING OUR SERVICE.<br>" +
                "WE HOPE EVERYTHING WENT SMOOTHLY.<br>" +
                "WE’RE GLAD YOU CHOSE ARIA COFFEE SHOP.<br>" +
                "LOOKING FORWARD TO YOUR NEXT ORDER ☕</div></html>", SwingConstants.CENTER);
        t1.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 16));
        t1.setForeground(THEME_DARK());
        t1.setAlignmentX(Component.CENTER_ALIGNMENT);
        thanks.add(t1);
        center.add(thanks);

        // ===== FOOTER: manual status advance (optional) =====
        // You said you want manual control. This button cycles the stage.
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        JButton next = new JButton("UPDATE STATUS ▸");
        next.setFocusPainted(false);
        next.setBackground(BEIGE);
        next.setForeground(THEME_DARK());
        next.setFont(new Font("SansSerif", Font.BOLD, 13));
        next.addActionListener(e -> {
            stage = (stage + 1) % 3;   // 0→1→2→0….
            stepper.setStage(stage);
        });
        footer.add(next);
        phone.add(footer, BorderLayout.SOUTH);

        // initial status
        stepper.setStage(stage);
    }

    // ===== Stepper component (vertical line with 3 dots + labels) =====
    private class Stepper extends JPanel {
        private final String[] labels = {"CONFIRMED", "ON THE WAY", "DELIVERED"};
        private int current = 0;

        Stepper() {
            setOpaque(false);
            setPreferredSize(new Dimension(460, 200));
        }
        void setStage(int s) { current = Math.max(0, Math.min(2, s)); repaint(); }
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int left = 60;
    int right = getWidth() - 60;
    int cy = getHeight() / 2;

    // draw horizontal line
    g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    g2.setColor(new Color(255, 255, 255, 60));
    g2.drawLine(left, cy, right, cy);

    // positions for 3 steps
    int[] xs = {
        left,
        left + (right - left) / 2,
        right
    };

    for (int i = 0; i < 3; i++) {
        int x = xs[i];
        // outer circle
        g2.setColor(i <= current ? BEIGE : new Color(255, 255, 255, 90));
        g2.fillOval(x - 12, cy - 12, 24, 24);
        // inner dot
        g2.setColor(i <= current ? THEME_DARK() : new Color(0, 0, 0, 80));
        g2.fillOval(x - 6, cy - 6, 12, 12);

        // label (below the circle)
        g2.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 18));
        g2.setColor(new Color(255, 255, 255, 210));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(labels[i]);
        g2.drawString(labels[i], x - textWidth / 2, cy + 40);
    }

    g2.dispose();
}

    }

    // ===== helpers =====
    private static Color THEME_DARK(){ return new Color(0x241B02); }

    private static ImageIcon scaleKeep(String path, int w, int h) {
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
        int nw = (int)Math.round(ic.getIconWidth()*s);
        int nh = (int)Math.round(ic.getIconHeight()*s);
        int x = (w - nw)/2, y = (h - nh)/2;
        g2.drawImage(ic.getImage(), x, y, nw, nh, null);
        g2.dispose();
        return new ImageIcon(out);
    }

    static class RoundedPanel extends JPanel {
        private final int r; RoundedPanel(int r){ this.r=r; setOpaque(false); }
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r);
            g2.dispose(); super.paintComponent(g);
        }
    }
    static class RoundedFill extends JPanel {
        private final int r; private final Color fill;
        RoundedFill(Color c,int r,float a){ this.r=r; this.fill=new Color(c.getRed(),c.getGreen(),c.getBlue(),Math.round(a*255)); setOpaque(false);}
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fill); g2.fillRoundRect(0,0,getWidth(),getHeight(),r,r); g2.dispose(); super.paintComponent(g);
        }
    }

    // quick test
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Map().setVisible(true));
    }
}

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SplashScreen extends JFrame {

    private BufferedImage bgImage;   // background image

    public SplashScreen() {
        super("ARIA COFFEE SHOP");

        // taille style mobile
        setSize(420, 860);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ====== 1. charger l’image de fond ======
        // mets ton image dans le même dossier et change le nom ici
        try {
            // tu peux changer "splash_bg.jpg" par le vraiw nom : "splash.png" ...
            bgImage = ImageIO.read(new File("coffeeshopback2.jpg"));
        } catch (IOException e) {
            bgImage = null; // on mettra un fond beige si pas d'image
        }

        // ====== 2. panel de fond qui dessine l’image ======
        BackgroundPanel root = new BackgroundPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(60, 32, 40, 32));
        setContentPane(root);

        // ====== 3. logo (un peu plus grand) ======
        ImageIcon icon = new ImageIcon("aria logo.png");
        // agrandi légèrement
        Image scaled = icon.getImage().getScaledInstance(210, 300, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaled));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ====== 4. textes ======
        // essayer d'utiliser Perpetua Titling MT
        Font titleFont;
     
        try {
            titleFont = new Font("Perpetua Titling MT", Font.BOLD, 22);
        
        } catch (Exception ex) {
            // fallback
            titleFont = new Font("SansSerif", Font.BOLD, 22);
          
        }

        JLabel title = new JLabel("ARIA COFFEE SHOP");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(0x1A1917)); // blanc pour bien ressortir sur la photo
        title.setFont(titleFont);

       JLabel title2 = new JLabel("Fresh brews, sweet vibes. Delivered to you.");


        title2.setAlignmentX(Component.CENTER_ALIGNMENT);
        title2.setForeground(new Color(0x1A1917));
        title2.setFont(titleFont.deriveFont(Font.BOLD, 10f));



        // ====== 5. loader ======
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setMaximumSize(new Dimension(200, 14));
        bar.setAlignmentX(Component.CENTER_ALIGNMENT);
        bar.setForeground(new Color(0x3F2F03));
        bar.setBackground(new Color(0x3F2F03));

        // ====== 6. ajout dans le panel ======
        root.add(Box.createVerticalGlue());
        root.add(logo);
        root.add(Box.createVerticalStrut(16));
        root.add(title);
        root.add(Box.createVerticalStrut(4));
        root.add(title2);
        root.add(Box.createVerticalStrut(12));
     
        root.add(Box.createVerticalStrut(32));
        root.add(bar);
        root.add(Box.createVerticalGlue());

        // ====== 7. timer 5s -> login ======
        Timer t = new Timer(5000, e -> {
            new WelcomePanelTest().setVisible(true);
            dispose();
        });
        t.setRepeats(false);
        t.start();
    }

    // ===== panel de fond qui dessine l’image en bonne qualité =====
    class BackgroundPanel extends JPanel {
        BackgroundPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // on étire l’image pour couvrir tout le splash (420x860)
                g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
                g2.dispose();
            } else {
                // si pas d'image, fond beige
                g.setColor(new Color(0xFBF1D6));
                g.fillRect(0, 0, getWidth(), getHeight());
         
            }
   
        }

    }

    // pour tester seul
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SplashScreen().setVisible(true);
        });
    }
}

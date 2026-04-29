package uk.ac.mmu.advprog.assessment.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TopBarPanel extends JPanel {
    private JLabel lblTitle, lblCreated;
    private JPanel leftPanel;

    public TopBarPanel() {

    	setLayout(new BorderLayout());
    	setBackground(new Color(255, 136, 90));
    	setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    	// Left side: Title and creation date
        lblTitle = new JLabel("WineTime");
        lblTitle.setFont(new Font("Grandstander", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);


        lblCreated = new JLabel("Est. 2026");
        lblCreated.setFont(new Font("Grandstander", Font.PLAIN, 18));
        lblCreated.setForeground(Color.WHITE);

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.add(lblTitle);
        leftPanel.add(lblCreated);

        add(leftPanel, BorderLayout.WEST);

        // Right side: Logo
        ImageIcon originalLogo = new ImageIcon(getClass().getResource("/assets/logo.png"));
        Image scaledLogo = originalLogo.getImage().getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
        ImageIcon logo = new ImageIcon(scaledLogo);
        add(new JLabel(logo), BorderLayout.EAST);
    }
}

package customer_ui;

import java.awt.*;
import javax.swing.*;

public class CustomerPortal extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JButton activeButton = null;

    public CustomerPortal() {
        setTitle("Automotive Service Centre – Customer Portal");
        setSize(1050, 680);
        setMinimumSize(new Dimension(900, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(createHeader(), BorderLayout.NORTH);
        add(createMainArea(), BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 20, 100));
        header.setPreferredSize(new Dimension(0, 52));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel title = new JLabel("Automotive Service Centre  –  Customer Portal");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 15));
        header.add(title, BorderLayout.WEST);

        return header;
    }

    private JPanel createMainArea() {
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(Color.WHITE);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(248, 248, 250));

        contentPanel.add(placeholder("Home"), "home");
        contentPanel.add(placeholder("Our Service"), "ourService");
        contentPanel.add(placeholder("Appointment & History"), "history");
        contentPanel.add(placeholder("Make Appointment"), "makeAppointment");
        contentPanel.add(placeholder("Profile"), "profile");

        mainArea.add(createSidebar(), BorderLayout.WEST);
        mainArea.add(contentPanel, BorderLayout.CENTER);

        return mainArea;
    }

    private JPanel placeholder(String name) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(248, 248, 250));
        JLabel lbl = new JLabel(name + " – Coming Soon");
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        lbl.setForeground(new Color(150, 150, 150));
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1,
                new Color(230, 230, 230)));

        // Logo block — 白底 + Petronas Teal 字
        JPanel logoBlock = new JPanel();
        logoBlock.setLayout(new BoxLayout(logoBlock, BoxLayout.Y_AXIS));
        logoBlock.setBackground(Color.WHITE);
        logoBlock.setMaximumSize(new Dimension(210, 90));
        logoBlock.setMinimumSize(new Dimension(210, 90));
        logoBlock.setPreferredSize(new Dimension(210, 90));
        logoBlock.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(18, 18, 14, 0)
        ));

        JLabel logoMain = new JLabel("🏎  AMG");
        logoMain.setFont(new Font("Arial", Font.BOLD, 20));
        logoMain.setForeground(new Color(0, 210, 190));
        logoMain.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoSub = new JLabel("Customer Portal");
        logoSub.setFont(new Font("Arial", Font.PLAIN, 11));
        logoSub.setForeground(new Color(150, 150, 150));
        logoSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        logoBlock.add(logoMain);
        logoBlock.add(Box.createRigidArea(new Dimension(0, 4)));
        logoBlock.add(logoSub);
        sidebar.add(logoBlock);

        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));

        // Section label
        JLabel sectionLabel = new JLabel("  MENU");
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 10));
        sectionLabel.setForeground(new Color(180, 180, 180));
        sectionLabel.setMaximumSize(new Dimension(210, 24));
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(0, 14, 6, 0));
        sidebar.add(sectionLabel);

        // Nav buttons
        String[][] navItems = {
            {"Home", "home"},
            {"Our Service", "ourService"},
            {"Appointment & History", "history"},
            {"Make Appointment", "makeAppointment"},
            {"Profile", "profile"},
        };

        for (String[] item : navItems) {
            JButton btn = createNavButton(item[0]);
            String key = item[1];
            btn.addActionListener(e -> {
                cardLayout.show(contentPanel, key);
                setActiveButton(btn);
            });
            sidebar.add(btn);
        }

        sidebar.add(Box.createVerticalGlue());

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(210, 1));
        sep.setForeground(new Color(230, 230, 230));
        sidebar.add(sep);
        sidebar.add(Box.createRigidArea(new Dimension(0, 6)));

        JButton logoutBtn = createNavButton("Logout");
        logoutBtn.setForeground(new Color(200, 60, 60));
        logoutBtn.addActionListener(e -> handleLogout());
        sidebar.add(logoutBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 12)));

        return sidebar;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(210, 42));
        btn.setMinimumSize(new Dimension(210, 42));
        btn.setPreferredSize(new Dimension(210, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(50, 50, 60));
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(new Color(245, 245, 250));
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(Color.WHITE);
                }
            }
        });

        return btn;
    }

    private void setActiveButton(JButton selected) {
        if (activeButton != null) {
            activeButton.setBackground(Color.WHITE);
            activeButton.setForeground(new Color(50, 50, 60));
            activeButton.setFont(new Font("Arial", Font.PLAIN, 13));
            activeButton.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        }
        selected.setBackground(new Color(237, 240, 255));
        selected.setForeground(new Color(20, 20, 100));
        selected.setFont(new Font("Arial", Font.BOLD, 13));
        selected.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(20, 20, 100)),
            BorderFactory.createEmptyBorder(0, 14, 0, 0)
        ));
        activeButton = selected;
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
        bar.setBackground(new Color(248, 248, 250));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
                new Color(220, 220, 220)));
        bar.setPreferredSize(new Dimension(0, 30));

        JLabel userLabel = new JLabel("User: John Doe");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        userLabel.setForeground(new Color(100, 100, 110));

        JLabel sep = new JLabel("   |   ");
        sep.setForeground(new Color(200, 200, 200));
        sep.setFont(new Font("Arial", Font.PLAIN, 11));

        JLabel dateLabel = new JLabel("Sun, 29 Mar 2026");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        dateLabel.setForeground(new Color(100, 100, 110));

        bar.add(Box.createHorizontalStrut(16));
        bar.add(userLabel);
        bar.add(sep);
        bar.add(dateLabel);

        return bar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerPortal());
    }
}
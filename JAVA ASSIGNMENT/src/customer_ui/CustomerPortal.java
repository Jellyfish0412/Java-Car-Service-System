package customer_ui;

import customer_function.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class CustomerPortal extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JButton activeButton = null;

    // temporary hardcoded customer ID
    private String customerID = "C001";

    private CustomerDAO customerDAO = new CustomerDAO();
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private TechnicianDAO technicianDAO = new TechnicianDAO();

    private Customer customer;
    private Appointment upcoming;

    public CustomerPortal() {
        // 先拿数据
        customer = customerDAO.getCustomerByID(customerID);
        upcoming = appointmentDAO.getUpcomingAppointmentByCustomerID(customerID);

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

        contentPanel.add(createHomePage(), "home");
        contentPanel.add(placeholder("Our Service"), "ourService");
        contentPanel.add(placeholder("Appointment & History"), "history");
        contentPanel.add(placeholder("Make Appointment"), "makeAppointment");
        contentPanel.add(placeholder("Profile"), "profile");

        mainArea.add(createSidebar(), BorderLayout.WEST);
        mainArea.add(contentPanel, BorderLayout.CENTER);

        return mainArea;
    }

    private JPanel createHomePage() {
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBackground(new Color(248, 248, 250));
        homePanel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + (customer != null ? customer.getName() : ""));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(20, 20, 100));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        homePanel.add(welcomeLabel);
        homePanel.add(Box.createRigidArea(new Dimension(0, 18)));

        // ===== APPOINTMENT CARD =====
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        // Card header
        JPanel cardHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        cardHeader.setBackground(new Color(20, 20, 100));
        JLabel cardTitle = new JLabel("Next Upcoming Appointment");
        cardTitle.setForeground(Color.WHITE);
        cardTitle.setFont(new Font("Arial", Font.BOLD, 13));
        cardHeader.add(cardTitle);
        card.add(cardHeader, BorderLayout.NORTH);

        // Card content
        JPanel cardContent = new JPanel(new GridLayout(6, 2, 10, 8));
        cardContent.setBackground(Color.WHITE);
        cardContent.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        if (upcoming != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
            String formattedDate = upcoming.getAppointmentDate().format(formatter);
            String techName = technicianDAO.getTechnicianName(upcoming.getTechnicianID());
            String vehicle = upcoming.getVehicleBrand() + " " + upcoming.getVehicleModel()
                           + " – " + upcoming.getVehiclePlate();

            addCardRow(cardContent, "Appointment Date:", formattedDate);
            addCardRow(cardContent, "Appointment Time:", upcoming.getStartTime());
            addCardRow(cardContent, "Service Type:", upcoming.getServiceType());
            addCardRow(cardContent, "Technician Assigned:", techName != null ? techName : "N/A");
            addCardRow(cardContent, "Vehicle:", vehicle);
            addCardRow(cardContent, "Status:", upcoming.getStatus());
        } else {
            JLabel noAppt = new JLabel("No upcoming appointment found.");
            noAppt.setForeground(Color.GRAY);
            cardContent.add(noAppt);
        }

        card.add(cardContent, BorderLayout.CENTER);

        // Reminder
        JPanel reminderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        reminderPanel.setBackground(new Color(255, 255, 220));
        reminderPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 210, 100)));
        JLabel reminderLabel = new JLabel("⚠  Reminder: Please arrive 10 minutes early. Bring your vehicle registration card.");
        reminderLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        reminderPanel.add(reminderLabel);
        card.add(reminderPanel, BorderLayout.SOUTH);

        homePanel.add(card);
        homePanel.add(Box.createRigidArea(new Dimension(0, 18)));

        // ===== BOTTOM BUTTONS =====
        JPanel bottomButtons = new JPanel(new GridLayout(1, 2, 14, 0));
        bottomButtons.setBackground(new Color(248, 248, 250));
        bottomButtons.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomButtons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JButton bookBtn = createActionButton("📅  Book New Service");
        JButton pastBtn = createActionButton("📋  View Past Services");

        bookBtn.addActionListener(e -> cardLayout.show(contentPanel, "makeAppointment"));
        pastBtn.addActionListener(e -> cardLayout.show(contentPanel, "history"));

        bottomButtons.add(bookBtn);
        bottomButtons.add(pastBtn);
        homePanel.add(bottomButtons);

        return homePanel;
    }

    private void addCardRow(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setForeground(new Color(100, 100, 110));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.PLAIN, 12));
        val.setForeground(new Color(30, 30, 40));

        panel.add(lbl);
        panel.add(val);
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(50, 50, 60));
        btn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 70));
        return btn;
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

        // Logo block
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

        // 从 customer 对象读取名字
        String name = (customer != null) ? customer.getName() : "Unknown";
        String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"));

        JLabel userLabel = new JLabel("User: " + name);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        userLabel.setForeground(new Color(100, 100, 110));

        JLabel sep = new JLabel("   |   ");
        sep.setForeground(new Color(200, 200, 200));
        sep.setFont(new Font("Arial", Font.PLAIN, 11));

        JLabel dateLabel = new JLabel(todayDate);
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
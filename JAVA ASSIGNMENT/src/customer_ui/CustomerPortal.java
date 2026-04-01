package customer_ui;

import customer_function.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;

public class CustomerPortal extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JButton activeButton = null;

    private String customerID = "C001";

    private CustomerDAO customerDAO = new CustomerDAO();
    private AppointmentDAO appointmentDAO = new AppointmentDAO();
    private TechnicianDAO technicianDAO = new TechnicianDAO();
    private FeedbackDAO feedbackDAO = new FeedbackDAO();
    private ServiceItemDAO serviceItemDAO = new ServiceItemDAO();

    private Customer customer;
    private Appointment upcoming;

    // 保存 home page 的引用，方便刷新
    private JPanel homePagePanel;

    public CustomerPortal() {
        customer = customerDAO.getCustomerByID(customerID);
        upcoming = appointmentDAO.getUpcomingAppointmentByCustomerID(customerID);

        setTitle("Automotive Service Centre – Customer Portal");
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 650));
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

        homePagePanel = createHomePage();
        contentPanel.add(homePagePanel, "home");
        contentPanel.add(placeholder("Our Service"), "ourService");
        contentPanel.add(createHistoryPage(), "history");
        contentPanel.add(createMakeAppointmentPage(), "makeAppointment");
        contentPanel.add(placeholder("Profile"), "profile");

        mainArea.add(createSidebar(), BorderLayout.WEST);
        mainArea.add(contentPanel, BorderLayout.CENTER);

        return mainArea;
    }

    // ─── HOME PAGE ───────────────────────────────────────────────────────────

    private JPanel createHomePage() {
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBackground(new Color(248, 248, 250));
        homePanel.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JLabel welcomeLabel = new JLabel("Welcome, " + (customer != null ? customer.getName() : ""));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setForeground(new Color(20, 20, 100));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        homePanel.add(welcomeLabel);
        homePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JPanel cardHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 10));
        cardHeader.setBackground(new Color(20, 20, 100));
        JLabel cardTitle = new JLabel("Next Upcoming Appointment");
        cardTitle.setForeground(Color.WHITE);
        cardTitle.setFont(new Font("Arial", Font.BOLD, 13));
        cardHeader.add(cardTitle);
        card.add(cardHeader, BorderLayout.NORTH);

        JPanel cardContent = new JPanel(new GridLayout(6, 2, 10, 10));
        cardContent.setBackground(Color.WHITE);
        cardContent.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        // 每次重新读取最新 upcoming
        upcoming = appointmentDAO.getUpcomingAppointmentByCustomerID(customerID);

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
            noAppt.setFont(new Font("Arial", Font.PLAIN, 13));
            noAppt.setForeground(Color.GRAY);
            cardContent.add(noAppt);
        }

        card.add(cardContent, BorderLayout.CENTER);

        JPanel reminderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        reminderPanel.setBackground(new Color(255, 255, 220));
        reminderPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 210, 100)));
        JLabel reminderLabel = new JLabel("⚠  Reminder: Please arrive 10 minutes early. Bring your vehicle registration card.");
        reminderLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        reminderPanel.add(reminderLabel);
        card.add(reminderPanel, BorderLayout.SOUTH);

        homePanel.add(card);
        homePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel bottomButtons = new JPanel(new GridLayout(1, 2, 16, 0));
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

    // 刷新 Home 页面（预约成功后调用）
    private void refreshHomePage() {
        contentPanel.remove(homePagePanel);
        homePagePanel = createHomePage();
        contentPanel.add(homePagePanel, "home");
        cardLayout.show(contentPanel, "home");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ─── MAKE APPOINTMENT PAGE ───────────────────────────────────────────────

    private JPanel createMakeAppointmentPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(new Color(248, 248, 250));
        page.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        // ── Top bar ──
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(248, 248, 250));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JLabel title = new JLabel("Book New Appointment");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(20, 20, 100));

        JButton backBtn = new JButton("← Back to Home");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        backBtn.setBackground(Color.WHITE);
        backBtn.setForeground(new Color(20, 20, 100));
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(20, 20, 100)));
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(true);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> cardLayout.show(contentPanel, "home"));

        topBar.add(title, BorderLayout.WEST);
        topBar.add(backBtn, BorderLayout.EAST);
        page.add(topBar, BorderLayout.NORTH);

        // ── Form ──
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(24, 32, 24, 32)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField brandField = createTextField("e.g. Honda, Toyota");
        JTextField modelField = createTextField("e.g. Civic, Vios");
        JTextField plateField = createTextField("e.g. WAB 1234");
        // 新增：备注 / Command 输入框
        JTextField commandField = createTextField("e.g. Please check brakes too");

        // Service Category & Item
        List<ServiceItem> allItems = serviceItemDAO.getAllServiceItems();
        Set<String> categorySet = new HashSet<>();
        for (ServiceItem item : allItems) {
            categorySet.add(item.getCategory());
        }

        JComboBox<String> categoryBox = new JComboBox<>(categorySet.toArray(new String[0]));
        // itemBox 存储 ServiceItem 对象，显示 description
        JComboBox<ServiceItem> itemBox = new JComboBox<>();
        styleComboBox(categoryBox);
        styleComboBox(itemBox);

        // 自定义 itemBox 显示文字
        itemBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ServiceItem) {
                    ServiceItem si = (ServiceItem) value;
                    setText(si.getDescription() + " (" + si.getDuration() + ")");
                }
                return this;
            }
        });

        categoryBox.addActionListener(e -> {
            itemBox.removeAllItems();
            String selectedCat = (String) categoryBox.getSelectedItem();
            if (selectedCat != null) {
                List<ServiceItem> filteredItems = serviceItemDAO.getServiceItemsByCategory(selectedCat);
                for (ServiceItem item : filteredItems) {
                    itemBox.addItem(item);
                }
            }
        });
        if (categoryBox.getItemCount() > 0) categoryBox.setSelectedIndex(0);

        // Date pickers
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        datePanel.setBackground(Color.WHITE);

        JComboBox<Integer> yearBox = new JComboBox<>();
        JComboBox<Integer> monthBox = new JComboBox<>();
        JComboBox<Integer> dayBox = new JComboBox<>();
        styleComboBox(yearBox);
        styleComboBox(monthBox);
        styleComboBox(dayBox);

        LocalDate today = LocalDate.now();
        yearBox.addItem(today.getYear());
        yearBox.addItem(today.getYear() + 1);

        for (int i = 1; i <= 12; i++) monthBox.addItem(i);
        monthBox.setSelectedItem(today.getMonthValue());

        java.awt.event.ActionListener updateDaysListener = e -> {
            if (yearBox.getSelectedItem() == null || monthBox.getSelectedItem() == null) return;
            int y = (Integer) yearBox.getSelectedItem();
            int m = (Integer) monthBox.getSelectedItem();
            int daysInMonth = java.time.YearMonth.of(y, m).lengthOfMonth();

            Object currentDayObj = dayBox.getSelectedItem();
            int currentDay = (currentDayObj != null) ? (Integer) currentDayObj : today.getDayOfMonth();

            dayBox.removeAllItems();
            for (int i = 1; i <= daysInMonth; i++) dayBox.addItem(i);

            dayBox.setSelectedItem(Math.min(currentDay, daysInMonth));
        };

        yearBox.addActionListener(updateDaysListener);
        monthBox.addActionListener(updateDaysListener);
        updateDaysListener.actionPerformed(null);

        datePanel.add(yearBox);
        datePanel.add(new JLabel("Year"));
        datePanel.add(monthBox);
        datePanel.add(new JLabel("Month"));
        datePanel.add(dayBox);
        datePanel.add(new JLabel("Day"));

        // 修复：时间格式统一为 24 小时制，与数据库一致
        JComboBox<String> timeBox = new JComboBox<>(new String[]{
            "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00"
        });
        styleComboBox(timeBox);

        int row = 0;
        addFormRow(formPanel, "Vehicle Brand *:", brandField, gbc, row++);
        addFormRow(formPanel, "Vehicle Model *:", modelField, gbc, row++);
        addFormRow(formPanel, "Vehicle Plate *:", plateField, gbc, row++);
        addFormRow(formPanel, "Service Category:", categoryBox, gbc, row++);
        addFormRow(formPanel, "Select Service *:", itemBox, gbc, row++);
        addFormRow(formPanel, "Preferred Date *:", datePanel, gbc, row++);
        addFormRow(formPanel, "Preferred Time *:", timeBox, gbc, row++);
        addFormRow(formPanel, "Remarks:", commandField, gbc, row++);

        // Submit Button
        JButton submitBtn = new JButton("Confirm Booking");
        submitBtn.setFont(new Font("Arial", Font.BOLD, 13));
        submitBtn.setBackground(new Color(20, 20, 100));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        submitBtn.setOpaque(true);

        submitBtn.addActionListener(e -> {
            // 验证必填项
            if (brandField.getText().trim().isEmpty()
                    || modelField.getText().trim().isEmpty()
                    || plateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please fill in all vehicle details.",
                    "Incomplete Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (itemBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                    "Please select a service item.",
                    "Incomplete Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 组合并验证日期
            int y = (Integer) yearBox.getSelectedItem();
            int m = (Integer) monthBox.getSelectedItem();
            int d = (Integer) dayBox.getSelectedItem();
            LocalDate selectedDate = LocalDate.of(y, m, d);

            if (selectedDate.isBefore(today)) {
                JOptionPane.showMessageDialog(this,
                    "You cannot book an appointment in the past. Please select a future date.",
                    "Invalid Date", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 获取选中的 ServiceItem ID
            ServiceItem selectedItem = (ServiceItem) itemBox.getSelectedItem();
            String serviceItemID = selectedItem.getServiceItemID();
            String time = (String) timeBox.getSelectedItem();
            String command = commandField.getText().trim().isEmpty() ? "N/A" : commandField.getText().trim();

            // 真正调用 DAO 提交预约
            boolean submitted = appointmentDAO.submitAppointment(
                customerID,
                serviceItemID,
                selectedDate,
                time,
                brandField.getText().trim(),
                modelField.getText().trim(),
                plateField.getText().trim(),
                command
            );

            if (submitted) {
                JOptionPane.showMessageDialog(this,
                    "Appointment Booked Successfully!\nSee you on "
                    + selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) + ".",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

                // 清空输入框
                brandField.setText("");
                modelField.setText("");
                plateField.setText("");
                commandField.setText("");
                categoryBox.setSelectedIndex(0);
                yearBox.setSelectedItem(today.getYear());
                monthBox.setSelectedItem(today.getMonthValue());
                dayBox.setSelectedItem(today.getDayOfMonth());
                timeBox.setSelectedIndex(0);

                // 刷新 Home 页面显示新预约
                refreshHomePage();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to book appointment.\nYou may already have an appointment at this date and time.",
                    "Booking Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        submitPanel.setBackground(Color.WHITE);
        submitPanel.add(submitBtn);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 12, 0, 12);
        formPanel.add(submitPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(248, 248, 250));
        page.add(scrollPane, BorderLayout.CENTER);

        return page;
    }

    private JTextField createTextField(String placeholder) {
        JTextField tf = new JTextField(20);
        tf.setFont(new Font("Arial", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        tf.setToolTipText(placeholder);
        return tf;
    }

    private void styleComboBox(JComboBox<?> box) {
        box.setFont(new Font("Arial", Font.PLAIN, 13));
        box.setBackground(Color.WHITE);
    }

    private void addFormRow(JPanel panel, String labelText, JComponent comp, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(new Color(50, 50, 60));

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0.2;
        panel.add(label, gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.8;
        panel.add(comp, gbc);
    }

    // ─── HISTORY PAGE ────────────────────────────────────────────────────────
    // (不变，原样保留)

    private JPanel createHistoryPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(new Color(248, 248, 250));
        page.setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(248, 248, 250));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));

        JLabel title = new JLabel("Appointment & Service History");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(20, 20, 100));

        JButton backBtn = new JButton("← Back to Home");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        backBtn.setBackground(Color.WHITE);
        backBtn.setForeground(new Color(20, 20, 100));
        backBtn.setBorder(BorderFactory.createLineBorder(new Color(20, 20, 100)));
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(true);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> cardLayout.show(contentPanel, "home"));

        topBar.add(title, BorderLayout.WEST);
        topBar.add(backBtn, BorderLayout.EAST);
        page.add(topBar, BorderLayout.NORTH);

        List<Appointment> history = appointmentDAO.getPassAppointments(customerID);
        Object[][] data = buildHistoryTableData(history);
        String[] columns = {"Date", "Service", "Technician", "Status"};

        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(237, 240, 255));
        table.setSelectionForeground(new Color(20, 20, 100));
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(30, 30, 40));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(20, 20, 100));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());

        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                if (isSelected) {
                    setBackground(new Color(237, 240, 255));
                    setForeground(new Color(20, 20, 100));
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 250, 252));
                    setForeground(new Color(30, 30, 40));
                    if (col == 3 && value != null) {
                        String s = value.toString();
                        if (s.equals("Completed"))      setForeground(new Color(40, 160, 80));
                        else if (s.equals("Cancelled")) setForeground(new Color(200, 60, 60));
                        else                            setForeground(new Color(180, 120, 0));
                    }
                }
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel feedbackSection = new JPanel(new BorderLayout(0, 8));
        feedbackSection.setBackground(new Color(248, 248, 250));
        feedbackSection.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel feedbackTitle = new JLabel("Feedback / Comments");
        feedbackTitle.setFont(new Font("Arial", Font.BOLD, 14));
        feedbackTitle.setForeground(Color.BLACK);

        JLabel feedbackHint = new JLabel("Click a row above to select an appointment before submitting feedback.");
        feedbackHint.setFont(new Font("Arial", Font.PLAIN, 11));
        feedbackHint.setForeground(new Color(150, 150, 160));

        JPanel feedbackTopPanel = new JPanel(new BorderLayout(0, 4));
        feedbackTopPanel.setBackground(new Color(248, 248, 250));
        feedbackTopPanel.add(feedbackTitle, BorderLayout.NORTH);
        feedbackTopPanel.add(feedbackHint, BorderLayout.SOUTH);

        JPanel ratingRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        ratingRow.setBackground(new Color(248, 248, 250));
        JLabel ratingLabel = new JLabel("Rating:");
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        ratingLabel.setForeground(Color.BLACK);
        String[] ratings = {"Select rating", "1 - Poor", "2 - Fair", "3 - Good", "4 - Very Good", "5 - Excellent"};
        JComboBox<String> ratingBox = new JComboBox<>(ratings);
        ratingBox.setFont(new Font("Arial", Font.PLAIN, 12));
        ratingBox.setEnabled(false);
        ratingRow.add(ratingLabel);
        ratingRow.add(ratingBox);

        JTextArea feedbackArea = new JTextArea(4, 0);
        feedbackArea.setFont(new Font("Arial", Font.PLAIN, 13));
        feedbackArea.setForeground(Color.BLACK);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        feedbackArea.setEnabled(false);
        feedbackArea.setDisabledTextColor(new Color(150, 150, 160));
        feedbackArea.setText("Select an appointment first...");

        JButton submitBtn = new JButton("Submit Feedback") {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isEnabled()) {
                    g.setColor(new Color(200, 200, 200));
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(new Color(120, 120, 120));
                    g.setFont(getFont());
                    FontMetrics fm = g.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(getText())) / 2;
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g.drawString(getText(), x, y);
                } else {
                    super.paintComponent(g);
                }
            }
        };
        submitBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        submitBtn.setBackground(new Color(20, 20, 100));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.setBorderPainted(false);
        submitBtn.setOpaque(true);
        submitBtn.setEnabled(false);

        JPanel submitRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        submitRow.setBackground(new Color(248, 248, 250));
        submitRow.add(submitBtn);

        JPanel inputPanel = new JPanel(new BorderLayout(0, 8));
        inputPanel.setBackground(new Color(248, 248, 250));
        inputPanel.add(ratingRow, BorderLayout.NORTH);
        inputPanel.add(new JScrollPane(feedbackArea), BorderLayout.CENTER);
        inputPanel.add(submitRow, BorderLayout.SOUTH);

        feedbackSection.add(feedbackTopPanel, BorderLayout.NORTH);
        feedbackSection.add(inputPanel, BorderLayout.CENTER);

        final String[] selectedApptID = {null};

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                if (history != null && row < history.size()) {
                    String apptID = history.get(row).getAppointmentID();
                    selectedApptID[0] = apptID;

                    Feedback existing = feedbackDAO.getFeedbackByAppointmentID(apptID);
                    if (existing != null) {
                        feedbackArea.setEnabled(false);
                        feedbackArea.setText(existing.getComment());
                        ratingBox.setEnabled(false);
                        try {
                            ratingBox.setSelectedIndex(Integer.parseInt(existing.getRating()));
                        } catch (NumberFormatException ignored) {
                            ratingBox.setSelectedIndex(0);
                        }
                        submitBtn.setEnabled(false);
                        feedbackHint.setText("You have already submitted feedback for this appointment.");
                        feedbackHint.setForeground(new Color(40, 160, 80));
                    } else {
                        feedbackArea.setEnabled(true);
                        feedbackArea.setText("");
                        feedbackArea.setForeground(Color.BLACK);
                        ratingBox.setEnabled(true);
                        ratingBox.setSelectedIndex(0);
                        submitBtn.setEnabled(true);
                        feedbackHint.setText("Write your feedback below and click Submit.");
                        feedbackHint.setForeground(new Color(150, 150, 160));
                    }
                }
            }
        });

        submitBtn.addActionListener(e -> {
            String comment = feedbackArea.getText().trim();
            int ratingIndex = ratingBox.getSelectedIndex();

            if (comment.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your comment before submitting.", "Empty Comment", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (ratingIndex == 0) {
                JOptionPane.showMessageDialog(this, "Please select a rating before submitting.", "No Rating", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = feedbackDAO.submitFeedback(selectedApptID[0], customerID, String.valueOf(ratingIndex), comment);
            if (success) {
                JOptionPane.showMessageDialog(this, "Feedback submitted successfully! Thank you.", "Submitted", JOptionPane.INFORMATION_MESSAGE);
                feedbackArea.setEnabled(false);
                ratingBox.setEnabled(false);
                submitBtn.setEnabled(false);
                feedbackHint.setText("You have already submitted feedback for this appointment.");
                feedbackHint.setForeground(new Color(40, 160, 80));
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        page.add(scrollPane, BorderLayout.CENTER);
        page.add(feedbackSection, BorderLayout.SOUTH);

        return page;
    }

    private Object[][] buildHistoryTableData(List<Appointment> history) {
        if (history == null || history.isEmpty()) return new Object[0][4];
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
        Object[][] data = new Object[history.size()][4];
        for (int i = 0; i < history.size(); i++) {
            Appointment a = history.get(i);
            String techName = technicianDAO.getTechnicianName(a.getTechnicianID());
            data[i][0] = a.getAppointmentDate().format(fmt);
            data[i][1] = a.getServiceType();
            data[i][2] = techName != null ? techName : "N/A";
            data[i][3] = a.getStatus();
        }
        return data;
    }

    // ─── SHARED HELPERS ──────────────────────────────────────────────────────

    private void addCardRow(JPanel panel, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setForeground(new Color(100, 100, 110));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.PLAIN, 13));
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

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(245, 245, 255));
                btn.setBorder(BorderFactory.createLineBorder(new Color(20, 20, 100)));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            }
        });

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

    // ─── SIDEBAR ─────────────────────────────────────────────────────────────

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(230, 230, 230)));

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

        JLabel sectionLabel = new JLabel("  MENU");
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 10));
        sectionLabel.setForeground(new Color(180, 180, 180));
        sectionLabel.setMaximumSize(new Dimension(210, 24));
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(0, 14, 6, 0));
        sidebar.add(sectionLabel);

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
                if (btn != activeButton) btn.setBackground(new Color(245, 245, 250));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != activeButton) btn.setBackground(Color.WHITE);
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
            this, "Are you sure you want to logout?",
            "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) dispose();
    }

    // ─── STATUS BAR ──────────────────────────────────────────────────────────

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
        bar.setBackground(new Color(248, 248, 250));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        bar.setPreferredSize(new Dimension(0, 30));

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
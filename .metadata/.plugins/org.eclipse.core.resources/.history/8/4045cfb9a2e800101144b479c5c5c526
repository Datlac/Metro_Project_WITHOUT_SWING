package com.metro.ui;

import com.metro.business.Ticket;
import com.metro.enums.TicketType;
import com.metro.people.Customer; // Giả sử dùng để test
import com.metro.enums.CustomerType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetroSystemUI extends JFrame {

    // Data Storage (State)
    private List<Ticket> soldTickets;
    private Customer dummyCustomer; // Một khách hàng giả lập để test mua vé

    // UI Components
    private JTextArea logArea;
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private JLabel totalRevenueLabel;
    private JLabel totalTicketsLabel;
    private JTextArea statsArea; // Khu vực hiển thị thống kê gộp

    public MetroSystemUI() {
        // Init Data
        soldTickets = new ArrayList<>();
        dummyCustomer = new Customer("Test User", "001", LocalDate.now(), "0909", "C01", 1000.0, CustomerType.ADULT);

        // Frame Settings
        setTitle("Metro System Management & Testing");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Container (Tabs)
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Add Tabs
        tabbedPane.addTab("Vận Hành & Test", createOperationPanel());
        tabbedPane.addTab("Thống Kê & Doanh Thu", createStatsPanel());

        add(tabbedPane);
    }

    // --- TAB 1: MÔ PHỎNG VẬN HÀNH ---
    private JPanel createOperationPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 1. Control Panel (Input)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Chức năng Test"));

        // Inputs
        JComboBox<TicketType> typeCombo = new JComboBox<>(TicketType.values());
        JTextField priceField = new JTextField("20.0", 10);
        JButton btnBuyTicket = new JButton("Mua Vé (Test Buy)");
        JButton btnClearLog = new JButton("Xóa Log");

        controlPanel.add(new JLabel("Loại Vé:"));
        controlPanel.add(typeCombo);
        controlPanel.add(new JLabel("Giá Vé:"));
        controlPanel.add(priceField);
        controlPanel.add(btnBuyTicket);
        controlPanel.add(btnClearLog);

        // 2. Log Area
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("System Logs"));

        // 3. Events (Java 8 Lambda)
        btnBuyTicket.addActionListener(e -> {
            try {
                double price = Double.parseDouble(priceField.getText());
                TicketType type = (TicketType) typeCombo.getSelectedItem();
                
                // Tạo ID ngẫu nhiên cho vé
                String ticketId = "TICK-" + System.currentTimeMillis() % 10000;
                
                // Tạo vé
                Ticket newTicket = new Ticket(ticketId, price, type);
                
                // Giả lập khách hàng mua
                if (dummyCustomer.deductBalance(price)) {
                    soldTickets.add(newTicket); // Lưu vào danh sách quản lý UI
                    dummyCustomer.addTicket(newTicket);
                    
                    log("SUCCESS: Mua thành công vé " + newTicket);
                    refreshTable(); // Cập nhật bên tab thống kê
                    updateStats();  // Cập nhật số liệu
                } else {
                    log("FAILED: Tài khoản khách không đủ tiền!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập giá vé hợp lệ!");
            } catch (Exception ex) {
                log("ERROR: " + ex.getMessage());
            }
        });

        btnClearLog.addActionListener(e -> logArea.setText(""));

        // Add components to panel
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // --- TAB 2: THỐNG KÊ & DOANH THU ---
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 1. Top Info Panel (Tổng quan)
        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        totalRevenueLabel = new JLabel("Tổng Doanh Thu: 0.0");
        totalRevenueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalRevenueLabel.setForeground(new Color(0, 100, 0)); // Dark Green
        
        totalTicketsLabel = new JLabel("Tổng Vé Đã Bán: 0");
        totalTicketsLabel.setFont(new Font("Arial", Font.BOLD, 16));

        infoPanel.add(totalRevenueLabel);
        infoPanel.add(totalTicketsLabel);

        // 2. Table (Danh sách chi tiết)
        String[] columns = {"Ticket ID", "Loại Vé", "Giá Tiền", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0);
        ticketTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(ticketTable);

        // 3. Stats & Sorting Panel (Bên phải)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 0));
        
        // 3a. Sorting Controls
        JPanel sortPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        sortPanel.setBorder(BorderFactory.createTitledBorder("Sắp Xếp & Lọc"));
        
        JButton btnSortPriceDesc = new JButton("Giá: Cao -> Thấp");
        JButton btnSortPriceAsc = new JButton("Giá: Thấp -> Cao");
        JButton btnSortType = new JButton("Sắp xếp theo Loại");
        JButton btnReset = new JButton("Mặc định (Theo thời gian)");

        sortPanel.add(btnSortPriceDesc);
        sortPanel.add(btnSortPriceAsc);
        sortPanel.add(btnSortType);
        sortPanel.add(btnReset);

        // 3b. Grouped Stats (Thống kê theo nhóm)
        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setBorder(BorderFactory.createTitledBorder("Thống kê chi tiết"));
        
        rightPanel.add(sortPanel, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(statsArea), BorderLayout.CENTER);

        // --- EVENTS CHO NÚT SẮP XẾP ---
        
        // Sắp xếp Giá Cao -> Thấp
        btnSortPriceDesc.addActionListener(e -> {
            soldTickets.sort(Comparator.comparingDouble(Ticket::getPrice).reversed());
            refreshTable();
            log("System: Đã sắp xếp theo giá giảm dần.");
        });

        // Sắp xếp Giá Thấp -> Cao
        btnSortPriceAsc.addActionListener(e -> {
            soldTickets.sort(Comparator.comparingDouble(Ticket::getPrice));
            refreshTable();
            log("System: Đã sắp xếp theo giá tăng dần.");
        });
        
        // Sắp xếp theo Loại vé (Alphabet)
        btnSortType.addActionListener(e -> {
            // Giả sử Ticket có method getType() trả về Enum hoặc String
            // Tôi sẽ ép kiểu toString() để so sánh
            soldTickets.sort((t1, t2) -> t1.toString().compareTo(t2.toString())); 
            refreshTable();
            log("System: Đã sắp xếp theo loại vé.");
        });

        // Reset (Sắp xếp theo list gốc - thời gian thêm vào)
        btnReset.addActionListener(e -> {
            // Ở đây demo đơn giản, thực tế nên có timestamp trong Ticket để sort lại
            // Tạm thời tôi sẽ chỉ refresh lại bảng dựa trên list hiện tại (đã bị sort).
            // Để reset đúng, cần 1 list backup hoặc sort theo ID/Time
             soldTickets.sort(Comparator.comparing(Ticket::getTicketId));
            refreshTable();
            log("System: Reset danh sách.");
        });

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    // --- HELPER METHODS ---

    private void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // Làm mới bảng dữ liệu từ List<Ticket>
    private void refreshTable() {
        tableModel.setRowCount(0); // Xóa hết dòng cũ
        for (Ticket t : soldTickets) {
            // Lưu ý: Đảm bảo class Ticket có các getter này, hoặc dùng toString tạm
            // Ở đây tôi giả định Ticket có toString chứa thông tin, 
            // Cần cập nhật Ticket.java để có getTicketType() public nếu muốn hiện đẹp hơn
            Object[] row = {
                t.getTicketId(),
                t.toString(), // Thay bằng t.getType() nếu có getter
                t.getPrice(),
                "ACTIVE"      // Thay bằng t.getStatus() nếu có getter
            };
            tableModel.addRow(row);
        }
    }

    // Tính toán doanh thu và thống kê nhóm
    private void updateStats() {
        // 1. Tính tổng doanh thu (Java 8 Stream)
        double totalRevenue = soldTickets.stream()
                .mapToDouble(Ticket::getPrice)
                .sum();
        
        DecimalFormat df = new DecimalFormat("#,###.00");
        totalRevenueLabel.setText("Tổng Doanh Thu: " + df.format(totalRevenue) + " VND");
        totalTicketsLabel.setText("Tổng Vé Đã Bán: " + soldTickets.size());

        // 2. Thống kê số lượng theo từng mệnh giá (Hoặc loại vé)
        // Group by Price
        Map<Double, Long> statsByPrice = soldTickets.stream()
                .collect(Collectors.groupingBy(Ticket::getPrice, Collectors.counting()));

        StringBuilder sb = new StringBuilder();
        sb.append("Số lượng vé theo Mệnh Giá:\n");
        statsByPrice.forEach((price, count) -> {
            sb.append(String.format("- Vé %.1f: %d cái\n", price, count));
        });

        statsArea.setText(sb.toString());
    }

    // --- MAIN ---
    public static void main(String[] args) {
        // Chạy UI trên Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MetroSystemUI().setVisible(true);
        });
    }
}
package com.metro.ui;

import com.metro.business.FareCalculator;
import com.metro.business.Ticket;
import com.metro.enums.CustomerType;
import com.metro.enums.TicketType;
import com.metro.infrastructure.Line;
import com.metro.infrastructure.Station;
import com.metro.people.Customer;
import com.metro.transport.RoutePlanner;
import com.metro.transport.TrafficControl;
import com.metro.app.DataLoader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MetroSystemUI extends JFrame {

    // --- MODEL ---
    private List<Customer> customerList;
    private List<Ticket> allSoldTickets;
    private RoutePlanner routePlanner;
    private List<Station> allStations;
    private Map<String, String> stationStatusMap;

    // --- FORMATTER ---
    private final DecimalFormat currencyFormatter;

    // --- UI COMPONENTS ---
    // 1. Khách Hàng
    private JComboBox<String> userSelectCombo;
    private JLabel lblBalance, lblName, lblType; 
    private JTable userHistoryTable;
    private DefaultTableModel userHistoryModel;
    private JComboBox<TicketType> typeCombo;
    private JComboBox<String> cbBuyStart, cbBuyEnd;
    private JTextField txtPrice;
    
    // --- MỚI: Combobox chọn phương thức thanh toán ---
    private JComboBox<String> cbPaymentMethod; 

    // 2. Quản Trị
    private JLabel lblTotalRevenue, lblTotalTickets;
    private JTable adminTable;
    private DefaultTableModel adminModel;
    
    // 3. Vận Hành
    private JComboBox<String> cbStartStation, cbEndStation;
    private JTextArea txtRouteResult, txtTrafficLog;

    // 4. Test Simulation
    private int maxTrainCapacity = 50; 
    private int currentPassengers = 0; 
    private JLabel lblSimStatus;
    private JCheckBox chkHolidayMode;
    
    // 5. Hóa Đơn
    private JList<String> listInvoices;
    private DefaultListModel<String> listInvoiceModel;
    private JTextArea txtInvoicePreview;
    
    // 6. Bảo Trì
    private JTable maintenanceTable;
    private DefaultTableModel maintenanceModel;

    public MetroSystemUI() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(','); 
        currencyFormatter = new DecimalFormat("#,###", symbols);

        loadData();
        initTransportSystem(); 
        initMaintenanceData(); 

        setTitle("Metro System 2026 - Hệ Thống Quản Lý Toàn Diện");
        setSize(1250, 820); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabbedPane.addTab("KHÁCH HÀNG", new ImageIcon(), createCustomerPanel(), "Bán vé và thông tin khách");
        tabbedPane.addTab("HÓA ĐƠN", new ImageIcon(), createInvoicePanel(), "In ấn hóa đơn");
        tabbedPane.addTab("VẬN HÀNH", new ImageIcon(), createOperationsPanel(), "Tìm tuyến và giám sát");
        tabbedPane.addTab("BẢO TRÌ", new ImageIcon(), createMaintenancePanel(), "Quản lý kỹ thuật");
        tabbedPane.addTab("QUẢN TRỊ", new ImageIcon(), createAdminPanel(), "Thống kê doanh thu");
        tabbedPane.addTab("TEST LAB", new ImageIcon(), createSimulationPanel(), "Giả lập hệ thống");

        add(tabbedPane);
    }

    private void loadData() {
        customerList = DataLoader.loadCustomersFromFile();
        allSoldTickets = new ArrayList<>();
    }

    private void initTransportSystem() {
        allStations = new ArrayList<>();
        
        // INIT STATIONS
        Station s1 = new Station("S01", "Ga Ben Thanh");
        Station s2 = new Station("S02", "Ga Nha Hat TP");
        Station s3 = new Station("S03", "Ga Ba Son"); 
        Station s4 = new Station("S04", "Ga Van Thanh");
        Station s5 = new Station("S05", "Ga Tan Cang");
        Station s6 = new Station("S06", "Ga Thao Dien");
        Station s7 = new Station("S07", "Ga An Phu");
        Station s8 = new Station("S08", "Ga Rach Chiec");
        Station s9 = new Station("S09", "Ga Phuoc Long");
        Station s10 = new Station("S10", "Ga Binh Thai");
        Station s11 = new Station("S11", "Ga Thu Duc");
        Station s12 = new Station("S12", "Ga Khu Cong Nghe Cao");
        Station s13 = new Station("S13", "Ga Dai Hoc Quoc Gia");
        Station s14 = new Station("S14", "Ga Suoi Tien");

        allStations.add(s1); allStations.add(s2); allStations.add(s3);
        allStations.add(s4); allStations.add(s5); allStations.add(s6);
        allStations.add(s7); allStations.add(s8); allStations.add(s9);
        allStations.add(s10); allStations.add(s11); allStations.add(s12);
        allStations.add(s13); allStations.add(s14);

        Station locNongLam = new Station("LOC-NL", "DH Nong Lam");
        Station locSuPham = new Station("LOC-SP", "DH Su Pham (Q5)");
        Station locSPKT = new Station("LOC-SPKT", "DH Su Pham Ky Thuat");
        allStations.add(locNongLam); allStations.add(locSuPham); allStations.add(locSPKT);

        // INIT ROUTES
        Line metroLine1 = new Line("METRO-01", "Metro Ben Thanh - Suoi Tien");
        for (Station s : allStations) { if (s.getStationId().startsWith("S")) metroLine1.addStation(s); }

        Line bus164 = new Line("BUS-164", "Bus 164: DH Nong Lam - Ga DHQG");
        bus164.addStation(locNongLam); bus164.addStation(s13); 
        Line bus56 = new Line("BUS-56", "Bus 56: DH Su Pham - Ga Ben Thanh");
        bus56.addStation(locSuPham); bus56.addStation(s1);
        Line bus141 = new Line("BUS-141", "Bus 141: DH SPKT - Ga Binh Thai");
        bus141.addStation(locSPKT); bus141.addStation(s10);
        Line bus19 = new Line("BUS-19", "Bus 19: Nong Lam - Ben Thanh (Chay thang)");
        bus19.addStation(locNongLam); bus19.addStation(s1);
        Line bus167 = new Line("BUS-167", "Bus 167: DH Nong Lam - Ga Khu CNC");
        bus167.addStation(locNongLam); bus167.addStation(s12);
        Line bus165 = new Line("BUS-165", "Bus 165: DH Nong Lam - Ga Suoi Tien");
        bus165.addStation(locNongLam); bus165.addStation(s14);
        Line bus06 = new Line("BUS-06", "Bus 06: DH Su Pham - Ga Nha Hat TP");
        bus06.addStation(locSuPham); bus06.addStation(s2);
        Line bus139 = new Line("BUS-139", "Bus 139: DH Su Pham - Ga Ba Son");
        bus139.addStation(locSuPham); bus139.addStation(s3);
        Line bus53 = new Line("BUS-53", "Bus 53: DH SPKT - Ga Thu Duc");
        bus53.addStation(locSPKT); bus53.addStation(s11);
        Line bus55 = new Line("BUS-55", "Bus 55: DH SPKT - Ga Khu CNC");
        bus55.addStation(locSPKT); bus55.addStation(s12);

        List<Line> lines = new ArrayList<>();
        lines.add(metroLine1);
        lines.add(bus164); lines.add(bus56); lines.add(bus141); lines.add(bus19);
        lines.add(bus167); lines.add(bus165); lines.add(bus06); lines.add(bus139);
        lines.add(bus53); lines.add(bus55);

        routePlanner = new RoutePlanner();
        routePlanner.buildGraph(lines);
    }
    
    private void initMaintenanceData() {
        stationStatusMap = new HashMap<>();
        for (Station s : allStations) {
            stationStatusMap.put(s.getStationId(), "Hoạt Động Tốt");
        }
        stationStatusMap.put("S05", "Đang Bảo Trì (Thang máy hỏng)");
    }

    // ==========================================
    // TAB 1: KHÁCH HÀNG (MUA VÉ)
    // ==========================================
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. THÔNG TIN KHÁCH HÀNG (TOP)
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Khách Hàng"));
        
        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectPanel.add(new JLabel("Chọn Khách Hàng: "));
        Vector<String> customerNames = new Vector<>();
        for (Customer c : customerList) customerNames.add(c.getCustomerId() + " - " + c.getFullName());
        userSelectCombo = new JComboBox<>(customerNames);
        userSelectCombo.setPreferredSize(new Dimension(300, 25));
        selectPanel.add(userSelectCombo);
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblName = new JLabel("Tên: ---");
        lblType = new JLabel(" | Loại: ---"); 
        lblType.setForeground(Color.BLUE);
        lblBalance = new JLabel(" | Số Dư: 0 VND");
        lblBalance.setForeground(new Color(0, 100, 0));
        lblBalance.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(lblName); infoPanel.add(lblType); infoPanel.add(lblBalance);
        
        topPanel.add(selectPanel); topPanel.add(infoPanel);

        // 2. KHU VỰC MUA VÉ (CENTER - CHIA 2 DÒNG)
        JPanel buyContainer = new JPanel(new GridLayout(2, 1, 5, 5)); 
        buyContainer.setBorder(BorderFactory.createTitledBorder("Mua Vé Tại Ga"));

        // --- Dòng 1: Cấu hình Vé & Thanh Toán ---
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        typeCombo = new JComboBox<>(TicketType.values());
        typeCombo.setPreferredSize(new Dimension(120, 25));
        
        String[] methods = {"Ví Điện Tử (Mặc định)", "Tiền Mặt (Tại Quầy)", "SmartCard (Tap)", "Thẻ Tín Dụng"};
        cbPaymentMethod = new JComboBox<>(methods);
        cbPaymentMethod.setPreferredSize(new Dimension(160, 25)); 
        
        txtPrice = new JTextField(10);
        txtPrice.setEditable(false);
        txtPrice.setForeground(Color.RED);
        txtPrice.setFont(new Font("Arial", Font.BOLD, 14));

        row1.add(new JLabel("Loại Vé:")); row1.add(typeCombo);
        row1.add(new JLabel("Thanh Toán:")); row1.add(cbPaymentMethod);
        row1.add(new JLabel("Thành Tiền:")); row1.add(txtPrice);

        // --- Dòng 2: Hành trình & Nút Bấm ---
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        
        Vector<String> stationList = new Vector<>();
        for (Station s : allStations) {
            if (s.getStationId().startsWith("S")) {
                stationList.add(s.getStationId() + " - " + s.getName());
            }
        }
        cbBuyStart = new JComboBox<>(stationList);
        cbBuyStart.setPreferredSize(new Dimension(200, 25)); 
        cbBuyEnd = new JComboBox<>(stationList);
        cbBuyEnd.setPreferredSize(new Dimension(200, 25));
        
        if (stationList.size() > 0) {
            cbBuyStart.setSelectedIndex(0);
            cbBuyEnd.setSelectedIndex(stationList.size() - 1);
        }

        JButton btnBuy = new JButton("THANH TOÁN");
        btnBuy.setBackground(new Color(0, 120, 215)); 
        btnBuy.setForeground(Color.WHITE);
        btnBuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuy.setFocusPainted(false);
        btnBuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBuy.setPreferredSize(new Dimension(150, 35)); 

        row2.add(new JLabel("Ga Đi:")); row2.add(cbBuyStart);
        row2.add(new JLabel("Ga Đến:")); row2.add(cbBuyEnd);
        row2.add(Box.createHorizontalStrut(20)); // Tạo khoảng cách
        row2.add(btnBuy);

        buyContainer.add(row1);
        buyContainer.add(row2);

        // Events
        typeCombo.addActionListener(e -> updateTicketPriceUI());
        cbBuyStart.addActionListener(e -> updateTicketPriceUI());
        cbBuyEnd.addActionListener(e -> updateTicketPriceUI());
        userSelectCombo.addActionListener(e -> {
            updateCurrentUserView();
            updateTicketPriceUI(); 
        });
        btnBuy.addActionListener(e -> handleBuyTicket());

        // 3. LỊCH SỬ (BOTTOM)
        String[] cols = {"Mã Vé", "Loại Vé", "Giá Tiền", "Trạng Thái"};
        userHistoryModel = new DefaultTableModel(cols, 0);
        userHistoryTable = new JTable(userHistoryModel);
        JScrollPane scrollHistory = new JScrollPane(userHistoryTable);
        scrollHistory.setBorder(BorderFactory.createTitledBorder("Lịch Sử Vé Của Khách"));

        // Lắp ráp giao diện
        panel.add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.add(buyContainer, BorderLayout.NORTH);
        centerPanel.add(scrollHistory, BorderLayout.CENTER);
        
        panel.add(centerPanel, BorderLayout.CENTER);

        // Init data
        if (customerList.size() > 0) {
            userSelectCombo.setSelectedIndex(0);
            updateCurrentUserView();
        }
        updateTicketPriceUI();
        return panel;
    }

    // ==========================================
    // TAB 2: HÓA ĐƠN (CẬP NHẬT HIỂN THỊ THANH TOÁN)
    // ==========================================
    private JPanel createInvoicePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        listInvoiceModel = new DefaultListModel<>();
        listInvoices = new JList<>(listInvoiceModel);
        listInvoices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollList = new JScrollPane(listInvoices);
        scrollList.setBorder(BorderFactory.createTitledBorder("Danh Sách Giao Dịch"));
        scrollList.setPreferredSize(new Dimension(300, 0));

        txtInvoicePreview = new JTextArea();
        txtInvoicePreview.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtInvoicePreview.setEditable(false);
        JScrollPane scrollPreview = new JScrollPane(txtInvoicePreview);
        scrollPreview.setBorder(BorderFactory.createTitledBorder("Xem Trước Hóa Đơn (Preview)"));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnPrint = new JButton("🖨️ IN HÓA ĐƠN");
        btnPrint.setBackground(new Color(40, 167, 69)); 
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnPanel.add(btnPrint);

        listInvoices.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listInvoices.getSelectedIndex() != -1) {
                int idx = listInvoices.getSelectedIndex();
                if (idx < allSoldTickets.size()) {
                    Ticket t = allSoldTickets.get(allSoldTickets.size() - 1 - idx);
                    showInvoiceDetail(t);
                }
            }
        });

        btnPrint.addActionListener(e -> {
            if (txtInvoicePreview.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để in!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Đang gửi lệnh in xuống máy in...\nIn Thành Công!", "Print System", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        panel.add(scrollList, BorderLayout.WEST);
        panel.add(scrollPreview, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showInvoiceDetail(Ticket t) {
        StringBuilder sb = new StringBuilder();
        sb.append("************************************\n");
        sb.append("       HỆ THỐNG METRO TP.HCM        \n");
        sb.append("          HÓA ĐƠN ĐIỆN TỬ           \n");
        sb.append("************************************\n\n");
        sb.append("Mã Vé:       ").append(t.getTicketId()).append("\n");
        sb.append("Khách Hàng:  ").append(t.getOwnerName()).append("\n");
        sb.append("Thời Gian:   ").append(t.getFormattedTime()).append("\n");
        sb.append("------------------------------------\n");
        sb.append("Loại Vé:     ").append(t.getType()).append("\n");
        sb.append("Hình Thức:   ").append(t.getPaymentMethod()).append("\n"); // <--- HIỂN THỊ Ở ĐÂY
        sb.append("------------------------------------\n");
        sb.append(String.format("TỔNG TIỀN:   %15s VND\n", currencyFormatter.format(t.getPrice())));
        sb.append("------------------------------------\n\n");
        sb.append("   Cảm ơn quý khách đã sử dụng \n");
        sb.append("      dịch vụ Metro 2026!      \n");
        sb.append("************************************");
        txtInvoicePreview.setText(sb.toString());
    }

    private void updateInvoiceList() {
        listInvoiceModel.clear();
        for (int i = allSoldTickets.size() - 1; i >= 0; i--) {
            Ticket t = allSoldTickets.get(i);
            listInvoiceModel.addElement(t.getFormattedTime() + " - " + t.getOwnerName() + " (" + currencyFormatter.format(t.getPrice()) + ")");
        }
    }

    // ==========================================
    // TAB 3: VẬN HÀNH
    // ==========================================
    private JPanel createOperationsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10)); 
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("📍 Gợi Ý Lộ Trình Thông Minh"));
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 10));
        Vector<String> sNames = new Vector<>();
        for (Station s : allStations) sNames.add(s.getStationId() + " - " + s.getName());
        cbStartStation = new JComboBox<>(sNames);
        cbEndStation = new JComboBox<>(sNames);
        cbStartStation.setSelectedItem("LOC-NL - DH Nong Lam");
        cbEndStation.setSelectedItem("S01 - Ga Ben Thanh"); 
        
        JButton btnFindRoute = new JButton("🔍 TÌM PHƯƠNG ÁN TỐI ƯU");
        btnFindRoute.setBackground(new Color(0, 150, 136)); 
        btnFindRoute.setForeground(Color.WHITE);
        btnFindRoute.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFindRoute.setFocusPainted(false);
        btnFindRoute.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        inputPanel.add(new JLabel("Điểm Xuất Phát:")); inputPanel.add(cbStartStation);
        inputPanel.add(new JLabel("Điểm Đích:")); inputPanel.add(cbEndStation);
        inputPanel.add(new JLabel("")); inputPanel.add(btnFindRoute);
        txtRouteResult = new JTextArea();
        txtRouteResult.setEditable(false);
        txtRouteResult.setFont(new Font("Monospaced", Font.PLAIN, 12));
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(txtRouteResult), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("🚦 Giám Sát Thời Gian Thực"));
        JButton btnCheckTraffic = new JButton("QUÉT TÌNH TRẠNG METRO & BUS");
        txtTrafficLog = new JTextArea();
        txtTrafficLog.setEditable(false);
        txtTrafficLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtTrafficLog.setBackground(Color.BLACK);
        txtTrafficLog.setForeground(Color.GREEN); 
        rightPanel.add(btnCheckTraffic, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(txtTrafficLog), BorderLayout.CENTER);

        btnFindRoute.addActionListener(e -> {
            String startId = ((String) cbStartStation.getSelectedItem()).split(" - ")[0];
            String endId = ((String) cbEndStation.getSelectedItem()).split(" - ")[0];
            RoutePlanner.RouteResult fastest = routePlanner.findFastestPath(startId, endId);
            RoutePlanner.RouteResult leastTransfers = routePlanner.findLeastTransferPath(startId, endId);
            StringBuilder sb = new StringBuilder();
            if (fastest.pathSteps.isEmpty()) sb.append("❌ Không tìm thấy lộ trình phù hợp!");
            else {
                sb.append("🚀 OPTION 1: NHANH NHẤT (Ưu tiên thời gian)\n--------------------------------------------------\n");
                sb.append("⏱️ Thời gian: ").append((int)fastest.totalMinutes).append(" phút | 🔄 Đổi tuyến: ").append(fastest.totalTransfers).append(" lần\n");
                for (String step : fastest.pathSteps) sb.append(step.contains("->") ? "  ⬇ " : "📍 ").append(step).append("\n");
                sb.append("\n\n🛋️ OPTION 2: ÍT CHUYỂN TUYẾN NHẤT (Ngồi khỏe)\n--------------------------------------------------\n");
                sb.append("⏱️ Thời gian: ").append((int)leastTransfers.totalMinutes).append(" phút | 🔄 Đổi tuyến: ").append(leastTransfers.totalTransfers).append(" lần\n");
                for (String step : leastTransfers.pathSteps) sb.append(step.contains("->") ? "  ⬇ " : "📍 ").append(step).append("\n");
            }
            txtRouteResult.setText(sb.toString());
            txtRouteResult.setCaretPosition(0);
        });

        btnCheckTraffic.addActionListener(e -> {
            txtTrafficLog.setText("--- KẾT NỐI CAMERA GIÁM SÁT ---\n");
            for (int i = 1; i <= 5; i++) {
                String tripId = "METRO-0" + i;
                int delay = TrafficControl.checkDelay();
                txtTrafficLog.append(delay > 0 ? String.format("[⚠️ ALERT] %s TRỄ %d phút.\n", tripId, delay) : String.format("[✔ OK] %s ĐÚNG GIỜ.\n", tripId));
            }
        });

        panel.add(leftPanel); panel.add(rightPanel);
        return panel;
    }

    // ==========================================
    // TAB 4: BẢO TRÌ
    // ==========================================
    private JPanel createMaintenancePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolsPanel.setBorder(BorderFactory.createTitledBorder("Công Cụ Kỹ Thuật"));
        
        JButton btnFix = new JButton("🛠️ Bảo Trì / Sửa Chữa");
        JButton btnRestore = new JButton("✅ Khôi Phục Hoạt Động");
        JButton btnRefreshStatus = new JButton("🔄 Làm Mới Trạng Thái");
        
        toolsPanel.add(btnFix);
        toolsPanel.add(btnRestore);
        toolsPanel.add(btnRefreshStatus);

        String[] cols = {"Mã Trạm", "Tên Trạm", "Trạng Thái Kỹ Thuật"};
        maintenanceModel = new DefaultTableModel(cols, 0);
        maintenanceTable = new JTable(maintenanceModel);
        JScrollPane scrollTable = new JScrollPane(maintenanceTable);
        scrollTable.setBorder(BorderFactory.createTitledBorder("Danh Sách Trạm & Thiết Bị"));

        updateMaintenanceTable();

        btnFix.addActionListener(e -> {
            int row = maintenanceTable.getSelectedRow();
            if (row != -1) {
                String stationId = (String) maintenanceModel.getValueAt(row, 0);
                String reason = JOptionPane.showInputDialog(this, "Nhập lý do bảo trì cho " + stationId + ":");
                if (reason != null && !reason.isEmpty()) {
                    stationStatusMap.put(stationId, "Đang Bảo Trì (" + reason + ")");
                    updateMaintenanceTable();
                    JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái bảo trì!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn trạm cần bảo trì!");
            }
        });

        btnRestore.addActionListener(e -> {
            int row = maintenanceTable.getSelectedRow();
            if (row != -1) {
                String stationId = (String) maintenanceModel.getValueAt(row, 0);
                stationStatusMap.put(stationId, "Hoạt Động Tốt");
                updateMaintenanceTable();
                JOptionPane.showMessageDialog(this, "Trạm " + stationId + " đã hoạt động trở lại!");
            }
        });
        
        btnRefreshStatus.addActionListener(e -> updateMaintenanceTable());

        panel.add(toolsPanel, BorderLayout.NORTH);
        panel.add(scrollTable, BorderLayout.CENTER);
        return panel;
    }

    private void updateMaintenanceTable() {
        maintenanceModel.setRowCount(0);
        for (Station s : allStations) {
            String status = stationStatusMap.getOrDefault(s.getStationId(), "Hoạt Động Tốt");
            maintenanceModel.addRow(new Object[]{s.getStationId(), s.getName(), status});
        }
    }

    // ==========================================
    // TAB 5: QUẢN TRỊ
    // ==========================================
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel statsPanel = new JPanel(new GridLayout(1, 2));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Tổng Quan Doanh Thu"));
        lblTotalRevenue = new JLabel("Doanh Thu: 0 VND");
        lblTotalRevenue.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalRevenue.setForeground(Color.RED);
        lblTotalTickets = new JLabel("Tổng Vé Bán: 0");
        lblTotalTickets.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalTickets.setForeground(Color.BLUE);
        statsPanel.add(lblTotalRevenue); statsPanel.add(lblTotalTickets);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Công Cụ Sắp Xếp"));
        JButton btnSortPriceAsc = new JButton("Giá Tăng Dần ⬆");
        JButton btnSortPriceDesc = new JButton("Giá Giảm Dần ⬇");
        JButton btnSortTime = new JButton("Mới Nhất 🕒");
        JButton btnRefresh = new JButton("Làm Mới");
        actionPanel.add(btnSortPriceAsc); actionPanel.add(btnSortPriceDesc); actionPanel.add(btnSortTime); actionPanel.add(btnRefresh);

        String[] cols = {"Mã Vé", "Khách Hàng", "Loại Vé", "Giá", "Ngày Mua"};
        adminModel = new DefaultTableModel(cols, 0);
        adminTable = new JTable(adminModel);
        JScrollPane scrollAdmin = new JScrollPane(adminTable);
        scrollAdmin.setBorder(BorderFactory.createTitledBorder("Chi Tiết Giao Dịch"));

        btnSortPriceAsc.addActionListener(e -> {
            allSoldTickets.sort(Comparator.comparingDouble(Ticket::getPrice));
            updateAdminStats();
        });
        btnSortPriceDesc.addActionListener(e -> {
            allSoldTickets.sort((t1, t2) -> Double.compare(t2.getPrice(), t1.getPrice()));
            updateAdminStats();
        });
        btnSortTime.addActionListener(e -> {
            allSoldTickets.sort((t1, t2) -> t2.getPurchaseTime().compareTo(t1.getPurchaseTime()));
            updateAdminStats();
        });
        btnRefresh.addActionListener(e -> updateAdminStats());

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(statsPanel, BorderLayout.NORTH);
        topContainer.add(actionPanel, BorderLayout.CENTER);

        panel.add(topContainer, BorderLayout.NORTH);
        panel.add(scrollAdmin, BorderLayout.CENTER);
        return panel;
    }

    private void updateAdminStats() {
        adminModel.setRowCount(0);
        double totalRev = 0;
        for (Ticket t : allSoldTickets) {
            totalRev += t.getPrice();
            adminModel.addRow(new Object[]{t.getTicketId(), t.getOwnerName(), t.getType(), currencyFormatter.format(t.getPrice()), t.getFormattedTime()});
        }
        lblTotalRevenue.setText("Doanh Thu: " + currencyFormatter.format(totalRev) + " VND");
        lblTotalTickets.setText("Tổng Vé Bán: " + allSoldTickets.size());
    }

    // ==========================================
    // TAB 6: TEST SIMULATION
    // ==========================================
    private JPanel createSimulationPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Cấu Hình Giả Lập Hệ Thống"));

        chkHolidayMode = new JCheckBox("Kích Hoạt Chế Độ Lễ Tết (Nhu cầu cao đột biến)");
        chkHolidayMode.setFont(new Font("Arial", Font.BOLD, 14));
        chkHolidayMode.setForeground(Color.RED);

        lblSimStatus = new JLabel("Trạng thái tàu: Đang chờ...");
        lblSimStatus.setFont(new Font("Arial", Font.BOLD, 16));
        
        JButton btnUpdateStatus = new JButton("Cập Nhật Trạng Thái (Mô phỏng lượng khách)");

        btnUpdateStatus.addActionListener(e -> {
            if (chkHolidayMode.isSelected()) {
                currentPassengers = 48 + (int)(Math.random() * 3); 
            } else {
                currentPassengers = 10 + (int)(Math.random() * 10);
            }
            updateSimLabel();
            JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái giả lập!\nSố khách hiện tại: " + currentPassengers);
        });

        panel.add(chkHolidayMode);
        panel.add(lblSimStatus);
        panel.add(btnUpdateStatus);
        
        updateSimLabel();

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainContainer.add(panel, BorderLayout.NORTH);
        
        return mainContainer;
    }

    private void updateSimLabel() {
        String color = (currentPassengers >= maxTrainCapacity) ? "red" : "green";
        String statusText = (currentPassengers >= maxTrainCapacity) ? "FULL (Hết chỗ)" : "AVAILABLE (Còn chỗ)";
        lblSimStatus.setText("<html>Trạng thái tàu: <font color='" + color + "'>" + 
            currentPassengers + "/" + maxTrainCapacity + " (" + statusText + ")</font></html>");
    }

    // --- UTILS ---
    private void updateTicketPriceUI() {
        TicketType type = (TicketType) typeCombo.getSelectedItem();
        Customer current = getSelectedCustomer();
        double price = 0;
        if (type == TicketType.SINGLERIDE) {
            cbBuyStart.setEnabled(true);
            cbBuyEnd.setEnabled(true);
            if (cbBuyStart.getSelectedItem() != null && cbBuyEnd.getSelectedItem() != null) {
                String startName = (String) cbBuyStart.getSelectedItem();
                String endName = (String) cbBuyEnd.getSelectedItem();
                price = FareCalculator.calculateTripFare(startName, endName);
            }
        } else {
            cbBuyStart.setEnabled(false);
            cbBuyEnd.setEnabled(false);
            CustomerType cType = (current != null) ? current.getType() : CustomerType.ADULT;
            price = FareCalculator.calculatePassPrice(type, cType);
        }
        txtPrice.setText(currencyFormatter.format(price));
    }

    private void handleBuyTicket() {
        if (currentPassengers >= maxTrainCapacity) {
            JOptionPane.showMessageDialog(this, "⚠️ CẢNH BÁO: Tàu đã hết chỗ (Full Slot)!", "Từ chối bán vé", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Customer current = getSelectedCustomer();
        if (current == null) return;

        try {
            TicketType selectedType = (TicketType) typeCombo.getSelectedItem();
            boolean isSlotTicket = (selectedType == TicketType.DAYPASS || selectedType == TicketType.THREEDAYPASS || selectedType == TicketType.MONTHLYPASS);

            if (isSlotTicket) {
                for (Ticket t : current.getTicketHistory()) {
                    boolean hasActiveSlotTicket = (t.getType() == TicketType.DAYPASS || t.getType() == TicketType.THREEDAYPASS || t.getType() == TicketType.MONTHLYPASS);
                    if (hasActiveSlotTicket && t.isValid()) {
                        JOptionPane.showMessageDialog(this, "🚫 BẠN ĐANG CÓ VÉ ĐỊNH KỲ CÒN HẠN!", "Quy định hạn chế", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }

            String priceStr = txtPrice.getText().replace(".", "").replace(",", "");
            double price = Double.parseDouble(priceStr);
            String ticketId = "T-" + System.currentTimeMillis();
            String method = (String) cbPaymentMethod.getSelectedItem();
            
            Ticket newTicket = new Ticket(ticketId, price, selectedType, current.getFullName(), method);
            
            if (current.deductBalance(price)) {
                current.addTicket(newTicket);
                allSoldTickets.add(newTicket);
                currentPassengers++;
                updateSimLabel(); 
                
                JOptionPane.showMessageDialog(this, "Mua vé thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                updateCurrentUserView();
                updateAdminStats();
                updateInvoiceList(); 
            } else {
                JOptionPane.showMessageDialog(this, "Số dư không đủ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private Customer getSelectedCustomer() {
        int index = userSelectCombo.getSelectedIndex();
        return (index >= 0 && index < customerList.size()) ? customerList.get(index) : null;
    }

    private void updateCurrentUserView() {
        Customer c = getSelectedCustomer();
        if (c != null) {
            lblName.setText("Tên: " + c.getFullName());
            lblType.setText(" | Loại: " + c.getType()); 
            lblBalance.setText(" | Số Dư: " + currencyFormatter.format(c.getWalletBalance()) + " VND");
            userHistoryModel.setRowCount(0);
            List<Ticket> history = c.getTicketHistory();
            for (int i = history.size() - 1; i >= 0; i--) {
                Ticket t = history.get(i);
                Object[] row = {t.getTicketId(), t.getType(), currencyFormatter.format(t.getPrice()), "ACTIVE"};
                userHistoryModel.addRow(row);
            }
        }
    }
}
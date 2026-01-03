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
import java.util.List;
import java.util.Vector;

public class MetroSystemUI extends JFrame {

    // --- MODEL ---
    private List<Customer> customerList;
    private List<Ticket> allSoldTickets;
    private RoutePlanner routePlanner;
    private List<Station> allStations;

    // --- FORMATTER ---
    private final DecimalFormat currencyFormatter;

    // --- UI COMPONENTS ---
    private JComboBox<String> userSelectCombo;
    private JLabel lblBalance, lblName, lblType; 
    private JTable userHistoryTable;
    private DefaultTableModel userHistoryModel;
    
    private JComboBox<TicketType> typeCombo;
    private JComboBox<String> cbBuyStart, cbBuyEnd;
    private JTextField txtPrice;
    
    private JLabel lblTotalRevenue, lblTotalTickets;
    private JTable adminTable;
    private DefaultTableModel adminModel;
    
    private JComboBox<String> cbStartStation, cbEndStation;
    private JTextArea txtRouteResult, txtTrafficLog;

    // --- BIẾN GIẢ LẬP ---
    private int maxTrainCapacity = 50; 
    private int currentPassengers = 0; 
    private JLabel lblSimStatus;
    private JCheckBox chkHolidayMode;

    public MetroSystemUI() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(','); 
        currencyFormatter = new DecimalFormat("#,###", symbols);

        loadData();
        initTransportSystem(); 

        setTitle("Metro System 2026 - Tích Hợp Đa Phương Tiện");
        setSize(1150, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        tabbedPane.addTab("KHÁCH HÀNG", createCustomerPanel());
        tabbedPane.addTab("QUẢN TRỊ", createAdminPanel());
        tabbedPane.addTab("VẬN HÀNH (GỢI Ý TUYẾN)", createOperationsPanel());
        tabbedPane.addTab("TEST (SIMULATION)", createSimulationPanel());

        add(tabbedPane);
    }

    private void loadData() {
        customerList = DataLoader.loadCustomersFromFile();
        allSoldTickets = new ArrayList<>();
    }

    private void initTransportSystem() {
        allStations = new ArrayList<>();
        
        // 1. CÁC GA METRO
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

        // 2. CÁC ĐỊA ĐIỂM
        Station locNongLam = new Station("LOC-NL", "DH Nong Lam");
        Station locSuPham = new Station("LOC-SP", "DH Su Pham (Q5)");
        Station locSPKT = new Station("LOC-SPKT", "DH Su Pham Ky Thuat");
        
        allStations.add(locNongLam); allStations.add(locSuPham); allStations.add(locSPKT);

        // 3. GRAPH
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

    // --- TAB KHÁCH HÀNG ---
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        JPanel buyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buyPanel.setBorder(BorderFactory.createTitledBorder("Mua Vé Tại Ga"));
        typeCombo = new JComboBox<>(TicketType.values());
        
        Vector<String> stationList = new Vector<>();
        for (Station s : allStations) {
            if (s.getStationId().startsWith("S")) {
                stationList.add(s.getStationId() + " - " + s.getName());
            }
        }
        cbBuyStart = new JComboBox<>(stationList);
        cbBuyEnd = new JComboBox<>(stationList);
        if (stationList.size() > 0) {
            cbBuyStart.setSelectedIndex(0);
            cbBuyEnd.setSelectedIndex(stationList.size() - 1);
        }
        txtPrice = new JTextField(10);
        txtPrice.setEditable(false);
        txtPrice.setForeground(Color.RED);
        txtPrice.setFont(new Font("Arial", Font.BOLD, 12));
        JButton btnBuy = new JButton("THANH TOÁN");
        btnBuy.setBackground(new Color(30, 144, 255));
        btnBuy.setForeground(Color.WHITE);

        buyPanel.add(new JLabel("Loại Vé:")); buyPanel.add(typeCombo);
        buyPanel.add(new JLabel("Ga Đi:")); buyPanel.add(cbBuyStart);
        buyPanel.add(new JLabel("Ga Đến:")); buyPanel.add(cbBuyEnd);
        buyPanel.add(new JLabel("Thành Tiền:")); buyPanel.add(txtPrice);
        buyPanel.add(btnBuy);

        typeCombo.addActionListener(e -> updateTicketPriceUI());
        cbBuyStart.addActionListener(e -> updateTicketPriceUI());
        cbBuyEnd.addActionListener(e -> updateTicketPriceUI());
        userSelectCombo.addActionListener(e -> {
            updateCurrentUserView();
            updateTicketPriceUI(); 
        });
        
        btnBuy.addActionListener(e -> handleBuyTicket());

        String[] cols = {"Mã Vé", "Loại Vé", "Giá Tiền", "Trạng Thái"};
        userHistoryModel = new DefaultTableModel(cols, 0);
        userHistoryTable = new JTable(userHistoryModel);
        JScrollPane scrollHistory = new JScrollPane(userHistoryTable);
        scrollHistory.setBorder(BorderFactory.createTitledBorder("Lịch Sử Vé Của Khách"));

        panel.add(topPanel, BorderLayout.NORTH);
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.add(buyPanel, BorderLayout.NORTH);
        centerContainer.add(scrollHistory, BorderLayout.CENTER);
        panel.add(centerContainer, BorderLayout.CENTER);

        if (customerList.size() > 0) {
            userSelectCombo.setSelectedIndex(0);
            updateCurrentUserView();
        }
        updateTicketPriceUI();
        return panel;
    }

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

    // --- HÀM XỬ LÝ MUA VÉ (LOGIC 1 SLOT CHO NHÓM VÉ ĐỊNH KỲ) ---
    private void handleBuyTicket() {
        // 1. KIỂM TRA FULL SLOT (TỪ TAB TEST)
        if (currentPassengers >= maxTrainCapacity) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ CẢNH BÁO: Tàu đã hết chỗ (Full Slot)!\nVui lòng chọn chuyến khác.", 
                "Từ chối bán vé", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Customer current = getSelectedCustomer();
        if (current == null) return;

        try {
            TicketType selectedType = (TicketType) typeCombo.getSelectedItem();
            
            // --- LOGIC MỚI: KIỂM TRA CHÉO CÁC LOẠI VÉ ĐỊNH KỲ ---
            // Định nghĩa nhóm vé "Slot": DayPass, 3DayPass, MonthlyPass
            boolean isSlotTicket = (selectedType == TicketType.DAYPASS || 
                                    selectedType == TicketType.THREEDAYPASS || 
                                    selectedType == TicketType.MONTHLYPASS);

            if (isSlotTicket) {
                for (Ticket t : current.getTicketHistory()) {
                    // Kiểm tra xem khách có đang giữ BẤT KỲ loại vé định kỳ nào còn hạn không
                    boolean hasActiveSlotTicket = (t.getType() == TicketType.DAYPASS || 
                                                   t.getType() == TicketType.THREEDAYPASS || 
                                                   t.getType() == TicketType.MONTHLYPASS);
                    
                    if (hasActiveSlotTicket && t.isValid()) {
                        JOptionPane.showMessageDialog(this, 
                            "🚫 BẠN ĐANG CÓ VÉ ĐỊNH KỲ CÒN HẠN!\n" +
                            "Hệ thống quy định: Mỗi khách hàng chỉ được sở hữu 01 vé định kỳ (Ngày/3 Ngày/Tháng) tại một thời điểm.\n" +
                            "Vé hiện tại của bạn: " + t.getType() + "\n" +
                            "Vui lòng đợi vé cũ hết hạn để mua vé mới.", 
                            "Quy định hạn chế", JOptionPane.WARNING_MESSAGE);
                        return; // Chặn mua
                    }
                }
            }

            // 3. TIẾN HÀNH MUA VÉ
            String priceStr = txtPrice.getText().replace(".", "").replace(",", "");
            double price = Double.parseDouble(priceStr);
            String ticketId = "T-" + System.currentTimeMillis();
            Ticket newTicket = new Ticket(ticketId, price, selectedType, current.getFullName());
            
            if (current.deductBalance(price)) {
                current.addTicket(newTicket);
                allSoldTickets.add(newTicket);
                
                currentPassengers++;
                updateSimLabel(); 
                
                JOptionPane.showMessageDialog(this, "Mua vé thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                updateCurrentUserView();
                updateAdminStats();
            } else {
                JOptionPane.showMessageDialog(this, "Số dư không đủ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    // --- TAB QUẢN TRỊ ---
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

    // --- TAB VẬN HÀNH ---
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
        JButton btnFindRoute = new JButton("TÌM PHƯƠNG ÁN TỐI ƯU");
        btnFindRoute.setBackground(new Color(0, 128, 0));
        btnFindRoute.setForeground(Color.WHITE);
        btnFindRoute.setFont(new Font("Arial", Font.BOLD, 14));
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

    // --- TAB TEST SIMULATION ---
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
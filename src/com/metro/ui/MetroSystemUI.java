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
import javax.swing.border.EmptyBorder;
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

	private static final long serialVersionUID = 1L;

	// --- MODEL HỆ THỐNG ---
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
	private JComboBox<String> cbPaymentMethod;

	// 2. Quản Trị
	private JLabel lblTotalRevenue, lblTotalTickets;
	private JTable adminTable;
	private DefaultTableModel adminModel;

	// 3. Vận Hành
	private JComboBox<String> cbStartStation, cbEndStation;
	private JTextArea txtRouteResult, txtTrafficLog;

	// 4. Test Lab
	private int maxTrainCapacity = 50;
	private int currentPassengers = 0;
	private JLabel lblSimCount;
	private JProgressBar pbCapacity;
	private JTextArea txtSimLog;
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

		setTitle("24130044_LạcThànhĐạt_24130081_Nguyễn_Hoàng_Phúc_Hãi");
		setSize(1280, 850);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));

		tabbedPane.addTab("KHÁCH HÀNG", new ImageIcon(), createCustomerPanel(), "Bán vé và thông tin khách");
		tabbedPane.addTab("HÓA ĐƠN", new ImageIcon(), createInvoicePanel(), "In ấn hóa đơn");
		tabbedPane.addTab("VẬN HÀNH", new ImageIcon(), createOperationsPanel(), "Tìm tuyến và giám sát");
		tabbedPane.addTab("BẢO TRÌ", new ImageIcon(), createMaintenancePanel(), "Quản lý kỹ thuật");
		tabbedPane.addTab("QUẢN TRỊ", new ImageIcon(), createAdminPanel(), "Thống kê & Báo cáo");
		tabbedPane.addTab("TEST LAB", new ImageIcon(), createSimulationPanel(), "Cấu hình giả lập hệ thống");

		add(tabbedPane);
	}

	private void loadData() {
		customerList = DataLoader.loadCustomersFromFile();
		allSoldTickets = new ArrayList<>();
	}

	// =========================================================
	// KHU VỰC CẤU HÌNH TUYẾN
	// =========================================================
	private void initTransportSystem() {
		allStations = new ArrayList<>();

		// Metro Stations
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

		allStations.add(s1);
		allStations.add(s2);
		allStations.add(s3);
		allStations.add(s4);
		allStations.add(s5);
		allStations.add(s6);
		allStations.add(s7);
		allStations.add(s8);
		allStations.add(s9);
		allStations.add(s10);
		allStations.add(s11);
		allStations.add(s12);
		allStations.add(s13);
		allStations.add(s14);

		// Bus Stations
		Station locNongLam = new Station("LOC-NL", "DH Nong Lam");
		Station locSuPham = new Station("LOC-SP", "DH Su Pham (Q5)");
		Station locSPKT = new Station("LOC-SPKT", "DH Su Pham Ky Thuat");
		Station locKTX = new Station("LOC-KTX", "KTX Khu B DHQG");
		Station locChoBenThanh = new Station("LOC-CHO", "Cho Ben Thanh");
		Station locLandmark = new Station("LOC-LM81", "Landmark 81");
		Station locGiga = new Station("LOC-GIGA", "GigaMall Thu Duc");
		Station locAeon = new Station("LOC-AEON", "Aeon Mall Binh Tan");

		allStations.add(locNongLam);
		allStations.add(locSuPham);
		allStations.add(locSPKT);
		allStations.add(locKTX);
		allStations.add(locChoBenThanh);
		allStations.add(locLandmark);
		allStations.add(locGiga);
		allStations.add(locAeon);

		// Lines
		List<Line> lines = new ArrayList<>();

		Line metroLine1 = new Line("METRO-01", "Metro Ben Thanh - Suoi Tien");
		for (Station s : allStations) {
			if (s.getStationId().startsWith("S"))
				metroLine1.addStation(s);
		}
		lines.add(metroLine1);

		Line bus19 = new Line("BUS-19", "Bus 19: KTX Khu B - Ben Thanh");
		bus19.addStation(locKTX);
		bus19.addStation(locNongLam);
		bus19.addStation(s14);
		bus19.addStation(s1);
		lines.add(bus19);

		Line bus33 = new Line("BUS-33", "Bus 33: KTX Khu B - DH SPKT");
		bus33.addStation(locKTX);
		bus33.addStation(s13);
		bus33.addStation(s14);
		bus33.addStation(locSPKT);
		lines.add(bus33);

		Line bus56 = new Line("BUS-56", "Bus 56: DH Su Pham - Landmark 81");
		bus56.addStation(locSuPham);
		bus56.addStation(locChoBenThanh);
		bus56.addStation(s1);
		bus56.addStation(s4);
		bus56.addStation(locLandmark);
		lines.add(bus56);

		Line bus53 = new Line("BUS-53", "Bus 53: Nong Lam - GigaMall - Ben Thanh");
		bus53.addStation(locNongLam);
		bus53.addStation(locSPKT);
		bus53.addStation(s11);
		bus53.addStation(locGiga);
		bus53.addStation(s1);
		lines.add(bus53);

		Line bus150 = new Line("BUS-150", "Bus 150: Ga Tan Cang - Suoi Tien");
		bus150.addStation(s5);
		bus150.addStation(s10);
		bus150.addStation(s12);
		bus150.addStation(s14);
		lines.add(bus150);

		Line bus99 = new Line("BUS-99", "Bus 99: KTX Khu B - Ga Khu CNC");
		bus99.addStation(locKTX);
		bus99.addStation(s12);
		lines.add(bus99);

		Line bus01 = new Line("BUS-01", "Bus 01: Ben Thanh - Aeon Mall");
		bus01.addStation(s1);
		bus01.addStation(locChoBenThanh);
		bus01.addStation(locAeon);
		lines.add(bus01);

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
	// TAB 1: KHÁCH HÀNG
	// ==========================================
	private JPanel createCustomerPanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
		topPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Khách Hàng"));

		JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		selectPanel.add(new JLabel("Chọn Khách Hàng: "));
		Vector<String> customerNames = new Vector<>();
		for (Customer c : customerList)
			customerNames.add(c.getCustomerId() + " - " + c.getFullName());
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
		infoPanel.add(lblName);
		infoPanel.add(lblType);
		infoPanel.add(lblBalance);
		topPanel.add(selectPanel);
		topPanel.add(infoPanel);

		JPanel buyContainer = new JPanel(new GridLayout(2, 1, 5, 5));
		buyContainer.setBorder(BorderFactory.createTitledBorder("Mua Vé Tại Ga"));

		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
		typeCombo = new JComboBox<>(TicketType.values());
		typeCombo.setPreferredSize(new Dimension(120, 25));
		String[] methods = { "Ví Điện Tử (Mặc định)", "Tiền Mặt (Tại Quầy)", "SmartCard (Tap)", "Thẻ Tín Dụng" };
		cbPaymentMethod = new JComboBox<>(methods);
		cbPaymentMethod.setPreferredSize(new Dimension(160, 25));
		txtPrice = new JTextField(10);
		txtPrice.setEditable(false);
		txtPrice.setForeground(Color.RED);
		txtPrice.setFont(new Font("Arial", Font.BOLD, 14));
		row1.add(new JLabel("Loại Vé:"));
		row1.add(typeCombo);
		row1.add(new JLabel("Thanh Toán:"));
		row1.add(cbPaymentMethod);
		row1.add(new JLabel("Thành Tiền:"));
		row1.add(txtPrice);

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
		btnBuy.setForeground(Color.BLACK);
		btnBuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnBuy.setPreferredSize(new Dimension(150, 35));
		row2.add(new JLabel("Ga Đi:"));
		row2.add(cbBuyStart);
		row2.add(new JLabel("Ga Đến:"));
		row2.add(cbBuyEnd);
		row2.add(Box.createHorizontalStrut(20));
		row2.add(btnBuy);

		buyContainer.add(row1);
		buyContainer.add(row2);

		typeCombo.addActionListener(e -> updateTicketPriceUI());
		cbBuyStart.addActionListener(e -> updateTicketPriceUI());
		cbBuyEnd.addActionListener(e -> updateTicketPriceUI());
		userSelectCombo.addActionListener(e -> {
			updateCurrentUserView();
			updateTicketPriceUI();
		});
		btnBuy.addActionListener(e -> handleBuyTicket());

		String[] cols = { "Mã Vé", "Loại Vé", "Giá Tiền", "Trạng Thái" };
		userHistoryModel = new DefaultTableModel(cols, 0);
		userHistoryTable = new JTable(userHistoryModel);
		JScrollPane scrollHistory = new JScrollPane(userHistoryTable);
		scrollHistory.setBorder(BorderFactory.createTitledBorder("Lịch Sử Vé Của Khách"));

		panel.add(topPanel, BorderLayout.NORTH);
		JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
		centerPanel.add(buyContainer, BorderLayout.NORTH);
		centerPanel.add(scrollHistory, BorderLayout.CENTER);
		panel.add(centerPanel, BorderLayout.CENTER);

		if (customerList.size() > 0) {
			userSelectCombo.setSelectedIndex(0);
			updateCurrentUserView();
		}
		updateTicketPriceUI();
		return panel;
	}

	// ==========================================
	// TAB 2: HÓA ĐƠN
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
		btnPanel.add(btnPrint);

		listInvoices.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && listInvoices.getSelectedIndex() != -1) {
				int idx = listInvoices.getSelectedIndex();
				if (idx < allSoldTickets.size())
					showInvoiceDetail(allSoldTickets.get(allSoldTickets.size() - 1 - idx));
			}
		});

		btnPrint.addActionListener(e -> {
			if (txtInvoicePreview.getText().trim().isEmpty())
				JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!");
			else
				JOptionPane.showMessageDialog(this, "Đang gửi lệnh in xuống máy in...\nIn Thành Công!");
		});

		panel.add(scrollList, BorderLayout.WEST);
		panel.add(scrollPreview, BorderLayout.CENTER);
		panel.add(btnPanel, BorderLayout.SOUTH);
		return panel;
	}

	private void showInvoiceDetail(Ticket t) {
		StringBuilder sb = new StringBuilder();
		sb.append("********* METRO TP.HCM *********\n");
		sb.append("       HÓA ĐƠN ĐIỆN TỬ          \n");
		sb.append("--------------------------------\n");
		sb.append("Mã Vé:     ").append(t.getTicketId()).append("\n");
		sb.append("Khách:     ").append(t.getOwnerName()).append("\n");
		sb.append("Ngày mua:  ").append(t.getFormattedTime()).append("\n");
		sb.append("Loại:      ").append(t.getType()).append("\n");
		sb.append("Thanh toán:").append(t.getPaymentMethod()).append("\n");
		sb.append("--------------------------------\n");
		sb.append(String.format("TỔNG TIỀN: %15s VND\n", currencyFormatter.format(t.getPrice())));
		sb.append("********************************");
		txtInvoicePreview.setText(sb.toString());
	}

	private void updateInvoiceList() {
		listInvoiceModel.clear();
		for (int i = allSoldTickets.size() - 1; i >= 0; i--) {
			Ticket t = allSoldTickets.get(i);
			listInvoiceModel.addElement(t.getFormattedTime() + " - " + t.getOwnerName() + " ("
					+ currencyFormatter.format(t.getPrice()) + ")");
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
		for (Station s : allStations)
			sNames.add(s.getStationId() + " - " + s.getName());
		cbStartStation = new JComboBox<>(sNames);
		cbEndStation = new JComboBox<>(sNames);
		cbStartStation.setSelectedItem("LOC-NL - DH Nong Lam");
		cbEndStation.setSelectedItem("S01 - Ga Ben Thanh");

		JButton btnFindRoute = new JButton("TÌM PHƯƠNG ÁN TỐI ƯU");
		btnFindRoute.setBackground(new Color(0, 150, 136));
		btnFindRoute.setForeground(Color.BLACK);

		inputPanel.add(new JLabel("Điểm Xuất Phát:"));
		inputPanel.add(cbStartStation);
		inputPanel.add(new JLabel("Điểm Đích:"));
		inputPanel.add(cbEndStation);
		inputPanel.add(new JLabel(""));
		inputPanel.add(btnFindRoute);
		txtRouteResult = new JTextArea();
		txtRouteResult.setEditable(false);
		txtRouteResult.setFont(new Font("Monospaced", Font.PLAIN, 12));
		leftPanel.add(inputPanel, BorderLayout.NORTH);
		leftPanel.add(new JScrollPane(txtRouteResult), BorderLayout.CENTER);

		JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
		rightPanel.setBorder(BorderFactory.createTitledBorder("🚦 Giám Sát Thời Gian Thực"));
		JButton btnCheckTraffic = new JButton("QUÉT TÌNH TRẠNG METRO & BUS");
		txtTrafficLog = new JTextArea();
		txtTrafficLog.setEditable(false);
		txtTrafficLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtTrafficLog.setBackground(Color.BLACK);
		txtTrafficLog.setForeground(Color.GREEN);
		txtTrafficLog.setText(">> Hệ thống giám sát sẵn sàng...\n");

		rightPanel.add(btnCheckTraffic, BorderLayout.NORTH);
		rightPanel.add(new JScrollPane(txtTrafficLog), BorderLayout.CENTER);

		btnFindRoute.addActionListener(e -> {
			String startId = ((String) cbStartStation.getSelectedItem()).split(" - ")[0];
			String endId = ((String) cbEndStation.getSelectedItem()).split(" - ")[0];
			RoutePlanner.RouteResult fastest = routePlanner.findFastestPath(startId, endId);
			RoutePlanner.RouteResult leastTransfers = routePlanner.findLeastTransferPath(startId, endId);
			StringBuilder sb = new StringBuilder();
			if (fastest.pathSteps.isEmpty())
				sb.append("❌ Không tìm thấy lộ trình phù hợp!");
			else {
				sb.append("🚀 OPTION 1: NHANH NHẤT\n---------------------------------\n");
				sb.append("⏱️ Thời gian: ").append((int) fastest.totalMinutes).append(" phút | 🔄 Đổi tuyến: ")
						.append(fastest.totalTransfers).append("\n");
				for (String step : fastest.pathSteps)
					sb.append(step.contains("->") ? "  ⬇ " : "📍 ").append(step).append("\n");
				sb.append("\n🛋️ OPTION 2: ÍT ĐỔI TUYẾN\n---------------------------------\n");
				sb.append("⏱️ Thời gian: ").append((int) leastTransfers.totalMinutes).append(" phút | 🔄 Đổi tuyến: ")
						.append(leastTransfers.totalTransfers).append("\n");
				for (String step : leastTransfers.pathSteps)
					sb.append(step.contains("->") ? "  ⬇ " : "📍 ").append(step).append("\n");
			}
			txtRouteResult.setText(sb.toString());
			txtRouteResult.setCaretPosition(0);
		});

		btnCheckTraffic.addActionListener(e -> {
			txtTrafficLog.setText("--- KẾT NỐI CAMERA GIÁM SÁT ---\n");
			for (int i = 1; i <= 5; i++) {
				String tripId = "METRO-0" + i;
				int delay = TrafficControl.checkDelay();
				txtTrafficLog.append(delay > 0 ? String.format("[⚠️ ALERT] %s TRỄ %d phút.\n", tripId, delay)
						: String.format("[✔ OK] %s ĐÚNG GIỜ.\n", tripId));
			}
			txtTrafficLog.append(">> Kiểm tra mạng lưới Bus... ỔN ĐỊNH.\n");
		});

		panel.add(leftPanel);
		panel.add(rightPanel);
		return panel;
	}

	// ==========================================
	// TAB 4: BẢO TRÌ
	// ==========================================
	private JPanel createMaintenancePanel() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		JPanel tools = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton btnFix = new JButton("🛠️ Bảo Trì");
		JButton btnRestore = new JButton("✅ Khôi Phục");
		tools.add(btnFix);
		tools.add(btnRestore);

		String[] cols = { "Mã Trạm", "Tên Trạm", "Trạng Thái" };
		maintenanceModel = new DefaultTableModel(cols, 0);
		maintenanceTable = new JTable(maintenanceModel);
		updateMaintenanceTable();

		btnFix.addActionListener(e -> {
			int r = maintenanceTable.getSelectedRow();
			if (r != -1) {
				stationStatusMap.put((String) maintenanceModel.getValueAt(r, 0), "Đang Bảo Trì");
				updateMaintenanceTable();
			}
		});
		btnRestore.addActionListener(e -> {
			int r = maintenanceTable.getSelectedRow();
			if (r != -1) {
				stationStatusMap.put((String) maintenanceModel.getValueAt(r, 0), "Hoạt Động Tốt");
				updateMaintenanceTable();
			}
		});

		panel.add(tools, BorderLayout.NORTH);
		panel.add(new JScrollPane(maintenanceTable), BorderLayout.CENTER);
		return panel;
	}

	private void updateMaintenanceTable() {
		maintenanceModel.setRowCount(0);
		for (Station s : allStations)
			maintenanceModel.addRow(new Object[] { s.getStationId(), s.getName(),
					stationStatusMap.getOrDefault(s.getStationId(), "OK") });
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
		statsPanel.add(lblTotalRevenue);
		statsPanel.add(lblTotalTickets);

		JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		actionPanel.setBorder(BorderFactory.createTitledBorder("Công Cụ Quản Lý"));
		JButton btnSortPriceAsc = new JButton("Giá Tăng Dần ⬆");
		JButton btnSortPriceDesc = new JButton("Giá Giảm Dần ⬇");
		JButton btnSortTime = new JButton("Mới Nhất 🕒");
		JButton btnRefresh = new JButton("Làm Mới Dữ Liệu 🔄");
		actionPanel.add(btnSortPriceAsc);
		actionPanel.add(btnSortPriceDesc);
		actionPanel.add(btnSortTime);
		actionPanel.add(btnRefresh);

		String[] cols = { "Mã Vé", "Khách Hàng", "Loại Vé", "Giá", "Ngày Mua" };
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
			adminModel.addRow(new Object[] { t.getTicketId(), t.getOwnerName(), t.getType(),
					currencyFormatter.format(t.getPrice()), t.getFormattedTime() });
		}
		lblTotalRevenue.setText("Doanh Thu: " + currencyFormatter.format(totalRev) + " VND");
		lblTotalTickets.setText("Tổng Vé Bán: " + allSoldTickets.size());
	}

	// ==========================================
	// TAB 6: TEST LAB
	// ==========================================
	private JPanel createSimulationPanel() {

		JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(new Color(245, 245, 245));

		// CARD PANEL (Khung điều khiển chính)
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBorder(new javax.swing.border.CompoundBorder(
				new javax.swing.border.LineBorder(new Color(200, 200, 200), 1, true),
				new javax.swing.border.EmptyBorder(20, 30, 20, 30)));
		card.setBackground(Color.WHITE);
		card.setPreferredSize(new Dimension(500, 450));

		// 1. TIÊU ĐỀ
		JLabel lblTitle = new JLabel("TRUNG TÂM GIẢ LẬP HỆ THỐNG");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitle.setForeground(new Color(0, 51, 102));
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		// 2. CHECKBOX CHẾ ĐỘ
		chkHolidayMode = new JCheckBox("Chế độ Lễ Tết (High Traffic)");
		chkHolidayMode.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		chkHolidayMode.setAlignmentX(Component.CENTER_ALIGNMENT);
		chkHolidayMode.setBackground(Color.WHITE);
		chkHolidayMode.setFocusPainted(false);

		// 3.Dashboard
		JPanel statsPanel = new JPanel(new GridLayout(2, 1));
		statsPanel.setBackground(Color.WHITE);
		statsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

		JLabel lblLabelCount = new JLabel("HÀNH KHÁCH TRÊN TÀU");
		lblLabelCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblLabelCount.setForeground(Color.GRAY);

		lblSimCount = new JLabel("0 / 50");
		lblSimCount.setFont(new Font("Impact", Font.PLAIN, 48));
		lblSimCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblSimCount.setForeground(new Color(0, 153, 76));

		statsPanel.add(lblLabelCount);
		statsPanel.add(lblSimCount);
		statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// 4. THANH TRẠNG THÁI (Progress Bar)
		pbCapacity = new JProgressBar(0, maxTrainCapacity);
		pbCapacity.setPreferredSize(new Dimension(400, 25));
		pbCapacity.setValue(0);
		pbCapacity.setStringPainted(true);
		pbCapacity.setForeground(new Color(0, 153, 76)); // Green
		pbCapacity.setAlignmentX(Component.CENTER_ALIGNMENT);

		// 5. NÚT CẬP NHẬT
		JButton btnUpdate = new JButton("GỌI KHÁCH ẢO (Random)");
		btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnUpdate.setBackground(new Color(0, 123, 255)); // Blue
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setFocusPainted(false);
		btnUpdate.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// 6. LOG NHỎ
		txtSimLog = new JTextArea(5, 30);
		txtSimLog.setEditable(false);
		txtSimLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
		txtSimLog.setBackground(new Color(250, 250, 250));
		txtSimLog.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		JScrollPane scrollLog = new JScrollPane(txtSimLog);
		scrollLog.setAlignmentX(Component.CENTER_ALIGNMENT);

		card.add(lblTitle);
		card.add(Box.createVerticalStrut(15));
		card.add(chkHolidayMode);
		card.add(statsPanel);
		card.add(pbCapacity);
		card.add(Box.createVerticalStrut(20));
		card.add(btnUpdate);
		card.add(Box.createVerticalStrut(20));
		card.add(scrollLog);

		// --- XỬ LÝ SỰ KIỆN ---
		btnUpdate.addActionListener(e -> {
			int addedPassengers;
			if (chkHolidayMode.isSelected()) {
				addedPassengers = 30 + (int) (Math.random() * 25); // 30-55 khách
			} else {
				addedPassengers = 5 + (int) (Math.random() * 15); // 5-20 khách
			}

			// Cập nhật số liệu
			currentPassengers = addedPassengers;

			// Giới hạn max
			if (currentPassengers > maxTrainCapacity)
				currentPassengers = maxTrainCapacity;

			lblSimCount.setText(currentPassengers + " / " + maxTrainCapacity);
			pbCapacity.setValue(currentPassengers);

			// đổi màu
			if (currentPassengers >= maxTrainCapacity) {
				lblSimCount.setForeground(Color.RED);
				pbCapacity.setForeground(Color.RED);
				txtSimLog.append(">> [CRITICAL] Tàu đã đầy! Không thể nhận thêm.\n");
			} else if (currentPassengers >= 35) {
				lblSimCount.setForeground(Color.ORANGE);
				pbCapacity.setForeground(Color.ORANGE);
				txtSimLog.append(">> [WARN] Tàu sắp đầy (" + currentPassengers + " pax).\n");
			} else {
				lblSimCount.setForeground(new Color(0, 153, 76));
				pbCapacity.setForeground(new Color(0, 153, 76));
				txtSimLog.append(">> [INFO] Cập nhật lượng khách: " + currentPassengers + "\n");
			}
			txtSimLog.setCaretPosition(txtSimLog.getDocument().getLength());
		});

		mainPanel.add(card);
		return mainPanel;
	}

	// --- UTILS ---
	private void updateTicketPriceUI() {
		TicketType type = (TicketType) typeCombo.getSelectedItem();
		Customer c = getSelectedCustomer();
		double price = 0;
		if (type == TicketType.SINGLERIDE) {
			cbBuyStart.setEnabled(true);
			cbBuyEnd.setEnabled(true);
			if (cbBuyStart.getSelectedItem() != null)
				price = FareCalculator.calculateTripFare(((String) cbBuyStart.getSelectedItem()).split(" - ")[0],
						((String) cbBuyEnd.getSelectedItem()).split(" - ")[0]);
		} else {
			cbBuyStart.setEnabled(false);
			cbBuyEnd.setEnabled(false);
			price = FareCalculator.calculatePassPrice(type, (c != null) ? c.getType() : CustomerType.ADULT);
		}
		txtPrice.setText(currencyFormatter.format(price));
	}

	private void handleBuyTicket() {
		if (currentPassengers >= maxTrainCapacity) {
			JOptionPane.showMessageDialog(this, "Tàu hết chỗ!");
			return;
		}
		try {
			double price = Double.parseDouble(txtPrice.getText().replace(".", "").replace(",", ""));
			Customer c = getSelectedCustomer();
			if (c != null && c.deductBalance(price)) {
				Ticket t = new Ticket("T-" + System.currentTimeMillis(), price,
						(TicketType) typeCombo.getSelectedItem(), c.getFullName(),
						(String) cbPaymentMethod.getSelectedItem());
				c.addTicket(t);
				allSoldTickets.add(t);
				currentPassengers++;

				if (pbCapacity != null) {
					lblSimCount.setText(currentPassengers + " / " + maxTrainCapacity);
					pbCapacity.setValue(currentPassengers);
				}

				JOptionPane.showMessageDialog(this, "Mua vé thành công!");
				updateCurrentUserView();
				updateAdminStats();
				updateInvoiceList();
			} else {
				JOptionPane.showMessageDialog(this, "Số dư không đủ!");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
		}
	}

	private Customer getSelectedCustomer() {
		int idx = userSelectCombo.getSelectedIndex();
		return (idx >= 0 && idx < customerList.size()) ? customerList.get(idx) : null;
	}

	private void updateCurrentUserView() {
		Customer c = getSelectedCustomer();
		if (c != null) {
			lblName.setText("Tên: " + c.getFullName());
			lblType.setText(" | " + c.getType());
			lblBalance.setText(" | Dư: " + currencyFormatter.format(c.getWalletBalance()));
			userHistoryModel.setRowCount(0);
			for (Ticket t : c.getTicketHistory())
				userHistoryModel.addRow(new Object[] { t.getTicketId(), t.getType(),
						currencyFormatter.format(t.getPrice()), "ACTIVE" });
		}
	}
}
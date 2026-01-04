package com.metro.app;

import com.metro.infrastructure.Line;
import com.metro.people.Staff;
import com.metro.transport.Train;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.metro.business.Ticket;
import com.metro.enums.TicketType;
import java.time.LocalDateTime;

public class MetroSystem {
	private static MetroSystem instance;
	private String systemName;
	private List<Line> lines;
	private List<Train> fleet;
	private List<Staff> staffList;

	private MetroSystem(String systemName) {
		this.systemName = systemName;
		this.lines = new ArrayList<>();
		this.fleet = new ArrayList<>();
		this.staffList = new ArrayList<>();
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public List<Line> getLines() {
		return lines;
	}

	public void setLines(List<Line> lines) {
		this.lines = lines;
	}

	public List<Train> getFleet() {
		return fleet;
	}

	public void setFleet(List<Train> fleet) {
		this.fleet = fleet;
	}

	public List<Staff> getStaffList() {
		return staffList;
	}

	public void setStaffList(List<Staff> staffList) {
		this.staffList = staffList;
	}

	public static void setInstance(MetroSystem instance) {
		MetroSystem.instance = instance;
	}

	// Singleton Pattern
	public static synchronized MetroSystem getInstance() {
		if (instance == null) {
			instance = new MetroSystem("Ho Chi Minh City Metro");
		}
		return instance;
	}

	public void addLine(Line line) {
		lines.add(line);
	}

	public void addTrain(Train train) {
		fleet.add(train);
	}

	public void addStaff(Staff staff) {
		staffList.add(staff);
	}

	// Java 8
	public Optional<Staff> findStaffById(String id) {
		return staffList.stream().filter(s -> s.getIdStaff().equals(id)).findFirst();
	}

	public static void main(String[] args) {

		System.out.println("========== BẮT ĐẦU TEST LOGIC THẺ TỪ ==========");

		// 1. KHỞI TẠO THẺ MỚI
		System.out.println("\n--- TEST 1: Tạo thẻ mới ---");
		com.metro.business.SmartCard myCard = new com.metro.business.SmartCard("SC-2026-TEST");

		System.out.println(">> Mã thẻ: " + myCard.getCardNumber());
		System.out.println(">> Số dư ban đầu: " + myCard.getBalance()); // Kỳ vọng: 0.0
		System.out.println(">> Trạng thái: " + myCard.getStatus()); // Kỳ vọng: ACTIVE

		// 2. TEST NẠP TIỀN
		System.out.println("\n--- TEST 2: Nạp tiền (Top Up) ---");
		System.out.println(">> Hành động: Nạp vào 100,000 VND");
		myCard.topUp(100000);

		// Kiểm tra số dư
		if (myCard.getBalance() == 100000) {
			System.out.println("Số dư đã lên 100,000 VND");
		} else {
			System.out.println("Số dư hiện tại là " + myCard.getBalance());
		}

		// 3. TEST THANH TOÁN (PAY) - TRƯỜNG HỢP ĐỦ TIỀN
		System.out.println("\n--- TEST 3: Thanh toán vé tàu (Đủ tiền) ---");
		double giaVe = 20000;
		System.out.println(">> Hành động: Quẹt thẻ thanh toán " + giaVe + " VND");

		boolean ketQua1 = myCard.pay(giaVe); // Hàm trả về true/false

		if (ketQua1) {
			System.out.println("Giao dịch được chấp nhận");
			System.out.println("   Số dư còn lại: " + myCard.getBalance()); // Kỳ vọng: 80,000
		} else {
			System.out.println("❌ THẤT BẠI: Lỗi không xác định");
		}

		// 4. TEST THANH TOÁN - TRƯỜNG HỢP KHÔNG ĐỦ TIỀN
		System.out.println("\n--- TEST 4: Thanh toán quá số dư ---");
		double giaVeDat = 500000; // 500k (Lớn hơn 80k đang có)
		System.out.println(">> Hành động: Cố tình mua vé " + giaVeDat + " VND");

		boolean ketQua2 = myCard.pay(giaVeDat);

		if (!ketQua2) {
			System.out.println(" Hệ thống đã TỪ CHỐI giao dịch");
			System.out.println("   Số dư vẫn giữ nguyên: " + myCard.getBalance());
		} else {
			System.out.println("Hệ thống cho phép thanh toán âm tiền!");
		}

		// 5. TEST KHÓA THẺ
		System.out.println("\n--- TEST 5: Kiểm tra trạng thái thẻ ---");
		System.out.println(">> Hành động: Khóa thẻ (Set status = LOCKED)");

		// Giả sử bạn có enum LOCKED trong AccountStatus, nếu tên khác hãy sửa lại
		myCard.setStatus(com.metro.enums.AccountStatus.LOCKED);
		System.out.println(">> Trạng thái hiện tại: " + myCard.getStatus());

		System.out.println(">> Hành động: Cố quẹt thẻ mua vé 10,000 VND");
		boolean ketQuaKhoa = myCard.pay(10000); // Thẻ đang khóa, dù đủ tiền cũng phải fail

		if (!ketQuaKhoa) {
			System.out.println("Thẻ khóa không thể thanh toán");
		} else {
			System.out.println("Thẻ bị khóa vẫn thanh toán được!");
		}

		System.out.println("\n========== KẾT THÚC TEST ==========\n");
		System.out.println("=============================================\n");
		System.out.println("========== BẮT ĐẦU TEST VÉ TÀU ==========");

		// TRƯỜNG HỢP 1: VÉ HỢP LỆ (Mới mua)
		System.out.println("\n--- TEST 1: Vé Mới (Hợp lệ) ---");
		Ticket veMoi = new Ticket("T-001", 10000, TicketType.SINGLERIDE, "Nguyen Van A", "Tien Mat");
		System.out.println(">> Trạng thái ban đầu: " + veMoi.getStatus());
		System.out.println(">> Hạn sử dụng: " + veMoi.getExpiryDate());

		System.out.print(">> Hành động: Quẹt thẻ lần 1... ");
		veMoi.useTicket(); // Gọi hàm sử dụng

		System.out.println(">> Trạng thái sau khi dùng: " + veMoi.getStatus());
		if (veMoi.getStatus().toString().equals("USED")) {
			System.out.println("KẾT QUẢ: Test Vé Hợp Lệ -> THÀNH CÔNG");
		} else {
			System.out.println("KẾT QUẢ: Test Vé Hợp Lệ -> THẤT BẠI");
		}

		// TRƯỜNG HỢP 2: DÙNG LẠI VÉ ĐÃ SỬ DỤNG
		System.out.println("\n--- TEST 2: Dùng lại vé đã qua cửa (Re-entry) ---");
		System.out.print(">> Hành động: Quẹt thẻ lần 2 (Gian lận)... ");
		veMoi.useTicket(); // Dùng lại vé T-001
		// Kỳ vọng: Hệ thống báo lỗi hoặc trạng thái vẫn là USED, không đổi

		// TRƯỜNG HỢP 3: VÉ HẾT HẠN
		System.out.println("\n--- TEST 3: Vé Hết Hạn (Expired) ---");
		Ticket veHetHan = new Ticket("T-002", 10000, TicketType.SINGLERIDE, "Tran Van B", "Visa");

		// Chỉnh hạn sử dụng về ngày hôm qua (Quá khứ)
		veHetHan.setExpiryDate(LocalDateTime.now().minusDays(1));
		System.out.println(">> Đã chỉnh hạn sử dụng về: " + veHetHan.getExpiryDate());

		System.out.println(">> Kiểm tra isValid()? " + veHetHan.isValid()); // Phải trả về false

		System.out.print(">> Hành động: Quẹt thẻ vé hết hạn... ");
		veHetHan.useTicket();

		System.out.println(">> Trạng thái vé: " + veHetHan.getStatus());

		// Kiểm tra logic
		if (!veHetHan.isValid() && veHetHan.getStatus().toString().equals("EXPIRED")) {
			System.out.println("KẾT QUẢ: Hệ thống bắt được vé hết hạn -> THÀNH CÔNG");
		} else if (veHetHan.getStatus().toString().equals("ACTIVE")) {
			System.out.println("KẾT QUẢ: Lỗi! Vé hết hạn nhưng vẫn ACTIVE.");
		}

		System.out.println("\n========== KẾT THÚC TEST ==========\n");
		System.out.println("=============================================\n");
		System.out.println("--- BẮT ĐẦU TEST TÌM KIẾM NHÂN VIÊN ---");

		MetroSystem app = new MetroSystem("Test Metro System");

		Staff nv1 = new Staff("Nguyen Quan Ly", "079090000001", LocalDate.of(1985, 5, 20), "0909111222", "NV001",
				"Ban Quan Ly", "Truong Phong");

		Staff nv2 = new Staff("Tran Nhan Vien", "079090000002", LocalDate.of(1995, 8, 10), "0909333444", "NV002",
				"Van Hanh", "Lai Tau");

		app.addStaff(nv1);
		app.addStaff(nv2);

		System.out.println(">> Đã thêm 2 nhân viên vào hệ thống.");

		String idCanTim = "NV001";

		Staff ketQua = app.findStaffById(idCanTim).orElse(null);

		if (ketQua != null) {
			System.out.println("TÌM THẤY NHÂN VIÊN!");
			System.out.println("   - Mã NV: " + ketQua.getIdStaff());
			System.out.println("   - Chức vụ: " + ketQua.getJobTitle());
			System.out.println("   - Phòng ban: " + ketQua.getDepartment());

		} else {
			System.out.println("KHÔNG TÌM THẤY nhân viên mã: " + idCanTim);
		}

		System.out.println("--- KẾT THÚC TEST ---\n");

	}
}
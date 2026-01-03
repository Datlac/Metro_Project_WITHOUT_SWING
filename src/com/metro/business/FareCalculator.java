package com.metro.business;

import com.metro.enums.CustomerType;
import com.metro.enums.TicketType;
import java.util.HashMap;
import java.util.Map;

public class FareCalculator {

    // Bảng giá vé lượt theo khoảng cách Zone (Diff = |EndZone - StartZone|)
    // Index 0 (Diff 0): 6k (Nội vùng)
    // Index 7 (Diff 7): 19k (Zone 1 -> Zone 8)
    private static final double[] ZONE_PRICES = {
        6000.0, 8000.0, 9000.0, 11000.0, 13000.0, 15000.0, 17000.0, 19000.0
    };
    
    // GIÁ VÉ ĐỊNH KỲ
    public static final double PRICE_1_DAY_PASS = 40000.0;
    public static final double PRICE_3_DAY_PASS = 90000.0;
    public static final double PRICE_MONTHLY_NORMAL = 200000.0;
    public static final double PRICE_MONTHLY_STUDENT = 150000.0;

    // Map lưu Zone, Key sẽ được chuẩn hóa về LowerCase để tránh lỗi so sánh
    private static final Map<String, Integer> stationZones = new HashMap<>();

    static {
        // Khởi tạo Zone và chuẩn hóa tên trạm (trim + lowercase)
        addZone("Ben Thanh", 1);
        addZone("Nha Hat TP", 1);
        addZone("Ba Son", 1);
        addZone("Van Thanh", 1);
        addZone("Tan Cang", 1);
        addZone("Thao Dien", 1);
        addZone("An Phu", 1);
        
        addZone("Rach Chiec", 2);
        addZone("Phuoc Long", 3);
        addZone("Binh Thai", 4);
        addZone("Thu Duc", 5);
        addZone("Khu Cong Nghe Cao", 6);
        addZone("Dai Hoc Quoc Gia", 7);
        addZone("Ben Xe Suoi Tien", 8);
    }

    // Helper thêm zone an toàn
    private static void addZone(String name, int zone) {
        stationZones.put(name.trim().toLowerCase(), zone);
    }

    /**
     * Tính giá vé lượt (Single Ride)
     */
    public static double calculateTripFare(String startStationName, String endStationName) {
        // 1. Chuẩn hóa tên trạm đầu vào (Lấy phần tên sau dấu "-", cắt khoảng trắng, chuyển thường)
        String start = cleanStationName(startStationName);
        String end = cleanStationName(endStationName);

        // 2. Kiểm tra tồn tại
        if (!stationZones.containsKey(start)) {
            System.err.println("[FareCalculator] Cảnh báo: Không tìm thấy trạm '" + start + "' trong bảng giá -> Tính giá mặc định.");
            return 6000.0;
        }
        if (!stationZones.containsKey(end)) {
            System.err.println("[FareCalculator] Cảnh báo: Không tìm thấy trạm '" + end + "' trong bảng giá -> Tính giá mặc định.");
            return 6000.0;
        }

        // 3. Lấy Zone và tính toán
        int startZone = stationZones.get(start);
        int endZone = stationZones.get(end);
        
        // Tính chênh lệch Zone (Trị tuyệt đối)
        int zoneDiff = Math.abs(endZone - startZone);

        // Đảm bảo không vượt quá bảng giá (Max diff là 7)
        if (zoneDiff >= ZONE_PRICES.length) {
            zoneDiff = ZONE_PRICES.length - 1;
        }

        double price = ZONE_PRICES[zoneDiff];
        
        // Debug log (giúp bạn kiểm tra xem nó đang tính đúng không)
        // System.out.println("Trip: " + start + " (Z" + startZone + ") -> " + end + " (Z" + endZone + ") | Diff: " + zoneDiff + " | Price: " + price);
        
        return price;
    }
    
    // Tính giá vé định kỳ
    public static double calculatePassPrice(TicketType type, CustomerType customerType) {
        switch (type) {
            case DAYPASS:
                return PRICE_1_DAY_PASS; 
            case THREEDAYPASS:
                return PRICE_3_DAY_PASS;
            case MONTHLYPASS:
                return (customerType == CustomerType.STUDENT) ? PRICE_MONTHLY_STUDENT : PRICE_MONTHLY_NORMAL;
            default:
                return 0;
        }
    }

    // Hàm làm sạch tên trạm: "S14 - Ben Xe Suoi Tien" -> "ben xe suoi tien"
    private static String cleanStationName(String rawName) {
        if (rawName == null) return "";
        
        String cleaned = rawName;
        // Nếu có dấu gạch ngang (định dạng từ ComboBox), lấy phần sau
        if (rawName.contains(" - ")) {
            String[] parts = rawName.split(" - ");
            if (parts.length > 1) {
                cleaned = parts[1];
            }
        }
        // Trim và Lowercase để so khớp chính xác với Key trong Map
        return cleaned.trim().toLowerCase();
    }
}
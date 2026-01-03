package com.metro.business;

import com.metro.enums.CustomerType;
import com.metro.enums.TicketType;
import java.util.HashMap;
import java.util.Map;

public class FareCalculator {

    // Bảng giá vé lượt theo khoảng cách Zone
    private static final double[] ZONE_PRICES = {
        6000.0, 8000.0, 9000.0, 11000.0, 13000.0, 15000.0, 17000.0, 19000.0
    };
    
    // Giá vé định kỳ
    public static final double PRICE_1_DAY_PASS = 40000.0;
    public static final double PRICE_3_DAY_PASS = 90000.0;
    public static final double PRICE_MONTHLY_NORMAL = 200000.0;
    public static final double PRICE_MONTHLY_STUDENT = 150000.0;

    private static final Map<String, Integer> stationZones = new HashMap<>();

    static {
        // --- 1. CÁC GA METRO (S01 - S14) ---
        addZone("Ga Ben Thanh", 1);
        addZone("Ga Nha Hat TP", 1);
        addZone("Ga Ba Son", 1);
        addZone("Ga Van Thanh", 1);
        addZone("Ga Tan Cang", 1);
        addZone("Ga Thao Dien", 1);
        addZone("Ga An Phu", 1);
        addZone("Ga Rach Chiec", 2);
        addZone("Ga Phuoc Long", 3);
        addZone("Ga Binh Thai", 4);
        addZone("Ga Thu Duc", 5);
        addZone("Ga Khu Cong Nghe Cao", 6);
        addZone("Ga Dai Hoc Quoc Gia", 7);
        addZone("Ga Suoi Tien", 8); // Zone 8

        // --- 2. CÁC ĐỊA ĐIỂM XE BUÝT (VÉ LIÊN THÔNG) ---
        // Quy ước: Điểm xe buýt nằm gần ga nào thì tính Zone của ga đó
        
        // ĐH Nông Lâm -> Gần Suối Tiên/ĐHQG -> Tính Zone 8
        addZone("DH Nong Lam", 8); 
        
        // ĐH Sư Phạm (Q5) -> Đi Bus vào Bến Thành -> Tính Zone 1
        addZone("DH Su Pham (Q5)", 1);
        
        // ĐH Sư Phạm Kỹ Thuật -> Gần Ngã 4 Thủ Đức -> Tính Zone 5
        addZone("DH Su Pham Ky Thuat", 5);
    }

    private static void addZone(String name, int zone) {
        // Lưu key dưới dạng chữ thường để dễ so sánh
        stationZones.put(name.trim().toLowerCase(), zone);
    }

    public static double calculateTripFare(String startStationName, String endStationName) {
        String start = cleanStationName(startStationName);
        String end = cleanStationName(endStationName);

        // Debug: In ra để kiểm tra nếu hệ thống không nhận diện được trạm
        // System.out.println("Checking fare: " + start + " to " + end);

        if (!stationZones.containsKey(start) || !stationZones.containsKey(end)) {
            // Nếu là địa điểm lạ chưa có trong Zone, tính giá mặc định thấp nhất
            return 6000.0; 
        }

        int startZone = stationZones.get(start);
        int endZone = stationZones.get(end);
        int zoneDiff = Math.abs(endZone - startZone);

        if (zoneDiff >= ZONE_PRICES.length) {
            zoneDiff = ZONE_PRICES.length - 1;
        }

        return ZONE_PRICES[zoneDiff];
    }
    
    public static double calculatePassPrice(TicketType type, CustomerType customerType) {
        switch (type) {
            case DAYPASS: return PRICE_1_DAY_PASS;
            case THREEDAYPASS: return PRICE_3_DAY_PASS;
            case MONTHLYPASS: return (customerType == CustomerType.STUDENT) ? PRICE_MONTHLY_STUDENT : PRICE_MONTHLY_NORMAL;
            default: return 0;
        }
    }

    // Làm sạch chuỗi: "LOC-NL - DH Nong Lam" -> "dh nong lam"
    private static String cleanStationName(String rawName) {
        if (rawName == null) return "";
        String cleaned = rawName;
        if (rawName.contains(" - ")) {
            String[] parts = rawName.split(" - ");
            if (parts.length > 1) cleaned = parts[1];
        }
        
        // Xử lý đặc biệt cho các tên dài/viết tắt nếu cần
        // Ví dụ: "DH Nong Lam TPHCM" -> cắt bớt "TPHCM" nếu trong map chỉ lưu "DH Nong Lam"
        // Ở đây ta dùng contains để map linh hoạt hơn
        String lower = cleaned.trim().toLowerCase();
        
        // Fix nhanh cho trường hợp tên dài ngắn không khớp
        if (lower.contains("nong lam")) return "dh nong lam";
        if (lower.contains("su pham (q5)")) return "dh su pham (q5)";
        if (lower.contains("su pham ky thuat")) return "dh su pham ky thuat";
        if (lower.contains("ben thanh")) return "ga ben thanh";
        if (lower.contains("nha hat")) return "ga nha hat tp";
        if (lower.contains("suoi tien") && !lower.contains("bus")) return "ga suoi tien";
        
        return lower;
    }
}
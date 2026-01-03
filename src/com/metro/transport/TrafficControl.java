package com.metro.transport;

import java.util.Random;

public class TrafficControl {

    private static final Random random = new Random();

    /**
     * Kiểm tra xem chuyến tàu có bị delay không.
     * Xác suất delay: 20%.
     * Thời gian delay: Random từ 1 đến 15 phút.
     * * @return Số phút delay (0 nếu đúng giờ)
     */
    public static int checkDelay() {
        // Random số từ 0-99. Nếu < 20 (20%) thì có delay
        if (random.nextInt(100) < 20) {
            // Random delay từ 1 đến 15 phút
            int delayMinutes = 1 + random.nextInt(15);
            return delayMinutes;
        }
        return 0; // Đúng giờ
    }

    /**
     * Báo cáo trạng thái giao thông
     */
    public static void reportStatus(String tripId) {
        int delay = checkDelay();
        if (delay > 0) {
            System.out.println("[⚠️ ALERT] Chuyến " + tripId + " đang bị trễ " + delay + " phút do sự cố tín hiệu.");
        } else {
            System.out.println("[✔ OK] Chuyến " + tripId + " đang vận hành đúng giờ.");
        }
    }
}
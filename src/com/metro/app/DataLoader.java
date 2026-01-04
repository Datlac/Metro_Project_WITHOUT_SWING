package com.metro.app;

import com.metro.business.Ticket;
import com.metro.enums.CustomerType;
import com.metro.enums.TicketType;
import com.metro.people.Customer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataLoader {

    // Đường dẫn file dữ liệu
    private static final String FILE_PATH = "data/customers.txt";

    public static List<Customer> loadCustomersFromFile() {
        List<Customer> customerList = new ArrayList<>();
        
        File file = new File(FILE_PATH);
        
        // 1. Kiểm tra file có tồn tại không
        if (!file.exists()) {
            System.err.println("⚠️ CẢNH BÁO: Không tìm thấy file '" + FILE_PATH + "'");
            System.out.println(">> Đang chuyển sang chế độ dữ liệu giả lập (Dummy Data)...");
            return createDummyData(); // Gọi hàm tạo dữ liệu mẫu nếu không có file
        }

        // 2. Nếu có file thì đọc
        try (Stream<String> stream = Files.lines(Paths.get(FILE_PATH))) {
            customerList = stream
                .filter(line -> !line.trim().isEmpty())
                .map(DataLoader::parseCustomer) // Gọi hàm parse từng dòng
                .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // 3. Nếu đọc file xong mà danh sách vẫn rỗng (file lỗi/trống) -> Dùng dummy data
        if (customerList.isEmpty()) {
            return createDummyData();
        }

        return customerList;
    }

    // Hàm phân tích từng dòng text thành đối tượng Customer
    private static Customer parseCustomer(String line) {
        try {
            // Cấu trúc mong đợi: ID,Name,Dob,IdCard,Phone,Balance,Type|Ticket1;Ticket2...
            String[] mainParts = line.split("\\|");
            String[] info = mainParts[0].split(",");
            
            // Parse thông tin khách hàng (7 trường thông tin)
            String id = info[0].trim();
            String name = info[1].trim();
            LocalDate dob = LocalDate.parse(info[2].trim());
            String idCard = info[3].trim();
            String phone = info[4].trim();
            double balance = Double.parseDouble(info[5].trim());
            CustomerType type = CustomerType.valueOf(info[6].trim().toUpperCase());

            // Tạo đối tượng Customer
            Customer customer = new Customer(name, idCard, dob, phone, id, balance, type);

            // Parse lịch sử vé (Nếu có)
            if (mainParts.length > 1 && !mainParts[1].trim().isEmpty()) {
                String[] ticketStrings = mainParts[1].split(";");
                for (String tStr : ticketStrings) {
                    try {
                        // Cấu trúc vé trong file cũ: TicketID:Price:Type
                        String[] tParts = tStr.split(":");
                        String tId = tParts[0].trim();
                        double tPrice = Double.parseDouble(tParts[1].trim());
                        TicketType tType = TicketType.valueOf(tParts[2].trim().toUpperCase());
                        
                        // --- FIX LỖI CONSTRUCTOR ---
                        // Vì Ticket mới cần 5 tham số, ta tự điền 2 tham số thiếu từ file cũ:
                        // 1. OwnerName: Lấy tên khách hàng
                        // 2. PaymentMethod: Gán mặc định "Lịch Sử Cũ"
                        Ticket t = new Ticket(tId, tPrice, tType, name, "Lịch Sử Cũ");
                        
                        customer.addTicket(t);
                    } catch (Exception ex) {
                        System.err.println("Lỗi đọc vé: " + tStr);
                    }
                }
            }
            return customer;
        } catch (Exception e) {
            System.err.println("Lỗi đọc dòng: " + line);
            return null; // Bỏ qua dòng lỗi
        }
    }

    // Hàm tạo dữ liệu giả lập (Dùng khi không có file hoặc file lỗi)
    private static List<Customer> createDummyData() {
        List<Customer> customers = new ArrayList<>();
        
        // Khách 1: Nguyễn Văn An
        Customer c1 = new Customer("Nguyen Van An", "079090000001", LocalDate.of(1990, 1, 1), "0909123456", "C001", 500000, CustomerType.ADULT);
        c1.addTicket(new Ticket("T-OLD-001", 19000, TicketType.SINGLERIDE, "Nguyen Hoang Phuc Hai", "Tiền Mặt"));
        customers.add(c1);
        
        // Khách 2: Trần Thị B
        Customer c2 = new Customer("Lac Thanh Dat", "079090000002", LocalDate.of(2003, 5, 15), "0909123789", "C002", 200000, CustomerType.STUDENT);
        customers.add(c2);
        
        // Khách 3: Lạc Thành Đạt (Như trong hình bạn gửi)
        Customer c4 = new Customer("Lac Thanh Dat", "24130044", LocalDate.of(2000, 1, 1), "0909888999", "24130044", 500000, CustomerType.ADULT);
        c4.addTicket(new Ticket("T-OLD-005", 19000, TicketType.SINGLERIDE, "Tran Thi B", "Ví Điện Tử"));
        customers.add(c4);

        return customers;
    }
}
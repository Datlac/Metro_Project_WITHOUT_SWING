package com.metro.app;

import com.metro.business.Ticket;
import com.metro.enums.CustomerType;
import com.metro.enums.TicketType;
import com.metro.people.Customer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataLoader {

    // Đường dẫn đến file
    private static final String FILE_PATH = "customers.txt";

    public static List<Customer> loadCustomersFromFile() {
        List<Customer> customerList = new ArrayList<>();

        // Java 8: Sử dụng Files.lines() để đọc file theo dòng (Lazy loading)
        try (Stream<String> stream = Files.lines(Paths.get(FILE_PATH))) {
            
            customerList = stream
                .filter(line -> !line.trim().isEmpty()) // Bỏ qua dòng trống
                .map(DataLoader::parseCustomer)        // Chuyển String -> Customer Object
                .collect(Collectors.toList());

        } catch (IOException e) {
            System.err.println("Lỗi đọc file: " + e.getMessage());
            e.printStackTrace();
        }

        return customerList;
    }

    private static Customer parseCustomer(String line) {
        try {
            // Tách phần Thông tin khách và Danh sách vé
            String[] mainParts = line.split("\\|"); // Dùng ký tự | để tách
            
            // 1. Xử lý thông tin khách hàng (Phần bên trái dấu |)
            String[] info = mainParts[0].split(",");
            
            String id = info[0].trim();
            String name = info[1].trim();
            LocalDate dob = LocalDate.parse(info[2].trim()); // Định dạng yyyy-MM-dd
            String idCard = info[3].trim();
            String phone = info[4].trim();
            double balance = Double.parseDouble(info[5].trim());
            CustomerType type = CustomerType.valueOf(info[6].trim().toUpperCase());

            // Tạo đối tượng Customer
            Customer customer = new Customer(name, idCard, dob, phone, id, balance, type); // Lưu ý: balance chưa set trong constructor cũ, cần kiểm tra lại constructor của bạn

            // Cập nhật số dư nếu constructor chưa gán (Giả sử bạn có method setBalance hoặc logic trừ tiền)
            // customer.setWalletBalance(balance); // Nếu cần

            // 2. Xử lý danh sách vé (Phần bên phải dấu | - nếu có)
            if (mainParts.length > 1 && !mainParts[1].trim().isEmpty()) {
                String[] ticketStrings = mainParts[1].split(";");
                
                for (String tStr : ticketStrings) {
                    // Format: ID:Price:Type
                    String[] tParts = tStr.split(":");
                    String tId = tParts[0].trim();
                    double tPrice = Double.parseDouble(tParts[1].trim());
                    TicketType tType = TicketType.valueOf(tParts[2].trim().toUpperCase());

                    Ticket ticket = new Ticket(tId, tPrice, tType);
                    customer.addTicket(ticket);
                }
            }

            return customer;

        } catch (Exception e) {
            System.err.println("Lỗi phân tích dòng: " + line + " -> " + e.getMessage());
            return null;
        }
    }
}
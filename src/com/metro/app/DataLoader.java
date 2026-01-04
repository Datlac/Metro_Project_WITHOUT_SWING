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

	private static final String FILE_PATH = "data/customers.txt";

	public static List<Customer> loadCustomersFromFile() {
		List<Customer> customerList = new ArrayList<>();

		File file = new File(FILE_PATH);

		if (!file.exists()) {
			System.err.println("⚠️ CẢNH BÁO: Không tìm thấy file '" + FILE_PATH + "'");
			System.out.println(">> Đang chuyển sang chế độ dữ liệu giả lập (Dummy Data)...");
			return createDummyData();
		}

		try (Stream<String> stream = Files.lines(Paths.get(FILE_PATH))) {
			customerList = stream.filter(line -> !line.trim().isEmpty()).map(DataLoader::parseCustomer)
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (customerList.isEmpty()) {
			return createDummyData();
		}

		return customerList;
	}

	private static Customer parseCustomer(String line) {
		try {

			String[] mainParts = line.split("\\|");
			String[] info = mainParts[0].split(",");

			String id = info[0].trim();
			String name = info[1].trim();
			LocalDate dob = LocalDate.parse(info[2].trim());
			String idCard = info[3].trim();
			String phone = info[4].trim();
			double balance = Double.parseDouble(info[5].trim());
			CustomerType type = CustomerType.valueOf(info[6].trim().toUpperCase());

			Customer customer = new Customer(name, idCard, dob, phone, id, balance, type);

			if (mainParts.length > 1 && !mainParts[1].trim().isEmpty()) {
				String[] ticketStrings = mainParts[1].split(";");
				for (String tStr : ticketStrings) {
					try {

						String[] tParts = tStr.split(":");
						String tId = tParts[0].trim();
						double tPrice = Double.parseDouble(tParts[1].trim());
						TicketType tType = TicketType.valueOf(tParts[2].trim().toUpperCase());

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
			return null;
		}
	}

	private static List<Customer> createDummyData() {
		List<Customer> customers = new ArrayList<>();

		Customer c1 = new Customer("Nguyen Van An", "079090000001", LocalDate.of(1990, 1, 1), "0909123456", "C001",
				500000, CustomerType.ADULT);
		c1.addTicket(new Ticket("T-OLD-001", 19000, TicketType.SINGLERIDE, "Nguyen Hoang Phuc Hai", "Tiền Mặt"));
		customers.add(c1);

		Customer c2 = new Customer("Lac Thanh Dat", "079090000002", LocalDate.of(2003, 5, 15), "0909123789", "C002",
				200000, CustomerType.STUDENT);
		customers.add(c2);

		Customer c4 = new Customer("Lac Thanh Dat", "24130044", LocalDate.of(2000, 1, 1), "0909888999", "24130044",
				500000, CustomerType.ADULT);
		c4.addTicket(new Ticket("T-OLD-005", 19000, TicketType.SINGLERIDE, "Tran Thi B", "Ví Điện Tử"));
		customers.add(c4);

		return customers;
	}
}
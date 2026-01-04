package com.metro.business;

import com.metro.enums.CustomerType;
import com.metro.enums.TicketType;

public class FareCalculator {

	// --- CẤU HÌNH GIÁ VÉ ĐỊNH KỲ ---
	public static final double PRICE_1_DAY_PASS = 40000.0;
	public static final double PRICE_3_DAY_PASS = 90000.0;
	public static final double PRICE_MONTHLY_NORMAL = 200000.0;
	public static final double PRICE_MONTHLY_STUDENT = 150000.0;

	private static final int[][] FARE_MATRIX = new int[14][14];
	private static boolean isMatrixInitialized = false;

	static {
		initFareMatrix();
	}

	private static void initFareMatrix() {
		if (isMatrixInitialized)
			return;

		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 14; j++) {
				FARE_MATRIX[i][j] = 0;
			}
		}

		// 1. Từ Bến Thành (S01 - Index 0)
		setPriceRow(0, 6000, // Đến S2, S3, S4, S5, S6, S7 (Zone 1)
				new int[] { 1, 2, 3, 4, 5, 6 });
		updatePrice(0, 7, 8000); // Đến Rạch Chiếc (S8)
		updatePrice(0, 8, 9000); // Đến Phước Long (S9)
		updatePrice(0, 9, 11000); // Đến Bình Thái (S10)
		updatePrice(0, 10, 13000); // Đến Thủ Đức (S11)
		updatePrice(0, 11, 15000); // Đến Khu CNC (S12)
		updatePrice(0, 12, 17000); // Đến ĐHQG (S13)
		updatePrice(0, 13, 19000); // Đến Suối Tiên (S14)

		// 2. Từ Nhà Hát TP (S02 - Index 1)
		setPriceRow(1, 6000, // Đến S1, S3, S4, S5, S6, S7
				new int[] { 0, 2, 3, 4, 5, 6 });
		updatePrice(1, 7, 7000); // Đến Rạch Chiếc
		updatePrice(1, 8, 9000); // Đến Phước Long
		updatePrice(1, 9, 10000); // Đến Bình Thái
		updatePrice(1, 10, 12000); // Đến Thủ Đức
		updatePrice(1, 11, 15000); // Đến Khu CNC
		updatePrice(1, 12, 16000); // Đến ĐHQG
		updatePrice(1, 13, 19000); // Đến Suối Tiên

		// 3. Từ Ba Son (S03 - Index 2)
		setPriceRow(2, 6000, // Đến S1, S2, S4, S5, S6, S7, S8
				new int[] { 0, 1, 3, 4, 5, 6, 7 });
		updatePrice(2, 8, 8000); // Đến Phước Long
		updatePrice(2, 9, 9000); // Đến Bình Thái
		updatePrice(2, 10, 11000); // Đến Thủ Đức
		updatePrice(2, 11, 14000); // Đến Khu CNC
		updatePrice(2, 12, 15000); // Đến ĐHQG
		updatePrice(2, 13, 17000); // Đến Suối Tiên

		// 4. Từ Văn Thánh (S04 - Index 3)
		setPriceRow(3, 6000, // Đến S1, S2, S3, S5, S6, S7, S8, S9
				new int[] { 0, 1, 2, 4, 5, 6, 7, 8 });
		updatePrice(3, 9, 7000); // Đến Bình Thái
		updatePrice(3, 10, 9000); // Đến Thủ Đức
		updatePrice(3, 11, 12000); // Đến Khu CNC
		updatePrice(3, 12, 13000); // Đến ĐHQG
		updatePrice(3, 13, 16000); // Đến Suối Tiên

		// 5. Từ Tân Cảng (S05 - Index 4)
		setPriceRow(4, 6000, // Đến S1..S4, S6..S10
				new int[] { 0, 1, 2, 3, 5, 6, 7, 8, 9 });
		updatePrice(4, 10, 8000); // Đến Thủ Đức
		updatePrice(4, 11, 11000); // Đến Khu CNC
		updatePrice(4, 12, 12000); // Đến ĐHQG
		updatePrice(4, 13, 15000); // Đến Suối Tiên

		// 6. Từ Thảo Điền (S06 - Index 5)
		setPriceRow(5, 6000, // Đến S1..S5, S7..S10
				new int[] { 0, 1, 2, 3, 4, 6, 7, 8, 9 });
		updatePrice(5, 10, 7000); // Đến Thủ Đức
		updatePrice(5, 11, 9000); // Đến Khu CNC
		updatePrice(5, 12, 11000); // Đến ĐHQG
		updatePrice(5, 13, 13000); // Đến Suối Tiên

		// 7. Từ An Phú (S07 - Index 6)
		setPriceRow(6, 6000, // Đến S1..S6, S8..S11
				new int[] { 0, 1, 2, 3, 4, 5, 7, 8, 9, 10 });
		updatePrice(6, 11, 8000); // Đến Khu CNC
		updatePrice(6, 12, 10000); // Đến ĐHQG
		updatePrice(6, 13, 12000); // Đến Suối Tiên

		// 8. Từ Rạch Chiếc (S08 - Index 7)
		updatePrice(7, 0, 8000); // Đến Bến Thành
		updatePrice(7, 1, 7000); // Đến Nhà Hát
		setPriceRow(7, 6000, new int[] { 2, 3, 4, 5, 6, 8, 9, 10 }); // Các trạm lân cận
		updatePrice(7, 11, 7000); // Đến Khu CNC
		updatePrice(7, 12, 8000); // Đến ĐHQG
		updatePrice(7, 13, 10000); // Đến Suối Tiên

		// 9. Từ Phước Long (S09 - Index 8)
		updatePrice(8, 0, 9000); // Đến Bến Thành
		updatePrice(8, 1, 8000); // Đến Nhà Hát (Ghi đè cho khớp file: 9k trong file ghi nhầm, nhưng logic trên
									// là 8k cho S2->S9? File ghi S9->S1,S2=9k)
		// Lưu ý: File ghi S9->S1,S2 là 9000. Nhưng S2->S9 file lại ghi 9000. OK khớp.
		updatePrice(8, 2, 8000); // Đến Ba Son
		setPriceRow(8, 6000, new int[] { 3, 4, 5, 6, 7, 9, 10, 11 }); // Lân cận
		updatePrice(8, 12, 7000); // Đến ĐHQG
		updatePrice(8, 13, 9000); // Đến Suối Tiên

		// 10. Từ Bình Thái (S10 - Index 9)
		updatePrice(9, 0, 11000); // Bến Thành
		updatePrice(9, 1, 10000); // Nhà Hát
		updatePrice(9, 2, 9000); // Ba Son
		updatePrice(9, 3, 7000); // Văn Thánh
		setPriceRow(9, 6000, new int[] { 4, 5, 6, 7, 8, 10, 11, 12 }); // Lân cận
		updatePrice(9, 13, 7000); // Suối Tiên

		// 11. Từ Thủ Đức (S11 - Index 10)
		updatePrice(10, 0, 13000);
		updatePrice(10, 1, 12000);
		updatePrice(10, 2, 11000);
		updatePrice(10, 3, 9000);
		updatePrice(10, 4, 8000);
		updatePrice(10, 5, 7000);
		setPriceRow(10, 6000, new int[] { 6, 7, 8, 9, 11, 12, 13 }); // Lân cận đến Suối Tiên

		// 12. Từ Khu Công Nghệ Cao (S12 - Index 11)
		updatePrice(11, 0, 15000);
		updatePrice(11, 1, 14000); // File ghi S12->S2=14k nhưng S2->S12=15k?
		// File ghi: "từ khu công nghệ cao đi zone1(15.000đ){bến thành, nhà hát thành
		// phố}". Vậy S12->S2 là 15k.
		updatePrice(11, 1, 15000); // Fix theo file
		updatePrice(11, 2, 14000);
		updatePrice(11, 3, 12000);
		updatePrice(11, 4, 11000);
		updatePrice(11, 5, 9000);
		updatePrice(11, 6, 8000);
		updatePrice(11, 7, 7000);
		setPriceRow(11, 6000, new int[] { 8, 9, 10, 12, 13 });

		// 13. Từ Đại Học Quốc Gia (S13 - Index 12)
		updatePrice(12, 0, 17000);
		updatePrice(12, 1, 16000);
		updatePrice(12, 2, 15000);
		updatePrice(12, 3, 13000);
		updatePrice(12, 4, 12000);
		updatePrice(12, 5, 11000);
		updatePrice(12, 6, 10000);
		updatePrice(12, 7, 8000);
		updatePrice(12, 8, 7000);
		setPriceRow(12, 6000, new int[] { 9, 10, 11, 13 });

		// 14. Từ Suối Tiên (S14 - Index 13)
		// File đoạn cuối ghi "từ khu công nghệ cao đi zone1(19.000đ)" khả năng cao là
		// typo của "Suối Tiên" vì giá cao nhất.
		updatePrice(13, 0, 19000);
		updatePrice(13, 1, 19000); // Theo file đoạn cuối
		updatePrice(13, 2, 17000);
		updatePrice(13, 3, 16000);
		updatePrice(13, 4, 15000);
		updatePrice(13, 5, 13000);
		updatePrice(13, 6, 12000);
		updatePrice(13, 7, 10000);
		updatePrice(13, 8, 9000);
		updatePrice(13, 9, 7000);
		setPriceRow(13, 6000, new int[] { 10, 11, 12 });

		isMatrixInitialized = true;
	}

	// Hàm cập nhật đối xứng
	private static void updatePrice(int idx1, int idx2, int price) {
		FARE_MATRIX[idx1][idx2] = price;
		FARE_MATRIX[idx2][idx1] = price;
	}

	// Hàm set giá hàng loạt
	private static void setPriceRow(int fromIdx, int price, int[] toIndices) {
		for (int toIdx : toIndices) {
			updatePrice(fromIdx, toIdx, price);
		}
	}

	public static double calculateTripFare(String startStationName, String endStationName) {
		int startIdx = getStationIndex(startStationName);
		int endIdx = getStationIndex(endStationName);

		if (startIdx != -1 && endIdx != -1) {
			return (double) FARE_MATRIX[startIdx][endIdx];
		}

		return 7000.0;
	}

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

	// Mapping Tên Trạm -> Index
	private static int getStationIndex(String rawName) {
		if (rawName == null)
			return -1;
		String lower = rawName.toLowerCase();

		// 1. Metro Stations
		if (lower.contains("ben thanh") || lower.contains("s01"))
			return 0;
		if (lower.contains("nha hat") || lower.contains("s02"))
			return 1;
		if (lower.contains("ba son") || lower.contains("s03"))
			return 2;
		if (lower.contains("van thanh") || lower.contains("s04"))
			return 3;
		if (lower.contains("tan cang") || lower.contains("s05"))
			return 4;
		if (lower.contains("thao dien") || lower.contains("s06"))
			return 5;
		if (lower.contains("an phu") || lower.contains("s07"))
			return 6;
		if (lower.contains("rach chiec") || lower.contains("s08"))
			return 7;
		if (lower.contains("phuoc long") || lower.contains("s09"))
			return 8;
		if (lower.contains("binh thai") || lower.contains("s10"))
			return 9;
		if (lower.contains("thu duc") && !lower.contains("ky thuat") || lower.contains("s11"))
			return 10;
		if (lower.contains("cong nghe cao") || lower.contains("s12"))
			return 11;
		if (lower.contains("quoc gia") || lower.contains("s13"))
			return 12;
		if (lower.contains("suoi tien") && !lower.contains("bus") || lower.contains("s14"))
			return 13;

		return -1;
	}
}
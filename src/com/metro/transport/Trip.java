package com.metro.transport;

import com.metro.people.Driver;
import com.metro.business.Ticket;
import com.metro.enums.TripStatus;
import com.metro.infrastructure.Route;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Trip {
	private String tripId;
	private Train train;
	private Driver driver;
	private TripStatus status;
	private LocalDateTime departureTime;
	private LocalDateTime arrivalTime;
	private Route route;
	private List<Ticket> passengerTickets = new ArrayList<>();

	public Trip(String tripId, Train train, Driver driver, Route route, LocalDateTime departureTime) {
		this.tripId = tripId;
		this.train = train;
		this.driver = driver;
		this.route = route;
		this.departureTime = departureTime;
		this.status = TripStatus.SCHEDULED;
	}

	public Trip(String tripId, Train train, Driver driver) {
		this.tripId = tripId;
		this.train = train;
		this.driver = driver;
		this.status = TripStatus.SCHEDULED;
		this.departureTime = LocalDateTime.now().plusMinutes(15);
	}

	public void start() {
		this.status = TripStatus.ON_GOING;
		this.departureTime = LocalDateTime.now();
		System.out.println("\n🚆 TRIP " + tripId + " BẮT ĐẦU KHỞI HÀNH LÚC " + departureTime.toLocalTime());

		new Thread(this::simulateJourney).start();
	}

	private void simulateJourney() {
		try {

			String[] stations = { "Bến Thành", "Nhà Hát TP", "Ba Son", "Tân Cảng", "Suối Tiên" };

			for (int i = 0; i < stations.length; i++) {

				Thread.sleep(2000);

				Duration duration = Duration.between(departureTime, LocalDateTime.now());
				long secondsRun = duration.getSeconds();

				String currentStation = stations[i];
				System.out.println("   📍 Đã đến: " + currentStation + " (Thời gian chạy: " + secondsRun + "s)");

				performRandomTicketCheck(currentStation);
			}

			complete();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void performRandomTicketCheck(String location) {
		if (passengerTickets.isEmpty())
			return;

		Random rand = new Random();

		if (rand.nextBoolean()) {
			System.out.println("   👮 [KIỂM TRA VÉ] Đội an ninh đang kiểm tra tại " + location + "...");
			Ticket luckyPassenger = passengerTickets.get(rand.nextInt(passengerTickets.size()));

			System.out.println("      -> Kiểm tra khách: " + luckyPassenger.getOwnerName());
			if (luckyPassenger.isValid()) {
				System.out.println("      ✅ Vé Hợp Lệ.");
			} else {
				System.out.println("      🚨 VÉ KHÔNG HỢP LỆ! Mời xuống tàu.");
			}
		}
	}

	public void complete() {
		this.status = TripStatus.COMPLETED;
		this.arrivalTime = LocalDateTime.now();
		System.out.println("🏁 TRIP " + tripId + " ĐÃ VỀ BẾN LÚC " + arrivalTime.toLocalTime());
	}

	public void addPassengerTicket(Ticket t) {
		this.passengerTickets.add(t);
	}

	public LocalDateTime getDepartureTime() {
		return departureTime;
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public LocalDateTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(LocalDateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public void setStatus(TripStatus status) {
		this.status = status;
	}

	public void setDepartureTime(LocalDateTime departureTime) {
		this.departureTime = departureTime;
	}

	public TripStatus getStatus() {
		return status;
	}

	public String getTripId() {
		return tripId;
	}
}
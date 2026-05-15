package com.mesa.model;

public class Reservation {
    private String reservationId;
    private String username;
    private String restaurantId;
    private String restaurantName;
    private String date;
    private int numberOfPeople;
    private String reservationTime;

    public Reservation(String reservationId, String username, String restaurantId,
                       String restaurantName, String date, int numberOfPeople, String reservationTime) {
        this.reservationId = reservationId;
        this.username = username;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.date = date;
        this.numberOfPeople = numberOfPeople;
        this.reservationTime = reservationTime;
    }

    public String getReservationId() { return reservationId; }
    public String getUsername() { return username; }
    public String getRestaurantId() { return restaurantId; }
    public String getRestaurantName() { return restaurantName; }
    public String getDate() { return date; }
    public int getNumberOfPeople() { return numberOfPeople; }
    public String getReservationTime() { return reservationTime; }
}
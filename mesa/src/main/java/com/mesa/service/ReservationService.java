package com.mesa.service;

import com.mesa.model.Reservation;
import com.mesa.model.Restaurant;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private List<Reservation> reservations = new ArrayList<>();
    private final RestaurantService restaurantService;

    public ReservationService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public Reservation makeReservation(String username, String restaurantId,
                                       String date, int numberOfPeople) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        if (restaurant == null) return null;

        String reservationId = UUID.randomUUID().toString().substring(0, 8);
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("h:mm a"));

        Reservation reservation = new Reservation(
                reservationId, username, restaurantId,
                restaurant.getStoreName(), date, numberOfPeople, time
        );

        reservations.add(reservation);
        return reservation;
    }

    public List<Reservation> getUserReservations(String username) {
        return reservations.stream()
                .filter(r -> r.getUsername().equals(username))
                .collect(Collectors.toList());
    }
}
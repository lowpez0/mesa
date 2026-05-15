package com.mesa.service;

import com.mesa.model.Restaurant;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final CsvLoader csvLoader;

    public RestaurantService(CsvLoader csvLoader) {
        this.csvLoader = csvLoader;
    }

    public List<Restaurant> getAllRestaurants() {
        return csvLoader.getRestaurantList();
    }

    public Restaurant getRestaurantById(String storeId) {
        return csvLoader.getRestaurantList()
                .stream()
                .filter(restaurant -> restaurant.getStoreId().equals(storeId))
                .findFirst()
                .orElse(null);
    }

    public List<Restaurant> getRestaurantsByFoodType(String foodType) {
        return csvLoader.getRestaurantList()
                .stream()
                .filter(restaurant -> restaurant.getFoodType().equalsIgnoreCase(foodType))
                .collect(Collectors.toList());
    }

    public List<Restaurant> getRestaurantsByCity(String city) {
        return csvLoader.getRestaurantList()
                .stream()
                .filter(restaurant -> restaurant.getCity() != null &&
                        restaurant.getCity().toLowerCase().contains(city.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Restaurant> getTop4PopularRestaurants() {
        return csvLoader.getRestaurantList()
                .stream()
                .sorted(Comparator.comparingDouble(Restaurant::getAvgRating).reversed()
                        .thenComparing(Comparator.comparingInt(Restaurant::getTotalRating).reversed()))
                .limit(4)
                .collect(Collectors.toList());
    }

    public List<Restaurant> sortByMostReviewed(List<Restaurant> restaurants) {
        return restaurants.stream()
                .sorted(Comparator.comparingInt(Restaurant::getTotalRating).reversed())
                .collect(Collectors.toList());
    }

    public List<Restaurant> sortByHighestRated(List<Restaurant> restaurants) {
        return restaurants.stream()
                .sorted(Comparator.comparingDouble(Restaurant::getAvgRating).reversed())
                .collect(Collectors.toList());
    }

    public List<Restaurant> sortRestaurants(List<Restaurant> restaurants, String sortBy) {
        if ("rating".equalsIgnoreCase(sortBy)) {
            return sortByHighestRated(restaurants);
        } else {
            return sortByMostReviewed(restaurants);
        }
    }
}
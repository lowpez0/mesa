package com.mesa.service;

import com.mesa.model.Restaurant;
import com.mesa.model.Review;
import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvLoader {

    private List<Restaurant> restaurantList;
    private Map<String, List<Review>> reviewsList;
    private final String restaurantCsvPath;
    private final String reviewCsvPath;

    public CsvLoader(
            @Value("${resto.csv}") String restaurantCsvPath,
            @Value("${resto-review.csv}") String reviewCsvPath
    ) {
        this.restaurantCsvPath = restaurantCsvPath;
        this.reviewCsvPath = reviewCsvPath;
    }

    public void loadReviews() {
        reviewsList = new HashMap<>();
        try {
            ClassPathResource resource = new ClassPathResource(reviewCsvPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            CSVReader csvReader = new CSVReader(reader);
            String[] row;
            csvReader.readNext(); // skip yung first row
            DateTimeFormatter csvFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy h:mm a");

            while ((row = csvReader.readNext()) != null) {
                String storeId = row[0].trim();
                String reviewerId = row[6].trim();
                String createdAt = row[2].trim();
                String reviewText = row[4].trim();
                List<Review> reviews =
                        reviewsList.computeIfAbsent(
                                storeId,
                                k -> new ArrayList<>()
                        );

                // skip pag 3 na reviewText
                if (reviews.size() >= 3) {
                    continue;
                }
                OffsetDateTime parsedDate = OffsetDateTime.parse(createdAt, csvFormatter);
                String formattedDate = parsedDate.format(displayFormatter);

                Review review = new Review(
                        reviewerId,
                        formattedDate,
                        reviewText
                );
                reviews.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRestaurants() {
        restaurantList = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource(restaurantCsvPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            CSVReader csvReader = new CSVReader(reader);
            String[] row;
            csvReader.readNext(); //skips yung first row

            while ((row = csvReader.readNext()) != null) {
                String storeId = row[0];
                String storeName = row[1];
                String foodType = row[2];
                double avgRating = Double.parseDouble(row[3]);
                int totalRating = Integer.parseInt(row[4]);
                String city = row[5];
                List<Review> reviews = new ArrayList<>();
                if(reviewsList.containsKey(storeId)) {
                    reviews = reviewsList.get(storeId);
                }
                restaurantList.add(new Restaurant(
                        storeId,
                        storeName,
                        foodType,
                        avgRating,
                        totalRating,
                        city,
                        reviews
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    //debugging shenanigans
    public void printRestaurantsAndReviews() {
        System.out.println(restaurantList.size());
        for (Restaurant restaurant : restaurantList) {
            System.out.println("\n=================================");
            System.out.println(restaurant);
            if (restaurant.getReviewList().isEmpty()) {
                System.out.println("No reviews found.");
            } else {
                System.out.println("\nReviews:");
                for (Review review : restaurant.getReviewList()) {
                    System.out.println(review);
                }
            }
        }
    }

    public void testLoadReviews() {
        System.out.println("TOTAL STORE IDS: " + reviewsList.size());

        reviewsList.entrySet()
                .stream()
                .limit(5)
                .forEach(entry -> {

                    System.out.println("\nSTORE ID: " + entry.getKey());

                    entry.getValue()
                            .stream()
                            .limit(3)
                            .forEach(review ->
                                    System.out.println(review)
                            );
                });
    }

    @PostConstruct
    public void init() {
        loadReviews();
        loadRestaurants();
    }
}

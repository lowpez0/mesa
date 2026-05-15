package com.mesa.model;

import java.util.List;

public class Restaurant {

    private String storeId;
    private String storeName;
    private String foodType;
    private double avgRating;
    private int totalRating;
    private String city;
    private List<Review> reviewList;

    public Restaurant(String storeId, String storeName, String foodType, double avgRating,
                      int totalRating, String city, List<Review> reviewList) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.foodType = foodType;
        this.avgRating = avgRating;
        this.totalRating = totalRating;
        this.city = city;
        this.reviewList = reviewList;
    }

    public String getImagePath() {
        return switch (foodType.toLowerCase()) {
            case "filipino" -> "/images/filipino.jpg";
            case "japanese" -> "/images/japanese.jpg";
            case "korean" -> "/images/korean.jpg";
            case "asian" -> "/images/asian.jpg";
            case "chicken" -> "/images/chicken.jpg";
            case "chinese" -> "/images/chinese.jpg";
            case "seafood" -> "/images/seafood.jpg";
            case "indian" -> "/images/indian.jpg";
            case "sushi" -> "/images/sushi.jpg";
            case "cakes" -> "/images/cakes.jpg";
            case "biryani" -> "/images/biryani.jpg";
            case "thai" -> "/images/thai.jpg";
            default -> "/images/asian.jpg";
        };
    }

    public String getStoreId() {
        return storeId;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getFoodType() {
        return foodType;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "storeName='" + storeName + '\'' +
                ", foodType='" + foodType + '\'' +
                ", avgRating=" + avgRating +
                ", totalRating=" + totalRating +
                ", city='" + city + '\'' +
                ", totalReviews=" + reviewList.size() +
                '}';
    }


}

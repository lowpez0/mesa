package com.mesa.model;

public class Review {

    private String reviewerID;
    private String dateTime;
    private String reviewText;

    public Review(String reviewerID, String dateTime, String reviewText) {
        this.reviewerID = reviewerID;
        this.dateTime = dateTime;
        this.reviewText = reviewText;
    }

    public String getReviewerID() {
        return reviewerID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getReviewText() {
        return reviewText;
    }


    @Override
    public String toString() {
        return "Review{" +
                "reviewerID='" + reviewerID + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", reviewText='" + reviewText + '\'' +
                '}';
    }
}

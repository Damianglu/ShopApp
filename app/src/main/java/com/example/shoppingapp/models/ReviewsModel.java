package com.example.shoppingapp.models;

import com.example.shoppingapp.adapters.ReviewsAdapter;

public class ReviewsModel {

    String fullName;
    String rating;
    String review;
    String image;

    public ReviewsModel(){

    }

    public ReviewsModel(String fullName, String rating, String review,String image) {
        this.fullName = fullName;
        this.rating = rating;
        this.review = review;
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

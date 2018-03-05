package com.maks.model;

/**
 * Created by maks on 5/3/18.
 */

import com.google.gson.annotations.SerializedName;
import com.maks.babyneeds.Utility.Constants;

public class Review {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("p_id")
    @Expose
    private String pId;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("review_date")
    @Expose
    private String reviewDate;
    @SerializedName("reviewer_name")
    @Expose
    private String reviewerName;
    @SerializedName("title")
    @Expose
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public java.lang.String getTitle() {
        return title;
    }

    public void setTitle(java.lang.String title) {
        this.title = title;
    }
}
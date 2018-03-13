package com.app.ssoft.vrs.Model;

/**
 * Created by Shekahar.Shrivastava on 13-Mar-18.
 */

public class FeedbackData {
    public String userId;
    public String fullName;
    public String bookingID;
    public String feedback;

    public FeedbackData() {
    }

    public FeedbackData(String userId, String fullName, String bookingID, String feedback) {
        this.userId = userId;
        this.fullName = fullName;
        this.bookingID = bookingID;
        this.feedback = feedback;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}

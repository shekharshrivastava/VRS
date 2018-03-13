package com.app.ssoft.vrs.View;

/**
 * Created by Shekahar.Shrivastava on 13-Mar-18.
 */

public class BookRideData {
    public String userId;
    public String firstName;
    public String lastName;
    public String licenceNumber;
    public String docFile;
    public String address;
    public String contactNumber;
    public String emailID;
    public String bookingAmount;
    public boolean isBooked;
    public String carName;
    public String ownerName;
    public String ownerNumber;
    public String bookingDate;


    public BookRideData() {
    }

    public BookRideData(String userId, String firstName, String lastName, String licenceNumber, String docFile, String address, String contactNumber, String emailID, String bookingAmount, boolean isBooked, String carName, String ownerName, String ownerNumber, String bookingDate) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.licenceNumber = licenceNumber;
        this.docFile = docFile;
        this.address = address;
        this.contactNumber = contactNumber;
        this.emailID = emailID;
        this.bookingAmount = bookingAmount;
        this.isBooked = isBooked;
        this.carName = carName;
        this.ownerName = ownerName;
        this.ownerNumber = ownerNumber;
        this.bookingDate = bookingDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getDocFile() {
        return docFile;
    }

    public void setDocFile(String docFile) {
        this.docFile = docFile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(String bookingAmount) {
        this.bookingAmount = bookingAmount;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerNumber() {
        return ownerNumber;
    }

    public void setOwnerNumber(String ownerNumber) {
        this.ownerNumber = ownerNumber;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
}

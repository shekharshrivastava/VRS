package com.app.ssoft.vrs.Model;

/**
 * Created by shekharshrivastava on 03/03/18.
 */

public class VehicleData {
    public String userID;
   public String vehicleType;
    public String ownerName;
    public String vehiclePhoto;
    public String vehicleModel;
    public String aip;
    public String routeType;
    public String source;
    public String destination;
    public  String rateValue;
    public  String fuelType;
    public  String numberOfseat;
    public  String driverReq;
    public  String driverName;
    public  String driverNumber;
    public  String driverAddress;
    public  String licenceNumber;
    public  String aadharNumber;
    public  String driverPhoto;
    public String currentUserID;
    public boolean isVehBooked;
    public String bookingDate;
    public String customerUserID;
    public String advanceAmnt;

    public VehicleData (){

    }



    public VehicleData(String userID, String vehicleType, String ownerName, String vehiclePhoto, String vehicleModel, String aip, String routeType, String source, String destination, String rateValue, String fuelType, String numberOfseat, String driverReq, String driverName, String driverNumber, String driverAddress, String licenceNumber, String aadharNumber, String driverPhoto, String currentUserID, boolean isVehBooked, String bookingDate,String customerUserID,String advanceAmnt) {
        this.userID = userID;
        this.vehicleType = vehicleType;
        this.ownerName = ownerName;
        this.vehiclePhoto = vehiclePhoto;
        this.vehicleModel = vehicleModel;
        this.aip = aip;
        this.routeType = routeType;
        this.source = source;
        this.destination = destination;
        this.rateValue = rateValue;
        this.fuelType = fuelType;
        this.numberOfseat = numberOfseat;
        this.driverReq = driverReq;
        this.driverName = driverName;
        this.driverNumber = driverNumber;
        this.driverAddress = driverAddress;
        this.licenceNumber = licenceNumber;
        this.aadharNumber = aadharNumber;
        this.driverPhoto = driverPhoto;
        this.currentUserID = currentUserID;
        this.isVehBooked = isVehBooked;
        this.bookingDate = bookingDate;
        this.customerUserID = customerUserID;
        this.advanceAmnt = advanceAmnt;
    }

    public String getCustomerUserID() {
        return customerUserID;
    }

    public void setCustomerUserID(String customerUserID) {
        this.customerUserID = customerUserID;
    }

    public boolean isVehBooked() {
        return isVehBooked;
    }

    public void setVehBooked(boolean vehBooked) {
        isVehBooked = vehBooked;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setVehiclePhoto(String vehiclePhoto) {
        this.vehiclePhoto = vehiclePhoto;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public void setAip(String aip) {
        this.aip = aip;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setNumberOfseat(String numberOfseat) {
        this.numberOfseat = numberOfseat;
    }

    public void setDriverReq(String driverReq) {
        this.driverReq = driverReq;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public void setDriverPhoto(String driverPhoto) {
        this.driverPhoto = driverPhoto;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getVehiclePhoto() {
        return vehiclePhoto;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getAip() {
        return aip;
    }

    public String getRouteType() {
        return routeType;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getRateValue() {
        return rateValue;
    }

    public String getFuelType() {
        return fuelType;
    }

    public String getNumberOfseat() {
        return numberOfseat;
    }

    public String getDriverReq() {
        return driverReq;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public String getDriverAddress() {
        return driverAddress;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public String getDriverPhoto() {
        return driverPhoto;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        this.currentUserID = currentUserID;
    }

    public String getAdvanceAmnt() {
        return advanceAmnt;
    }

    public void setAdvanceAmnt(String advanceAmnt) {
        this.advanceAmnt = advanceAmnt;
    }
}

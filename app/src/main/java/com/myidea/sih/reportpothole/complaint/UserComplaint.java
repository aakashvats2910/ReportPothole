package com.myidea.sih.reportpothole.complaint;

public class UserComplaint {

    public String comment;
    public double lat;
    public double lng;
    public String complainerName;
    public String userID;
    public String mobileNumber;
    public String imageUniqueID;
    public String status;

    // Status functionality will be added later.

    public UserComplaint() {}

    public UserComplaint(String comment, double lat, double lng, String complainerName, String userID, String mobileNumber, String imageUniqueID, String status) {
        this.comment = comment;
        this.lat = lat;
        this.lng = lng;
        this.complainerName = complainerName;
        this.userID = userID;
        this.mobileNumber = mobileNumber;
        this.imageUniqueID = imageUniqueID;
        this.status = status;
    }

}

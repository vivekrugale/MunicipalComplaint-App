package com.example.complaintapp;

public class modelComplaints {
    String area, complaint, user, field;

    modelComplaints()
    {

    }

    public modelComplaints(String area, String complaint, String user, String field) {
        this.area = area;
        this.complaint = complaint;
        this.user = user;
        this.field = field;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }


}

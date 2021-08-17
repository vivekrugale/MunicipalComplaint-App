package com.example.complaintapp;

public class dataholder_complaintData {
    String complaint, area;

    public dataholder_complaintData(String complaint, String area) {
        this.complaint = complaint;
        this.area = area;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}

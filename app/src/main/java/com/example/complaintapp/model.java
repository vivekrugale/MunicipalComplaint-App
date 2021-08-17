package com.example.complaintapp;

public class model {
    String Name, Ward;

    model()
    {

    }

    public model(String Name, String Ward) {
        this.Name = Name;
        this.Ward = Ward;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = Name;
    }

    public String getWard() { return Ward; }

    public void setWard(String ward) {
        this.Ward = Ward;
    }
}

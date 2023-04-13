package com.example.serviceapp;

public class historyClass {
    private String service;
    private String date;
    private String status;

    public historyClass(String service, String date,String status) {
        this.service=service;
        this.status=status;
        this.date=date;


    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
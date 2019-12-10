package com.example.sistemagestioncuentas.model;

public class Usuario {
    private String uid;
    private  String name;
    private String email;
    private String status;
    private String information;
    public Usuario() {
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Usuario(String uid, String name, String email, String status, String information) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.status = status;
        this.information = information;
    }



}

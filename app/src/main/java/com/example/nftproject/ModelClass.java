package com.example.nftproject;

public class ModelClass {

    String ID ;
    String URI ;
    String owner ;

    public ModelClass(String ID, String URI, String owner) {
        this.ID = ID;
        this.URI = URI;
        this.owner = owner;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

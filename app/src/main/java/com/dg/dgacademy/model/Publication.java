package com.dg.dgacademy.model;

import org.parceler.Parcel;

@Parcel
public class Publication {

    public String title;
    public String createdAt;
    public Owner owner;
    public String url;
    public String content;
    public int likes;

    public Publication(){}

}

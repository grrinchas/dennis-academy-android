package com.dg.dgacademy.model;

import org.parceler.Parcel;

@Parcel
public class Draft {

    public String id;
    public String title;
    public String createdAt;
    public Owner owner;
    public String content;
    public int likes;

    public Draft(){}

}

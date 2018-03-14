package com.dg.dgacademy.model;


import org.parceler.Parcel;

@Parcel
public class Notification {

    public enum Type {
        LIKE_DRAFT,
        UNLIKE_DRAFT,
        LIKE_PUBLICATION,
        UNLIKE_PUBLICATION
    }

    public Type type;
   public String id;
   public Owner sender;
   public String message;
   public String createdAt;
   public boolean isPresent;
}

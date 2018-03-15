package com.dg.dgacademy.model;


import java.util.List;

import api.fragment.PublicationInfo;

public class PublicationsEvent {
    public List<PublicationInfo> publications;
    public PublicationsEvent(List<PublicationInfo> publicationsEvents){
        this.publications = publicationsEvents;
    }
}

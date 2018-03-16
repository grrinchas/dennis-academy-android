package com.dg.dgacademy.model;


import java.util.List;

import api.fragment.DraftInfo;

public class DraftsEvent {
    public List<DraftInfo> drafts;
    public DraftsEvent(List<DraftInfo> drafts) {
        this.drafts = drafts;
    }
}

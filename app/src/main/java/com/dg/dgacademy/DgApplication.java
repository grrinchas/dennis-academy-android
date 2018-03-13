package com.dg.dgacademy;


import android.app.Application;

import com.dg.dgacademy.Model.Owner;
import com.dg.dgacademy.Model.Publication;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

public class DgApplication extends Application {


    private static void getPublications() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Publication> publications = new ArrayList<>();

                Publication info1 = new Publication();
                info1.title = "Lorem Ipsum 2";
                info1.createdAt = "Jan 23, 2019";
                info1.url = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
                info1.likes = 20;
                info1.content = "# This is big header \n \n ## Smaller header \n\n * List Item 1 \n * List Item 2\n\n [I am a link](https://www.google.co.uk)";
                Owner owner1 = new Owner();

                owner1.name = "admin1";
                owner1.picture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
                owner1.bio = "This is some bio";

                info1.owner = owner1;

                Publication info2 = new Publication();
                info2.title = "Lorem Ipsum 2";
                info2.createdAt = "Jan 23, 2019";
                info2.url = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
                info2.likes = 20;
                info2.content = "# This is big header \n \n ## Smaller header \n\n * List Item 1 \n * List Item 2\n\n [I am a link](https://www.google.co.uk)";

                Owner owner2 = new Owner();
                owner2.name = "admin1";
                owner2.picture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
                owner2.bio = "This is some bio";
                info2.owner = owner2;

                publications.add(info1);
                publications.add(info2);
                EventBus.getDefault().postSticky(publications);
            }
        });

        thread.start();
    }

    public static void requestDrafts() {
        getPublicDrafts();
    }


    public static void requestPublications() {
        getPublications();
    }

    private static void getPublicDrafts() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<DraftInfo> drafts = new ArrayList<>();

                DraftInfo info1 = new DraftInfo();
                info1.title = "Lorem Ipsum 2";
                info1.createdAt = "Jan 23, 2019";
                info1.ownerName = "admin1";
                info1.preview = "This is some previou aosid a pbapos pas apsdfo ihapsuidhfaspdfaisdh apsdifh asdf";
                info1.ownerPicture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
                info1.likes = 20;

                DraftInfo info2 = new DraftInfo();
                info2.title = "Lorem Ipsum 2";
                info2.createdAt = "Jan 23, 2019";
                info2.ownerName = "admin1";
                info2.preview = "This is some previou aosid a pbapos pas apsdfo ihapsuidhfaspdfaisdh apsdifh asdf";
                info2.ownerPicture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
                info2.likes = 20;


                drafts.add(info1);
                drafts.add(info2);

                EventBus.getDefault().postSticky(drafts);
            }
        });
        thread.start();

    }

    @Parcel
    static class DraftInfo {

        public DraftInfo() {
        }

        public String title;
        public String preview;
        public String createdAt;
        public String ownerName;
        public String ownerPicture;
        public int likes;

    }
}





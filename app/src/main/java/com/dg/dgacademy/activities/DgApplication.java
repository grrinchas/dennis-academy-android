package com.dg.dgacademy.activities;


import android.app.Application;

import com.dg.dgacademy.model.Draft;
import com.dg.dgacademy.model.Owner;
import com.dg.dgacademy.model.Profile;
import com.dg.dgacademy.model.Publication;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class DgApplication extends Application {


    public static void requestProfile() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile profile = new Profile();

                profile.username = "admin1";
                profile.picture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
                profile.bio = "This is some bio";
                profile.email = "dg4675dg@gmail.com";

                EventBus.getDefault().postSticky(profile);
            }
        });
        thread.start();
    }

    public static void requestPublicDrafts() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Draft> drafts = new ArrayList<>();

                Draft info1 = new Draft();
                info1.title = "Lorem Ipsum 2";
                info1.createdAt = "Jan 23, 2019";
                info1.content = "# This is big header \n \n ## Smaller header \n\n * List Item 1 \n * List Item 2\n\n [I am a link](https://www.google.co.uk)";
                info1.likes = 20;

                Owner owner1 = new Owner();
                owner1.name = "admin1";

                owner1.picture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
                owner1.bio = "This is some bio";
                info1.owner = owner1;

                Draft info2 = new Draft();
                info2.title = "Lorem Ipsum 2";
                info2.createdAt = "Jan 23, 2019";
                info2.likes = 20;

                Owner owner2 = new Owner();
                owner2.name = "admin1";
                owner2.picture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
                owner2.bio = "This is some bio";
                info2.content = "# This is big header \n \n ## Smaller header \n\n * List Item 1 \n * List Item 2\n\n [I am a link](https://www.google.co.uk)";
                info2.owner = owner2;

                drafts.add(info1);
                drafts.add(info2);

                EventBus.getDefault().postSticky(drafts);
            }
        });
        thread.start();

    }

    public static void requestPublicPublications() {
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


    public static void requestPrivateDrafts() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Draft> drafts = new ArrayList<>();

                Draft info1 = new Draft();
                info1.title = "Lorem Ipsum 2";
                info1.createdAt = "Jan 23, 2019";
                info1.content = "# This is big header \n \n ## Smaller header \n\n * List Item 1 \n * List Item 2\n\n [I am a link](https://www.google.co.uk)";
                info1.likes = 20;

                Owner owner1 = new Owner();
                owner1.name = "admin1";

                owner1.picture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
                owner1.bio = "This is some bio";
                info1.owner = owner1;

                Draft info2 = new Draft();
                info2.title = "Lorem Ipsum 2";
                info2.createdAt = "Jan 23, 2019";
                info2.likes = 20;

                Owner owner2 = new Owner();
                owner2.name = "admin1";
                owner2.picture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
                owner2.bio = "This is some bio";
                info2.content = "# This is big header \n \n ## Smaller header \n\n * List Item 1 \n * List Item 2\n\n [I am a link](https://www.google.co.uk)";
                info2.owner = owner2;

                drafts.add(info1);
                drafts.add(info2);

                EventBus.getDefault().postSticky(drafts);
            }
        });
        thread.start();

    }

    public static void requestPrivatePublications() {
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

}





package com.dg.dgacademy;

import android.app.Application;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.authentication.request.SignUpRequest;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.result.Credentials;
import com.dg.dgacademy.model.Draft;
import com.dg.dgacademy.model.DraftsEvent;
import com.dg.dgacademy.model.Notification;
import com.dg.dgacademy.model.NotificationsEvent;
import com.dg.dgacademy.model.Owner;
import com.dg.dgacademy.model.Profile;
import com.dg.dgacademy.model.Publication;
import com.dg.dgacademy.model.PublicationsEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class DgApplication extends Application {

    private static AuthenticationAPIClient auth0Client;

    @Override
    public void onCreate() {
        super.onCreate();

        Auth0 auth0 = new Auth0(getString(R.string.com_auth0_client_id), getString(R.string.com_auth0_domain));
        auth0.setOIDCConformant(true);

        auth0Client = new AuthenticationAPIClient(auth0);
    }

    public static void signUp(String username, String email, String password) {
        SignUpRequest request = auth0Client.signUp(email, password, username, "academy-db-connection");
        request.start(new BaseCallback<Credentials, AuthenticationException>() {
            @Override
            public void onSuccess(Credentials payload) {
               EventBus.getDefault().post(payload);
            }

            @Override
            public void onFailure(AuthenticationException error) {
                EventBus.getDefault().post(error);
            }
        });
    }

    public static void requestProfile() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Profile profile = new Profile();

                profile.id = "1";
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
                info1.id = "1";
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

                info2.id = "2";
                info2.title = "Lorem Ipsum 2";
                info2.createdAt = "Jan 23, 2019";
                info2.likes = 20;

                Owner owner2 = new Owner();
                owner2.name = "admin1";
                owner2.picture = "https://media.istockphoto.com/photos/growing-money-chart-in-rise-picture-id506181336";
                owner2.bio = "This is some bio";
                info2.content = "# This is big header \n \n ## Smaller header \n\n * List Item 1 \n * List Item 2\n\n [I am a link](https://www.google.co.uk)";
                info2.owner = owner2;

                drafts.add(info1);
                drafts.add(info2);

                DraftsEvent event = new DraftsEvent();
                event.drafts = drafts;
                EventBus.getDefault().postSticky(event);
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
                info1.id = "1";
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
                info2.id = "2";
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

                PublicationsEvent event = new PublicationsEvent();
                event.publications = publications;
                EventBus.getDefault().postSticky(event);
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
                info1.id = "1";
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
                info2.id = "2";
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

                DraftsEvent event = new DraftsEvent();
                event.drafts = drafts;
                EventBus.getDefault().postSticky(event);
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
                info1.id = "1";
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
                info2.id = "2";
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

                PublicationsEvent event = new PublicationsEvent();
                event.publications = publications;
                EventBus.getDefault().postSticky(event);
            }
        });
        thread.start();
    }

    public static void requestNotifications() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Notification> notifications = new ArrayList<>();

                Owner owner = new Owner();
                owner.id = "1";
                owner.name = "admin1";
                owner.picture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
                owner.bio = "This is some bio";

                Notification note1 = new Notification();
                note1.type = Notification.Type.LIKE_DRAFT;
                note1.sender = owner;
                note1.message = "Lorem ipsum message";
                note1.isPresent = true;
                note1.createdAt = "Jun 12, 2017";

                Notification note2 = new Notification();
                note2.type = Notification.Type.LIKE_DRAFT;
                note2.sender = owner;
                note2.message = "Lorem ipsum message";
                note2.isPresent = false;
                note2.createdAt = "Jun 12, 2017";

                Notification note3 = new Notification();
                note3.type = Notification.Type.UNLIKE_DRAFT;
                note3.sender = owner;
                note3.message = "Lorem ipsum message";
                note3.isPresent = true;
                note3.createdAt = "Jun 12, 2017";

                Notification note4 = new Notification();
                note4.type = Notification.Type.UNLIKE_DRAFT;
                note4.sender = owner;
                note4.message = "Lorem ipsum message";
                note4.isPresent = false;
                note4.createdAt = "Jun 12, 2017";

                Notification note5 = new Notification();
                note5.type = Notification.Type.LIKE_PUBLICATION;
                note5.sender = owner;
                note5.message = "Lorem ipsum message";
                note5.isPresent = true;
                note5.createdAt = "Jun 12, 2017";

                Notification note6 = new Notification();
                note6.type = Notification.Type.LIKE_PUBLICATION;
                note6.sender = owner;
                note6.message = "Lorem ipsum message";
                note6.isPresent = false;
                note6.createdAt = "Jun 12, 2017";

                Notification note7 = new Notification();
                note7.type = Notification.Type.UNLIKE_PUBLICATION;
                note7.sender = owner;
                note7.message = "Lorem ipsum message";
                note7.isPresent = true;
                note7.createdAt = "Jun 12, 2017";

                Notification note8 = new Notification();
                note8.type = Notification.Type.UNLIKE_PUBLICATION;
                note8.sender = owner;
                note8.message = "Lorem ipsum message";
                note8.isPresent = false;
                note8.createdAt = "Jun 12, 2017";

                notifications.add(note1);
                notifications.add(note2);
                notifications.add(note3);
                notifications.add(note4);
                notifications.add(note5);
                notifications.add(note6);
                notifications.add(note7);
                notifications.add(note8);

                NotificationsEvent event = new NotificationsEvent();
                event.notifications = notifications;
                EventBus.getDefault().postSticky(event);
            }
        });

        thread.start();
    }


}





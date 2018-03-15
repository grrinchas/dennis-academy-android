package com.dg.dgacademy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.CustomTypeAdapter;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.ResponseField;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.cache.normalized.CacheKey;
import com.apollographql.apollo.cache.normalized.CacheKeyResolver;
import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory;
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy;
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory;
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper;
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory;
import com.apollographql.apollo.exception.ApolloException;
import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.authentication.request.SignUpRequest;
import com.auth0.android.authentication.storage.CredentialsManagerException;
import com.auth0.android.authentication.storage.SecureCredentialsManager;
import com.auth0.android.authentication.storage.SharedPreferencesStorage;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.request.AuthenticationRequest;
import com.auth0.android.result.Credentials;
import com.dg.dgacademy.activities.LoginActivity;
import com.dg.dgacademy.model.Draft;
import com.dg.dgacademy.model.DraftsEvent;
import com.dg.dgacademy.model.GlobalNetworkException;
import com.dg.dgacademy.model.Owner;
import com.dg.dgacademy.model.Profile;
import com.dg.dgacademy.model.PublicationsEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import api.AdminQuery;
import api.AllPublicationsQuery;
import api.AuthenticateMutation;
import api.DeleteNotificationMutation;
import api.fragment.PublicationInfo;
import api.type.CustomType;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class DgApplication extends Application {

    private static AuthenticationAPIClient auth0Client;
    private static ApolloClient apolloClient;
    private static String NETWORK_ERROR;
    private static String GRAPHCOOL_URL;
    private static SecureCredentialsManager tokenStorage;
    private static String USER_ID = "";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().register(this);

        Auth0 auth0 = new Auth0(getString(R.string.com_auth0_client_id), getString(R.string.com_auth0_domain));
        auth0.setOIDCConformant(true);

        context = getApplicationContext();
        NETWORK_ERROR = getString(R.string.network_error);
        GRAPHCOOL_URL = getString(R.string.graphCool_url);
        auth0Client = new AuthenticationAPIClient(auth0);
        tokenStorage = new SecureCredentialsManager(context, auth0Client, new SharedPreferencesStorage(context));

        apolloClient = ApolloClient.builder().serverUrl(GRAPHCOOL_URL).build();
    }

    public static boolean isLoggedIn() {
        return tokenStorage.hasValidCredentials();
    }

    public static void login(String email, String password) {
        AuthenticationRequest request = auth0Client.login(email, password, "academy-db-connection");
        request.setAudience("dg-academy");
        request.setScope("offline_access");

        request.start(new BaseCallback<Credentials, AuthenticationException>() {

            @Override
            public void onSuccess(Credentials payload) {
                tokenStorage.saveCredentials(payload);
                EventBus.getDefault().post(payload);
            }

            @Override
            public void onFailure(AuthenticationException error) {
                EventBus.getDefault().post(new GlobalNetworkException(error.getDescription()));
            }

        });

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
                EventBus.getDefault().post(new GlobalNetworkException(error.getDescription()));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGlobalNetworkException(GlobalNetworkException e) {
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshTokenFailure(CredentialsManagerException e) {
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, LoginActivity.class));
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
        checkCredentials(() -> {
            AllPublicationsQuery query = AllPublicationsQuery.builder().build();

            apolloClient.query(query).enqueue(new ApolloCall.Callback<AllPublicationsQuery.Data>() {
                @Override
                public void onResponse(@Nonnull Response<AllPublicationsQuery.Data> response) {
                    if (response.data().allPublications() != null) {
                        EventBus.getDefault().post(response.data().allPublications());
                        List<PublicationInfo> publications = response.data().allPublications().stream().map(p -> p.fragments().publicationInfo()).collect(Collectors.toList());
                        EventBus.getDefault().postSticky(new PublicationsEvent(publications));
                    } else {
                        EventBus.getDefault().post(new GlobalNetworkException(NETWORK_ERROR));
                    }
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    EventBus.getDefault().post(new GlobalNetworkException(NETWORK_ERROR));
                }
            });
        });
    }


    public static void requestPublication(String id) {
        checkCredentials(() -> {
            AllPublicationsQuery query = AllPublicationsQuery.builder().build();

            apolloClient.query(query).enqueue(new ApolloCall.Callback<AllPublicationsQuery.Data>() {
                @Override
                public void onResponse(@Nonnull Response<AllPublicationsQuery.Data> response) {
                    if (response.data().allPublications() != null) {
                        EventBus.getDefault().post(response.data().allPublications());
                        List<PublicationInfo> publications = response.data().allPublications()
                                .stream().map(p -> p.fragments().publicationInfo()).collect(Collectors.toList());
                        EventBus.getDefault().postSticky(publications.stream().filter(p -> Objects.equals(p.id(), id)).findFirst().get());
                    } else {
                        EventBus.getDefault().post(new GlobalNetworkException(NETWORK_ERROR));
                    }
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    EventBus.getDefault().post(new GlobalNetworkException(NETWORK_ERROR));
                }
            });
        });
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
        checkCredentials(() -> {
            AllPublicationsQuery query = AllPublicationsQuery.builder().build();

            apolloClient.query(query).enqueue(new ApolloCall.Callback<AllPublicationsQuery.Data>() {
                @Override
                public void onResponse(@Nonnull Response<AllPublicationsQuery.Data> response) {
                    if (response.data().allPublications() != null) {
                        EventBus.getDefault().post(response.data().allPublications());
                        List<PublicationInfo> publications = response.data().allPublications()
                                .stream().map(p -> p.fragments().publicationInfo())
                                .filter(p -> Objects.equals(p.owner().fragments().userInfo().id(), USER_ID))
                                .collect(Collectors.toList());

                        EventBus.getDefault().postSticky(new PublicationsEvent(publications));
                    } else {
                        EventBus.getDefault().post(new GlobalNetworkException(NETWORK_ERROR));
                    }
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    EventBus.getDefault().post(new GlobalNetworkException(NETWORK_ERROR));
                }
            });
        });
    }

    private static void checkCredentials(Runnable runnable) {
        if (tokenStorage.hasValidCredentials() && !USER_ID.isEmpty()) {
            runnable.run();
        } else {
            tokenStorage.getCredentials(new BaseCallback<Credentials, CredentialsManagerException>() {
                @Override
                public void onSuccess(Credentials payload) {
                    tokenStorage.saveCredentials(payload);
                    authorize(payload.getAccessToken(), runnable);
                }

                @Override
                public void onFailure(CredentialsManagerException error) {
                    EventBus.getDefault().post(error);
                }
            });
        }

    }

    public static void deleteNotification(String id) {
        checkCredentials(() -> {
            AdminQuery adminQuery = AdminQuery.builder().id(USER_ID).build();
            DeleteNotificationMutation delete = DeleteNotificationMutation.builder().id(id).build();
            apolloClient.mutate(delete).refetchQueries(adminQuery).enqueue(new ApolloCall.Callback<DeleteNotificationMutation.Data>() {
                @Override
                public void onResponse(@Nonnull Response<DeleteNotificationMutation.Data> response) {
                    if (response.data().deleteNotification() != null) {
                        EventBus.getDefault().post(response.data().deleteNotification());
                    } else {
                        EventBus.getDefault().post(new GlobalNetworkException(NETWORK_ERROR));
                    }

                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    EventBus.getDefault().post(new GlobalNetworkException(NETWORK_ERROR));
                }
            });
        });
    }


    public static void requestAdmin() {
        checkCredentials(() -> {
            AdminQuery adminQuery = AdminQuery.builder().id(USER_ID).build();
            apolloClient.query(adminQuery).httpCachePolicy(HttpCachePolicy.NETWORK_ONLY).enqueue(new ApolloCall.Callback<AdminQuery.Data>() {
                @Override
                public void onResponse(@Nonnull Response<AdminQuery.Data> response) {
                    if (response.data().User() != null) {
                        EventBus.getDefault().postSticky(response.data().User());
                    } else {
                        EventBus.getDefault().post(new GlobalNetworkException(NETWORK_ERROR));
                    }
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    EventBus.getDefault().post(new GlobalNetworkException(e.getMessage()));
                }
            });
        });
    }

    private static ApolloClient getApolloClient(String token) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder().method(original.method(), original.body());
                    builder.header("Authorization", "Bearer " + token);
                    return chain.proceed(builder.build());
                })
                .build();

        ApolloSqlHelper apolloSqlHelper = ApolloSqlHelper.create(context, "db_dgacademy");
        NormalizedCacheFactory sqlCache = new SqlNormalizedCacheFactory(apolloSqlHelper);

        CacheKeyResolver resolver = new CacheKeyResolver() {
            @Nonnull
            @Override
            public CacheKey fromFieldRecordSet(@Nonnull ResponseField field, @Nonnull Map<String, Object> recordSet) {
                return formatCacheKey((String) recordSet.get("id"));
            }

            @Nonnull
            @Override
            public CacheKey fromFieldArguments(@Nonnull ResponseField field, @Nonnull Operation.Variables variables) {
                return formatCacheKey((String) field.resolveArgument("id", variables));
            }

            private CacheKey formatCacheKey(String id) {
                if (id == null || id.isEmpty()) {
                    return CacheKey.NO_KEY;
                } else {
                    return CacheKey.from(id);
                }
            }
        };

        NormalizedCacheFactory memoryThenSqlCache = new LruNormalizedCacheFactory(
                EvictionPolicy.builder().maxSizeBytes(10 * 1024).build()
        ).chain(sqlCache);


        CustomTypeAdapter<Date> dateAdapterAdapter = new CustomTypeAdapter<Date>() {
            @Override
            public Date decode(String value) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(value);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public String encode(Date value) {
                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(value);
            }
        };


        return ApolloClient
                .builder()
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST)
                .serverUrl(GRAPHCOOL_URL)
                .addCustomTypeAdapter(CustomType.DATETIME, dateAdapterAdapter)
                .normalizedCache(memoryThenSqlCache, resolver)
                .okHttpClient(httpClient)
                .build();
    }


    private static void authorize(String token, Runnable runnable) {

        Log.d("Auth0 token is", token);
        AuthenticateMutation authenticate = AuthenticateMutation.builder().token(token).build();
        apolloClient.mutate(authenticate).enqueue(new ApolloCall.Callback<AuthenticateMutation.Data>() {
            @Override
            public void onResponse(@Nonnull Response<AuthenticateMutation.Data> response) {
                if (response.data().authenticate() != null) {
                    apolloClient = getApolloClient(response.data().authenticate().token());
                    USER_ID = response.data().authenticate().id();
                    runnable.run();

                } else {
                    EventBus.getDefault().post(new GlobalNetworkException(NETWORK_ERROR));
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                EventBus.getDefault().post(new GlobalNetworkException(e.getMessage()));
            }

        });
    }

}





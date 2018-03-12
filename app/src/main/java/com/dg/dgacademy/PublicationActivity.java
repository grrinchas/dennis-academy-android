package com.dg.dgacademy;


import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.noties.markwon.Markwon;

public class PublicationActivity extends AppCompatActivity {


    public static final String OWNER_BIO = "OWNER_BIO";
    public static final String OWNER_PICTURE = "OWNER_PICTURE";
    public static final String OWNER_NAME = "OWNER_NAME";
    public static final String PUBLICATION_TITLE = "PUBLICATION_TITLE";
    public static final String PUBLICATION_CONTENT = "PUBLICATION_CONTENT";
    public static final String PUBLICATION_CREATED_AT = "PUBLICATION_CREATED_AT";
    public static final String PUBLICATION_LIKES = "PUBLICATION_LIKES";
    public static final String PUBLICATION_IMAGE = "PUBLICATION_IMAGE";

    private static final String BUNDLE = "BUNDLE";

    private Bundle bundle;


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication);
        initToolbar();

        if(bundle == null)
            this.bundle = getIntent().getExtras();

        initBody(bundle);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putBundle(BUNDLE, bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
       // initBody(savedInstanceState.getBundle(BUNDLE));
    }

    private void initBody(Bundle bundle) {
        if (bundle != null) {
            TextView ownerName = findViewById(R.id.publication_owner_name);
            ownerName.setText(bundle.getString(OWNER_NAME));
            TextView ownerBio = findViewById(R.id.publication_owner_bio);
            ownerBio.setText(bundle.getString(OWNER_BIO));
            TextView pubTitle = findViewById(R.id.publication_title);
            pubTitle.setText(bundle.getString(PUBLICATION_TITLE));

            TextView pubContent = findViewById(R.id.publication_content);
            Markwon.setMarkdown(pubContent, bundle.getString(PUBLICATION_CONTENT));

            TextView pubCreatedAtAndLikes = findViewById(R.id.publication_created_at_and_likes);
            String createdAt = bundle.getString(PUBLICATION_CREATED_AT);
            String likes = String.valueOf(bundle.getInt(PUBLICATION_LIKES));
            pubCreatedAtAndLikes.setText(createdAt + "  |  LIKES(" + likes + ")");

            ImageView ownerPicture = findViewById(R.id.publication_owner_picture);
            Picasso.get().load(bundle.getString(OWNER_PICTURE)).fit().into(ownerPicture);


            ImageView pubImage = findViewById(R.id.publication_image);
            Picasso.get().load(bundle.getString(PUBLICATION_IMAGE)).fit().into(pubImage);

        }

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findViewById(R.id.toolbar_menu).setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));
        findViewById(R.id.publication_like).setOnClickListener(v -> Log.d("Publication", "Click like publication"));
    }
}

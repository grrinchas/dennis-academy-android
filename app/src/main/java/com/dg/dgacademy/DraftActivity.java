package com.dg.dgacademy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.noties.markwon.Markwon;


public class DraftActivity extends AppCompatActivity {

    private Bundle bundle;


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        initToolbar();

        if(bundle == null)
            this.bundle = getIntent().getExtras();

        initBody(bundle);

    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findViewById(R.id.toolbar_menu).setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));
        findViewById(R.id.draft_like).setOnClickListener(v -> Log.d("Draft", "Click like draft"));
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(C.BUNDLE, bundle);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initBody(savedInstanceState.getBundle(C.BUNDLE));
    }

    private void initBody(Bundle bundle) {
        if (bundle != null) {
            TextView ownerName = findViewById(R.id.draft_owner_name);
            ownerName.setText(bundle.getString(C.OWNER_NAME));
            TextView ownerBio = findViewById(R.id.draft_owner_bio);
            ownerBio.setText(bundle.getString(C.OWNER_BIO));
            TextView pubTitle = findViewById(R.id.draft_title);
            pubTitle.setText(bundle.getString(C.TITLE));

            TextView pubContent = findViewById(R.id.draft_content);
            Markwon.setMarkdown(pubContent, bundle.getString(C.CONTENT));

            TextView pubCreatedAtAndLikes = findViewById(R.id.draft_created_at_and_likes);
            String createdAt = bundle.getString(C.CREATED_AT);
            String likes = String.valueOf(bundle.getInt(C.LIKES));
            pubCreatedAtAndLikes.setText(createdAt + "  |  LIKES(" + likes + ")");

            ImageView ownerPicture = findViewById(R.id.draft_owner_picture);
            Picasso.get().load(bundle.getString(C.OWNER_PICTURE)).fit().into(ownerPicture);

        }

    }
}

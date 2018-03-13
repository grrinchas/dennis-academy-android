package com.dg.dgacademy.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.dg.dgacademy.R;
import com.dg.dgacademy.model.Publication;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.noties.markwon.Markwon;

public class PublicationActivity extends AppCompatActivity {

    @BindView(R.id.publication_owner_name) TextView ownerName;
    @BindView(R.id.publication_owner_bio) TextView ownerBio;
    @BindView(R.id.publication_owner_picture) ImageView ownerPicture;
    @BindView(R.id.publication_image) ImageView pubImage;
    @BindView(R.id.publication_title) TextView pubTitle;
    @BindView(R.id.publication_content) TextView pubContent;
    @BindView(R.id.publication_created_at_and_likes) TextView pubLikes;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Publication pub = Parcels.unwrap(getIntent().getExtras().getParcelable("BUNDLE"));
        ownerName.setText(pub.owner.name);
        ownerBio.setText(pub.owner.bio);
        Picasso.get().load(pub.owner.picture).fit().into(ownerPicture);
        pubTitle.setText(pub.title);
        Markwon.setMarkdown(pubContent, pub.content);
        Picasso.get().load(pub.url).fit().into(pubImage);
        pubLikes.setText(pub.createdAt + String.valueOf(pub.likes));

    }
    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    @OnClick(R.id.publication_like)
    public void onClickPublicationLike() {
        Log.d("Publication", "Click like publication");
    }

}

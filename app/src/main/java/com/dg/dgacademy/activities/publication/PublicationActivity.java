package com.dg.dgacademy.activities.publication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;

import api.fragment.PublicationInfo;
import api.fragment.UserInfo;
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

        DgApplication.requestPublication(getIntent().getExtras().getString("ID"));

    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPublicationRequest(PublicationInfo pub) {
        UserInfo user = pub.owner().fragments().userInfo();
        ownerName.setText(user.username());
        ownerBio.setText(user.bio());
        Picasso.get().load(user.picture()).fit().into(ownerPicture);
        pubTitle.setText(pub.title());
        Markwon.setMarkdown(pubContent, pub.content());
        Picasso.get().load(pub.image()).fit().into(pubImage);
        String createdAt = new SimpleDateFormat("MMMM dd, yyyy").format(pub.createdAt());
        pubLikes.setText(createdAt + "  |  LIKES ("+ String.valueOf(pub._publicationFanMeta().count() + ")"));
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

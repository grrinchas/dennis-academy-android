package com.dg.dgacademy.activities.publication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

import api.AdminQuery;
import api.LikePublicationMutation;
import api.UnlikePublicationMutation;
import api.fragment.PublicationInfo;
import api.fragment.UserInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.noties.markwon.Markwon;

public class PublicationActivity extends AppCompatActivity {

    @BindView(R.id.publication_owner_name)
    TextView ownerName;
    @BindView(R.id.publication_owner_bio)
    TextView ownerBio;
    @BindView(R.id.publication_owner_picture)
    ImageView ownerPicture;
    @BindView(R.id.publication_image)
    ImageView pubImage;
    @BindView(R.id.publication_title)
    TextView pubTitle;
    @BindView(R.id.publication_content)
    TextView pubContent;
    @BindView(R.id.publication_created_at_and_likes)
    TextView pubLikes;
    @BindView(R.id.publication_like)
    ImageView publicationLike;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private String pubId;
    private String ownerId;
    private boolean like;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        pubId = getIntent().getExtras().getString("ID");
        DgApplication.requestPublication(pubId, ApolloResponseFetchers.CACHE_FIRST);
        DgApplication.requestAdmin(ApolloResponseFetchers.CACHE_FIRST);

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

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPublicationRequest(PublicationInfo pub) {
        progressBar.setVisibility(ProgressBar.GONE);
        UserInfo user = pub.owner().fragments().userInfo();
        ownerName.setText(user.username());
        ownerBio.setText(user.bio());
        Picasso.get().load(user.picture()).fit().into(ownerPicture);
        pubTitle.setText(pub.title());
        Markwon.setMarkdown(pubContent, pub.content());
        Picasso.get().load(pub.image()).fit().into(pubImage);
        String createdAt = new SimpleDateFormat("MMMM dd, yyyy").format(pub.createdAt());
        pubLikes.setText(createdAt + "  |  LIKES (" + String.valueOf(pub._publicationFanMeta().count() + ")"));
        ownerId = user.id();

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onAdminRequest(AdminQuery.User user) {
        if (user.likedPublications().stream().map(p -> p.fragments().publicationInfo().id()).collect(Collectors.toList()).contains(pubId)) {
            like = true;
            publicationLike.setImageResource(R.drawable.ic_favorite_black_36dp);
        } else {
            like = false;
            publicationLike.setImageResource(R.drawable.ic_favorite_border_black_36dp);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLikePublication(LikePublicationMutation.AddToUserOnLikedPublication u) {
        like = true;
        publicationLike.setImageResource(R.drawable.ic_favorite_black_36dp);
        Toast.makeText(this, R.string.like, Toast.LENGTH_LONG).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnLikePublication(UnlikePublicationMutation.RemoveFromUserOnLikedPublication u) {
        like = false;
        publicationLike.setImageResource(R.drawable.ic_favorite_border_black_36dp);
        Toast.makeText(this, R.string.unlike, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.publication_like)
    public void onClickPublicationLike() {
        if (like)
            DgApplication.unlikePublication(pubId, ownerId);
        else
            DgApplication.likePublication(pubId, ownerId);
    }

}

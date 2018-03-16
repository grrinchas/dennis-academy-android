package com.dg.dgacademy.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;


import api.AdminQuery;
import api.fragment.UserInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.picture) ImageView picture;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.username) TextView username;

    private String id;
    private String bio;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DgApplication.requestAdmin(ApolloResponseFetchers.CACHE_FIRST);

    }

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    @OnClick(R.id.bio)
    public void onClickBio (){
        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
        intent.putExtra("BIO", bio);
        startActivity(intent);
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
    public void onProfileRequest(AdminQuery.User user) {
        UserInfo  userInfo = user.fragments().userInfo();
        this.id = userInfo.id();
        this.bio = userInfo.bio();
        Picasso.get().load(userInfo.picture()).fit().into(picture);
        email.setText(user.email());
        username.setText(userInfo.username());
    }
}

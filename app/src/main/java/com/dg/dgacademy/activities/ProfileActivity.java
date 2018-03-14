package com.dg.dgacademy.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.model.Profile;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.picture) ImageView picture;
    @BindView(R.id.email) TextView email;
    @BindView(R.id.username) TextView username;

    private Profile profile;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DgApplication.requestProfile();

    }

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    @OnClick(R.id.bio)
    public void onClickBio (){
        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("BUNDLE", Parcels.wrap(profile.bio));
        intent.putExtras(bundle);
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
    public void onProfileRequest(Profile profile) {
        this.profile = profile;
        Picasso.get().load(profile.picture).fit().into(picture);
        email.setText(profile.email);
        username.setText(profile.username);
    }
}

package com.dg.dgacademy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.draft.AllDraftsActivity;
import com.dg.dgacademy.activities.draft.AllPrivateDraftsActivity;
import com.dg.dgacademy.activities.publication.AllPrivatePublicationsActivity;
import com.dg.dgacademy.activities.publication.AllPublicationsActivity;
import com.dg.dgacademy.model.NotificationsEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.notifications)
    ImageView notificationsIcon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        DgApplication.requestNotifications();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
    public void onNotificationsRequest(NotificationsEvent event) {
        if (event.notifications.isEmpty())
            notificationsIcon.setImageResource(R.drawable.ic_notifications_none_black_36dp);
        else
            notificationsIcon.setImageResource(R.drawable.ic_notifications_active_black_36dp);
    }

    @OnClick(R.id.notifications)
    public void onClickNotifications(){
        startActivity(new Intent(this, NotificationsActivity.class));
    }

    @OnClick(R.id.menu_publications)
    public void onClickMenuPublications() {
        startActivity(new Intent(this, AllPublicationsActivity.class));
    }

    @OnClick(R.id.menu_drafts)
    public void onClickMenuDrafts() {
        startActivity(new Intent(this, AllDraftsActivity.class));
    }


    @OnClick(R.id.menu_private_drafts)
    public void onClickPrivateDrafts() {
        startActivity(new Intent(this, AllPrivateDraftsActivity.class));
    }

    @OnClick(R.id.menu_private_publications)
    public void onClickPrivatePublications() {
        startActivity(new Intent(this, AllPrivatePublicationsActivity.class));
    }

    @OnClick(R.id.menu_dashboard)
    public void onClickMenuDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
    }

    @OnClick(R.id.menu_profile)
    public void onClickMenuProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
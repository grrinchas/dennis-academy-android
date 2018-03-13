package com.dg.dgacademy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dg.dgacademy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
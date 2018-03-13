package com.dg.dgacademy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dg.dgacademy.Model.Publication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.total_likes) TextView totalLikes;
    @BindView(R.id.total_your_likes) TextView totalYourLikes;
    @BindView(R.id.total_publications) TextView totalPublications;
    @BindView(R.id.total_drafts) TextView totalDrafts;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        DgApplication.requestDrafts();
        DgApplication.requestPublications();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
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
    public void onDraftsRequest(List<DgApplication.DraftInfo> drafts) {
        totalDrafts.setText(String.valueOf(drafts.size()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onPublicationsRequest(List<Publication> publications) {
        totalPublications.setText(String.valueOf(publications.size()));
    }
}

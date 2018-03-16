package com.dg.dgacademy.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import api.AdminQuery;
import api.UpdateUserMutation;
import api.fragment.UserInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.about) EditText about;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        about.setText(getIntent().getExtras().getString("BIO"));
    }

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }


    @OnClick(R.id.save)
    public void onClickSave (){
        DgApplication.updateUser(about.getText().toString());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateUser(UpdateUserMutation.UpdateUser u) {
        Toast.makeText(this, R.string.profile_update, Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, ProfileActivity.class));
    }
}

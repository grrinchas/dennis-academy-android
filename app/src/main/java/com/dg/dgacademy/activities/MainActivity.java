package com.dg.dgacademy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.draft.AllPrivateDraftsActivity;
import com.dg.dgacademy.activities.draft.DraftSettingsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import api.AdminQuery;

public class MainActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(DgApplication.isLoggedIn()){
            startActivity(new Intent(this, MenuActivity.class));
        }
        else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

}

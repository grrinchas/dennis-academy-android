package com.dg.dgacademy.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.draft.AllPrivateDraftsActivity;
import com.dg.dgacademy.activities.draft.DraftSettingsActivity;

public class MainActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_row_notification);
        startActivity(new Intent(this, AllPrivateDraftsActivity.class));

    }
}

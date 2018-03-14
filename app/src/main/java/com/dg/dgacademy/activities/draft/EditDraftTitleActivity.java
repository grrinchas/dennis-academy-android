package com.dg.dgacademy.activities.draft;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;
import com.dg.dgacademy.model.Draft;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditDraftTitleActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.draft_title)
    EditText draftTitle;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_draft_title);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Draft draft = Parcels.unwrap(getIntent().getExtras().getParcelable("BUNDLE"));
        draftTitle.setText(draft.title);
    }

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    @OnClick(R.id.save)
    public void onClickSave (){

    }
}

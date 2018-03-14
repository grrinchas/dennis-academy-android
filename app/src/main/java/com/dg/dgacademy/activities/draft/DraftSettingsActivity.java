package com.dg.dgacademy.activities.draft;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Switch;

import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;
import com.dg.dgacademy.model.Draft;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class DraftSettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Draft draft;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        draft = Parcels.unwrap(getIntent().getExtras().getParcelable("BUNDLE"));
    }

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    @OnClick(R.id.draft_edit_title)
    public void onClickEditDraftTitle() {
        Intent intent = new Intent(getApplicationContext(), EditDraftTitleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("BUNDLE", Parcels.wrap(draft));
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @OnClick(R.id.draft_edit_markdown)
    public void onClickEditMarkdown() {
        Intent intent = new Intent(getApplicationContext(), EditMarkdownActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("BUNDLE", Parcels.wrap(draft));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.draft_publish)
    public void onClickDraftPublish() {
        Log.d("Draft", "Click publish");
    }

    @OnClick(R.id.draft_duplicate)
    public void onClickDraftDuplicate() {
        Log.d("Draft", "Click duplicate");
    }

    @OnClick(R.id.draft_delete)
    public void onClickDraftDelete() {
        Log.d("Draft", "Click delete");
    }

    @OnCheckedChanged(R.id.draft_make_public_switch)
    public void onSwitchPublic(Switch s) {
        Log.d("Draft", String.valueOf(s.isChecked()));
    }
}

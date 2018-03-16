package com.dg.dgacademy.activities.draft;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;

import org.parceler.Parcels;

import api.fragment.DraftInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditMarkdownActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.markdown)
    EditText markdown;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_markdown);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DraftInfo draft = Parcels.unwrap(getIntent().getExtras().getParcelable("BUNDLE"));
        markdown.setText(draft.content());
    }

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    @OnClick(R.id.save)
    public void onClickSave (){

    }
}

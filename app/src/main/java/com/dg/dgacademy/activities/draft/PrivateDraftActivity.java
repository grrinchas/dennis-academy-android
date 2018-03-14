package com.dg.dgacademy.activities.draft;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;
import com.dg.dgacademy.model.Draft;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.noties.markwon.Markwon;

public class PrivateDraftActivity extends AppCompatActivity {

    @BindView(R.id.draft_created_at_and_likes) TextView draftLikes;
    @BindView(R.id.draft_title) TextView draftTitle;
    @BindView(R.id.draft_content) TextView draftContent;

    @BindView(R.id.toolbar) Toolbar toolbar;
    private Draft draft;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_draft);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        draft = Parcels.unwrap(getIntent().getExtras().getParcelable("BUNDLE"));
        draftTitle.setText(draft.title);
        Markwon.setMarkdown(draftContent, draft.content);
        draftLikes.setText(draft.createdAt + String.valueOf(draft.likes));

    }

    @OnClick(R.id.toolbar_back)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    @OnClick(R.id.draft_more)
    public void onClickDraftMore() {
        Intent intent = new Intent(getApplicationContext(), DraftSettingsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("BUNDLE", Parcels.wrap(draft));
        intent.putExtras(bundle);
        startActivity(intent);
    }

}

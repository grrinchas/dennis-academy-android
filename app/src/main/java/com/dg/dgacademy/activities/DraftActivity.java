package com.dg.dgacademy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.dg.dgacademy.R;
import com.dg.dgacademy.model.Draft;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.noties.markwon.Markwon;


public class DraftActivity extends AppCompatActivity {

    @BindView(R.id.draft_owner_name) TextView ownerName;
    @BindView(R.id.draft_owner_bio) TextView ownerBio;
    @BindView(R.id.draft_owner_picture) ImageView ownerPicture;
    @BindView(R.id.draft_title) TextView draftTitle;
    @BindView(R.id.draft_content) TextView draftContent;
    @BindView(R.id.draft_created_at_and_likes) TextView draftLikes;

    @BindView(R.id.toolbar) Toolbar toolbar;


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Draft draft = Parcels.unwrap(getIntent().getExtras().getParcelable("BUNDLE"));
        ownerName.setText(draft.owner.name);
        ownerBio.setText(draft.owner.bio);
        Picasso.get().load(draft.owner.picture).fit().into(ownerPicture);
        draftTitle.setText(draft.title);
        Markwon.setMarkdown(draftContent, draft.content);
        draftLikes.setText(draft.createdAt + String.valueOf(draft.likes));
    }

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    @OnClick(R.id.draft_like)
    public void onClickDraftLike() {
        Log.d("Draft", "Click like draft");
    }

    @OnClick(R.id.draft_duplicate)
    public void onClickDraftDuplicate() {
        Log.d("Draft", "Click like draft");
    }

}

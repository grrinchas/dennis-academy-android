package com.dg.dgacademy.activities.draft;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;

import api.CreateDraftMutation;
import api.fragment.DraftInfo;
import api.fragment.UserInfo;
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
    private DraftInfo draft;


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DgApplication.requestPublicDraft(getIntent().getExtras().getString("ID"), ApolloResponseFetchers.CACHE_FIRST);

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
    public void onDraftRequest(DraftInfo draft) {
        this.draft = draft;
        UserInfo owner = draft.owner().fragments().userInfo();
        ownerName.setText(owner.username());
        ownerBio.setText(owner.bio());
        Picasso.get().load(owner.picture()).fit().into(ownerPicture);
        draftTitle.setText(draft.title());
        Markwon.setMarkdown(draftContent, draft.content());
        String createdAt = new SimpleDateFormat("MMMM dd, yyyy").format(draft.createdAt());
        draftLikes.setText(createdAt + "  |  LIKES ("+ String.valueOf(draft._draftFanMeta().count() + ")"));
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDuplicateDraft(CreateDraftMutation.CreateDraft d) {
        Toast.makeText(this, getString(R.string.draft_duplicated), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, PrivateDraftActivity.class);
        intent.putExtra("ID", d.fragments().draftInfo().id());
        intent.putExtra("FETCH", "NETWORK");
        startActivity(intent);
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
        DgApplication.duplicateDraft(this.draft);
    }

}

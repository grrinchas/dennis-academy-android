package com.dg.dgacademy.activities.draft;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import api.AdminQuery;
import api.UpdateDraftMutation;
import api.fragment.DraftInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditDraftTitleActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.draft_title)
    EditText draftTitle;

    private DraftInfo draft;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_draft_title);
        ButterKnife.bind(this);

        Log.d("Call me once", "Call me oncel");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DgApplication.requestPrivateDraft(getIntent().getExtras().getString("ID"), ApolloResponseFetchers.CACHE_FIRST);
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
    public void onDraftRequest(DraftInfo d) {
        draft = d;
        draftTitle.setText(d.title());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDraftUpdate(UpdateDraftMutation.UpdateDraft d) {
        Toast.makeText(this, R.string.draft_updated, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, PrivateDraftActivity.class);
        intent.putExtra("ID", d.fragments().draftInfo().id());
        intent.putExtra("FETCH", "NETWORK");
        startActivity(intent);
    }

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    @OnClick(R.id.save)
    public void onClickSave (){
        UpdateDraftMutation updateDraft = UpdateDraftMutation.builder()
                .content(draft.content())
                .id(draft.id())
                .title(draftTitle.getText().toString())
                .visibility(draft.visibility())
                .build();
        DgApplication.updateDraft(updateDraft);
    }
}

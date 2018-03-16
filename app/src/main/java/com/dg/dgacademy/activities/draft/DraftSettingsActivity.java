package com.dg.dgacademy.activities.draft;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import java.io.IOException;
import java.text.SimpleDateFormat;

import api.CreateDraftMutation;
import api.DeleteDraftMutation;
import api.UpdateDraftMutation;
import api.fragment.DraftInfo;
import api.fragment.UserInfo;
import api.type.Visibility;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import ru.noties.markwon.Markwon;

public class DraftSettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private DraftInfo draft;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_settings);
        ButterKnife.bind(this);

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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onDraftRequest(DraftInfo draft) {
        this.draft = draft;
    }


    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    @OnClick(R.id.draft_edit_title)
    public void onClickEditDraftTitle() {
        Intent intent = new Intent(getApplicationContext(), EditDraftTitleActivity.class);
        intent.putExtra("ID", draft.id());
        startActivity(intent);
    }


    private static final int PICK_IMAGE = 0;

    @OnClick(R.id.draft_new_publication)
    public void onClickDraftPublish() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
        Log.d("Draft", "Click create new publication");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri photoUri = data.getData();
            if (photoUri != null)
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    Log.d("Success", bitmap.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDraftUpdate(UpdateDraftMutation.UpdateDraft d) {
        if (d.fragments().draftInfo().visibility() == Visibility.PUBLIC)
            Toast.makeText(this, R.string.draft_public, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, R.string.draft_private, Toast.LENGTH_LONG).show();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDuplicateDraft(CreateDraftMutation.CreateDraft d) {
        Toast.makeText(this, getString(R.string.draft_duplicated), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, PrivateDraftActivity.class);
        intent.putExtra("ID", d.fragments().draftInfo().id());
        intent.putExtra("FETCH", "NETWORK");
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteDraft(DeleteDraftMutation.DeleteDraft d) {
        Toast.makeText(this, getString(R.string.draft_deleted), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, AllPrivateDraftsActivity.class);
        intent.putExtra("FETCH", "NETWORK");
        startActivity(intent);
    }


    @OnClick(R.id.update_publication)
    public void onClickUpdatePublication() {
        Log.d("Draft", "Click update publication");
    }

    @OnClick(R.id.draft_duplicate)
    public void onClickDraftDuplicate() {
        DgApplication.duplicateDraft(this.draft);
    }

    @OnClick(R.id.draft_delete)
    public void onClickDraftDelete() {
        DgApplication.deleteDraft(this.draft.id());
    }

    @OnCheckedChanged(R.id.draft_make_public_switch)
    public void onSwitchPublic(Switch s) {
        Visibility visibility;
        if (s.isChecked())
            visibility = Visibility.PUBLIC;
        else
            visibility = Visibility.PRIVATE;
        UpdateDraftMutation updateDraft = UpdateDraftMutation.builder()
                .content(draft.content())
                .id(draft.id())
                .title(draft.title())
                .visibility(visibility)
                .build();
        DgApplication.updateDraft(updateDraft);
    }
}

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

import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;
import com.dg.dgacademy.model.Draft;

import org.parceler.Parcels;

import java.io.IOException;

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
        }
        else if (resultCode == Activity.RESULT_CANCELED)
        {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.update_publication)
    public void onClickUpdatePublication() {
        Log.d("Draft", "Click update publication");
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

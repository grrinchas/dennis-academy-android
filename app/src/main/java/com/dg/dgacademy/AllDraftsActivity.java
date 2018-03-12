package com.dg.dgacademy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AllDraftsActivity extends AppCompatActivity {

    private DraftsAdapter adapter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_drafts);
        initToolbar();
        initRecyclerView();

        adapter.drafts = prepareData();
        adapter.notifyDataSetChanged();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findViewById(R.id.toolbar_menu).setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));
    }
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.drafts_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DraftsAdapter(Collections.emptyList());
        recyclerView.setAdapter(adapter);
    }

    private List<DraftInfo> prepareData() {
        List<DraftInfo> drafts = new ArrayList<>();

        DraftInfo info1 = new DraftInfo();
        info1.title = "Lorem Ipsum 2";
        info1.createdAt = "Jan 23, 2019";
        info1.ownerName = "admin1";
        info1.preview = "This is some previou aosid a pbapos pas apsdfo ihapsuidhfaspdfaisdh apsdifh asdf";
        info1.ownerPicture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
        info1.likes = 20;

        DraftInfo info2 = new DraftInfo();
        info2.title = "Lorem Ipsum 2";
        info2.createdAt = "Jan 23, 2019";
        info2.ownerName = "admin1";
        info2.preview = "This is some previou aosid a pbapos pas apsdfo ihapsuidhfaspdfaisdh apsdifh asdf";
        info2.ownerPicture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
        info2.likes = 20;


        drafts.add(info1);
        drafts.add(info2);

        return drafts;
    }

    private class DraftsHolder extends RecyclerView.ViewHolder {

        TextView title, preview, ownerName, createdAt, likesCount;
        ImageView ownerPicture;

        DraftsHolder(View view) {
            super(view);
            ownerPicture = view.findViewById(R.id.drafts_owner_picture);
            title = view.findViewById(R.id.drafts_title);
            ownerName = view.findViewById(R.id.drafts_owner_name);
            preview = view.findViewById(R.id.drafts_preview);
            createdAt = view.findViewById(R.id.drafts_created_at);
            likesCount = view.findViewById(R.id.drafts_likes_count);
        }
    }

    private class DraftsAdapter extends RecyclerView.Adapter<DraftsHolder> {

        List<DraftInfo> drafts;

        DraftsAdapter(List<DraftInfo> drafts) {
            this.drafts = drafts;
        }

        @Override
        public DraftsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_drafts, parent, false);
            return new DraftsHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DraftsHolder holder, int position) {
            DraftInfo draft = drafts.get(position);
            setCreatedAt(holder, draft);
            setOwner(holder, draft);
            setDraft(holder, draft);
            setLikes(holder, draft);
        }

        void setCreatedAt(DraftsHolder holder, DraftInfo pub) {
            holder.createdAt.setText(pub.createdAt);
        }

        void setOwner(DraftsHolder holder, DraftInfo pub) {
            Picasso.get().load(pub.ownerPicture).fit().into(holder.ownerPicture);
            holder.ownerName.setText(pub.ownerName);
            holder.ownerName.setOnClickListener(v -> Log.d("Drafts", "Click on draft owner"));
        }

        void setDraft(DraftsHolder holder, DraftInfo pub) {
            holder.title.setText(pub.title);
            holder.title.setOnClickListener(v -> Log.d("Drafts", "Click on draft title"));
            holder.preview.setText(pub.preview);
            holder.preview.setOnClickListener(v -> Log.d("Drafts", "Click on draft preview"));
        }

        void setLikes(DraftsHolder holder, DraftInfo pub) {
            holder.likesCount.setText("LIKES (" + String.valueOf(pub.likes) + ")");
        }

        @Override
        public int getItemCount() {
            return drafts.size();
        }
    }

    private class DraftInfo {

        String title;
        String preview;
        String createdAt;
        String ownerName;
        String ownerPicture;
        int likes;

    }
}

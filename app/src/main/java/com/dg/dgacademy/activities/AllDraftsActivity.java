package com.dg.dgacademy.activities;

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

import com.dg.dgacademy.R;
import com.dg.dgacademy.model.Draft;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AllDraftsActivity extends AppCompatActivity {

    private DraftsAdapter adapter;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_drafts);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DgApplication.requestPublicDrafts();
        initRecyclerView();
    }

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
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
    public void onDraftsRequest(List<Draft> drafts) {
        adapter.drafts = drafts;
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.drafts_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DraftsAdapter(Collections.emptyList());
        recyclerView.setAdapter(adapter);
    }


    class DraftsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.drafts_title) TextView title;
        @BindView(R.id.drafts_owner_name) TextView ownerName;
        @BindView(R.id.drafts_created_at) TextView createdAt;
        @BindView(R.id.drafts_likes_count) TextView likesCount;
        @BindView(R.id.drafts_preview) TextView preview;
        @BindView(R.id.drafts_owner_picture) ImageView ownerPicture;


        DraftsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private class DraftsAdapter extends RecyclerView.Adapter<DraftsHolder> {

        List<Draft> drafts;

        DraftsAdapter(List<Draft> drafts) {
            this.drafts = drafts;
        }

        @Override
        public DraftsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_drafts, parent, false);
            return new DraftsHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DraftsHolder holder, int position) {
            Draft draft = drafts.get(position);
            setCreatedAt(holder, draft);
            setOwner(holder, draft);
            setDraft(holder, draft);
            setLikes(holder, draft);
        }

        void setCreatedAt(DraftsHolder holder, Draft pub) {
            holder.createdAt.setText(pub.createdAt);
        }

        void setOwner(DraftsHolder holder, Draft pub) {
            Picasso.get().load(pub.owner.picture).fit().into(holder.ownerPicture);
            holder.ownerName.setText(pub.owner.name);
            holder.ownerName.setOnClickListener(v -> Log.d("Drafts", "Click on draft owner"));
        }

        void setDraft(DraftsHolder holder, Draft pub) {
            holder.title.setText(pub.title);
            holder.preview.setText(pub.content);
            holder.preview.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), DraftActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("BUNDLE", Parcels.wrap(pub));
                intent.putExtras(bundle);
                startActivity(intent);
            });
        }

        void setLikes(DraftsHolder holder, Draft pub) {
            holder.likesCount.setText("LIKES (" + String.valueOf(pub.likes) + ")");
        }

        @Override
        public int getItemCount() {
            return drafts.size();
        }
    }


}

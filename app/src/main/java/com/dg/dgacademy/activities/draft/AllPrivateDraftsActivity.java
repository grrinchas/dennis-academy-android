package com.dg.dgacademy.activities.draft;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;
import com.dg.dgacademy.model.DraftsEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

import api.fragment.DraftInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllPrivateDraftsActivity extends AppCompatActivity {

    private DraftsAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_private_drafts);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getIntent().getExtras() != null)
            DgApplication.requestPrivateDrafts(getIntent().getExtras().get("FETCH") == null ? ApolloResponseFetchers.CACHE_FIRST : ApolloResponseFetchers.NETWORK_ONLY);
        else
            DgApplication.requestPrivateDrafts(ApolloResponseFetchers.CACHE_FIRST);
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
    public void onDraftsRequest(DraftsEvent event) {
        adapter.drafts = event.drafts;
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

        @BindView(R.id.drafts_title)
        TextView title;
        @BindView(R.id.draft)
        View draft;

        DraftsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    private class DraftsAdapter extends RecyclerView.Adapter<DraftsHolder> {

        List<DraftInfo> drafts;

        DraftsAdapter(List<DraftInfo> drafts) {
            this.drafts = drafts;
        }

        @Override
        public DraftsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_draft, parent, false);
            return new DraftsHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DraftsHolder holder, int position) {
            DraftInfo draft = drafts.get(position);
            setDraft(holder, draft);
        }


        void setDraft(DraftsHolder holder, DraftInfo pub) {
            holder.title.setText(pub.title());
            holder.draft.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), PrivateDraftActivity.class);
                intent.putExtra("ID", pub.id());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return drafts.size();
        }
    }

}

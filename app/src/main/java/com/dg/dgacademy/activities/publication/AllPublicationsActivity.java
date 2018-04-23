package com.dg.dgacademy.activities.publication;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;
import com.dg.dgacademy.model.PublicationsEvent;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import api.fragment.ProfileInfo;
import api.fragment.PublicationInfo;
import api.fragment.UserInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AllPublicationsActivity extends AppCompatActivity {

    private PublicationsAdapter adapter;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_publications);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DgApplication.requestPublicPublications(ApolloResponseFetchers.CACHE_FIRST);

        initRecyclerView();
    }

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.publications_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PublicationsAdapter(Collections.emptyList());
        recyclerView.setAdapter(adapter);
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
    public void onPublicationsRequest(PublicationsEvent event) {
        adapter.publications = event.publications;
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(ProgressBar.GONE);
    }

   class PublicationsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.publications_title) TextView title;
        @BindView(R.id.publications_owner_name) TextView ownerName;
        @BindView(R.id.publications_created_at) TextView createdAt;
        @BindView(R.id.publications_likes_count) TextView likesCount;
        @BindView(R.id.publications_image) ImageView image;
        @BindView(R.id.publications_owner_picture) ImageView ownerPicture;

        PublicationsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private class PublicationsAdapter extends RecyclerView.Adapter<PublicationsHolder> {

        List<PublicationInfo> publications;

        PublicationsAdapter(List<PublicationInfo> publications) {
            this.publications = publications;
        }

        @Override
        public PublicationsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_publications, parent, false);
            return new PublicationsHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PublicationsHolder holder, int position) {
            PublicationInfo pub = publications.get(position);
            setCreatedAt(holder, pub);
            setOwner(holder, pub);
            setPublication(holder, pub);
            setLikes(holder, pub);
        }

        void setCreatedAt(PublicationsHolder holder, PublicationInfo pub) {
            holder.createdAt.setText(new SimpleDateFormat("MMMM dd, yyyy").format(pub.createdAt()));
        }

        void setOwner(PublicationsHolder holder, PublicationInfo pub) {
            UserInfo info =pub.owner().fragments().userInfo();
            Picasso.get().load(info.picture()).fit().into(holder.ownerPicture);
            holder.ownerName.setText(info.username());
            holder.ownerName.setOnClickListener(v -> Log.d("Publications", "Click on draft owner"));
        }

        void setPublication(PublicationsHolder holder, PublicationInfo pub) {
            Picasso.get().load(pub.image()).into(holder.image);
            holder.title.setText(pub.title());

            holder.image.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), PublicationActivity.class);
                intent.putExtra("ID", pub.id());
                startActivity(intent);
            });
        }

        void setLikes(PublicationsHolder holder, PublicationInfo pub) {
            holder.likesCount.setText("LIKES (" + String.valueOf(pub._publicationFanMeta().count()) + ")");
        }

        @Override
        public int getItemCount() {
            return publications.size();
        }
    }



}

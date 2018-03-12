package com.dg.dgacademy;

import android.app.Activity;
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

public class PublicationsActivity extends AppCompatActivity {

    private PublicationsAdapter adapter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publications);
        initToolbar();
        initRecyclerView();

        adapter.publications = prepareData();
        adapter.notifyDataSetChanged();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findViewById(R.id.toolbar_menu).setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));
    }
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.publications_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PublicationsAdapter(Collections.emptyList());
        recyclerView.setAdapter(adapter);
    }

    private List<PublicationInfo> prepareData() {
        List<PublicationInfo> publications = new ArrayList<>();

        PublicationInfo info1 = new PublicationInfo();
        info1.title = "Lorem Ipsum 2";
        info1.createdAt = "Jan 23, 2019";
        info1.ownerName = "admin1";
        info1.url = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
        info1.ownerPicture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
        info1.likes = 20;

        PublicationInfo info2 = new PublicationInfo();
        info2.title = "Lorem Ipsum 2";
        info2.createdAt = "Jan 23, 2019";
        info2.ownerName = "admin1";
        info2.url = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
        info2.ownerPicture = "https://s10.postimg.org/qvvi5ot7t/healthy-fruits-morning-kitchen.jpg";
        info2.likes = 20;


        publications.add(info1);
        publications.add(info2);

        return publications;
    }



    private class PublicationsHolder extends RecyclerView.ViewHolder {

        TextView title, ownerName, createdAt, likesCount;
        ImageView image, ownerPicture, likes;

        PublicationsHolder(View view) {
            super(view);
            image = view.findViewById(R.id.publications_image);
            ownerPicture = view.findViewById(R.id.publications_owner_picture);
            likes = view.findViewById(R.id.publications_likes);
            title = view.findViewById(R.id.publications_title);
            ownerName = view.findViewById(R.id.publications_owner_name);
            createdAt = view.findViewById(R.id.publications_created_at);
            likesCount = view.findViewById(R.id.publications_likes_count);
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
            holder.createdAt.setText(pub.createdAt);
        }

        void setOwner(PublicationsHolder holder, PublicationInfo pub) {
            Picasso.get().load(pub.ownerPicture).fit().into(holder.ownerPicture);
            holder.ownerName.setText(pub.ownerName);
        }

        void setPublication(PublicationsHolder holder, PublicationInfo pub) {
            Picasso.get().load(pub.url).fit().into(holder.image);
            holder.image.setOnClickListener(v -> Log.d("Publications", "Click on publication image"));
            holder.title.setText(pub.title);
        }

        void setLikes(PublicationsHolder holder, PublicationInfo pub) {
            holder.likes.setOnClickListener( v -> Log.d("Publications", "Click like publication"));
            holder.likesCount.setText(String.valueOf(pub.likes));
        }

        @Override
        public int getItemCount() {
            return publications.size();
        }
    }

    private class PublicationInfo {

        String title;
        String createdAt;
        String ownerName;
        String url;
        String ownerPicture;
        int likes;

    }

}

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

import com.bumptech.glide.Glide;
import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.model.Notification;
import com.dg.dgacademy.model.NotificationsEvent;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class NotificationsActivity extends AppCompatActivity{

    private ArticleAdapter draftsAdapter;
    private ArticleAdapter publicationsAdapter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.publications_recycler_view) RecyclerView publicationsRecyclerView;
    @BindView(R.id.drafts_recycler_view) RecyclerView draftsRecyclerView;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DgApplication.requestNotifications();
        initRecyclerViews();
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
    public void onNotificationsRequest(NotificationsEvent event) {
        List<Notification> drafts = new ArrayList<>();
        List<Notification> publications = new ArrayList<>();
        for(Notification n: event.notifications) {
            if(n.type == Notification.Type.LIKE_DRAFT || n.type == Notification.Type.UNLIKE_DRAFT)
                drafts.add(n);
            else
                publications.add(n);
        }

        publicationsAdapter.notifications = publications;
        publicationsAdapter.notifyDataSetChanged();

        draftsAdapter.notifications = drafts;
        draftsAdapter.notifyDataSetChanged();

    }
    private void initRecyclerViews() {
        draftsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        draftsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        draftsAdapter = new ArticleAdapter(Collections.emptyList());
        draftsRecyclerView.setAdapter(draftsAdapter);

        publicationsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        publicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        publicationsAdapter = new ArticleAdapter(Collections.emptyList());
        publicationsRecyclerView.setAdapter(publicationsAdapter);
    }

    class ArticleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.note_username) TextView username;
        @BindView(R.id.note_picture) ImageView picture;
        @BindView(R.id.note_article) TextView article;
        @BindView(R.id.note_created_at) TextView createdAt;
        @BindView(R.id.note_delete) ImageView delete;
        @BindView(R.id.note_action_row) View actionRow;
        @BindView(R.id.note_action_text) TextView actionText;
        @BindView(R.id.note_action_image) ImageView actionImage;


        ArticleHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private class ArticleAdapter extends RecyclerView.Adapter<ArticleHolder> {

        List<Notification> notifications;

        ArticleAdapter(List<Notification> notifications) {
            this.notifications = notifications;
        }

        @Override
        public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_notification, parent, false);
            return new ArticleHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ArticleHolder holder, int position) {
            Notification note = notifications.get(position);
            holder.createdAt.setText(note.createdAt);
            Picasso.get().load(note.sender.picture).fit().into(holder.picture);
            holder.username.setText(note.sender.name);
            holder.picture.setOnClickListener(v -> Log.d("Note", "Click display sender profile"));
            holder.delete.setOnClickListener(v -> Log.d("Note","Click delete notification"));


            switch (note.type) {
                case LIKE_DRAFT:
                    holder.actionImage.setImageResource(R.drawable.ic_favorite_black_36dp);
                    holder.actionText.setText(R.string.your_draft);
                    if(note.isPresent) {
                        holder.actionRow.setOnClickListener(v -> Log.d("Note", "Click display article"));
                        holder.article.setText(note.message);
                        holder.article.setTextColor(getResources().getColor(R.color.colorNotification));
                    }
                    else {
                        holder.article.setText(R.string.draft_deleted);
                        holder.article.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    return;
                case UNLIKE_DRAFT:
                    holder.actionImage.setImageResource(R.drawable.ic_favorite_border_black_36dp);
                    holder.actionText.setText(R.string.your_draft);
                    if(note.isPresent) {
                        holder.actionRow.setOnClickListener(v -> Log.d("Note", "Click display article"));
                        holder.article.setText(note.message);
                        holder.article.setTextColor(getResources().getColor(R.color.colorNotification));
                    }
                    else {
                        holder.article.setText(R.string.draft_deleted);
                        holder.article.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    return;
                case LIKE_PUBLICATION:
                    holder.actionImage.setImageResource(R.drawable.ic_favorite_black_36dp);
                    holder.actionText.setText(R.string.your_publication);
                    if(note.isPresent) {
                        holder.actionRow.setOnClickListener(v -> Log.d("Note", "Click display article"));
                        holder.article.setText(note.message);
                        holder.article.setTextColor(getResources().getColor(R.color.colorNotification));
                    }
                    else {
                        holder.article.setText(R.string.publication_deleted);
                        holder.article.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    return;
                case UNLIKE_PUBLICATION:
                    holder.actionImage.setImageResource(R.drawable.ic_favorite_border_black_36dp);
                    holder.actionText.setText(R.string.your_publication);
                    if(note.isPresent) {
                        holder.actionRow.setOnClickListener(v -> Log.d("Note", "Click display article"));
                        holder.article.setText(note.message);
                        holder.article.setTextColor(getResources().getColor(R.color.colorNotification));
                    }
                    else {
                        holder.article.setText(R.string.publication_deleted);
                        holder.article.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
            }

        }

        @Override
        public int getItemCount() {
            return notifications.size();
        }
    }

}

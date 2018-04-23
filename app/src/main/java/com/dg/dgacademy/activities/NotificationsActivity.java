package com.dg.dgacademy.activities;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
import android.widget.Toast;

import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.draft.DraftActivity;
import com.dg.dgacademy.activities.draft.PrivateDraftActivity;
import com.dg.dgacademy.activities.publication.PrivatePublicationActivity;
import com.dg.dgacademy.activities.publication.PublicationActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import api.AdminQuery;
import api.DeleteNotificationMutation;
import api.fragment.DraftInfo;
import api.fragment.NotificationInfo;
import api.fragment.PublicationInfo;
import api.fragment.UserInfo;
import api.type.NotificationType;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NotificationsActivity extends AppCompatActivity {

    private ArticleAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DgApplication.requestAdmin(ApolloResponseFetchers.NETWORK_FIRST);

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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteNotification(DeleteNotificationMutation.DeleteNotification note) {
        Toast.makeText(this, getString(R.string.deleted_notifications), Toast.LENGTH_LONG).show();
        DgApplication.requestAdmin(ApolloResponseFetchers.NETWORK_ONLY);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onNotificationsRequest(AdminQuery.User user) {
        progressBar.setVisibility(ProgressBar.GONE);
        adapter.publications.clear();
        adapter.drafts.clear();
        adapter.notifications.clear();
        adapter.publications.addAll(user.publications().stream().map(p -> p.fragments().publicationInfo()).collect(Collectors.toList()));
        adapter.drafts.addAll(user.drafts().stream().map(p -> p.fragments().draftInfo()).collect(Collectors.toList()));
        adapter.notifications.addAll(
                user.receivedNotifications()
                        .stream()
                        .map(n -> n.fragments().notificationInfo())
                        .collect(Collectors.toList()));
        adapter.notifyDataSetChanged();
        EventBus.getDefault().removeAllStickyEvents();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ArticleAdapter();
        recyclerView.setAdapter(adapter);

    }

    class ArticleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.note_username)
        TextView username;
        @BindView(R.id.note_picture)
        ImageView picture;
        @BindView(R.id.note_article)
        TextView article;
        @BindView(R.id.note_created_at)
        TextView createdAt;
        @BindView(R.id.note_delete)
        ImageView delete;
        @BindView(R.id.note_action_row)
        View actionRow;
        @BindView(R.id.note_action_text)
        TextView actionText;
        @BindView(R.id.note_action_image)
        ImageView actionImage;


        ArticleHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private class ArticleAdapter extends RecyclerView.Adapter<ArticleHolder> {

        List<NotificationInfo> notifications = new ArrayList<>();
        List<DraftInfo> drafts = new ArrayList<>();
        List<PublicationInfo> publications = new ArrayList<>();


        @Override
        public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_notification, parent, false);
            return new ArticleHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ArticleHolder holder, int position) {
            NotificationInfo note = notifications.get(position);
            holder.createdAt.setText(new SimpleDateFormat("MMMM dd, yyyy").format(note.createdAt()));
            UserInfo sender = note.sender().fragments().profileInfo().fragments().userInfo();
            Picasso.get().load(sender.picture()).fit().into(holder.picture);
            holder.username.setText(sender.username());

            //TODO: Not implemented
            holder.picture.setOnClickListener(v -> Log.d("Note", "Click display sender profile"));

            holder.delete.setOnClickListener(v -> DgApplication.deleteNotification(note.id()));

            Optional<String> draftTitle = drafts.stream().filter(d -> Objects.equals(d.id(), note.message())).map(DraftInfo::title).findFirst();
            Optional<String> pubTitle = publications.stream().filter(d -> Objects.equals(d.id(), note.message())).map(PublicationInfo::title).findFirst();


            switch (note.type()) {
                case LIKED_DRAFT:
                    holder.actionImage.setImageResource(R.drawable.ic_favorite_black_36dp);
                    holder.actionText.setText(R.string.your_draft);
                    if (draftTitle.isPresent()) {
                        holder.actionRow.setOnClickListener(v -> {
                            Intent intent = new Intent(NotificationsActivity.this, PrivateDraftActivity.class);
                            intent.putExtra("ID", note.message());
                            startActivity(intent);
                        });
                        holder.article.setText(draftTitle.get());
                        holder.article.setTextColor(getResources().getColor(R.color.colorNotification));
                    } else {
                        holder.article.setText(R.string.draft_deleted);
                        holder.article.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    return;
                case UNLIKED_DRAFT:
                    holder.actionImage.setImageResource(R.drawable.ic_favorite_border_black_36dp);
                    holder.actionText.setText(R.string.your_draft);
                    if (draftTitle.isPresent()) {
                        holder.actionRow.setOnClickListener(v -> {
                            Intent intent = new Intent(NotificationsActivity.this, PrivateDraftActivity.class);
                            intent.putExtra("ID", note.message());
                            startActivity(intent);
                        });
                        holder.article.setText(draftTitle.get());
                        holder.article.setTextColor(getResources().getColor(R.color.colorNotification));
                    } else {
                        holder.article.setText(R.string.draft_deleted);
                        holder.article.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    return;
                case LIKED_PUBLICATION:
                    holder.actionImage.setImageResource(R.drawable.ic_favorite_black_36dp);
                    holder.actionText.setText(R.string.your_publication);
                    if (pubTitle.isPresent()) {
                        holder.actionRow.setOnClickListener(v -> {
                            Intent intent = new Intent(NotificationsActivity.this, PrivatePublicationActivity.class);
                            intent.putExtra("ID", note.message());
                            startActivity(intent);
                        });
                        holder.article.setText(pubTitle.get());
                        holder.article.setTextColor(getResources().getColor(R.color.colorNotification));
                    } else {
                        holder.article.setText(R.string.publication_deleted);
                        holder.article.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    return;
                case UNLIKED_PUBLICATION:
                    holder.actionImage.setImageResource(R.drawable.ic_favorite_border_black_36dp);
                    holder.actionText.setText(R.string.your_publication);
                    if (pubTitle.isPresent()) {
                        holder.actionRow.setOnClickListener(v -> {
                            Intent intent = new Intent(NotificationsActivity.this, PrivatePublicationActivity.class);
                            intent.putExtra("ID", note.message());
                            startActivity(intent);
                        });
                        holder.article.setText(pubTitle.get());
                        holder.article.setTextColor(getResources().getColor(R.color.colorNotification));
                    } else {
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

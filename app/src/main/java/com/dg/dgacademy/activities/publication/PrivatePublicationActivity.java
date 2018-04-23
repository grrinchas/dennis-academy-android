package com.dg.dgacademy.activities.publication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.dg.dgacademy.DgApplication;
import com.dg.dgacademy.R;
import com.dg.dgacademy.activities.MenuActivity;
import com.squareup.picasso.Picasso;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;

import api.DeletePublicationMutation;
import api.fragment.PublicationInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.noties.markwon.Markwon;

public class PrivatePublicationActivity extends AppCompatActivity {

    @BindView(R.id.publication_image)
    ImageView pubImage;
    @BindView(R.id.publication_title)
    TextView pubTitle;
    @BindView(R.id.publication_content)
    TextView pubContent;
    @BindView(R.id.publication_created_at_and_likes)
    TextView pubLikes;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String id;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_publication);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        id = getIntent().getExtras().getString("ID");
        DgApplication.requestPublication(id, ApolloResponseFetchers.CACHE_FIRST);

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
    public void onPublicationRequest(PublicationInfo pub) {
        pubTitle.setText(pub.title());
        Markwon.setMarkdown(pubContent, pub.content());
        Picasso.get().load(pub.image()).fit().into(pubImage);
        String createdAt = new SimpleDateFormat("MMMM dd, yyyy").format(pub.createdAt());
        pubLikes.setText(createdAt + "  |  LIKES (" + String.valueOf(pub._publicationFanMeta().count() + ")"));
        progressBar.setVisibility(ProgressBar.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeletePublicationRequest(DeletePublicationMutation.DeletePublication d) {
        Toast.makeText(this, getString(R.string.publication_deleted), Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, AllPrivatePublicationsActivity.class));
    }

    @OnClick(R.id.toolbar_menu)
    public void onClickToolbarMenu() {
        startActivity(new Intent(this, MenuActivity.class));
    }

    @OnClick(R.id.publication_delete)
    public void onClickPublicationDelete() {
        DgApplication.deletePublication(id);
    }

}

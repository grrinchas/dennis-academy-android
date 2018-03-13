package com.dg.dgacademy.activities;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.dg.dgacademy.R;
import com.dg.dgacademy.model.Publication;
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

public class AllPrivatePublicationsActivity extends AppCompatActivity {

    private PublicationsAdapter adapter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_private_drafts);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText(R.string.publications);

        DgApplication.requestPrivatePublications();
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
    public void onPublicationsRequest(List<Publication> pubs) {
        adapter.publications = pubs;
        adapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.drafts_recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PublicationsAdapter(Collections.emptyList());
        recyclerView.setAdapter(adapter);
    }

    class PublicationsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.publications_title) TextView title;
        @BindView(R.id.publications_image) ImageView image;
        @BindView(R.id.publication) View publication;

        PublicationsHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    private class PublicationsAdapter extends RecyclerView.Adapter<PublicationsHolder> {

        List<Publication> publications;

        PublicationsAdapter(List<Publication> pubs) {
            this.publications = pubs;
        }

        @Override
        public PublicationsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_publication, parent, false);
            return new PublicationsHolder(itemView);
        }

        @Override
        public void onBindViewHolder(PublicationsHolder holder, int position) {
            Publication pub = publications.get(position);
            Picasso.get().load(pub.url).fit().into(holder.image);
            setPublication(holder, pub);
        }


        void setPublication(PublicationsHolder holder, Publication pub) {
            holder.title.setText(pub.title);
            holder.publication.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), PrivatePublicationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("BUNDLE", Parcels.wrap(pub));
                intent.putExtras(bundle);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return publications.size();
        }
    }

}

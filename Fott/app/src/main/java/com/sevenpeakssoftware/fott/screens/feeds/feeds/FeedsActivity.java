package com.sevenpeakssoftware.fott.screens.feeds.feeds;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sevenpeakssoftware.fott.R;
import com.sevenpeakssoftware.fott.database.RealmController;
import com.sevenpeakssoftware.fott.models.Article;
import com.sevenpeakssoftware.fott.models.FeedResponse;
import com.sevenpeakssoftware.fott.network.ApiClient;
import com.sevenpeakssoftware.fott.network.FeedsInterface;
import com.sevenpeakssoftware.fott.screens.feeds.baseactivity.RealmActivity;
import com.sevenpeakssoftware.fott.screens.feeds.feeddetail.FeedDetailActivity;
import com.sevenpeakssoftware.fott.utils.Util;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedsActivity extends RealmActivity
        implements NavigationView.OnNavigationItemSelectedListener, FeedsAdapter.OnItemClickListener {

    private static final String TAG = FeedsActivity.class.getSimpleName();

    private RecyclerView recycler;
    private FeedsAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);
        showProgressBar(View.VISIBLE);
        initToolbarNavDrawer();

        setupRecycler();

        if (Util.isNetworkConnectionAvailable(this)) {
            fetchFeeds();
        } else {
            setupAdapter();
        }
    }

    private void showProgressBar(int visibility) {
        findViewById(R.id.progressBar).setVisibility(visibility);
    }

    private void fetchFeeds() {
        FeedsInterface apiService = ApiClient.getBaseClient().create(FeedsInterface.class);

        Call<FeedResponse> call = apiService.getFeeds();

        call.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                if (response.isSuccessful()) {
                    List<Article> list = ((FeedResponse) response.body()).getArticles();
                    Log.i(TAG, "RETROFIT SUCCESS :  " + list.size());
                    saveToDatabase(list);
                    setupAdapter();
                } else {
                    Log.e(TAG, "RETROFIT NOT SUCCESSFUL.");
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                Log.e(TAG, "RETROFIT FAILED " + t.toString());
            }
        });
    }

    private void setupAdapter() {
        showProgressBar(View.GONE);
        feedAdapter = new FeedsAdapter(RealmController.getInstance().getFeeds());
        feedAdapter.setOnItemClickListener(this);
        recycler.setAdapter(feedAdapter);
    }

    private void setupRecycler() {
        recycler = (RecyclerView) findViewById(R.id.rvList);
        recycler.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
    }

    private void initToolbarNavDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_fott_feed) {

        } else if (id == R.id.nav_trips) {
            Toast.makeText(FeedsActivity.this, "Feature will be available soon.", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_people) {
            Toast.makeText(FeedsActivity.this, "Feature will be available soon.", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void saveToDatabase(List<Article> articles) {
        for (Article article : articles) {
            realm.beginTransaction();
            try {
                realm.copyToRealmOrUpdate(article);
            } catch (Exception e) {
                Log.e(TAG, "Error during saving data : " + e.getMessage());
            }
            realm.commitTransaction();
        }
    }

    @Override
    public void onClick(FeedsAdapter.FeedViewHolder holder, Article article) {
        ActivityOptionsCompat activityOption = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> image = Pair.create((View) holder.image, holder.image.getTransitionName());
            Pair<View, String> title = Pair.create((View) holder.title, holder.title.getTransitionName());
            Pair<View, String> dateTime = Pair.create((View) holder.dateTime, holder.dateTime.getTransitionName());
            Pair<View, String> ingress = Pair.create((View) holder.ingress, holder.ingress.getTransitionName());
            activityOption = ActivityOptionsCompat.makeSceneTransitionAnimation(this, image, title, dateTime, ingress);
        }

        Intent intent = new Intent(this, FeedDetailActivity.class);
        intent.putExtra(FeedDetailActivity.ARTICLE, Parcels.wrap(article));
        ActivityCompat.startActivity(this, intent, activityOption == null ? null : activityOption.toBundle());
    }
}

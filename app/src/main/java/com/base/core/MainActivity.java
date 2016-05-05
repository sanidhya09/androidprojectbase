package com.base.core;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.base.R;
import com.base.activities.AppSettings;
import com.base.activities.LocationActivity;
import com.base.adapters.NavigationDrawerAdapter;
import com.base.models.Contributor;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements NavigationDrawerAdapter.ItmClicked {
    private DrawerLayout mDrawerLayout;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        String[] stringArray = getResources().getStringArray(R.array.planets_array);
        List<String> strings = Arrays.asList(stringArray);
        linearLayout = (LinearLayout) findViewById(R.id.ll_drawer_left);
        RecyclerView mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mDrawerList.setLayoutManager(llm);
        mDrawerList.setHasFixedSize(true);
        NavigationDrawerAdapter itemListAdapter = new NavigationDrawerAdapter(this);
        itemListAdapter.addAll(strings);
        mDrawerList.setAdapter(itemListAdapter);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        AppApplication appApplication = (AppApplication) getApplication();

        Call<List<Contributor>> call = appApplication.getRestService().contributors("square", "retrofit");

        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                if (response.isSuccessful()) {
                    for (Contributor contributor : response.body()) {
                        System.out.println(contributor.login + " (" + contributor.contributions + ")");
                    }
                } else {
                    // error response, no access to resource?
                }

            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {

            }
        });

//        Observable.just("hii").map(new Func1<String, Contributor>() {
//            public Contributor baseModel;
//
//            @Override
//            public Contributor call(String regID) {
//
//                return baseModel;
//            }
//
//        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Contributor>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onNext(Contributor baseModel) {
//
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, AppSettings.class));

            return true;
        }
        if (id == R.id.action_favorite) {
            startActivity(new Intent(MainActivity.this, LocationActivity.class));
            Snackbar.make(this.getWindow().getDecorView().findViewById(android.R.id.content), "Reloading...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mDrawerLayout.isDrawerOpen(linearLayout);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onItemClick(int position) {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}

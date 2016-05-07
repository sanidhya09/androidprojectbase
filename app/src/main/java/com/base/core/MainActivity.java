package com.base.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.R;
import com.base.activities.AppSettings;
import com.base.activities.LocationActivity;
import com.base.activities.PlacesAutoCompleteSample;
import com.base.adapters.NavigationDrawerAdapter;
import com.base.fragments.FragmentA;
import com.base.gcm.QuickstartPreferences;
import com.base.gcm.RegistrationIntentService;
import com.base.models.Contributor;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements NavigationDrawerAdapter.ItmClicked {
    private DrawerLayout mDrawerLayout;
    private LinearLayout linearLayout;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolBarAndDrawer();
        sampleRestService();
        initGCM();
    }

    private void initGCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    // mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    //  mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };
        // Registering BroadcastReceiver
        registerReceiver();

        if (utilitySingleton.checkPlayServices(this)) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void setUpToolBarAndDrawer() {
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
    }

    private void sampleRestService() {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_favorite);
        MenuItemCompat.setActionView(item, R.layout.feed_update_count);
        RelativeLayout relativeLayout = (RelativeLayout) MenuItemCompat.getActionView(item);
        TextView notificationCount = (TextView) relativeLayout.findViewById(R.id.notification_count);
        notificationCount.setText(String.valueOf(5));
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
                Snackbar.make(v, "Reloading...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        if (id == R.id.action_exit) {
            finish();
            return true;
        }
        if (id == R.id.action_places) {
            startActivity(new Intent(MainActivity.this, PlacesAutoCompleteSample.class));
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
        FragmentA fragmentA = new FragmentA();
        loadRootFragment(fragmentA);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    private void loadRootFragment(Fragment fragment) {
        loadRootFragment(fragment, null);
    }

    private void loadRootFragment(Fragment fragment, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();

        clearBackStack();
    }

    public void clearBackStack() {
        getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}

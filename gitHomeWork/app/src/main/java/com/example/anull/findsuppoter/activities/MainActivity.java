package com.example.anull.findsuppoter.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.anull.findsuppoter.R;
import com.example.anull.findsuppoter.fragments.ChatFragment;
import com.example.anull.findsuppoter.fragments.ChooseFragment;
import com.example.anull.findsuppoter.fragments.MapFragment;
import com.example.anull.findsuppoter.fragments.ProfileFragment;
import com.example.anull.findsuppoter.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "";
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference users;
    private Toolbar toolbar;
    private NavigationView nv_menu;
    // fragment
    ProfileFragment profile = new ProfileFragment();

    //


    private AVLoadingIndicatorView av_loadmain;

    private TextView tv_name_header;
    private TextView tv_email_header;
    private View headerView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupUI();

        av_loadmain.show();


        if (firebaseAuth.getCurrentUser() != null) {
            users.orderByChild("email").equalTo(firebaseAuth.getCurrentUser().getEmail())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "onDataChange: " + dataSnapshot);
                            if (dataSnapshot.getChildrenCount() > 0) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    User user = userSnapshot.getValue(User.class);


                                    tv_name_header.setText(user.getName());
                                    tv_email_header.setText(user.getEmail());

                                    EventBus.getDefault().postSticky(user);

                                    av_loadmain.hide();
                                }

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Snackbar.make(nv_menu, "Get Data false! Please Check Your Internet", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    });

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupUI() {
        nv_menu = findViewById(R.id.nav_view);
        headerView = nv_menu.getHeaderView(0);


        tv_email_header = headerView.findViewById(R.id.tv_emailHeader);
        tv_name_header = headerView.findViewById(R.id.tv_nameHeader);

        av_loadmain = findViewById(R.id.av_loadmain);


        nv_menu.setCheckedItem(R.id.it_map);


        if (firebaseAuth.getCurrentUser() != null) {
            nv_menu.getMenu().performIdentifierAction(R.id.it_map, 0);
            MapFragment mapFragment = new MapFragment();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container, mapFragment).commit();
            toolbar.setTitle("Map");

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else {
            nv_menu.getMenu().performIdentifierAction(R.id.it_logout, 0);
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        setTitle(item.getTitle());

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.it_profile) {
            if (av_loadmain.isShown()) {

                Snackbar.make(nv_menu, "Loading data", Snackbar.LENGTH_SHORT)
                        .show();

                android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.container, profile).commit();
                toolbar.setTitle(item.getTitle());


            } else {

                android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.container, profile).commit();
                toolbar.setTitle(item.getTitle());
            }

            // Handle the camera action
        } else if (id == R.id.it_map) {
            toolbar.setTitle(item.getTitle());
            MapFragment mapFragment = new MapFragment();

            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container, mapFragment).commit();

        } else if (id == R.id.it_mssage) {
            toolbar.setTitle(item.getTitle());

            ChatFragment chatFragment = new ChatFragment();

            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container, chatFragment).commit();

        } else if (id == R.id.it_logout) {
            SpotsDialog waitingDialog = new SpotsDialog(this);
            waitingDialog.show();

            firebaseAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);

            this.startActivities(new Intent[]{intent});

        } else if (id == R.id.nav_share) {

            ChooseFragment mapFragment = new ChooseFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.container, mapFragment).commit();

        } else if (id == R.id.nav_send) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

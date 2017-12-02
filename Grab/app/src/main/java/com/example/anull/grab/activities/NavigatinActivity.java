package com.example.anull.grab.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.anull.grab.fragments.MapFragment;
import com.example.anull.grab.fragments.ProfileFragment;
import com.example.anull.grab.R;
import com.example.anull.grab.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NavigatinActivity extends AppCompatActivity {
    private static final String TAG = "fix";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;


    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference users;

    private TextView tv_name_header;
    private TextView tv_email_header;
    private ArrayList<User> listUser;
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
        setContentView(R.layout.activity_navigatin);

        setUpUI();

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

                                Log.d(TAG, "onDataChange: pppppppp" + user.getName());
                                EventBus.getDefault().postSticky(user);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    private void setUpUI() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = database.getReference("users");


        NavigationView nv_menu = findViewById(R.id.nv_menu);

        headerView = nv_menu.getHeaderView(0);

        tv_email_header = headerView.findViewById(R.id.tv_email_header);
        tv_name_header = headerView.findViewById(R.id.tv_name_header);

        drawerLayout = findViewById(R.id.draw_navi);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_toggle, R.string.close_toggle);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpDrawerContent(nv_menu);

        nv_menu.setCheckedItem(R.id.it_map);
        nv_menu.getMenu().performIdentifierAction(R.id.it_map, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void selectItemDrawer(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.it_profile:
                fragmentClass = ProfileFragment.class;
                break;

            case R.id.it_map:
                fragmentClass = MapFragment.class;
                break;

            default:
                fragmentClass = MapFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl_navi, fragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }

    private void setUpDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);

                return true;
            }
        });
    }
}

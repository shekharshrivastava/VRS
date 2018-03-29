package com.app.ssoft.vrs.View;


import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ssoft.vrs.Model.UserProfile;
import com.app.ssoft.vrs.R;
import com.app.ssoft.vrs.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<Address> addresses;
    Fragment fragment = null;
    private FrameLayout content_frame;
    private FirebaseAuth auth;
    private TextView emailTextView;
    private AlertDialog.Builder builder;
    private DatabaseReference ref;
    private TextView userNameTv;
    private ImageView imageView;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content_frame = findViewById(R.id.content_frame);
        auth = FirebaseAuth.getInstance();
        final String emailId = auth.getCurrentUser().getEmail();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        userNameTv = navHeaderView.findViewById(R.id.userNameTv);
        imageView = navHeaderView.findViewById(R.id.imageView);
        emailTextView = navHeaderView.findViewById(R.id.emailTextView);
        emailTextView.setText(emailId);
        fragment = new VehicleListFragment();
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        /*m_listAdapter = new ListAdapter(MainActivity.this, vehicleDetails);
        rl_lvListRoot.setAdapter(m_listAdapter);*/

        //Get datasnapshot at your "users" root node

        ref = FirebaseDatabase.getInstance().getReference().child("userProfile");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                if ((userProfile.getEmailId()).equals(emailId)) {
                    if (userProfile.getFirstName() != null && userProfile.getLastName() != null) {
                        userNameTv.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
                    }
                    if (userProfile.getProfilePicture() != null) {
                        imageView.setImageBitmap(Utils.StringToBitMap(userProfile.getProfilePicture()));
                    } else {
                        imageView.setImageResource(R.drawable.placeholder_user);
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            builder = new AlertDialog.Builder(this);
            final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
                    this);
            alertDialog.setTitle("Exit");
            alertDialog.setMessage("Are you sure you want to exit ?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
//            super.onBackPressed();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            auth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragment = new VehicleListFragment();
        } else if (id == R.id.nav_gallery) {
            fragment = new MyVehicleFragment();
        } else if (id == R.id.nav_slideshow) {
            fragment = new FeedbackFragment();
        } else if (id == R.id.nav_camera) {
            fragment = new MyRidesFragment();
        } else if (id == R.id.nav_manage) {
        } else if (id == R.id.nav_logout) {
            auth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





}


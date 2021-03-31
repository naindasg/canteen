package com.example.canteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        //  ActionBar actionBar = getSupportActionBar();
        //  actionBar.setDisplayHomeAsUpEnabled(true); //Shows the menu icon
        //  actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24); // menu icon
        //  getSupportActionBar().setDisplayShowTitleEnabled(false); //Removes the 'Canteen' subtitle


//        mDrawerLayout = findViewById(R.id.drawer_layout);
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//                mDrawerLayout.closeDrawers(); //Closing the navigation once the item is clicked
//
//                int id = menuItem.getItemId();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                if (id == R.id.nav_restaurant) {
//                } else if (id == R.id.nav_tray) {
//                    transaction.replace(R.id.content_view, new BasketFragment()).commit();
//                } else if (id == R.id.nav_order) {
//                } else if (id == R.id.nav_logout) {
//
//                }
//
//                return true;
//            }
//        });

        Intent intent = getIntent();
        String screen = intent.getStringExtra("screen");

        String mealPrice = intent.getStringExtra("mealPrice");


        if(Objects.equals(screen, "tray")) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_view, new BasketFragment()).commit();

        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        }


    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        switch(itemId) {
//            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
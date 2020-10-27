package com.secretdevbd.dexian.dailybudget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.secretdevbd.dexian.dailybudget.Fragment.BudgetFragment;
import com.secretdevbd.dexian.dailybudget.Fragment.CategoryFragment;
import com.secretdevbd.dexian.dailybudget.Fragment.DashboardFragment;
import com.secretdevbd.dexian.dailybudget.Fragment.TransactionFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.draw_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.menu_dashboard);
        }

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu_dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DashboardFragment()).commit();
                break;
            case R.id.menu_transaction:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TransactionFragment()).commit();
                break;
            case R.id.menu_category:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CategoryFragment()).commit();
                break;
            case R.id.menu_budget:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BudgetFragment()).commit();

                break;
            case R.id.menu_about:

                break;
            case R.id.menu_report:

                break;
            case R.id.menu_setting:

                break;
            case R.id.menu_support:

                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}

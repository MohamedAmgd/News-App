/**
 * Copyright 2020 Mohamed Amgd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.mohamedamgd.newsapp.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.mohamedamgd.newsapp.R;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static MenuItem mSelectedMenuItem;
    MenuItem mSearchMenuItem;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mActionBarDrawerToggle;
    Toolbar mToolbar;
    SearchView mSearchView;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Initialize the navigation drawer
        initNavDrawer();

        // check if the activity is recreated after screen rotation or not
        if (savedInstanceState != null) {
            // find the last selected item before rotation
            int id = savedInstanceState.getInt("selected item");

            // set it to be the selected item
            mSelectedMenuItem = mNavigationView.getMenu().findItem(id);
        } else {
            // set the selected menu item to be Home
            mSelectedMenuItem = mNavigationView.getMenu().getItem(0);
        }
        onNavigationItemSelected(mSelectedMenuItem);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected item", mSelectedMenuItem.getItemId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_options, menu);
        mSearchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                newsFragment("search", 0, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                onNavigationItemSelected(mSelectedMenuItem);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (mSearchMenuItem != null) mSearchMenuItem.setVisible(true);
        switch (menuItem.getItemId()) {
            case R.id.home_item:
                mToolbar.setTitle("Home");
                newsFragment("topHeadlines", 0, "");
                break;
            case R.id.settings_item:
                mToolbar.setTitle("Settings");
                settingsFragment();
                mSearchMenuItem.setVisible(false);
                break;
            case R.id.sports_item:
                mToolbar.setTitle("Sports");
                newsFragment("topHeadlines", 1, "");
                break;
            case R.id.science_item:
                mToolbar.setTitle("Science");
                newsFragment("topHeadlines", 2, "");
                break;
            case R.id.technology_item:
                mToolbar.setTitle("Technology");
                newsFragment("topHeadlines", 3, "");
                break;
            case R.id.health_item:
                mToolbar.setTitle("Health");
                newsFragment("topHeadlines", 4, "");
                break;
            case R.id.business_item:
                mToolbar.setTitle("Business");
                newsFragment("topHeadlines", 5, "");
                break;
            case R.id.entertainment_item:
                mToolbar.setTitle("Entertainment");
                newsFragment("topHeadlines", 6, "");
                break;
            case R.id.favorites_item:
                mToolbar.setTitle("Favorites");
                newsFragment("favorites", 0, "");
                break;
            case R.id.exit_item:
                finish();

        }
        mDrawerLayout.closeDrawers();
        return true;
    }


    private void initNavDrawer() {
        mDrawerLayout = findViewById(R.id.drawer);
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mActionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mActionBarDrawerToggle.syncState();
    }

    void newsFragment(String type, int categoryIndex, String query) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putInt("categoryIndex", categoryIndex);
        bundle.putString("query", query);
        NewsFragment newsFragment = NewsFragment.newInstance();
        newsFragment.setArguments(bundle);
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container, newsFragment);
        mFragmentTransaction.commit();
    }

    void settingsFragment() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_container, new SettingsFragment());
        mFragmentTransaction.commit();
    }

}

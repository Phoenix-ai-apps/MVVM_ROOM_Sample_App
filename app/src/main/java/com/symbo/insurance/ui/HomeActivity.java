package com.symbo.insurance.ui;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.symbo.insurance.AppExecutors;
import com.symbo.insurance.R;
import com.symbo.insurance.SymboApp;
import com.symbo.insurance.adapters.InsuranceSearchAdapter;
import com.symbo.insurance.adapters.MenuItemsAdapter;
import com.symbo.insurance.constants.AppConstants;
import com.symbo.insurance.databinding.AppDrawerLayoutBinding;
import com.symbo.insurance.db.SymboDatabase;
import com.symbo.insurance.db.entity.InsuranceTypeEntity;
import com.symbo.insurance.interfaces.InsuranceSearchListener;
import com.symbo.insurance.interfaces.LoadWebViewListener;
import com.symbo.insurance.ui.fragments.HomeFragment;
import com.symbo.insurance.utils.ApplicationUtils;
import com.symbo.insurance.utils.DialogUtils;
import com.symbo.insurance.utils.NetworkUtils;
import com.symbo.insurance.viewModel.HomeViewModel;

import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements LoadWebViewListener, AppConstants, View.OnClickListener, InsuranceSearchListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private ActionBarDrawerToggle toggle;
    private AppDrawerLayoutBinding binding;
    private MenuItemsAdapter       menuItemsAdapter;
    private InsuranceSearchAdapter searchAdapter;
    private LinearLayoutManager    mLinearLayoutManagerMenu;
    private LinearLayoutManager    mLinearLayoutManagerSearch;
    private AppExecutors           appExecutor;
    private SymboDatabase          database;
    private List<InsuranceTypeEntity> entityList;
    private List<InsuranceTypeEntity> entityListSearch;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.app_drawer_layout);
        final HomeViewModel mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        subscribeUi(mViewModel);
        initResources();
    }

    private void subscribeUi(HomeViewModel homeViewModel) {
        homeViewModel.getLiveDataNotifyEntityUnreadList().observe(this, new Observer<List<InsuranceTypeEntity>>() {
            @Override
            public void onChanged(List<InsuranceTypeEntity> insuranceTypeEntities) {
                if (insuranceTypeEntities != null && insuranceTypeEntities.size() > 0) {
                    entityList       = insuranceTypeEntities;
                    entityListSearch = insuranceTypeEntities;
                    Collections.sort(entityList, InsuranceTypeEntity.InsurancePrecedence);
                    menuItemsAdapter.setMenuItemList(entityList);
                    searchAdapter.setMenuItemList(entityListSearch);
                }
            }
        });
    }

    private void initResources() {
        appExecutor = new AppExecutors();
        database = ((SymboApp) SymboApp.getInstance().getApplicationContext()).getDatabase();

        // Below line of code is to support Search menu from menu folder
        setSupportActionBar(binding.incHomeAct.toolbar);

        binding.incHomeAct.ivHeart.setOnClickListener(this);
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.incHomeAct.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        binding.drawerLayout.addDrawerListener(toggle);
        binding.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawer, float slideOffset) {
                binding.incHomeAct.layoutMain.setX(binding.navView.getWidth() * slideOffset);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }
        });
        toggle.syncState();

        mLinearLayoutManagerMenu = new LinearLayoutManager(SymboApp.getInstance(), LinearLayoutManager.VERTICAL, false);
        menuItemsAdapter = new MenuItemsAdapter(entityList, this::startWebView);
        binding.incNav.rvMenu.setLayoutManager(mLinearLayoutManagerMenu);
        binding.incNav.rvMenu.setAdapter(menuItemsAdapter);

        //Below init for search result
        mLinearLayoutManagerSearch = new LinearLayoutManager(SymboApp.getInstance(), LinearLayoutManager.VERTICAL, false);
        searchAdapter = new InsuranceSearchAdapter(entityList, this::startWebView, this::isDataAvailable);
        binding.incHomeAct.loSearchIns.rvInsSearch.setLayoutManager(mLinearLayoutManagerSearch);
        binding.incHomeAct.loSearchIns.rvInsSearch.setAdapter(searchAdapter);

        loadFragment(new HomeFragment(), HomeFragment.class.getSimpleName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint(SymboApp.getInstance().getString(R.string.search_insurance));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    searchAdapter.getFilter().filter("");
                    binding.incHomeAct.loSearchIns.rlSearch.setVisibility(View.GONE);
                } else {
                    searchAdapter.getFilter().filter(newText);
                    binding.incHomeAct.loSearchIns.rlSearch.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        return true;
    }

    private void loadFragment(Fragment fragment, String fragmentName) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_main, fragment, fragmentName);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void startWebView(String url, String name) {
        // start webView activity to load url
        if (!TextUtils.isEmpty(url)) {
            Bundle bundle = new Bundle();
            bundle.putString(WEB_URL, url);
            if (!TextUtils.isEmpty(name)) {
                bundle.putString(NAME, name);
            }
            if (NetworkUtils.isConnected(SymboApp.getInstance())) {
                ApplicationUtils.startActivityIntent(this, WebViewActivity.class, bundle);
            } else {
                dialog = DialogUtils.showNoInternetDialog(HomeActivity.this);
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Nothing to show
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (view == binding.incHomeAct.ivHeart) {
            Toast.makeText(SymboApp.getInstance(), SymboApp.getInstance().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void isDataAvailable(boolean avaliable) {
       if(avaliable){
           binding.incHomeAct.loSearchIns.rvInsSearch.setVisibility(View.VISIBLE);
           binding.incHomeAct.loSearchIns.tvNoResults.setVisibility(View.GONE);
       }else {
           binding.incHomeAct.loSearchIns.rvInsSearch.setVisibility(View.GONE);
           binding.incHomeAct.loSearchIns.tvNoResults.setVisibility(View.VISIBLE);
       }
    }
}

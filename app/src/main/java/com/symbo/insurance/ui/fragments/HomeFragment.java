package com.symbo.insurance.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.symbo.insurance.R;
import com.symbo.insurance.SymboApp;
import com.symbo.insurance.adapters.HomeItemsAdapter;
import com.symbo.insurance.constants.AppConstants;
import com.symbo.insurance.databinding.FragmentHomeBinding;
import com.symbo.insurance.db.entity.InsuranceTypeEntity;
import com.symbo.insurance.interfaces.LoadWebViewListener;
import com.symbo.insurance.ui.WebViewActivity;
import com.symbo.insurance.utils.ApplicationUtils;
import com.symbo.insurance.utils.DialogUtils;
import com.symbo.insurance.utils.NetworkUtils;
import com.symbo.insurance.viewModel.HomeViewModel;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements LoadWebViewListener, AppConstants, View.OnClickListener {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private FragmentHomeBinding binding;
    private List<InsuranceTypeEntity> entityList;
    private HomeItemsAdapter homeItemsAdapter;
    private Dialog dialog;
    private GridLayoutManager gridLayoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        final HomeViewModel mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        subscribeUi(mViewModel);
        initResources();
        return binding.getRoot();
    }

    private void subscribeUi(HomeViewModel homeViewModel) {
        homeViewModel.getLiveDataNotifyEntityUnreadList().observe(this, new Observer<List<InsuranceTypeEntity>>() {
            @Override
            public void onChanged(List<InsuranceTypeEntity> insuranceTypeEntities) {
                if(insuranceTypeEntities != null && insuranceTypeEntities.size() > 0){
                    entityList = insuranceTypeEntities;
                    Collections.sort(entityList, InsuranceTypeEntity.InsurancePrecedence);
                    homeItemsAdapter.setHomeItemList(entityList);
                }
            }
        });
    }

    private void initResources() {
        gridLayoutManager = new GridLayoutManager(SymboApp.getInstance(),3);
        homeItemsAdapter  = new HomeItemsAdapter(entityList, this::startWebView);
        binding.rvHomeMenu.setLayoutManager(gridLayoutManager);
        binding.rvHomeMenu.setAdapter(homeItemsAdapter);
        binding.tvShare.setOnClickListener(this);
        binding.tvViewInsurance.setOnClickListener(this);
    }

    @Override
    public void startWebView(String url, String name) {
        // start webView activity to load url
        if(!TextUtils.isEmpty(url)){
            Bundle bundle = new Bundle();
            bundle.putString(WEB_URL, url);
            if(!TextUtils.isEmpty(name)){
                bundle.putString(NAME, name);
            }
            if(getActivity() != null && isAdded()){
                if(NetworkUtils.isConnected(SymboApp.getInstance())){
                    ApplicationUtils.startActivityIntent(getActivity(), WebViewActivity.class, bundle);
                }else {
                    dialog = DialogUtils.showNoInternetDialog(getActivity());
                }
            }
        }else {
            // Nothing to show
        }
    }

    @Override
    public void onClick(View view) {
        if(view == binding.tvShare){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, SymboApp.getInstance().getString(R.string.card_details));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent,""));
        }else if(view == binding.tvViewInsurance){
            Toast.makeText(SymboApp.getInstance(), SymboApp.getInstance().getString(R.string.no_data), Toast.LENGTH_SHORT).show();
        }
    }
}

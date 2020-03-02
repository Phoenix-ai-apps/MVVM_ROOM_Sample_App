package com.symbo.insurance.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbo.insurance.R;
import com.symbo.insurance.constants.AppConstants;
import com.symbo.insurance.databinding.AdapterHomeItemsBinding;
import com.symbo.insurance.db.entity.InsuranceTypeEntity;
import com.symbo.insurance.interfaces.LoadWebViewListener;

import java.util.List;

public class HomeItemsAdapter extends RecyclerView.Adapter<HomeItemsAdapter.HomeItemsViewHolder>
        implements AppConstants {

    private List<InsuranceTypeEntity>  entityList;
    private LoadWebViewListener loadWebView;

    public HomeItemsAdapter(List<InsuranceTypeEntity> entities, LoadWebViewListener loadWebView){
        this.entityList  = entities;
        this.loadWebView = loadWebView;
    }

    public void setHomeItemList(List<InsuranceTypeEntity> entities){
        this.entityList         = entities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        AdapterHomeItemsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.adapter_home_items, viewGroup, false);
        return new HomeItemsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(HomeItemsViewHolder holder, int position) {
        InsuranceTypeEntity entity = entityList.get(position);
        if (entity != null && !TextUtils.isEmpty(entity.getWebviewURL())){
            holder.binding.setInsuranceEntity(entity);
            // If server image is not availabe then we are replacing it with stored icon
            if(!TextUtils.isEmpty(entity.getImageURL())){
                holder.binding.setImageURL(entity.getImageURL());
            }else if(!TextUtils.isEmpty(entity.getCategory())){
                holder.binding.setImageURL(entity.getCategory());
            }
            holder.binding.llCointainer.setOnClickListener((View)->{
                loadWebView.startWebView(entity.getWebviewURL(), entity.getName());
            });
        }
    }

    class HomeItemsViewHolder extends RecyclerView.ViewHolder {
        AdapterHomeItemsBinding binding;
        public HomeItemsViewHolder(AdapterHomeItemsBinding itemsBinding) {
            super(itemsBinding.getRoot());
            binding = itemsBinding;
        }
    }

    @Override
    public int getItemCount() {
        return (entityList != null && entityList.size() > 0) ? entityList.size() : 0;
    }


}

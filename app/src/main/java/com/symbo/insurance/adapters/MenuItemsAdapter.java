package com.symbo.insurance.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbo.insurance.R;
import com.symbo.insurance.constants.AppConstants;
import com.symbo.insurance.databinding.AdapterMenuItemsBinding;
import com.symbo.insurance.db.entity.InsuranceTypeEntity;
import com.symbo.insurance.interfaces.LoadWebViewListener;

import java.util.List;

public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.MenuItemsViewHolder>
        implements AppConstants {

    private List<InsuranceTypeEntity>  entityList;
    private LoadWebViewListener loadWebView;

    public MenuItemsAdapter(List<InsuranceTypeEntity> entities, LoadWebViewListener loadWebView){
        this.entityList  = entities;
        this.loadWebView = loadWebView;
    }

    public void setMenuItemList(List<InsuranceTypeEntity> entities){
        this.entityList         = entities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        AdapterMenuItemsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.adapter_menu_items, viewGroup, false);
        return new MenuItemsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MenuItemsViewHolder holder, int position) {
        InsuranceTypeEntity entity = entityList.get(position);
        if (entity != null && !TextUtils.isEmpty(entity.getWebviewURL())){
            holder.binding.setInsuranceEntity(entity);
            // If server image is not available then we are replacing it with stored icon
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

    class MenuItemsViewHolder extends RecyclerView.ViewHolder {
        AdapterMenuItemsBinding binding;
        public MenuItemsViewHolder(AdapterMenuItemsBinding itemsBinding) {
            super(itemsBinding.getRoot());
            binding = itemsBinding;
        }
    }

    @Override
    public int getItemCount() {
        return (entityList != null && entityList.size() > 0) ? entityList.size() : 0;
    }


}

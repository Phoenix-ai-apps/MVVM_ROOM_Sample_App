package com.symbo.insurance.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbo.insurance.R;
import com.symbo.insurance.constants.AppConstants;
import com.symbo.insurance.databinding.AdapterMenuItemsBinding;
import com.symbo.insurance.db.entity.InsuranceTypeEntity;
import com.symbo.insurance.interfaces.InsuranceSearchListener;
import com.symbo.insurance.interfaces.LoadWebViewListener;

import java.util.ArrayList;
import java.util.List;

public class InsuranceSearchAdapter extends RecyclerView.Adapter<InsuranceSearchAdapter.MenuItemsViewHolder>
        implements AppConstants, Filterable {

    private List<InsuranceTypeEntity>  entityList;
    private List<InsuranceTypeEntity>  entityListFiltered;
    private LoadWebViewListener        loadWebView;
    private InsuranceSearchListener   searchListener;

    public InsuranceSearchAdapter(List<InsuranceTypeEntity> entities, LoadWebViewListener loadWebView, InsuranceSearchListener listener){
        this.entityList          = entities;
        this.entityListFiltered  = entities;
        this.loadWebView         = loadWebView;
        this.searchListener      = listener;
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
        InsuranceTypeEntity entity = entityListFiltered.get(position);
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

    class MenuItemsViewHolder extends RecyclerView.ViewHolder {
        AdapterMenuItemsBinding binding;
        public MenuItemsViewHolder(AdapterMenuItemsBinding itemsBinding) {
            super(itemsBinding.getRoot());
            binding = itemsBinding;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    entityListFiltered = entityList;
                } else {
                    if(entityList != null && entityList.size() > 0){
                        List<InsuranceTypeEntity> filteredList = new ArrayList<>();
                        for (InsuranceTypeEntity entity : entityList) {
                            if (entity.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(entity);
                            }
                        }
                        entityListFiltered = filteredList;
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = entityListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                List<InsuranceTypeEntity> friendEntities = (ArrayList<InsuranceTypeEntity>) filterResults.values;
                if(friendEntities == null || (friendEntities != null && friendEntities.size() == 0)){
                    entityListFiltered = new ArrayList<>();
                    searchListener.isDataAvailable(false);
                }else {
                    entityListFiltered = friendEntities;
                    searchListener.isDataAvailable(true);
                }
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return (entityListFiltered != null && entityListFiltered.size() > 0) ? entityListFiltered.size() : 0;
    }


}

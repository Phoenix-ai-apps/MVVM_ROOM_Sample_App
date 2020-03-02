package com.symbo.insurance.adapters;

import android.text.TextUtils;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;
import com.symbo.insurance.R;
import com.symbo.insurance.SymboApp;
import com.symbo.insurance.constants.AppConstants;
import com.symbo.insurance.glide.GlideApp;

public class BindingAdapters implements AppConstants {

    @BindingAdapter("icon")
    public static void setIcon(View view, String icon) {
        if (!TextUtils.isEmpty(icon) && icon.trim().length() > 0) {
            if (icon.contains(HTTP) || icon.contains(HTTPS)) {
                // show network image
                GlideApp.with(SymboApp.getInstance())
                        .load(icon)
                        .into(((AppCompatImageView) view));
            } else {
                    int resourceId = SymboApp.getInstance().getResources().getIdentifier(icon.trim().toLowerCase(), "drawable",
                            SymboApp.getInstance().getPackageName());
                    if (resourceId != 0) {
                        ((AppCompatImageView) view).setImageResource(resourceId);
                    } else {
                        ((AppCompatImageView) view).setImageResource(R.drawable.ic_launcher);
                    }
            }
        } else {
            ((AppCompatImageView) view).setImageResource(R.drawable.ic_launcher);
        }
    }


}

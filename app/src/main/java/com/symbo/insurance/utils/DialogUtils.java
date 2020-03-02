package com.symbo.insurance.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.LinearLayout;
import androidx.databinding.DataBindingUtil;
import com.symbo.insurance.R;
import com.symbo.insurance.constants.AppConstants;
import com.symbo.insurance.databinding.DialogNoInternetBinding;
import com.symbo.insurance.databinding.DialogPleaseWaitBinding;

public class DialogUtils implements AppConstants {
    public static Dialog showLoadingDialog(Context context) {
        Dialog alert = new Dialog(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DialogPleaseWaitBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.dialog_please_wait, null, false);
        alert.setCancelable(true);
        alert.setCanceledOnTouchOutside(false);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alert.setContentView(binding.getRoot());
        alert.show();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        alert.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
        return alert;
    }

    public static Dialog showNoInternetDialog(Context context) {
        Dialog alert = new Dialog(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DialogNoInternetBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.dialog_no_internet, null, false);
        binding.tvDismiss.setOnClickListener((View)->{
            alert.dismiss();
        });
        alert.setCancelable(true);
        alert.setCanceledOnTouchOutside(false);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alert.setContentView(binding.getRoot());
        alert.show();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        alert.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
        return alert;
    }
}

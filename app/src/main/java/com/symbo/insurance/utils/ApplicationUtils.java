package com.symbo.insurance.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.symbo.insurance.BuildConfig;
import com.symbo.insurance.SymboApp;
import com.symbo.insurance.constants.AppConstants;
import com.symbo.insurance.db.entity.InsuranceTypeEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ApplicationUtils implements AppConstants {

    public static void printLog(String strTag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(strTag, !TextUtils.isEmpty(message) ? message : "Message string is null");
        }
    }

    public static void startActivityIntent(Context context, Class aClass, Bundle bundle) {
        Intent intent = new Intent(context, aClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static List<InsuranceTypeEntity> getInsuranceDataFromAsset() {
        List<InsuranceTypeEntity> entityList = null;
        try {
            String json = null;
            InputStream is = SymboApp.getInstance().getAssets().open(MOCK_DATA_FILE);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            if(json != null){
                entityList = DeseriallizeUtils.deserializeInsuranceTypeEntityList(json.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return entityList;
    }

}

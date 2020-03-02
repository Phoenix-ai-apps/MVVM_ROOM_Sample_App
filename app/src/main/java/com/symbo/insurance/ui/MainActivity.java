package com.symbo.insurance.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;

import com.symbo.insurance.AppExecutors;
import com.symbo.insurance.DataRepository;
import com.symbo.insurance.R;
import com.symbo.insurance.SymboApp;
import com.symbo.insurance.databinding.ActivityMainBinding;
import com.symbo.insurance.db.SymboDatabase;
import com.symbo.insurance.db.entity.InsuranceTypeEntity;
import com.symbo.insurance.utils.ApplicationUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String   TAG = MainActivity.class.getSimpleName();
    private AppExecutors          appExecutor;
    private SymboDatabase         database;
    private ActivityMainBinding   binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initResources();
    }

    private void initResources() {
        appExecutor = new AppExecutors();
        database = ((SymboApp) SymboApp.getInstance().getApplicationContext()).getDatabase();
        appExecutor.getExeDiskIO().execute(()->{
            List<InsuranceTypeEntity> entityList = ApplicationUtils.getInsuranceDataFromAsset();
            if(entityList != null && entityList.size() > 0){
               //Delete all old data and insert new data
               int del = database.getInsuranceTypeDao().deleteAllInsuranceTypeEntity();
               ApplicationUtils.printLog(TAG, "Table delete with id: "+del);
               for (InsuranceTypeEntity entity : entityList){
                   database.getInsuranceTypeDao().insertInsuranceTypEntity(entity);
               }

               appExecutor.getExeMainThread().execute(()->{
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           ApplicationUtils.startActivityIntent(MainActivity.this, HomeActivity.class, null);
                           finish();
                       }
                   }, 2000);
               });

            }else {
                ApplicationUtils.printLog(TAG, "Data is null");
            }
        });
    }
}

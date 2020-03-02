package com.symbo.insurance;

import android.app.Application;
import android.content.Context;
import com.symbo.insurance.db.SymboDatabase;

public class SymboApp extends Application {
    private AppExecutors       appExecutors;
    private static Context     context;

    @Override
    public void onCreate() {
        super.onCreate();
        appExecutors = new AppExecutors();
        context = getApplicationContext();
    }

    public SymboDatabase getDatabase(){
        return SymboDatabase.getInstance(this, appExecutors);
    }

    public DataRepository getDataRepository(){
        return DataRepository.getDataRepository(getDatabase());
    }

    public static Context getInstance(){
        return SymboApp.context;
    }

}

package com.symbo.insurance;

import androidx.lifecycle.LiveData;

import com.symbo.insurance.constants.AppConstants;
import com.symbo.insurance.db.SymboDatabase;
import com.symbo.insurance.db.entity.InsuranceTypeEntity;

import java.util.List;

public class DataRepository implements AppConstants {

    private static DataRepository dataRepository;
    private final SymboDatabase database;

    private DataRepository(SymboDatabase database) {
        this.database = database;
    }

    public static DataRepository getDataRepository(final SymboDatabase database) {
        if (dataRepository == null) {
            synchronized (DataRepository.class) {
                if (dataRepository == null) {
                    dataRepository = new DataRepository(database);
                }
            }
        }
        return dataRepository;
    }

    // Get the data from the database and get notified when the data changes.
    public LiveData<List<InsuranceTypeEntity>> getLiveDataInsuranceTypeEntityList() {
        return database.getInsuranceTypeDao().getLiveDataInsuranceTypeList();
    }

    public List<InsuranceTypeEntity> getInsuranceTypeEntityList() {
        return database.getInsuranceTypeDao().getInsuranceTypeList();
    }
}

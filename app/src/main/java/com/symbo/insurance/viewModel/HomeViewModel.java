package com.symbo.insurance.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import com.symbo.insurance.DataRepository;
import com.symbo.insurance.SymboApp;
import com.symbo.insurance.db.entity.InsuranceTypeEntity;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final MediatorLiveData<List<InsuranceTypeEntity>> liveData;
    private DataRepository dataRepository;

    public HomeViewModel(Application application) {
        super(application);
        liveData = new MediatorLiveData<>();
        dataRepository = ((SymboApp) application).getDataRepository();
    }

    public LiveData<List<InsuranceTypeEntity>> getLiveDataNotifyEntityUnreadList() {
        LiveData<List<InsuranceTypeEntity>> listLiveData = dataRepository.getLiveDataInsuranceTypeEntityList();
        liveData.addSource(listLiveData, liveData::setValue);
        return liveData;
    }

}

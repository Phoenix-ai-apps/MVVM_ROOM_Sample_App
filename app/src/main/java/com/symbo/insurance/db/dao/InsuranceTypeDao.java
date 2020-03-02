package com.symbo.insurance.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.symbo.insurance.db.entity.InsuranceTypeEntity;

import java.util.List;

@Dao
public interface InsuranceTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertInsuranceTypEntity(InsuranceTypeEntity entity);

    @Query("SELECT * FROM InsuranceType WHERE id = :id")
    InsuranceTypeEntity getInsuranceTypeEntityById(int id);

    @Query("SELECT * FROM InsuranceType")
    LiveData<List<InsuranceTypeEntity>> getLiveDataInsuranceTypeList();

    @Query("SELECT * FROM InsuranceType")
    List<InsuranceTypeEntity> getInsuranceTypeList();

    @Query("DELETE FROM InsuranceType")
    int deleteAllInsuranceTypeEntity();

    @Update
    int updateInsuranceTypeEntity(InsuranceTypeEntity entity);


}

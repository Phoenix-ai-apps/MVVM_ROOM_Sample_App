package com.symbo.insurance.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.symbo.insurance.db.entity.InsuranceTypeEntity;

import java.util.List;

public class DeseriallizeUtils {

    public static Gson getGsonObject() {
        return new GsonBuilder().create();
    }

    public static List<InsuranceTypeEntity> deserializeInsuranceTypeEntityList(String response){
        List<InsuranceTypeEntity> responseClass = null;
        try{
            responseClass = getGsonObject().fromJson(response,
                    new TypeToken<List<InsuranceTypeEntity>>() {
                    }.getType());
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseClass;
    }

}


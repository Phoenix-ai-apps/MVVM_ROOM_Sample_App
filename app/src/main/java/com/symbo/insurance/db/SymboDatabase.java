package com.symbo.insurance.db;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.symbo.insurance.AppExecutors;
import com.symbo.insurance.constants.AppConstants;
import com.symbo.insurance.db.dao.InsuranceTypeDao;
import com.symbo.insurance.db.entity.InsuranceTypeEntity;
import com.symbo.insurance.utils.ApplicationUtils;

import static com.symbo.insurance.constants.AppConstants.DATABASE_VERSION;

@Database(entities = InsuranceTypeEntity.class, version = DATABASE_VERSION, exportSchema = false)
public abstract class SymboDatabase extends RoomDatabase implements AppConstants {

    private static final String TAG      = SymboDatabase.class.getSimpleName();
    private static SymboDatabase           symboDatabase;
    private final MutableLiveData<Boolean> isDBCreated = new MutableLiveData<>();
    public abstract InsuranceTypeDao       getInsuranceTypeDao();

    public static SymboDatabase getInstance(final Context context, final AppExecutors appExecutors){
        if(symboDatabase == null){
            synchronized (SymboDatabase.class){
              symboDatabase = Room.databaseBuilder(context, SymboDatabase.class, DATABASE_NAME)
                      .addMigrations(MIGRATION_0_1)
                      .build();
                ApplicationUtils.printLog(TAG, "DB created Successfully");
            }
        }
        return symboDatabase;
    }

    //Migrations
    static final Migration MIGRATION_0_1 = new Migration(0, 1) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };
}


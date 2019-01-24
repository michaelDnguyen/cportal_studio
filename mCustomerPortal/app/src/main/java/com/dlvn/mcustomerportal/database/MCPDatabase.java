package com.dlvn.mcustomerportal.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dlvn.mcustomerportal.database.entity.ClaimDocEntity;
import com.dlvn.mcustomerportal.database.entity.ClaimEntity;
import com.dlvn.mcustomerportal.database.entity.DocumentEntity;
import com.dlvn.mcustomerportal.utils.myLog;

@Database(entities = {ClaimEntity.class, ClaimDocEntity.class, DocumentEntity.class}, version = 1, exportSchema = false)
public abstract class MCPDatabase extends RoomDatabase {

    private static final String TAG = "MCPDatabase";

    public abstract ClaimDao claimDao();

    public abstract ClaimDocDao claimDocDao();

    public abstract DocumentDao documentDao();

    @Override
    public void clearAllTables() {
        myLog.e(TAG, "clear All Tables");
        claimDao().deleteAllClaims();
        claimDocDao().deleteAllClaimDoc();
        documentDao().deleteAllDocument();
    }
}

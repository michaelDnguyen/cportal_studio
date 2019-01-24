package com.dlvn.mcustomerportal.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DataClient {

    private Context mCtx;
    private static DataClient mInstance;

    //our app database object
    private MCPDatabase appDatabase;

    private DataClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, MCPDatabase.class, "mCP").allowMainThreadQueries().build();
    }

    public static synchronized DataClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DataClient(mCtx);
        }
        return mInstance;
    }

    public MCPDatabase getAppDatabase() {
        return appDatabase;
    }

}

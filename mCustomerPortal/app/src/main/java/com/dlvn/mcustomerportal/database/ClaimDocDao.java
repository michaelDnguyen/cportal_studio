package com.dlvn.mcustomerportal.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dlvn.mcustomerportal.database.entity.ClaimDocEntity;

import java.util.List;

@Dao
public interface ClaimDocDao {

    /**
     * Get all claims in DB
     * @return
     */
    @Query("select * from ClaimDocEntity")
    List<ClaimDocEntity> getAll();

    /**
     * get List Claism Doc by ClaimID, ClaimID is ID return server
     * @param clamEntityID
     * @return
     */
    @Query("select * from ClaimDocEntity where ClaimEntityID = :clamEntityID")
    List<ClaimDocEntity> getListDocTypeByClaimID(long clamEntityID);

    /**
     * get List Claims Doc by Claim Doc id
     * @param id
     * @return
     */
    @Query("select * from ClaimDocEntity where id = :id")
    List<ClaimDocEntity> getListDocTypeByDocID(long id);

    @Insert
    long insert(ClaimDocEntity entity);

    @Update
    int update(ClaimDocEntity entity);

    @Delete
    int delete(ClaimDocEntity entity);

    @Query("delete from ClaimDocEntity")
    int deleteAllClaimDoc();
}

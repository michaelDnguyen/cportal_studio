package com.dlvn.mcustomerportal.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dlvn.mcustomerportal.database.entity.ClaimEntity;

import java.util.List;

@Dao
public interface ClaimDao {

    @Query("select * from ClaimEntity")
    List<ClaimEntity> getAllClaims();

    @Query("select * from ClaimEntity where mainClaimID  = 0 order by UpdateDate desc")
    List<ClaimEntity> getAllClaimsNoSupplement();

    /**
     * get Claims by claim ID
     * @param ID
     * @return
     */
    @Query("select * from ClaimEntity where id = :ID")
    ClaimEntity getClaimsByID(long ID);

    /**
     * get All Supplement Obj by mainClaimID
     * @param ID: mainClaimID
     * @return
     */
    @Query("select * from ClaimEntity where mainClaimID = :ID")
    List<ClaimEntity> getClaimsSuppByID(long ID);

    /**
     * get all claims by ClaimID of server
     * @param submissID
     * @return
     */
    @Query("select * from ClaimEntity where SubmissionID = :submissID")
    ClaimEntity getClaimsBySubmissID(long submissID);

    @Insert
    long insert(ClaimEntity entity);

    @Update
    int update(ClaimEntity entity);

    @Delete
    int delete(ClaimEntity entity);

    @Query("delete from ClaimEntity")
    int deleteAllClaims();

}

package com.dlvn.mcustomerportal.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dlvn.mcustomerportal.database.entity.DocumentEntity;

import java.util.List;

@Dao
public interface DocumentDao {

    /**
     * get List doc of claim by docID, docID is primary key in table ClaimDoc
     *
     * @param DocEntityID
     * @return
     */
    @Query("SELECT * FROM DocumentEntity where DocEntityID = :DocEntityID")
    List<DocumentEntity> getDocumentByDocTypeID(long DocEntityID);

    @Query("SELECT * FROM DocumentEntity where id = :ID")
    List<DocumentEntity> getDocumentByDocumentID(long ID);

    @Query("SELECT * FROM DocumentEntity where id = :tbDocID AND Status like :statusDoc")
    List<DocumentEntity> getDocumentUploadedByDocTypeID(long tbDocID, String statusDoc);

    @Insert
    long insert(DocumentEntity documentEntity);

    @Delete
    int delete(DocumentEntity documentEntity);

    @Query("delete from DocumentEntity where DocEntityID = :DocEntityID")
    int deleteAllDocByDocTypeID(long DocEntityID);

    @Query("delete from DocumentEntity where id = :ID")
    int deleteDocByDocumentID(long ID);

    @Update
    int update(DocumentEntity documentEntity);

    @Query("DELETE FROM DocumentEntity")
    int deleteAllDocument();
}

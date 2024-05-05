package com.app.assisment.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.assisment.common.Constants.UNIVERSITY_TABLE
import com.app.assisment.data.remote.model.UniversityResponseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(item: List<UniversityResponseItem>)

    @Query("DELETE FROM $UNIVERSITY_TABLE")
    suspend fun deleteData()

    @Query("SELECT * FROM $UNIVERSITY_TABLE")
    fun getAllData(): Flow<List<UniversityResponseItem>>

}
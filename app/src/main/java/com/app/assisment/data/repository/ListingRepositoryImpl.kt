package com.app.assisment.data.repository

import androidx.room.withTransaction
import com.app.assisment.common.DataStatus
import com.app.assisment.common.networkBoundResource
import com.app.assisment.data.db.RoomDatabase
import com.app.assisment.data.remote.ApiService
import com.app.assisment.data.remote.model.UniversityResponseItem
import com.app.assisment.domain.repository.ListingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListingRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val db: RoomDatabase,
) : ListingRepository {

    private val roomDao = db.roomDao()

    override suspend fun getData(hashMap: HashMap<String, String>): Flow<DataStatus<List<UniversityResponseItem>>> {
        return networkBoundResource(
            query = {
                roomDao.getAllData()
            },
            fetch = {
                delay(2000)
                api.getSearchData(hashMap)
            },
            saveFetchResult = { data ->
                db.withTransaction {
                    roomDao.deleteData()
                    roomDao.insertData(data)
                }

            }
        )
    }
}
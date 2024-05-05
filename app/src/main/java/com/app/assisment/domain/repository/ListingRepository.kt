package com.app.assisment.domain.repository

import com.app.assisment.common.DataStatus
import com.app.assisment.data.remote.model.UniversityResponseItem
import kotlinx.coroutines.flow.Flow

interface ListingRepository {
    suspend fun getData(hashMap: HashMap<String, String>): Flow<DataStatus<List<UniversityResponseItem>>>
}
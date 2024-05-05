package com.app.assisment.data.remote

import com.app.assisment.data.remote.model.UniversityResponseItem
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {

    @GET("search")
    suspend fun getSearchData(
        @QueryMap query: Map<String, String>
    ): ArrayList<UniversityResponseItem>
}
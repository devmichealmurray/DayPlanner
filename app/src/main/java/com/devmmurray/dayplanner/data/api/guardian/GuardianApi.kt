package com.devmmurray.dayplanner.data.api.guardian

import com.devmmurray.dayplanner.data.model.dto.news.NewsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  Call to Guardian News
 *      https://content.guardianapis.com/search?
 *      q=
 *      &page-size=24
 *      &from-date=2021-03-07
 *      &api-key=ed41fb6c-28a3-4bd2-989c-ebc34d98f916
 */

interface GuardianApi {

    @GET("/search?")
    suspend fun guardianCall(
        @Query("page-size") pageSize: String,
        @Query("from-date") date: String,
        @Query("api-key") apiKey: String
    ) : Response<NewsDTO>


    @GET("/search")
    suspend fun guardianSearchCall(
        @Query("page-size") pageSize: String,
        @Query("q") searchTerms: String,
        @Query("api-key") apiKey: String
    ) : Response<NewsDTO>
}

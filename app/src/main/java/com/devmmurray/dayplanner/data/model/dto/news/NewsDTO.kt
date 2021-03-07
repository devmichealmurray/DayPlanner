package com.devmmurray.dayplanner.data.model.dto.news

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsDTO(
    @Json(name = "response") val response: Results?
)

@JsonClass(generateAdapter = true)
data class Results(
    @Json(name = "results") val results: List<NewsArticles>?
)

@JsonClass(generateAdapter = true)
data class NewsArticles(
    @Json(name = "sectionName") val sectionName: String?,
    @Json(name = "webPublicationDate") val pubDate: String?,
    @Json(name = "webTitle") val newsTitle: String?,
    @Json(name = "webUrl") val url: String?,
    @Json(name = "pillarName") val pillarName: String?
)
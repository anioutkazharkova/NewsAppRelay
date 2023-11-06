package com.azharkova.newsapprelay.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class NewsItem(
    val author: String?,
    val title: String?, val description: String?,
    val url: String?, val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)

fun NewsItem.toModel(): NewsItemModel {
    return NewsItemModel(title,description,urlToImage, publishedAt, content)
}

@Parcelize
data class NewsItemModel(
    val title: String?, val description: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
) : Parcelable


package com.azharkova.newsapprelay

import androidx.annotation.StringRes

enum class NewsScreen(@StringRes val value: Int) {
    NewsList(R.string.news_list),
    NewsItem(R.string.news_item)
}
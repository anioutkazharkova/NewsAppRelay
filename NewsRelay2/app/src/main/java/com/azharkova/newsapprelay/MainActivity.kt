package com.azharkova.newsapprelay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import com.azharkova.newsapprelay.data.NewsItemModel
import com.azharkova.newsapprelay.data.toModel
import com.azharkova.newsapprelay.newsitemrow.NewsItemRow
import com.azharkova.newsapprelay.newsitemview.NewsItemView
import com.azharkova.newsapprelay.services.NewsService
import com.azharkova.newsapprelay.ui.theme.NewsAppRelayTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           NewsApp()
        }
    }
}

@Composable
fun NewsListScreen(navigate: (NewsItemModel)->Unit) {
    val vm by lazy {
        NewsListVM()
    }
    LaunchedEffect(Unit) {
        vm.loadData()
    }
    val data by vm.data.collectAsState(initial = null)
    LazyColumn {
      items(data.orEmpty()){
          NewsItemRow(
              image = rememberAsyncImagePainter(it.urlToImage),
              title = it.title.orEmpty(),
              text = it.content.orEmpty(),
              date = it.publishedAt.orEmpty(),
              onRowTapped = {navigate.invoke(it)}
          )
      }
    }
}

@Composable
fun NewsItemDataView(viewModel: NewsItemVM) {
    val data by viewModel.model.collectAsState()
    NewsItemView(
        image = rememberAsyncImagePainter(data?.urlToImage),
        title = data?.title.orEmpty(),
        text = data?.content.orEmpty()
    )
}

class NewsItemVM(item: NewsItemModel?): ViewModel() {
    private val _item: MutableStateFlow<NewsItemModel?> = MutableStateFlow<NewsItemModel?>(item)
    val model = _item.asStateFlow()
}

class NewsListVM: ViewModel() {
    var data = MutableSharedFlow<List<NewsItemModel>?>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val service by lazy {
        NewsService()
    }

    fun loadData() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                service.loadNews()
            }
            data.tryEmit(result.articles?.map {
                it.toModel()
            }.orEmpty())
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsAppRelayTheme {
        Greeting("Android")
    }
}
package com.openinapp.task.ui.linkFrag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.openinapp.task.helper.CONSTANTS
import com.openinapp.task.helper.logThis
import com.openinapp.task.model.LinkResponse
import com.openinapp.task.repo.network.LinkApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LinkViewModel
@Inject
constructor(
    private val linkApi: LinkApi,
    @Named(CONSTANTS.API_KEY) private val apiToken: String
) : ViewModel() {

    // greet message
    val greetMessage: String = getGreeting(System.currentTimeMillis())

    // response
    private var _response = MutableLiveData<LinkResponse>()
    val response: LiveData<LinkResponse> get() = _response

    // Line Chart
    private val _lineChartData = MutableLiveData<List<Entry>?>()
    val lineChartData: LiveData<List<Entry>?> get() = _lineChartData

    init {
        getDataFromInternet()
    }

    private fun getDataFromInternet() {
        logThis("Starting Engine ....\n Token : $apiToken")
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    _response.postValue(linkApi.getLinkData(apiToken))
                    logThis("Success getting data ${_response.value?.data?.overallUrlChart}")
                    sortLineData()
                }
            } catch (e: Exception) {
                logThis(e.message.toString())
            }
        }
    }

    private fun sortLineData() {
        val overallUrlChart = _response.value?.data?.overallUrlChart
        if (overallUrlChart != null) {
            val data = overallUrlChart
                .filter { entry ->
                    entry.key.startsWith("2024-01") || entry.key.startsWith("2024-02")
                }
                .map { (date, count) ->
                    Entry(
                        date.substring(8).toFloat(),
                        count.toFloat()
                    )
                }
            _lineChartData.postValue(data)
            logThis("Chart data $data")
        } else {
            logThis("OverallUrlChart data is null")
        }
    }


    /* Greet message function*/
    private fun getGreeting(timeInMillis: Long): String {
        val dateFormat = SimpleDateFormat("HH", Locale.getDefault())
        return when (dateFormat.format(Date(timeInMillis)).toInt()) {
            in 6..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
}
package com.openinapp.task.ui.linkFrag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openinapp.task.helper.CONSTANTS
import com.openinapp.task.helper.logThis
import com.openinapp.task.model.LinkResponse
import com.openinapp.task.repo.network.LinkApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    fun getDataFromInternet() {
        logThis("Starting Engine ....\n Token : $apiToken")
        viewModelScope.launch {
            try {
                _response.postValue(linkApi.getLinkData(apiToken))
                logThis("Success getting data ${_response.value?.statusCode}")
            } catch (e: Exception) {
                logThis(e.message.toString())
            }
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
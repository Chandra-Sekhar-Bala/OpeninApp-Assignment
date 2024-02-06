package com.openinapp.task.ui.linkFrag

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openinapp.task.helper.CONSTANTS
import com.openinapp.task.helper.logthis
import com.openinapp.task.model.LinkResponse
import com.openinapp.task.repo.network.LinkApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LinkViewModel
@Inject
constructor(
    private val linkApi: LinkApi,
    @Named(CONSTANTS.API_KEY) private val apiToken: String
) : ViewModel() {

    private var _response = MutableLiveData<LinkResponse>()
    val response: LiveData<LinkResponse> get() = _response

    init {

    }

    fun getDataFromInternet() {
        logthis("Starting Engine ....\n Token : $apiToken")
        viewModelScope.launch {
            try {
                val data = linkApi.getLinkData(apiToken)
                logthis(data.toString())
            } catch (e: Exception) {
                logthis(e.message.toString())
            }
        }
    }
}
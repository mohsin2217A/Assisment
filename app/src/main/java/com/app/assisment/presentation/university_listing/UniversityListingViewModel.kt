package com.app.assisment.presentation.university_listing

import com.app.assisment.common.DataStatus
import com.app.assisment.core.dispatchers.Dispatcher
import com.app.assisment.core.viewmodel.BaseViewModel
import com.app.assisment.data.remote.model.UniversityResponseItem
import com.app.assisment.domain.use_case.get_university_listing.GetUniversityListingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UniversityListingViewModel @Inject constructor(
    private val getUniversityListingUseCase: GetUniversityListingUseCase,
    appDispatcher: Dispatcher
) : BaseViewModel(appDispatcher) {

    private val _list =
        MutableStateFlow<DataStatus<List<UniversityResponseItem>>>(DataStatus.Loading())
    val list = _list.asStateFlow()

    init {
        getUniversityListing()
    }

    fun getUniversityListing() {
        launchOnMain {
            val hashMap = HashMap<String, String>()
            hashMap["country"] = "United Arab Emirates"
            getUniversityListingUseCase(hashMap).collect {
                _list.value = it
            }
        }
    }
}
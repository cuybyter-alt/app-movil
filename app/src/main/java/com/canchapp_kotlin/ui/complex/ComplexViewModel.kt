package com.canchapp_kotlin.ui.complex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canchapp_kotlin.data.network.ComplexListResponse
import com.canchapp_kotlin.data.repository.ComplexRepository
import com.canchapp_kotlin.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ComplexViewModel : ViewModel() {

    private val repository = ComplexRepository()

    private val _complexesState = MutableStateFlow<Resource<ComplexListResponse>?>(null)
    val complexesState = _complexesState.asStateFlow()

    init {
        loadComplexes()
    }

    fun loadComplexes(page: Int = 1) {
        viewModelScope.launch {
            _complexesState.value = Resource.Loading
            _complexesState.value = repository.getComplexes(page)
        }
    }
}

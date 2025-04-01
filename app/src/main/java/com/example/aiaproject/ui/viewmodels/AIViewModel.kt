package com.example.aiaproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aiaproject.data.model.Message
import com.example.aiaproject.data.network.ApiResponse
import com.example.aiaproject.domain.AIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AIViewModel @Inject constructor(private val usecase: AIUseCase) : ViewModel() {

    private val _homeUIState = MutableStateFlow<ApiResponse<List<Message>>>(ApiResponse.Empty)
    val homeUIState = _homeUIState.asStateFlow()

    fun askAI(content:String) {
        viewModelScope.launch {
            usecase.invoke(content).collect {
                _homeUIState.value = it
            }
        }
    }
}
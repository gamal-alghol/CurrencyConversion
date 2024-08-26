package com.gamal.currencyconversion.ui.HomeScreen.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VarViewModel : ViewModel() {
    private val _bottomSheetOpenType = MutableStateFlow("")
    private val _editCurrency = MutableStateFlow(0)
    private val _size = MutableStateFlow(0)
    val bottomSheetOpenType: StateFlow<String> = _bottomSheetOpenType
    val editCurrency: StateFlow<Int> = _editCurrency
    val size: StateFlow<Int> = _size

    fun setBottomSheetOpenType(type: String) {
        _bottomSheetOpenType.value = type
    }

    fun setEditCurrency(type: Int) {
        _editCurrency.value = type
    }

    fun setSize(type: Int) {
        _size.value = type
    }
}
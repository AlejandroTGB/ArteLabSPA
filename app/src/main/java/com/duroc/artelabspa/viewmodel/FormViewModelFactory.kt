package com.duroc.artelabspa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duroc.artelabspa.repository.ProductoRepository

@Suppress("UNCHECKED_CAST")
class FormViewModelFactory(private val repository: ProductoRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormViewModel::class.java)) {
            return FormViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
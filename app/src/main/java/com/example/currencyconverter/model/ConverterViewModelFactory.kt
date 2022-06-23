package com.example.currencyconverter.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConverterViewModelFactory (private val repository: ConversionRepo): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ConverterViewModel(repository) as T
    }

}
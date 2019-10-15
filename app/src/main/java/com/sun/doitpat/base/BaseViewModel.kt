package com.sun.doitpat.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    private val errorMessage = MutableLiveData<String>()

    private fun showError(e: Throwable) {
        errorMessage.value = e.message
    }

}

package com.brsan7.imc.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoricoEditViewModel : ViewModel() {
    var heObservacao = MutableLiveData<String>().apply{value = ""}
    var fstScan: Boolean = true
}
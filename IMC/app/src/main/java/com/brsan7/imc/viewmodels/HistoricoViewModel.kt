package com.brsan7.imc.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brsan7.imc.adapter.HistoricoAdapter

class HistoricoViewModel : ViewModel() {
    var hAdapter = MutableLiveData<HistoricoAdapter?>().apply { value = null }
    var fstScan: Boolean = true
}
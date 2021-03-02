package com.brsan7.imc.application

import android.app.Application
import com.brsan7.imc.helper.HelperDB

class HistoricoApplication : Application(){

    var helperDB : HelperDB? = null
        private set

    companion object{
        lateinit var instance: HistoricoApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(this)
    }
}
package com.brsan7.imc

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.brsan7.imc.R.layout
import com.brsan7.imc.R.string
import com.brsan7.imc.application.HistoricoApplication
import com.brsan7.imc.model.HistoricoVO
import com.brsan7.imc.model.RecursosStrings
import com.brsan7.imc.viewmodels.MainViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity() {

    lateinit var mViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setupToolBar(toolBar, getString(string.mainTitulo), false)
        setListeners()
        setUltimoRegistro()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menuHistorico ->{
                val intent = Intent(this, HistoricoActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchtUltimoRegistro(ultimoReg: HistoricoVO){
        getInstanceSharedPreferences().edit(){
            putString("ultimoReg",Gson().toJson(ultimoReg))
            commit()
        }
    }

    private fun getInstanceSharedPreferences() : SharedPreferences{
        return getSharedPreferences("com.brsan7.imc.ULTIMO_REGISTRO", Context.MODE_PRIVATE)
    }

    private fun getSharedUltimoRegistro() : HistoricoVO{
        val defHist = HistoricoVO(
                        id = 0,
                        data = "",
                        hora = "",
                        peso = "",
                        altura = "",
                        genero = "Masculino",
                        observacao = ""
                )
        val ultimoItemRegGson = getInstanceSharedPreferences().getString("ultimoReg",Gson().toJson(defHist))
        val convTipo = object : TypeToken<HistoricoVO>(){}.type
        return Gson().fromJson(ultimoItemRegGson,convTipo)
    }

    private fun setUltimoRegistro(){
        val ultimoItemReg:HistoricoVO = getSharedUltimoRegistro()
        etPeso.setText(ultimoItemReg.peso)
        etAltura.setText(ultimoItemReg.altura)
        if(ultimoItemReg.genero == getString(R.string.seletorGenCMP)
                && mViewModel.fstScan) {

            seletorGen.performClick()
        }
    }

    private fun setListeners(){
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        sendStringRes()
        updateViewModel()
        calcularBt.setOnClickListener {
            validarCalculo()
        }
        seletorGen.setOnClickListener {
            mViewModel.selecionaGenero(seletorGen.text.toString())
            updateViewModel()
        }
    }

    private fun sendStringRes(){
        val recursos = RecursosStrings(
                str1_1 = getString(string.abaixoPeso),
                str2_1 = getString(string.pesoIdeal),
                str3_1 = getString(string.acimaPeso),
                str4_1 = getString(string.obesidade1),
                str5_1 = getString(string.obesidade2),
                str6_1 = getString(string.obesidade3),
                str2 = getString(string.falta),
                str3 = getString(string.kg),
                str4 = getString(string.para),
                str5 = getString(string.calcPesoIdealBaixo),
                str6 = getString(string.e),
                str7 = getString(R.string.seletorGenCMP)
        )
        mViewModel.getStringRes(recursos)
    }

    private fun updateViewModel(){
        mViewModel.mResultadoImc.observe(this, androidx.lifecycle.Observer { valor->
            tvResultadoImc.text = valor
        })
        mViewModel.mClassificacaoImc.observe(this, androidx.lifecycle.Observer { valor->
            tvClassificacaoImc.text = valor
        })
        mViewModel.mFaixaPeso.observe(this, androidx.lifecycle.Observer { valor->
            tvFaixaPeso.text = valor
        })
        mViewModel.mSilhueta.observe(this, androidx.lifecycle.Observer { valor->
            ivSilhueta.setImageResource(valor)
        })
    }

    private fun validarCalculo(){
        val peso = etPeso.text.toString().toFloatOrNull()
        val altura = etAltura.text.toString().toFloatOrNull()

        if (peso != null && altura != null){
            mViewModel.calcularImc(peso,altura,seletorGen.text.toString())
            updateViewModel()
            val date = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val itemHist = HistoricoVO(
                    altura = altura.toString(),
                    data = dateFormat.format(date),
                    hora = timeFormat.format(date),
                    peso = peso.toString(),
                    genero = seletorGen.text.toString(),
                    observacao = ""
            )
            pbMain.visibility = View.VISIBLE
            Thread(Runnable {
                HistoricoApplication.instance.helperDB?.salvarRegistro(itemHist)
                runOnUiThread {
                    pbMain.visibility = View.GONE
                }
            }).start()
            fetchtUltimoRegistro(itemHist)
        }
        else{Toast.makeText(this, getString(R.string.msgToastMain), Toast.LENGTH_SHORT).show()}
    }
}
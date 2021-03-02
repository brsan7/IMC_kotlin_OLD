package com.brsan7.imc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.brsan7.imc.R.layout
import com.brsan7.imc.R.string
import com.brsan7.imc.application.HistoricoApplication
import com.brsan7.imc.model.HistoricoVO
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setupToolBar(toolBar, getString(string.mainTitulo),false)
        setListeners()
        setUltimoRegistro()
    }

    private fun setUltimoRegistro(){
        var lista = HistoricoApplication.instance.helperDB?.buscarContatos("",false)
        var itemHist = lista?.let { lista.getOrNull(it.lastIndex) } ?: return
        etPeso.setText(itemHist.peso)
        etAltura.setText(itemHist.altura)
        seletorGen.setText(itemHist.genero)
        selecionaGenero()
    }

    private fun setListeners(){
        calcularBt.setOnClickListener {
            calcularImc(etPeso.text.toString(), etAltura.text.toString())
        }
        seletorGen.setOnClickListener {
            selecionaGenero()
        }
        fab.setOnClickListener { onClickAdd() }
    }

    private fun selecionaGenero(){
        if(seletorGen.text==getString(string.seletorGenCMP)){imagemResult.setImageResource(R.drawable.f2)}
        else{imagemResult.setImageResource(R.drawable.m2)}
        faixaPesoTv.text = getString(string.peso_ideal)
        resultadoFaixaPesoTv.text = ""
        resultadoTv.text = ""
    }

    private fun onClickAdd(){
        val intent = Intent(this,HistoricoActivity::class.java)
        startActivity(intent)
    }

    private fun calcularImc(pesoIn: String, alturaIn: String){
        val peso = pesoIn.toFloatOrNull()
        val altura = alturaIn.toFloatOrNull()
        var formatar = DecimalFormat(".##")

        if (peso!=null && altura!=null) {
            val imc = peso / (altura * altura)
            val pesoIdealIni=18.0*(altura*altura)
            val pesoIdealFim=25.0*(altura*altura)
            val pesoSobrando=peso-pesoIdealFim
            val pesoFaltando=pesoIdealIni-peso
            var msgTrasf:String

            msgTrasf="IMC: "+formatar.format(imc)
            resultadoTv.text = msgTrasf

            when {
                imc<18.5->{
                    msgTrasf = getString(string.abaixoPeso)+getString(string.falta)+formatar.format(pesoFaltando)+getString(string.kg)+getString(string.para)
                    faixaPesoTv.text = msgTrasf
                    msgTrasf = getString(string.calcPesoIdealCima)+formatar.format(pesoIdealIni)+getString(string.e)+formatar.format(pesoIdealFim)+getString(string.kg)
                    resultadoFaixaPesoTv.text = msgTrasf
                    if(seletorGen.text == getString(string.seletorGenCMP)){imagemResult.setImageResource(R.drawable.f1)}
                    else{imagemResult.setImageResource(R.drawable.m1)}
                }
                imc>=18.5&&imc<25->{
                    faixaPesoTv.text = getString(string.pesoIdeal)
                    msgTrasf = getString(string.calcPesoIdealBaixo)+formatar.format(pesoIdealIni)+getString(string.e)+formatar.format(pesoIdealFim)+getString(string.kg)
                    resultadoFaixaPesoTv.text = msgTrasf
                    if(seletorGen.text == getString(string.seletorGenCMP)){imagemResult.setImageResource(R.drawable.f2)}
                    else{imagemResult.setImageResource(R.drawable.m2)}
                }
                imc>=25.0&&imc<30->{
                    msgTrasf = getString(string.acimaPeso)+getString(string.falta)+formatar.format(pesoSobrando)+getString(string.kg)+getString(string.para)
                    faixaPesoTv.text = msgTrasf
                    msgTrasf = getString(string.calcPesoIdealBaixo)+formatar.format(pesoIdealIni)+getString(string.e)+formatar.format(pesoIdealFim)+getString(string.kg)
                    resultadoFaixaPesoTv.text = msgTrasf
                    if(seletorGen.text == getString(string.seletorGenCMP)){imagemResult.setImageResource(R.drawable.f3)}
                    else{imagemResult.setImageResource(R.drawable.m3)}
                }
                imc>=30.0&&imc<35->{
                    msgTrasf = getString(string.obesidade1)+getString(string.falta)+formatar.format(pesoSobrando)+getString(string.kg)+getString(string.para)
                    faixaPesoTv.text = msgTrasf
                    msgTrasf = getString(string.calcPesoIdealBaixo)+formatar.format(pesoIdealIni)+getString(string.e)+formatar.format(pesoIdealFim)+getString(string.kg)
                    resultadoFaixaPesoTv.text = msgTrasf
                    if(seletorGen.text == getString(string.seletorGenCMP)){imagemResult.setImageResource(R.drawable.f4)}
                    else{imagemResult.setImageResource(R.drawable.m4)}
                }
                imc>=35.0&&imc<40->{
                    msgTrasf = getString(string.obesidade2)+getString(string.falta)+formatar.format(pesoSobrando)+getString(string.kg)+getString(string.para)
                    faixaPesoTv.text = msgTrasf
                    msgTrasf = getString(string.calcPesoIdealBaixo)+formatar.format(pesoIdealIni)+getString(string.e)+formatar.format(pesoIdealFim)+getString(string.kg)
                    resultadoFaixaPesoTv.text = msgTrasf
                    if(seletorGen.text == getString(string.seletorGenCMP)){imagemResult.setImageResource(R.drawable.f5)}
                    else{imagemResult.setImageResource(R.drawable.m5)}
                }
                imc>=40.0->{
                    msgTrasf = getString(string.obesidade3)+getString(string.falta)+formatar.format(pesoSobrando)+getString(string.kg)+getString(string.para)
                    faixaPesoTv.text = msgTrasf
                    msgTrasf = getString(string.calcPesoIdealBaixo)+formatar.format(pesoIdealIni)+getString(string.e)+formatar.format(pesoIdealFim)+getString(string.kg)
                    resultadoFaixaPesoTv.text = msgTrasf
                    if(seletorGen.text == getString(string.seletorGenCMP)){imagemResult.setImageResource(R.drawable.f5)}
                    else{imagemResult.setImageResource(R.drawable.m5)}
                }
            }
        }
        else{
            Toast.makeText(this, getString(string.msgToast), Toast.LENGTH_SHORT).show()}

        val date = Calendar.getInstance().time
        var dateTimeFormat = SimpleDateFormat("dd/MM/yyyy_HH:mm:ss", Locale.getDefault())
        val itemHist = HistoricoVO(
            data = dateTimeFormat.format(date),
            peso = formatar.format(peso),
            altura = formatar.format(altura),
            genero = seletorGen.text.toString(),
            observacao = ""
        )
        pbMain.visibility = View.VISIBLE
        Thread(Runnable {
            Thread.sleep(500)//retorno visual de acesso e alteração no Banco de Dados
            HistoricoApplication.instance.helperDB?.salvarContato(itemHist)
            runOnUiThread {
                pbMain.visibility = View.GONE
            }
        }).start()
    }
}
package com.brsan7.imc

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brsan7.imc.R.layout
import com.brsan7.imc.R.string
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setListeners()
    }

    private fun setListeners(){
        calcularBt.setOnClickListener {
            calcularImc(pesoTed.text.toString(), alturaTed.text.toString())
        }
        seletorGen.setOnClickListener {
            if(seletorGen.text=="Feminino"){imagemResult.setImageResource(R.drawable.f2)}
            else{imagemResult.setImageResource(R.drawable.m2)}
            faixaPesoTv.text = getString(string.peso_ideal)
            resultadoFaixaPesoTv.text = ""
        }

    }

    private fun calcularImc(pesoIn: String, alturaIn: String){
        val peso = pesoIn.toFloatOrNull()
        val altura = alturaIn.toFloatOrNull()


        if (peso!=null && altura!=null) {
            val imc = peso / (altura * altura)
            val pesoIdealIni=18.0*(altura*altura)
            val pesoIdealFim=25.0*(altura*altura)
            val pesoSobrando=peso-pesoIdealFim
            val pesoFaltando=pesoIdealIni-peso
            var msgTrasf:String
            var formatar = DecimalFormat(".##")
            msgTrasf="IMC: "+formatar.format(imc)
            resultadoTv.text = msgTrasf

            when {
                imc<18.5->{
                    msgTrasf = getString(string.abaixoPeso)+getString(string.falta)+formatar.format(pesoFaltando)+getString(string.kg)+getString(string.para)
                    faixaPesoTv.text = msgTrasf
                    msgTrasf = getString(string.calcPesoIdealCima)+formatar.format(pesoIdealIni)+getString(string.e)+formatar.format(pesoIdealFim)+getString(string.kg)
                    resultadoFaixaPesoTv.text = msgTrasf
                    if(seletorGen.text == "Feminino"){imagemResult.setImageResource(R.drawable.f1)}
                    else{imagemResult.setImageResource(R.drawable.m1)}
                }
                imc>=18.5&&imc<25->{
                    faixaPesoTv.text = getString(string.pesoIdeal)
                    msgTrasf = getString(string.calcPesoIdealBaixo)+formatar.format(pesoIdealIni)+getString(string.e)+formatar.format(pesoIdealFim)+getString(string.kg)
                    resultadoFaixaPesoTv.text = msgTrasf
                    if(seletorGen.text == "Feminino"){imagemResult.setImageResource(R.drawable.f2)}
                    else{imagemResult.setImageResource(R.drawable.m2)}
                }
                imc>=25.0&&imc<30->{
                    msgTrasf = getString(string.acimaPeso)+getString(string.falta)+formatar.format(pesoSobrando)+getString(string.kg)+getString(string.para)
                    faixaPesoTv.text = msgTrasf
                    msgTrasf = getString(string.calcPesoIdealBaixo)+formatar.format(pesoIdealIni)+getString(string.e)+formatar.format(pesoIdealFim)+getString(string.kg)
                    resultadoFaixaPesoTv.text = msgTrasf
                    if(seletorGen.text == "Feminino"){imagemResult.setImageResource(R.drawable.f3)}
                    else{imagemResult.setImageResource(R.drawable.m3)}
                }
                imc>=30.0&&imc<35->{
                    msgTrasf = getString(string.obesidade1)+getString(string.falta)+formatar.format(pesoSobrando)+getString(string.kg)+getString(string.para)
                    faixaPesoTv.text = msgTrasf
                    msgTrasf = getString(string.calcPesoIdealBaixo)+formatar.format(pesoIdealIni)+getString(string.e)+formatar.format(pesoIdealFim)+getString(string.kg)
                    resultadoFaixaPesoTv.text = msgTrasf
                    if(seletorGen.text == "Feminino"){imagemResult.setImageResource(R.drawable.f4)}
                    else{imagemResult.setImageResource(R.drawable.m4)}
                }
                imc>=35.0&&imc<40->{
                    msgTrasf = getString(string.obesidade2)+getString(string.falta)+formatar.format(pesoSobrando)+getString(string.kg)+getString(string.para)
                    faixaPesoTv.text = msgTrasf
                    msgTrasf = getString(string.calcPesoIdealBaixo)+formatar.format(pesoIdealIni)+getString(string.e)+formatar.format(pesoIdealFim)+getString(string.kg)
                    resultadoFaixaPesoTv.text = msgTrasf
                    if(seletorGen.text == "Feminino"){imagemResult.setImageResource(R.drawable.f5)}
                    else{imagemResult.setImageResource(R.drawable.m5)}
                }
                imc>=40.0->{
                    msgTrasf = getString(string.obesidade3)+getString(string.falta)+formatar.format(pesoSobrando)+getString(string.kg)+getString(string.para)
                    faixaPesoTv.text = msgTrasf
                    msgTrasf = getString(string.calcPesoIdealBaixo)+formatar.format(pesoIdealIni)+getString(string.e)+formatar.format(pesoIdealFim)+getString(string.kg)
                    resultadoFaixaPesoTv.text = msgTrasf
                    if(seletorGen.text == "Feminino"){imagemResult.setImageResource(R.drawable.f5)}
                    else{imagemResult.setImageResource(R.drawable.m5)}
                }
            }
        }
        else{
            Toast.makeText(this, getString(string.msgToast), Toast.LENGTH_SHORT).show()}

    }
}
package com.brsan7.imc

import android.os.Bundle
import android.view.View
import com.brsan7.imc.application.HistoricoApplication
import com.brsan7.imc.model.HistoricoVO
import kotlinx.android.synthetic.main.activity_historico_edit.*

class HistoricoEditActivity : BaseActivity() {

    private var idHistorico: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico_edit)
        setupToolBar(toolBar, getString(R.string.historicoEditTitulo),true)
        setupContato()
        btnSalvarConato.setOnClickListener { onClickSalvarContato() }
    }

    private fun setupContato(){
        tvDataEdit.text = ""
        tvPesoEdit.text = ""
        tvAlturaEdit.text = ""
        tvGeneroEdit.text = ""
        idHistorico = intent.getIntExtra("index",-1)
        if (idHistorico == -1){
            btnExcluirContato.visibility = View.GONE
            return
        }
        pbHistoricoEdit.visibility = View.VISIBLE
        Thread(Runnable {
            Thread.sleep(500)//retorno visual de acesso e alteração no Banco de Dados
            var lista = HistoricoApplication.instance.helperDB?.buscarRegistros("$idHistorico",false) ?: return@Runnable
            var itemHist = lista.getOrNull(0) ?: return@Runnable
            runOnUiThread {
                tvDataEdit.setText(itemHist.data)
                tvPesoEdit.setText(itemHist.peso)
                tvAlturaEdit.setText(itemHist.altura)
                tvGeneroEdit.setText(itemHist.genero)
                etObservacaoEdit.setText(itemHist.observacao)
                pbHistoricoEdit.visibility = View.GONE
            }
        }).start()
    }

    private fun onClickSalvarContato(){
        val data = tvDataEdit.text.toString()
        val peso = tvPesoEdit.text.toString()
        val altura = tvAlturaEdit.text.toString()
        val genero = tvGeneroEdit.text.toString()
        val observacao = etObservacaoEdit.text.toString()
        val itemHist = HistoricoVO(
                idHistorico,
                data,
                peso,
                altura,
                genero,
                observacao
        )
        pbHistoricoEdit.visibility = View.VISIBLE
        Thread(Runnable {
            Thread.sleep(500)//retorno visual de acesso e alteração no Banco de Dados
            HistoricoApplication.instance.helperDB?.updateRegistro(itemHist)
            runOnUiThread {
                finish()
                pbHistoricoEdit.visibility = View.GONE
            }
        }).start()
    }

    fun onClickExcluirContato(view: View) {
        if(idHistorico > -1){
            pbHistoricoEdit.visibility = View.VISIBLE
            Thread(Runnable {
                Thread.sleep(500)//retorno visual de acesso e alteração no Banco de Dados
                HistoricoApplication.instance.helperDB?.deletarRegistro(idHistorico)
                runOnUiThread {
                    finish()
                    pbHistoricoEdit.visibility = View.GONE
                }
            }).start()
        }
    }
}
package com.brsan7.imc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.brsan7.imc.application.HistoricoApplication
import com.brsan7.imc.model.HistoricoVO
import com.brsan7.imc.viewmodels.HistoricoEditViewModel
import kotlinx.android.synthetic.main.activity_historico_edit.*
import kotlinx.android.synthetic.main.activity_historico_edit.toolBar
import kotlinx.android.synthetic.main.activity_main.*

class HistoricoEditActivity : BaseActivity() {

    lateinit var heViewModel: HistoricoEditViewModel
    private var idHistorico: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico_edit)
        setupToolBar(toolBar, getString(R.string.historicoEditTitulo),true)
        setupRegistro()
        btnSalvarRegistro.setOnClickListener { onClickSalvarRegistro() }
    }

    override fun onPause() {
        super.onPause()
        heViewModel.heObservacao.value = etObservacaoEdit.text.toString()
    }

    private fun setupRegistro(){
        heViewModel = ViewModelProvider(this).get(HistoricoEditViewModel::class.java)
        idHistorico = intent.getIntExtra("index",-1)
        pbHistoricoEdit.visibility = View.VISIBLE
        Thread(Runnable {
            val lista = HistoricoApplication.instance.helperDB?.buscarRegistros("$idHistorico",false) ?: return@Runnable
            val itemHist = lista.getOrNull(0) ?: return@Runnable
            runOnUiThread {
                tvDataEdit.setText(itemHist.data)
                tvPesoEdit.setText(itemHist.peso)
                tvAlturaEdit.setText(itemHist.altura)
                tvGeneroEdit.setText(itemHist.genero)
                if (heViewModel.fstScan) {
                    heViewModel.heObservacao.value = itemHist.observacao
                }
                heViewModel.fstScan = false
                pbHistoricoEdit.visibility = View.GONE
            }
        }).start()
        heViewModel.heObservacao.observe(this, androidx.lifecycle.Observer { valor->
            etObservacaoEdit.setText(valor)
        })

    }

    private fun onClickSalvarRegistro(){
        val itemHist = HistoricoVO(
                id = idHistorico,
                data = tvDataEdit.text.toString(),
                peso = tvPesoEdit.text.toString(),
                altura = tvAlturaEdit.text.toString(),
                genero = tvGeneroEdit.text.toString(),
                observacao = etObservacaoEdit.text.toString()
        )
        pbHistoricoEdit.visibility = View.VISIBLE
        Thread(Runnable {
            HistoricoApplication.instance.helperDB?.updateRegistro(itemHist)
            runOnUiThread {
                finish()
                pbHistoricoEdit.visibility = View.GONE
            }
        }).start()
    }

    fun onClickExcluirRegistro(view: View) {
        pbHistoricoEdit.visibility = View.VISIBLE
        Thread(Runnable {
            HistoricoApplication.instance.helperDB?.deletarRegistro(idHistorico)
            runOnUiThread {
                pbHistoricoEdit.visibility = View.GONE
            }
        }).start()
        Toast.makeText(this,getString(R.string.msgToastHistoricoEdit), Toast.LENGTH_SHORT).show()
        val intent = Intent(this, HistoricoActivity::class.java)
        startActivity(intent)
    }
}
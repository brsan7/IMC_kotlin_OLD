package com.brsan7.imc

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.brsan7.imc.adapter.HistoricoAdapter
import com.brsan7.imc.application.HistoricoApplication
import com.brsan7.imc.model.HistoricoVO
import kotlinx.android.synthetic.main.activity_historico.*

class HistoricoActivity : BaseActivity() {

    private var adapter: HistoricoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)
        setupToolBar(toolBar, getString(R.string.historicoTitulo),true)
        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        onClickBuscar()
    }

    private fun onClickItemRecyclerView(index: Int){
        val intent = Intent(this,HistoricoEditActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }

    private fun onClickBuscar(){
        var listaFiltrada: List<HistoricoVO> = mutableListOf()
        pbHistorico.visibility = View.VISIBLE
        Thread(Runnable {
            Thread.sleep(500)//retorno visual de acesso e alteração no Banco de Dados
            try {
                listaFiltrada = HistoricoApplication.instance.helperDB?.buscarContatos("",false) ?: mutableListOf()
            }catch (ex: Exception){
                ex.printStackTrace()
            }
            runOnUiThread {
                adapter = HistoricoAdapter(this,listaFiltrada) {onClickItemRecyclerView(it)}
                recyclerView.adapter = adapter
                pbHistorico.visibility = View.GONE
            }
        }).start()
    }
}
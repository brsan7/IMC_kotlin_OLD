package com.brsan7.imc

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.brsan7.imc.adapter.HistoricoAdapter
import com.brsan7.imc.application.HistoricoApplication
import com.brsan7.imc.model.HistoricoVO
import com.brsan7.imc.viewmodels.HistoricoViewModel
import kotlinx.android.synthetic.main.activity_historico.*

class HistoricoActivity : BaseActivity() {

    lateinit var hViewModel : HistoricoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)
        setupToolBar(toolBar, getString(R.string.historicoTitulo),true)
        setupRecyclerView()
        onClickBuscar("",false)
        onClickBuscaData()
        calBusca.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_busca, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.menuFiltro ->{
                calBusca.visibility = View.VISIBLE
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        hViewModel = ViewModelProvider(this).get(HistoricoViewModel::class.java)
    }

    private fun onClickItemRecyclerView(index: Int){
        val intent = Intent(this,HistoricoEditActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }

    private fun onClickBuscar(busca:String, isBuscaPorData:Boolean){
        if(hViewModel.fstScan || isBuscaPorData) {
            var listaFiltrada: List<HistoricoVO> = mutableListOf()
            hViewModel.fstScan = false
            pbHistorico.visibility = View.VISIBLE
            Thread(Runnable {
                try {
                    listaFiltrada = HistoricoApplication.instance.helperDB?.buscarRegistros(busca, isBuscaPorData)
                            ?: mutableListOf()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                runOnUiThread {
                    hViewModel.hAdapter.value = HistoricoAdapter(this, listaFiltrada) { onClickItemRecyclerView(it) }
                    if (hViewModel.hAdapter.value?.itemCount ?: 0 > 4 ) {
                        calBusca.visibility = View.GONE
                    }
                    pbHistorico.visibility = View.GONE
                }
            }).start()
        }
        hViewModel.hAdapter.observe(this, androidx.lifecycle.Observer { valor->
            recyclerView.adapter = valor
        })
    }

    fun onClickBuscaData() {
        calBusca.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, ano, mes, dia ->
                onClickBuscar("$dia/${mes + 1}/$ano", true)
            Toast.makeText(this,getString(R.string.msgToastHistorico)+"$dia/${mes + 1}/$ano",Toast.LENGTH_SHORT).show()
        })
    }
}


package com.brsan7.imc

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
    lateinit var adapter : HistoricoAdapter
    lateinit var lstfiltrada : List<HistoricoVO>
    var buscaPorData: Boolean = false

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
        val fragment = HistoricoEditDialog.newInstance(index.toLong())
        fragment.show(supportFragmentManager, "dialog")
    }

    private fun onClickBuscar(busca:String, isBuscaPorData:Boolean){
        buscaPorData = isBuscaPorData
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
                    lstfiltrada = listaFiltrada
                    adapter = HistoricoAdapter(this,lstfiltrada ,object : HistoricoClickedListener{
                        override fun historicoClickedItem(index: Int) {onClickItemRecyclerView(index)}
                        override fun historicoRemoveItem(index: Int) {onClickExcluir(listaFiltrada.first().data,index)}
                    })
                    hViewModel.hAdapter.value = adapter
                    if (hViewModel.hAdapter.value?.itemCount ?: 0 > 3 ) {
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
            Toast.makeText(this,getString(R.string.msgToastHistoricoBusca)+"$dia/${mes + 1}/$ano",Toast.LENGTH_SHORT).show()
        })
    }
    private fun onClickExcluir(dataSelecionada: String,index: Int){
        pbHistorico.visibility = View.VISIBLE
        Thread(Runnable {
            HistoricoApplication.instance.helperDB?.deletarRegistro(index)
            runOnUiThread {
                if (buscaPorData) {
                    onClickBuscar(dataSelecionada, true)
                }
                else{
                    onClickBuscar("", true)
                }
                pbHistorico.visibility = View.GONE
            }
        }).start()
    }
}


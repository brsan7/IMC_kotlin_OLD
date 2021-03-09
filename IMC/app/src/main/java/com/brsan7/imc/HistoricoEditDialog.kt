package com.brsan7.imc

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View.LAYOUT_DIRECTION_INHERIT
import android.view.View.LAYOUT_DIRECTION_RTL
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.brsan7.imc.application.HistoricoApplication
import com.brsan7.imc.model.HistoricoVO
import com.brsan7.imc.viewmodels.HistoricoEditViewModel
import kotlinx.android.synthetic.main.activity_historico_edit.*
//import kotlinx.android.synthetic.main.activity_historico_edit.toolBar
import kotlinx.android.synthetic.main.activity_main.*

class HistoricoEditDialog : DialogFragment(), DialogInterface.OnClickListener {

    lateinit var heViewModel: HistoricoEditViewModel
    private var idHistorico: Int = 0
    lateinit var tvDataDiag: TextView
    lateinit var tvHoraDiag: TextView
    lateinit var tvPesoDiag: TextView
    lateinit var tvAlturaDiag: TextView
    lateinit var tvAGeneroDiag: TextView
    lateinit var etObservacaoDiag: EditText

    companion object{
        private const val EXTRA_ID = "id"

        fun newInstance(id: Long): HistoricoEditDialog{
            val bundle = Bundle()
            bundle.putLong(EXTRA_ID, id)
            val historicoEditFragment = HistoricoEditDialog()
            historicoEditFragment.arguments = bundle
            return historicoEditFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.activity_historico_edit, null)
        idHistorico = arguments?.getLong(EXTRA_ID)?.toInt() ?: 0
        tvDataDiag = view?.findViewById(R.id.tvDataEdit) as TextView
        tvHoraDiag = view?.findViewById(R.id.tvHoraEdit) as TextView
        tvPesoDiag = view?.findViewById(R.id.tvPesoEdit) as TextView
        tvAlturaDiag = view?.findViewById(R.id.tvAlturaEdit) as TextView
        tvAGeneroDiag = view?.findViewById(R.id.tvGeneroEdit) as TextView
        etObservacaoDiag = view?.findViewById(R.id.etObservacaoEdit) as EditText
        setupRegistro()
        return AlertDialog.Builder(activity as Activity)
                .setTitle(getString(R.string.historicoEditTitulo))
                .setView(view)
                .setNeutralButton(getString(R.string.btnVoltarHistoricoEdit),this)
                .setPositiveButton(getString(R.string.btnSalvarHistoricoEdit),this)
                .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        if(which==-1){onClickSalvarRegistro()}
    }

    override fun onPause() {
        super.onPause()
        heViewModel.heObservacao.value = etObservacaoDiag.text.toString()
    }

    private fun setupRegistro(){
        heViewModel = ViewModelProvider(this).get(HistoricoEditViewModel::class.java)
        Thread(Runnable {
            val lista = HistoricoApplication.instance.helperDB?.buscarRegistros("$idHistorico",false) ?: return@Runnable
            val itemHist = lista.getOrNull(0) ?: return@Runnable
            tvDataDiag.setText(itemHist.data)
            tvHoraDiag.setText(itemHist.hora)
            tvPesoDiag.setText(itemHist.peso)
            tvAlturaDiag.setText(itemHist.altura)
            tvAGeneroDiag.setText(itemHist.genero)
            if (heViewModel.fstScan) {
                etObservacaoDiag.setText(itemHist.observacao)
            }
            else{
                heViewModel.heObservacao.observe(this, androidx.lifecycle.Observer { valor->
                    etObservacaoDiag.setText(valor)
                })
            }
            heViewModel.fstScan = false
        }).start()

    }

    private fun onClickSalvarRegistro(){
        val itemHist = HistoricoVO(
                id = idHistorico,
                data = tvDataDiag.text.toString(),
                hora = tvHoraDiag.text.toString(),
                peso = tvPesoDiag.text.toString(),
                altura = tvAlturaDiag.text.toString(),
                genero = tvAGeneroDiag.text.toString(),
                observacao = etObservacaoDiag.text.toString()
        )
        Thread(Runnable {
            HistoricoApplication.instance.helperDB?.updateRegistro(itemHist)
        }).start()
    }
}
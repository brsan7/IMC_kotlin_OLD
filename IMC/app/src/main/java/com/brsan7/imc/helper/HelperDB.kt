package com.brsan7.imc.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.brsan7.imc.model.HistoricoVO

class HelperDB (
        context: Context
): SQLiteOpenHelper(context, NOME_BANCO, null, VESAO_ATUAL) {

    companion object{
        private val NOME_BANCO = "historico.db"
        private val VESAO_ATUAL = 1

    }

    val TABLE_NAME = "historico"
    val COLUMNS_ID = "id"
    val COLUMNS_DATA = "data"
    val COLUMNS_HORA = "hora"
    val COLUMNS_PESO = "peso"
    val COLUMNS_ALTURA = "altura"
    val COLUMNS_GENERO = "genero"
    val COLUMNS_OBS = "observacao"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(" +
            "$COLUMNS_ID INTEGER NOT NULL," +
            "$COLUMNS_DATA TEXT NOT NULL," +
            "$COLUMNS_HORA TEXT NOT NULL," +
            "$COLUMNS_PESO TEXT NOT NULL," +
            "$COLUMNS_ALTURA TEXT NOT NULL," +
            "$COLUMNS_GENERO TEXT NOT NULL," +
            "$COLUMNS_OBS TEXT NOT NULL," +
            "" +
            "PRIMARY KEY($COLUMNS_ID AUTOINCREMENT)" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion){
            db?.execSQL(DROP_TABLE)
        }
        onCreate(db)
    }

    fun buscarRegistros(busca : String, isBuscaPorData : Boolean = false) : List<HistoricoVO>{

        val db = readableDatabase ?: return mutableListOf()
        var lista = mutableListOf<HistoricoVO>()
        val sql:String
        if(isBuscaPorData){
            sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_DATA LIKE '%$busca%'"
        }else{
            sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_ID LIKE '%$busca%'"
        }
        var cursor = db.rawQuery(sql, arrayOf())
        if (cursor == null){
            db.close()
            return mutableListOf()
        }
        while (cursor.moveToNext()){
            var itemHist = HistoricoVO(
                    cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMNS_DATA)),
                    cursor.getString(cursor.getColumnIndex(COLUMNS_HORA)),
                    cursor.getString(cursor.getColumnIndex(COLUMNS_PESO)),
                    cursor.getString(cursor.getColumnIndex(COLUMNS_ALTURA)),
                    cursor.getString(cursor.getColumnIndex(COLUMNS_GENERO)),
                    cursor.getString(cursor.getColumnIndex(COLUMNS_OBS))
            )
            lista.add(itemHist)
        }
        db.close()
        return lista
    }

    fun salvarRegistro(itemHist: HistoricoVO){
        val db: SQLiteDatabase = writableDatabase ?: return
        val sql = "INSERT INTO $TABLE_NAME " +
                "($COLUMNS_DATA,$COLUMNS_HORA,$COLUMNS_PESO,$COLUMNS_ALTURA,$COLUMNS_GENERO,$COLUMNS_OBS) " +
                "VALUES(?,?,?,?,?,?)"
        val argumento = arrayOf(itemHist.data, itemHist.hora, itemHist.peso, itemHist.altura, itemHist.genero, itemHist.observacao)
        db.execSQL(sql,argumento)
        db.close()
    }

    fun deletarRegistro(id:Int){
        val db = writableDatabase ?: return
        val sql = "DELETE FROM $TABLE_NAME WHERE $COLUMNS_ID = ?"
        val argumento = arrayOf("$id")
        db.execSQL(sql,argumento)
        db.close()
    }

    fun updateRegistro(itemHist: HistoricoVO){
        val db = writableDatabase ?: return
        val sql = "UPDATE $TABLE_NAME SET $COLUMNS_OBS = ? WHERE $COLUMNS_ID = ?"
        val argumento = arrayOf(itemHist.observacao,itemHist.id)
        db.execSQL(sql,argumento)
        db.close()
    }
}